package at.rc.tacos.core.net;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import at.rc.tacos.common.INetworkLayer;
import at.rc.tacos.common.INetworkListener;
import at.rc.tacos.common.IXMLObject;
import at.rc.tacos.core.net.event.*;
import at.rc.tacos.core.net.internal.*;
import at.rc.tacos.core.xml.XMLFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin implements INetListener,INetworkLayer
{
    // The plug-in ID
    public static final String PLUGIN_ID = "at.rc.tacos.core.net";
    // The shared instance
    private static NetWrapper plugin;

    // Session information
    private String sessionUserId;
    // Service listener
    private INetworkListener listener;

    /**
     * The constructor
     */
    public NetWrapper() { }

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
        //set up the factory to decode
        XMLFactory factory = new XMLFactory();
        factory.setupDecodeFactory(ne.getMessage());
        //decode the message
        ArrayList<IXMLObject> objects = factory.decode();
        //get the type of the item
        String type = factory.getType(); 
        String action = factory.getAction();
        //pass the message
        listener.fireNetMessage(type, action, objects);
    }

    /**
     * Notification that the status of the socket changed.<br>
     * @param status the new status.
     **/
    @Override
    public void socketStatusChanged(MyClient client, int status)
    {
        System.out.println("socket status changed:"+status);
    }

    /**
     * Norification that the transfer of the data failed.
     * @param ne the triggered net event
     */
    @Override
    public void dataTransferFailed(NetEvent ne)
    {    
        System.out.println("failed to send the data:"+ne.getMessage());
    }
    
    // LISTENER SUPPORT
    /**
     * Registers a listener to inform about network changes.
     * @param listener the listener to inform
     */
    @Override
    public void registerNetworkListener(INetworkListener listener)
    {
       this.listener = listener;
        
    }
    
    @Override
    public void removeNetworkListener(INetworkListener listener)
    {
        this.listener = null;
    }
    
    //METHODS FOR INTERACTION WITH THE SERVICE LAYER
    @Override
    public void fireNetworkMessage(String objectType,String action,ArrayList<IXMLObject> object)
    {
        XMLFactory factory = new XMLFactory();
        factory.setupEncodeFactory(
                sessionUserId,
                new Date().getTime(),
                objectType,
                action,
                0);
        String message = factory.encode(object);
        //send the message
        NetSource.getInstance().getConnection().sendMessage(message);
    }
    
    @Override
    public void fireNetworkMessage(String objectType,String action,IXMLObject object)
    {
        //create a list 
        ArrayList<IXMLObject> list = new ArrayList<IXMLObject>();
        //add the object
        list.add(object);
        fireNetworkMessage(objectType,action, list);
    }
    
    // SESSION PARAMETER
    /**
     * Sets the session parameters that should be used
     * for the whole network communication session
     */
    @Override
    public void setSessionParameter(String userId)
    {
        this.sessionUserId = userId;
    }
}
