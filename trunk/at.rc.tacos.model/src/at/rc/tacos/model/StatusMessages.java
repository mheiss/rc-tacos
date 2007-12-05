package at.rc.tacos.model;

import java.util.Calendar;

/**
 * Represents a StatusMessage
 * @author b.thek
 */
public class StatusMessages implements ITransportStatus
{
	private int status;
	private long timestamp;
	
	/**
	 * Default constructor for a status message
	 * @param status the status message to set
	 * @param timestamp the timestamp when the status was set
	 * @see ITransportStatus
	 */
	public StatusMessages(int status, long timestamp)
	{
		setStatus(status);
		setTimestamp(timestamp);
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
