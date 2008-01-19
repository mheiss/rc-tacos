package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

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
	public String getLastChangedBy() {
		return lastChangedBy;
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
