/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.booking.rest.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.rest.IEntityMapper;
import de.htw.fb4.imi.jumpup.rest.RestUtil;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.TripDAO;
import de.htw.fb4.imi.jumpup.trip.ajax.QueryResultFactory;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.entity.Trip;
import de.htw.fb4.imi.jumpup.validation.ValidationException;

/**
 * <p>
 * Entity mapper between {@link Booking} and {@link BookingWebServiceModel}
 * </p>
 * .
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 02.01.2016
 *
 */
@Named(value = BeanNames.BOOKING_ENTITY_MAPPER)
@RequestScoped
public class BookingEntityMapper
        implements IEntityMapper<BookingWebServiceModel, Booking>
{
    @Inject
    private TripDAO tripDAO;

    @Inject
    protected QueryResultFactory queryResultFactory;

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapEntity(de.htw.fb4.imi.jumpup.
     * entity.AbstractEntity)
     */
    public BookingWebServiceModel mapEntity(Booking entity)
    {
        BookingWebServiceModel booking = new BookingWebServiceModel();
        booking.setStartLatitude(entity.getStartLatitude());
        booking.setStartLongitude(entity.getStartLongitude());
        booking.setEndLatitude(entity.getEndLatitude());
        booking.setEndLongitude(entity.getEndLongitude());
        booking.setConfirmationDateTime(
                RestUtil.getTimestampForWebServiceRespones(
                        entity.getConfirmationDateTime().getTime())); // seconds

        if (null != entity.getCancellationDateTime()) {
            booking.setCancellationDateTime(
                    RestUtil.getTimestampForWebServiceRespones(
                            entity.getCancellationDateTime().getTime())); // seconds
        }

        booking.setActorOnLastChange(entity.getActorOnLastChange());
        booking.setTripId(entity.getTripIdentity());

        return booking;
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapWebServiceModel(de.htw.fb4.
     * imi.jumpup.rest.response.model.AbstractRestModel)
     */
    public Booking mapWebServiceModel(BookingWebServiceModel webServiceModel)
            throws ValidationException
    {
        Booking booking = new Booking();

        booking.setStartPoint(webServiceModel.getStartPoint());
        booking.setEndPoint(webServiceModel.getEndPoint());
        booking.setStartLatitude(webServiceModel.getStartLatitude());
        booking.setStartLongitude(webServiceModel.getStartLongitude());
        booking.setEndLatitude(webServiceModel.getEndLatitude());
        booking.setEndLongitude(webServiceModel.getEndLongitude());
        booking.setConfirmationDateTime(
                RestUtil.convertTimestampFromWebserviceRequestToDatetime(
                        webServiceModel.getConfirmationDateTime()));
        booking.setCancellationDateTime(
                RestUtil.convertTimestampFromWebserviceRequestToDatetime(
                        webServiceModel.getCancellationDateTime()));
        booking.setActorOnLastChange(webServiceModel.getActorOnLastChange());

        Trip trip = this.validateTrip(webServiceModel.getTripId());
        booking.setTrip(trip);

        // TODO the booking hash should be passed as parameter. The client (e.g.
        // Android app) recieves the hash during the LookUpTrips request.
        this.calculateHashAndAddToBooking(booking, trip);

        return booking;
    }

    private Trip validateTrip(Long tripId) throws ValidationException
    {
        try {
            return this.tripDAO.getTripByID(tripId);
        } catch (EJBException e) {
            throw new ValidationException("tripId",
                    "The given trip couldn't be found.");
        }
    }

    private void calculateHashAndAddToBooking(Booking booking, Trip trip)
    {
        TripSearchCriteria bookingSearchCriteria = this.queryResultFactory
                .newTripSearchCriteriaBy(booking);
        String hash = bookingSearchCriteria.createBookingHash(trip);
        booking.setBookingHash(hash);
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapEntities(java.util.
     * Collection)
     */
    public Collection<BookingWebServiceModel> mapEntities(
            Collection<Booking> entityTypes)
    {
        Collection<BookingWebServiceModel> bookings = new ArrayList<>();

        for (Booking bookingEntity : entityTypes) {
            bookings.add(this.mapEntity(bookingEntity));
        }

        return bookings;
    }

}
