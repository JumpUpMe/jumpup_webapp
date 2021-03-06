/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014 Sebastian Renner, Marco Seidler, Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.htw.fb4.imi.jumpup.Application;
import de.htw.fb4.imi.jumpup.Application.LogType;


/**
 * 
 * <p>
 * This class is a generalization for reading from a properties file.
 * </p>
 * <p>
 * ConfigReader classes in each module can extend it to avoid code duplication.
 * </p>
 * 
 * @author <a href="mailto:sascha.feldmann@gmx.de">Sascha Feldmann</a>
 * @since 20.11.2013
 * 
 */
public abstract class APropertiesConfigReader implements IConfigReader {
    public static final String PATH_CONFIG = "./conf/production/";
    public static final String PATH_CONFIG_UNITTEST = "./conf/unittest/";
    
    protected Properties properties;
    protected Map<String, String> propertiesMap;

    /**
     * Create a new config reader which reads from a '.property' - file. <br />
     * A critical log entry will be executed if the given file doesn't exist or
     * isn't accessible.
     * 
     * @param inputFile
     */
    public APropertiesConfigReader(File inputFile) {
        super();

        properties = new Properties();
        try {
            properties.load(new FileInputStream(inputFile));
        } catch (IOException e) { // log critical message
            Application.log(e.getLocalizedMessage(), LogType.CRITICAL, getClass());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValue(java
     * .lang.String)
     */
    public String fetchValue(String property) {
        String value = this.properties.getProperty(property.trim()
                .toLowerCase());

        if (null == value) {
            throw new IllegalArgumentException(
                    "ConfigReader::fetchValue(): No value found for the given property '"
                            + property + "'.");
        }
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchValues()
     */
    public Map<String, String> fetchValues() {
        if (null == this.propertiesMap) { // be lazy
            this.propertiesMap = new HashMap<String, String>();

            // fill properties map
            for (Object key : properties.keySet()) {
                Object property = properties.get(key);
                if (key instanceof String && property instanceof String) {
                    propertiesMap.put((String) key, (String) property);
                } else {
                    Application
                            .log("ConfigReader::fetchValues(): the properties keys or values are not strings.",
                                    LogType.ERROR,
                                    getClass());
                }
            }
        }

        return this.propertiesMap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.organization.IConfigReader#fetchMultipleValues
     * (java.lang.String)
     */
    public List<String> fetchMultipleValues(String propertyKey)
            {
        List<String> returnValues = new ArrayList<String>();
        Map<String, String> propertiesMap = this.fetchValues();

        /*
         * Check for properties with the format: a.b.c.d = property1 a.b.c.d.1 =
         * property2 a.b.c.d.2 = property3
         * 
         * An example input to get a list of the properties 1 - 3 would be:
         * a.b.c.d
         */
        for (String key : propertiesMap.keySet()) {
            // check if the propertyKey is the beginning of the one in the map
            // and the one in the map is followed by other characters (such as
            // ".anotherconfigkey")
            if (key.startsWith(propertyKey)
                    && key.substring(propertyKey.length(), key.length())
                            .length() > 0) {
                returnValues.add(propertiesMap.get(key));
            }
        }

        /*
         * Otherwise, check for properties with the format: a.b.c.d =
         * property1,property2,property3
         * 
         * The result would also be a list of properties 1 - 3.
         */
        // if the list is empty the config key doesn't point to a list of values
        if (0 == returnValues.size()) {
            // maybe the config key is single and the values are comma-separated
            if (propertiesMap.containsKey(propertyKey)) {
                String property = propertiesMap.get(propertyKey);
                String[] properties = property.split(",");
                returnValues.addAll(Arrays.asList(properties));
            }

            else {
                throw new IllegalArgumentException(
                        getClass()
                                + ":fetchMultipleValues: the given config key ("
                                + propertyKey
                                + ") doesn't point to a list of values.");
            }
        }

        return returnValues;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.bht.fb6.s778455.bachelor.organization.IConfigReader#getConfiguredClass
     * (java.lang.String)
     */
    public <T> T getConfiguredClass(String classPropertyKey, Object... params)
        {
        String classProperty = this.fetchValue(classPropertyKey);
        try { // try to instanciate an instance from the property value
            Class<?> className = Class.forName(classProperty);

            // fetch parameters' classes and hand it to the reflection method
            // getConstructor()
            Class<?>[] parameterTypes = new Class<?>[params.length];
            for (int i = 0; i < params.length; i++) {
                parameterTypes[i] = params[i].getClass();
            }
            Constructor<?> constructor = className
                    .getConstructor(parameterTypes);

            // try to create the given instance: a class cast exception will be
            // thrown in the case of no success
            @SuppressWarnings("unchecked")
            T strategyObject = (T) constructor.newInstance(params);
            return strategyObject;
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | ClassCastException e) {
            String errorMessage = "de.bht.fb6.s778455.bachelor.organization.APropertiesConfigReader.getConfiguredClass(): the instanciation of the given configured class (given value: "
                    + classPropertyKey
                    + ") was not successfull. Either it doesn't exist or doesn't extend the given class. Or the constructor raises an exception. Full exception: \n"
                    + e;
            throw new IllegalArgumentException(errorMessage);
        }
    }
    
    /**
     * Check if the value for the given config key is 'enabled'.
     * @param configKey
     * @return
     */
    public boolean isValueEnabled( final String configKey ) 
    {
        if ("enabled".equals(this.fetchValue(configKey).trim().toLowerCase())) {
            return true;
        }
        
        return false;
    }

}