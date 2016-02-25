/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.entity.User;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 30.01.2015
 *
 */
@Stateless(name = BeanNames.BOOKING_DAO)
public class BookingDAOImpl implements BookingDAO
{
    @PersistenceContext(unitName = PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManager em;

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingDAO#queryById(long)
     */
    @Override
    public Booking queryById(long bookingId)
    {
        final Query query = this.em.createNamedQuery(Booking.NAME_QUERY_BY_ID,
                Booking.class);
        query.setParameter("identity", new Long(bookingId));
        return (Booking) query.getSingleResult();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingDAO#save(de.htw.fb4.imi.jumpup.booking.entities.Booking)
     */
    public long save(Booking booking)
    {
        this.em.persist(this.em.contains(booking) ? booking : this.em.merge(booking));
        
        this.em.flush();
        
        return booking.getIdentity();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingDAO#getBookingsByTrip(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    public List<Booking> getBookingsByTrip(Trip trip)
    {
        final TypedQuery<Booking> query = this.em.createNamedQuery(Booking.NAME_QUERY_BY_TRIP,
            Booking.class);
        query.setParameter("trip", trip);
        return query.getResultList(); 
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingDAO#getBookingsByPassenger(de.htw.fb4.imi.jumpup.user.entities.User)
     */
    public List<Booking> getBookingsByPassenger(User passenger)
    {
        Application.log("getBookingsByPassenger(): passenger " + passenger, LogType.DEBUG, getClass());
        final TypedQuery<Booking> query = this.em.createNamedQuery(Booking.NAME_QUERY_BY_PASSENGER,
                Booking.class);
        query.setParameter("passenger", passenger);
        return query.getResultList(); 
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.booking.BookingDAO#getBookingsByTrip(java.lang.Long)
     */
    public List<Booking> getBookingsByTrip(Long entityId)
    {
        final TypedQuery<Booking> query = this.em.createNamedQuery(Booking.NAME_QUERY_BY_TRIP_ID,
                Booking.class);
            query.setParameter("tripId", entityId);
            
        return query.getResultList();         
    }
}
