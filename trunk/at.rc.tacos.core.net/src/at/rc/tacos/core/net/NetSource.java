package at.rc.tacos.core.net;

//java
import java.util.HashMap;
//net
import at.rc.tacos.core.net.internal.MyClient;

/**
 * The network source that handles the connections 
 * to the servers and the failover when a server connection
 * is down. 
 * @author Michael
 */
public class NetSource
{
    /** identification of the primary server */
    public static final String PRIMARY_SERVER_ID = "Server.primary";
    /** identification of the failback server */
    public static final String FAILBACK_SERVER_ID = "Server.failback";
    
    //the instance
    private static NetSource instance;
    
    //the server pool
    private HashMap<String,MyClient> serverPool;
    
    /**
     * Default class constructor
     */
    private NetSource() 
    { 
        serverPool = new HashMap<String, MyClient>();
    }
    
    /**
     * Returns the instance of the database connection
     * @return the shared instance
     */
    public static NetSource getInstance()
    {
        if (instance == null)
            instance = new NetSource();
        return instance;
    }
    
    /**
     * Adds the server to the handler. The id specifies whether this is the 
     * primary or the failback server.
     * @param id the identification of the server (primary or failback)
     * @param serverHost the network address or name of the server
     * @param serverPort the port number of the server
     * @return the created client connection
     */
    public MyClient addServer(String id,String serverHost,int serverPort)
    {
        //create the server object
        MyClient newServer = new MyClient();
        newServer.setServerAddress(serverHost);
        newServer.setServerPort(serverPort);
        //add to the pool
        serverPool.put(id, newServer);
        //return the new client
        return newServer;
    }    
    
    /**
     * Removes the server from the server list.
     * When the id is not found nothing will happen
     * @param id the identification of the server
     */
    public void removeServer(String id)
    {
        //remove the server
        serverPool.remove(id);
    }
    
    /**
     *  Opens a connection to the server.
     *  When a 
     */
    
    /**
     * Returns a valid connection to interact with the server.
     * When no connection is available the Method will return null.
     * @return a valid server connection or null
     */
    public MyClient getConnection()
    {
        //test the primary server
        MyClient primary = serverPool.get(PRIMARY_SERVER_ID);
        if (primary != null)
        {
            if (primary.isConnected())
                return primary;
        }
        
        //test the failback
        MyClient failback = serverPool.get(FAILBACK_SERVER_ID);
        if (failback != null)
        {
            if (failback.isConnected())
                return failback;
        }
        
        //no active connection found
        return null;
    }
}
