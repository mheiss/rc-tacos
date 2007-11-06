package at.rc.tacos.core.net;

//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
//net
import at.rc.tacos.common.INetClientLayer;
import at.rc.tacos.common.INetServerLayer;
import at.rc.tacos.common.NetManager;
import at.rc.tacos.core.net.event.*;
import at.rc.tacos.core.net.internal.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin implements INetServerLayer,INetListener
{
    // The plug-in ID
    public static final String PLUGIN_ID = "at.rc.tacos.core.net";
    // The shared instance
    private static NetWrapper plugin;
    
    // The service layer to inform about new data
    private NetManager netChangeSupport;

    /**
     * The constructor
     */
    public NetWrapper() 
    { 
        netChangeSupport = new NetManager();
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
     *  Add a NetChangeListener to the listener list.
     *  The listeners will be informed uppon new received objects.
     *  @param listener the listener to add
     */
    public void addNetChangeListener(INetClientLayer listener)
    {
        netChangeSupport.addNetChangeListener(listener);
    }
    
    /**
     * Remove a NetChangeListener from the listener list. 
     * This removes a listener that was registered for all properties.
     * @param listener The NetChangeListener to be removed
     */
    public void removeNetChangeListener(INetClientLayer listener)
    {
        netChangeSupport.removeNetChangeListener(listener);
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
        NetSource.getInstance().connect();
    }
    
    //NET LISTENER METHODS
    /**
     * Notification that new data is available.<br>
     * @param ne the triggered net event
     */
    @Override
    public void dataReceived(NetEvent ne)
    {
        System.out.println("received data: "+ne.getMessage());
        netChangeSupport.fireItemAdded(ne.getMessage());
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
            
    }

    //METHODS TO UPDATE THE SERVER
    @Override
    public void requestAddItem(String item)
    {
        NetSource.getInstance().getConnection().sendMessage(item); 
    }
}
