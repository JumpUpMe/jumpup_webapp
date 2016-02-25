/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ejb.creation;

import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

/**
 * <p>Interface for all mails that can be sent on Trip operations.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 14.12.2015
 *
 */
public interface TripMails
{
    /**
     * Mail is sent to driver after the trip was created.
     * @param trip
     * @throws ApplicationUserException if the mail could not be sent
     */
    void sendTripAddedMailToDriver(Trip trip) throws ApplicationUserException;
    
    /**
     * Mail sent to driver after trip update.
     * @param trip
     * @throws ApplicationUserException
     */
    void sendTripUpdatedMailToDriver(Trip trip) throws ApplicationUserException;
    
    /**
     * Mail sent to driver after cancelation.
     * @param trip
     * @throws ApplicationUserException
     */
    void sendTripCanceledMailToDriver(Trip trip) throws ApplicationUserException;
}
