/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2015 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.user.rest.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import de.htw.fb4.imi.jumpup.rest.IEntityMapper;
import de.htw.fb4.imi.jumpup.rest.RestUtil;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.entity.UserDetails;
import de.htw.fb4.imi.jumpup.user.registration.RegistrationModel;
import de.htw.fb4.imi.jumpup.validation.ValidationException;
import de.htw.fb4.imi.jumpup.validation.validator.JumpUpValidator;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 07.12.2015
 *
 */
@Named(value = BeanNames.USER_ENTITY_MAPPER)
@RequestScoped
public class UserEntityMapper
        implements IEntityMapper<UserWebServiceModel, User>
{
    @Inject
    @Named(BeanNames.USERNAME_VALIDATOR)
    protected JumpUpValidator usernameValidator;

    @Inject
    @Named(BeanNames.EMAIL_VALIDATOR)
    protected JumpUpValidator emailValidator;

    @Inject
    @Named(BeanNames.PRENAME_VALIDATOR)
    protected JumpUpValidator prenameValidator;

    @Inject
    @Named(BeanNames.LASTNAME_VALIDATOR)
    protected JumpUpValidator lastNameValidator;

    @Inject
    @Named(BeanNames.TOWN_VALIDATOR)
    protected JumpUpValidator townValidator;

    @Inject
    @Named(BeanNames.COUNTRY_VALIDATOR)
    protected JumpUpValidator countryValidator;

    @Inject
    @Named(BeanNames.DATE_OF_BIRTH)
    protected JumpUpValidator dateOfBirthValidator;

    @Inject
    @Named(BeanNames.PLACE_OF_BIRTH_VALIDATOR)
    protected JumpUpValidator placeOfBirthValidator;

    @Inject
    @Named(BeanNames.MOBILE_NUMBER_VALIDATOR)
    protected JumpUpValidator mobileNumberValidator;

    @Inject
    @Named(BeanNames.SKYPE_VALIDATOR)
    protected JumpUpValidator skypeValidator;

    @Inject
    @Named(BeanNames.PASSWORD_VALIDATOR)
    protected JumpUpValidator passwordValidator;

    @Inject
    @Named(BeanNames.REPEAT_PASSWORD_VALIDATOR)
    protected JumpUpValidator passwordConfirmValidator;

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapEntity(de.htw.fb4.imi.jumpup.
     * entities.AbstractEntity)
     */
    public UserWebServiceModel mapEntity(User entity)
    {
        UserWebServiceModel webServiceModel = new UserWebServiceModel();

        webServiceModel.setIdentity(entity.getIdentity());
        webServiceModel.setUsername(entity.getUsername());
        webServiceModel.seteMail(entity.geteMail());
        webServiceModel.setPrename(entity.getPrename());
        webServiceModel.setLastname(entity.getLastname());
        webServiceModel.setTown(entity.getTown());
        webServiceModel.setCountry(entity.getCountry());
        webServiceModel.setLocale(entity.getLocale());
        webServiceModel.setIsConfirmed(entity.getIsConfirmed());

        UserDetails userDetails = entity.getUserDetails();

        if (null != userDetails && userDetails.isFilled()) {
            webServiceModel
                    .setDateOfBirth(RestUtil.getTimestampForWebServiceRespones(
                            userDetails.getDateOfBirth().getTime()));
            webServiceModel.setPlaceOfBirth(userDetails.getPlaceOfBirth());
            webServiceModel.setGender(userDetails.getGender());
            webServiceModel.setMobileNumber(userDetails.getMobileNumber());
            webServiceModel.setSkype(userDetails.getSkype());
        }

        return webServiceModel;
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see
     * de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapWebServiceModel(de.htw.fb4.
     * imi.jumpup.rest.AbstractRestModel)
     */
    public User mapWebServiceModel(UserWebServiceModel webServiceModel)
            throws ValidationException
    {
        User entity = new User();

        this.validateUsername(webServiceModel.getUsername());
        entity.setUsername(webServiceModel.getUsername());

        this.validateMail(webServiceModel.geteMail());
        entity.seteMail(webServiceModel.geteMail());

        this.validatePrename(webServiceModel.getPrename());
        entity.setPrename(webServiceModel.getPrename());

        this.validateLastname(webServiceModel.getLastname());
        entity.setLastname(webServiceModel.getLastname());

        this.validateTown(webServiceModel.getTown());
        entity.setTown(webServiceModel.getTown());

        this.validateCountry(webServiceModel.getCountry());
        entity.setCountry(webServiceModel.getCountry());

        entity.setLocale(webServiceModel.getLocale());
        entity.setIsConfirmed(webServiceModel.getIsConfirmed());

        UserDetails userDetails = entity.getUserDetails();

        if (null == userDetails) {
            userDetails = new UserDetails();
            entity.setUserDetails(userDetails);
        }

        Timestamp dob = RestUtil
                .convertTimestampFromWebserviceRequestToDatetime(
                        webServiceModel.getDateOfBirth());
        this.validateDateOfBirth(dob);
        userDetails.setDateOfBirth(dob);

        this.validatePlaceOfBirth(webServiceModel.getPlaceOfBirth());
        userDetails.setPlaceOfBirth(webServiceModel.getPlaceOfBirth());

        userDetails.setGender(webServiceModel.getGender());

        this.validateMobileNumber(webServiceModel.getMobileNumber());
        userDetails.setMobileNumber(webServiceModel.getMobileNumber());

        this.validateSkype(webServiceModel.getSkype());
        userDetails.setSkype(webServiceModel.getSkype());

        return entity;
    }

    private void validateUsername(String username) throws ValidationException
    {
        if (!this.usernameValidator.validate(username)) {
            throw new ValidationException(
                    UserWebServiceModel.FIELD_NAME_USERNAME,
                    this.usernameValidator.getErrorMessages());
        }
    }

    private void validateMail(String email) throws ValidationException
    {
        if (!this.emailValidator.validate(email)) {
            throw new ValidationException(UserWebServiceModel.FIELD_EMAIL,
                    this.emailValidator.getErrorMessages());
        }
    }

    private void validatePrename(String prename) throws ValidationException
    {
        if (!this.prenameValidator.validate(prename)) {
            throw new ValidationException(UserWebServiceModel.FIELD_PRENAME,
                    this.prenameValidator.getErrorMessages());
        }
    }

    private void validateLastname(String lastname) throws ValidationException
    {
        if (!this.lastNameValidator.validate(lastname)) {
            throw new ValidationException(UserWebServiceModel.FIELD_LASTNAME,
                    this.lastNameValidator.getErrorMessages());
        }
    }

    private void validateTown(String town) throws ValidationException
    {
        if (!this.townValidator.validate(town)) {
            throw new ValidationException(UserWebServiceModel.FIELD_TOWN,
                    this.townValidator.getErrorMessages());
        }
    }

    private void validateCountry(String country) throws ValidationException
    {
        if (!this.countryValidator.validate(country)) {
            throw new ValidationException(UserWebServiceModel.FIELD_COUNTRY,
                    this.countryValidator.getErrorMessages());
        }
    }

    private void validateDateOfBirth(Date dateOfBirth)
            throws ValidationException
    {
        if (!this.dateOfBirthValidator.validate(dateOfBirth)) {
            throw new ValidationException(
                    UserWebServiceModel.FIELD_DATE_OF_BIRTH,
                    this.dateOfBirthValidator.getErrorMessages());
        }
    }

    private void validatePlaceOfBirth(String placeOfBirth)
            throws ValidationException
    {
        if (!this.placeOfBirthValidator.validate(placeOfBirth)) {
            throw new ValidationException(
                    UserWebServiceModel.FIELD_PLACE_OF_BIRTH,
                    this.placeOfBirthValidator.getErrorMessages());
        }
    }

    private void validateMobileNumber(String mobileNumber)
            throws ValidationException
    {
        if (!this.mobileNumberValidator.validate(mobileNumber)) {
            throw new ValidationException(
                    UserWebServiceModel.FIELD_MOBILE_NUMBER,
                    this.mobileNumberValidator.getErrorMessages());
        }
    }

    private void validateSkype(String skype) throws ValidationException
    {
        if (!this.skypeValidator.validate(skype)) {
            throw new ValidationException(UserWebServiceModel.FIELD_SKYPE,
                    this.skypeValidator.getErrorMessages());
        }
    }

    private void validatePassword(String password) throws ValidationException
    {
        if (!this.passwordValidator.validate(password)) {
            throw new ValidationException(RegistrationModel.FIELD_PASSWORD,
                    this.passwordValidator.getErrorMessages());
        }
    }

    private void validateConfirmPassword(String password,
            String confirmPassword) throws ValidationException
    {
        String[] values = { confirmPassword, password };

        if (!this.passwordConfirmValidator.validate(values)) {
            throw new ValidationException(
                    RegistrationModel.FIELD_PASSWORD_CONFIRM,
                    this.passwordValidator.getErrorMessages());
        }
    }

    @Override
    /*
     * (non-Javadoc)
     * 
     * @see de.htw.fb4.imi.jumpup.rest.IEntityMapper#mapEntities(java.util.
     * Collection)
     */
    public Collection<UserWebServiceModel> mapEntities(
            Collection<User> entityTypes)
    {
        Collection<UserWebServiceModel> userWebServiceCollection = new ArrayList<UserWebServiceModel>();

        for (User user : entityTypes) {
            userWebServiceCollection.add(this.mapEntity(user));
        }

        return userWebServiceCollection;
    }

    /**
     * Validate the given registration model.
     * 
     * @param restModel
     * @throws ValidationException
     */
    public void validateRegistration(RegistrationModel restModel)
            throws ValidationException
    {
        this.validateUsername(restModel.getUsername());
        this.validateMail(restModel.geteMail());
        this.validatePrename(restModel.getPrename());
        this.validateLastname(restModel.getLastname());
        this.validatePassword(restModel.getPassword());
        this.validateConfirmPassword(restModel.getPassword(),
                restModel.getConfirmPassword());
    }

}
