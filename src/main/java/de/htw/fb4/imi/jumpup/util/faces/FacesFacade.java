/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.util.faces;

import javax.ejb.Stateless;
import javax.el.ExpressionFactory;
import javax.el.StandardELContext;
import javax.el.ValueExpression;
import javax.enterprise.inject.spi.BeanManager;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.inject.Inject;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.translate.Translatable;

/**
 * <p>JSF util</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 10.11.2014
 *
 */
@Stateless
public class FacesFacade
{
    @Inject
    BeanManager beanManager;
    
    @Inject
    protected static Translatable translator;
    
    /**
     * Main method to add error messages to be shown in JSF frontend.
     * @param message
     */
    public final void addErrorMessage(final String message)
    {
        FacesContext.getCurrentInstance().addMessage(null, newFacesMessage(FacesMessage.SEVERITY_ERROR, message));
    }

    /**
     * Add an info message that will be shown in JSF frontend.
     * @param message
     */
    public final void addInfoMessage(final String message)
    {
        FacesContext.getCurrentInstance().addMessage(null, newFacesMessage(FacesMessage.SEVERITY_INFO, message));
    }
    
    /**
     * Create a new JSF message. Make sure to do certain work like translations before the creation.
     * @param severity
     * @param message
     * @return
     */
    public FacesMessage newFacesMessage(final Severity severity,
            final String message)
    {
        String translatedMessage;
        
        if (null == translator) {
            Application.log(getClass() + ":newFacesMessage(): translator is null. Please check why it was not injected correctly. Continuing without translation...", 
                    LogType.CRITICAL, getClass());
            translatedMessage = message;            
        } else {
            translatedMessage = translator.translate(message);
        }
        
        return new FacesMessage(severity, translatedMessage, null);
    }

    /**
     * Create a new JSF validation error message. This is thrown by {@link Validator} instances on validation failures in general.
     * @param summary
     * @param detail
     * @return
     */
    public FacesMessage newValidationErrorMessage(final String summary,
            final String detail)
    {
        FacesMessage msg = newFacesMessage(FacesMessage.SEVERITY_ERROR, summary);        
        msg.setDetail(detail);
        
        return msg;
    }
    
    /**
     * Method to directly evaluate expression language tags, such as #{bean1.property1}
     * 
     * @param expression
     * @return
     */
    public String evaluateExpressionLanguage(final String expression)
    {
        ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
        StandardELContext elContext = new StandardELContext(expressionFactory);
        elContext.addELResolver(beanManager.getELResolver());        
        
        ValueExpression valueExpression = expressionFactory.createValueExpression(elContext, expression, expression.getClass());
        
        try {
            String evaluated = (String) valueExpression.getValue(elContext);
            
            return evaluated;  
        } catch (Exception e) {
            Application.log("Will not evaluate expression, error while evaluation EL expression '" + expression + "'.\nException: " 
                    + e.getMessage() + "\nStack trace:\n" + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            return "";
        }      
    }
    
}
