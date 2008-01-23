package at.rc.tacos.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Convinient helper methods for common use
 * @author Michael
 */
public class MyUtils 
{
    //formatters
    private final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private final static SimpleDateFormat timeAndDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    /**
     * Convinience helper method to ensure a long value is a valid date.
     * @param timestamp the value to check
     * @return true if the value is a date, otherwise false
     */
    public static boolean isValidDate(long timestamp)
    {
        try
        {
            //create a calendar entry
            Calendar cal = Calendar.getInstance();
            cal.setLenient(false);
            cal.setTimeInMillis(timestamp);
            cal.getTimeInMillis();
        }
        catch(Exception e)
        {
            return false;
        }
        //date is valid
        return true;
    }

    /**
     * Convinience helper method to ensure a given integer value is a valid year
     * @param year the value to test
     * @return true if the year is valid, otherwise false
     */
    public static boolean isValidYear(int year)
    {
        //create a calendar entry
        Calendar cal = Calendar.getInstance();
        System.out.println(cal.getMaximum(Calendar.YEAR));
        if( year > cal.getMaximum(Calendar.YEAR))
            return false;
        //date is valid
        return true;
    }

    /**
     * Returns weth the two given timestamps have the same day,month and year.
     * @param time1 the first timestamp
     * @param time2 the second timestamp to compare
     * @return true if the day month and year is equal, otherwise false
     */
    public static boolean isEqualDate(long time1,long time2)
    {
        //set the first time
        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(time1);
        //set the second time
        Calendar cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(time2);

        //compare
        if(cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
            return true;

        //the timestamps are not equal
        return false;
    }

    /**
     * Returns wheter or not the given string timevalues have the same day,year,month.
     * The format should be dd-MM-yyyy otherwise the method will always return false.
     * @param strTime1 the first time as string
     * @param strTime2 the second time as string
     * @return true if the times are equal otherwise false
     */
    public static boolean isEqualsDateString(String strTime1,String strTime2)
    {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        //try to parse the strings
        try
        {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(df.parse(strTime1));
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(df.parse(strTime2));

            //check the dates
            return MyUtils.isEqualDate(cal1.getTimeInMillis(), cal2.getTimeInMillis());	
        }
        catch(ParseException pe)
        {
            //failed to parse the dates
            return false;
        }
    }

    /**
     * Formats and returns the given timestamp as time string.<br>
     * The format options will be hh:mm
     * @param timestamp the time to format
     * @return the formatted string
     */
    public static String formatTime(long timestamp)
    {
        return timeFormat.format(new Date(timestamp));
    }

    /**
     * Formats and returns the given timestamp as date string.<br>
     * The format options will be dd-MM-yyyy
     * @param timestamp the time to format
     * @return the formatted string
     */
    public static String formatDate(long timestamp)
    {
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * Formats and returns the given timestamp as time and date string.<br>
     * The format options will be dd-MM-yyyy hh:mmm
     * @param timestamp the time to format
     * @return the formatted string
     */
    public static String formatTimeAndDate(long timestamp)
    {
        return timeAndDateFormat.format(new Date(timestamp));
    }

    /**
     * Parses the given time string into a timestamp.<br>
     * When the string is not parseable the mehtod will return -1
     * @param soureString the string to parse
     * @return the timestamp or -1 in case of an error
     */
    public final static long getTimestampFromTime(String time)
    {
        //set up the parser
        DateFormat formatter = timeFormat;
        formatter.setLenient(true);
        //try to parse
        try
        {
            return formatter.parse(time).getTime();
        }
        catch(ParseException pe)
        {
            System.out.println("Failed to validate the given time: "+time);
            return -1;
        }
    }

    /**
     * Parses the given date string into a timestamp.<br>
     * When the string is not parseable the mehtod will return -1
     * @param soureString the string to parse
     * @return the timestamp or -1 in case of an error
     */
    public final static long getTimestampFromDate(String time)
    {
        //set up the parser
        DateFormat formatter = dateFormat;
        formatter.setLenient(true);
        //try to parse
        try
        {
            return formatter.parse(time).getTime();
        }
        catch(ParseException pe)
        {
            System.out.println("Failed to validate the given date: "+time);
            return -1;
        }
    }
}
