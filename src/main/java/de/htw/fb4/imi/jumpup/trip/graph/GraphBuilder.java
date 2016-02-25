/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.graph;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;

import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.util.math.CoordinateUtil;
import de.htw.fb4.imi.jumpup.util.math.Coordinates;

/**
 * <p>Helper to build a {@link Graph} instance from given {@link Trip} list.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 09.08.2015
 *
 */
@Stateless(name = BeanNames.GRAPH_BUILDER)
public class GraphBuilder
{

    /**
     * Build a {@link Graph} instance. 
     * Therfore, take each origin, waypoint and destination of a trip and add {@link Vertex} and {@link Edge} instances.
     * 
     * This method therefore structures the waypoint data.
     * @param trips
     * @return
     */
    public Graph buildGraphFromTripList(List<Trip> trips)
    {
        Graph g = new Graph();
        
        this.addVerticesAndEdges(g, trips);
        
        return g;
    }

    private void addVerticesAndEdges(Graph g, List<Trip> trips)
    {
        for (Trip trip : trips) {
            Vertex origin = this.addAndGetOriginVertex(g, trip);          
            
            Vertex lastVertex = this.addVerticesAndEdgeForEachWaypoint(g, 
                    trip, 
                    CoordinateUtil.parseCoordinateSetBy(trip.getOverViewPath()),
                    origin);
            
            this.addDestinationVertexAndEdge(g, trip, lastVertex);
        }        
    }   

    protected Vertex createOrGetExistingVertex(Graph g, Coordinates coordinates)
    {
        Vertex a = new Vertex(coordinates);         
        
        if (!g.contains(a)) {
            // only add the vertex if it isn't contained already (might be contained already on intersection waypoints)
            g.addVertex(a);
        } else {
            // get the vertex from the graph to continue processing
            a = g.getVerticeById(a.getId());
        }
        
        return a;
    }
    
    private Vertex addAndGetOriginVertex(Graph g, Trip trip)
    {
        Vertex origin = createOrGetExistingVertex(g, new Coordinates(trip.getLatStartpoint(), trip.getLongStartpoint()));
        origin.getTrips().add(trip);
        
        return origin;
    } 

    private Vertex addVerticesAndEdgeForEachWaypoint(Graph g, Trip trip, Set<Coordinates> waypoints, Vertex origin)
    {
        Vertex previousVertex = origin;
        for (Coordinates waypoint : waypoints) {
            Vertex a = createOrGetExistingVertex(g, waypoint);
            
            // add the trip to the vertex to allow path & intersection info
            a.getTrips().add(trip);   
            
            // add edge between current and previous vertex
            Edge e = new Edge(previousVertex, a, CoordinateUtil.calculateDistanceBetween(previousVertex.getCoordinates(), a.getCoordinates()));   
            g.addEdge(e);
            
            previousVertex = a;
        }        
        
        return previousVertex;
    }
    
    private void addDestinationVertexAndEdge(Graph g, Trip trip, Vertex lastVertex)
    {
        Vertex destination = createOrGetExistingVertex(g, new Coordinates(trip.getLatEndpoint(), trip.getLongEndpoint()));
        destination.getTrips().add(trip);
        
        // add edge from lastVertex to destination
        Edge a = new Edge(lastVertex, 
                destination, 
                CoordinateUtil.calculateDistanceBetween(lastVertex.getCoordinates(), destination.getCoordinates()));
        
        g.addEdge(a);
    }   
}
