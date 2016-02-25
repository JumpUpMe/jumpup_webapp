/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.graph;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 09.08.2015
 *
 */
public class GraphBuilderTest
{
    private List<Trip> trips;
    
    @Before
    public void before()
    {
        this.trips = new ArrayList<Trip>();
        
        this.givenATripFromKarlshorstToSchoeneweide();
        this.givenATripFromSchoeneweideToHtw();
    }

    @After
    public void after()
    {
        this.trips = null;
    }
    
    private void givenATripFromKarlshorstToSchoeneweide()
    {
       Trip t = new Trip();
       
       // Aristotelessteig, Karlshorst
       t.setLatStartpoint(52.491587);
       t.setLongStartpoint(13.521616);
       
       t.setOverViewPath("52.491979,13.526595;52.486883,13.52711;52.477553,13.523977;52.46205,13.514063");
       
       // S-Bahn Schöneweide
       t.setLatStartpoint(52.455905);
       t.setLongEndpoint(13.510416);
       
       trips.add(t);
    }
    
    private void givenATripFromSchoeneweideToHtw()
    {
        Trip t = new Trip();
        
        // Nalepastr
        t.setLatStartpoint(52.467985);
        t.setLongStartpoint(13.504794);
        
        
        t.setOverViewPath("52.46205,13.514063;52.460664,13.521402");
        
        //HTW Wilhelminenhofstr
        t.setLatStartpoint(52.457369);
        t.setLongEndpoint(13.528097);
        
        trips.add(t);
    }

    @Test
    public void test()
    {
       GraphBuilder builder = new GraphBuilder();
       Graph resultingGraph = builder.buildGraphFromTripList(this.trips);
       
       assertEquals(8, resultingGraph.getNumberOfVertices());
       assertEquals(8, resultingGraph.getNumberOfEdges());
    }
}
