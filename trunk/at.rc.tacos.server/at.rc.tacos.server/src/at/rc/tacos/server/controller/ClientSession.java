package at.rc.tacos.server.controller;

import at.rc.tacos.core.net.internal.MyClient;

/**
 * This class contains the data for a single client connection
 * @author Michael
 */
public class ClientSession
{
    //properties
    private String username;
    private boolean webClient;
    private MyClient connection;
    
    /**
     * Constructor for a new session
     * @param connection the connection of the client
     */
    public ClientSession(MyClient connection)
    {
        this.connection = connection;
    }
    
    //METHODS
    /**
     * Returns the description for this session
     * @return the description
     */
    @Override
    public String toString()
    {
    	return username + ", webClient = "+webClient + " , " + "connection = "+connection;
    }
    
    /**
     * Helper method to determine wheter the client is logged in or not.
     * @return true if the client is logged in, otherwise false
     */
    public boolean isAuthenticated()
    {
        if(username == null || username.isEmpty())
            return false;
        return true;
    }
    
    /**
     * Sets this client as authenticated.
     * @param username the username of the authenticated user
     * @param webClient flag to determine whether this is a web client
     */
    public void setAuthenticated(String username,boolean webClient)
    {
        this.username = username;
        this.webClient = webClient;
    }
    
    /**
     * Sets this client as not deauthenticated.<br>
     * No more requests are accepted until a new login.
     */
    public void setDeAuthenticated()
    {
        this.username = null;
    }
    
    /**
     * Returns the username of this session.
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }
    
    /**
     * Returns wheter or not his is a web client
     * @return true if this is a web client
     */
    public boolean isWebClient()
    {
        return webClient;
    }
    
    /**
     * Returns the connection of this session
     * @return the connection
     */
    public MyClient getConnection()
    {
        return connection;
    }
}
