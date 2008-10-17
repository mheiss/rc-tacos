package at.rc.tacos.platform.log.test;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class LogTestPlugin extends Plugin {

	//The shared instance.
	private static LogTestPlugin plugin;
	
	/**
	 * The constructor.
	 */
	public LogTestPlugin() {
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static LogTestPlugin getDefault() {
		return plugin;
	}
}
