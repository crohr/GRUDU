/**
 * 
 */
package diet.grid.api.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    
    /**
     * Method returning the Date in the OAR Date format
     *
     * @param aDate a Date
     * @return the date formatted for OAR
     */
    public static String getOARDateFromDate(Date aDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(aDate);
    }
    
    /**
     * Method returning a walltime from a duration
     *
     * @param aTimeInMillis a duration
     * @return a walltime
     */
    public static String getWallTimeFromDate(long aTimeInMillis){
        int hours=(int)(aTimeInMillis/HistoryUtil.HOUR);
        long temp = aTimeInMillis - hours*HistoryUtil.HOUR;
        int minutes = (int)(temp/HistoryUtil.MINUTE);
        int seconds = (int)((temp - minutes*HistoryUtil.MINUTE)/HistoryUtil.SECOND);
        NumberFormat formatter = new DecimalFormat("##");
        String result = formatter.format(hours);
        result +=":" + formatter.format(minutes);
        result +=":" + formatter.format(seconds);
        return result;
    }
}
