/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.util;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 17.01.2015
 *
 */
public interface TripAndBookingsConfigKeys
{
    String JUMPUP_TRIP_VALIDATION_LOCATION_MIN_LENGTH = "jumpup.trip.validation.location.min_length";
    String JUMPUP_TRIP_VALIDATION_LOCATION_MAX_LENGTH = "jumpup.trip.validation.location.max_length";
    
    String JUMPUP_TRIP_VALIDATION_START_END_DATETIME_MIN_HOURS_IN_FUTURE = "jumpup.trip.validation.start_end_datetime.min_hours_in_future";
    
    String JUMPUP_TRIP_VALIDATION_PRICE_MIN_VALUE_IN_EURO = "jumpup.trip.validation.price.min_value_in_euro";
    String JUMPUP_TRIP_VALIDATION_PRICE_MAX_VALUE_IN_EURO = "jumpup.trip.validation.price.max_value_in_euro";
    
    String JUMPUP_TRIP_VALIDATION_NUMBER_SEATS_MIN_VALUE = "jumpup.trip.validation.number_seats.min_value";
    String JUMPUP_TRIP_VALIDATION_NUMBER_SEATS_MAX_VALUE = "jumpup.trip.validation.number_seats.max_value";
    
    String JUMPUP_TRIP_GOOGLEMAP_API = "jumpup.trip.googlemap.api";
    
    String JUMPUP_TRIP_CREATED_MAIL_TEMPLATE_TXT = "jumpup.trip.created.mail.template.txt";
    String JUMPUP_TRIP_CHANGED_MAIL_TEMPLATE_TXT = "jumpup.trip.changed.mail.template.txt";
    String JUMPUP_TRIP_CANCELED_MAIL_DRIVER_TEMPLATE_TXT = "jumpup.trip.canceled.mail_driver.template.txt";
    String JUMPUP_TRIP_CANCELED_MAIL_PASSENGER_TEMPLATE_TXT = "jumpup.trip.canceled.mail_passenger.template.txt";
    
    String JUMPUP_BOOKING_CREATED_MAIL_PASSENGER_TEMPLATE_TXT = "jumpup.booking.created.mail_passenger.template.txt";
    String JUMPUP_BOOKING_CREATED_MAIL_DRIVER_TEMPLATE_TXT = "jumpup.booking.created.mail_driver.template.txt";
    String JUMPUP_BOOKING_CONFIRMED_MAIL_PASSENGER_TEMPLATE_TXT = "jumpup.booking.confirmed.mail_passenger.template.txt";
    String JUMPUP_BOOKING_CANCELED_MAIL_PASSENGER_TEMPLATE_TXT = "jumpup.booking.canceled.mail_passenger.template.txt";
    String JUMPUP_BOOKING_CANCELED_MAIL_DRIVER_TEMPLATE_TXT = "jumpup.booking.canceled.mail_driver.template.txt";
}
