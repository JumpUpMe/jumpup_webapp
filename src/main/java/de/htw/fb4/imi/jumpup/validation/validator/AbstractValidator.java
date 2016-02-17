/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.validation.validator;

import java.util.HashSet;
import java.util.Set;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.util.faces.FacesFacade;

/**
 * <p>Abstract class for JSF validation classes.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 24.11.2014
 *
 */
public abstract class AbstractValidator implements Validator, JumpUpValidator
{
    @PersistenceContext(unitName=PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManager entityManager;
    
    @PersistenceUnit(unitName=PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManagerFactory emFactory;
    
    @Inject
    protected FacesFacade facesFacade;
    
    protected Set<String> errorMessages = new HashSet<>();
    

    @Override    
    /* (non-Javadoc)
     * @see javax.faces.validator.Validator#validate(javax.faces.context.FacesContext, javax.faces.component.UIComponent, java.lang.Object)
     */
    public void validate(final FacesContext context, final UIComponent component,
            final Object value) throws ValidatorException
    {
        Application.log(getClass() + " validating users input",
                LogType.DEBUG, getClass());
       
        // throw validator with invalid entry message per default if validate() returns false
        if (!this.validate(value)) {
            // get first error message or print default
            String msg = this.getDefaultFailureMessage();
            if (this.errorMessages.size() > 0) {
                msg = (String) this.errorMessages.toArray()[0];
            }
            
            Application.log(getClass() + " validation failed",
                    LogType.DEBUG, getClass());
            
            throw new ValidatorException(this.facesFacade.newValidationErrorMessage(msg, 
                    msg));
        }
    }
 
    /**
     * Define the default validation failure message to be shown.
     * @return
     */
    protected abstract String getDefaultFailureMessage();

    @Override
    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.JumpUpValidator#validate(java.lang.Object)
     */
    public boolean validate(final Object value)
    {
        return false;
    }

    /**
     * 
     * @param value
     * @return
     */
    protected boolean checkLength(String value)
    {        
        if (value.length() < this.getMinLength() || value.length() > this.getMaxLength()) {
            return false;
        }
        
        return true;
    }    
    
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.validator.JumpUpValidator#getFrontendValidationPattern()
     */
    public String getFrontendValidationPattern()
    {
        // the default validation pattern is the minimum and maximum string length.
        return ".{" + this.getMinLength() + "," + this.getMaxLength() + "}";
    }
    
    @Override
    public Set<String> getErrorMessages()
    {
        return errorMessages;
    }
}
