/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.rest;

import java.util.List;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.rest.controller.SecuredRestController;
import de.htw.fb4.imi.jumpup.rest.response.builder.IResponseEntityBuilder;
import de.htw.fb4.imi.jumpup.rest.response.builder.ISuccessResponseEntityBuilder;
import de.htw.fb4.imi.jumpup.trip.TripDAO;
import de.htw.fb4.imi.jumpup.trip.TripRequest;
import de.htw.fb4.imi.jumpup.trip.creation.TripManagementMethod;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.trip.rest.model.TripEntityMapper;
import de.htw.fb4.imi.jumpup.trip.rest.model.TripWebServiceModel;
import de.htw.fb4.imi.jumpup.trip.util.IMessages;
import de.htw.fb4.imi.jumpup.util.model.Pagination;
import de.htw.fb4.imi.jumpup.validation.ValidationException;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 25.11.2015
 *
 */
public class TripsBaseController extends SecuredRestController<TripWebServiceModel>
{
    public static final String PATH = "/trip";
    private static final String PATH_PARAM_TRIP_ID = "tripId";
    protected static final String ENTITY_NAME = "trip";

    @Inject
    protected TripDAO tripDAO;

    @Inject
    protected TripManagementMethod tripManagement;

    @Inject
    protected TripRequest tripRequest;

    @Inject
    protected IResponseEntityBuilder responseEntityBuilder;

    @Inject
    protected TripEntityMapper entityMapper;

    private TripManagementMethod getTripManagementMethod()
    {
        return this.tripManagement;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers,
            @QueryParam(QUERY_PARAM_LIMIT) Integer limit,
            @QueryParam(QUERY_PARAM_OFFSET) Integer offset)
    {
        Response response = super.get(headers);

        if (null != response) {
            return response;
        }

        if (null == limit) {
            limit = Pagination.NO_LIMIT;
        }

        if (null == offset) {
            offset = Pagination.NO_OFFSET;
        }

        return this
                .tryToLoadUsersTrips(Pagination.fromParameters(limit, offset));
    }

    private Response tryToLoadUsersTrips(Pagination pagination)
    {
        List<Trip> offeredTrips = this.tripDAO
                .getOfferedTrips(getLoginModel().getCurrentUser(), pagination);

        if (null == offeredTrips) {
            return this.sendNotFoundResponse(this.translator.translate(""));
        }

        return Response.ok(entityMapper.mapEntities(offeredTrips)).build();
    }

    @GET
    @Path("{" + PATH_PARAM_TRIP_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers,
            @PathParam(PATH_PARAM_TRIP_ID) Long entityId)
    {
        Response response = super.get(headers, entityId);

        if (null != response) {
            return response;
        }

        return this.tryToLoadTrip(entityId);
    }

