package at.rc.tacos.web.web;

import at.rc.tacos.core.net.internal.WebClient;

/**
 * Session information
 * @author Nechan
 */
public class UserSession 
{
	private Boolean loggedIn;
	private String username;
	private WebClient connection;

	/**
	 * Default constructor
	 */
	public UserSession()
	{
		loggedIn = false;
	}

	/**
	 * Returns whether the user is logged in or not.
	 * @return true if the user is logged in
	 */
	public Boolean getLoggedIn()
	{
		return loggedIn;
	}

	/**
	 * Logs in the current user and stores the information
	 * about the user.
	 * @param loggedIn true if the login was successfully
	 * @param userData the information about the user
	 */
	public void setLoggedIn(Boolean loggedIn,String username,WebClient connection)
	{
		this.loggedIn = loggedIn;
		this.username = username;
		this.connection = connection;
	}
	
	/**
	 * Returns the username.
	 * @return the username.
	 */
	public String getUsername()
	{
		return username;
	}
	
	/**
	 * Returns the connection to the server
	 * @return the connection
	 */
	public WebClient getConnection()
	{
		return connection;
	}
}
