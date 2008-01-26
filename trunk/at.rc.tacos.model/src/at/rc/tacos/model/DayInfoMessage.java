package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.util.MyUtils;

/**
 * Info text for each day to display
 * @author Michael
 */
public class DayInfoMessage extends AbstractMessage
{
	//the id
	public final static String ID = "dayInfo";
	
	//properties
	private long timestamp;
	private String message;
	private String lastChangedBy;
	
	//additonal information
	private boolean dirty;
	
	/**
	 * Default class constructor
	 */
	public DayInfoMessage()
	{
		super(ID);
	}
	
	/**
	 * Default class constructor for a complete day info
	 */
	public DayInfoMessage(String message,long timestamp,String lastChangedBy)
	{
		super(ID);
		setMessage(message);
		setTimestamp(timestamp);
		setLastChangedBy(lastChangedBy);
	}
	
	//methods
	/**
	 * Returns the string based description of the message.
	 * @return the message and the meta data
	 */
	@Override
	public String toString()
	{
		return message + ": last changed: "+MyUtils.timestampToString(timestamp, MyUtils.dateFormat) +", by " +lastChangedBy +" local changes" + dirty;
	}

	/**
     * Returns the calculated hash code based on the day info message.<br>
     * Two day info messages have the same hash code if the timestamp is the same.
     * @return the calculated hash code
     */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}


	/**
     * Returns whether the objects are equal or not.<br>
     * Two day info messages are equal if, and only if, the timestamp is the same.
     * @return true if the message is the same otherwise false.
     */
	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DayInfoMessage other = (DayInfoMessage) obj;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}

	//getters and setters
	/**
	 * Returns the timestamp containing the day the message is assigned to
	 * @return the timestamp the day the message is assigned to
	 */
	public long getTimestamp() 
	{
		return timestamp;
	}

	/**
	 * Returns the message for the given day.
	 * @return the message
	 */
	public String getMessage() 
	{
		return message;
	}

	/**
	 * Returns the username of the member that changed the message at last.
	 * @return the lastChangedBy the username of the member
	 */
	public String getLastChangedBy() 
	{
		return lastChangedBy;
	}
	
	/**
	 * Returns whether the day info has local unsaved changes and should be
	 * saved in order to keep the changes.
	 * @return true if there are local changes
	 */
	public boolean isDirty()
	{
		return dirty;
	}
	
	/**
	 * Sets the flag to indicate that the day info was changed locally.
	 * @param dirty set to true to indicate local changes
	 */
	public void setDirty(boolean dirty)
	{
		this.dirty = dirty;
	}
	
	/**
	 * Sets the timestamp for which day the message is
	 * @param timestamp the day the message is assigned to
	 * @throws IllegalArgumentException if the date is not valid
	 */
	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
	}

	/**
	 * Sets the specifiy day info message
	 * @param message the message to set
	 * @throws IllegalArgumentException if the message is null
	 */
	public void setMessage(String message) 
	{
		this.message = message;
	}

	/**
	 * Sets the username of the member who lastly changed the message
	 * @param lastChangedBy the lastly edited username
	 * @throws IllegalArgumentException if the username is null
	 */
	public void setLastChangedBy(String lastChangedBy) 
	{
		this.lastChangedBy = lastChangedBy;
	}
}
