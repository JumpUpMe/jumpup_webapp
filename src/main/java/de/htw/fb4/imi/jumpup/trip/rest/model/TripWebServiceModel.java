/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.rest.model;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.htw.fb4.imi.jumpup.rest.response.model.AbstractRestModel;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.verhicle.entity.Vehicle;

/**
 * <p>
 * Plain old object that will be (un-)marshalled and sent to REST callers.
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 02.12.2015
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TripWebServiceModel extends AbstractRestModel
{
    public static final String FIELD_NAME_START_POINT = "startpoint";
    public static final String FIELD_NAME_END_POINT = "endpoint";
    public static final String FIELD_NAME_START_LAT = "latStartpoint";
    public static final String FIELD_NAME_START_LNG = "longStartpoint";
    public static final String FIELD_NAME_END_LAT = "latEndpoint";
    public static final String FIELD_NAME_END_LNG = "longEndpoint";
    public static final String FIELD_NAME_START_DATETIME = "startDateTime";
    public static final String FIELD_NAME_END_DATETIME = "endDateTime";
    public static final String FIELD_NAME_PRICE = "price";
    public static final String FIELD_NAME_OVERVIEW_PATH = "overViewPath";
    public static final String FIELD_NAME_VIA_WAYPOINTS = "viaWaypoints";
    public static final String FIELD_NAME_NUMBER_OF_SEATS = "numberOfSeats";

    protected String startpoint;
    protected String endpoint;
    protected double latStartpoint;
    protected double longStartpoint;
    protected double latEndpoint;
    protected double longEndpoint;
    protected long startDateTime;
    protected long endDateTime;
    protected float price;
    protected String overViewPath;
    protected String viaWaypoints;
    protected Integer numberOfSeats;
    protected Vehicle vehicle;
    protected User driver;
    protected Long cancelationDateTime;
    protected long distanceMeters;
    protected long durationSeconds;

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

    public void setStartDateTime(long startDateTime)
    {
        this.startDateTime = startDateTime;
    }

    public long getStartDateTime()
    {
        return startDateTime;
    }

    public void setEndDateTime(long endDateTime)
    {
        this.endDateTime = endDateTime;
    }

    public long getEndDateTime()
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
     * @param driver
     *            the driver to set
     */
    public void setDriver(User driver)
    {
        this.driver = driver;
    }

    public void setCancelationDateTime(Long cancelationDateTime)
    {
        this.cancelationDateTime = cancelationDateTime;
    }

    public Long getCancelationDateTime()
    {
        return cancelationDateTime;
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

    /**
     * 
     * @param cancelationDateTime2
     */
    public void setCancelationDateTime(Timestamp cancelationDateTime2)
    {
        if (null != cancelationDateTime2) {
            this.setCancelationDateTime(cancelationDateTime2.getTime());
        }
    }
}
