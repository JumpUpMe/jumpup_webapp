/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query;

import java.text.ParseException;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.htw.fb4.imi.jumpup.util.LocaleHelper;

/**
 * <p>
 * Query-Model for queries that are done via REST service.
 * </p>
 * 
 * <p>
 * This model will be bound by XML / JSON.
 * </p>
 * 
 * 
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 22.01.2015
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TripSearchCriteria
{
    protected String startPoint;
    protected Float latStartPoint;
    protected Float longStartPoint;
    protected Float latEndPoint;
    protected Float longEndPoint;
    protected String endPoint;

    protected Date dateFrom;
    protected Date dateTo;

    protected Float priceFrom;
    protected Float priceTo;

    protected Integer maxDistance;

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
     * @return the latStartPoint
     */
    public Float getLatStartPoint()
    {
        return latStartPoint;
    }

    /**
     * @param latStartPoint
     *            the latStartPoint to set
     */
    public void setLatStartPoint(Float latStartPoint)
    {
        this.latStartPoint = latStartPoint;
    }

    /**
     * @return the longStartPoint
     */
    public Float getLongStartPoint()
    {
        return longStartPoint;
    }

    /**
     * @param longStartPoint
     *            the longStartPoint to set
     */
    public void setLongStartPoint(Float longStartPoint)
    {
        this.longStartPoint = longStartPoint;
    }

    /**
     * @return the latEndPoint
     */
    public Float getLatEndPoint()
    {
        return latEndPoint;
    }

    /**
     * @param latEndPoint
     *            the latEndPoint to set
     */
    public void setLatEndPoint(Float latEndPoint)
    {
        this.latEndPoint = latEndPoint;
    }

    /**
     * @return the longEndPoint
     */
    public Float getLongEndPoint()
    {
        return longEndPoint;
    }

    /**
     * @param longEndPoint
     *            the longEndPoint to set
     */
    public void setLongEndPoint(Float longEndPoint)
    {
        this.longEndPoint = longEndPoint;
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
     * @return the dateFrom
     */
    public Date getDateFrom()
    {
        return dateFrom;
    }

    /**
     * @param dateFrom
     *            the dateFrom to set
     * @throws ParseException
     */
    public void setDateFrom(String dateFrom) throws ParseException
    {
        this.dateFrom = new LocaleHelper().parseDateFromString(dateFrom);
    }

    /**
     * @return the dateTo
     */
    public Date getDateTo()
    {
        return dateTo;
    }

    /**
     * @param dateTo
     *            the dateTo to set
     * @throws ParseException
     */
    public void setDateTo(String dateTo) throws ParseException
    {
        this.dateTo = new LocaleHelper().parseDateFromString(dateTo);
    }

    /**
     * @return the priceFrom
     */
    public Float getPriceFrom()
    {
        return priceFrom;
    }

    /**
     * @param priceFrom
     *            the priceFrom to set
     */
    public void setPriceFrom(Float priceFrom)
    {
        this.priceFrom = priceFrom;
    }

    /**
     * @return the priceTo
     */
    public Float getPriceTo()
    {
        return priceTo;
    }

    /**
     * @param priceTo
     *            the priceTo to set
     */
    public void setPriceTo(Float priceTo)
    {
        this.priceTo = priceTo;
    }

    /**
     * @return the maxDistance
     */
    public Integer getMaxDistance()
    {
        return maxDistance;
    }

    /**
     * @param maxDistance
     *            the maxDistance to set
     */
    public void setMaxDistance(Integer maxDistance)
    {
        this.maxDistance = maxDistance;
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
        result = prime * result
                + ((dateFrom == null) ? 0 : dateFrom.hashCode());
        result = prime * result + ((dateTo == null) ? 0 : dateTo.hashCode());
        result = prime * result
                + ((endPoint == null) ? 0 : endPoint.hashCode());
        result = prime * result
                + ((latEndPoint == null) ? 0 : latEndPoint.hashCode());
        result = prime * result
                + ((latStartPoint == null) ? 0 : latStartPoint.hashCode());
        result = prime * result
                + ((longEndPoint == null) ? 0 : longEndPoint.hashCode());
        result = prime * result
                + ((longStartPoint == null) ? 0 : longStartPoint.hashCode());
        result = prime * result
                + ((maxDistance == null) ? 0 : maxDistance.hashCode());
        result = prime * result
                + ((priceFrom == null) ? 0 : priceFrom.hashCode());
        result = prime * result + ((priceTo == null) ? 0 : priceTo.hashCode());
        result = prime * result
                + ((startPoint == null) ? 0 : startPoint.hashCode());
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
        TripSearchCriteria other = (TripSearchCriteria) obj;
        if (dateFrom == null) {
            if (other.dateFrom != null)
                return false;
        } else if (!dateFrom.equals(other.dateFrom))
            return false;
        if (dateTo == null) {
            if (other.dateTo != null)
                return false;
        } else if (!dateTo.equals(other.dateTo))
            return false;
        if (endPoint == null) {
            if (other.endPoint != null)
                return false;
        } else if (!endPoint.equals(other.endPoint))
            return false;
        if (latEndPoint == null) {
            if (other.latEndPoint != null)
                return false;
        } else if (!latEndPoint.equals(other.latEndPoint))
            return false;
        if (latStartPoint == null) {
            if (other.latStartPoint != null)
                return false;
        } else if (!latStartPoint.equals(other.latStartPoint))
            return false;
        if (longEndPoint == null) {
            if (other.longEndPoint != null)
                return false;
        } else if (!longEndPoint.equals(other.longEndPoint))
            return false;
        if (longStartPoint == null) {
            if (other.longStartPoint != null)
                return false;
        } else if (!longStartPoint.equals(other.longStartPoint))
            return false;
        if (maxDistance == null) {
            if (other.maxDistance != null)
                return false;
        } else if (!maxDistance.equals(other.maxDistance))
            return false;
        if (priceFrom == null) {
            if (other.priceFrom != null)
                return false;
        } else if (!priceFrom.equals(other.priceFrom))
            return false;
        if (priceTo == null) {
            if (other.priceTo != null)
                return false;
        } else if (!priceTo.equals(other.priceTo))
            return false;
        if (startPoint == null) {
            if (other.startPoint != null)
                return false;
        } else if (!startPoint.equals(other.startPoint))
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
        builder.append("QueryModel [startPoint=");
        builder.append(startPoint);
        builder.append(", latStartPoint=");
        builder.append(latStartPoint);
        builder.append(", longStartPoint=");
        builder.append(longStartPoint);
        builder.append(", latEndPoint=");
        builder.append(latEndPoint);
        builder.append(", longEndPoint=");
        builder.append(longEndPoint);
        builder.append(", endPoint=");
        builder.append(endPoint);
        builder.append(", dateFrom=");
        builder.append(dateFrom);
        builder.append(", dateTo=");
        builder.append(dateTo);
        builder.append(", priceFrom=");
        builder.append(priceFrom);
        builder.append(", priceTo=");
        builder.append(priceTo);
        builder.append(", maxDistance=");
        builder.append(maxDistance);
        builder.append("]");
        return builder.toString();
    }

}
