package at.rc.tacos.server.net;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import at.rc.tacos.factory.PropertyManager;
import at.rc.tacos.model.OnlineUser;
import at.rc.tacos.net.MySocket;

/**
 * This class manages the available and connected users
 * @author Michael
 */
public class OnlineUserManager extends PropertyManager 
{
	//the shared instance
	private static OnlineUserManager instance;

	//the list of connected users
	private final List<OnlineUser> onlineUsers;

	/**
	 * Default class constructor
	 */
	private OnlineUserManager()
	{
		onlineUsers = Collections.synchronizedList(new ArrayList<OnlineUser>());
	}

	/**
	 * Returns the shared instance
	 */
	public static OnlineUserManager getInstance()
	{
		if(instance == null)
			instance = new OnlineUserManager();
		return instance;
	}

	//COMMON METHODS
	/**
	 * Adds a new user to the syncronized list and informs the view
	 */
	public void addUser(final OnlineUser onlineUser)
	{
		onlineUsers.add(onlineUser);
		firePropertyChange("ONLINEUSER_ADDED", null, onlineUser); 
	}

	/**
	 * Removes the user from the synchronized list and informs the views
	 */
	public void removeUser(final OnlineUser onlineUser)
	{
		boolean removed = false;
		//remove the user session
		synchronized(onlineUsers) 
		{
			ListIterator<OnlineUser> listIter = onlineUsers.listIterator();
			while(listIter.hasNext())
			{ 
				OnlineUser listItem = listIter.next();
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

		onlineUsers.add(onlineUser);
		firePropertyChange("ONLINEUSER_REMOVED", onlineUser, null); 
	}

	//GETTERS
	/**
	 * Returns the user object accociated with the given socket object
	 * @return the user object or null if nothing found
	 */
	public OnlineUser getUserBySocket(MySocket socket)
	{
		//loop and search for the user object
		ListIterator<OnlineUser> listIter = onlineUsers.listIterator();
		while(listIter.hasNext())
		{ 
			OnlineUser listItem = listIter.next();
			if(listItem.getSocket() == socket)
				return listItem;
		}
		//nothing found
		return null;
	}

	/**
	 * Returns the syncronized list of user object
	 * @return the list of users
	 */
	public List<OnlineUser> getOnlineUsers()
	{
		return onlineUsers;
	}

	/**
	 * Converts the list to an array
	 * @return the list as a array
	 */
	public Object[] toArray()
	{
		return onlineUsers.toArray();
	}
}
