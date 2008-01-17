package at.rc.tacos.util;

import java.util.Calendar;

/**
 * Convinient helper methods for common use
 * @author Michael
 */
public class MyUtils 
{
	/**
	 * Convinience helper method to ensure a long value
	 * is a valid date.
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
		}
		catch(IllegalArgumentException ie)
		{
			return false;
		}
	    //date is valid
	    return true;
	}
}
