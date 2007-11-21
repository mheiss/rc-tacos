package at.rc.tacos.client;

//rcp
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
//client
import at.rc.tacos.client.listeners.*;
import at.rc.tacos.client.modelManager.*;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.model.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client";
	// The shared instance
	private static Activator plugin;
	
	//the object manager   
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
		//register the encoders and decoders
		NetWrapper.getDefault().registerEncoderAndDecoder();
		//register model listeners
		registerModelListeners();
		//set the session
	    NetWrapper.getDefault().setSessionUsername("Client user");
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
     * Convenience method to registers the listeners
     * for the network updates.
     */
	public void registerModelListeners()
	{
	    ListenerFactory factory = ListenerFactory.getDefault();
	    //register the listeners
	    factory.registerModelListener(Item.ID, new ItemListener());
	    factory.registerModelListener(MobilePhoneDetail.ID, new MobilePhoneListener());
	    factory.registerModelListener(NotifierDetail.ID, new NotifyDetailListener());
	    factory.registerModelListener(Patient.ID, new PatientListener());
	    factory.registerModelListener(RosterEntry.ID, new RosterEntryListener());
	    factory.registerModelListener(StaffMember.ID, new StaffMemberListener());
	    factory.registerModelListener(Transport.ID, new TransportListener());
	    factory.registerModelListener(VehicleDetail.ID, new VehicleDetailListener());
	}
	
    /**
     * Returns the object list containing the items
     * @return the objectList
     */
    public ItemManager getItemList() 
    {
        return itemList;
    }
    
    /**
     * Returns the object list containing the roster entries
     * @return the objectList
     */
    public RosterEntryManager getRosterEntryList()
    {
    	return rosterEntryList;
    }
}
