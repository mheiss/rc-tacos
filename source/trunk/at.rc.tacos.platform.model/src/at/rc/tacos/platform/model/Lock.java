package at.rc.tacos.platform.model;

/**
 * This object represents a single lock for a model object
 * @author Michael
 */
public class Lock extends AbstractMessage
{
	//the unique id
	public static String ID = "lock";
	
	//properties
	private String contentId;
	private String lockedId;
	private String lockedBy;
	private boolean hasLock;
	
	/**
	 * Default class constructor
	 */
	public Lock()
	{
		super(ID);
	}
	
	/**
	 * Class constructor to set up a new lock object using a string value to identify a locked entry
	 * @param contentId the type of the object to lock
	 * @param lockedBy the username who owns the lock
	 * @param lockedId the id of the element to lock
	 * @param hasLock a flag indication whether the lock is valid or not
	 */
	public Lock(String contentId,String lockedBy,String lockedId,boolean hasLock)
	{
		super(ID);
		this.contentId = contentId;
		this.lockedBy = lockedBy;
		this.lockedId = lockedId;
		this.hasLock = hasLock;
	}
	
	/**
	 * Class constructor to set up a new lock object using a integer value to identify a locked entry
	 * @param contentId the type of the object to lock
	 * @param lockedBy the username who owns the lock
	 * @param lockedId the id of the element to lock
	 * @param hasLock a flag indication whether the lock is valid or not
	 */
	public Lock(String contentId,String lockedBy,int lockedId,boolean hasLock)
	{
		super(ID);
		this.contentId = contentId;
		this.lockedBy = lockedBy;
		this.lockedId = String.valueOf(lockedId);
		this.hasLock = hasLock;
	}
	
	//METHODS
	/**
	 * Returns a human readable version of the lock objec
	 */
	public String toString()
	{
		return "Lock for "+contentId + ":"+lockedId+" - "+lockedBy+" -  status:"+hasLock;
	}
	
    /**
     * Returns the calculated hash code based on the contentId and the lockedId.<br>
     * Two logins have the same hash code if the username is the same.
     * @return the calculated hash code
     */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contentId == null) ? 0 : contentId.hashCode());
		result = prime * result
				+ ((lockedId == null) ? 0 : lockedId.hashCode());
		return result;
	}

	/**
     * Returns whether the objects are equal or not.<br>
     * Two locks are equal if, and only if, the content id and the lockedId is the same.
     * @return true if the lock is the same otherwise false.
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
		final Lock other = (Lock) obj;
		if (contentId == null) 
		{
			if (other.contentId != null)
				return false;
		} 
		else if (!contentId.equalsIgnoreCase(other.contentId))
			return false;
		if (lockedId == null) 
		{
			if (other.lockedId != null)
				return false;
		} 
		else if (!lockedId.equalsIgnoreCase(other.lockedId))
			return false;
		return true;
	}

	
	//GETTERS AND SETTERS
	public String getContentId() 
	{
		return contentId;
	}

	public void setContentId(String contentId) 
	{
		this.contentId = contentId;
	}

	public String getLockedBy() 
	{
		return lockedBy;
	}

	public void setLockedBy(String lockedBy) 
	{
		this.lockedBy = lockedBy;
	}

	public String getLockedId() 
	{
		return lockedId;
	}

	public void setLockedId(String lockedId) 
	{
		this.lockedId = lockedId;
	}

	public boolean isHasLock() 
	{
		return hasLock;
	}

	public void setHasLock(boolean hasLock) 
	{
		this.hasLock = hasLock;
	}
}
