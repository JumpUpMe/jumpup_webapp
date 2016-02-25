/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking;

import javax.ejb.Local;

import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.entity.User;

/**
 * <p>Business interface defining the core functionality to access our bookings.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 29.01.2015
 *
 */
@Local
public interface BookingMethod
{   
    /**
     * Prepare and persist the given booking.
     * 
     * Also trigger e-Mail notifcations.
     * @param booking
     * @param trip
     * @return
     */
    void createBooking(Booking booking, Trip trip) throws ApplicationUserException;
    
    /**
     * Send the booking confirmation mail to the passenger.
     * @param booking
     */
    void sendBookingCreationMailToPassenger(Booking booking) throws ApplicationUserException;
    
    /**
     * Send the booking information mail to the driver.
     * @param booking
     */
    void sendBookingInformationMailToDriver(Booking booking, User driver) throws ApplicationUserException;
    
    /**
     * Driver confirms the booking.
     * @param bookingId
     */
    void confirmBooking(Booking bookingId) throws ApplicationUserException;
    
    /**
     * Send booking confirmation mail to passenger.
     * @param booking
     */
    void sendBookingConfirmationMailToPassenger(Booking booking) throws ApplicationUserException;
    
    /**
     * Driver cancels a passenger's booking.
     * @param booking
     */
    void cancelBooking(Booking booking) throws ApplicationUserException;
    
    /**
     * Send booking cancellation mail to passenger.
     * @param booking
     */
    void sendBookingCancelationMailToPassenger(Booking booking) throws ApplicationUserException;

    /**
     * Send booking cancellation (done by passenger) to the driver.
     * @param booking
     */
    void sendBookingCancelationMailToDriver(Booking booking) throws ApplicationUserException;
}
