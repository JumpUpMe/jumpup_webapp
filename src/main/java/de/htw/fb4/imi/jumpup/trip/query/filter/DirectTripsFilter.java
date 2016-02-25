/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.util.math.CoordinateUtil;
import de.htw.fb4.imi.jumpup.util.math.Coordinates;

/**
 * <p>This filter is the most important one. <br />
 * It is responsible for identifying nearby {@link Trip} that are located next to passengers start and end location.
 * </p>
 * 
 * <p>Therefore, check if the given latitudes and longitudes are near some of the waypoints of one {@link Trip} instance.<br />
 * The property overviewPath contains most of the waypoints as comma-separated list of lat/lng values.</p>
 * 
 * <p>The criteria is given by a {@link TripSearchCriteria} query model. The maximum distance to the {@link Trip} waypoints at both, passenger's start and end location must not be greater than "maximumDistance".</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 08.08.2015
 *
 */
public class DirectTripsFilter extends AbstractTripFilter
{

    private double lastDistance;
    private double distanceToPassengersStartLocation;
    private double distanceToPassengersEndLocation;

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.filter.TripFilter#applyFilter(java.util.List)
     */
    @Override
    public List<Trip> applyFilter(final List<Trip> givenTrips)
    {
        if (null == super.tripSearchCriteria) {
            throw new NullPointerException("You need to set tripSearchCriteria before calling applyFilter");
        }
        
        if (null == super.tripSearchCriteria.getLatStartPoint() || null == super.tripSearchCriteria.getLongStartPoint()
            || null == super.tripSearchCriteria.getLatEndPoint() || null == super.tripSearchCriteria.getLongEndPoint()) {
            throw new NullPointerException("Null value given for latStartPoint, longStartPoint, latEndPoint or longEndpoint.");
        }
        
        List<Trip> preFilteredTrips =  super.applyFilter(givenTrips);
        
        List<Trip> filteredTrips = this.findNearbyTrips(preFilteredTrips);
        
        return filteredTrips;
    }

    private List<Trip> findNearbyTrips(List<Trip> preFilteredTrips)
    {
        List<Trip> nearbyTrips = new ArrayList<Trip>();
        
        for (Trip trip : preFilteredTrips) {
            if (this.isNearPassenger(trip)) {
                nearbyTrips.add(trip);
                trip.setDistanceToPassengersStartLocation(this.distanceToPassengersStartLocation);
                trip.setDistanceToPassengersEndLocation(this.distanceToPassengersEndLocation);
            }
        }
        
        return nearbyTrips;
    }

    private boolean isNearPassenger(Trip trip)
    {
        Coordinates tripStart = new Coordinates(trip.getLatStartpoint(), trip.getLongStartpoint());
        Coordinates tripEnd = new Coordinates(trip.getLatEndpoint(), trip.getLongEndpoint());
        
        Coordinates passengersStart = new Coordinates(this.tripSearchCriteria.getLatStartPoint(), this.tripSearchCriteria.getLongStartPoint());
        Coordinates passengersEnd = new Coordinates(this.tripSearchCriteria.getLatEndPoint(), this.tripSearchCriteria.getLongEndPoint());
        
        if (this.isNearLocation(tripStart, passengersStart)                
            && this.isNearLocation(tripEnd, passengersEnd)) {
            // passenger's start and endpoint are near driver's start and endpoint
            return true;
        }
        
        // otherwise iterate over waypoints
        if (this.isNearPath(trip.getOverViewPath(), passengersStart, passengersEnd)) {
            return true;
        }
        
        return false;
    }

    private boolean isNearPath(String overViewPath,
            Coordinates passengersStart, Coordinates passengersEnd)
    {
        boolean isNearPassengerStartLocation = false;
        boolean isNearPassengerEndLocation = false;
        
        Set<Coordinates> waypoints = CoordinateUtil.parseCoordinateSetBy(overViewPath);
        for (Coordinates tripWaypoint : waypoints) {
            if (!isNearPassengerStartLocation && this.isNearLocation(tripWaypoint, passengersStart)) {
                isNearPassengerStartLocation = true;
                this.distanceToPassengersStartLocation = this.lastDistance;
            }
            
            if (!isNearPassengerEndLocation && this.isNearLocation(tripWaypoint, passengersEnd)) {
                isNearPassengerEndLocation = true;
                this.distanceToPassengersEndLocation = this.lastDistance;
            }          
            
            // break if near start and end location was found to avoid redundant work
            if (isNearPassengerStartLocation && isNearPassengerEndLocation) {
                break;
            }
            
        }
        
        return (isNearPassengerStartLocation && isNearPassengerEndLocation);
    }

    private boolean isNearLocation(Coordinates tripCoordinates, Coordinates passengersCoordinates)
    {        
        if (null == tripCoordinates) {
            throw new NullPointerException("Nullpointer for tripCoordinates");
        }
        if (null == passengersCoordinates) {
            throw new NullPointerException("Nullpointer for passengersCoordinates");
        }
        if (null == this.tripSearchCriteria.getMaxDistance()) {
            throw new NullPointerException("Nullpointer for tripSearchCriteria:maxDistance");
        }
        
        
        this.lastDistance = CoordinateUtil.calculateDistanceBetween(tripCoordinates, passengersCoordinates);
        if (this.lastDistance< this.tripSearchCriteria.getMaxDistance()) {
            return true;
        }
        
        return false;
    }
}
