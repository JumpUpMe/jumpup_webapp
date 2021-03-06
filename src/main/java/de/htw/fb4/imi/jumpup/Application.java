/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup;

import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.logging.Logger;

import de.htw.fb4.imi.jumpup.settings.PersistenceSettings;

/**
 * <p>
 * The central static facade of our application.
 * </p>
 * 
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 24.11.2014
 * 
 */
public class Application
{
    @PersistenceContext(name = PersistenceSettings.PERSISTENCE_UNIT)
    protected EntityManager entityManager;

    protected static final boolean debugMode;

    static {
        jBossLoggers = new HashMap<>();
        
        if (null != FacesContext.getCurrentInstance()) {
            String s = FacesContext.getCurrentInstance().getExternalContext()
                    .getInitParameter("javax.faces.PROJECT_STAGE");
    
            log("JSF Config Mode: " + s, LogType.INFO, Application.class);
    
            debugMode = s.equals("Development") ? true : false;
        } else {
            debugMode = true;
        }
    }

    /**
     * 
     * <p>
     * The LogType enumeration follows the strategy by implementing a log()
     * method for each strategy.
     * </p>
     * 
     * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
     * @since 24.11.2014
     * 
     */
    public enum LogType
    {
        INFO
        {
            @Override
            /*
             * (non-Javadoc)
             * 
             * @see
             * de.htw.fb4.imi.jumpup.Application.LogType#log(java.lang.String,
             * org.jboss.logging.Logger)
             */
            public <T> void log(final String message, final Logger jBossLogger)
            {
                jBossLogger.info(message);
            }
        },

        DEBUG
        {
            @Override
            /*
             * (non-Javadoc)
             * 
             * @see
             * de.htw.fb4.imi.jumpup.Application.LogType#log(java.lang.String,
             * org.jboss.logging.Logger)
             */
            public <T> void log(final String message, final Logger jBossLogger)
            {
                if (debugMode)
                    jBossLogger.info(message);
            }
        },

        WARNING
        {
            @Override
            /*
             * (non-Javadoc)
             * 
             * @see
             * de.htw.fb4.imi.jumpup.Application.LogType#log(java.lang.String,
             * org.jboss.logging.Logger)
             */
            public <T> void log(final String message, final Logger jBossLogger)
            {
                jBossLogger.debug(message);
            }
        },

        ERROR
        {
            @Override
            /*
             * (non-Javadoc)
             * 
             * @see
             * de.htw.fb4.imi.jumpup.Application.LogType#log(java.lang.String,
             * org.jboss.logging.Logger)
             */
            public <T> void log(final String message, final Logger jBossLogger)
            {
                jBossLogger.error(message);
            }
        },

        CRITICAL
        {
            @Override
            /*
             * (non-Javadoc)
             * 
             * @see
             * de.htw.fb4.imi.jumpup.Application.LogType#log(java.lang.String,
             * org.jboss.logging.Logger)
             */
            public <T> void log(final String message, final Logger jBossLogger)
            {
                jBossLogger.fatal(message);
            }
        };

        /**
         * Log the given message using the given JBoss Logger.
         * 
         * @param message
         * @param jBossLogger
         * @throws UnsupportedOperationException
         */
        public <T> void log(final String message, final Logger jBossLogger)
        {
            throw new UnsupportedOperationException(
                    "The enumeration value "
                            + this
                            + " doesn't implement the method log(). Please implement it!");
        }
    }

    /**
     * JBoss Loggers attached to the Java classes.
     */
    protected static Map<Class<?>, Logger> jBossLoggers;

    /**
     * Central facade method for using our logging system / adaption.
     * 
     * @param message
     * @param logType
     * @param clazz
     */
    public static <T> void log(final String message, final LogType logType,
            final Class<T> clazz)
    {
        addLoggerIfNotYetDone(clazz);

        // Log using JBoss
        logType.log(message, jBossLoggers.get(clazz));
    }

    /**
     * Add logger being lazy.
     * 
     * @param clazz
     */
    protected static <T> void addLoggerIfNotYetDone(final Class<T> clazz)
    {
        if (!jBossLoggers.containsKey(clazz)) {
            jBossLoggers.put(clazz, Logger.getLogger(clazz));
        }
    }
}
