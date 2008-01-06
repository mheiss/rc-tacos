package at.rc.tacos.client.util;

import java.util.regex.Matcher;
/**
 * TimeValidator
 * validates the given time string
 * @author b.thek	
 */
import java.util.regex.Pattern;

public class TimeValidator 
{

	String checkStatus;
	String time;
	String field;
	
	
	
	public TimeValidator()
	{

	}
	
	public void checkTime(String time, String field)
	{
		this.time = time;
		this.field = field;
		
		Pattern p4 = Pattern.compile("(\\d{2})(\\d{2})");//if content is e.g. 1234
		Pattern p5 = Pattern.compile("(\\d{2}):(\\d{2})");//if content is e.g. 12:34
		
		//for each field
		if(!time.equalsIgnoreCase(""))
		{
			Matcher m41= p4.matcher(time);
			Matcher m51= p5.matcher(time);
				if(m41.matches())
				{
					int hour = Integer.parseInt(m41.group(1));
					int minutes = Integer.parseInt(m41.group(2));
					
					if(hour >= 0 && hour <=23 && minutes >= 0 && minutes <=59)
					{
						time = hour + ":" +minutes;//for the splitter
					}
					else
					{
						checkStatus = " " +field;
					}
				}
				else if(m51.matches())
				{
						int hour = Integer.parseInt(m51.group(1));
						int minutes = Integer.parseInt(m51.group(2));
					
					if(!(hour >= 0 && hour <=23 && minutes >= 0 && minutes <=59))
					{
						checkStatus = " " +field;
					}
				}
				else
				{
					checkStatus = " " +field;
				}
		}
		else checkStatus="";
	}
	
	public String getTime()
	{
		return time;
	}
	
	public String getCheckStatus()
	{
		return checkStatus;
	}
	
	
	
}
