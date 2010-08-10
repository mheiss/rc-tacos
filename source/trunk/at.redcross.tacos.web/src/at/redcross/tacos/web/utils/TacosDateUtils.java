package at.redcross.tacos.web.utils;

import java.util.Calendar;
import java.util.Date;

import at.redcross.tacos.web.beans.LocaleBean;
import at.redcross.tacos.web.faces.FacesUtils;

/**
 * The {@code DateUtils} provides static helper methods to work with
 * {@linkplain Date date and time}.
 */
public class TacosDateUtils {

    /**
     * Creates a new date from the provided separate date and time values.
     * 
     * @param date
     *            the date value
     * @param time
     *            the time value
     * @return a new date
     */
    public static Calendar mergeDateAndTime(Date date, Date time) {
        Calendar dCal = getCalendar(date.getTime());
        Calendar tCal = getCalendar(time.getTime());
        dCal.set(Calendar.HOUR_OF_DAY, tCal.get(Calendar.HOUR_OF_DAY));
        dCal.set(Calendar.MINUTE, tCal.get(Calendar.MINUTE));
        dCal.set(Calendar.SECOND, tCal.get(Calendar.SECOND));
        dCal.set(Calendar.MILLISECOND, tCal.get(Calendar.MILLISECOND));
        return dCal;
    }

    // returns a calendar instance using the current locale and timezone
    public static Calendar getCalendar(long timestamp) {
        LocaleBean bean = (LocaleBean) FacesUtils.lookupBean("localeBean");
        Calendar calendar = Calendar.getInstance(bean.getTimeZone(), bean.getLocale());
        calendar.setTimeInMillis(timestamp);
        return calendar;
    }

}
