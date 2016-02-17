/**
 * JumpUp.Me Car Pooling Application
 *
 * Copyright (c) 2014-2016 Sascha Feldmann
 */
package de.htw.fb4.imi.jumpup.rest;

import java.sql.Timestamp;

/**
 * <p>
 * </p>
 *
 * @author <a href="mailto:me@saschafeldmann.de">Sascha Feldmann</a>
 * @since 17.02.2016
 *
 */
public class RestUtil
{
    /**
     * Get the timestamp to be sent in all web services responses. We send
     * timestamps in seconds, but Java deals with milliseconds.
     * 
     * 
     * @param timestampInMilliseconds
     * @return timestamp in seconds that should be returned by web services
     */
    public static long getTimestampForWebServiceRespones(
            long timestampInMilliseconds)
    {
        return timestampInMilliseconds / 1000;
    }

    /**
     * Get the Timestamp object to the given milliseconds timestamp that was
     * sent in a web service request.
     * 
     * @param d
     *            a timestamp in seconds
     * @return the Timestamp object which calculates with seconds
     */
    public static Timestamp convertTimestampFromWebserviceRequestToDatetime(
            Long d)
    {
        return new Timestamp(d * 1000);
    }
}
