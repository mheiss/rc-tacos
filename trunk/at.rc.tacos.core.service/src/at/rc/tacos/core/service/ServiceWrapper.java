package at.rc.tacos.core.service;

//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
//common
import at.rc.tacos.common.INetClientLayer;
import at.rc.tacos.common.NetManager;
import at.rc.tacos.core.net.NetWrapper;

/**
 * The activator class controls the plug-in life cycle
 */
public class ServiceWrapper extends Plugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.service";

	// The shared instance
	private static ServiceWrapper plugin;
	
	//the layers
	private NetLayerImpl netLayerImpl;
	private DatabaseLayerImpl databaseLayerImpl;
	
	//the client listeners
	private NetManager netManager;
	
	/**
     * The constructor
     */
	public ServiceWrapper() 
	{ 
	    //create the handlers
	    netLayerImpl = new NetLayerImpl();
	    databaseLayerImpl = new DatabaseLayerImpl();
		netManager = new NetManager();
		//register the handler to listen to net events
		NetWrapper.getDefault().addNetChangeListener(netLayerImpl);
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
     * Called when the plugin is stopped
     */
	public void stop(BundleContext context) throws Exception 
	{
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static ServiceWrapper getDefault() 
	{
		return plugin;
	}
	
	/**
     *  Add a NetChangeListener to the listener list.
     *  The listeners will be informed uppon new received objects.
     *  @param listener the listener to add
     */
    public void addNetChangeListener(INetClientLayer listener)
    {
        netManager.addNetChangeListener(listener);
    }
    
    /**
     * Remove a NetChangeListener from the listener list. 
     * This removes a listener that was registered for all properties.
     * @param listener The NetChangeListener to be removed
     */
    public void removeNetChangeListener(INetClientLayer listener)
    {
        netManager.removeNetChangeListener(listener);
    }
	
	/**
	 * Returns the nework service layer.<br>
	 * The network service layer provides the access to the network.
	 */
	public NetLayerImpl getNetService()
	{
		return netLayerImpl;
	}
	
	/**
	 * Returns the database service layer.<br>
	 * The database service layer provides the access to the database
	 */
	public DatabaseLayerImpl getDatabaseService()
	{
		return databaseLayerImpl;
	}
	
	/**
	 * Returns the listeners to inform about net events.
	 * @return the registered listeners
	 */
	public NetManager getNetManager()
	{
	    return netManager;
	}
}