    private Response tryToLoadTrip(Long entityId)
    {
        try {
            Trip trip = this.tripDAO.getTripByID(entityId);

            return Response.ok(entityMapper.mapEntity(trip)).build();
        } catch (EJBException e) {
            if (e.getCausedByException() instanceof NoResultException) {
                return this.sendNotFoundResponse(
                        translator.translate(IMessages.NO_TRIPS_YET));
            } else {
                throw e;
            }
        }
    }

    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context HttpHeaders headers,
            TripWebServiceModel restModel)
    {
        Response response = super.post(headers, restModel);

        if (null != response) {
            return response;
        }

        try {
            Trip trip = entityMapper.mapWebServiceModel(restModel);

            return this.tryToCreateTrip(trip);
        } catch (ValidationException e) {
            return this.sendBadRequestResponse(e);
        }
    }

    private Response tryToCreateTrip(Trip trip)
    {
        TripManagementMethod tripManagementMethod = this
                .getTripManagementMethod();

        try {
            tripRequest.setTrip(trip);
            tripManagementMethod.addTrip(trip);

            try {
                tripManagementMethod.sendTripAddedMailToDriver(trip);
            } catch (ApplicationUserException e) {
                Application.log("RestBaseController: tryToCreateTrip() "
                        + e.getMessage(), LogType.ERROR, getClass());
                return this.sendOkButErrorResponse(e);
            }
        } catch (ApplicationUserException tripCreationException) {
            Application.log(
                    "RestBaseController: tryToCreateTrip() "
                            + tripCreationException.getMessage(),
                    LogType.ERROR, getClass());
            return this.sendInternalServerErrorResponse(tripCreationException);
        }

        return this.sendCreatedResponse(ENTITY_NAME, trip.getIdentity());
    }

    @Override
    @PUT
    @Path("{" + PATH_PARAM_TRIP_ID + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response put(@Context HttpHeaders headers,
            @PathParam(PATH_PARAM_TRIP_ID) Long entityId,
            TripWebServiceModel restModel)
    {
        Response response = super.put(headers, entityId, restModel);

        if (null != response) {
            return response;
        }

        try {
            Trip trip = entityMapper.mapWebServiceModel(restModel);

            return this.tryToUpdateTrip(entityId, trip);
        } catch (ValidationException e) {
            return this.sendBadRequestResponse(e);
        }
    }

    private Response tryToUpdateTrip(Long entityId, Trip trip)
    {
        try {
            Trip loadedTrip = this.tripDAO.getTripByID(entityId);

            // trip exists -> authorize
            Response authorizeResponse = authorizeForTrip(loadedTrip);

            if (null != authorizeResponse) {
                // not authorized
                return authorizeResponse;
            }

            // authorized -> update trip
            trip.setIdentity(loadedTrip.getIdentity());
            TripManagementMethod tripManagementMethod = this
                    .getTripManagementMethod();
            tripRequest.setTrip(trip);

            try {
                tripManagementMethod.changeTrip(trip);

                try {
                    tripManagementMethod.sendTripUpdatedMailToDriver(trip);
                } catch (ApplicationUserException e) {
                    Application.log(
                            "RestBaseController: tryToUpdateTrip() "
                                    + e.getMessage(),
                            LogType.ERROR, getClass());
                    return this.sendOkButErrorResponse(e);
                }
            } catch (ApplicationUserException changeTripException) {
                Application.log(
                        "RestBaseController: tryToUpdateTrip() "
                                + changeTripException.getMessage(),
                        LogType.ERROR, getClass());
                return this
                        .sendInternalServerErrorResponse(changeTripException);
            }

            return this.sendPutOkResponse(ENTITY_NAME, trip.getIdentity());
        } catch (EJBException e) {
            if (e.getCausedByException() instanceof NoResultException) {
                return this.sendNotFoundResponse(
                        String.format(IMessages.NO_TRIP_WITH_ID, entityId));
            } else {
                throw e;
            }
        }
    }

    @Override
    @DELETE
    @Path("{" + PATH_PARAM_TRIP_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@Context HttpHeaders headers,
            @PathParam(PATH_PARAM_TRIP_ID) Long entityId)
    {
        Response response = super.delete(headers, entityId);

        if (null != response) {
            return response;
        }

        return this.tryToLoadTripAuthorizeAndDelete(entityId);
    }

    private Response tryToLoadTripAuthorizeAndDelete(Long entityId)
    {
        try {
            Trip trip = this.tripDAO.getTripByID(entityId);

            Response authorizeResponse = authorizeForTrip(trip);
            if (null != authorizeResponse) {
                // not authorized
                return authorizeResponse;
            }

            // try to cancel trip
            TripManagementMethod tripManagementMethod = this
                    .getTripManagementMethod();
            tripRequest.setTrip(trip);

            try {
                tripManagementMethod.cancelTrip(trip);

                try {
                    tripManagementMethod.sendTripCanceledMailToDriver(trip);
                } catch (ApplicationUserException e) {
                    Application.log(
                            "RestBaseController: tryToLoadTripAuthorizeAndDelete() "
                                    + e.getMessage(),
                            LogType.ERROR, getClass());
                    return this.sendOkButErrorResponse(e);
                }
            } catch (ApplicationUserException cancelTripException) {
                Application.log(
                        "RestBaseController: tryToLoadTripAuthorizeAndDelete() "
                                + cancelTripException.getMessage(),
                        LogType.ERROR, getClass());
                return this
                        .sendInternalServerErrorResponse(cancelTripException);
            }

            return this.sendOkResponse(String.format(
                    ISuccessResponseEntityBuilder.MESSAGE_CANCELLED,
                    ENTITY_NAME, trip.getIdentity()));
        } catch (EJBException e) {
            if (e.getCausedByException() instanceof NoResultException) {
                return this.sendNotFoundResponse(
                        String.format(IMessages.NO_TRIP_WITH_ID, entityId));
            } else {
                throw e;
            }
        }
    }

    protected Response authorizeForTrip(Trip trip)
    {
        if (trip.getDriver().getIdentity() != this.getLoginModel()
                .getCurrentUser().getIdentity()) {
            return this.sendForbiddenResponse();
        }

        return null;
    }
}
