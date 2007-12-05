package at.rc.tacos.client;

import java.util.ResourceBundle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import at.rc.tacos.client.listeners.*;
import at.rc.tacos.client.modelManager.*;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.model.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin 
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.client";
	// Configuration file for the images
	public static final String IMAGE_CLIENT_CONFIG_PATH = "at.rc.tacos.client.config.images";
	
	// The shared instance
	private static Activator plugin;
	
	//the object manager   
    private ItemManager itemList = new ItemManager();
    private RosterEntryManager rosterEntryList = new RosterEntryManager();
    private VehicleManager vehicleManager = new VehicleManager();
	
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

		//set the session
	    NetWrapper.getDefault().setSessionUsername("Client user");
	    
	    //load all needed images and register them
	    loadAndRegisterImages();
	    
	    //load default data
	    vehicleManager.init();
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
     * Convinience method to registers the ui listeners 
     * to get updates from the network layer.
     */
	public void registerListeners()
	{
	    ListenerFactory factory = ListenerFactory.getDefault();
	    //register the listeners
	    factory.registerListener(Item.ID, new ItemListener());
	    factory.registerListener(MobilePhoneDetail.ID, new MobilePhoneListener());
	    factory.registerListener(NotifierDetail.ID, new NotifyDetailListener());
	    factory.registerListener(Patient.ID, new PatientListener());
	    factory.registerListener(RosterEntry.ID, new RosterEntryListener());
	    factory.registerListener(StaffMember.ID, new StaffMemberListener());
	    factory.registerListener(Transport.ID, new TransportListener());
	    factory.registerListener(VehicleDetail.ID, new VehicleDetailListener());
	    factory.registerListener(Login.ID, new AuthenticationListener());
	    factory.registerListener(Logout.ID, new AuthenticationListener());
	    factory.registerListener(SystemMessage.ID, new SystemMessageListener());
	}
	
	/**
	 * Loads all image files from the image.properties 
	 * and registers them in the application.<br>
	 * The images can be accessed through the key value of the
	 * properties file.
	 */
	public void loadAndRegisterImages()
	{
	    try
        {
    	    //the factory to register the images
    	    ImageFactory f = ImageFactory.getInstance();
    	    
    	    //open the properties file
    	    ResourceBundle imageBundle = ResourceBundle.getBundle(Activator.IMAGE_CLIENT_CONFIG_PATH);
    	    //loop and register all images
    	    for(String imageKey:imageBundle.keySet())
    	    {
    	        //Create the image file with the given path
    	        String imagePath = imageBundle.getString(imageKey);
    	        ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, imagePath);
    	        f.registerImage(imageKey, imageDescriptor);
    	    }
        }
	    catch(NullPointerException npe)
	    {
	        System.out.println("Faild to load the images files");
	        System.out.println("Please check the images and the properties file");
	    }
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
    
    /**
     * Returns the vehicle manager containing all vehicle entries
     * @return the vehicle manager
     */
    public VehicleManager getVehicleManager()
    {
        return vehicleManager;
    }
}
