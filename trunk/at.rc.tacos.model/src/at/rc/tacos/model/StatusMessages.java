package at.rc.tacos.model;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Represents one StatusMessage
 * @author b.thek
 */

public class StatusMessages implements ITransportStatus
{
	private int status;
	private long timestamp;
	private ArrayList<StatusMessages> statusMessageArray;
	
	public StatusMessages(int status, long timestamp)
	{
		this.status = status;
		this.timestamp = timestamp;
	}

	//METHODS
	/**
	 * Convinience helper method to ensure a long value
	 * is a valid date.
	 * @param timestamp the value to check
	 * @return true if the value is a date, otherwise false
	 */
	private boolean isValidDate(long timestamp)
	{
	    //create a calendar entry
	    Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(timestamp);
	    //check the year
	    if(cal.get(Calendar.YEAR) > 1960 && cal.get(Calendar.YEAR) < 2100)
	        return true;
	    //date out of range
	    return false;
	}
	
	public void addStatus(int statusId, long timestamp)
	{
		statusMessageArray.add(new StatusMessages(statusId,timestamp));
	}
	
	//SETTER & GETTER
	/**
	 * @return the status
	 */
	public int getStatus() 
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) 
	{
		if(status < 0 || status > 9)
	        throw new IllegalArgumentException("Invalid status value. Possible values: 1,2,3,4,5,6,7,8,9");
		this.status = status;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() 
	{
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) 
	{
		if(timestamp < 0)
	        throw new IllegalArgumentException("Date cannot be negative");
	    if(!isValidDate(timestamp))
	        throw new IllegalArgumentException("Date is out of range");
		this.timestamp = timestamp;
	}
	
}
