/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query.filter;

import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.graph.Graph;
import de.htw.fb4.imi.jumpup.trip.graph.GraphBuilder;
import de.htw.fb4.imi.jumpup.trip.graph.Path;
import de.htw.fb4.imi.jumpup.trip.graph.Vertex;
import de.htw.fb4.imi.jumpup.trip.graph.shortest.PathNotFoundException;
import de.htw.fb4.imi.jumpup.trip.graph.shortest.Routable;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.util.math.Coordinates;

/**
 * <p>
 * Convert trips from HQL into a graph structure, apply the shortest path algorithm and return a {@link Path} instance.</p>
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 09.08.2015
 *
 */
@Stateful(name = BeanNames.OVERLAPPING_TRIP_SEARCH_FILTER)
public class OverlappingTripsFilter extends AbstractTripFilter
{
    @Inject
    private GraphBuilder builder;
    
    @Inject
    private Routable shortestPathStrategy;

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.filter.TripFilter#applyFilter(java.util.List)
     */
    @Override
    public List<Trip> applyFilter(final List<Trip> givenTrips)
    {
       throw new UnsupportedOperationException("This method is not supported!");
    }

    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.filter.AbstractTripFilter#applyOverlappingTripsFilter(java.util.List)
     */
    public Path applyOverlappingTripsFilter(final List<Trip> givenTrips) throws PathNotFoundException
    {       
        Graph tripsGraph = this.structureToTripsGraph(givenTrips);
        
        Path shortestPath = this.findShortestPath(tripsGraph);
        
        return shortestPath;
    }

    private Path findShortestPath(Graph tripsGraph) throws PathNotFoundException
    {
        Vertex passengersOrigin = this.getPassengersOriginVertex(tripsGraph);
        Vertex passengersDestination = this.getPassengersDestinationVertex(tripsGraph);
        
        return this.shortestPathStrategy.findShortestPath(tripsGraph, passengersOrigin, passengersDestination);
    }
    
    private Vertex getPassengersOriginVertex(Graph tripsGraph) throws PathNotFoundException
    {
        // build a vertex for the passenger's destination, check if its contained in the graph
        Vertex passengersOrigin = new Vertex(
                new Coordinates(tripSearchCriteria.getLatStartPoint(), tripSearchCriteria.getLongStartPoint()));
        
        if (!tripsGraph.contains(passengersOrigin)) {
            // it's not, so no trip is available
            throw new PathNotFoundException();
        }
        
        return tripsGraph.getVerticeById(passengersOrigin.getId());
    }

    private Vertex getPassengersDestinationVertex(Graph tripsGraph) throws PathNotFoundException
    {
        // build a vertex for the passenger's destination, check if its contained in the graph
        Vertex passengersDestination = new Vertex(
                new Coordinates(tripSearchCriteria.getLatEndPoint(), tripSearchCriteria.getLongEndPoint()));
        
        if (!tripsGraph.contains(passengersDestination)) {
            // it's not, so no trip is available
            throw new PathNotFoundException();
        }
        
        return tripsGraph.getVerticeById(passengersDestination.getId());
    }  

    private Graph structureToTripsGraph(List<Trip> givenTrips)
    {
        Graph resultingGraph = builder.buildGraphFromTripList(givenTrips);
        
        Application.log("Created graph with " + resultingGraph.getNumberOfVertices() + " vertices and " + resultingGraph.getNumberOfEdges() + " edges", LogType.DEBUG, getClass());
        
        return resultingGraph;
    }
}
