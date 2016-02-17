/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking.rest.model;

import de.htw.fb4.imi.jumpup.rest.response.model.AbstractRestModel;
import de.htw.fb4.imi.jumpup.user.Role;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 02.01.2016
 *
 */
public class BookingWebServiceModel extends AbstractRestModel
{
    private String startPoint;
    private String endPoint;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private long confirmationDateTime;
    private Long cancellationDateTime;
    private Role actorOnLastChange;
    private Long tripId;

    /**
     * @return the startPoint
     */
    public String getStartPoint()
    {
        return startPoint;
    }

    /**
     * @param startPoint
     *            the startPoint to set
     */
    public void setStartPoint(String startPoint)
    {
        this.startPoint = startPoint;
    }

    /**
     * @return the endPoint
     */
    public String getEndPoint()
    {
        return endPoint;
    }

    /**
     * @param endPoint
     *            the endPoint to set
     */
    public void setEndPoint(String endPoint)
    {
        this.endPoint = endPoint;
    }

    /**
     * @return the startLatitude
     */
    public double getStartLatitude()
    {
        return startLatitude;
    }

    /**
     * @param startLatitude
     *            the startLatitude to set
     */
    public void setStartLatitude(double startLatitude)
    {
        this.startLatitude = startLatitude;
    }

    /**
     * @return the startLongitude
     */
    public double getStartLongitude()
    {
        return startLongitude;
    }

    /**
     * @param startLongitude
     *            the startLongitude to set
     */
    public void setStartLongitude(double startLongitude)
    {
        this.startLongitude = startLongitude;
    }

    /**
     * @return the endLatitude
     */
    public double getEndLatitude()
    {
        return endLatitude;
    }

    /**
     * @param endLatitude
     *            the endLatitude to set
     */
    public void setEndLatitude(double endLatitude)
    {
        this.endLatitude = endLatitude;
    }

    /**
     * @return the endLongitude
     */
    public double getEndLongitude()
    {
        return endLongitude;
    }

    /**
     * @param endLongitude
     *            the endLongitude to set
     */
    public void setEndLongitude(double endLongitude)
    {
        this.endLongitude = endLongitude;
    }

    /**
     * @return the confirmationDateTime
     */
    public long getConfirmationDateTime()
    {
        return confirmationDateTime;
    }

    /**
     * @param confirmationDateTime
     *            the confirmationDateTime to set
     */
    public void setConfirmationDateTime(long confirmationDateTime)
    {
        this.confirmationDateTime = confirmationDateTime;
    }

    /**
     * @return the cancellationDateTime
     */
    public Long getCancellationDateTime()
    {
        return cancellationDateTime;
    }

    /**
     * @param cancellationDateTime
     *            the cancellationDateTime to set
     */
    public void setCancellationDateTime(Long cancellationDateTime)
    {
        this.cancellationDateTime = cancellationDateTime;
    }

    /**
     * @return the actorOnLastChange
     */
    public Role getActorOnLastChange()
    {
        return actorOnLastChange;
    }

    /**
     * @param actorOnLastChange
     *            the actorOnLastChange to set
     */
    public void setActorOnLastChange(Role actorOnLastChange)
    {
        this.actorOnLastChange = actorOnLastChange;
    }

    public Long getTripId()
    {
        return tripId;
    }

    public void setTripId(Long long1)
    {
        this.tripId = long1;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((actorOnLastChange == null) ? 0
                : actorOnLastChange.hashCode());
        result = prime * result + ((cancellationDateTime == null) ? 0
                : cancellationDateTime.hashCode());
        result = prime * result
                + (int) (confirmationDateTime ^ (confirmationDateTime >>> 32));
        long temp;
        temp = Double.doubleToLongBits(endLatitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(endLongitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((endPoint == null) ? 0 : endPoint.hashCode());
        temp = Double.doubleToLongBits(startLatitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(startLongitude);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result
                + ((startPoint == null) ? 0 : startPoint.hashCode());
        result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
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
        BookingWebServiceModel other = (BookingWebServiceModel) obj;
        if (actorOnLastChange != other.actorOnLastChange)
            return false;
        if (cancellationDateTime == null) {
            if (other.cancellationDateTime != null)
                return false;
        } else if (!cancellationDateTime.equals(other.cancellationDateTime))
            return false;
        if (confirmationDateTime != other.confirmationDateTime)
            return false;
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
        if (tripId == null) {
            if (other.tripId != null)
                return false;
        } else if (!tripId.equals(other.tripId))
            return false;
        return true;
    }
}
