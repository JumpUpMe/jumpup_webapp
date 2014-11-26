/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.user.registration;

/**
 * <p>Simple plain object holding the registration information.</p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 25.11.2014
 *
 */
public class RegistrationModel
{
    protected String username;
    protected String eMail;
    protected String confirmeMail;
    protected String prename;
    protected String lastname;
    protected String password;
    protected String confirmPassword;
    
    /**
     * @return the username
     */
    public final String getUsername()
    {
        return username;
    }
    /**
     * @param username the username to set
     */
    public final void setUsername(String username)
    {
        this.username = username;
    }
    /**
     * @return the eMail
     */
    public final String geteMail()
    {
        return eMail;
    }
    /**
     * @param eMail the eMail to set
     */
    public final void seteMail(String eMail)
    {
        this.eMail = eMail;
    }
    /**
     * @return the confirmeMail
     */
    public final String getConfirmeMail()
    {
        return confirmeMail;
    }
    /**
     * @param confirmeMail the confirmeMail to set
     */
    public final void setConfirmeMail(String confirmeMail)
    {
        this.confirmeMail = confirmeMail;
    }
    /**
     * @return the prename
     */
    public final String getPrename()
    {
        return prename;
    }
    /**
     * @param prename the prename to set
     */
    public final void setPrename(String prename)
    {
        this.prename = prename;
    }
    /**
     * @return the lastname
     */
    public final String getLastname()
    {
        return lastname;
    }
    /**
     * @param lastname the lastname to set
     */
    public final void setLastname(String lastname)
    {
        this.lastname = lastname;
    }
    /**
     * @return the password
     */
    public final String getPassword()
    {
        return password;
    }
    /**
     * @param password the password to set
     */
    public final void setPassword(String password)
    {
        this.password = password;
    }
    /**
     * @return the confirmPassword
     */
    public final String getConfirmPassword()
    {
        return confirmPassword;
    }
    /**
     * @param confirmPassword the confirmPassword to set
     */
    public final void setConfirmPassword(String confirmPassword)
    {
        this.confirmPassword = confirmPassword;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((confirmPassword == null) ? 0 : confirmPassword.hashCode());
        result = prime * result
                + ((confirmeMail == null) ? 0 : confirmeMail.hashCode());
        result = prime * result + ((eMail == null) ? 0 : eMail.hashCode());
        result = prime * result
                + ((lastname == null) ? 0 : lastname.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((prename == null) ? 0 : prename.hashCode());
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RegistrationModel other = (RegistrationModel) obj;
        if (confirmPassword == null) {
            if (other.confirmPassword != null)
                return false;
        } else if (!confirmPassword.equals(other.confirmPassword))
            return false;
        if (confirmeMail == null) {
            if (other.confirmeMail != null)
                return false;
        } else if (!confirmeMail.equals(other.confirmeMail))
            return false;
        if (eMail == null) {
            if (other.eMail != null)
                return false;
        } else if (!eMail.equals(other.eMail))
            return false;
        if (lastname == null) {
            if (other.lastname != null)
                return false;
        } else if (!lastname.equals(other.lastname))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (prename == null) {
            if (other.prename != null)
                return false;
        } else if (!prename.equals(other.prename))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("RegistrationModel [username=");
        builder.append(username);
        builder.append(", eMail=");
        builder.append(eMail);
        builder.append(", confirmeMail=");
        builder.append(confirmeMail);
        builder.append(", prename=");
        builder.append(prename);
        builder.append(", lastname=");
        builder.append(lastname);
        builder.append(", password=");
        builder.append(password);
        builder.append(", confirmPassword=");
        builder.append(confirmPassword);
        builder.append("]");
        return builder.toString();
    }        
}
