/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.restservice.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.htw.fb4.imi.jumpup.trip.query.TripQueryMethod;
import de.htw.fb4.imi.jumpup.util.Gender;
import de.htw.fb4.imi.jumpup.util.Languages;

/**
 * <p>
 * Result of {@link TripQueryMethod}
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 25.01.2015
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class SingleTripQueryResult implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -3179074383171618295L;

    public class Driver implements Serializable
    {
        /**
         * 
         */
        private static final long serialVersionUID = 5450451948907569162L;

        protected Integer id;      
        protected String username;
        protected String email;
        protected String prename;
        protected String lastname;
        protected String town;
        protected String country;
        protected Gender gender;
        protected String mobileNumber;
        protected String skype;
        protected Set<Languages> spokenLanguages;

        /** 
         * @return the username
         */
        public String getUsername()
        {
            return username;
        }

        /**
         * @param username
         *            the username to set
         */
        public void setUsername(String username)
        {
            this.username = username;
        }

        /**
         * @return the email
         */
        public String getEmail()
        {
            return email;
        }

        /**
         * @param email
         *            the email to set
         */
        public void setEmail(String email)
        {
            this.email = email;
        }

        /**
         * @return the prename
         */
        public String getPrename()
        {
            return prename;
        }

        /**
         * @param prename
         *            the prename to set
         */
        public void setPrename(String prename)
        {
            this.prename = prename;
        }

        /**
         * @return the lastname
         */
        public String getLastname()
        {
            return lastname;
        }

        /**
         * @param lastname
         *            the lastname to set
         */
        public void setLastname(String lastname)
        {
            this.lastname = lastname;
        }

        /**
         * @return the town
         */
        public String getTown()
        {
            return town;
        }

        /**
         * @param town
         *            the town to set
         */
        public void setTown(String town)
        {
            this.town = town;
        }

        /**
         * @return the country
         */
        public String getCountry()
        {
            return country;
        }

        /**
         * @param country
         *            the country to set
         */
        public void setCountry(String country)
        {
            this.country = country;
        }

        /**
         * @return the gender
         */
        public Gender getGender()
        {
            return gender;
        }

        /**
         * @param gender
         *            the gender to set
         */
        public void setGender(Gender gender)
        {
            this.gender = gender;
        }

        /**
         * @return the mobileNumber
         */
        public String getMobileNumber()
        {
            return mobileNumber;
        }

        /**
         * @param mobileNumber
         *            the mobileNumber to set
         */
        public void setMobileNumber(String mobileNumber)
        {
            this.mobileNumber = mobileNumber;
        }

        /**
         * @return the skype
         */
        public String getSkype()
        {
            return skype;
        }

        /**
         * @param skype
         *            the skype to set
         */
        public void setSkype(String skype)
        {
            this.skype = skype;
        }

        /**
         * @return the spokenLanguages
         */
        public Set<Languages> getSpokenLanguages()
        {
            return spokenLanguages;
        }

        /**
         * @param spokenLanguages
         *            the spokenLanguages to set
         */
        public void setSpokenLanguages(Set<Languages> spokenLanguages)
        {
            this.spokenLanguages = spokenLanguages;
        }
    }

    public class Vehicle implements Serializable
    {

        /**
         * 
         */
        private static final long serialVersionUID = 363242122701716963L;
        
        protected String manufactor;

        /**
         * @return the manufactor
         */
        public String getManufactor()
        {
            return manufactor;
        }

        /**
         * @param manufactor the manufactor to set
         */
        public void setManufactor(String manufactor)
        {
            this.manufactor = manufactor;
        }       
        
    }

    public class Trip implements Serializable
    {

        /**
         * 
         */
        private static final long serialVersionUID = -8609518419139415328L;
        
        protected long id;      
        protected String startpoint;
        protected String endpoint;
        protected float latStartpoint;
        protected float longStartpoint;
        protected float latEndpoint;
        protected float longEndpoint;
        protected Timestamp startDateTime;
        protected Timestamp endDateTime;
        protected float price;
        protected String viaWaypoints;
        protected Integer numberOfSeats;
        protected String bookingUrl;
        
        
        /**
         * @return the id
         */
        public long getId()
        {
            return id;
        }

        /**
         * @param l the id to set
         */
        public void setId(long l)
        {
            this.id = l;
        }

        /**
         * @return the startpoint
         */
        public String getStartpoint()
        {
            return startpoint;
        }

        /**
         * @param startpoint
         *            the startpoint to set
         */
        public void setStartpoint(String startpoint)
        {
            this.startpoint = startpoint;
        }

        /**
         * @return the endpoint
         */
        public String getEndpoint()
        {
            return endpoint;
        }

        /**
         * @param endpoint
         *            the endpoint to set
         */
        public void setEndpoint(String endpoint)
        {
            this.endpoint = endpoint;
        }

        /**
         * @return the latStartpoint
         */
        public float getLatStartpoint()
        {
            return latStartpoint;
        }

        /**
         * @param latStartpoint
         *            the latStartpoint to set
         */
        public void setLatStartpoint(float latStartpoint)
        {
            this.latStartpoint = latStartpoint;
        }

        /**
         * @return the longStartpoint
         */
        public float getLongStartpoint()
        {
            return longStartpoint;
        }

        /**
         * @param longStartpoint
         *            the longStartpoint to set
         */
        public void setLongStartpoint(float longStartpoint)
        {
            this.longStartpoint = longStartpoint;
        }

        /**
         * @return the latEndpoint
         */
        public float getLatEndpoint()
        {
            return latEndpoint;
        }

        /**
         * @param latEndpoint
         *            the latEndpoint to set
         */
        public void setLatEndpoint(float latEndpoint)
        {
            this.latEndpoint = latEndpoint;
        }

        /**
         * @return the longEndpoint
         */
        public float getLongEndpoint()
        {
            return longEndpoint;
        }

        /**
         * @param longEndpoint
         *            the longEndpoint to set
         */
        public void setLongEndpoint(float longEndpoint)
        {
            this.longEndpoint = longEndpoint;
        }

        /**
         * @return the startDateTime
         */
        public Timestamp getStartDateTime()
        {
            return startDateTime;
        }

        /**
         * @param startDateTime
         *            the startDateTime to set
         */
        public void setStartDateTime(Timestamp startDateTime)
        {
            this.startDateTime = startDateTime;
        }

        /**
         * @return the endDateTime
         */
        public Timestamp getEndDateTime()
        {

            return endDateTime;
        }

        /**
         * @param endDateTime
         *            the endDateTime to set
         */
        public void setEndDateTime(Timestamp endDateTime)
        {
            this.endDateTime = endDateTime;
        }

        /**
         * @return the price
         */
        public float getPrice()
        {
            return price;
        }

        /**
         * @param price
         *            the price to set
         */
        public void setPrice(float price)
        {
            this.price = price;
        }

        /**
         * @return the viaWaypoints
         */
        public String getViaWaypoints()
        {
            return viaWaypoints;
        }

        /**
         * @param viaWaypoints
         *            the viaWaypoints to set
         */
        public void setViaWaypoints(String viaWaypoints)
        {
            this.viaWaypoints = viaWaypoints;
        }

        /**
         * @return the numberOfSeats
         */
        public Integer getNumberOfSeats()
        {
            return numberOfSeats;
        }

        /**
         * @param numberOfSeats
         *            the numberOfSeats to set
         */
        public void setNumberOfSeats(Integer numberOfSeats)
        {
            this.numberOfSeats = numberOfSeats;
        }

        /**
         * @return the bookingUrl
         */
        public String getBookingUrl()
        {
            return bookingUrl;
        }

        /**
         * @param bookingUrl the bookingUrl to set
         */
        public void setBookingUrl(String bookingUrl)
        {
            this.bookingUrl = bookingUrl;
        }

    }

    protected Driver driver = new Driver();
    protected Vehicle vehicle = new Vehicle();
    protected Trip trip = new Trip();

    /**
     * @return the driver
     */
    public Driver getDriver()
    {
        return driver;
    }

    /**
     * @return the vehicle
     */
    public Vehicle getVehicle()
    {
        return vehicle;
    }

    /**
     * @return the trip
     */
    public Trip getTrip()
    {
        return trip;
    }

}
