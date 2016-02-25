/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ajax;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.navigation.NavigationBean;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.translate.Translatable;
import de.htw.fb4.imi.jumpup.trip.ajax.model.OverlappingPartialTripQueryResult;
import de.htw.fb4.imi.jumpup.trip.ajax.model.SingleTripQueryResult;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryNoResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryResults;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripSearchCriteria;
import de.htw.fb4.imi.jumpup.trip.ajax.model.TripQueryResults.Translations;
import de.htw.fb4.imi.jumpup.trip.graph.Path;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.util.LocaleHelper;
import de.htw.fb4.imi.jumpup.verhicle.entity.Vehicle;

/**
 * <p>
 * Factory for creating models that will be returned by our REST services.
 * </p>
 * 
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 26.01.2015
 * 
 */
@Stateless(name = BeanNames.QUERY_RESULT_FACTORY)
public class QueryResultFactory
{
    @Inject
    protected Translatable translator;

    @Inject
    protected NavigationBean navigationHelper;
    
    @Inject
    protected LocaleHelper localeHelper;

    /**
     * Build a {@link SingleTripQueryResult} which will be returned marshalled
     * within our REST services.
     * 
     * @param trip
     *            entity from database
     * @return
     */
    public SingleTripQueryResult newSingleTripQueryResult(final Trip trip,
            TripSearchCriteria searchCriteria)
    {
        SingleTripQueryResult singleTripQueryResult = new SingleTripQueryResult();
;
        fillFromTripEntity(singleTripQueryResult, trip, searchCriteria);
        fillFromDriverEntity(singleTripQueryResult, trip.getDriver(), searchCriteria);
        fillFromVehicleEntity(singleTripQueryResult, trip.getVehicle());

        return singleTripQueryResult;
    }

    private void fillFromTripEntity(
            SingleTripQueryResult singleTripQueryResult, Trip trip, TripSearchCriteria searchCriteria)
    {
        singleTripQueryResult.getTrip().setIdentity(trip.getIdentity());
        singleTripQueryResult.getTrip().setStartpoint(trip.getStartpoint());
        singleTripQueryResult.getTrip().setEndpoint(trip.getEndpoint());
        singleTripQueryResult.getTrip().setLatStartpoint(
                trip.getLatStartpoint());
        singleTripQueryResult.getTrip().setLongStartpoint(
                trip.getLongStartpoint());
        singleTripQueryResult.getTrip().setLatEndpoint(trip.getLatEndpoint());
        singleTripQueryResult.getTrip().setLongEndpoint(trip.getLongEndpoint());
        singleTripQueryResult.getTrip().setStartDateTime(localeHelper.formatDateTime(trip.getStartDateTime()));
        singleTripQueryResult.getTrip().setEndDateTime(localeHelper.formatDateTime(trip.getEndDateTime()));
        singleTripQueryResult.getTrip().setPrice(trip.getPrice());
        singleTripQueryResult.getTrip().setViaWaypoints(trip.getViaWaypoints());
        singleTripQueryResult.getTrip().setNumberOfSeats(
                trip.getNumberOfSeats());
        // generate and set booking URL to enable booking this trip
        singleTripQueryResult.getTrip().setBookingUrl(
                navigationHelper.generateAddBookingUrl(
                        searchCriteria, trip));
        singleTripQueryResult.getTrip().setNumberOfBookings(trip.getNumberOfBookings());
        singleTripQueryResult.getTrip().setDistanceFromPassengersLocation(trip.getDistanceToPassengersStartLocation());
        singleTripQueryResult.getTrip().setDistanceFromPassengersDestination(trip.getDistanceToPassengersEndLocation());
    }

    private void fillFromDriverEntity(
            SingleTripQueryResult singleTripQueryResult, User driver, TripSearchCriteria searchCriteria)
    {
        singleTripQueryResult.getDriver().setUsername(driver.getUsername());
        singleTripQueryResult.getDriver().setEmail(driver.geteMail());
        singleTripQueryResult.getDriver().setCountry(driver.getCountry());
        singleTripQueryResult.getDriver().setGender(
                driver.getUserDetails().getGender());
        singleTripQueryResult.getDriver().setMobileNumber(
                driver.getUserDetails().getMobileNumber());
        singleTripQueryResult.getDriver().setPrename(driver.getPrename());
        singleTripQueryResult.getDriver().setLastname(driver.getLastname());
        singleTripQueryResult.getDriver().setSkype(
                driver.getUserDetails().getSkype());
        singleTripQueryResult.getDriver().setSpokenLanguages(
                driver.getUserDetails().getLanguages());
        singleTripQueryResult.getDriver().setTown(driver.getTown());
        singleTripQueryResult.getDriver().setUrl(
                navigationHelper.generateDriverUrl(
                        searchCriteria, driver));

    }

