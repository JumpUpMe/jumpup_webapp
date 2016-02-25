/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.htw.fb4.imi.jumpup.entity.AbstractEntity;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.Role;
import de.htw.fb4.imi.jumpup.user.entity.User;

/**
 * <p>
 * Basis Entity for Bookings.
 * </p>
 * 
 * @author <a href="mailto:m_seidler@hotmail.de">Marco Seidler</a>
 * @since 22.01.2015
 * 
 */

@Entity
@Table(name = "booking")
@NamedQueries({
        @NamedQuery(name = Booking.NAME_QUERY_BY_STARTPOINT, query = "SELECT b FROM Booking b WHERE b.startPoint = :startpoint"),
        @NamedQuery(name = Booking.NAME_QUERY_BY_ENDPOINT, query = "SELECT b FROM Booking b WHERE b.endPoint = :endpoint"),
        @NamedQuery(name = Booking.NAME_QUERY_BY_ID, query = "SELECT b FROM Booking b WHERE b.identity = :identity"),
        @NamedQuery(name = Booking.NAME_QUERY_BY_PASSENGER, query = "SELECT b FROM Booking b WHERE b.passenger = :passenger"),
        @NamedQuery(name = Booking.NAME_QUERY_BY_TRIP_ID, query = "SELECT b FROM Booking b WHERE b.tripIdentity = :tripId"),
        @NamedQuery(name = Booking.NAME_QUERY_BY_TRIP, query = "SELECT b FROM Booking b WHERE b.trip = :trip") })
public class Booking extends AbstractEntity
{

    public static final String NAME_QUERY_BY_STARTPOINT = "BOOKING_QUERY_BY_STARTPOINT";
    public static final String NAME_QUERY_BY_ENDPOINT = "BOOKING_QUERY_BY_ENDPOINT";
    public static final String NAME_QUERY_BY_TRIP = "BOOKING_QUERY_BY_TRIP";
    public static final String NAME_QUERY_BY_PASSENGER = "BOOKING_QUERY_BY_PASSENGER";
    public static final String NAME_QUERY_BY_ID = "BOOKING_QUERY_BY_ID";
    public static final String NAME_QUERY_BY_TRIP_ID = "BOOKING_QUERY_BY_TRIP_ID";

    /**
     * 
     */
    private static final long serialVersionUID = 3235003970981904704L;

    @Column(name = "startpoint", nullable = false, updatable = true, unique = false)
    private String startPoint;

    @Column(name = "endpoint", nullable = false, updatable = true, unique = false)
    private String endPoint;

    @Column(name = "startLatitude", nullable = false, updatable = true, unique = false)
    private double startLatitude;

    @Column(name = "startLongitude", nullable = false, updatable = true, unique = false)
    private double startLongitude;

    @Column(name = "endLatitude", nullable = false, updatable = true, unique = false)
    private double endLatitude;

    @Column(name = "endLongitude", nullable = false, updatable = true, unique = false)
    private double endLongitude;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tripIdentity")
    private Trip trip;
    
    @Column(name = "tripIdentity", insertable = false, updatable = false)
    private Long tripIdentity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passengerIdentity")
    private User passenger;
    
    @Column(name = "confirmation_datetime", nullable = true, updatable = true, unique = false)
    protected Timestamp confirmationDateTime;
    
    @Column(name = "cancelation_datetime", nullable = true, updatable = true, unique = false)
    protected Timestamp cancellationDateTime;
    
    @Column(name = "actor_on_last_change", nullable = false, updatable = true, unique = false)
    @Enumerated(value = EnumType.ORDINAL)
    protected Role actorOnLastChange;
   
    
    @Transient
    protected String bookingHash;

    public String getStartPoint()
    {
        return startPoint;
    }

    public void setStartPoint(String startPoint)
    {
        this.startPoint = startPoint;
    }

