/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.restservice.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>POJO for indicating empty results.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 26.01.2015
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TripQueryNoResults extends TripQueryResults
{
    protected boolean noTrips;    
    protected String message;
    
    public TripQueryNoResults()
    {
        this.type = Type.NO_RESULT;
    }

    /**
     * @return the noTrips
     */
    public boolean isNoTrips()
    {
        return noTrips;
    }

    /**
     * @param noTrips the noTrips to set
     */
    public void setNoTrips(boolean noTrips)
    {
        this.noTrips = noTrips;
    }

    /**
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message)
    {
        this.message = message;
    }
}
