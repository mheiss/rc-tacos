package at.rc.tacos.model;

import java.util.Calendar;


/**
 * Represents one StatusMessage
 * @author b.thek
 */

public class StatusMessages implements ITransportStatus
{
	private String status;
	private long timestamp;
	
	public StatusMessages(String status, long timestamp)
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
	/**
	 * @return the status
	 */
	public String getStatus() 
	{
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) 
	{
		if(status == null || status.trim().isEmpty())
	        throw new IllegalArgumentException("The status cannot be null or empty");
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
