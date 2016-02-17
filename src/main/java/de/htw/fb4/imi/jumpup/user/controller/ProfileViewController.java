package de.htw.fb4.imi.jumpup.user.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.controller.AbstractFacesController;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.user.UserDAO;
import de.htw.fb4.imi.jumpup.user.details.UserDetailsMethod;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.entity.UserDetails;
import de.htw.fb4.imi.jumpup.util.model.Gender;
import de.htw.fb4.imi.jumpup.util.model.Languages;

/**
 * Controller class for {@link de.htw.fb4.imi.jumpup.user.entity.UserDetails}.
 * 
 * @author Marco Seidler
 * 
 */

@Named(value = BeanNames.PROFILE_VIEW_CONTROLLER)
@SessionScoped
public class ProfileViewController extends AbstractFacesController implements
        Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 39062452564614352L;

    @Inject
    protected UserDAO userDAO;
    @Inject
    protected UserDetailsMethod userDetailsMethod;

    protected UserDetails userDetails;

    protected Languages languages = Languages.GERMAN;
    protected Gender genders = Gender.MAN;
    protected String userID;
    protected User user;

    public UserDetails getUserDetails()
    {

        if (userID != null & userDetails == null) {
            try {
                user = userDAO.loadById(new Long(userID));
                Application.log("UserDetailsContoller: current user "
                        + user, LogType.DEBUG, getClass());
                userDetails = user.getUserDetails();

            } catch (Exception e) {
                Application.log("Exception here: " + e.getLocalizedMessage(),
                        LogType.CRITICAL, getClass());
                throw e;
            }
        }
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails)
    {
        this.userDetails = userDetails;
    }

    /**
     * @return the languages
     */
    public Languages getLanguages()
    {
        return languages;
    }

    /**
     * @param languages
     *            the languages to set
     */
    public void setLanguages(Languages languages)
    {
        this.languages = languages;
    }

    /**
     * @return the genders
     */
    public Gender getGenders()
    {
        return genders;
    }

    /**
     * @param genders
     *            the genders to set
     */
    public void setGenders(Gender genders)
    {
        this.genders = genders;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

}
