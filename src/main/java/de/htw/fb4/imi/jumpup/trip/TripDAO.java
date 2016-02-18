/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip;

import java.util.List;

import javax.ejb.Local;
import javax.persistence.NoResultException;

import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.util.model.Pagination;

/**
 * <p>
 * Data access object to perform database operations on {@link Trip} entities.
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 30.01.2015
 *
 */
@Local
public interface TripDAO
{
    /**
     * Load the given trip by ID.
     * 
     * @param id
     * @return
     * @throws NoResultException
     *             if the identity couldn't be matched.
     */
    Trip getTripByID(long identity);

    /**
     * Force the join of the driver.
     * 
     * @param trip
     */
    void joinDriver(Trip trip);

    /**
     * Load trip criteria by the given {@link TripSearchCriteria} model.
     * 
     * @param tripSearchCriteria
     * @return
     */
    List<Trip> getByCriteria(TripSearchCriteria tripSearchCriteria);

    /**
     * Get trips that the given {@link User} offered as a driver.
     * 
     * @param user
     *            owner of offered trips (driver)
     * @return
     */
    List<Trip> getOfferedTrips(final User user);

    /**
     * Get trips that the given {@link User} offered as a driver.
     * 
     * @param user
     *            owner of offered trips (driver)
     * @param pagination
     *            the pagination infos
     * @return
     */
    List<Trip> getOfferedTrips(final User user, final Pagination pagination);
}
