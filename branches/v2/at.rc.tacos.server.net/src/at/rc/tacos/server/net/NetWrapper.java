package at.rc.tacos.server.net;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class NetWrapper extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.server.net";

	// The shared instance
	private static NetWrapper plugin;
	
	/**
	 * The constructor
	 */
	public NetWrapper() { }

	/**
	 * Called when the plugin is started
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during startup
	 */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
		System.out.println("hallo");
	}

	/**
	 * Called when the plugin is stopped
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during shutdown
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
	public static NetWrapper getDefault() 
	{
		return plugin;
	}
	
	/**
	 * Logs the error with the build in log
	 */
	public static void log(String message,int severity,Throwable cause)
	{
		NetWrapper.getDefault().getLog().log(new Status(severity,PLUGIN_ID,message,cause));
	}
}
