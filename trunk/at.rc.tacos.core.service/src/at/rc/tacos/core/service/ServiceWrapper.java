package at.rc.tacos.core.service;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

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
	private NetServiceImpl netServiceImpl;
	private DatabaseServiceImpl databaseServiceImpl;
	
	/**
     * The constructor
     */
	public ServiceWrapper() 
	{ 
		netServiceImpl = new NetServiceImpl();
		databaseServiceImpl = new DatabaseServiceImpl();
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
	 * Returns the nework service layer
	 */
	public INetLayer getNetService()
	{
		return netServiceImpl;
	}
	
	/**
	 * Returns the database service layer
	 */
	public IDatabaseLayer getDatabaseService()
	{
		return databaseServiceImpl;
	}
}
