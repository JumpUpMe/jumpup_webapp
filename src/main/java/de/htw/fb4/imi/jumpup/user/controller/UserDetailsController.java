package de.htw.fb4.imi.jumpup.user.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.controller.AbstractFacesController;
import de.htw.fb4.imi.jumpup.navigation.NavigationOutcomes;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.user.details.UserDetailsMethod;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.entity.UserDetails;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;
import de.htw.fb4.imi.jumpup.util.model.Gender;
import de.htw.fb4.imi.jumpup.util.model.Languages;

/**
 * Controller class for {@link de.htw.fb4.imi.jumpup.user.entity.UserDetails}.
 * 
 * @author Marco Seidler
 * 
 */

@Named(value = BeanNames.USER_DETAILS_CONTROLLER)
@SessionScoped
public class UserDetailsController extends AbstractFacesController implements
        Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 39062452564614352L;

    @Inject
    protected LoginSession loginSession;
    
    @Inject
    protected UserDetailsMethod userDetailsMethod;
    protected UserDetails newUserDetails = new UserDetails();
    protected Languages languages = Languages.GERMAN;
    protected Gender genders = Gender.MAN;

    /**
     * Get user details from current user instance OR fresh model.
     * 
     * @return
     */
    public UserDetails getUserDetails()
    {

        User currentUser = loginSession.getCurrentUser();
        Application.log("UserDetailsContoller: current user " + currentUser,
                LogType.DEBUG, getClass());

        try {

            if (null == currentUser.getUserDetails()) {
                return this.newUserDetails;
            }

            return currentUser.getUserDetails();
        } catch (Exception e) {
            Application.log("Exception here: " + e.getLocalizedMessage(),
                    LogType.CRITICAL, getClass());
            throw e;
        }
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

    /**
     * Method tries to add {@link UserDetails} and displays errors if something
     * fails.
     * 
     * @return the view for continuing
     *         {@link NavigationOutcomes.TO_USER_PROFILE}
     */
    public String editProfile()
    {
        try {
            Application.log("UserDetailsContoller: try to edit Profile",
                    LogType.DEBUG, getClass());
            UserDetailsMethod userDetailsMethod = getUserDetailsMethod();
            userDetailsMethod.sendUserDetails(getUserDetails());
            if (userDetailsMethod.hasError()) {
                for (String error : userDetailsMethod.getErrors()) {
                    this.addDisplayErrorMessage(error);
                }
            } else {
                // set current user details in session
                this.loginSession.getCurrentUser()
                        .setUserDetails(getUserDetails());
                addDisplayInfoMessage("Your given information have been saved.");
            }

        } catch (Exception e) {
            Application.log("UserDetailsController: " + e.getMessage(),
                    LogType.ERROR, getClass());
            this.addDisplayErrorMessage("Could not change your details.");
        }

        return NavigationOutcomes.TO_USER_PROFILE;
    }

    protected UserDetailsMethod getUserDetailsMethod()
    {
        return userDetailsMethod;
    }

    /**
     * Action for uploading the {@link User} avatar which is saved in his/her
     * related {@link UserDetails}.
     * 
     * @return {@link NavigationOutcomes.TO_USER_PROFILE}
     */
    public String uploadAvatar()
    {
        try {
            Application.log("UserDetailsContoller: try to upload avatar",
                    LogType.DEBUG, getClass());
            UserDetailsMethod userDetailsMethod = getUserDetailsMethod();
            userDetailsMethod.uploadAvatar(getUserDetails());
            if (userDetailsMethod.hasError()) {
                for (String error : userDetailsMethod.getErrors()) {
                    this.addDisplayErrorMessage(error);
                }
            } else {
                addDisplayInfoMessage("Your avatar have been saved.");
            }

        } catch (Exception e) {
            Application.log("UserDetailsController: " + e.getMessage(),
                    LogType.ERROR, getClass());
            this.addDisplayErrorMessage("Could not upload your avatar.");
        }

        return null;
    }
}
