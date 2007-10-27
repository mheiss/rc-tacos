package at.rc.tacos.core.net;

//net
import at.rc.tacos.core.net.internal.*;

/**
 * The server handler manages the primary and the failback server.
 * @author Michael
 */
public class ConnectionHandler 
{
    /** identification of the primary server */
    public static final String PRIMARY_SERVER_ID = "Server.primary";
    /** identification of the failback server */
    public static final String FAILBACK_SERVER_ID = "Server.failback";

    //the server
    private ConnectionInfo primaryServer;
    private ConnectionInfo failbackServer;

    /**
     * Default class constructor
     */
    public ConnectionHandler() { }

    /**
     * Adds the server to the handler. The id specifies whether this is the 
     * primary or the failback server.
     * @param id the identification of the server (primary or failback)
     * @param serverHost the network address or name of the server
     * @param serverPort the port number of the server
     */
    public void addServer(String id,String serverHost,int serverPort)
    {
        //create the server object
        MyClient server = new MyClient();
        server.setServerAddress(serverHost);
        server.setServerPort(serverPort);
        //add as primary or failback
        if (id.equalsIgnoreCase(PRIMARY_SERVER_ID))
            primaryServer = new ConnectionInfo(id,server);
        if (id.equalsIgnoreCase(FAILBACK_SERVER_ID))
            failbackServer = new ConnectionInfo(id,server);
    }    

    /**
     * Removes the server from the server list.
     * When the id is not found nothing will happen
     * @param id the identification of the server
     */
    public void removeServer(String id)
    {
        //add as primary or failback
        if (id.equalsIgnoreCase(PRIMARY_SERVER_ID))
            primaryServer = null;
        if (id.equalsIgnoreCase(FAILBACK_SERVER_ID))
            failbackServer = null;
    }
    
    /**
     *  Changes the status of the given connection.
     *  @param id the identification of the connection
     *  @param status the new status to set
     */
    public void updateConnectionStatus(String id,int status)
    {
        //update status
        if (id.equalsIgnoreCase(PRIMARY_SERVER_ID))
            primaryServer.updateStatus(status);
        if (id.equalsIgnoreCase(FAILBACK_SERVER_ID))
            primaryServer.updateStatus(status);
    }
    
    //GETTERS 
    /**
     * Returns the active server connection.
     * When no active connection is available the method
     * will return null.
     * @return the active connection or null
     */
    public MyClient getActiveServer()
    {
        //primary server connected?
        if (primaryServer.isConected())
            return primaryServer.getConnection();
        if (failbackServer.isConected())
            return failbackServer.getConnection();
        //no active connection
        return null;
    }
    
    /**
     * Returns the identification of the active server.
     * @return the id of the connection or null if there is no connection
     */
    public String getActiveServerId()
    {
        //primary server connected?
        if (primaryServer.isConected())
            return PRIMARY_SERVER_ID;
        if (failbackServer.isConected())
            return FAILBACK_SERVER_ID;
        //no active connection
        return null;
        
    }
    
    /**
     * Returns the connection identified by the given id string
     * @param id the connection identification
     * @return the connection or null if no connection found
     */
    public ConnectionInfo getConnectionById(String id)
    {
        if (id.equalsIgnoreCase(PRIMARY_SERVER_ID))
            return primaryServer;
        if (id.equalsIgnoreCase(FAILBACK_SERVER_ID)) 
            return failbackServer;
        //no connection found
        return null;
    }
    
    /**
     * Returns the connection identified by the given client object
     * @param connection the client idenification
     * @return the connection or null if nothing found
     */
    public ConnectionInfo getConnectionByClient(MyClient client)
    {
        if (primaryServer.getConnection() == client)
            return primaryServer;
        if (failbackServer.getConnection() == client)
            return failbackServer;
        //no connection found
        return null;
    }
}
