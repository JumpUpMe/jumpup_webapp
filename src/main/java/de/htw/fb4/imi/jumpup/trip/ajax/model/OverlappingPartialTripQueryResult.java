/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ajax.model;

import java.util.Set;

import de.htw.fb4.imi.jumpup.trip.graph.Vertex;

/**
 * <p>Record returned via AJAX on overlapping partial trip query results.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 09.08.2015
 *
 */
public class OverlappingPartialTripQueryResult extends TripQueryResults
{
    public class Intersections {
        protected Set<Vertex> intersectionVertices;

        /**
         * @return the intersectionVertices
         */
        public Set<Vertex> getIntersectionVertices()
        {
            return intersectionVertices;
        }

        /**
         * @param intersectionVertices the intersectionVertices to set
         */
        public void setIntersectionVertices(Set<Vertex> intersectionVertices)
        {
            this.intersectionVertices = intersectionVertices;
        }       
        
    }
    
    protected Set<Vertex> intersections;
    protected String message;
    
    public OverlappingPartialTripQueryResult()
    {
        this.type = Type.MULTIPLE_PARTIAL_TRIP_RESULT;
    }

    /**
     * @return the intersections
     */
    public Set<Vertex> getIntersections()
    {
        return intersections;
    }

    /**
     * @param intersections the intersections to set
     */
    public void setIntersections(Set<Vertex> intersections)
    {
        this.intersections = intersections;
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
