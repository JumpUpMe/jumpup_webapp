/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.mail.MailAdapter;
import de.htw.fb4.imi.jumpup.mail.MailModel;
import de.htw.fb4.imi.jumpup.mail.builder.MailBuilder;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.translate.Translatable;
import de.htw.fb4.imi.jumpup.trip.ajax.QueryResultFactory;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.trip.util.ConfigReader;
import de.htw.fb4.imi.jumpup.trip.util.TripAndBookingsConfigKeys;
import de.htw.fb4.imi.jumpup.user.Role;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;
import de.htw.fb4.imi.jumpup.util.FileUtil;

@Stateless(name = BeanNames.BOOKING_EJB)
public class BookingEJB implements BookingMethod
{
    @Inject
    private LoginSession loginSession;

    @Inject
    protected QueryResultFactory queryResultFactory;
    
    @Inject
    protected MailBuilder mailBuilder;
    
    @Inject
    protected MailAdapter mailAdapter;  
    
    @Inject
    protected Translatable translator;
    
    @EJB(beanName = BeanNames.TRIP_CONFIG_READER)
    protected ConfigReader tripConfigReader;
    
    @Inject
    protected BookingDAO bookingDAO;

    protected void reset()
    {
        this.mailBuilder.reset();
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.booking.BookingMethod#createBooking(de.htw.fb4.
     * imi.jumpup.booking.entities.Booking, long)
     */
    public void createBooking(Booking booking, Trip trip) throws ApplicationUserException
    {
        this.reset();

        // The trip cannot be booked anymore
        this.loadBookings(trip);
        
        if (!trip.canBeBooked(getCurrentUser())) {
            throw new ApplicationUserException("Can't book trip anymore.", "We are sorry to tell you that this trip can't be booked anymore. Please search for other trips in our big community!");
        }

        // Hash check to make sure that the request parameters were not
        // manipulated
        if (!this.checkBookingHash(booking, trip)) {
            String logMessage = "createBooking(): booking hash check showed that booking data was manipulated. User ID: "
                    + getCurrentUser().getIdentity()
                    + " Booking data: " + booking + " Trip: " + trip;
            
            Application
                    .log(logMessage,
                            LogType.CRITICAL, getClass());            
            throw new ApplicationUserException(logMessage, "The given booking data is invalid. Please return to the trip page and try again.");
        }

        this.completeBooking(booking, trip);
        this.persistBooking(booking);
    }

    private void loadBookings(Trip trip)
    {
        trip.setBookings(this.bookingDAO.getBookingsByTrip(trip)); 
     }

