package at.rc.tacos.core.service;

/**
 * The network layer defines the methods for the tacos net
 * @author Michael
 */
public interface INetLayer 
{
	/** Sets the session parameter for the communication */
	public void setSession(String sessionName);
	/** Sends the login request to the server */
	public void login(String username,String password);
	/** Sends the logout request to the server */
	public void logout();

	/** Notification that the item has changed **/
	public void itemChanged(String eventId,String oldItem,String newItem);
}
