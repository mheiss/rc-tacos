package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * This message is for logout purpose.<br>
 * In the response the server sets the <code>isLoggedOut</code> to true if the
 * logout was successfully.<br>
 * In case of a failed logout request the error message can be retrieved 
 * with <code>getErrorMessage</code>.<br>
 * @author Michael
 */
public class Logout extends AbstractMessage
{
    // Identification of a login message
    public final static String ID = "logout";
    
    //properties
    private String username;
    private boolean loggedOut;
    private String errorMessage;
    
    /**
     * Default class constructor
     */
    public Logout()
    {
        super(ID);
    }
    
    /**
     * Default constructor of a logout message
     * @param username the username to logout
     */
    public Logout(String username)
    {
        super(ID);
        setUsername(username);
    }
    
    /**
     * Returns the username and the logout status.
     * @return the username and the status
     */
    @Override
    public String toString()
    {
        return username+","+loggedOut;
    }

    //GETTERS AND SETTERS
    /**
     * Returns the username of this logout object.<br>
     * This method will contain the username in the response from the server.
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Returns whether the user logout was successfully or not.
     * This field will be set by the server, if the logout was not
     * successfully the <code>getErrorMessage</code> can be used to get
     * the error message.
     * @return true if the user is logged out, otherwise false.
     */
    public boolean isLoggedOut()
    {
        return loggedOut;
    }

    /**
     * Returns the error message when the logout was not successfully.<br>
     * This method will only return a valid string if the logout was not
     * successfully.
     * @return the error message why the logout failed
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * Sets the username to use during the logout process<br>
     * @param username the username of the staff member to logout
     * @throws IllegalArgumentException if the username is null or empty
     */
    public void setUsername(String username)
    {
        if(username == null || username.trim().isEmpty())
            throw new IllegalArgumentException("Username cannot be null or empty");
        this.username = username;
    }

    /**
     * Sets the given user as logged out.<br>
     * The error message can be used to specify a error in case of a invalid logout.
     * @param loggedOut true if the logout was successfully, otherwise false.
     */
    public void setLoggedOut(boolean loggedOut)
    {
        this.loggedOut = loggedOut;
    }

    /**
     * Sets the error message why the logout process failed.
     * @param errorMessage the cause of the logout failure
     * @throws IllegalArgumentException if the error message is null or empty
     */
    public void setErrorMessage(String errorMessage)
    {
        if(errorMessage == null || errorMessage.trim().isEmpty())
            throw new IllegalArgumentException("The error message cannot be null or empty");
        this.errorMessage = errorMessage;
    }
}
