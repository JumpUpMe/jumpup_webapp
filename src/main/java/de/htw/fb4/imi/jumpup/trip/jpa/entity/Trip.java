/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.jpa.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.entity.AbstractEntity;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.verhicle.entity.Vehicle;

/**
 * <p>
 * Basis Entity for trips.
 * </p>
 * 
 * @author <a href="mailto:m_seidler@hotmail.de">Marco Seidler</a>
 * @since 03.01.2015
 * 
 */
@Entity
@Table(name = "trip")
@NamedQueries({
        @NamedQuery(name = Trip.NAME_QUERY_BY_USER, query = "SELECT t FROM Trip t WHERE t.driver = :driver ORDER BY t.startDateTime DESC, t.endDateTime DESC"),
        @NamedQuery(name = Trip.NAME_QUERY_BY_STARTPOINT, query = "SELECT t FROM Trip t WHERE t.startpoint = :startpoint"),
        @NamedQuery(name = Trip.NAME_QUERY_BY_ENDPOINT, query = "SELECT t FROM Trip t WHERE t.endpoint = :endpoint"),
        @NamedQuery(name = Trip.NAME_QUERY_BY_ID, query = "SELECT t FROM Trip t WHERE t.identity = :identity"),
        @NamedQuery(name = Trip.NAME_QUERY_ALL, query = "SELECT t FROM Trip t"),
        @NamedQuery(name = Trip.NAME_CRITERIA_QUERY, query = "SELECT t FROM Trip t WHERE"
                + " t.cancelationDateTime IS NULL"
                + " AND (t.driver != :passenger)"
                + " AND (:dateFrom IS NULL OR t.startDateTime >= :dateFrom)"
                + " AND (:dateTo IS NULL OR t.endDateTime <= :dateTo)"
                + " AND (:priceFrom IS NULL OR t.price >= :priceFrom)"
                + " AND (:priceTo IS NULL OR t.price <= :priceTo)")

})
public class Trip extends AbstractEntity
{

    public static final String NAME_QUERY_BY_USER = "TRIP_QUERY_BY_USER";
    public static final String NAME_QUERY_BY_STARTPOINT = "TRIP_QUERY_BY_STARTPOINT";
    public static final String NAME_QUERY_BY_ENDPOINT = "TRIP_QUERY_BY_ENDPOINT";
    public static final String NAME_QUERY_ALL = "TRIP_FETCH_ALL";
    public static final String NAME_QUERY_BY_ID = "TRIP_GET_BY_ID";
    public static final String NAME_CRITERIA_QUERY = "TRIP_QUERY_BY_CRITERIA";

    /**
     * 
     */
    private static final long serialVersionUID = -3854579506642418644L;

    @OneToMany(mappedBy = "trip", fetch = FetchType.EAGER)
    @XmlTransient
    private List<Booking> bookings;

    @Column(name = "startpoint", nullable = false, updatable = true, unique = false)
    protected String startpoint;

    @Column(name = "endpoint", nullable = false, updatable = true, unique = false)
    protected String endpoint;

    @Column(name = "latstartpoint", nullable = false, updatable = true, unique = false)
    protected double latStartpoint;

    @Column(name = "longstartpoint", nullable = false, updatable = true, unique = false)
    protected double longStartpoint;

    @Column(name = "latendpoint", nullable = false, updatable = true, unique = false)
    protected double latEndpoint;

    @Column(name = "longendpoint", nullable = false, updatable = true, unique = false)
    protected double longEndpoint;

    @Column(name = "startdate", nullable = false, updatable = true, unique = false)
    protected Timestamp startDateTime;

    @Column(name = "enddate", nullable = false, updatable = true, unique = false)
    protected Timestamp endDateTime;

    @Column(name = "price", nullable = false, updatable = true, unique = false)
    protected float price;

    @Lob
    @Column(name = "overviewpath", nullable = false, updatable = true, unique = false, length = PersistenceSettings.LONG_TEXT_MAX_LENGTH)
    protected String overViewPath;

    @Column(name = "viawaypoints", nullable = true, updatable = true, unique = false)
    protected String viaWaypoints;

    @Column(name = "numberofseats", nullable = false, updatable = true, unique = false)
    protected Integer numberOfSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trips")
    protected Vehicle vehicle;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @XmlTransient
    protected User driver;

    @Column(name = "cancelation_datetime", nullable = true, updatable = true, unique = false)
    protected Timestamp cancelationDateTime;

    @Column(name = "distance_meters", nullable = false, updatable = true)
    protected long distanceMeters;

    @Column(name = "duration_seconds", nullable = false, updatable = true)
    protected long durationSeconds;

    /**
     * First matching distance to passenger's start location on look for trips.
     */
    protected double distanceToPassengersStartLocation;
    /**
     * First matching distance to passenger's end location on look for trips.
     */
    protected double distanceToPassengersEndLocation;

