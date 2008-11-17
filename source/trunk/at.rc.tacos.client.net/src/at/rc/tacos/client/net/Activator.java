package at.rc.tacos.client.net;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client.net";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/**
	 * Called when the plugin is started
	 * 
	 * @param context
	 *            lifecyle informations
	 * @throws Exception
	 *             when a error occures during startup
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * Called when the plugin is stopped
	 * 
	 * @param context
	 *            lifecyle informations
	 * @throws Exception
	 *             when a error occures during shutdown
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		if (plugin == null)
			plugin = new Activator();
		return plugin;
	}

}
