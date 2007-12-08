package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * This message is for login purpose. A username a password an can be specified.<br>
 * In the response the server sets the <code>isLoggedIn</code> to true if the
 * login was successfully.<br>
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
    private boolean loggedIn;
    private String errorMessage;
    
    
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
     */
    public Login(String username,String password)
    {
        super(ID);
        setUsername(username);
        setPassword(password);
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
        if(password == null)
            throw new IllegalArgumentException("The password cannot be null");
        this.password = password;
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
}
