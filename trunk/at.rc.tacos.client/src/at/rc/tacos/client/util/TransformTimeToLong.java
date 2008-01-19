package at.rc.tacos.client.util;

import java.util.Calendar;
/**
 * TransformTimeToLong
 * @author b.thek
 */
import java.util.GregorianCalendar;

public class TransformTimeToLong 
{
	String stringTime;
	long longTime;
	
	GregorianCalendar cal = new GregorianCalendar();

	public TransformTimeToLong()
	{
		
	}
	
	public long transform(String stringTime)
	{
		if (!stringTime.equalsIgnoreCase(""))
		{
			System.out.println("TransformTimeToLong, im !\"\", die übergebene zeit als string: " +stringTime );
			String[] theTerm = stringTime.split(":");
			
			int hoursTerm = Integer.valueOf(theTerm[0]).intValue();
			int minutesTerm = Integer.valueOf(theTerm[1]).intValue();
			cal.set(Calendar.HOUR_OF_DAY, hoursTerm);
			cal.set(Calendar.MINUTE, minutesTerm);
			longTime = cal.getTimeInMillis();
			
			return longTime;
		}
		else
			return 0;
		
		
	}
}
