/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.user.UserDAO;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.util.PersistenceHelper;
import de.htw.fb4.imi.jumpup.util.model.Pagination;

import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 30.01.2015
 *
 */
@Stateless(name = BeanNames.TRIP_DAO)
public class TripDAOImpl implements TripDAO
{
    @PersistenceContext(unitName = PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManager em;

    @Inject
    protected UserDAO userDAO;

    /*
     * (non-Javadoc)
     * 
     * @see de.htw.fb4.imi.jumpup.trip.TripDAO#getTripByID(long)
     */
    @Override
    public Trip getTripByID(long identity)
    {
        final Query query = this.em.createNamedQuery(Trip.NAME_QUERY_BY_ID,
                Trip.class);
        query.setParameter("identity", new Long(identity));
        return (Trip) query.getSingleResult();

    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.trip.TripDAO#joinDriver(de.htw.fb4.imi.jumpup.trip.
     * entities.Trip)
     */
    public void joinDriver(Trip trip)
    {
        if (!em.contains(trip)) {
            trip = em.merge(trip);
        }

        User driver = userDAO.loadById(trip.getDriver().getIdentity());

        if (!em.contains(driver)) {
            driver = em.merge(driver);
        }

        trip.setDriver(driver);
    }

    @Override
    public List<Trip> getByCriteria(TripSearchCriteria tripSearchCriteria)
    {
        Timestamp dateFromTimeStamp = this
                .convertToTimestamp(tripSearchCriteria.getDateFrom());
        Timestamp dateToTimeStamp = this
                .convertToTimestamp(tripSearchCriteria.getDateTo());
        Float priceFrom = tripSearchCriteria.getPriceFrom();
        Float priceTo = tripSearchCriteria.getPriceTo();
        User passenger = tripSearchCriteria.getPassenger();

        try {
            // search within date and price range, exclude trips offered by
            // currently logged-in user

            return em.createNamedQuery(Trip.NAME_CRITERIA_QUERY, Trip.class)
                    .setParameter("dateFrom", dateFromTimeStamp)
                    .setParameter("dateTo", dateToTimeStamp)
                    .setParameter("priceFrom", priceFrom)
                    .setParameter("priceTo", priceTo)
                    .setParameter("passenger", passenger).getResultList();
        } catch (Exception e) {
            Application.log(
                    "prepareCriteriaSearch(): exception " + e.getMessage()
                            + "\nstack:" + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
        }

        return null;
    }

    /**
     * Convert from date to {@link Timestamp}.
     * 
     * @param date
     * @return
     */
    private Timestamp convertToTimestamp(Date date)
    {
        if (null == date) {
            return null;
        }

        return new Timestamp(date.getTime());
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.trip.query.TripQueryMethod#getOfferedTrips(de.htw.
     * fb4.imi.jumpup.user.entities.User)
     */
    @Override
    public List<Trip> getOfferedTrips(final User user)
    {
        try {
            return prepareOfferedTripsQuery(user).getResultList();
        } catch (Exception e) {
            Application.log("getOfferedTrips(): exception " + e.getMessage()
                    + "\nStack trace: " + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
        }

        return null;
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.trip.TripDAO#getOfferedTrips(de.htw.fb4.imi.jumpup.
     * user.entity.User, de.htw.fb4.imi.jumpup.util.model.Pagination)
     */
    public List<Trip> getOfferedTrips(User user, Pagination pagination)
    {
        try {
            return PersistenceHelper.addPaginationToQuery(
                    prepareOfferedTripsQuery(user), pagination).getResultList();
        } catch (Exception e) {
            Application.log("getOfferedTrips(): exception " + e.getMessage()
                    + "\nStack trace: " + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
        }

        return null;
    }

    private Query prepareOfferedTripsQuery(final User user)
    {
        EntityManager em = this.prepareEntityManager(user);
        Query q = em.createNamedQuery(Trip.NAME_QUERY_BY_USER, Trip.class)
                .setParameter("driver", user);
        return q;
    }

    private EntityManager prepareEntityManager(User user)
    {
        em.merge(user);

        return em;
    }
}
