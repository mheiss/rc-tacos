package at.redcross.tacos.tests.query;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateHelper {

    public static Date parseDate(String dateString) throws Exception {
        return DateUtils.parseDate(dateString, new String[] { "dd.MM.yyyy" });
    }

    public static Date parseTime(String timeString) throws Exception {
        return DateUtils.parseDate(timeString, new String[] { "HH:mm" });
    }

    public static Date parseCustom(String string, String format) throws Exception {
        return DateUtils.parseDate(string, new String[] { format });
    }

}
