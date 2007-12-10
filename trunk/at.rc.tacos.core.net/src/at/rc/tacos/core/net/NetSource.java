package at.rc.tacos.core.net;

//java
import java.util.HashMap;
import java.util.Iterator;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Map.Entry;
//net
import at.rc.tacos.core.net.event.INetListener;
import at.rc.tacos.core.net.event.NetEvent;
import at.rc.tacos.core.net.internal.MyClient;

/**
 * The network source that handles the connections 
 * to the servers and the failover when a server connection
 * is down. 
 * @author Michael
 */
public class NetSource
{    
    //config file
    public static final String NET_SETTINGS_BUNDLE_PATH = "at.rc.tacos.core.net.config.server";

    //the instance
    private static NetSource instance;   

    //the server pool
    private HashMap<String,MyClient> serverPool;
    //the listener
    private INetListener listener;

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
        activeConnection = "";
        primaryConnection = "";
    }

    /**
     * Loads the configuration and sets up the primary and the failback connection.
     * @return true if the initialisation was successfully
     */
    public boolean initNetwork()
    {    
        try
        {
            ResourceBundle bundle = ResourceBundle.getBundle(NetSource.NET_SETTINGS_BUNDLE_PATH);
            //load the settings from the file
            String primaryHost = bundle.getString("server.primary.host");
            String strPrimaryPort = bundle.getString("server.primary.port");
            String failbackHost = bundle.getString("server.failback.host");
            String strFailbackPort = bundle.getString("server.failback.port");
            int primaryPort = -1;
            int failbackPort = -1;
            //parse
            primaryPort = Integer.parseInt(strPrimaryPort);
            failbackPort = Integer.parseInt(strFailbackPort);
            //add servers
            addServer("Server.primary", true, primaryHost, primaryPort);
            addServer("Server.failback", false, failbackHost, failbackPort);
            //seccessfully
            return true;
        }
        catch(MissingResourceException mre)
        {
            System.out.println("Missing resource, cannot init network");
            System.out.println(mre.getMessage());
        }
        catch(NumberFormatException nfe)
        {
            System.out.println("Port number must be a integer");
            System.out.println(nfe.getMessage());
        }
        catch(NullPointerException npe)
        {
            System.out.println("Configuration file for the server is missing");
            System.out.println(npe.getMessage());
        }
        return false;
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
     * Add a NetEvent listener to the listener list.
     * The listener is registered for all properties.
     * @param listener the NetEvent listener to add
     */
    public void addNetEventListener(INetListener listener)
    {
        this.listener = listener;
    }

    /**
     * Removes the listener from the listener list.
     * This removes a INetEvent listener that was registered for all properties.
     * @param listener the listener to remove
     */
    public void removeNetEventListner(INetListener listener)
    {
        listener = null;
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
     * @return true if the failover was successfully and another connection 
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
    private MyClient getConnection()
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
    
    /**
     * Sends the given string to the remove host.
     * @param message the message to send
     */
    public void sendMessage(String message)
    {
        //get the connection
        MyClient client = getConnection();
        //assert valid
        if(client == null)
        {
            //create a new net event
            NetEvent ne = new NetEvent(client,"Faild to send the message");
            listener.dataTransferFailed(ne);
        }
        else
            client.sendMessage(message);
    }
    
    /**
     * Returns the ip address of the active server.
     * @return the ip adress of the server
     */
    public String getServerIp()
    {
        return serverPool.get(primaryConnection).getServerAddress();
    }
}
