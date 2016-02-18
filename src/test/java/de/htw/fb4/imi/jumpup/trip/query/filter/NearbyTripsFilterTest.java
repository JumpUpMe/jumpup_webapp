/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query.filter;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;

/**
 * <p>Test of {@link NearbyTripsFilter} to find passenger's trips that are near the driver's route.</p>
 * 
 * <p>This test works parameterized by a test dataset which consists of expected {@link Trip} lists, {@link TripSearchCriteria} simulating a passenger's trip search and {@link Trip} entities that could have been loaded from a database.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 24.01.2015
 *
 */
@RunWith(Parameterized.class)
public class NearbyTripsFilterTest
{    
    private List<Trip> givenOriginalTripList;
    private TripSearchCriteria givenTripSearchCriteria;
    private List<Trip> expectedTripList;
    protected static NearbyTripsFilter TRIP_FILTER;
    

    public NearbyTripsFilterTest(List<Trip> givenOriginalTripList,  TripSearchCriteria givenTripSearchCriteria, List<Trip> expectedTripList)
    {
        this.givenOriginalTripList = givenOriginalTripList;
        this.givenTripSearchCriteria = givenTripSearchCriteria;
        this.expectedTripList = expectedTripList;
    }
    
    @BeforeClass
    public static void initializeTests()
    {
        TRIP_FILTER = new NearbyTripsFilter();
    }
    
    @Test
    public void testFilterNearby()
    {
        TRIP_FILTER.setCriteria(this.givenTripSearchCriteria);
        List<Trip> resultingTrips = TRIP_FILTER.applyFilter(this.givenOriginalTripList);
        
        for (Trip trip : resultingTrips) {
            boolean contained = false;
            
            for (Trip expectedTrip : expectedTripList) {
                if (expectedTrip.getStartpoint() == trip.getStartpoint()
                        && expectedTrip.getEndpoint() == trip.getEndpoint()) {
                    contained = true;
                }
            }
            
            assertTrue(contained);
        }    
    }
    
    
    /*
     * ############ TEST DATA ###############
     */   
    
    @Parameters
    public static Iterable<Object[]> data() 
    {
        return Arrays.asList(
                new Object[][] {
                        // test 1: given several trips including one from Berlin -> Leipzig and a search query for Berlin -> Leipzig, than expect Berlin -> Leipzig as only element in trip list
                        {getOriginalTripList1(), getTripSearchCriteria1(), getExpectedTripList1()},  
                        // test 2: given several trips including one from Berlin -> Trier and a search query for Leipzig -> Erfurt, than expect Leipzig -> Erfurt and Berlin -> Trier as results
                        {getOriginalTripList2(), getTripSearchCriteria2(), getExpectedTripList2()},  
                        // test 3: given several trips and search from Weimar -> Trier, expect Berlin -> Trier and Weimar -> Trier to be in result list
                        {getOriginalTripList3(), getTripSearchCriteria3(), getExpectedTripList3()},  
                        // test 4: basically same as test 2, but maxDistance is set to 0, then Leipzig -> Erfurt should be the only resulting route
                        {getOriginalTripList4(), getTripSearchCriteria4(), getExpectedTripList4()}, 
                });
    }

    private static List<Trip> getExpectedTripList1()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromBerlinToLeipzig());
        
        return tripList;
    }

    private static TripSearchCriteria getTripSearchCriteria1()
    {
        // max distance to trips = 10km
        return FilterTestHelper.getTripSearchFromBerlinToLeipzig(10);
    }

    private static List<Trip>  getOriginalTripList1()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromBerlinToLeipzig());
        tripList.add(FilterTestHelper.getTripFromLeipzigToErfurt());
        tripList.add(FilterTestHelper.getTripFromWeimarToTrier());
        
        return tripList;
    }
    
    private static List<Trip> getExpectedTripList2()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromLeipzigToErfurt());
        tripList.add(FilterTestHelper.getTripFromBerlinToTrier());
        
        return tripList;
    }
    
    private static TripSearchCriteria getTripSearchCriteria2()
    {
        // max distance to trips = 10km
        return FilterTestHelper.getTripSearchFromLeipzigToErfurt(20);
    }
    
    private static List<Trip>  getOriginalTripList2()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromBerlinToLeipzig());
        tripList.add(FilterTestHelper.getTripFromLeipzigToErfurt());
        tripList.add(FilterTestHelper.getTripFromWeimarToTrier());
        tripList.add(FilterTestHelper.getTripFromBerlinToTrier());
        
        return tripList;
    }
    
    private static List<Trip> getExpectedTripList3()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromWeimarToTrier());
        tripList.add(FilterTestHelper.getTripFromBerlinToTrier());
        
        return tripList;
    }
    
    private static TripSearchCriteria getTripSearchCriteria3()
    {
        // max distance to trips = 10km
        return FilterTestHelper.getTripSearchFromWeimarToTrier(20);
    }
    
    private static List<Trip>  getOriginalTripList3()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromBerlinToLeipzig());
        tripList.add(FilterTestHelper.getTripFromLeipzigToErfurt());
        tripList.add(FilterTestHelper.getTripFromWeimarToTrier());
        tripList.add(FilterTestHelper.getTripFromBerlinToTrier());
        
        return tripList;
    }
    
    private static List<Trip> getExpectedTripList4()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromLeipzigToErfurt());
        
        return tripList;
    }
    
    private static TripSearchCriteria getTripSearchCriteria4()
    {
        // max distance to trips = 10km
        return FilterTestHelper.getTripSearchFromLeipzigToErfurt(1);
    }
    
    private static List<Trip>  getOriginalTripList4()
    {
        List<Trip> tripList = new ArrayList<Trip>();
        
        tripList.add(FilterTestHelper.getTripFromBerlinToLeipzig());
        tripList.add(FilterTestHelper.getTripFromLeipzigToErfurt());
        tripList.add(FilterTestHelper.getTripFromWeimarToTrier());
        tripList.add(FilterTestHelper.getTripFromBerlinToTrier());
        
        return tripList;
    }

}
