/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.user.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import de.htw.fb4.imi.jumpup.rest.response.model.AbstractRestModel;
import de.htw.fb4.imi.jumpup.util.model.Gender;

/**
 * <p>
 * Plain old object that will be (un-)marshalled and sent to REST callers.
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 07.12.2015
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class UserWebServiceModel extends AbstractRestModel
{
    public static final String FIELD_NAME_USERNAME = "username";
    public static final String FIELD_EMAIL = "eMail";
    public static final String FIELD_PRENAME = "prename";
    public static final String FIELD_LASTNAME = "lastname";
    public static final String FIELD_TOWN = "town";
    public static final String FIELD_COUNTRY = "country";
    public static final String FIELD_DATE_OF_BIRTH = "dateOfBirth";
    public static final String FIELD_PLACE_OF_BIRTH = "placeOfBirth";
    public static final String FIELD_MOBILE_NUMBER = "mobileNumber";
    public static final String FIELD_SKYPE = "skype";

    protected String username;
    protected String eMail;
    protected String prename;
    protected String lastname;
    protected String town;
    protected String country;
    protected String locale;
    protected Boolean isConfirmed;
    protected long dateOfBirth;
    protected String placeOfBirth;
    protected Gender gender;
    protected String mobileNumber;
    protected String skype;

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getUsername()
    {
        return username;
    }

    public void seteMail(String eMail)
    {
        this.eMail = eMail;
    }

    public String geteMail()
    {
        return eMail;
    }

    public void setPrename(String prename)
    {
        this.prename = prename;
    }

    public String getPrename()
    {
        return prename;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    public String getLastname()
    {
        return lastname;
    }

    public void setTown(String town)
    {
        this.town = town;
    }

    public String getTown()
    {
        return town;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getCountry()
    {
        return country;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setIsConfirmed(Boolean isConfirmed)
    {
        this.isConfirmed = isConfirmed;
    }

    public Boolean getIsConfirmed()
    {
        return isConfirmed;
    }

    public void setDateOfBirth(long dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public long getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth)
    {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPlaceOfBirth()
    {
        return placeOfBirth;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setSkype(String skype)
    {
        this.skype = skype;
    }

    public String getSkype()
    {
        return skype;
    }
}
