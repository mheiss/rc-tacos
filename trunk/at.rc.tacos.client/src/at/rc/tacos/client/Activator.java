package at.rc.tacos.client;

//rcp
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
//client
import at.rc.tacos.client.controller.NetController;
import at.rc.tacos.client.modelManager.*;
import at.rc.tacos.core.model.RosterEntry;
import at.rc.tacos.core.service.ServiceWrapper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client";

	// The shared instance
	private static Activator plugin;
	
	//the listener for net events
	private NetController netController;
	
	//the object data   
    private ItemManager itemList = new ItemManager();
    private RosterEntryManager rosterEntryList = new RosterEntryManager();
	
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
		//the listener for net changes
		netController = new NetController();
		ServiceWrapper.getDefault().addNetChangeListener(netController);
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
    public ItemManager getItemList() 
    {
        return itemList;
    }
    
    public RosterEntryManager getRosterEntryList()
    {
    	return rosterEntryList;
    }
}
