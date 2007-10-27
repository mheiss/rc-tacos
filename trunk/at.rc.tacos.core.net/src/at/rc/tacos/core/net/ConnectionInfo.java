package at.rc.tacos.core.net;

import at.rc.tacos.core.net.event.*;
import at.rc.tacos.core.net.internal.*;

/**
 * Holds infomation about a specific connection.
 * @author Michael
 */
public class ConnectionInfo
{
    //status
    private String id;
    private boolean connected;
    private MyClient client;
    
    /**
     * Default class constructor specifying the client to observe
     * @param id the identification of the connection
     * @param client the server to manage
     */
    public ConnectionInfo(String id,MyClient client)
    {
        this.id = id;
        this.client = client;
    }
    
    /**
     * Updates the status of the connection
     * @param newStatus the status to set
     */
    public void updateStatus(int newStatus)
    {
        //connected
        if (newStatus == IConnectionStates.STATE_CONNECTED)
            connected = true;
        //disconnected
        if (newStatus == IConnectionStates.STATE_DISCONNECTED)
            connected = false;
    }
    
    /**
     *  Returns wheter this connection is the primary or failback.
     *  @return the identification of the connection
     */
    public String getId()
    {
        return id;
    }
    
    /**
     * Returns the actual status of the connection
     * @return true if a connection is established otherwise false
     */
    public boolean isConected()
    {
        return connected;
    }
    
    /**
     * Returns the connection to the server that is observed.
     * @return the client connection
     */
    public MyClient getConnection()
    {
        return client;
    }
}
