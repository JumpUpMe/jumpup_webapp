/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query;

import javax.ejb.Local;

import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryNoResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.util.ErrorPrintable;

/**
 * <p>This EJB offers services to:</p> 
 * <ul>
 *  <li>Fetching trips that a driver offers</li>
 *  <li>TODO Fetching trips that a passenger booked</li>
 *  <li>TODO A passenger searchs for trips matching special criteria</li>
 * </ul>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 19.01.2015
 *
 */
@Local
public interface TripQueryMethod extends ErrorPrintable
{    
    
    /**
     * Get trips that match the criteria as given by the {@link TripSearchCriteria} instance.
     * 
     * @param tripSearchModel
     * @return a list of {@link Trip} which can be empty if the criteria was not met
     */
    TripQueryResults searchForTrips(final TripSearchCriteria tripSearchModel);

    /**
     * Build and reutrn TripQueryNoResult.
     * @return
     */
    TripQueryNoResults getNoTripsResult();

}
