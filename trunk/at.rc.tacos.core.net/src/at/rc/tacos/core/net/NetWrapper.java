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
    // The shared instance
    private static NetWrapper plugin;

    // The UI listeners to inform about new data
    private Vector<IClientNetEventListener> uiEventListeners;

    // The listener interface for the login
    private IClientLoginListener loginListener;

    /**
     * The constructor
     */
    public NetWrapper() 
    {
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
        System.out.println("login successfully");
        loginListener.loginSuccessfully();
    }

    /**
     * Notification that the status of the socket changed.<br>
     * @param status the new status.
     **/
    @Override
    public void socketStatusChanged(MyClient client, int status)
    {

    }

    /**
     * Norification that the transfer of the data failed.
     * @param ne the triggered net event
     */
    @Override
    public void dataTransferFailed(NetEvent ne)
    {
        System.out.println("Failed");
        //do we have a login listener?
        if (loginListener != null)
            loginListener.loginFailed("Failed to send the login request to the server");
    }


    /**
     * Sends the given message to the connected server
     * @param message the string to send
     */
    public void sendMessage(String message)
    {
        //get the connection
        MyClient activeConnection = NetSource.getInstance().getConnection();
        //assert we have a connection
        if (activeConnection != null)
            activeConnection.sendMessage(message);
        else
        {
            //notify that the transfer failed
            dataTransferFailed(new NetEvent(null,message));
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
     * Registers a listener to receive login events.
     * @param listener the listener to register
     */
    public void registerLoginListener(IClientLoginListener listener)
    {
        loginListener = listener;
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

        //set the listener
        NetSource.getInstance().addNetEventListener(this);
        //add primary server
        NetSource.getInstance().addServer("Server.primary", true, primaryHost, primaryPort);
        //add failback
        NetSource.getInstance().addServer("Server.failback", false, failbackHost, failbackPort);
    }

    /**
     *  Opens a connection to the primary server
     */
    private void connectNetwork()
    {
        //open a connection to the primary server
        if(NetSource.getInstance().connect())
        {
            firePrimaryServerConnectionEvent(IConnectionStates.STATE_CONNECTED);
            return;
        }

        //inform that the primary is not available
        firePrimaryServerConnectionEvent(IConnectionStates.STATE_DISCONNECTED);

        //try the other server
        if(NetSource.getInstance().failover())
        {
            firePrimaryServerConnectionEvent(IConnectionStates.STATE_CONNECTED);
            return;
        }
        
        //inform that the failback is not available
        fireFailbackServerConnectionEvent(IConnectionStates.STATE_DISCONNECTED);
    }
}
