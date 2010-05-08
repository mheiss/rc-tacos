package at.rc.tacos.web.session;

import java.util.Date;

import at.rc.tacos.core.net.socket.WebClient;
import at.rc.tacos.model.Login;

/**
 * Stores user (session) specific values.
 * @author Payer Martin
 * @version 1.0
 */
public class UserSession 
{
	private Boolean loggedIn;
	private WebClient connection;
	private Login loginInfo;
	private DefaultFormValues defaultFormValues;
	private boolean internalSession;
	private Date today;

	/**
	 * Default constructor
	 */
	public UserSession()
	{
		loggedIn = false;
		defaultFormValues = new DefaultFormValues();
		connection = new WebClient();
		today = new Date();
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
	public void setLoggedIn(Boolean loggedIn,Login loginInfo,WebClient connection)
	{
		this.loggedIn = loggedIn;
		this.loginInfo = loginInfo;
		this.connection = connection;
	}

	/**
	 * Returns the connection to the server
	 * @return the connection
	 */
	public WebClient getConnection()
	{
		return connection;
	}
	
	/**
	 * Returns the login information and the authorization for this user. <br>
	 * Note that the password is not set in this login info.
	 * @return the login information
	 */
	public Login getLoginInformation()
	{
		return loginInfo;
	}
	
	public void setLoginInformation(Login login) {
		this.loginInfo = login;
	}

	public DefaultFormValues getDefaultFormValues() {
		return defaultFormValues;
	}

	public void setDefaultFormValues(DefaultFormValues defaultFormValues) {
		this.defaultFormValues = defaultFormValues;
	}

	public boolean isInternalSession() {
		return internalSession;
	}

	public void setInternalSession(boolean internalSession) {
		this.internalSession = internalSession;
	}

	public Date getToday() {
		return today;
	}

	public void setToday(Date today) {
		this.today = today;
	}
}
