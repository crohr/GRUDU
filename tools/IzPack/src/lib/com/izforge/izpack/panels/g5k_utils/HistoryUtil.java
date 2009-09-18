/**
 * 
 */
package com.izforge.izpack.panels.g5k_utils;

import java.util.GregorianCalendar;

/**
 * @author david
 *
 */
public class HistoryUtil {
    /**
     * Number of milliseconds for a second
     */
    public static final long SECOND = 1000;
    /**
     * Number of milliseconds for a minute
     */
    public static final long MINUTE = SECOND*60;
    /**
     * Number of milliseconds for an hour
     */
    public static final long HOUR   = 60*MINUTE;
    /**
     * Number of milliseconds for a day
     */
    public static final long DAY    = 24*HOUR;
    /**
     * Number of milliseconds for a week
     */
    public static final long WEEK   = 7*DAY;
    
    /**
     * array containing the blocks width corresponding to the durations for the Gantt chart
     */
    public static long[] blockWidthsArray = { MINUTE,
        2*MINUTE,
        5*MINUTE,
        10*MINUTE,
        30*MINUTE,
        HOUR,
        6*HOUR
    };
    /**
     * array containing the times corresponding to the durations for the Gantt chart
     */
    public static long[] durationsTimesArray = { 6*HOUR,
            12*HOUR,
            DAY,
            2*DAY,
            7*DAY,
            14*DAY,
            GregorianCalendar.getInstance().getActualMaximum(GregorianCalendar.DAY_OF_MONTH)*DAY
    };
}
