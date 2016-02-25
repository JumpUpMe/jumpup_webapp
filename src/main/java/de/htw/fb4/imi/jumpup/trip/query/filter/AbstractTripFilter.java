/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.query.filter;

import java.util.List;

import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.graph.Path;
import de.htw.fb4.imi.jumpup.trip.graph.shortest.PathNotFoundException;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;

/**
 * <p>The AbstractFilter realizes the decorator.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 22.01.2015
 *
 */
public abstract class AbstractTripFilter implements TripFilter
{
    protected TripFilter decoratingTripFilter;
    protected TripSearchCriteria tripSearchCriteria;
    
    public AbstractTripFilter(TripFilter decorating)
    {
        super();
        this.decoratingTripFilter = decorating;
    }
    
    public AbstractTripFilter()
    {
        super();
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.filter.TripFilter#applyFilter(java.util.List)
     */
    @Override
    public List<Trip> applyFilter(final List<Trip> givenTrips)
    {
        // delegate to decorating filter if given
        if (null != this.decoratingTripFilter) {
            return this.decoratingTripFilter.applyFilter(givenTrips);
        }
        
        return givenTrips;
    }
    
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.filter.TripFilter#applyOverlappingTripsFilter(java.util.List)
     */
    public Path applyOverlappingTripsFilter(final List<Trip> givenTrips) throws PathNotFoundException
    {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.query.filter.TripFilter#setCriteria(de.htw.fb4.imi.jumpup.trip.query.TripSearch)
     */
    public void setCriteria(TripSearchCriteria tripSearchCriteria)
    {
        // delegate to decorating filter if given
        if (null != this.decoratingTripFilter) {
            this.decoratingTripFilter.setCriteria(tripSearchCriteria);
        }
        
        this.tripSearchCriteria = tripSearchCriteria;        
    }

}
