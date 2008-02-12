package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * This message is for login purpose. A username a password an can be specified.<br>
 * In the response the server sets the <code>isLoggedIn</code> to true if the
 * login was successfully. Additional information about the user will also be stored 
 * within this object and send after the login back to the client.
 * In case of a failed login request the error message can be retrieved 
 * with <code>getErrorMessage</code>.<br>
 * Notice that in the response from the server the password field is not valid.
 * @author Michael
 */
public class Login extends AbstractMessage
{
    // Identification of a login message
    public final static String ID = "login";
    
    //properties
    private String username;
    private String password;
    private StaffMember userInformation;
	private String authorization;
	private boolean islocked;
	
	//additonal information
	private boolean loggedIn;
	private String errorMessage;
	private boolean webClient;
	
	//define constants
	public final static String AUTH_USER = "Benutzer";
	public final static String AUTH_ADMIN = "Administrator";
	public final static String[] AUTHORIZATION = { AUTH_USER, AUTH_ADMIN };
 
    /**
     * Default class constructor
     */
    public Login() 
    {
        super(ID);
    }
    
    /**
     * Default constructor of a login message
     * @param username the username to login
     * @param password the encrypted password 
     * @param webClient flag to determine whether this is a web client
     */
    public Login(String username,String password,boolean webClient)
    {
        super(ID);
        setUsername(username);
        setPassword(password);
        setWebClient(webClient);
    }
    
    /**
     * Returns the username and the login status.
     * @return the username and the status
     */
    @Override
    public String toString()
    {
        return username+","+loggedIn;
    }
    
    /**
     * Removes the password from the login object
     */
    public void resetPassword()
    {
    	this.password = null;
    }
    
    /**
     * Returns the calculated hash code based on the username.<br>
     * Two logins have the same hash code if the username is the same.
     * @return the calculated hash code
     */
	@Override
	public int hashCode() 
	{
		return  31 + username.hashCode();
	}

	/**
     * Returns whether the objects are equal or not.<br>
     * Two logins are equal if, and only if, the location id is the same.
     * @return true if the id is the same otherwise false.
     */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Login other = (Login) obj;
		if (!username.equals(other.username))
			return false;
		return true;
	}

	//GETTERS AND SETTERS
    /**
     * Returns the username of this login object.<br>
     * This method will contain the username in the response from the server.
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Returns the password that was specified by the user.<br>
     * Note that this field is <b>not valid</b> in a server response.
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }
    
    /**
     * Returns the additional informations stored about this 
     * user login.
     * @return the additional information about the login
     */
    public StaffMember getUserInformation()
    {
        return userInformation;
    }

    /**
     * Returns whether the user is logged in or not.<br>
     * This field will be set by the server, if the login was not
     * successfully the <code>getErrorMessage</code> can be used to get
     * the error message.
     * @return true if the user is logged in, otherwise false
     */
    public boolean isLoggedIn()
    {
        return loggedIn;
    }
    
    /**
     * Returns wheterh of not this login request is form a web client.
     * @return true if the request if from a web client
     */
    public boolean isWebClient()
    {
        return webClient;
    }

    /**
     * Returns the error message when the login was not successfully.<br>
     * This method will only return a valid string if the login was not
     * successfully.
     * @return the error message why the login failed
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * Sets the username to use during the authentication process<br>
     * @param username the username of the staff member to login
     * @throws IllegalArgumentException if the username is null or empty
     */
    public void setUsername(String username)
    {
        if(username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty");
        this.username = username;
    }

    /**
     * Sets the encrypted password for the user to login.
     * @param password the encrypted password from the user
     * @throws IllegalArgumentException if the password is null
     */
    public void setPassword(String password)
    {
        if(password == null || password.trim().isEmpty())
            throw new IllegalArgumentException("The password cannot be null or empty");
        this.password = password;
    }
    
    /**
     * Sets the additional information that should be accociated with this
     * login object and send to the client.<br>
     * The additional information also contains the specific rights for the user.
     * @param userInformation the information about the user
     */
    public void setUserInformation(StaffMember userInformation)
    {
        if(userInformation == null)
            throw new IllegalArgumentException("The user information cannot be null");
        this.userInformation = userInformation;
    }

    /**
     * Sets the given user as logged int.<br>
     * The error message can be used to specify a error in case of a invalid login.
     * @param loggedIn true if the login was successfully, otherwise false.
     */
    public void setLoggedIn(boolean loggedIn)
    {
        this.loggedIn = loggedIn;
    }
    
    /**
     * Treats the given login request as a request from a web client.
     * @param webClient true if the request is from a webClient
     */
    public void setWebClient(boolean webClient)
    {
        this.webClient = webClient;
    }

    /**
     * Sets the error message why the login process failed.
     * @param errorMessage the cause of the login failure
     * @throws IllegalArgumentException if the error message is null or empty
     */
    public void setErrorMessage(String errorMessage)
    {
        if(errorMessage == null || errorMessage.trim().isEmpty())
            throw new IllegalArgumentException("The error message cannot be null or empty");
        this.errorMessage = errorMessage;
    }

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}

	public boolean isIslocked() {
		return islocked;
	}

	public void setIslocked(boolean islocked) {
		this.islocked = islocked;
	}
}
