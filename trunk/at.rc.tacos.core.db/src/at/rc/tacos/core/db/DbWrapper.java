package at.rc.tacos.core.db;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class DbWrapper extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.db";

	// The shared instance
	private static DbWrapper plugin;
	
	/**
	 * The constructor
	 */
	public DbWrapper() 
	{
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
	public static DbWrapper getDefault() 
	{
		return plugin;
	}
}
