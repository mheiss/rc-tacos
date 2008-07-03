package at.rc.tacos.server.net;

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
	private Helo primaryServer;
	private Helo failbackServer;
	
	//THE SERVER EVENTS TO UPDATE THE VIEW
	public final static String PRIMARY_ONLINE = "primaryOnline";
	public final static String PRIMARY_OFFLINE = "primaryOffline";
	public final static String SECONDARY_ONLINE = "secondaryOnline";
	public final static String SECONDARY_OFFLINE = "secondaryOffline";
	
	/**
	 * Default class constructor
	 */
	private ServerManager() { }
	
	/**
	 * Returns the shared instance
	 */
	public static ServerManager getInstance()
	{
		if(instance == null)
			instance = new ServerManager();
		return instance;
	}
	
	//METHODS TO UPDATE THE VIEW
	/**
	 * Changes the status of the primary server
	 */
	public void primaryServerUpdate(Helo primaryServer)
	{
		//check the server update
		if(primaryServer == null)
			firePropertyChange(PRIMARY_OFFLINE, primaryServer,null);
		else
			firePropertyChange(PRIMARY_ONLINE, null, primaryServer);
		
		//save the update
		this.primaryServer = primaryServer;
	}
	
	/**
	 * Changes the status of the failback server
	 */
	public void failbackServerUpdate(Helo failbackServer)
	{
		//check the server update
		if(failbackServer == null)
			firePropertyChange(SECONDARY_OFFLINE, failbackServer,null);
		else
			firePropertyChange(SECONDARY_OFFLINE, null, failbackServer);
		
		//save the update
		this.failbackServer = failbackServer;
	}
	
	//GETTERS AND SETTERS
	/**
	 * @return the primaryServer
	 */
	public Helo getPrimaryServer() {
		return primaryServer;
	}

	/**
	 * @return the failbackServer
	 */
	public Helo getFailbackServer() {
		return failbackServer;
	}
}
