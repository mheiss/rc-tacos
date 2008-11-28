package at.rc.tacos.client.ui;

import java.util.ResourceBundle;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client.ui";

	// The shared instance
	private static Activator plugin;

	// The logging instance
	private Logger log = LoggerFactory.getLogger(Activator.class);

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
	 * Called when the plugin is stopped.
	 * 
	 * @param context
	 *            lifecyle informations
	 * @throws Exception
	 *             when a error occures during shutdown
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	@Override
	protected ImageRegistry createImageRegistry() {
		ImageRegistry registry = new ImageRegistry();
		try {
			// try to load the image description file in the workspace
			ResourceBundle imageBundle = ResourceBundle.getBundle("at.rc.tacos.client.config.images");
			// loop and register all imagee
			for (String imageKey : imageBundle.keySet()) {
				String imageValue = imageBundle.getString(imageKey);
				// Create the image file with the given path
				ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, imageValue);
				if (descriptor == null) {
					log.warn("Could not create the image descriptor for the image '" + imageKey + "'");
					continue;
				}
				registry.put(imageKey, descriptor);
			}
		}
		catch (Exception npe) {
			log.error("Failed to load the images files", npe);
		}
		return registry;
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
