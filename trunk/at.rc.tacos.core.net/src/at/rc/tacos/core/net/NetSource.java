package at.rc.tacos.core.net;

//java
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.Map.Entry;
//net
import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.internal.MyClient;

/**
 * The network source that handles the connections 
 * to the servers and the failover when a server connection
 * is down. 
 * @author Michael
 */
public class NetSource
{    
    //the instance
    private static NetSource instance;

    //the server pool
    private HashMap<String,MyClient> serverPool;
    //the listeners
    private Vector<INetListener> listeners;

    //identification string of the active connection
    private String activeConnection;
    
    //the identification of the primary server
    private String primaryConnection;

    /**
     * Default class constructor
     */
    private NetSource() 
    { 
        serverPool = new HashMap<String, MyClient>();
        listeners = new Vector<INetListener>();
        activeConnection = "";
        primaryConnection = "";
    }

    /**
     * Returns the instance of the network source
     * @return the shared instance
     */
    public static NetSource getInstance()
    {
        if (instance == null)
            instance = new NetSource();
        return instance;
    }

    /**
     * Adds the server to the handler and defines whether this is the 
     * primary connection or not. If two connections are set to primary
     * then the last added connection will winn. <br>
     * The id must be a uniqe name to identify the server connection. 
     * Adding a server with the same id will overwrite the existing one. 
     * @param id the identification of the server (primary or failback)
     * @param isPrimary defines this connection as primary
     * @param serverHost the network address or name of the server
     * @param serverPort the port number of the server
     */
    public void addServer(String id,boolean isPrimary,String serverHost,int serverPort)
    {
        //primary?
        if (isPrimary)
            primaryConnection = id;
        //create the server object
        MyClient newServer = new MyClient();
        newServer.setServerAddress(serverHost);
        newServer.setServerPort(serverPort);
        //add to the pool
        serverPool.put(id, newServer);
    }   

    /**
     * Add a NetEvent listener to the listener list.
     * The listener is registered for all properties.
     * @param The NetEvent listener to add
     */
    public void addNetEventListener(INetListener listener)
    {
        listeners.addElement(listener);
    }

    /**
     * Removes the server from the server list.
     * When the id is not found nothing will happen.
     * @param id the of the server to remove
     */
    public void removeServer(String id)
    {
        //remove the server
        serverPool.remove(id);
    }

    /**
     * Removes the listener from the listener list.
     * This removes a INetEvent listener that was registered for all properties.
     */
    public void removeNetEventListner(INetListener listener)
    {
        listeners.removeElement(listener);
    }

    /**
     *  Opens a connection to the primary server and sets the active
     *  connection to this id if successfully connected.
     *  @return true if a connection is established
     */
    public boolean connect()
    {
        if (connectTo(primaryConnection))
            return true;
        //connection failed, or no connection found with that id
        return false;
    }

    /**
     * Opens a connection to the next server in the pool.<br>
     * The method will only succeed if there is at least one primary 
     * server and one failback server. <br>
     * If the connection to the failback server is successfully the currently
     * active connection will be closed and the listeners will be removed.
     * @param true if the failover was successfully and another connection 
     *        is established.
     */
    public boolean failover()
    {
        //save the current connection
        String tempActiveConnection = activeConnection;
        //loop over the pool
        Iterator<Entry<String,MyClient>> iter = serverPool.entrySet().iterator();
        while (iter.hasNext())
        {
            //get the entry
            Entry<String, MyClient> entry = iter.next();
            //ignore the current active connection
            if (!entry.getKey().equalsIgnoreCase(tempActiveConnection))
            {
                //connect to the next
                if (connectTo(entry.getKey()))
                {
                    //get the connection before the failover
                    if (serverPool.containsKey(tempActiveConnection))
                    {
                        MyClient tempClient = serverPool.get(tempActiveConnection);
                        //close and unregister
                        tempClient.removeAllNetListeners();
                        tempClient.requestStop();
                        //successfully changed to another server
                        return true;
                    }
                }
            }
        }
        // failed to open another connection
        return false;
    }
    
    /**
     * Returns wheter the primary server is currently 
     * active and connected.
     * @return true if the primary connection is active
     */
    public boolean isPrimaryServerConnected()
    {
        //primary = active
        if (primaryConnection.equalsIgnoreCase(activeConnection))
        {
            if(getConnection() != null)
                return true;
        }
        //failback in use or primary not connected
        return false;
    }

    /**
     * Opens a connection to the given server id.
     * @return true if the connection is established
     */
    private boolean connectTo(String id)
    {
        //assert valid
        if (serverPool.containsKey(id))
        {
            //get the connection
            MyClient client = serverPool.get(id);
            if (client.connect())
            {
                //set as active
                activeConnection = id;
                //add listeners
                for (INetListener listener:listeners)
                    client.addNetListener(listener);
                //ready to work
                return true;
            }
        }
        //failed
        return false;
    }

    /**
     * Returns the connection that is marked as active (=connected).
     * If the active connection is not connected to the server the
     * method will return null.
     * When no connection is available the Method will return null.
     * @return a valid server connection or null
     */
    public MyClient getConnection()
    {
        //assert valid
        if (serverPool.containsKey(activeConnection))
        {
            //check status
            if(serverPool.get(activeConnection).isConnected())
                return serverPool.get(activeConnection);
            else
                return null;
        }
        //no active connection found
        return null;
    }
}
