/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.util;

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

	// 03.01.2008 21:54

	/**
	 * Convinience helper method to ensure a long value is a valid date.
	 * 
	 * @param timestamp
	 *            the value to check
	 * @return true if the value is a date, otherwise false
	 */
	public static boolean isValidDate(long timestamp) {
		try {
			// create a calendar entry
			Calendar cal = Calendar.getInstance();
			cal.setLenient(false);
			cal.setTimeInMillis(timestamp);
			cal.getTimeInMillis();
		}
		catch (Exception e) {
			return false;
		}
		// date is valid
		return true;
	}

	/**
	 * Convenience helper method to ensure a given integer value is a valid year
	 * 
	 * @param year
	 *            the value to test
	 * @return true if the year is valid, otherwise false
	 */
	public static boolean isValidYear(int year) {
		// create a calendar entry
		Calendar cal = Calendar.getInstance();
		if (year > cal.getMaximum(Calendar.YEAR))
			return false;
		// date is valid
		return true;
	}

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

		// compare
		if (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR))
			return true;

		// the timestamps are not equal
		return false;
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
	 * @param soureString
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
			return -1;
		}
	}
}
