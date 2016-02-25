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
@Named(value=BeanNames.PRICE_VALIDATOR)
@RequestScoped
public class PriceValidator extends AbstractTripValidator
{
    private static final String PATTERN_PRICE = "^[0-9]+.?[0-9]{0,2}$";

    @Override
    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.JumpUpValidator#validate(java.lang.Object)
     */
    public boolean validate(final Object value)
    {
        final Float price = castToFloat(value);
        
        if (null == price || !this.checkRange(price)) {
                return false;
            }
        
        return true;
    }
    
    private Float castToFloat(Object value)
    {
        if (value instanceof String) {
            try {
                return Float.parseFloat((String) value);
            } catch (NumberFormatException e) {
                errorMessages.add("Please type in a valid price as decimal.");
                return null;
            }
        } else if (value instanceof Float) {
            return (Float) value;
        } else {
            errorMessages.add("Please type in a valid price as decimal.");
            return null;
        }
    }

    private boolean checkRange(Float price)
    {
        Float minPrice = getMinPrice(); 
        Float maxPrice = getMaxPrice(); 
         
        if (price >= minPrice && price <= maxPrice) {
            return true;
        }
        
        return false;
    }

    public Float getMaxPrice()
    {
        Float maxPrice = Float.parseFloat(this.tripConfigReader.fetchValue(TripAndBookingsConfigKeys.JUMPUP_TRIP_VALIDATION_PRICE_MAX_VALUE_IN_EURO));
        return maxPrice;
    }

    public Float getMinPrice()
    {
        Float minPrice = Float.parseFloat(this.tripConfigReader.fetchValue(TripAndBookingsConfigKeys.JUMPUP_TRIP_VALIDATION_PRICE_MIN_VALUE_IN_EURO));
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
        return "Please enter a price between " + this.getMinPrice() + " and " + this.getMaxPrice() + " euros.";
    }
    
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.validator.AbstractValidator#getFrontendValidationPattern()
     */
    public String getFrontendValidationPattern()
    {
        return PATTERN_PRICE;
    }
}
