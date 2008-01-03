package at.rc.tacos.client.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.graphics.Color;

/**
 * Contains convinient methods for common use
 * @author Michael
 */
public class Util 
{
	//formatters
	private final static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	private final static SimpleDateFormat timeAndDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
	
	
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
		DateFormat formatter = new SimpleDateFormat("hh:mm");
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
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
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
	
	/**
	 * Returns the color object for the given RGB values
	 * @param r the red value
	 * @param g the green value
	 * @param b the blue value
	 * @return the color object
	 */
	public final static Color getColor(int r,int g,int b)
	{
		return new Color(null,r,g,b);
	}
}
