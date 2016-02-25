/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.ejb.creation;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.resource.spi.IllegalStateException;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.ApplicationUserException;
import de.htw.fb4.imi.jumpup.mail.MailAdapter;
import de.htw.fb4.imi.jumpup.mail.MailModel;
import de.htw.fb4.imi.jumpup.mail.builder.MailBuilder;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.translate.Translatable;
import de.htw.fb4.imi.jumpup.trip.jpa.entity.Trip;
import de.htw.fb4.imi.jumpup.trip.util.ConfigReader;
import de.htw.fb4.imi.jumpup.trip.util.TripAndBookingsConfigKeys;
import de.htw.fb4.imi.jumpup.user.entity.User;
import de.htw.fb4.imi.jumpup.user.login.LoginSession;
import de.htw.fb4.imi.jumpup.util.FileUtil;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 12.01.2015
 *
 */
@Stateful(name = BeanNames.WEBSITE_TRIP_CREATION)
public class WebsiteTripManagement implements TripManagementMethod, TripMails
{
    @PersistenceContext(unitName = PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManager entityManager;
    
    @Inject
    protected MailBuilder mailBuilder;
    
    @Inject
    protected MailAdapter mailAdapter;  
    
    @Inject
    protected Translatable translator;
    
    @EJB(beanName = BeanNames.TRIP_CONFIG_READER)
    protected ConfigReader tripConfigReader;
    
    @Inject
    protected LoginSession loginSession;
   
    
    public WebsiteTripManagement()
    {
        super();
    }

    /**
     * Reset current state.
     */
    protected void reset()
    {
        this.mailBuilder.reset();
    }
    
    /**
     * 
     * @return
     * @throws IllegalStateException
     */
    protected User getCurrentUser() throws IllegalStateException
    {
        return this.loginSession.getCurrentUser();
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.creation.TripCreationMethod#addTrip(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    @Override
    public void addTrip(final Trip trip) throws ApplicationUserException
    {
        this.reset();
        
        try {         
            this.persistTrip(trip);
        } catch ( Exception e ) {
            throw new ApplicationUserException("addTrip(): exception " + e.getMessage(), "We could not add the trip. Please inform our customer care team.");
        }
    }

    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.creation.TripMails#sendTripAddedMailToDriver(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    public void sendTripAddedMailToDriver(final Trip trip) throws ApplicationUserException
    {        
        this.reset();
        
        String userErrorMsg = "We could not send an confirmation mail.";
        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_TRIP_CREATED_MAIL_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(trip.getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your trip was added"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            String logMessage = "sendTripAddedMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            Application.log(logMessage, LogType.ERROR, getClass());
            
            throw new ApplicationUserException(logMessage, userErrorMsg);
        } catch (Exception e) {
            String logMessage = "sendTripAddedMailToDriver(): error while sending trip updated mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            Application.log(logMessage, LogType.ERROR, getClass());
            
            throw new ApplicationUserException(logMessage, userErrorMsg);
        }      
    }

    protected void buildTxtMail(String txtTemplateConfigKey)
    {
        File templateFile = new File(FileUtil.getPathToWebInfFolder(), this.tripConfigReader.fetchValue(txtTemplateConfigKey));
        this.mailBuilder.addPlainTextContent(templateFile);
    }

    protected void persistTrip(final Trip trip) throws IllegalStateException
    {
        User currentUser = this.getCurrentUser();
        
        // set driver to current logged in user and persist trip
        trip.setDriver(currentUser);
        currentUser.getOfferedTrips().add(trip);
        entityManager.persist(trip);              
        persistCurrentUser(entityManager, currentUser);
    }

    /**
     * Refresh currently logged in user to have actual database state and return the most recent offered trips for example.
     * @param entityManager
     * @param currentUser
     */
    protected void persistCurrentUser(EntityManager entityManager,
            User currentUser)
    {
        entityManager.persist(entityManager.contains(currentUser) ? currentUser : entityManager.merge(currentUser));
    }
   

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.creation.TripCreationMethod#changeTrip(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    @Override
    public void changeTrip(final Trip trip) throws ApplicationUserException
    {
        this.reset();
        
        try {
            this.updateTrip(trip);
        } catch ( Exception e ) {            
            throw new ApplicationUserException("changeTrip(): exception" + e.getMessage(), "We could not change the trip. Please inform our customer care team.");
        }
    }   

    private void updateTrip(Trip trip) throws IllegalStateException
    {        
        trip.setDriver(getCurrentUser()); 
        entityManager.merge(trip);  
    }
    
    @Override
    /*
     * (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.creation.TripMails#sendTripUpdatedMailToDriver(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    public void sendTripUpdatedMailToDriver(Trip trip) throws ApplicationUserException
    {        
        String userErrorMessage = "We could not send an confirmation mail.";
        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_TRIP_CHANGED_MAIL_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(trip.getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your trip was changed"));
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            String logMessage = "sendTripUpdatedMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            
            throw new ApplicationUserException(logMessage, userErrorMessage);
        } catch (Exception e) {
            String logMessage = "sendTripUpdatedMailToDriver(): error while sending trip updated mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            
            throw new ApplicationUserException(logMessage, userErrorMessage);
        }
    }

    private void sendTripUpdatedMailToPassengers(Trip trip)
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.creation.TripCreationMethod#cancelTrip(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    @Override
    public void cancelTrip(final Trip trip) throws ApplicationUserException
    {
        this.reset();
        
        try {
            this.softDeleteTrip(trip);
        } catch ( Exception e ) {
            throw new ApplicationUserException("cancelTrip(): exception" + e.getMessage(), "We could not cancel the trip. Please inform our customer care team.");    
        }
    }
  
    /**
     * Soft-Delete means: only cancel the trip. It will not appear in the listings anymore, but the entity still exists.
     * @param trip
     * @throws IllegalStateException 
     */
    private void softDeleteTrip(Trip trip) throws IllegalStateException
    {  
        Timestamp currentTimestamp = this.getCurrentTimestamp();
        Application.log("softDeleteTrip: cancelling trip with ID " + trip.getIdentity() + " and setting timestamp " + currentTimestamp.toString(), LogType.DEBUG, getClass());
        
        trip.setCancelationDateTime(currentTimestamp);        

        entityManager.merge(trip);
    }      

    @Override
    public void sendTripCanceledMailToDriver(Trip trip) throws ApplicationUserException
    {        
        String userErrorMessage = "We could not send an confirmation mail.";
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_TRIP_CANCELED_MAIL_DRIVER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(trip.getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your trip was canceled"));
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            String logMessage = "sendTripCanceledMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);
            
            throw new ApplicationUserException(logMessage, userErrorMessage);
            
        } catch (Exception e) {
            String logMessage = "sendTripAddedMailToDriver(): error while sending trip updated mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e);       
            
            throw new ApplicationUserException(logMessage, userErrorMessage);
        }                
    }
    
    private void sendTripCanceledMailToPassengers(Trip trip)
    {
        // TODO Auto-generated method stub        
    }
    
    private Timestamp getCurrentTimestamp()
    {
        return new Timestamp(new Date().getTime());
    }
}
