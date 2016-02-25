/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.graph;

import java.util.HashSet;
import java.util.Set;

import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.util.math.Coordinates;

/**
 * <p>
 * Vertex class./p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 08.08.2015
 *
 */
public class Vertex
{
    /**
     * Tolerance factor: number of digits behind comma in a coordinate that will be floored.
     * 
     * To increase the probability of finding overlapping partial {@link Trip}, try to reduce this value.
     * Otherwise, try to increase it.
     * 
     * The number of digits is 10 ^ (number of digits) = TOLERANCE_FACTOR
     */    
    public static final int TOLERANCE_FACTOR = 1000;
    
    protected final Coordinates coordinates;
    protected Set<Trip> trips;
    protected double id = -1.0;

    /**
     * Create a vertex that is part of multiple trips.
     * @param coordinates
     * @param trips
     */
    public Vertex(Coordinates coordinates)
    {
        super();
        this.coordinates = coordinates;
        this.trips = new HashSet<Trip>();
    }
    
    /**
     * Get all trips that this vertex is contained in. 
     * Use this method from outside to manage a trip vertices by adding the trip to the vertex.
     * @return
     */
    public Set<Trip> getTrips()
    {
        return this.trips;
    }

    /**
     * Get the vertex / node ID.
     * Can be imagined as vertex label.
     * @return
     */
    public double getId()
    {
        if (-1 == this.id) {
            this.calculateId();
        }

        return this.id;
    }

    /**
     * Calculate the vertex identity. 
     * 
     * Therefore, floor the coordinates to a special number of digits which can be set within TOLERANCE_FACTOR constant.
     */
    private void calculateId()
    {
        // floor the coordinate values to increase the intersection probability for multiple partial trips
        double latitude = this.coordinates.getLatitudeDegrees();
        double longitude = this.coordinates.getLongitudeDegrees();
        
        double toleranceLatitude =  Math.floor(latitude * TOLERANCE_FACTOR) / TOLERANCE_FACTOR;
        double toleranceLongitude =  Math.floor(longitude * TOLERANCE_FACTOR) / TOLERANCE_FACTOR;
        
        this.id = toleranceLatitude + toleranceLongitude;
    }

    /**
     * 
     * @return
     */
    public Coordinates getCoordinates()
    {
        return this.coordinates;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((coordinates == null) ? 0 : coordinates.hashCode());
        long temp;
        temp = Double.doubleToLongBits(id);
        result = prime * result + (int) (temp ^ (temp >>> 32));
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
        Vertex other = (Vertex) obj;
        if (coordinates == null) {
            if (other.coordinates != null)
                return false;
        } else if (!coordinates.equals(other.coordinates))
            return false;
        if (Double.doubleToLongBits(id) != Double.doubleToLongBits(other.id))
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
        builder.append("Vertex [coordinates=");
        builder.append(coordinates);
        builder.append(", trips=");
        builder.append(trips);
        builder.append(", id=");
        builder.append(id);
        builder.append("]");
        return builder.toString();
    }  
}
