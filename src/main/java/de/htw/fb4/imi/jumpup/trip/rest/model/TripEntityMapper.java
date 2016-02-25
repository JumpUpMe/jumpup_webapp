/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.rest.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.rest.IEntityMapper;
import de.htw.fb4.imi.jumpup.rest.RestUtil;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.validation.ValidationException;
import de.htw.fb4.imi.jumpup.validation.validator.JumpUpValidator;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 02.12.2015
 *
 */
@Named(value = BeanNames.TRIP_ENTITY_MAPPER)
@RequestScoped
public class TripEntityMapper
        implements IEntityMapper<TripWebServiceModel, Trip>
{
    @Inject
    @Named(value = BeanNames.LOCATION_VALIDATOR)
    private JumpUpValidator startPointValidator;

    @Inject
    @Named(value = BeanNames.LOCATION_VALIDATOR)
    private JumpUpValidator endPointValidator;

    @Inject
    @Named(value = BeanNames.LATITUDE_VALIDATOR)
    private JumpUpValidator startLatValidator;

    @Inject
    @Named(value = BeanNames.LONGITUDE_VALIDATOR)
    private JumpUpValidator startLongValidator;

    @Inject
    @Named(value = BeanNames.LATITUDE_VALIDATOR)
    private JumpUpValidator endLatValidator;

    @Inject
    @Named(value = BeanNames.LONGITUDE_VALIDATOR)
    private JumpUpValidator endLongValidator;

    @Inject
    @Named(value = BeanNames.NUMBER_SEATS_VALIDATOR)
    private JumpUpValidator numberOfSeatsValidator;

    @Inject
    @Named(value = BeanNames.OVERVIEW_PATH_VALIDATOR)
    private JumpUpValidator overviewPathValidator;

    @Inject
    @Named(value = BeanNames.PRICE_VALIDATOR)
    private JumpUpValidator priceValidator;

    @Inject
    @Named(value = BeanNames.START_DATETIME_VALIDATOR)
    private JumpUpValidator startDatetimeValidator;

    @Inject
    @Named(value = BeanNames.END_DATETIME_VALIDATOR)
    private JumpUpValidator endDateTimeValidator;

    @Inject
    @Named(value = BeanNames.VIA_WAYPOINTS_VALIDATOR)
    private JumpUpValidator viaWaypointsValidator;

    @Override
    public TripWebServiceModel mapEntity(Trip entity)
    {
        if (!(entity instanceof de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip)) {
            throw new IllegalArgumentException("Type of entity must be Trip");
        }

        TripWebServiceModel webServiceTrip = new TripWebServiceModel();
        de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip entityTrip = entity;

        webServiceTrip.setIdentity(entityTrip.getIdentity());
        webServiceTrip.setCreationTimestamp(
                RestUtil.getTimestampForWebServiceRespones(
                        entityTrip.getCreationTimestamp().getTime())); // seconds
        webServiceTrip
                .setUpdateTimestamp(RestUtil.getTimestampForWebServiceRespones(
                        entityTrip.getUpdateTimestamp().getTime())); // seconds
        webServiceTrip.setStartpoint(entityTrip.getStartpoint());
        webServiceTrip.setEndpoint(entityTrip.getEndpoint());
        webServiceTrip.setLatStartpoint(entityTrip.getLatStartpoint());
        webServiceTrip.setLongStartpoint(entityTrip.getLongStartpoint());
        webServiceTrip.setLatEndpoint(entityTrip.getLatEndpoint());
        webServiceTrip.setLongEndpoint(entityTrip.getLongEndpoint());
        webServiceTrip
                .setStartDateTime(RestUtil.getTimestampForWebServiceRespones(
                        entityTrip.getStartDateTime().getTime())); // seconds
        webServiceTrip
                .setEndDateTime(RestUtil.getTimestampForWebServiceRespones(
                        entityTrip.getEndDateTime().getTime())); // seconds
        webServiceTrip.setPrice(entityTrip.getPrice());
        webServiceTrip.setOverViewPath(entityTrip.getOverViewPath());
        webServiceTrip.setViaWaypoints(entityTrip.getViaWaypoints());
        webServiceTrip.setNumberOfSeats(entityTrip.getNumberOfSeats());
        webServiceTrip.setVehicle(entityTrip.getVehicle());
        // webServiceTrip.setDriver(entityTrip.getDriver());

        if (null != entityTrip.getCancelationDateTime()) {
            webServiceTrip.setCancelationDateTime(
                    RestUtil.getTimestampForWebServiceRespones(
                            entityTrip.getCancelationDateTime().getTime())); // seconds
        }

        webServiceTrip.setDistanceMeters(entityTrip.getDistanceMeters());
        webServiceTrip.setDurationSeconds(entityTrip.getDurationSeconds());

        return webServiceTrip;
    }

    @Override
    public Trip mapWebServiceModel(TripWebServiceModel webServiceModel)
            throws ValidationException
    {
        Trip entityTrip = new de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip();

        entityTrip.setIdentity(webServiceModel.getIdentity());
        entityTrip.setCreationTimestamp(
                convertToDatetime(webServiceModel.getCreationTimestamp()));
        entityTrip.setUpdateTimestamp(
                convertToDatetime(webServiceModel.getUpdateTimestamp()));

        this.validateStartpoint(webServiceModel.getStartpoint());
        entityTrip.setStartpoint(webServiceModel.getStartpoint());

        this.validateEndpoint(webServiceModel.getEndpoint());
        entityTrip.setEndpoint(webServiceModel.getEndpoint());

        this.validatelatStartpoint(webServiceModel.getLatStartpoint());
        entityTrip.setLatStartpoint(webServiceModel.getLatStartpoint());

        this.validateLongStartpoint(webServiceModel.getLongStartpoint());
        entityTrip.setLongStartpoint(webServiceModel.getLongEndpoint());

        this.validatelatEndpoint(webServiceModel.getLatEndpoint());
        entityTrip.setLatEndpoint(webServiceModel.getLatEndpoint());

        this.validateLongEndpoint(webServiceModel.getLongEndpoint());
        entityTrip.setLongEndpoint(webServiceModel.getLongEndpoint());

        Timestamp startDateTime = convertToDatetime(
                webServiceModel.getStartDateTime());
        this.validateStartDateTime(startDateTime);
        entityTrip.setStartDateTime(startDateTime);

        Timestamp endDateTime = convertToDatetime(
                webServiceModel.getEndDateTime());
        this.validateEndDateTime(endDateTime);
        entityTrip.setEndDateTime(endDateTime);

        this.validatePrice(webServiceModel.getPrice());
        entityTrip.setPrice(webServiceModel.getPrice());

        this.validateOverviewPath(webServiceModel.getOverViewPath());
        entityTrip.setOverViewPath(webServiceModel.getOverViewPath());

        this.validateViaWaypoints(webServiceModel.getViaWaypoints());
        entityTrip.setViaWaypoints(webServiceModel.getViaWaypoints());

        this.validateNumberOfSeats(webServiceModel.getNumberOfSeats());
        entityTrip.setNumberOfSeats(webServiceModel.getNumberOfSeats());

        entityTrip.setVehicle(webServiceModel.getVehicle());
        // webServiceTrip.setDriver(entityTrip.getDriver());

        if (null != webServiceModel.getCancelationDateTime()) {
            entityTrip.setCancelationDateTime(convertToDatetime(
                    webServiceModel.getCancelationDateTime()));
        }

        entityTrip.setDistanceMeters(webServiceModel.getDistanceMeters());
        entityTrip.setDurationSeconds(webServiceModel.getDurationSeconds());

        return entityTrip;
    }

    private void validateNumberOfSeats(Integer numberOfSeats)
            throws ValidationException
    {
        if (!this.numberOfSeatsValidator.validate(numberOfSeats)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_NUMBER_OF_SEATS,
                    this.numberOfSeatsValidator.getErrorMessages());
        }
    }

    private void validateViaWaypoints(String viaWaypoints)
            throws ValidationException
    {
        if (!this.viaWaypointsValidator.validate(viaWaypoints)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_VIA_WAYPOINTS,
                    this.viaWaypointsValidator.getErrorMessages());
        }
    }

    private void validateOverviewPath(String overViewPath)
            throws ValidationException
    {
        if (!this.overviewPathValidator.validate(overViewPath)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_OVERVIEW_PATH,
                    this.overviewPathValidator.getErrorMessages());
        }
    }

    private void validatePrice(float price) throws ValidationException
    {
        if (!this.priceValidator.validate(price)) {
            throw new ValidationException(TripWebServiceModel.FIELD_NAME_PRICE,
                    this.priceValidator.getErrorMessages());
        }
    }

    private void validateEndDateTime(Date endDateTime)
            throws ValidationException
    {
        if (!this.endDateTimeValidator.validate(endDateTime)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_END_DATETIME,
                    this.endDateTimeValidator.getErrorMessages());
        }
    }

    private void validateStartDateTime(Date startDateTime)
            throws ValidationException
    {
        if (!this.startDatetimeValidator.validate(startDateTime)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_START_DATETIME,
                    this.startDatetimeValidator.getErrorMessages());
        }
    }

    private void validateLongEndpoint(double longEndpoint)
            throws ValidationException
    {
        if (!this.endLongValidator.validate(longEndpoint)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_END_LNG,
                    this.endLongValidator.getErrorMessages());
        }
    }

    private void validatelatEndpoint(double latEndpoint)
            throws ValidationException
    {
        if (!this.endLatValidator.validate(latEndpoint)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_END_LAT,
                    this.endLatValidator.getErrorMessages());
        }
    }

    private void validateLongStartpoint(double longStartpoint)
            throws ValidationException
    {
        if (!this.startLongValidator.validate(longStartpoint)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_START_LNG,
                    this.startLongValidator.getErrorMessages());
        }
    }

    private void validatelatStartpoint(double latStartpoint)
            throws ValidationException
    {
        if (!this.startLatValidator.validate(latStartpoint)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_START_LAT,
                    this.startLatValidator.getErrorMessages());
        }
    }

    private void validateEndpoint(String endpoint) throws ValidationException
    {
        if (!this.endPointValidator.validate(endpoint)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_END_POINT,
                    this.endPointValidator.getErrorMessages());
        }
    }

    private void validateStartpoint(String startpoint)
            throws ValidationException
    {
        if (!this.startPointValidator.validate(startpoint)) {
            throw new ValidationException(
                    TripWebServiceModel.FIELD_NAME_START_POINT,
                    this.startPointValidator.getErrorMessages());
        }
    }

    protected Timestamp convertToDatetime(Long d)
    {
        return RestUtil.convertTimestampFromWebserviceRequestToDatetime(d);
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapEntities(java.util.
     * Collection)
     */
    public Collection<TripWebServiceModel> mapEntities(
            Collection<Trip> entityTypes)
    {
        Collection<TripWebServiceModel> tripWebServiceCollection = new ArrayList<TripWebServiceModel>();

        for (Trip trip : entityTypes) {
            tripWebServiceCollection.add(this.mapEntity(trip));
        }

        return tripWebServiceCollection;
    }
}
