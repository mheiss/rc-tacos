package at.redcross.tacos.web.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * The {@code DateUtils} provides static helper methods to work with
 * {@linkplain Date date and time}.
 */
public class DateUtils {

    /**
     * Creates a new date from the provided separate date and time values.
     * 
     * @param date
     *            the date value
     * @param time
     *            the time value
     * @return a new date
     */
    public static Date mergeDateAndTime(Date date, Date time) {
        Calendar dCal = Calendar.getInstance();
        dCal.setTime(date);
        Calendar tCal = Calendar.getInstance();
        tCal.setTime(time);
        dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
        dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
        dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
        dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
        return dCal.getTime();
    }

}
