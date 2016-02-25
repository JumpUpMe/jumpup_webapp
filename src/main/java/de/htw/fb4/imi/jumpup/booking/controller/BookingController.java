package de.htw.fb4.imi.jumpup.booking.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.booking.BookingDAO;
import de.htw.fb4.imi.jumpup.booking.BookingMethod;
import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.controller.AbstractFacesController;
import de.htw.fb4.imi.jumpup.navigation.NavigationBean;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.ejb.TripDAO;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.Role;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;
import de.htw.fb4.imi.jumpup.util.model.Gender;

/**
 * 
 * <p>
 * Controller classes for bookings.
 * </p>
 * 
 * @author <a href="mailto:m_seidler@hotmail.de">Marco Seidler</a>
 * @since 29.01.2015
 * 
 */

@Named(value = BeanNames.BOOKING_CONTROLLER)
@SessionScoped
public class BookingController extends AbstractFacesController implements Serializable
{

    /**
     * 
     */
    private static final long serialVersionUID = -4989295237379145060L;

    @Inject
    private BookingMethod bookingEJB;

    @Inject
    private TripDAO tripDAO;

    @Inject
    private BookingDAO bookingDAO;

    @Inject
    private LoginSession loginSession;

    @Inject
    protected NavigationBean navigationBean;

    @Inject
    protected BookingListController bookingListController;

    private Long tripId;

    private Long bookingId;

    protected Trip trip;

    protected String action;

    private Booking booking;

    protected Role roleDriver = Role.DRIVER;

    protected Role rolePassenger = Role.PASSENGER;

    public String getIconUrl()
    {
        if (null == this.booking || null == this.booking.getPassenger()
                || null == this.booking.getPassenger().getUserDetails()
                || null == this.booking.getPassenger().getUserDetails().getGender()
                || this.booking.getPassenger().getUserDetails().getGender().equals(Gender.MAN)) {
            return navigationBean.pathToAppFallback() + "/resources/img/icons/male.png";
        } else if (this.booking.getPassenger().getUserDetails().getGender().equals(Gender.LADYBOY)) {
            return navigationBean.pathToAppFallback() + "/resources/img/icons/ladyboy.png";
        }

        return navigationBean.pathToAppFallback() + "/resources/img/icons/female.png";
    }

    public void setIconUrl(String iconUrl)
    {

    }

    /**
     * @return the roleDriver
     */
    public Role getRoleDriver()
    {
        return roleDriver;
    }

    /**
     * @return the rolePassenger
     */
    public Role getRolePassenger()
    {
        return rolePassenger;
    }

