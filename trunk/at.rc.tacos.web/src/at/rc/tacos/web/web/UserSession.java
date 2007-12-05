package at.rc.tacos.web.web;

import tacosdp.domain.UserData;

/**
 * @author PayerM
 * @version 1.0
 */
public class UserSession {
	private Boolean loggedIn;
	private UserData userData;

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
	public void setLoggedIn(Boolean loggedIn,UserData userData)
	{
		this.loggedIn = loggedIn;
		this.userData = userData;
	}
	
	/**
	 * Returns login and personal information about
	 * the authenticated user. <br>
	 * Note: The password is not requested from the 
	 * database and will not be available <br>
	 * The method will return null if the user is
	 * not logged in.
	 * @return information about the user or null
	 */
	public UserData getUserData()
	{
		return userData;
	}
}
