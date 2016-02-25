/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.controller;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.controller.AbstractFacesController;
import de.htw.fb4.imi.jumpup.navigation.NavigationOutcomes;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.ejb.TripDAO;
import de.htw.fb4.imi.jumpup.trip.ejb.TripRequest;
import de.htw.fb4.imi.jumpup.trip.ejb.creation.TripManagementMethod;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.trip.util.ConfigReader;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 12.01.2015
 *
 */
@Named(value = BeanNames.TRIP_CREATION_CONTROLLER)
@RequestScoped
public class TripCreation extends AbstractFacesController
{
    @Inject
    protected TripManagementMethod tripManagementMethod;
    protected Trip trip;

    @Inject
    protected LoginSession loginSession;

    @EJB(beanName = BeanNames.TRIP_CONFIG_READER)
    protected ConfigReader tripConfigReader;

    @Inject
    protected TripDAO tripDAO;
    
    @Inject
    protected TripRequest tripRequest;

    /**
     * Trip ID (used in edit mode only).
     */
    protected Long tripId;

    private TripManagementMethod getTripManagementMethod()
    {
        return this.tripManagementMethod;
    }

    /**
     * @return the tripConfigReader
     */
    public ConfigReader getTripConfigReader()
    {
        return tripConfigReader;
    }

    /**
     * @param tripConfigReader
     *            the tripConfigReader to set
     */
    public void setTripConfigReader(ConfigReader tripConfigReader)
    {
        this.tripConfigReader = tripConfigReader;
    }

    /**
     * Get {@link Trip}.
     * 
     * Load from DB if the tripID was set, or create new.
     * 
     * @return
     */
    public Trip getTrip()
    {
        if (null != this.tripId && null == this.trip) {
            try {
                // Load from DB
                Application.log("TripCreation: tripId " + tripId + "given, trying to load from DB...", LogType.DEBUG,
                        getClass());
                this.trip = this.tripDAO.getTripByID(this.tripId);

                // ensure that the trip belongs to the logged-in customer
                if (this.tripDoesNotBelongToLoggedInCustomer(trip)) {
                    Application.log("TripCreation: the customer " + loginSession.getCurrentUser().getIdentity()
                            + " tried to manipulate a trip that doesn't belong to him. Trip ID: " + trip.getIdentity(),
                            LogType.CRITICAL, getClass());
                    this.trip = null;
                }
            } catch (EJBException e) {
                // not found - switch to Add mode
                this.trip = null;
            }
        }

        if (null == this.trip) {
            Application.log("TripCreation: No tripId given, creating new...", LogType.DEBUG, getClass());
            this.trip = new Trip();
        }

        // set in trip request singleton also
        this.tripRequest.setTrip(this.trip);
        
        return this.trip;
    }

    private boolean tripDoesNotBelongToLoggedInCustomer(Trip trip)
    {
        return !(loginSession.getCurrentUser().getIdentity() == trip.getDriver().getIdentity());
    }

    /**
     * @return the tripId
     */
    public Long getTripId()
    {
        return tripId;
    }

    /**
     * @param tripId
     *            the tripId to set
     */
    public void setTripId(Long tripId)
    {
        this.tripId = tripId;
    }

    /**
     * Action for adding a given trip.
     * 
     * @return
     */
    public String addTrip()
    {
        try {
            Application.log("TripCreation: try to add trip", LogType.DEBUG, getClass());
            TripManagementMethod tripManagementMethod = getTripManagementMethod();
            Trip trip = getTrip();

            try {
                tripManagementMethod.addTrip(trip);
                addDisplayInfoMessage("Your trip was added.");
                
                try {
                    tripManagementMethod.sendTripAddedMailToDriver(trip);
                } catch (ApplicationUserException e) {
                    Application.log("TripCreationController: addTrip() " + e.getMessage(), LogType.ERROR, getClass());
                    this.addDisplayErrorMessage(
                            "We couldn't send a confirmation mail. However, you can look up your trips on page 'Show my trips'.");
                }
            } catch (ApplicationUserException creationException) {
                Application.log("TripCreationController: addTrip() " + creationException.getMessage(), LogType.ERROR,
                        getClass());
                this.addDisplayErrorMessage(creationException.getUserMsg());
            }           
        } catch (Exception e) {
            Application.log("TripCreationController: " + e.getMessage(), LogType.ERROR, getClass());
            this.addDisplayErrorMessage("Could not add you trip. Please contact our customer care team.");
        }

        return NavigationOutcomes.TO_ADD_TRIP;
    }

