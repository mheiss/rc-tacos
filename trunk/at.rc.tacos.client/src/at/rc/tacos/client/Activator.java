package at.rc.tacos.client;

//rcp
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
//client
import at.rc.tacos.client.model.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client";

	// The shared instance
	private static Activator plugin;
	
	//the object data   
    private ObjectList objectList = new ObjectList();
	
	/**
	 * The constructor
	 */
	public Activator() { }

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
	public static Activator getDefault() 
	{
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) 
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
    /**
     * Returns the object list containing the items
     * @return the objectList
     */
    public ObjectList getObjectList() 
    {
        return objectList;
    }
}
