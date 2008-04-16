package at.rc.tacos.common;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.model.QueryFilter;

/**
 * This class holds the information about the send items
 * @author Michael
 */
public class AbstractMessageInfo 
{
	//properties
	private int transmitted;
	private long timestamp;
	private String sequenceId;
	private String contentType;
	private String queryString;
	private QueryFilter queryFilter;
	private List<AbstractMessage> messageList;
	
	/**
	 * Default class constructor
	 */
	public AbstractMessageInfo()
	{
		
	}
	
	/**
	 * Sets one message object to send to the server
	 */
	public void setMessage(AbstractMessage message)
	{
		messageList = new ArrayList<AbstractMessage>();
		messageList.add(message);
	}
	
	@Override
	public String toString()
	{
		return "Package info: #"+sequenceId+" content: "+contentType +" query:"+queryString;
	}
	
	//GETTERS AND SETTERS
	/**
	 * @return the timestamp
	 */
	public long getTimestamp() 
	{
		return timestamp;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType() 
	{
		return contentType;
	}

	/**
	 * @return the queryString
	 */
	public String getQueryString() 
	{
		return queryString;
	}

	/**
	 * @return the queryFilter
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
	 * @return the sequenceId
	 */
	public String getSequenceId() 
	{
		return sequenceId;
	}
	
	/**
	 * @return the transmitted
	 */
	public int getTransmitted() 
	{
		return transmitted;
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
	 * @param transmitted the transmitted to set
	 */
	public void setTransmitted(int transmitted) 
	{
		this.transmitted = transmitted;
	}
}