    /**
     * @return the startpoint
     */
    public String getStartpoint()
    {
        return startpoint;
    }

    /**
     * @param startpoint
     *            the startpoint to set
     */
    public void setStartpoint(String startpoint)
    {
        this.startpoint = startpoint;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint()
    {
        return endpoint;
    }

    /**
     * @param endpoint
     *            the endpoint to set
     */
    public void setEndpoint(String endpoint)
    {
        this.endpoint = endpoint;
    }

    /**
     * @return the latStartpoint
     */
    public double getLatStartpoint()
    {
        return latStartpoint;
    }

    /**
     * @param latStartpoint
     *            the latStartpoint to set
     */
    public void setLatStartpoint(double latStartpoint)
    {
        this.latStartpoint = latStartpoint;
    }

    /**
     * @return the longStartpoint
     */
    public double getLongStartpoint()
    {
        return longStartpoint;
    }

    /**
     * @param longStartpoint
     *            the longStartpoint to set
     */
    public void setLongStartpoint(double longStartpoint)
    {
        this.longStartpoint = longStartpoint;
    }

    /**
     * @return the latEntpoint
     */
    public double getLatEndpoint()
    {
        return latEndpoint;
    }

    /**
     * @param latEndpoint
     *            the latEntpoint to set
     */
    public void setLatEndpoint(double latEndpoint)
    {
        this.latEndpoint = latEndpoint;
    }

    /**
     * @return the longEndpoint
     */
    public double getLongEndpoint()
    {
        return longEndpoint;
    }

    /**
     * @param longEndpoint
     *            the longEndpoint to set
     */
    public void setLongEndpoint(double longEndpoint)
    {
        this.longEndpoint = longEndpoint;
    }

    /**
     * @param startDateTime
     *            the startDateTime to set
     */
    public void setStartDateTime(Timestamp startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public Timestamp getStartDateTime()
    {
        return startDateTime;
    }

    /**
     * @param endDateTime
     *            the endDateTime to set
     */
    public void setEndDateTime(Timestamp endDateTime)
    {
        this.endDateTime = endDateTime;
    }

    public Timestamp getEndDateTime()
    {
        return endDateTime;
    }

    /**
     * @return the price
     */
    public float getPrice()
    {
        return price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(float price)
    {
        this.price = price;
    }

    /**
     * @return the overViewPath
     */
    public String getOverViewPath()
    {
        return overViewPath;
    }

    /**
     * @param overViewPath
     *            the overViewPath to set
     */
    public void setOverViewPath(String overViewPath)
    {
        this.overViewPath = overViewPath;
    }

    /**
     * @return the viaWaypoints
     */
    public String getViaWaypoints()
    {
        return viaWaypoints;
    }

    /**
     * @param viaWaypoints
     *            the viaWaypoints to set
     */
    public void setViaWaypoints(String viaWaypoints)
    {
        this.viaWaypoints = viaWaypoints;
    }

    /**
     * @return the numberOfSeats
     */
    public Integer getNumberOfSeats()
    {
        return numberOfSeats;
    }

    /**
     * @param numberOfSeats
     *            the numberOfSeats to set
     */
    public void setNumberOfSeats(Integer numberOfSeats)
    {
        this.numberOfSeats = numberOfSeats;
    }

    /**
     * @return the vehicle
     */
    public Vehicle getVehicle()
    {
        return vehicle;
    }

    /**
     * @param vehicle
     *            the vehicle to set
     */
    public void setVehicle(Vehicle vehicle)
    {
        this.vehicle = vehicle;
    }

    /**
     * @return the driver
     */
    public User getDriver()
    {
        return driver;
    }

    /**
     * @return the distanceToPassengersStartLocation
     */
    public double getDistanceToPassengersStartLocation()
    {
        return distanceToPassengersStartLocation;
    }

    /**
     * @param distanceToPassengersStartLocation2
     *            the distanceToPassengersStartLocation to set
     */
    public void setDistanceToPassengersStartLocation(
            double distanceToPassengersStartLocation2)
    {
        this.distanceToPassengersStartLocation = distanceToPassengersStartLocation2;
    }

    /**
     * @return the distanceToPassengersEndLocation
     */
    public double getDistanceToPassengersEndLocation()
    {
        return distanceToPassengersEndLocation;
    }

    /**
     * @param distanceToPassengersEndLocation2
     *            the distanceToPassengersEndLocation to set
     */
    public void setDistanceToPassengersEndLocation(
            double distanceToPassengersEndLocation2)
    {
        this.distanceToPassengersEndLocation = distanceToPassengersEndLocation2;
    }

    /**
     * @param driver
     *            the driver to set
     */
    public void setDriver(User driver)
    {
        this.driver = driver;
    }

    /**
     * @return the cancelationDateTime
     */
    public Timestamp getCancelationDateTime()
    {
        return cancelationDateTime;
    }

    /**
     * @param cancelationDateTime
     *            the cancelationDateTime to set
     */
    public void setCancelationDateTime(Timestamp cancelationDateTime)
    {
        this.cancelationDateTime = cancelationDateTime;
    }

    /**
     * Check whether the trip was cancelled by the driver.
     * 
     * @return
     */
    public boolean wasCancelled()
    {
        boolean wasCancelled = null != this.getCancelationDateTime();

        Application.log(
                "Trip " + getIdentity() + " was cancelled: " + wasCancelled,
                LogType.DEBUG, getClass());
        return wasCancelled;
    }

    /**
     * Check whether the trip can still be edited:
     * <ul>
     * <li>if it is in the future</li>
     * <li>if it was not cancelled yet</li>
     * <li>if it has no (confirmed) bookings yet</li>
     * </ul>
     * 
     * @return
     */
    public boolean canBeEdited()
    {
        return this.isInFuture() && !this.wasCancelled() && !this.hasBookings();
    }

    /**
     * Check whether the trip can still be canceled.
     * 
     * <ul>
     * <li>if takes place in the future.</li>
     * <li>if it wasn't cancelled yet.</li>
     * </ul>
     * 
     * @return
     */
    public boolean canBeCancelled()
    {
        return this.isInFuture() && !this.wasCancelled();
    }

    /**
     * Check whether the trip has booking relations
     * 
     * @return
     */
    public boolean hasBookings()
    {
        return (null != this.getBookings() && this.getBookings().size() > 0);
    }

    /**
     * <p>
     * Check whether this trip can still be booked.<br />
     * Therefore, it must met the following criteria:
     * </p>
     * <ul>
     * <li>The trip wasn't cancelled.</li>
     * <li>The trip takes place in the future.</li>
     * <li>There are still places left.</li>
     * </ul>
     * 
     * @return
     */
    public boolean canBeBooked()
    {
        return !this.wasCancelled() && this.isInFuture()
                && this.getNumberOfBookings() < this.getNumberOfSeats();
    }

    /**
     * <p>
     * Check whether this trip can still be booked.<br />
     * Therefore, it must met the following criteria:
     * </p>
     * <ul>
     * <li>The trip wasn't cancelled.</li>
     * <li>The trip takes place in the future.</li>
     * <li>There are still places left.</li>
     * <li>The given {@link User} didn't book it yet.</li>
     * </ul>
     * 
     * @param currentUser
     *            the {@link User} who wants to book the trip
     * @return
     */
    public boolean canBeBooked(User currentUser)
    {
        if (!this.canBeBooked()) {
            return false;
        }

        for (Booking booking : this.bookings) {
            if (booking.getPassenger().getIdentity() == currentUser
                    .getIdentity()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get the number of bookings.
     * 
     * @return
     */
    public Integer getNumberOfBookings()
    {
        if (null == this.getBookings()) {
            throw new NullPointerException(
                    "getNumberOfBookings(): No bookings given.");
        }

        return this.getBookings().size();
    }

    /**
     * Check whether the trip is in future, return true.
     * 
     * Otherwise return false (the trip is in past).
     * 
     * @return
     */
    public boolean isInFuture()
    {
        if (null == this.getStartDateTime()) {
            return false;
        }

        long currentTime = System.currentTimeMillis();

        return (this.getStartDateTime().getTime() - currentTime) > 0;
    }

    public List<Booking> getBookings()
    {
        return bookings;
    }

    public void setBookings(List<Booking> bookings)
    {
        this.bookings = bookings;
    }

    /**
     * @return the distanceMeters
     */
    public long getDistanceMeters()
    {
        return distanceMeters;
    }

    /**
     * @param distanceMeters
     *            the distanceMeters to set
     */
    public void setDistanceMeters(long distanceMeters)
    {
        this.distanceMeters = distanceMeters;
    }

    /**
     * @return the durationSeconds
     */
    public long getDurationSeconds()
    {
        return durationSeconds;
    }

    /**
     * @param durationSeconds
     *            the durationSeconds to set
     */
    public void setDurationSeconds(long durationSeconds)
    {
        this.durationSeconds = durationSeconds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cancelationDateTime == null) ? 0
                : cancelationDateTime.hashCode());
        result = prime * result
                + (int) (distanceMeters ^ (distanceMeters >>> 32));
        long temp;
        temp = Double.doubleToLongBits(distanceToPassengersEndLocation);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(distanceToPassengersStartLocation);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((driver == null) ? 0 : driver.hashCode());
        result = prime * result
                + (int) (durationSeconds ^ (durationSeconds >>> 32));
        result = prime * result
                + ((endDateTime == null) ? 0 : endDateTime.hashCode());
        result = prime * result
                + ((endpoint == null) ? 0 : endpoint.hashCode());
        temp = Double.doubleToLongBits(latEndpoint);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(latStartpoint);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longEndpoint);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(longStartpoint);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((numberOfSeats == null) ? 0 : numberOfSeats.hashCode());
        result = prime * result
                + ((overViewPath == null) ? 0 : overViewPath.hashCode());
        result = prime * result + Float.floatToIntBits(price);
        result = prime * result
                + ((startDateTime == null) ? 0 : startDateTime.hashCode());
        result = prime * result
                + ((startpoint == null) ? 0 : startpoint.hashCode());
        result = prime * result + ((vehicle == null) ? 0 : vehicle.hashCode());
        result = prime * result
                + ((viaWaypoints == null) ? 0 : viaWaypoints.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Trip other = (Trip) obj;
        if (bookings == null) {
            if (other.bookings != null)
                return false;
        }
        if (cancelationDateTime == null) {
            if (other.cancelationDateTime != null)
                return false;
        } else if (!cancelationDateTime.equals(other.cancelationDateTime))
            return false;
        if (distanceMeters != other.distanceMeters)
            return false;
        if (Double.doubleToLongBits(distanceToPassengersEndLocation) != Double
                .doubleToLongBits(other.distanceToPassengersEndLocation))
            return false;
        if (Double.doubleToLongBits(distanceToPassengersStartLocation) != Double
                .doubleToLongBits(other.distanceToPassengersStartLocation))
            return false;
        if (driver == null) {
            if (other.driver != null)
                return false;
        } else if (!driver.equals(other.driver))
            return false;
        if (durationSeconds != other.durationSeconds)
            return false;
        if (endDateTime == null) {
            if (other.endDateTime != null)
                return false;
        } else if (!endDateTime.equals(other.endDateTime))
            return false;
        if (endpoint == null) {
            if (other.endpoint != null)
                return false;
        } else if (!endpoint.equals(other.endpoint))
            return false;
        if (Double.doubleToLongBits(latEndpoint) != Double
                .doubleToLongBits(other.latEndpoint))
            return false;
        if (Double.doubleToLongBits(latStartpoint) != Double
                .doubleToLongBits(other.latStartpoint))
            return false;
        if (Double.doubleToLongBits(longEndpoint) != Double
                .doubleToLongBits(other.longEndpoint))
            return false;
        if (Double.doubleToLongBits(longStartpoint) != Double
                .doubleToLongBits(other.longStartpoint))
            return false;
        if (numberOfSeats == null) {
            if (other.numberOfSeats != null)
                return false;
        } else if (!numberOfSeats.equals(other.numberOfSeats))
            return false;
        if (overViewPath == null) {
            if (other.overViewPath != null)
                return false;
        } else if (!overViewPath.equals(other.overViewPath))
            return false;
        if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
            return false;
        if (startDateTime == null) {
            if (other.startDateTime != null)
                return false;
        } else if (!startDateTime.equals(other.startDateTime))
            return false;
        if (startpoint == null) {
            if (other.startpoint != null)
                return false;
        } else if (!startpoint.equals(other.startpoint))
            return false;
        if (vehicle == null) {
            if (other.vehicle != null)
                return false;
        } else if (!vehicle.equals(other.vehicle))
            return false;
        if (viaWaypoints == null) {
            if (other.viaWaypoints != null)
                return false;
        } else if (!viaWaypoints.equals(other.viaWaypoints))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Trip [startpoint=");
        builder.append(startpoint);
        builder.append(", endpoint=");
        builder.append(endpoint);
        builder.append(", latStartpoint=");
        builder.append(latStartpoint);
        builder.append(", longStartpoint=");
        builder.append(longStartpoint);
        builder.append(", latEndpoint=");
        builder.append(latEndpoint);
        builder.append(", longEndpoint=");
        builder.append(longEndpoint);
        builder.append(", startDateTime=");
        builder.append(startDateTime);
        builder.append(", endDateTime=");
        builder.append(endDateTime);
        builder.append(", price=");
        builder.append(price);
        builder.append(", overViewPath=");
        builder.append(overViewPath);
        builder.append(", viaWaypoints=");
        builder.append(viaWaypoints);
        builder.append(", numberOfSeats=");
        builder.append(numberOfSeats);
        builder.append(", vehicle=");
        builder.append(vehicle);
        builder.append(", driver=");
        builder.append(driver);
        builder.append(", cancelationDateTime=");
        builder.append(cancelationDateTime);
        builder.append(", distanceMeters=");
        builder.append(distanceMeters);
        builder.append(", durationSeconds=");
        builder.append(durationSeconds);
        builder.append("]");
        return builder.toString();
    }

}
