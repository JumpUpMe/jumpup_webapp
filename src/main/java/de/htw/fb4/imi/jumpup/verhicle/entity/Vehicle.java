/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.verhicle.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import de.htw.fb4.imi.jumpup.entity.AbstractEntity;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

/**
 * <p>
 * Basic Entity for Vehicles.
 * </p>
 * 
 * @author <a href="mailto:m_seidler@hotmail.de">Marco Seidler</a>
 * @since 03.01.2015
 * 
 */

@Entity
public class Vehicle extends AbstractEntity
{

    /**
     * 
     */
    private static final long serialVersionUID = 6527900884514589268L;

    @OneToMany
    @JoinColumn(name = "vehicle")
    protected List<Trip> trips;

    /**
     * @return the trips
     */
    public List<Trip> getTrips()
    {
        return trips;
    }

    /**
     * @param trips the trips to set
     */
    public void setTrips(List<Trip> trips)
    {
        this.trips = trips;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((trips == null) ? 0 : trips.hashCode());
        return result;
    }

    /* (non-Javadoc)
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
        Vehicle other = (Vehicle) obj;
        if (trips == null) {
            if (other.trips != null)
                return false;
        } else if (!trips.equals(other.trips))
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Vehicle [getTrips()=");
        builder.append(getTrips());
        builder.append(", hashCode()=");
        builder.append(hashCode());
        builder.append("]");
        return builder.toString();
    }
    
    
}
