package at.rc.tacos.server.net.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import at.rc.tacos.factory.PropertyManager;
import at.rc.tacos.model.Helo;

/**
 * The server manager is responsible for the connected servers
 * @author Michael
 */
public class ServerManager extends PropertyManager 
{
	//the shared instance
	private static ServerManager instance;
	
	//the connected servers
	private List<Helo> serverList;
	
	//THE EVENTS TO UPDATE THE VIEW
	/**
	 * A new session was added to the manager
	 */
	public final static String SERVER_ADDED = "serverAdded";
	/**
	 * The session was removed from the manager
	 */
	public final static String SESSION_REMOVED = "serverRemoved";
	
	/**
	 * Default class constructor
	 */
	private ServerManager()
	{
		serverList = Collections.synchronizedList(new ArrayList<Helo>());
	}
	
	/**
	 * Returns the shared instance
	 */
	public static ServerManager getInstance()
	{
		if(instance == null)
			instance = new ServerManager();
		return instance;
	}
	
	//METHODS
	/**
	 * Adds a new server to the list of servers and updates the view
	 */
	public void addServer(Helo newServer)
	{
		synchronized (serverList) 
		{
			serverList.add(newServer);
		}
		firePropertyChange(SERVER_ADDED, null, newServer);
	}
	
	/**
	 * Removes the server from the list of servers and updates the view
	 */
	public void removeServer(Helo removeServer)
	{
		boolean removed = false;
		//remove the user session
		synchronized(serverList) 
		{
			ListIterator<Helo> listIter = serverList.listIterator();
			while(listIter.hasNext())
			{ 
				Helo listItem = listIter.next();
				if(listItem.equals(removeServer))
				{
					listIter.remove();
					removed = true;
				}
			}
		}
		//check if the user is removed
		if(!removed)
			return;
		firePropertyChange(SESSION_REMOVED, removeServer, null); 
	}

}
