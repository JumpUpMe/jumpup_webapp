/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.mail.builder;

import java.io.File;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.config.IConfigKeys;
import de.htw.fb4.imi.jumpup.mail.MailModel;
import de.htw.fb4.imi.jumpup.mail.util.ConfigReader;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.util.TemplateHandler;
import de.htw.fb4.imi.jumpup.util.faces.FaceletRenderer;
import de.htw.fb4.imi.jumpup.util.faces.FacesFacade;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 28.11.2014
 *
 */
@Stateful
public class MailBuilderImpl implements MailBuilder
{
    @Inject
    protected FacesFacade facesFacade;
    
    protected FaceletRenderer faceletRenderer;
    
    protected TemplateHandler templateHandler;
    
    protected MailModel mailModel;
    
    @EJB(beanName = BeanNames.MAIL_CONFIG_READER)
    protected ConfigReader mailConfigReader;
    
    /**
     * Get template renderer.
     * @return
     */
    public TemplateHandler getTemplateHandler()
    {
        if (null == this.templateHandler) {
            this.templateHandler = new TemplateHandler(FacesContext.getCurrentInstance());                        
        }
        
        return this.templateHandler;
    }
    
    /**
     * Get facelet renderer.
     * @return
     */
    public FaceletRenderer getFaceletRenderer()
    {
        if (null == this.faceletRenderer) {
            this.faceletRenderer = new FaceletRenderer(FacesContext.getCurrentInstance());
        }
        
        return this.faceletRenderer;
    }
    
    protected MailModel getMailModel()
    {
        if (null == this.mailModel) {
            this.mailModel = new MailModel();
        }
        
        return this.mailModel;
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.mail.builder.MailBuilder#addPlainTextContent(java.io.File)
     */
    @Override
    public MailBuilder addPlainTextContent(File templateFile)
    {
        TemplateHandler handler = this.prepareTemplateHandler(templateFile);
        
        String plainContent = handler.evaluateTemplate();
        
        // set plain content in mail model
        this.getMailModel().setContentText(plainContent);
        
        return this;
    }

    private TemplateHandler prepareTemplateHandler(File templateFile)
    {
        TemplateHandler handler = this.getTemplateHandler();
        handler.setTemplate(templateFile);
        handler.setFacesFacade(this.facesFacade);
        return handler;
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.mail.builder.MailBuilder#addHtmlContent(java.io.File)
     */
    @Override
    public MailBuilder addHtmlContent(final String faceletPath)
    {
        FaceletRenderer renderer = this.getFaceletRenderer();
        
        try {
            String htmlContent = renderer.renderView(faceletPath);
            
            // set html content in mail model
            this.getMailModel().setContentHtml(htmlContent);
        } catch (IllegalStateException e) {
            // rendering failed
            this.getMailModel().setContentHtml(null);
            Application.log("addHtmlContent(): Could not render view " + faceletPath + " - will only send txt mail\nException: " + e.getMessage(), LogType.ERROR, getClass());
        }
        
        return this;
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.mail.builder.MailBuilder#getBuildedMailModel()
     */
    public MailModel getBuildedMailModel()
    {
        MailModel mailModel = this.getMailModel();
        
        // set default sender
        try {
            mailModel.setSender(new InternetAddress(this.mailConfigReader.fetchValue(IConfigKeys.JUMPUP_MAIL_DEFAULT_SENDER)));
        } catch (AddressException e) {
            Application.log("getBuildedMailModel(): the default sender mail as configured in mail.properties.local is malformed. Will not set the sender.\nException: " 
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
        }
        
        return this.getMailModel();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.mail.builder.MailBuilder#reset()
     */
    public void reset()
    {
       this.mailModel = null;        
    }

}
