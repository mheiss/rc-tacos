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

	String checkStatus="";
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
					System.out.println("TimeValidator---- im matcher für \"1234\"");
					int hour = Integer.parseInt(m41.group(1));
					int minutes = Integer.parseInt(m41.group(2));
					
					if(hour >= 0 && hour <=23 && minutes >= 0 && minutes <=59)
					{
						time = hour + ":" +minutes;//for the splitter
						System.out.println("TimeValidator---- im matcher für \"1234\"- die time nach ergänzung des doppelpunktes: " +time);
						this.setTime(time);
					}
					else
					{
						checkStatus = " " +field;
					}
				}
				else if(m51.matches())
				{
					System.out.println("TimeValidator---- im matcher für \"12:34\"");
						int hour = Integer.parseInt(m51.group(1));
						int minutes = Integer.parseInt(m51.group(2));
					
					if(!(hour >= 0 && hour <=23 && minutes >= 0 && minutes <=59))
					{
						checkStatus = " " +field;
					}
				}
				else
				{
					System.out.println("TimeValidator---- im else - also nix matcht");
					checkStatus = " " +field;
				}
		}
		else checkStatus="";
		System.out.println("TimeValidator---- ganz draussen im else");
	}
	
	public void setTime(String time)
	{
		this.time = time;
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
