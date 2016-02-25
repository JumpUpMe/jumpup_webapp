/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.graph.shortest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.htw.fb4.imi.jumpup.trip.graph.Edge;
import de.htw.fb4.imi.jumpup.trip.graph.Graph;
import de.htw.fb4.imi.jumpup.trip.graph.Path;
import de.htw.fb4.imi.jumpup.trip.graph.Vertex;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.util.math.CoordinateUtil;
import de.htw.fb4.imi.jumpup.util.math.Coordinates;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 09.08.2015
 *
 */
public class DijkstraSinglePairTest
{
    private Graph graph;
    private Vertex origin;
    private Vertex destination;
    private Vertex connectingVertex1;

    @Before
    public void before()
    {
        this.graph = buildTestGraph();
    }
    
    @After
    public void after()
    {
        this.graph = null;
        this.origin = null;
        this.destination = null;
    }
    
    protected Graph buildTestGraph()
    {
        Graph g = new Graph();
        
        fillRouteFromKarlshorstToSchoeneweide(g);
        fillRouteFromSchoenweideToHtw(g);       
        
        return g;
    }

    protected void fillRouteFromKarlshorstToSchoeneweide(Graph g)
    {
        Trip trip = new Trip();        
        trip.setStartpoint("Karlhorst");
        trip.setEndpoint("Schöneweide");
        
        // Aristotelessteig
        this.origin = new Vertex(CoordinateUtil.newCoordinatesBy("52.491587,13.521616"));
        this.origin.getTrips().add(trip);
        // Römerweg corner Treskowallee
        Vertex b = new Vertex(CoordinateUtil.newCoordinatesBy("52.491979,13.526595"));
        b.getTrips().add(trip);
        // Karlshorst center
        Vertex c = new Vertex(CoordinateUtil.newCoordinatesBy("52.486883,13.52711"));
        c.getTrips().add(trip);
        // Karlshorst Trabrennbahn
        Vertex d = new Vertex(CoordinateUtil.newCoordinatesBy("52.477553,13.523977"));
        d.getTrips().add(trip);
        // Edisonstr / Wilhelminenhofstr
        this.connectingVertex1 = new Vertex(CoordinateUtil.newCoordinatesBy("52.46205,13.514063"));
        connectingVertex1.getTrips().add(trip);
        // S-Bahn Schöneweide
        Vertex f = new Vertex(CoordinateUtil.newCoordinatesBy("52.455905,13.510416"));
        f.getTrips().add(trip);
        
        // edges
        Edge ab = new Edge(this.origin, b, CoordinateUtil.calculateDistanceBetween(this.origin.getCoordinates(), b.getCoordinates()));
        Edge bc = new Edge(b, c, CoordinateUtil.calculateDistanceBetween(b.getCoordinates(), c.getCoordinates()));
        Edge cd = new Edge(c, d, CoordinateUtil.calculateDistanceBetween(c.getCoordinates(), d.getCoordinates()));
        Edge de = new Edge(d, connectingVertex1, CoordinateUtil.calculateDistanceBetween(d.getCoordinates(), connectingVertex1.getCoordinates()));
        Edge ef = new Edge(connectingVertex1, f, CoordinateUtil.calculateDistanceBetween(connectingVertex1.getCoordinates(), f.getCoordinates()));
        
        g.addVertex(origin)
         .addVertex(b)
         .addVertex(c)
         .addVertex(d)
         .addVertex(connectingVertex1)
         .addVertex(f);
        
        g.addEdge(ab)
         .addEdge(bc)
         .addEdge(cd)
         .addEdge(de)
         .addEdge(ef);        
    }
    
    protected void fillRouteFromSchoenweideToHtw(Graph g)
    {
        Trip trip = new Trip();    
        trip.setStartpoint("Schöneweide");
        trip.setEndpoint("HTW Berlin");
        
        // Nalepastr
        Vertex a = new Vertex(CoordinateUtil.newCoordinatesBy("52.467985,13.504794"));
        a.getTrips().add(trip);
        // Edisonstr / Wilhelminenhofstr
        connectingVertex1.getTrips().add(trip);
        // Wilhelminenhofstr 78
        Vertex c = new Vertex(CoordinateUtil.newCoordinatesBy("52.460664,13.521402"));
        c.getTrips().add(trip);
        // HTW Wilhelminenhofstr
        this.destination = new Vertex(CoordinateUtil.newCoordinatesBy("52.457369,13.528097"));
        destination.getTrips().add(trip);
        
        // edges
        Edge ab = new Edge(a, connectingVertex1, CoordinateUtil.calculateDistanceBetween(a.getCoordinates(), connectingVertex1.getCoordinates()));
        Edge bc = new Edge(connectingVertex1, c, CoordinateUtil.calculateDistanceBetween(connectingVertex1.getCoordinates(), c.getCoordinates()));
        Edge cd = new Edge(c, this.destination, CoordinateUtil.calculateDistanceBetween(c.getCoordinates(), this.destination.getCoordinates()));
  
        g.addVertex(a)
        .addVertex(c)
        .addVertex(destination);
        
        g.addEdge(ab)
        .addEdge(bc)
        .addEdge(cd);        
    }
    
    @Test
    public void testFindShortestPath() throws PathNotFoundException
    {
        Routable shortestPathStrategy = new DijkstraSinglePair();
        
        Path foundPath = shortestPathStrategy.findShortestPath(this.graph, this.origin, this.destination);
        
        assertEquals(2, foundPath.getTripsOnPath().size());
        assertEquals(1, foundPath.getIntersectionsOnPath().size());
    }
    
    @Test
    public void haversineTest()
    {
        Coordinates one = CoordinateUtil.newCoordinatesBy("55.11111,55.11111");
        Coordinates two = CoordinateUtil.newCoordinatesBy("55.11111,55.22222");
        
        System.out.println(CoordinateUtil.calculateDistanceBetween(one, two));
    }

}
