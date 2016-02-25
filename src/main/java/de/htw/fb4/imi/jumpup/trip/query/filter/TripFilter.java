/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query.filter;

import java.util.List;

import javax.ejb.Local;

import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.graph.Path;
import de.htw.fb4.imi.jumpup.trip.graph.shortest.PathNotFoundException;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

/**
 * <p>Filter {@link Trip} entities.</p>
 * 
 * <p>Filters follow the decorator pattern and implement this filter method.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 22.01.2015
 *
 */
@Local
public interface TripFilter
{
    /**
     * Set criteria that can be applied by this trip filter.
     * @param tripSearchCriteria
     */
    void setCriteria(TripSearchCriteria tripSearchCriteria);
    
    /**
     * Apply the filter function on the given list of trips.
     * @param givenTrips
     * @return
     */
    List<Trip> applyFilter(final List<Trip> givenTrips);    
    
    /**
     * Apply the filter and return a Path instances instead of a trip list.
     * Pathes allow connecting multiple partial trips.
     * @param givenTrips
     * @return
     * @throws PathNotFoundException 
     */
    Path applyOverlappingTripsFilter(final List<Trip> givenTrips) throws PathNotFoundException;
}
