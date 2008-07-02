package at.rc.tacos.server.net.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import at.rc.tacos.factory.PropertyManager;
import at.rc.tacos.model.Session;

/**
 * This class manages the available and connected users
 * @author Michael
 */
public class SessionManager extends PropertyManager 
{
	//the shared instance
	private static SessionManager instance;

	//the list of connected users
	private final List<Session> sessions;
	
	//THE EVENTS TO UPDATE THE VIEW
	/**
	 * A new session was added to the manager
	 */
	public final static String SESSION_ADDED = "sessionAdded";
	/**
	 * The session object was updated
	 */
	public final static String SESSION_UPDATED = "sessionUpdated";
	/**
	 * The session was removed from the manager
	 */
	public final static String SESSION_REMOVED = "sessionRemoved";
	/**
	 * The session was removed from the manager
	 */
	public final static String SESSION_CLEARED = "sessionCleared";
	

	/**
	 * Default class constructor
	 */
	private SessionManager()
	{
		sessions = Collections.synchronizedList(new ArrayList<Session>());
	}

	/**
	 * Returns the shared instance
	 */
	public static SessionManager getInstance()
	{
		if(instance == null)
			instance = new SessionManager();
		return instance;
	}

	//COMMON METHODS
	/**
	 * Adds a new user to the syncronized list and informs the view
	 */
	public void addUser(final Session onlineUser)
	{
		synchronized (onlineUser) 
		{
			sessions.add(onlineUser);
		}
		firePropertyChange(SESSION_ADDED, null, onlineUser); 
	}

	/**
	 * Removes the user from the synchronized list and informs the views
	 */
	public void removeSession(final Session onlineUser)
	{
		boolean removed = false;
		//remove the user session
		synchronized(sessions) 
		{
			ListIterator<Session> listIter = sessions.listIterator();
			while(listIter.hasNext())
			{ 
				Session listItem = listIter.next();
				if(listItem.equals(onlineUser))
				{
					listIter.remove();
					removed = true;
				}
			}
		}
		//check if the user is removed
		if(!removed)
			return;
		firePropertyChange(SESSION_REMOVED, onlineUser, null); 
	}
	
	/**
	 * Updates the given client session object and updates the views
	 */
	public void updateSession(final Session clientSession)
	{
		//try to get the session
		int index = sessions.indexOf(clientSession);
		if(index == -1)
			return;
		
		//update the object
		synchronized (clientSession)
		{
			sessions.set(index, clientSession);
		}
		//update the views
		firePropertyChange(SESSION_UPDATED, null, clientSession);
	}
	
	/**
	 * Removes all client session from the list of managed objects
	 */
	public void removeAllSessions()
	{
		synchronized (sessions) 
		{
			//remove all sessions
			ListIterator<Session> listIter = sessions.listIterator();
			while(listIter.hasNext())
			{
				listIter.next();
				listIter.remove();
			}	
		}
		//inform the views
		firePropertyChange(SESSION_CLEARED, null, null);
	}

	/**
	 * Returns the syncronized list of user object
	 * @return the list of users
	 */
	public List<Session> getClientSessions()
	{
		return sessions;
	}

	/**
	 * Converts the list to an array
	 * @return the list as a array
	 */
	public Object[] toArray()
	{
		return sessions.toArray();
	}
}
