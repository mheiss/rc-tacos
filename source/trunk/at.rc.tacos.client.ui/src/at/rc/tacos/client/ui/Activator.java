package at.rc.tacos.client.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.ListenerList;
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

    // the list of listeners for ui updates
    private ListenerList listeners = new ListenerList();

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
            // try to load the bundle
            URL imageConf = getBundle().getEntry("/conf/images.properties");
            if (imageConf == null) {
                log.error("Unable to load the image configuration file");
                return registry;
            }
            // try to load the image description file in the workspace
            Properties imageProperties = new Properties();
            imageProperties.load(imageConf.openStream());

            // loop and register all imagee
            for (String imageKey : imageProperties.stringPropertyNames()) {
                String imageValue = imageProperties.getProperty(imageKey);
                // Create the image file with the given path
                ImageDescriptor descriptor = getImageDescriptor(imageValue);
                if (descriptor == null) {
                    log.warn("Could not create the image descriptor for the image '" + imageKey
                            + "'");
                    continue;
                }
                registry.put(imageKey, descriptor);
            }
        } catch (Exception npe) {
            log.error("Failed to load the image definitions from the configuration file", npe);
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
     * Returns an image descriptor for the image file at the given plug-in relative path
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    protected static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    //
    // LISTENERS
    //
    /**
     * Registers a new general purpose listener for all custom ui events that are triggered.
     * <p>
     * Note that you cannot register a <code>PropertyChangeListener</code> only for a specific
     * event. It is up to the handler implementation to check the fired event and deside whether
     * this event is interesting or not.
     * </p>
     * 
     * @param listener
     *            the listener to register.
     */
    public void registerListener(PropertyChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the given listener from the list of listeners.
     * 
     * @param listener
     *            the listener to remove
     */
    public void removeListener(PropertyChangeListener listener) {
        listeners.remove(listener);

    }

    /**
     * Fires the given <code>PropertyChangeEvent</code> and notifies all registered listeners about
     * the event.
     * 
     * @param event
     *            the <code>PropertyChangeEvent</code> to send to all listeners
     */
    public void firePropertyChangeEvent(PropertyChangeEvent event) {
        Object[] listenerArray = listeners.getListeners();
        for (int i = 0; i < listenerArray.length; ++i) {
            ((PropertyChangeListener) listenerArray[i]).propertyChange(event);
        }
    }
}
