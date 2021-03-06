/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.validation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.settings.BeanNames;

/**
 * <p>Validator for overview path, a concatenated string of LatLng values that mark the driver's route.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 18.01.2015
 *
 */
@Named(value=BeanNames.OVERVIEW_PATH_VALIDATOR)
@RequestScoped
public class OverviewPath extends AbstractTripValidator
{

    @Override
    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.JumpUpValidator#validate(java.lang.Object)
     */
    public boolean validate(final Object value)
    {        
        // TODO implement validation
        
        return true;
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.validator.JumpUpValidator#getMinLength()
     */
    @Override
    public int getMinLength()
    {
        return 0;
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.validator.JumpUpValidator#getMaxLength()
     */
    @Override
    public int getMaxLength()
    {
        return 0;
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.validator.AbstractValidator#getDefaultFailureMessage()
     */
    protected String getDefaultFailureMessage()
    {
        return "Invalid overview path given. Did you plan your trip on the map?";
    }
}
