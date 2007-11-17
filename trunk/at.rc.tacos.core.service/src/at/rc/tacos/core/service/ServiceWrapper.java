package at.rc.tacos.core.service;

//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

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
	
	//the network layer
	private ServiceLayerImpl serviceImpl;
	//the database layer
	private DatabaseLayerImpl databaseImpl;
		
	/**
     * The constructor
     */
	public ServiceWrapper() 
	{ 
	    //create the service implementations
	    serviceImpl = new ServiceLayerImpl();
	    databaseImpl = new DatabaseLayerImpl();
	    //register the serviceImpl for net events
	    NetWrapper.getDefault().registerNetworkListener(serviceImpl);
	    //register the needed model types
	    
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
	 * Returns the network service implementation
	 * to access the network layer and register listeners.
	 * @return the implementation of the network layer
	 */
	public ServiceLayerImpl getServiceLayer()
	{
	    return serviceImpl;
	}
	
	/**
	 * Returns the database service implementation
	 * to access the database.
	 * @return the implementation of the database layer
	 */
	public DatabaseLayerImpl getDatabaseLayer()
	{
	    return databaseImpl;
	}
}
