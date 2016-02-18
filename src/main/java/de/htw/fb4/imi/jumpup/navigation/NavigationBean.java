/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.navigation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.ApplicationProperties;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.user.controller.Login;
import de.htw.fb4.imi.jumpup.user.entity.User;

/**
 * <p>
 * </p>
 * 
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 28.11.2014
 * 
 */
@Named(value=BeanNames.NAVIGATION_BEAN)
@ApplicationScoped
public class NavigationBean implements NavigationOutcomes
{
    @Inject
    protected Login loginController;
    
    @Inject
    protected HttpServletRequest httpServletRequest;

    private static final String UTF_8 = "UTF-8";

    /**
     * Path to registration index.
     * 
     * @return
     */
    public static String toRegistration()
    {
        return "registration/";
    }
    
    public static String toRegistration(boolean outcome) {
        if (outcome) {
            return TO_REGISTRATION;
        }
        
        return toRegistration();
    }

    /**
     * Return the path to the webapp.
     * 
     * @return
     */
    public static String pathToApp()
    {
        return ApplicationProperties.getWebAppUrl().toString();
    }

    public String pathToAppFallback()
    {
        if (null != FacesContext.getCurrentInstance()) {
            return pathToApp();
        }

        if (null == loginController) {
            throw new IllegalStateException(
                    "pathToAppFallback(): can't determine path to web app in current context!");
        }

        return loginController.getPathToApp();
    }

    /**
     * Redirect to login page.
     * 
     * @return
     */
    public static String redirectToLogin()
    {
        return "index.xhtml?faces-redirect=true";
    }

    /**
     * 
     * @return
     */
    public String toUserProfileEdit()
    {
        return TO_USER_PROFILE;
    }

    public String toAddTrip()
    {
        return TO_ADD_TRIP;
    }

    public String toEditTrip(Integer tripId)
    {
        if (null == tripId) {
            return toAddTrip();
        }

        return pathToApp() + "/portal/trip/create.xhtml" + "?trip=" + tripId;
    }

    public String toCancelTrip(Integer tripId)
    {
        if (null == tripId) {
            return toAddTrip();
        }

        return pathToApp() + "/portal/trip/cancel.xhtml" + "?trip=" + tripId;
    }

    public String toViewBooking(Long bookingId)
    {
        return pathToApp() + "/portal/booking/view.xhtml" + "?booking="
                + bookingId;
    }

    public String toConfirmBooking(Long bookingId)
    {
        return toViewBooking(bookingId) + "&a=" + "confirm";
    }

    public String toDeclineBooking(Long bookingId)
    {
        return toViewBooking(bookingId) + "&a=" + "decline";
    }

    public String toListOfferedTrips(boolean isOutCome)
    {
        if (isOutCome) {
            return toListOfferedTrips();
        }

        return pathToApp() + "/portal/trip/list_offered.xhtml";
    }

    public String toListOfferedTrips()
    {
        return TO_LIST_OFFERED_TRIPS;
    }

    public String toLookForTrips()
    {
        return TO_LOOK_FOR_TRIPS;
    }

    public String toLookForTrips(boolean isOutCome)
    {
        if (isOutCome) {
            return toLookForTrips();
        }

        return pathToApp() + "/portal/trip/look_for_trips.xhtml";
    }

    public String toAddBooking()
    {
        return pathToAppFallback() + "/portal/trip/booking.xhtml";
    }

    public String toViewProfile()
    {
        return pathToAppFallback() + "/portal/profile/personal_show.xhtml";
    }

    public String toListBookings()
    {
        return TO_LIST_BOOKINGS;
    }

    public String toEditProfile(boolean prependWebAppPath)
    {
        String path = "";
        if (prependWebAppPath) {
            path += pathToAppFallback() + "/";
        }

        return path + "portal/profile/personal.xhtml";
    }

    /**
     * Generate the URL to the booking facelet by the given trip.
     * 
     * @param trip
     * @return
     */
    public String generateAddBookingUrl(TripSearchCriteria tripSearch, Trip trip)
    {
        StringBuilder urlBuilder = new StringBuilder();

        try {
            appendUrlEncoded(urlBuilder, "t", toString(trip.getIdentity()), '?');
            appendUrlEncoded(urlBuilder, "s", tripSearch.getStartPoint(), '&');
            appendUrlEncoded(urlBuilder, "e", tripSearch.getEndPoint(), '&');
            appendUrlEncoded(urlBuilder, "sL",
                    toString(tripSearch.getLatStartPoint()), '&');
            appendUrlEncoded(urlBuilder, "sLo",
                    toString(tripSearch.getLongStartPoint()), '&');
            appendUrlEncoded(urlBuilder, "eL",
                    toString(tripSearch.getLatEndPoint()), '&');
            appendUrlEncoded(urlBuilder, "eLo",
                    toString(tripSearch.getLongEndPoint()), '&');
            appendUrlEncoded(urlBuilder, "h",
                    tripSearch.createBookingHash(trip), '&');
        } catch (UnsupportedEncodingException e) {
            Application.log(
                    "generateAddBookingUrl: cannot generate booking URL due to exception "
                            + e.getMessage() + "\nstack:"
                            + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
        }

        return toAddBooking() + urlBuilder.toString();
    }

    private String toString(float value)
    {
        return Float.toString(value);
    }
    
    private String toString(double value)
    {
        return Double.toString(value);
    }

    private String toString(long value)
    {
        return Long.toString(value);
    }

    private void appendUrlEncoded(StringBuilder urlBuilder, String key,
            String value, char delimiter) throws UnsupportedEncodingException
    {
        urlBuilder.append(delimiter + key + "="
                + URLEncoder.encode(value, UTF_8));
    }

    public String generateDriverUrl(TripSearchCriteria tripSearchCriteria,
            User driver)
    {
        StringBuilder urlBuilder = new StringBuilder();

        try {
            appendUrlEncoded(urlBuilder, "u", toString(driver.getIdentity()),
                    '?');
        } catch (UnsupportedEncodingException e) {
            Application.log(
                    "generateDriverUrl: cannot generate driver URL due to exception "
                            + e.getMessage() + "\nstack:"
                            + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
        }

        return toViewProfile() + urlBuilder.toString();
    }
}
