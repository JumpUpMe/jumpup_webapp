/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ejb.creation;

import javax.ejb.Local;

import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.entity.User;

/**
 * <p>Business interface to be used within the creation of {@link Trip} entities.</p>
 * 
 * <p>Implementations allow to persist trips that are offered by {@link User} entities.<br /
 * It works as DAO for creating and updating trips. 
 * </p>
 * 
 * <p>It doesn't handle the search for trips.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 12.01.2015
 *
 */
@Local
public interface TripManagementMethod extends TripMails
{
    
    /**
     * Add / persist the given {@link Trip} and do further related work.
     * @param trip
     * @throws ApplicationUserException
     */
    void addTrip(final Trip trip) throws ApplicationUserException;
    
    /**
     * Change / update the given {@link Trip}. Inform passengers about changes.
     * @param trip
     * @throws ApplicationUserException
     */
    void changeTrip(final Trip trip) throws ApplicationUserException;
    
    /**
     * Cancel/Delete the given {@link Trip}. Make sure to inform the passengers about the cancellation.
     * @param trip
     * @throws ApplicationUserException
     */
    void cancelTrip(final Trip trip) throws ApplicationUserException;
}
