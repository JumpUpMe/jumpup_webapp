/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.trip.creation;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.resource.spi.IllegalStateException;

import org.apache.commons.lang.exception.ExceptionUtils;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;
import de.htw.fb4.imi.jumpup.mail.MailAdapter;
import de.htw.fb4.imi.jumpup.mail.MailModel;
import de.htw.fb4.imi.jumpup.mail.builder.MailBuilder;
import de.htw.fb4.imi.jumpup.settings.BeanNames;
import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;
import de.htw.fb4.imi.jumpup.translate.Translatable;
import de.htw.fb4.imi.jumpup.trip.entities.Trip;
import de.htw.fb4.imi.jumpup.trip.util.ConfigReader;
import de.htw.fb4.imi.jumpup.trip.util.TripAndBookingsConfigKeys;
import de.htw.fb4.imi.jumpup.user.entities.User;
import de.htw.fb4.imi.jumpup.user.export.ILoginDependent;
import de.htw.fb4.imi.jumpup.user.login.LoginModel;
import de.htw.fb4.imi.jumpup.util.ErrorPrintable;
import de.htw.fb4.imi.jumpup.util.FileUtil;

/**
 * <p></p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 12.01.2015
 *
 */
@Stateless(name = BeanNames.WEBSITE_TRIP_CREATION)
public class WebsiteTripManagement implements TripManagementMethod, ErrorPrintable, ILoginDependent
{
    @PersistenceUnit(unitName = PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManagerFactory entityManagerFactory;
    
    @Inject
    protected MailBuilder mailBuilder;
    
    @Inject
    protected MailAdapter mailAdapter;  
    
    @Inject
    protected Translatable translator;
    
    @EJB(name = BeanNames.TRIP_CONFIG_READER)
    protected ConfigReader tripConfigReader;
    
    protected Set<String> errorMessages;

    private LoginModel loginModel;
    
    
    public WebsiteTripManagement()
    {
        super();
    }
    

    /**
     * Create fresh entity manager.
     * @return
     */
    protected EntityManager getFreshEntityManager()
    {
        EntityManager em = this.entityManagerFactory.createEntityManager();

        return em;
    }
    
    /**
     * Reset current state.
     */
    protected void reset()
    {
        this.errorMessages = new HashSet<>();
        this.mailBuilder.reset();
    }
    
    /**
     * 
     * @return
     * @throws IllegalStateException
     */
    protected User getCurrentUser() throws IllegalStateException
    {
        if (null == this.loginModel) {
            throw new IllegalStateException("loginModel was not injected.");
        }
        
        return this.loginModel.getCurrentUser();
    }
   
    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.ErrorPrintable#hasError()
     */
    @Override
    public boolean hasError()
    {
        return (getErrors().length > 0);
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.ErrorPrintable#getErrors()
     */
    @Override
    public String[] getErrors()
    {
        return this.errorMessages.toArray(new String[this.errorMessages.size()]);
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.util.ErrorPrintable#getSingleErrorString()
     */
    @Override
    public String getSingleErrorString()
    {
        // return first element if given
        String[] errors = this.getErrors();
        
        if (errors.length > 0 ) {
            return errors[0];
        }
        
        return "";
    }

    /* (non-Javadoc)
     * @see de.htw.fb4.imi.jumpup.trip.creation.TripCreationMethod#addTrip(de.htw.fb4.imi.jumpup.trip.entities.Trip)
     */
    @Override
    public void addTrip(final Trip trip)
    {
        this.reset();
        
        try {         
            this.persistTrip(trip);
            this.sendTripAddedMailToDriver(trip);
        } catch ( Exception e ) {
            Application.log("addTrip(): exception" + e.getMessage(), LogType.ERROR, getClass());
            this.errorMessages.add("We could not add the trip. Please inform our customer care team.");
        }
    }

    protected void sendTripAddedMailToDriver(final Trip trip)
    {        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_TRIP_CREATED_MAIL_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(trip.getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your trip was added"));
            
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendTripAddedMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            this.errorMessages.add("We could not send an confirmation mail.");
        } catch (Exception e) {
            Application.log("sendTripAddedMailToDriver(): error while sending trip updated mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            this.errorMessages.add("We could not send an confirmation mail.");
        }      
    }

    protected void buildTxtMail(String txtTemplateConfigKey)
    {
        File templateFile = new File(FileUtil.getPathToWebInfFolder(), this.tripConfigReader.fetchValue(txtTemplateConfigKey));
        this.mailBuilder.addPlainTextContent(templateFile);
    }

    protected void persistTrip(final Trip trip) throws IllegalStateException
    {
        EntityManager entityManager = this.getFreshEntityManager();
        User currentUser = this.getCurrentUser();
        
        // set driver to current logged in user and persist trip
        trip.setDriver(currentUser);
        currentUser.getOfferedTrips().add(trip);
        entityManager.persist(trip);              
        persistCurrentUser(entityManager, currentUser);
        entityManager.flush();
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
    public void changeTrip(final Trip trip)
    {
        this.reset();
        
        try {
            this.updateTrip(trip);
            this.sendTripUpdatedMailToDriver(trip);
            this.sendTripUpdatedMailToPassengers(trip);
        } catch ( Exception e ) {
            Application.log("changeTrip(): exception" + e.getMessage(), LogType.ERROR, getClass());
            this.errorMessages.add("We could not change the trip. Please inform our customer care team.");
        }
    }   

    private void updateTrip(Trip trip) throws IllegalStateException
    {
        Application.log("updateTrip(): saving trip " + trip, LogType.DEBUG, getClass());
        
        trip.setDriver(getCurrentUser());
        EntityManager entityManager = this.getFreshEntityManager();   
        entityManager.merge(trip);  
        entityManager.flush();
    }
    
    private void sendTripUpdatedMailToDriver(Trip trip)
    {        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_TRIP_CHANGED_MAIL_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(trip.getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your trip was changed"));
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendTripUpdatedMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            this.errorMessages.add("We could not send an confirmation mail.");
        } catch (Exception e) {
            Application.log("sendTripUpdatedMailToDriver(): error while sending trip updated mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            this.errorMessages.add("We could not send an confirmation mail.");
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
    public void cancelTrip(final Trip trip)
    {
        this.reset();
        
        try {
            this.softDeleteTrip(trip);
            this.sendTripCanceledMailToDriver(trip);
            this.sendTripCanceledMailToPassengers(trip);
        } catch ( Exception e ) {
            Application.log("cancelTrip(): exception" + e.getMessage(), LogType.ERROR, getClass());
            this.errorMessages.add("We could not cancel the trip. Please inform our customer care team.");
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
        
        EntityManager entityManager = this.getFreshEntityManager();
        
        trip.setCancelationDateTime(currentTimestamp);        

        entityManager.merge(trip);
        entityManager.flush();
    }      

    private void sendTripCanceledMailToDriver(Trip trip)
    {        
        try {
            buildTxtMail(TripAndBookingsConfigKeys.JUMPUP_TRIP_CANCELED_MAIL_DRIVER_TEMPLATE_TXT);
            MailModel m = this.mailBuilder.getBuildedMailModel()
                    .addRecipient(new InternetAddress(trip.getDriver().geteMail()))
                    .setSubject(this.translator.translate("JumpUp.Me - Your trip was canceled"));
            this.mailAdapter.sendHtmlMail(m);
        } catch (AddressException e) {
            Application.log("sendTripCanceledMailToDriver(): the recipient mail of the driver is malformed. Will not set the sender.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            this.errorMessages.add("We could not send an confirmation mail.");
        } catch (Exception e) {
            Application.log("sendTripAddedMailToDriver(): error while sending trip updated mail to driver.\nException: "
                    + e.getMessage() + "\n"
                    + ExceptionUtils.getFullStackTrace(e), LogType.ERROR, getClass());
            this.errorMessages.add("We could not send an confirmation mail.");
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

    @Override
    public LoginModel getLoginModel()
    {
       return this.loginModel;
    }

    @Override
    public void setLoginModel(LoginModel loginModel)
    {
       this.loginModel = loginModel;        
    }
}
