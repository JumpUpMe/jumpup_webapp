/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ejb;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 14.12.2015
 *
 */
@Named(value = BeanNames.TRIP_REQUEST)
@RequestScoped
public class TripRequest
{
    
    private Trip trip;
    
    public void setTrip(Trip trip)
    {
        this.trip = trip;
    }
    
    public Trip getTrip()
    {
        return trip;
    }
}
