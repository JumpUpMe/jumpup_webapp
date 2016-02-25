/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking;

import java.util.List;

import javax.ejb.Local;

import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.entity.User;

/**
 * <p>Data access object class for performing {@link Booking} database operations.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 30.01.2015
 *
 */
@Local
public interface BookingDAO
{
    /**
     * Query booking by the given ID.
     * @param bookingId
     * @return
     */
    Booking queryById(long bookingId);
    
    List<Booking> getBookingsByTrip(Trip trip);

    /**
     * Save the given {@link Booking} entity and flush into database.
     * @param booking
     * 
     * @return entity ID
     */
    long save(Booking booking);

    /**
     * Get all bookings done by the given passenger.
     * @param currentUser
     * @return
     */
    List<Booking> getBookingsByPassenger(User currentUser);

    /**
     * Get bookings by the given trip ID.
     * @param entityId
     */
    List<Booking> getBookingsByTrip(Long entityId);
}
