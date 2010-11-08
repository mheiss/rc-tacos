package at.redcross.tacos.tests.query;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public class DateHelper {

    public static Date parseDate(String dateString) throws Exception {
        return parseCustom(dateString, "dd.MM.yyyy");
    }

    public static Date parseDateTime(String dateTimeString) throws Exception {
        return parseCustom(dateTimeString, "dd.MM.yyyy HH:mm");
    }

    public static Date parseCustom(String string, String format) throws Exception {
        return DateUtils.parseDate(string, new String[] { format });
    }

    public static String formatDate(Date date) {
        return formatCustom(date, "dd.MM.yyyy");
    }

    public static String formatCustom(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

}
