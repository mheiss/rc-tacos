package at.rc.tacos.model;

import java.util.Calendar;

import at.rc.tacos.net.MySocket;

/**
 * This represents a connected user 
 * @author Michael
 */
public class Session 
{
	//properties
	private String type;
	private String name;
	private long onlineSince;
	private MySocket socket;
	private Login login;
	
	//define the session types
	/**
	 * The current connection is a client
	 */
	public final static String SESSION_SERVER = "clientSession";
	/**
	 * The current connection is a server
	 */
	public final static String SESSION_CLIENT = "serverSession";
	
	/**
	 * Default class constructor.
	 * @param socket the connected socket
	 */
	public Session(MySocket socket)
	{
		this.socket = socket;
		this.onlineSince = Calendar.getInstance().getTimeInMillis();
	}
	
	//HELPER METHODS	
	/**
	 * Sets the current connection as server
	 */
	public void setServer(String serverName)
	{
		this.type = SESSION_SERVER;
		this.name = serverName;
	}
	
	/**
	 * Sets the current connection as client
	 */
	public void setClient()
	{
		this.type = SESSION_CLIENT;
		this.name = "client_"+getUsername();
	}
	
	/**
	 * Returns whether or not the session is authenticated 
	 * @return true if the session is session is authenticated
	 */
	public boolean isAuthenticated()
	{
		return login == null ? false : true;
	}
	
	/**
	 * Returns the username of the connected user
	 * @return the username of the authenticated user or anonymouse for not authenticated session
	 */
	public String getUsername()
	{
		//check if the client is authenticated
		if(!isAuthenticated())
			return "Anonymous";
		
		//return the username
		return login.getUsername();
	}
	
	//COMPARE METHODS
	/**
     * Returns whether the objects are equal or not.<br>
     * Two client sessions are equal if, and only if, the socket is the same.
     * @return true if the socket is equal
     */
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Session other = (Session) obj;
		if (socket == null) {
			if (other.socket != null)
				return false;
		} else if (!socket.equals(other.socket))
			return false;
		return true;
	}
	
	/**
     * Returns the calculated hash code based on the socket.<br>
     * Two client sessions have the same hash code if the socket is the same.
     * @return the calculated hash code
     */
	@Override
	public int hashCode() 
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((socket == null) ? 0 : socket.hashCode());
		return result;
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

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
