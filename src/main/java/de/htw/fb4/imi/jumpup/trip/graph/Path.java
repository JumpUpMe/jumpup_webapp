/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.graph;

import java.util.HashSet;
import java.util.Set;

import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

/**
 * <p>A path is a set of {@link Vertex} instances in a {@link Graph}.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 09.08.2015
 *
 */
public class Path
{
    protected final Graph graph;
    protected final Set<Vertex> vertices;
    protected Set<Trip> trips;
    protected Set<Vertex> intersections;
    
    public Path(Graph graph, Set<Vertex> vertices) 
    {
        this.graph = graph;
        this.vertices = vertices;      
    }

    /**
     * Get the {@link Trip} instances that are covered by the path segment. 
     * @return
     */
    public Set<Trip> getTripsOnPath()
    {        
       if (null == this.trips) {
           this.buildTripsAndIntersectionSet();
       }
       
       return this.trips;
    }

    private void buildTripsAndIntersectionSet()
    {        
        this.trips = new HashSet<Trip>();
        this.intersections = new HashSet<Vertex>();
        
        for (Vertex vertex : this.vertices) {
            Set<Trip> vertexInTrips = vertex.getTrips();
            int count = 0;
            
            for (Trip trip : vertexInTrips) {
                if (!this.trips.contains(trip)) {
                    this.trips.add(trip);
                }
                
                if (vertexInTrips.size() > 1 && count < 2) {
                    // this vertex is contained in more than one trip, so add it to intersection set
                    this.intersections.add(vertex);
                }
                
                count++;
            }
        }
    }
    
    /**
     * Get the intersections between {@link Trip} instances on this path.
     * Therefore, return a set of two-dimensional {@link Vertex} arrays.
     * Each two-dimensional array represents a single intersection between two {@link Trip} instances.
     * 
     * @return
     */
    public Set<Vertex> getIntersectionsOnPath()
    {
        if (null == this.intersections) {
            this.buildTripsAndIntersectionSet();
        }
        
        return this.intersections;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((graph == null) ? 0 : graph.hashCode());
        result = prime * result
                + ((intersections == null) ? 0 : intersections.hashCode());
        result = prime * result + ((trips == null) ? 0 : trips.hashCode());
        result = prime * result
                + ((vertices == null) ? 0 : vertices.hashCode());
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
        Path other = (Path) obj;
        if (graph == null) {
            if (other.graph != null)
                return false;
        } else if (!graph.equals(other.graph))
            return false;
        if (intersections == null) {
            if (other.intersections != null)
                return false;
        } else if (!intersections.equals(other.intersections))
            return false;
        if (trips == null) {
            if (other.trips != null)
                return false;
        } else if (!trips.equals(other.trips))
            return false;
        if (vertices == null) {
            if (other.vertices != null)
                return false;
        } else if (!vertices.equals(other.vertices))
            return false;
        return true;
    }    
}
