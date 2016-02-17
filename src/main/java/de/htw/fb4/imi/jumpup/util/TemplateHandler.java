/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.context.FacesContext;

import de.htw.fb4.imi.jumpup.ApplicationError;
import de.htw.fb4.imi.jumpup.util.faces.FacesFacade;

/**
 * <p>This class handles any template file, such as *.txt, scans for expression language tags and evaluates them.</p>
 * 
 * <p>It returns the template with evaluated expression language tags.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 28.11.2014
 *
 */
public class TemplateHandler
{
    protected static final String PATTERN_EXPRESSION_LANGUAGE = "(#\\{.*?\\})";
    
    protected FacesContext facesContext;
    
    protected String template;
    
    protected FacesFacade facesFacade;   
    
   
    public TemplateHandler(FacesContext facesContext)
    {
        this.facesContext = facesContext;
    }
    
    /**
     * Set the template file.
     * @param templateFile
     */
    public void setTemplate(File templateFile)
    {
        if (null == templateFile || !templateFile.exists()) {
            throw new IllegalArgumentException("setTemplate(): Illegal argument for templateFile. Either it is null or doesn't exist: " 
              + (templateFile != null ? templateFile.getAbsolutePath() : " null"));
        }
        
        // read complete file
        String template = FileUtil.readFile(templateFile, "UTF-8");
        
        if (null == template) 
        {
            throw new IllegalArgumentException("setTemplate(): The given file isn't valid: " + templateFile);
        }
        
        this.template = template;
    }
    
    /**
     * @param facesFacade the facesFacade to set
     */
    public void setFacesFacade(FacesFacade facesFacade)
    {
        this.facesFacade = facesFacade;
    }

    
    /**
     * Start the evaluation.
     * Scan for expression language tags and replace them by the evaluated language tags.
     * 
     * For example: replace #{someBean.propertyA} by the value of the propertyA.
     * @return
     * @throws ApplicationError
     */
    public String evaluateTemplate()
    {
        if (null == facesFacade) {
            throw new ApplicationError("evaluateTemplate(): facesFacade is null", getClass());
        }
        
        if (null == template) {
            throw new ApplicationError("evaluateTemplate(): template is null", getClass());
        }
        
        
        Pattern pExpressionLanguage = Pattern.compile(PATTERN_EXPRESSION_LANGUAGE);
        
        Matcher mExpressionLanguage = pExpressionLanguage.matcher(this.template);
        
        while (mExpressionLanguage.find()) {
            String matchedEl = mExpressionLanguage.group();
            
            // evaluate EL expression
            String evaluated = this.facesFacade.evaluateExpressionLanguage(matchedEl);
            
            // replace matched sequence in the template by evaluated expression
            this.template = this.template.replaceAll(Pattern.quote(matchedEl), evaluated);
        }
        
    
        return this.template;
    }
    

}
