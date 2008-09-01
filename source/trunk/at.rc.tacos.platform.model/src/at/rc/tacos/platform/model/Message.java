package at.rc.tacos.platform.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the needed information about a message and the information to send the message
 * @author Michael
 */
public class Message 
{
	//properties of the message object
	private long timestamp;
	private int counter;
	private String username;
	private String sequenceId;
	private String contentType;
	private String queryString;
	private QueryFilter queryFilter;
	private List<AbstractMessage> messageList;

	/**
	 * Default class constructor to setup a new message object
	 */
	public Message() 
	{ 
		queryFilter = new QueryFilter();
		messageList = new ArrayList<AbstractMessage>();
	}

	//COMMON METHODS
	/**
	 * Sets one message object to send to the server
	 */
	public void addMessage(AbstractMessage message)
	{
		messageList.add(message);
	}
	
	@Override
	public String toString()
	{
		return "Message #"+sequenceId+" from " +username +"  content: "+contentType +" query:"+queryString;
	}
	
	//GETTERS AND SETTERS
	/**
	 * Returns how often this message has been send without getting a reply from the server
	 * @return the counter
	 */
	public int getCounter() 
	{
		return counter;
	}
	
	/**
	 * Returns the timestamp of the xml message
	 * @return the timestamp
	 */
	public long getTimestamp() 
	{
		return timestamp;
	}

	/**
	 * Returns the type of this xml message.<br>
	 * The type specifies the content of the message.<br>
	 * Examples for the type: <code>RosterEntry.ID</code><br>
	 * When you call <code>decode</code> you will get a list of
	 * Objects from the given message type.
	 * @return the messageType
	 */
	public String getContentType() 
	{
		return contentType;
	}

	/**
	 * Returns the queryString used in this xml message.<br>
	 * The queryString helps to categorize the message.<br>
	 * Examples for the query string <code>message.add</code><br>
	 * With this string you can process the data and set the actions needed.
	 * For example add the content to a list.
	 * @return the queryString
	 */
	public String getQueryString() 
	{
		return queryString;
	}

	/**
	 * Returns the applied query filter for the message.<br>
	 * The query filter is used to filter the result by specific values
	 * like the id, so that only results are listed with the id 1 for example.
	 * @return the applied filter
	 */
	public QueryFilter getQueryFilter()
	{
		return queryFilter;
	}
	
	/**
	 * @return the messageList
	 */
	public List<AbstractMessage> getMessageList() 
	{
		return messageList;
	}
	
	/**
	 * Returns the sequence number for that message.
	 * The sequence id can be used to identify the message object after the server response
	 * @return the used sequence number
	 */
	public String getSequenceId() 
	{
		return sequenceId;
	}
	
	/**
	 * Returns the username that owns the message
	 * @return the userId
	 */
	public String getUsername() 
	{
		return username;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) 
	{
		this.timestamp = timestamp;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) 
	{
		this.contentType = contentType;
	}

	/**
	 * @param queryString the queryString to set
	 */
	public void setQueryString(String queryString) 
	{
		this.queryString = queryString;
	}

	/**
	 * @param queryFilter the queryFilter to set
	 */
	public void setQueryFilter(QueryFilter queryFilter) 
	{
		this.queryFilter = queryFilter;
	}

	/**
	 * @param messageList the messageList to set
	 */
	public void setMessageList(List<AbstractMessage> messagList) 
	{
		this.messageList = messagList;
	}

	/**
	 * @param sequenceId the sequenceId to set
	 */
	public void setSequenceId(String sequenceId) 
	{
		this.sequenceId = sequenceId;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	/**
	 * @param counter the counter to set
	 */
	public void setCounter(int counter) 
	{
		this.counter = counter;
	}	
}