    /**
     * Action for editing a trip, only if the trip ID is given.
     * 
     * @return
     */
    public String editTrip()
    {
        // no trip ID given
        if (null == this.getTripId()) {
            return NavigationOutcomes.TO_LIST_OFFERED_TRIPS;
        }

        try {
            Application.log("TripCreation: try to edit trip", LogType.DEBUG, getClass());
            // we have to set the trip ID again, because it is lost for some
            // reason
            this.getTrip().setIdentity(this.getTripId());

            TripManagementMethod tripManagementMethod = getTripManagementMethod();
            
            try {
                tripManagementMethod.changeTrip(getTrip());
                addDisplayInfoMessage("Your trip was edited.");
                
                try {
                    tripManagementMethod.sendTripUpdatedMailToDriver(trip);
                } catch (ApplicationUserException e) {
                    Application.log("TripCreationController: editTrip() " + e.getMessage(), LogType.ERROR, getClass());
                    this.addDisplayErrorMessage(
                            "We couldn't send a confirmation mail. However, you can look up your trips on page 'Show my trips'.");
                }
            } catch (ApplicationUserException changeTripException) {
                Application.log("TripCreationController: editTrip() " + changeTripException.getMessage(), LogType.ERROR,
                        getClass());
                this.addDisplayErrorMessage(changeTripException.getUserMsg());
            }
        } catch (Exception e) {
            Application.log("TripCreationController: " + e.getMessage(), LogType.ERROR, getClass());
            this.addDisplayErrorMessage("Could not add your trip. Please contact our customer care team.");
        }

        return NavigationOutcomes.TO_EDIT_TRIP;
    }

    /**
     * Action for trip cancelation (soft-deletion), only if the trip ID is
     * given.
     * 
     * @return
     */
    public String cancelTrip()
    {
        // no trip ID given
        if (null == this.getTripId()) {
            return NavigationOutcomes.TO_LIST_OFFERED_TRIPS;
        }

        try {
            Application.log("TripCreation: try to cancel trip", LogType.DEBUG, getClass());
            // we have to set the trip ID again, because it is lost for some
            // reason
            this.getTrip().setIdentity(this.getTripId());

            TripManagementMethod tripManagementMethod = getTripManagementMethod();
            
            try {
                tripManagementMethod.cancelTrip(getTrip());
                
                addDisplayInfoMessage("Your trip was cancelled.");
                
                try {
                    tripManagementMethod.sendTripCanceledMailToDriver(trip);
                } catch (ApplicationUserException e) {
                    Application.log("TripCreationController: cancelTrip() " + e.getMessage(), LogType.ERROR, getClass());
                    this.addDisplayErrorMessage(
                            "We couldn't send a confirmation mail. However, you can look up your trips on page 'Show my trips'.");
                }
            } catch (ApplicationUserException deletionException) {
                Application.log("TripCreationController: cancelTrip() " + deletionException.getMessage(), LogType.ERROR,
                        getClass());
                this.addDisplayErrorMessage(deletionException.getUserMsg());
            }      
        } catch (Exception e) {
            Application.log("UserDetailsController: " + e.getMessage(), LogType.ERROR, getClass());
            this.addDisplayErrorMessage("Could not cancel your trip. Please contact our customer care team.");
        }

        return NavigationOutcomes.TO_LIST_OFFERED_TRIPS;
    }

}
