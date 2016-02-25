/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.validation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.trip.util.TripAndBookingsConfigKeys;

/**
 * <p>Validator for prices configured on {@link Trip}</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 18.01.2015
 *
 */
@Named(value=BeanNames.NUMBER_SEATS_VALIDATOR)
@RequestScoped
public class NumberSeatsValidator extends AbstractTripValidator
{
    @Override
    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.JumpUpValidator#validate(java.lang.Object)
     */
    public boolean validate(final Object value)
    {
        final Integer numberSeats = castToInt(value);
        
        if (null == numberSeats || !this.checkRange(numberSeats)) {
                return false;
            }
        
        return true;
    }
    
    private Integer castToInt(Object value)
    {
        if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                errorMessages.add("Please type in valid number of seats as decimal.");
                return null;
            }
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else {
            errorMessages.add("Please type in a valid number of seats as decimal.");
            return null;
        }
    }

    private boolean checkRange(Integer numberSeats)
    {
        Float minNumberSeats = getMinNumberOfSeats(); 
        Float maxNumberSeats = getMaxNumberOfSeats(); 
         
        if (numberSeats >= minNumberSeats && numberSeats <= maxNumberSeats) {
            return true;
        }
        
        return false;
    }

    public Float getMaxNumberOfSeats()
    {
        Float maxPrice = Float.parseFloat(this.tripConfigReader.fetchValue(TripAndBookingsConfigKeys.JUMPUP_TRIP_VALIDATION_NUMBER_SEATS_MAX_VALUE));
        return maxPrice;
    }

    public Float getMinNumberOfSeats()
    {
        Float minPrice = Float.parseFloat(this.tripConfigReader.fetchValue(TripAndBookingsConfigKeys.JUMPUP_TRIP_VALIDATION_NUMBER_SEATS_MIN_VALUE));
        return minPrice;
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
        return "Please enter number of seats between " + this.getMinNumberOfSeats() + " and " + this.getMaxNumberOfSeats() + " euros.";
    }
}
