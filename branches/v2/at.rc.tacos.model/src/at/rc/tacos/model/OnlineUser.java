package at.rc.tacos.model;

import java.util.Calendar;

import at.rc.tacos.net.MySocket;


/**
 * This represents a connected user 
 * @author Michael
 */
public class OnlineUser 
{
	//properties
	private long onlineSince;
	private MySocket socket;
	private Login login;
	
	/**
	 * Default class constructor.
	 * @param socket the connected socket
	 */
	public OnlineUser(MySocket socket)
	{
		this.socket = socket;
		this.onlineSince = Calendar.getInstance().getTimeInMillis();
	}
	
	//HELPER METHODS
	/**
	 * Returns whether or not the user is authenticated 
	 * @return
	 */
	public boolean isAuthenticated()
	{
		return login == null ? false : true;
	}
	
	/**
	 * Returns the username of the connected user
	 * @return
	 */
	public String getUsername()
	{
		//check if the client is authenticated
		if(!isAuthenticated())
			return "Anonymouse";
		
		//return the username
		return login.getUsername();
	}

	//GETTERS AND SETTERS
	public long getOnlineSince() {
		return onlineSince;
	}

	public void setOnlineSince(long onlineSince) {
		this.onlineSince = onlineSince;
	}

	public MySocket getSocket() {
		return socket;
	}

	public void setSocket(MySocket socket) {
		this.socket = socket;
	}

	public Login getLogin() {
		return login;
	}

	public void setLogin(Login login) {
		this.login = login;
	}
}
