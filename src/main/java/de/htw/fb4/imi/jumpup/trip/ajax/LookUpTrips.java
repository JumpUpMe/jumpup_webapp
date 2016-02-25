/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ajax;

import javax.faces.bean.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.ejb.query.TripQueryMethod;

/**
 * <p>
 * Base class for Rest-Service management via Jax-RS.
 * </p>
 * 
 * @author <a href="mailto:m_seidler@hotmail.de">Marco Seidler</a>
 * @since 24.01.2015
 * 
 */
@Named
@Path("/lookuptrips")
@RequestScoped
public class LookUpTrips
{
    @Inject
    protected TripQueryMethod tripQueryMethod;

    /**
     * 
     * @param tripCriteria
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchForTrip(final TripSearchCriteria tripCriteria)
    {
        Application.log("Resource: TripSearchCriteria= " + tripCriteria,
                LogType.DEBUG, getClass());

        try {
            TripQueryResults matchedTrips = tripQueryMethod.searchForTrips(tripCriteria);
            
            // direct trips, multiple partial trips or no trips found
            return Response
                    .ok(matchedTrips)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception e) {
            Application.log("searchForTrip(): exception " + e.getMessage() + "\nStack trace:\n" + ExceptionUtils.getFullStackTrace(e),
                    LogType.ERROR, getClass());
            
            return Response
                    .status(Status.INTERNAL_SERVER_ERROR)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                    .build();
        }
    }
    

}
