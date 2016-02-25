/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking.rest;

import java.util.Collection;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.booking.BookingDAO;
import de.htw.fb4.imi.jumpup.booking.BookingMethod;
import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.booking.rest.model.BookingEntityMapper;
import de.htw.fb4.imi.jumpup.booking.rest.model.BookingWebServiceModel;
import de.htw.fb4.imi.jumpup.rest.controller.SecuredRestController;
import de.htw.fb4.imi.jumpup.trip.ejb.TripDAO;
import de.htw.fb4.imi.jumpup.validation.ValidationException;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 02.01.2016
 *
 */
public class BaseController
        extends SecuredRestController<BookingWebServiceModel>
{
    public static final String PATH = "/booking";
    private static final String PATH_PARAM_TRIP_ID = "tripId";
    private static final String MESSAGE_NOT_FOUND = "We didn't find any booking on the given trip yet.";
    private static final String MESSAGE_PASSENGER_NO_BOOKINGS = "You did not book any trip yet.";

    @Inject
    protected BookingDAO bookingDAO;

    @Inject
    protected TripDAO tripDAO;

    @Inject
    protected BookingMethod bookingMethod;

    @Inject
    protected BookingEntityMapper entityMapper;

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

        return this.tryToLoadBookingsForTrip(entityId);
    }

    private Response tryToLoadBookingsForTrip(Long entityId)
    {
        Collection<BookingWebServiceModel> tripBookings = entityMapper
                .mapEntities(bookingDAO.getBookingsByTrip(entityId));

        return return404IfNoTrips(tripBookings, MESSAGE_NOT_FOUND);
    }

    protected Response return404IfNoTrips(
            Collection<BookingWebServiceModel> tripBookings, String message)
    {
        if (null == tripBookings || tripBookings.size() == 0) {
            return this.sendNotFoundResponse(message);
        }

        return Response.ok().entity(tripBookings).build();
    }

    @Override
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpHeaders headers)
    {
        Response response = super.get(headers);

        if (null != response) {
            return response;
        }

        return this.tryToLoadBookingsOfCurrentUser();
    }

    private Response tryToLoadBookingsOfCurrentUser()
    {
        Collection<BookingWebServiceModel> passengerBookings = entityMapper
                .mapEntities(this.bookingDAO.getBookingsByPassenger(
                        this.loginSession.getCurrentUser()));

        return return404IfNoTrips(passengerBookings,
                MESSAGE_PASSENGER_NO_BOOKINGS);
    }

    @Override
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Context HttpHeaders headers,
            BookingWebServiceModel restModel)
    {
        Response response = super.post(headers, restModel);

        if (null != response) {
            return response;
        }

        try {
            Booking booking = entityMapper.mapWebServiceModel(restModel);

            return this.tryToCreateBooking(booking);
        } catch (ValidationException e) {
            return this.sendBadRequestResponse(e);
        }
    }

    private Response tryToCreateBooking(Booking booking)
    {
        try {
            bookingMethod.createBooking(booking, booking.getTrip());

            return this.sendCreatedResponse("booking", booking.getIdentity());
        } catch (ApplicationUserException e) {
            return this.sendInternalServerErrorResponse(e);
        }
    }
}
