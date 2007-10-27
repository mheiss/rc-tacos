package at.rc.tacos.core.net;

//java
import java.util.Vector;
//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
//net
import at.rc.tacos.core.net.event.*;
import at.rc.tacos.core.net.internal.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin implements INetListener
{
    // The plug-in ID
    public static final String PLUGIN_ID = "at.rc.tacos.core.net";

    //The configuration files
    public static final String CLIENT_SETTINGS_PATH = "at/rc/tacos/core/net/properties/client.properties";
    public static final String SERVER_SETTINGS_PATH = "at.rc.tacos.core.net.properties.server";

    // The shared instance
    private static NetWrapper plugin;

    // The UI listeners to inform about new data
    private Vector<IClientNetEventListener> uiEventListeners;

    // The handler for the connections
    private ConnectionHandler connectionHandler;

    /**
     * The constructor
     */
    public NetWrapper() 
    {
        //set up the connection handler
        connectionHandler = new ConnectionHandler();
        //init the listeners list
        uiEventListeners = new Vector<IClientNetEventListener>();
    }

    /**
     * Called when the plugin is started
     */
    public void start(BundleContext context) throws Exception 
    {
        super.start(context);
        plugin = this;

    }
    
    /**
     * Initialize and start the network connection
     */
    public void initAndStartup()
    {
        // initialize the connections
        initNetwork();
        // open the connections
        connectNetwork();
    }

    /**
     * Called when the plugin is stopped
     */
    public void stop(BundleContext context) throws Exception 
    {
        super.stop(context);
        plugin = null;
    }

    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static NetWrapper getDefault() 
    {
        return plugin;
    }

    /**
     * Notification that new data is available.<br>
     * @param ne the triggered net event
     */
    @Override
    public void dataReceived(NetEvent ne)
    {


    }

    /**
     * Notification that the status of the socket changed.<br>
     * @param status the new status.
     **/
    @Override
    public void socketStatusChanged(MyClient client, int status)
    {
        //get the connection
        ConnectionInfo connectionInfo = connectionHandler.getConnectionByClient(client);
        //assert valid
        if(connectionInfo == null)
            return;

        //update the status
        connectionInfo.updateStatus(status);

        if(connectionInfo.getId() == ConnectionHandler.PRIMARY_SERVER_ID)
            firePrimaryServerConnectionEvent(status);
        if(connectionInfo.getId() == ConnectionHandler.FAILBACK_SERVER_ID)
            fireFailbackServerConnectionEvent(status);
    }

    /**
     * Norification that the transfer of the data failed.
     * @param ne the triggered net event
     */
    @Override
    public void dataTransferFailed(NetEvent ne)
    {

    }
    

    /**
     * Sends the given message to the connected server
     * @param message the string to send
     */
    public void sendMessage(String message)
    {
        //get the connection
        MyClient activeConnection = connectionHandler.getActiveServer();
        //assert we have a valid object
        if(activeConnection != null)
        {
            activeConnection.getSocket().sendMessage(message);                      
        }
    }

    /**
     * Inform the listeners that the primary server status has changed
     * @param newStatus the new status of the server
     */
    protected void firePrimaryServerConnectionEvent(int newStatus)
    {
        for(IClientNetEventListener listeners:uiEventListeners)
            listeners.updatePrimaryServerConnection(newStatus);
    }

    /**
     * Inform the listeners that the failback server status has changed
     * @param newStatus the new status of the server
     */
    protected void fireFailbackServerConnectionEvent(int newStatus)
    {
        for(IClientNetEventListener listeners:uiEventListeners)
            listeners.updatePrimaryServerConnection(newStatus);
    }

    /**
     * Registers a listener to receive ui update events.
     * @param listener the listener to register
     */
    public void registerUIListener(IClientNetEventListener listener)
    {
        uiEventListeners.addElement(listener); 
    }

    /**
     * Removes the listener from the list so that no events
     * will be forwareded to this ui object.
     * @param listener the listener to unregister
     */
    public void unregisterUIListener(IClientNetEventListener listener)
    {
        uiEventListeners.removeElement(listener);
    }

    /**
     * Loads the configuration and sets up the primary and the failback connection.
     */
    private void initNetwork()
    {        
        //primary connection
        String primaryHost = "localhost";
        int primaryPort = 4711;
        
        //failback
        String failbackHost = "localhost";
        int failbackPort = 4712;
        

        //add both to the connection handler
        connectionHandler.addServer(
                ConnectionHandler.PRIMARY_SERVER_ID,
                primaryHost,
                primaryPort);
        connectionHandler.addServer(
                ConnectionHandler.FAILBACK_SERVER_ID,
                failbackHost,
                failbackPort);
    }
    
    /**
     *  Opens the connection to the primary and the failback server.
     */
    private void connectNetwork()
    {
        //get the primary server
        ConnectionInfo primaryConnection = connectionHandler.getConnectionById(ConnectionHandler.PRIMARY_SERVER_ID);
        if(primaryConnection.getConnection().connect())
            primaryConnection.updateStatus(IConnectionStates.STATE_CONNECTED);

        //get the failback server
        ConnectionInfo failbackConnection = connectionHandler.getConnectionById(ConnectionHandler.FAILBACK_SERVER_ID);
        if(failbackConnection.getConnection().connect())
            failbackConnection.updateStatus(IConnectionStates.STATE_CONNECTED);
    }

    /**
     * Returns the handler for the primary and failback server
     * @return the connection handler
     */
    public ConnectionHandler getConnectionHandler()
    {
        return connectionHandler;
    }
}
