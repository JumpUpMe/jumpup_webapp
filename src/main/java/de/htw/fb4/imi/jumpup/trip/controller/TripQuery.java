/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.controller;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.controller.AbstractFacesController;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.ejb.TripDAO;
import de.htw.fb4.imi.jumpup.trip.ejb.query.TripQueryMethod;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.Role;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;

/**
 * <p>This controller is responsible for general queries on trips, such as:</p>
 * <ul>
 *  <li>Fetching trips that a driver offers</li>
 *  <li>Fetching trips that a passenger booked</li>
 *  <li>A passenger searchs for trips matching special criteria</li>
 * </ul>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 19.01.2015
 *
 */
@Named(value = BeanNames.TRIP_QUERY_CONTROLLER)
@RequestScoped
public class TripQuery extends AbstractFacesController implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 8956925900998562587L;

    @Inject
    protected TripQueryMethod tripQueryMethod;
    
    @Inject
    protected LoginSession loginSession;
    
    @Inject
    protected TripDAO tripDAO;
    
    protected Role roleDriver = Role.DRIVER;
    
    protected Role rolePassenger = Role.PASSENGER;
    
    
    
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
    
  
    /**
     * @return the tripQueryMethod
     */
    public TripQueryMethod getTripQueryMethod()
    {
        return tripQueryMethod;
    }

    /**
     * @param tripQueryMethod the tripQueryMethod to set
     */
    public void setTripQueryMethod(TripQueryMethod tripQueryMethod)
    {
        this.tripQueryMethod = tripQueryMethod;
    }

    /**
     * Get all offered trips by the logged in user.
     * 
     * @return null or the {@link Trip} instances in a {@link List} if any is given
     */
    public List<Trip> getOfferedTrips()
    {
        try {
            User loggedInUser = loginSession.getCurrentUser();
                        
            List<Trip> offeredTrips = this.tripDAO.getOfferedTrips(loggedInUser);
            
            return offeredTrips;
        } catch (Exception e) {
            Application.log("getOfferedTrips(): an exception occured:" + e.getMessage() 
                    + "\n" + ExceptionUtils.getStackTrace(e) , LogType.CRITICAL, getClass());
        }
        
        return null;
    }

}
