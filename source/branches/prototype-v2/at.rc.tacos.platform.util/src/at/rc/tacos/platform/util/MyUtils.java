package at.rc.tacos.platform.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Convenient helper methods for common use
 * 
 * @author Michael
 */
public class MyUtils {

	// formatters
	public final static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	public final static SimpleDateFormat timeAndDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	public final static SimpleDateFormat timeAndDateFormatShort = new SimpleDateFormat("dd.MM.yy HH:mm");

	// sql date
	public final static SimpleDateFormat sqlTime = new SimpleDateFormat("HH:mm:ss");
	public final static SimpleDateFormat sqlDate = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat sqlDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public final static SimpleDateFormat sqlServerDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

	/**
	 * Returns whether the two given timestamps have the same day,month and
	 * year.
	 * 
	 * @param time1
	 *            the first timestamp
	 * @param time2
	 *            the second timestamp to compare
	 * @return true if the day month and year is equal, otherwise false
	 */
	public static boolean isEqualDate(long time1, long time2) {
		// set the first time
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(time1);
		// set the second time
		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(time2);
		
		// compare day
		if (cal1.get(Calendar.DAY_OF_MONTH) != cal2.get(Calendar.DAY_OF_MONTH)) {
			return false;
		}
		//compare month
		if(cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)) {
			return false;
		}
		//compare year
		if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)) {
			return false;
		}

		// the timestamps are equal
		return true;
	}

	/**
	 * Returns whether or not the given string timevalues have the same
	 * day,year,month. The format should be dd-MM-yyyy otherwise the method will
	 * always return false.
	 * 
	 * @param strTime1
	 *            the first time as string
	 * @param strTime2
	 *            the second time as string
	 * @return true if the times are equal otherwise false
	 */
	public static boolean isEqualsDateString(String strTime1, String strTime2) {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		// try to parse the strings
		try {
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(df.parse(strTime1));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(df.parse(strTime2));

			// check the dates
			return MyUtils.isEqualDate(cal1.getTimeInMillis(), cal2.getTimeInMillis());
		}
		catch (ParseException pe) {
			// failed to parse the dates
			return false;
		}
	}

	/**
	 * Formats and returns the given timestamp as date string.<br>
	 * The format options will must be given
	 * 
	 * @param timestamp
	 *            the time to format
	 * @param dateFormat
	 *            the format of the date to use
	 * @return the formatted string
	 */
	public static String timestampToString(long timestamp, SimpleDateFormat dateFormat) {
		if (timestamp == 0)
			return null;
		String string = dateFormat.format(new Date(timestamp));
		return string;
	}

	/**
	 * Parses the given time string into a timestamp.<br>
	 * The parsing string must be specified <br>
	 * When the string is not parseable the mehtod will return -1
	 * 
	 * @param time
	 *            the string to parse
	 * @param dateFormat
	 *            the format of the date
	 * @return the timestamp or -1 in case of an error
	 */
	public final static long stringToTimestamp(String time, SimpleDateFormat dateFormat) {
		if (time == null)
			return -1;

		// set up the parser
		DateFormat formatter = dateFormat;
		// try to parse
		try {
			long timestamp = formatter.parse(time).getTime();
			return timestamp;
		}
		catch (ParseException pe) {
			System.out.println("Failed to validate the given time: " + time);
			return -1;
		}
	}
}
