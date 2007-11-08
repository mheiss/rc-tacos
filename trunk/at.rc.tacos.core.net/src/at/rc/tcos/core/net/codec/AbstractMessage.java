package at.rc.tcos.core.net.decoder;

/**
 * The base message for the tacos net protocol messages.
 * @author Michael
 */
public abstract class AbstractMessage 
{
	//the session
	private String userName;
	private long timestamp;
	private String messageAction;
	private String messageType;
	
	/**
	 * Default class constructor
	 */
	public AbstractMessage() { }
	
	//SETTERS AND GETTERS
	public String getUserName() 
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

	public String getMessageAction() 
	{
		return messageAction;
	}

	public void setMessageAction(String messageAction) 
	{
		this.messageAction = messageAction;
	}

	public long getTimestamp() 
	{
		return timestamp;
	}

	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
	}

	public String getMessageType() 
	{
		return messageType;
	}

	public void setMessageType(String messageType) 
	{
		this.messageType = messageType;
	}
}