    /**
     * TODO implement
     * 
     * @param singleTripQueryResult
     * @param vehicle
     */
    private void fillFromVehicleEntity(
            SingleTripQueryResult singleTripQueryResult, Vehicle vehicle)
    {
        // TODO Auto-generated method stub
        // only dummy implementation
        singleTripQueryResult.getVehicle().setManufactor("BMW");
    }

    /**
     * 
     * @param message
     * @return
     */
    public TripQueryNoResults newNoTripsResult(String message)
    {
        TripQueryNoResults noResults = new TripQueryNoResults();

        noResults.setMessage(message);

        return noResults;
    }

    public TripQueryResults newTripQueryResults(List<SingleTripQueryResult> list)
    {
        TripQueryResults results = new TripQueryResults();
        addTranslations(results);
        results.setTrips(list);
        return results;
    }
    
    public TripQueryResults newOverlappingPartialTripsResult(
            Path overlappingPartialTrips, TripSearchCriteria tripSearchModel)
    {
        OverlappingPartialTripQueryResult results = new OverlappingPartialTripQueryResult();
        
        addTranslations(results);
        
        List<SingleTripQueryResult> tripsOnPathQueryResult = getTripsRecords(
                overlappingPartialTrips, tripSearchModel);        
        results.setTrips(tripsOnPathQueryResult);     
        results.setMessage(translator.translate("We didn't find a direct trip but " + tripsOnPathQueryResult.size() + " connecting trips. Please have a look."));
        //results.setIntersections(overlappingPartialTrips.getIntersectionsOnPath());
        
        return results;
    }

    protected List<SingleTripQueryResult> getTripsRecords(
            Path overlappingPartialTrips, TripSearchCriteria tripSearchModel)
    {
        List<SingleTripQueryResult> tripsOnPathQueryResult = new ArrayList<SingleTripQueryResult>();
        
        for (Trip tripOnPath : overlappingPartialTrips.getTripsOnPath()) {
            tripsOnPathQueryResult.add(this.newSingleTripQueryResult(tripOnPath, tripSearchModel));
        }
        
        return tripsOnPathQueryResult;
    }

    private void addTranslations(TripQueryResults results)
    {
        Translations translations = results.getTranslations();

        translations.setDestinationDistance(translator.translate(translations
                .getDestinationDistance()));
        translations.setDriver(translator.translate(translations.getDriver()));
        translations.setLocationDistance(translator.translate(translations
                .getLocationDistance()));
        translations.setNumberBookings(translator.translate(translations
                .getNumberBookings()));
        translations.setOverallPrice(translator.translate(translations
                .getOverallPrice()));
        translations.setStartDate(translator.translate(translations
                .getStartDate()));
        translations.setTo(translator.translate(translations.getTo()));
        translations
                .setVehicle(translator.translate(translations.getVehicle()));
        translations.setBook(translator.translate(translations.getBook()));
        translations.setBookTooltip(translator.translate(translations
                .getBookTooltip()));
    }

    /**
     * Reconstruct a {@link TripSearchCriteria} by a given {@link Booking}
     * instance, e.g. for hash checking.
     * 
     * @param booking
     * @return
     */
    public TripSearchCriteria newTripSearchCriteriaBy(Booking booking)
    {
        TripSearchCriteria searchCriteria = new TripSearchCriteria();

        searchCriteria.setStartPoint(booking.getStartPoint());
        searchCriteria.setEndPoint(booking.getEndPoint());
        searchCriteria.setLatStartPoint(booking.getStartLatitude());
        searchCriteria.setLongStartPoint(booking.getStartLongitude());
        searchCriteria.setLatEndPoint(booking.getEndLatitude());
        searchCriteria.setLongEndPoint(booking.getEndLongitude());

        return searchCriteria;
    }  
}
