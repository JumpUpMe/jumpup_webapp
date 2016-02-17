package de.htw.fb4.imi.jumpup.user.entity;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.Part;

import de.htw.fb4.imi.jumpup.entity.AbstractEntity;
import de.htw.fb4.imi.jumpup.util.model.Gender;
import de.htw.fb4.imi.jumpup.util.model.Languages;

@Entity
@Table(name = "user_details")
public class UserDetails extends AbstractEntity
{

    /**
     * 
     */
    private static final long serialVersionUID = -4235428005961187774L;

    @OneToOne(fetch = FetchType.LAZY)
    protected User user;

    @Column(name = "dateOfBirth", nullable = false, updatable = true)
    protected Date dateOfBirth;

    @Column(name = "placeOfBirth", nullable = false, updatable = true)
    protected String placeOfBirth;

    // @XmlAttribute(name = "avatarIdentity")
    // @XmlJavaTypeAdapter(EntityIdentityAdapter.class)
    // @ManyToOne
    // @JoinColumn(name = "avatarIdentity", nullable = true, updatable = true)
    // protected volatile Document avatar;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    protected Byte[] avatar;

    @ElementCollection(targetClass = Languages.class, fetch = FetchType.EAGER)
    @JoinTable(name = "languages", joinColumns = @JoinColumn(name = "identity") )
    @Column(name = "languages", nullable = true, updatable = true)
    @Enumerated(EnumType.ORDINAL)
    protected Set<Languages> languages;

    @Transient
    protected Set<String> stringLanguages;

    @Column(name = "gender", nullable = false, updatable = true)
    @Enumerated(EnumType.ORDINAL)
    protected Gender gender;

    @Column(name = "mobilenumber", nullable = false, updatable = true)
    protected String mobileNumber;

    @Column(name = "skype", nullable = false, updatable = true)
    protected String skype;

    @Transient
    protected Part avatarFile;

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Date getDateOfBirth()
    {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth)
    {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth()
    {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth)
    {
        this.placeOfBirth = placeOfBirth;
    }

    public Byte[] getAvatar()
    {
        return avatar;
    }

    public void setAvatar(Byte[] avatar)
    {
        this.avatar = avatar;
    }

    public Set<Languages> getLanguages()
    {
        return languages;
    }

    public void setLanguages(Set<Languages> languages)
    {
        this.languages = languages;
    }

    /**
     * @return the stringLanguages
     */
    public Set<String> getStringLanguages()
    {
        return stringLanguages;
    }

    /**
     * @param stringLanguages
     *            the stringLanguages to set
     */
    public void setStringLanguages(Set<String> stringLanguages)
    {
        this.stringLanguages = stringLanguages;

        HashSet<Languages> languagesSet = new HashSet<>();
        for (String language : stringLanguages) {
            languagesSet.add(Languages.valueOf(language));
        }
    }

    public Gender getGender()
    {
        return gender;
    }

    public void setGender(Gender gender)
    {
        this.gender = gender;
    }

    public String getMobileNumber()
    {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber)
    {
        this.mobileNumber = mobileNumber;
    }

    public String getSkype()
    {
        return skype;
    }

    public void setSkype(String skype)
    {
        this.skype = skype;
    }

    /**
     * @return the avatarFile
     */
    public Part getAvatarFile()
    {
        return avatarFile;
    }

    /**
     * Set the file which will be set into the avatar bytecode.
     * 
     * @param avatarFile
     *            the avatarFile to set
     */
    public void setAvatarFile(Part avatarFile)
    {
        this.avatarFile = avatarFile;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(avatar);
        result = prime * result
                + ((dateOfBirth == null) ? 0 : dateOfBirth.hashCode());
        result = prime * result + ((gender == null) ? 0 : gender.hashCode());
        result = prime * result
                + ((languages == null) ? 0 : languages.hashCode());
        result = prime * result
                + ((mobileNumber == null) ? 0 : mobileNumber.hashCode());
        result = prime * result
                + ((placeOfBirth == null) ? 0 : placeOfBirth.hashCode());
        result = prime * result + ((skype == null) ? 0 : skype.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        UserDetails other = (UserDetails) obj;
        if (!Arrays.equals(avatar, other.avatar))
            return false;
        if (dateOfBirth == null) {
            if (other.dateOfBirth != null)
                return false;
        } else if (!dateOfBirth.equals(other.dateOfBirth))
            return false;
        if (gender != other.gender)
            return false;
        if (languages != other.languages)
            return false;
        if (mobileNumber == null) {
            if (other.mobileNumber != null)
                return false;
        } else if (!mobileNumber.equals(other.mobileNumber))
            return false;
        if (placeOfBirth == null) {
            if (other.placeOfBirth != null)
                return false;
        } else if (!placeOfBirth.equals(other.placeOfBirth))
            return false;
        if (skype == null) {
            if (other.skype != null)
                return false;
        } else if (!skype.equals(other.skype))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    /**
     * Check whether the user's details are filled/completed or whether he still
     * needs to complete it.
     * 
     * @return
     */
    public boolean isFilled()
    {
        if (null == this.dateOfBirth
                // || null == this.avatar
                || null == this.gender || null == this.mobileNumber) {
            return false;
        }

        return true;
    }
}
