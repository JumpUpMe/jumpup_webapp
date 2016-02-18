/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ajax.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 26.01.2015
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class TripQueryResults
{
    /**
     * 
     * <p>Type to simplify the client (e.g. frontend) to handle the polymorphistic query result (e.g. direct trips list, partial overlap trips with intersections, no result).</p>
     *
     * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
     * @since 09.08.2015
     *
     */
    public enum Type {
        DIRECT_TRIP_RESULT,
        MULTIPLE_PARTIAL_TRIP_RESULT,
        NO_RESULT
    }
    
    /**
     * Translated messages given to the frontend.
     * <p></p>
     *
     * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
     * @since 29.01.2015
     *
     */
    public class Translations {
        protected String to = "to";
        protected String locationDistance = "Distance to Start";
        protected String startDate = "Start Date";
        protected String driver = "Driver";
        protected String overallPrice = "Overall price";
        protected String numberBookings = "Number of bookings";
        protected String destinationDistance = "Distance to destination";
        protected String vehicle = "Vehicle";
        protected String book = "Book";
        protected String bookTooltip = "Click to book this trip now";
        
        /**
         * @return the to
         */
        public String getTo()
        {
            return to;
        }
        /**
         * @param to the to to set
         */
        public void setTo(String to)
        {
            this.to = to;
        }
        /**
         * @return the locationDistance
         */
        public String getLocationDistance()
        {
            return locationDistance;
        }
        /**
         * @param locationDistance the locationDistance to set
         */
        public void setLocationDistance(String locationDistance)
        {
            this.locationDistance = locationDistance;
        }
        /**
         * @return the startDate
         */
        public String getStartDate()
        {
            return startDate;
        }
        /**
         * @param startDate the startDate to set
         */
        public void setStartDate(String startDate)
        {
            this.startDate = startDate;
        }
        /**
         * @return the driver
         */
        public String getDriver()
        {
            return driver;
        }
        /**
         * @param driver the driver to set
         */
        public void setDriver(String driver)
        {
            this.driver = driver;
        }
        /**
         * @return the overallPrice
         */
        public String getOverallPrice()
        {
            return overallPrice;
        }
        /**
         * @param overallPrice the overallPrice to set
         */
        public void setOverallPrice(String overallPrice)
        {
            this.overallPrice = overallPrice;
        }
        /**
         * @return the numberBookings
         */
        public String getNumberBookings()
        {
            return numberBookings;
        }
        /**
         * @param numberBookings the numberBookings to set
         */
        public void setNumberBookings(String numberBookings)
        {
            this.numberBookings = numberBookings;
        }
        /**
         * @return the destinationDistance
         */
        public String getDestinationDistance()
        {
            return destinationDistance;
        }
        /**
         * @param destinationDistance the destinationDistance to set
         */
        public void setDestinationDistance(String destinationDistance)
        {
            this.destinationDistance = destinationDistance;
        }
        /**
         * @return the vehicle
         */
        public String getVehicle()
        {
            return vehicle;
        }
        /**
         * @param vehicle the vehicle to set
         */
        public void setVehicle(String vehicle)
        {
            this.vehicle = vehicle;
        }
        /**
         * @return the book
         */
        public String getBook()
        {
            return book;
        }
        /**
         * @param book the book to set
         */
        public void setBook(String book)
        {
            this.book = book;
        }
        /**
         * @return the bookTooltip
         */
        public String getBookTooltip()
        {
            return bookTooltip;
        }
        /**
         * @param bookTooltip the bookTooltip to set
         */
        public void setBookTooltip(String bookTooltip)
        {
            this.bookTooltip = bookTooltip;
        }               
        
    }
    
    protected List<SingleTripQueryResult> trips = new ArrayList<SingleTripQueryResult>();
    protected Translations translations = new Translations();
    protected Type type = Type.DIRECT_TRIP_RESULT;
    
    public TripQueryResults()
    {
        this.type = Type.DIRECT_TRIP_RESULT;
    }
    
    /**
     * @return the trips
     */
    public List<SingleTripQueryResult> getTrips()
    {
        return trips;
    }

    /**
     * @param trips the trips to set
     */
    public void setTrips(List<SingleTripQueryResult> trips)
    {
        this.trips = trips;
    }

    /**
     * @return the translations
     */
    public Translations getTranslations()
    {
        return translations;
    }

    /**
     * @return the type
     */
    public Type getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Type type)
    {
        this.type = type;
    }    
}
