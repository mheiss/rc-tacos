package at.rc.tacos.client.ui.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TransformTimeToLong {

	private GregorianCalendar cal = new GregorianCalendar();

	public long transform(String stringTime) {
		if (stringTime.equalsIgnoreCase("")) {
			return 0;
		}

		String[] theTerm = stringTime.split(":");
		int hoursTerm = Integer.valueOf(theTerm[0]).intValue();
		int minutesTerm = Integer.valueOf(theTerm[1]).intValue();
		cal.set(Calendar.HOUR_OF_DAY, hoursTerm);
		cal.set(Calendar.MINUTE, minutesTerm);
		return cal.getTimeInMillis();
	}
}