    public void bookingMustExist()
    {
        if (null == this.getBooking()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(navigationBean.toListOfferedTrips(false));
            } catch (IOException e) {
                Application.log(
                        "bookingMustExist: " + e.getMessage() + "\n Stack: " + ExceptionUtils.getFullStackTrace(e),
                        LogType.ERROR, getClass());
            }
        }
    }

    public void tripMustExist()
    {
        if (null == this.getTrip()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(navigationBean.toLookForTrips(false));
            } catch (IOException e) {
                Application.log(
                        "bookingMustExist: " + e.getMessage() + "\n Stack: " + ExceptionUtils.getFullStackTrace(e),
                        LogType.ERROR, getClass());
            }
        }
    }

    public void currentUserMustBeDriverOrPassenger()
    {
        if (!currentUserIsDriver() && !currentUserIsPassenger()) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect(navigationBean.toListOfferedTrips(false));
            } catch (IOException e) {
                Application.log("currentUserMustBeDriver: " + e.getMessage() + "\n Stack: "
                        + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            }
        }
    }

    protected boolean currentUserIsPassenger()
    {
        Booking booking = getBooking();

        long currentId = this.loginSession.getCurrentUser().getIdentity();
        return currentId == booking.getPassenger().getIdentity();
    }

    protected boolean currentUserIsDriver()
    {
        getBooking();

        long currentId = this.loginSession.getCurrentUser().getIdentity();

        return currentId == this.getTrip().getDriver().getIdentity();
    }

    /**
     * @return the action
     */
    public String getAction()
    {
        return action;
    }

    /**
     * @param action
     *            the action to set
     */
    public void setAction(String action)
    {
        this.action = action;
    }

    /**
     * @return the bookingId
     */
    public Long getBookingId()
    {
        return bookingId;
    }

    /**
     * @param bookingId
     *            the bookingId to set
     */
    public void setBookingId(Long bookingId)
    {
        this.bookingId = bookingId;
    }

    /**
     * Bind-booking-data action.
     * 
     * @return
     */
    public String bindBookingData()
    {
        try {
            bookingEJB.createBooking(this.getBooking(), this.getTrip());

            // refresh passenger's booking list
            this.bookingListController.refresh();

            // try to send mail
            try {
                bookingEJB.sendBookingCreationMailToPassenger(this.getBooking());
            } catch (Exception e) {
                addDisplayErrorMessage(
                        "Your booking was successful, but we couldn't send a confirmation mail. Please refer to your 'My bookings' page.");
                return null;
            }

            try {
                bookingEJB.sendBookingInformationMailToDriver(this.getBooking(), this.getTrip().getDriver());
            } catch (Exception e) {
                Application.log("bindBookingData(): " + e.getMessage() + "\n" + ExceptionUtils.getFullStackTrace(e),
                        LogType.ERROR, getClass());
            }

            // no error, redirect to booking page
            addDisplayInfoMessage(
                    "Your booking was successful! You will recieve an eMail with further information and are able to view the details in your booking overview.");
        } catch (ApplicationUserException e) {
            this.addDisplayErrorMessage(e.getUserMsg());

            return null;
        }

        return null;

    }

    /**
     * Confirm booking action.
     * 
     * @return
     */
    public String confirm()
    {
        try {
            bookingEJB.confirmBooking(this.getBooking());

            // try to send mail
            try {
                bookingEJB.sendBookingConfirmationMailToPassenger(this.getBooking());
            } catch (Exception e) {
                addDisplayErrorMessage(
                        "The booking was confirmed, but we couldn't send an eMail to the passenger. Please refer to your 'My bookings' page.");
                return null;
            }

            // no error, redirect to booking page
            addDisplayInfoMessage("The booking was confirmed successfully!");
        } catch (ApplicationUserException e) {
            this.addDisplayErrorMessage(e.getUserMsg());
            return null;
        }

        return NavigationBean.TO_LIST_OFFERED_TRIPS;
    }

    /**
     * Cancel booking action.
     * 
     * @return
     */
    public String cancel()
    {
        try {
            bookingEJB.cancelBooking(this.getBooking());

            // try to send mail
            try {
                if (currentUserIsDriver()) {
                    bookingEJB.sendBookingCancelationMailToPassenger(this.getBooking());
                } else if (currentUserIsPassenger()) {
                    bookingEJB.sendBookingCancelationMailToDriver(this.getBooking());

                    // refresh booking list of passenger
                    this.bookingListController.refresh();
                }
            } catch (Exception e) {
                addDisplayErrorMessage(
                        "The booking was cancelled, but we couldn't send an eMail to the passenger. Please refer to your 'My bookings' page.");
                return null;
            }

            // no error, redirect to booking page
            addDisplayInfoMessage("The booking was cancelled successfully!");
        } catch (ApplicationUserException e) {
            this.addDisplayErrorMessage(e.getUserMsg());

            return null;
        }

        return NavigationBean.TO_LIST_OFFERED_TRIPS;

    }

    public Booking getBooking()
    {
        if (null == this.booking) {
            // try to load by ID
            if (null != this.bookingId) {
                try {
                    this.booking = this.bookingDAO.queryById(this.bookingId);
                    this.tripId = this.booking.getTrip().getIdentity();
                } catch (EJBException e) {
                    this.addDisplayErrorMessage("Could not find booking.");
                    this.booking = null;
                }
            } else {
                this.booking = new Booking();
            }
        }

        return booking;
    }

    public void setBooking(Booking booking)
    {
        this.booking = booking;
    }

    public Long getTripId()
    {
        return tripId;
    }

    public Trip getTrip()
    {
        if (null == this.trip) {
            try {
                Application.log("TripId: " + this.getTripId(), LogType.DEBUG, getClass());
                this.trip = this.tripDAO.getTripByID(this.getTripId());
            } catch (EJBException e) {
                this.addDisplayErrorMessage("Could not find any matching trip.");
                this.trip = null;
            }
        }

        return this.trip;
    }

    public void setTripId(Long tripId)
    {
        Application.log("Setting tripID " + tripId, LogType.DEBUG, getClass());
        this.tripId = tripId;
    }

}