    public String getEndPoint()
    {
        return endPoint;
    }

    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }

    public double getStartLatitude()
    {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude)
    {
        this.startLatitude = startLatitude;
    }

    public double getStartLongitude()
    {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude)
    {
        this.startLongitude = startLongitude;
    }

    public double getEndLatitude()
    {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude)
    {
        this.endLatitude = endLatitude;
    }

    public double getEndLongitude()
    {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude)
    {
        this.endLongitude = endLongitude;
    }
    
    public Long getTripIdentity()
    {
        return tripIdentity;
    }
    
    public void setTripIdentity(Long tripIdentity)
    {
        this.tripIdentity = tripIdentity;
    }

    public Trip getTrip()
    {
        return trip;
    }

    public void setTrip(Trip trip)
    {
        this.trip = trip;
    }

    public User getPassenger()
    {
        return passenger;
    }

    public void setPassenger(User passenger)
    {
        this.passenger = passenger;
    }

    /**
     * @return the bookingHash
     */
    public String getBookingHash()
    {
        return bookingHash;
    }

    /**
     * @param bookingHash the bookingHash to set
     */
    public void setBookingHash(String bookingHash)
    {
        this.bookingHash = bookingHash;
    }
    
    /**
     * @return the cancelationDateTime
     */
    public Timestamp getCancellationDateTime()
    {
        return cancellationDateTime;
    }

    /**
     * @param cancelationDateTime
     *            the cancelationDateTime to set
     */
    public void setCancellationDateTime(Timestamp cancelationDateTime)
    {
        this.cancellationDateTime = cancelationDateTime;
    }

    /**
     * Check whether the trip was cancelled by the driver.
     * 
     * @return
     */
    public boolean wasConfirmed()
    {
        boolean wasConfirmed = null != this.getConfirmationDateTime();

        return wasConfirmed && !wasCancelled();
    }
    
    /**
     * @return the cancelationDateTime
     */
    public Timestamp getConfirmationDateTime()
    {
        return confirmationDateTime;
    }

    /**
     * @param cancellationDateTime
     *            the cancelationDateTime to set
     */
    public void setConfirmationDateTime(Timestamp confirmationDateTime)
    {
        this.confirmationDateTime = confirmationDateTime;
    }

    /**
     * Check whether the trip was cancelled by the driver.
     * 
     * @return
     */
    public boolean wasCancelled()
    {
        boolean wasCancelled = null != this.getCancellationDateTime();

        return wasCancelled;
    }

    /**
     * @return the actorOnLastChange
     */
    public Role getActorOnLastChange()
    {
        return actorOnLastChange;
    }

    /**
     * @param actorOnLastChange the actorOnLastChange to set
     */
    public void setActorOnLastChange(Role actorOnLastChange)
    {
        this.actorOnLastChange = actorOnLastChange;
    }

    @Override
    public String toString()
    {
        return "Booking [startPoint=" + startPoint + ", endPoint=" + endPoint
                + ", startLatitude=" + startLatitude + ", startLongitude="
                + startLongitude + ", endLatitude=" + endLatitude
                + ", endLongitude=" + endLongitude + ", trip=" + trip;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(endLatitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endLongitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((endPoint == null) ? 0 : endPoint.hashCode());
        result = prime * result
                + ((passenger == null) ? 0 : passenger.hashCode());
        temp = Double.doubleToLongBits(startLatitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLongitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((startPoint == null) ? 0 : startPoint.hashCode());
        result = prime * result + ((trip == null) ? 0 : trip.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Booking other = (Booking) obj;
        if (Double.doubleToLongBits(endLatitude) != Double
                .doubleToLongBits(other.endLatitude))
            return false;
        if (Double.doubleToLongBits(endLongitude) != Double
                .doubleToLongBits(other.endLongitude))
            return false;
        if (endPoint == null) {
            if (other.endPoint != null)
                return false;
        } else if (!endPoint.equals(other.endPoint))
            return false;
        if (passenger == null) {
            if (other.passenger != null)
                return false;
        } else if (!passenger.equals(other.passenger))
            return false;
        if (Double.doubleToLongBits(startLatitude) != Double
                .doubleToLongBits(other.startLatitude))
            return false;
        if (Double.doubleToLongBits(startLongitude) != Double
                .doubleToLongBits(other.startLongitude))
            return false;
        if (startPoint == null) {
            if (other.startPoint != null)
                return false;
        } else if (!startPoint.equals(other.startPoint))
            return false;
        if (trip == null) {
            if (other.trip != null)
                return false;
        } else if (!trip.equals(other.trip))
            return false;
        return true;
    }

}