    private void persistBooking(Booking booking) throws ApplicationUserException
    {
        try {
            this.bookingDAO.save(booking);            
        } catch (Exception e) {
            Application.log("persistBooking: exception " + e.getMessage()
                    + "\nStack trace:\n" + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
            
           throw new ApplicationUserException("", "Could not save your booking. Please contact the customer care team.");
        }
    }

    private void completeBooking(Booking booking, Trip trip)
    {
        booking.setTrip(trip);
        booking.setPassenger(this.getCurrentUser());
        booking.setActorOnLastChange(Role.PASSENGER);
    }

    /**
     * Check whether the POSTed booking hash matches the one calculated by the
     * related {@link TripSearchCriteria}.
     * 
     * @param booking
     * @param trip
     * @return
     */
    private boolean checkBookingHash(Booking booking, Trip trip)
    {
        TripSearchCriteria reconstructedSearchCriteria = this.queryResultFactory
                .newTripSearchCriteriaBy(booking);

        if (!booking.getBookingHash().equals(
                reconstructedSearchCriteria.createBookingHash(trip))) {
            return false;
        }

        return true;
    }

    protected User getCurrentUser()
    {
        return loginSession.getCurrentUser();
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.htw.fb4.imi.jumpup.booking.BookingMethod#
     * sendBookingConfirmationToPassenger
     * (de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public void sendBookingCreationMailToPassenger(Booking booking) throws ApplicationUserException
    {
        try {
            this.reset();
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_BOOKING_CREATED_MAIL_PASSENGER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(booking.getPassenger().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your booking request was sent to the driver"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendBookingCreationMailToPassenger(): the recipient mail of the passenger is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            
            throw new ApplicationUserException("", "We could not send the booking mail.");
        } catch (Exception e) {
            Application.log("sendBookingCreationMailToPassenger(): error while sending booking creation mail to passenger.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking mail.");
        }
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.booking.BookingMethod#sendBookingInformationToDriver
     * (de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public void sendBookingInformationMailToDriver(Booking booking, User driver) throws ApplicationUserException
    {
        try {
            this.reset();
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_BOOKING_CREATED_MAIL_DRIVER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(driver.geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - You got one new booking request"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendBookingInformationMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking mail.");
        } catch (Exception e) {
            Application.log("sendBookingInformationMailToDriver(): error while sending booking creation mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking mail.");
        }
    }
    
    protected void buildTxtMail(String txtTemplateConfigKey)
    {
        File templateFile = new File(FileUtil.getPathToWebInfFolder(), this.tripConfigReader.fetchValue(txtTemplateConfigKey));
        this.mailBuilder.addPlainTextContent(templateFile);
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingMethod#confirmBooking(de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public void confirmBooking(Booking booking) throws ApplicationUserException
    {
        this.reset();
        try {
            if (this.currentUserIsNotDriver(booking)) {
                throw new ApplicationUserException("", "You are not the driver of this trip and therefore can't modify bookings.");
            }
            
            this.confirmBookingIfNotCanceledAndDoneYet(booking);
        } catch (Exception e) {
            if (e instanceof ApplicationUserException) {
                throw e;
            }
            
            Application.log("confirmBooking(): exception " + e.getMessage() + "\nStack trace:\n" + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "Error while trying to confirm the booking. Please try again");
        }        
    }

    private boolean currentUserIsNotDriver(Booking booking)
    {
        return this.getCurrentUser().getIdentity() != booking.getTrip().getDriver().getIdentity();
    }
    
    private boolean currentUserIsNotDriverAndPassenger(Booking booking)
    {
        return this.getCurrentUser().getIdentity() != booking.getTrip().getDriver().getIdentity()
             && this.getCurrentUser().getIdentity() != booking.getPassenger().getIdentity()  ;
    }

    private void confirmBookingIfNotCanceledAndDoneYet(Booking booking) throws ApplicationUserException
    {
        if (!booking.wasCancelled() && !booking.wasConfirmed()) {
            booking.setConfirmationDateTime(this.getCurrentTimestamp());
            booking.setActorOnLastChange(Role.DRIVER);
            bookingDAO.save(booking);
            return;
        }        
        
        // booking cannot be confirmed
        throw new ApplicationUserException("", "The booking cannot be confirmed because it was previously canceled or already booked.");
    }
    
    private Timestamp getCurrentTimestamp()
    {
        return new Timestamp(new Date().getTime());
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingMethod#sendBookingConfirmationMailToPassenger(de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public void sendBookingConfirmationMailToPassenger(Booking booking) throws ApplicationUserException
    {
        this.reset();
        try {            
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_BOOKING_CONFIRMED_MAIL_PASSENGER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(booking.getPassenger().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your booking was confirmed by the driver"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendBookingConfirmationMailToPassenger(): the recipient mail of the passenger is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking confirmation mail.");
        } catch (Exception e) {
            Application.log("sendBookingConfirmationMailToPassenger(): error while sending booking confirmation mail to the passenger.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking confirmation mail.");
        }        
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingMethod#cancelBooking(de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public void cancelBooking(Booking booking) throws ApplicationUserException
    {
        this.reset();
        try {
            if (this.currentUserIsNotDriverAndPassenger(booking)) {
                throw new ApplicationUserException("", "You are not the driver of this trip and therefore can't modify bookings.");
            }
            
            this.cancelBookingIfCanBeCancelled(booking);
        } catch (Exception e) {
            if (e instanceof ApplicationUserException) {
                throw e;
            }
            
            Application.log("cancelBooking(): exception " + e.getMessage() + "\nStack trace:\n" + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "Error while trying to cancel the booking. Please try again.");
        } 
    }

    private void cancelBookingIfCanBeCancelled(Booking booking) throws ApplicationUserException
    {
        if (!booking.wasCancelled()) {
             booking.setCancellationDateTime(this.getCurrentTimestamp());
             
             Role actor = Role.DRIVER;
             if (currentUserIsNotDriver(booking)) {
                 actor = Role.PASSENGER;
             }
             
             booking.setActorOnLastChange(actor);
             bookingDAO.save(booking);
                
             return;
        }

        throw new ApplicationUserException("", "The booking can't be cancelled. Did you already cancel it?");
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingMethod#sendBookingCancelationMailToPassenger(de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public void sendBookingCancelationMailToPassenger(Booking booking) throws ApplicationUserException
    {
        this.reset();
        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_BOOKING_CANCELED_MAIL_PASSENGER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(booking.getPassenger().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your booking was canceled by the driver"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendBookingCancelationMailToPassenger(): the recipient mail of the passenger is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());

            throw new ApplicationUserException("", "We could not send the booking cancelation mail.");
        } catch (Exception e) {
            Application.log("sendBookingCancelationMailToPassenger(): error while sending booking cancelation mail to the passenger.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking cancelation mail.");
        }                
    }

    @Override
    public void sendBookingCancelationMailToDriver(Booking booking) throws ApplicationUserException
    {
        this.reset();
        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_BOOKING_CANCELED_MAIL_DRIVER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(booking.getTrip().getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - A booking was canceled by the passenger"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendBookingCancelationMailToDriver(): the recipient mail of the passenger is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking cancelation mail.");
        } catch (Exception e) {
            Application.log("sendBookingCancelationMailToDriver(): error while sending booking cancelation mail to the passenger.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            throw new ApplicationUserException("", "We could not send the booking cancelation mail.");
        }                
        
    }

}
