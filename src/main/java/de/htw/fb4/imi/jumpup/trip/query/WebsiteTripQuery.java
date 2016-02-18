/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.translate.Translatable;
import de.htw.fb4.imi.jumpup.trip.TripDAO;
import de.htw.fb4.imi.jumpup.trip.ajax.QueryResultFactory;
import de.htw.fb4.imi.jumpup.trip.ajax.model.SingleTripQueryResult;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryNoResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.trip.graph.Path;
import de.htw.fb4.imi.jumpup.trip.graph.shortest.PathNotFoundException;
import de.htw.fb4.imi.jumpup.trip.query.filter.DirectTripsSearchFilterChain;
import de.htw.fb4.imi.jumpup.trip.query.filter.OverlappingTripsFilter;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 19.01.2015
 *
 */
@Stateful(name = BeanNames.WEBSITE_TRIP_QUERY)
public class WebsiteTripQuery implements TripQueryMethod
{
    @PersistenceUnit(unitName = PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManagerFactory entityManagerFactory;
    
    @Inject
    protected DirectTripsSearchFilterChain tripSearchFilterChain;
    
    @Inject
    protected OverlappingTripsFilter overlappingPartialTripsFilter;
    
    @Inject
    protected LoginSession loginSession;    
    
    @Inject
    protected Translatable translator;
    
    @Inject
    protected QueryResultFactory queryResultFactory;
    
    @Inject
    protected TripDAO tripDao;
    
    protected List<String> messages;

    /**
     * Create fresh entity manager.
     * @return
     */
    protected EntityManager getFreshEntityManager()
    {
        EntityManager em = this.entityManagerFactory.createEntityManager();

        return em;
    }    
    
    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.ErrorPrintable#hasError()
     */
    public boolean hasError()
    {
        return !this.messages.isEmpty();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.ErrorPrintable#getErrors()
     */
    public String[] getErrors()
    {
        return this.messages.toArray(new String[this.messages.size()]);
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.ErrorPrintable#getSingleErrorString()
     */
    public String getSingleErrorString()
    {
        // TODO Auto-generated method stub
        return null;
    }
   

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.TripQueryMethod#searchForTrips(de.htw.fb4.imi.jumpup.trip.query.TripSearchCriteria)
     */
    public TripQueryResults searchForTrips(TripSearchCriteria tripSearchModel)
    {
        // exclude passenger's own trip first
        tripSearchModel.setPassenger(this.getCurrentlyLoggedInUser());
        List<Trip> matchedTrips = this.tripDao.getByCriteria(tripSearchModel);
        
        Application.log("got trips from HQL: " + matchedTrips, LogType.DEBUG, getClass());
        
        // first, filter for direct trips
        List<Trip> filteredTrips = this.triggerDirectTripsFilteringChain(tripSearchModel, matchedTrips);
        
        Application.log("filtered trips: " + filteredTrips, LogType.DEBUG, getClass());
        
        if (0 != filteredTrips.size()) {
            // direct trips were found
            return this.toQueryResultList(filteredTrips, tripSearchModel);
        }
        
        // no direct trips were found, so start to find overlapping partial trips
        try {
            long startTime = System.currentTimeMillis();
            Path overlappingPartialTrips = this.findOverlappingPartialTrips(tripSearchModel, matchedTrips);
            long stopTime = System.currentTimeMillis();
            Application.log("WebsiteTripQuery: runtime for overlapping partial trips in ms: " + (stopTime - startTime), LogType.DEBUG, getClass()  );
            return this.toQueryResultList(overlappingPartialTrips, tripSearchModel);
        } catch (PathNotFoundException e) {
            // no path found at all
            return this.getNoTripsResult();
        }
    }  

    private Path findOverlappingPartialTrips(
            TripSearchCriteria tripSearchCriteria, List<Trip> matchedTrips) throws PathNotFoundException
    {
        if (null == matchedTrips) {
            throw new NullPointerException("matchedTrips must not be null");
        }
        
        this.overlappingPartialTripsFilter.setCriteria(tripSearchCriteria);
        return this.overlappingPartialTripsFilter.applyOverlappingTripsFilter(matchedTrips);      
    }

    private TripQueryResults toQueryResultList(
            List<Trip> filteredTrips, TripSearchCriteria tripSearchModel)
    {
        List<SingleTripQueryResult> list = new ArrayList<SingleTripQueryResult>();
        
        for (Trip filteredTrip : filteredTrips) {
            list.add(this.toSingleTripQueryResult(filteredTrip, tripSearchModel));
        }        
        
        return queryResultFactory.newTripQueryResults(list);
    }
    
    private TripQueryResults toQueryResultList(Path overlappingPartialTrips,
            TripSearchCriteria tripSearchModel)
    {
        return queryResultFactory.newOverlappingPartialTripsResult(overlappingPartialTrips, tripSearchModel);
    }

    private SingleTripQueryResult toSingleTripQueryResult(Trip filteredTrip, TripSearchCriteria tripSearchModel)
    {
        return queryResultFactory.newSingleTripQueryResult(filteredTrip, tripSearchModel);
    }

    private List<Trip> triggerDirectTripsFilteringChain(TripSearchCriteria tripSearchCriteria, List<Trip> matchedTrips)
    {
        if (null == matchedTrips) {
            return new ArrayList<Trip>();
        }
        
        this.tripSearchFilterChain.setCriteria(tripSearchCriteria);
        return this.tripSearchFilterChain.applyFilter(matchedTrips);        
    }

    /**
     * Get currently logged-in user.
     * @return
     */
    private User getCurrentlyLoggedInUser()
    {
        return loginSession.getCurrentUser();
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.TripQueryMethod#getNoTripsResult()
     */
    public TripQueryNoResults getNoTripsResult()
    {
        return queryResultFactory.newNoTripsResult(this.translator.translate("Sorry, we didn't find any trips that matched your criteria. Please try again and be less restrictive."));
    }
}
