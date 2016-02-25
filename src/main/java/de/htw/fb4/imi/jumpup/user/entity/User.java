/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.user.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import de.htw.fb4.imi.jumpup.booking.entity.Booking;
import de.htw.fb4.imi.jumpup.entity.AbstractEntity;
import de.htw.fb4.imi.jumpup.settings.ResidenceSettings;
import de.htw.fb4.imi.jumpup.settings.UserSettings;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.user.util.HashGenerable;

/**
 * <p>
 * The basic user entity.
 * </p>
 * 
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 06.11.2014
 * 
 */
@Entity
@Table(name = "user")
@NamedQueries({
        @NamedQuery(name = User.NAME_QUERY_BY_ID, query = "SELECT u FROM User u WHERE u.identity = :identity"),
        @NamedQuery(name = User.NAME_QUERY_BY_USERNAME, query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = User.NAME_QUERY_BY_EMAIL, query = "SELECT u FROM User u WHERE u.eMail = :email"),
        @NamedQuery(name = User.NAME_QUERY_LOGIN, query = "SELECT u FROM User u "
                + "WHERE (u.eMail = :token OR u.username = :token) "
                + "AND u.passwordHash = :passwordHash"), })
public class User extends AbstractEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = -4397896384511726160L;
    
    /**
     * Name of named query to fetch users by ID.
     */
    public static final String NAME_QUERY_BY_ID = "User.fetchByIdentity";
    /**
     * Name of named query to fetch users by username.
     */
    public static final String NAME_QUERY_BY_USERNAME = "User.fetchByUsername";
    /**
     * Only fetch by the unique eMail.
     */
    public static final String NAME_QUERY_BY_EMAIL = "User.fetchByEMail";
    /**
     * Login by parameters: token (might be a username or eMail) and
     * passwordHash
     */
    public static final String NAME_QUERY_LOGIN = "User.login";

    @OneToMany(mappedBy = "passenger")
    private List<Booking> bookings;

    @Column(name = "username", nullable = false, updatable = true, unique = true, length = UserSettings.MAX_LENGTH_USERNAME)
    protected String username;

    @Column(name = "email", nullable = false, updatable = true, unique = true, length = UserSettings.MAX_LENGTH_MAIL)
    protected String eMail;

    @Column(name = "prename", nullable = false, updatable = false)
    protected String prename;

    @Column(name = "lastname", nullable = false, updatable = false)
    protected String lastname;

    @Column(name = "passwordhash", nullable = false, updatable = true, columnDefinition = "BINARY(32)")
    protected byte[] passwordHash;

    @Column(name = "town", nullable = true, updatable = true, length = ResidenceSettings.MAX_LENGTH_TOWN)
    protected String town;

    @Column(name = "country", nullable = true, updatable = true, length = ResidenceSettings.MAX_LENGTH_COUNTRY)
    protected String country;

    @Column(name = "locale", nullable = true, updatable = true, length = ResidenceSettings.MAXL_LENGTH_LOCALE)
    protected String locale;

    @Column(name = "is_confirmed", nullable = false, updatable = true)
    protected Boolean isConfirmed;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    protected UserDetails userDetails;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    protected List<Trip> offeredTrips;

    /**
     * Hash generable.
     * 
     * TODO add hash generable by dependency injection.
     */
    @Transient
    protected HashGenerable hashGenerable;

    public User()
    {
        super();

        this.initalize();
    }

    private void initalize()
    {
        this.offeredTrips = new ArrayList<>();
    }

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
     * @return the eMail
     */
    public String geteMail()
    {
        return eMail;
    }

    /**
     * @param eMail
     *            the eMail to set
     */
    public void setEmail(String eMail)
    {
        this.eMail = eMail;
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
     * @return the passwordHash
     */
    public byte[] getPasswordHash()
    {
        return passwordHash;
    }

    /**
     * Set the raw password.
     * 
     * This will be hashed and save on this entity.
     * 
     * @param password
     */
    public void setPassword(String password)
    {
        if (null == this.hashGenerable) {
            throw new AssertionError(
                    "No hashGenerable instance given - can't generate password hash. Please make sure that the dependency injection is configured correctly.");
        }

        this.setPasswordHash(this.hashGenerable.generateHash(password));
    }

    /**
     * @param passwordHash
     *            the passwordHash to set
     */
    protected void setPasswordHash(byte[] passwordHash)
    {
        this.passwordHash = passwordHash;
    }

    /**
     * @return the isConfirmed
     */
    public Boolean getIsConfirmed()
    {
        return isConfirmed;
    }

    /**
     * @param isConfirmed
     *            the isConfirmed to set
     */
    public void setIsConfirmed(Boolean isConfirmed)
    {
        this.isConfirmed = isConfirmed;
    }

    /**
     * Set the {@link HashGenerable} instance which will be used to digest the
     * password.
     * 
     * @param newPasswordHashGenerable
     */
    public void setHashGenerable(final HashGenerable newPasswordHashGenerable)
    {
        this.hashGenerable = newPasswordHashGenerable;
    }

    /**
     * @return the userDetails
     */
    public UserDetails getUserDetails()
    {
        return userDetails;
    }

    /**
     * @param userDetails
     *            the userDetails to set
     */
    public void setUserDetails(UserDetails userDetails)
    {
        this.userDetails = userDetails;
    }

    /**
     * @param eMail
     *            the eMail to set
     */
    public void seteMail(String eMail)
    {
        this.eMail = eMail;
    }

    /**
     * @return the town
     */
    public final String getTown()
    {
        return town;
    }

    /**
     * @param town
     *            the town to set
     */
    public final void setTown(String town)
    {
        this.town = town;
    }

    /**
     * @return the country
     */
    public final String getCountry()
    {
        return country;
    }

    /**
     * @param country
     *            the country to set
     */
    public final void setCountry(String country)
    {
        this.country = country;
    }

    /**
     * @return the locale
     */
    public final String getLocale()
    {
        return locale;
    }

    /**
     * @param locale
     *            the locale to set
     */
    public final void setLocale(String locale)
    {
        this.locale = locale;
    }

    /**
     * @return the offeredTrips
     */
    public List<Trip> getOfferedTrips()
    {
        return offeredTrips;
    }

    /**
     * @param offeredTrips
     *            the offeredTrips to set
     */
    public void setOfferedTrips(List<Trip> offeredTrips)
    {
        this.offeredTrips = offeredTrips;
    }

    public List<Booking> getBookings()
    {
        return bookings;
    }

    public void setBookings(List<Booking> bookings)
    {
        this.bookings = bookings;
    }

    @Override
    public String toString()
    {
        return "User [username=" + username
                + ", eMail=" + eMail + ", prename=" + prename + ", lastname="
                + lastname + ", passwordHash=" + Arrays.toString(passwordHash)
                + ", town=" + town + ", country=" + country + ", locale="
                + locale + ", isConfirmed=" + isConfirmed + ", hashGenerable=" + hashGenerable + "]";
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((eMail == null) ? 0 : eMail.hashCode());
        result = prime * result
                + ((hashGenerable == null) ? 0 : hashGenerable.hashCode());
        result = prime * result
                + ((isConfirmed == null) ? 0 : isConfirmed.hashCode());
        result = prime * result
                + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result + ((locale == null) ? 0 : locale.hashCode());
        result = prime * result + Arrays.hashCode(passwordHash);
        result = prime * result + ((prename == null) ? 0 : prename.hashCode());
        result = prime * result + ((town == null) ? 0 : town.hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;        
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (eMail == null) {
            if (other.eMail != null)
                return false;
        } else if (!eMail.equals(other.eMail))
            return false;
        if (hashGenerable == null) {
            if (other.hashGenerable != null)
                return false;
        } else if (!hashGenerable.equals(other.hashGenerable))
            return false;
        if (isConfirmed == null) {
            if (other.isConfirmed != null)
                return false;
        } else if (!isConfirmed.equals(other.isConfirmed))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (locale == null) {
            if (other.locale != null)
                return false;
        } else if (!locale.equals(other.locale))
            return false;       
        if (!Arrays.equals(passwordHash, other.passwordHash))
            return false;
        if (prename == null) {
            if (other.prename != null)
                return false;
        } else if (!prename.equals(other.prename))
            return false;
        if (town == null) {
            if (other.town != null)
                return false;
        } else if (!town.equals(other.town))
            return false;   
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }

}
