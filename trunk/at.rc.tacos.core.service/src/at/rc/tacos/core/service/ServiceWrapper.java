package at.rc.tacos.core.service;

//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.core.xml.codec.*;
import at.rc.tacos.model.*;

/**
 * The activator class controls the plug-in life cycle
 */
public class ServiceWrapper extends Plugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "at.rc.tacos.core.service";

	// The shared instance
	private static ServiceWrapper plugin;
	
	//the network layer
	private ServiceLayerImpl serviceImpl;
	//the database layer
	private DatabaseLayerImpl databaseImpl;
		
	/**
     * The constructor
     */
	public ServiceWrapper() 
	{ 
	    //create the service implementations
	    serviceImpl = new ServiceLayerImpl();
	    databaseImpl = new DatabaseLayerImpl();
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
	public static ServiceWrapper getDefault() 
	{
	    if (plugin == null)
	        plugin = new ServiceWrapper();
		return plugin;
	}
	
	/**
	 * Returns the network service implementation
	 * to access the network layer and register listeners.
	 * @return the implementation of the network layer
	 */
	public ServiceLayerImpl getServiceLayer()
	{
	    return serviceImpl;
	}
	
	/**
	 * Returns the database service implementation
	 * to access the database.
	 * @return the implementation of the database layer
	 */
	public DatabaseLayerImpl getDatabaseLayer()
	{
	    return databaseImpl;
	}
	
	/**
	 * Register the network listener
	 */
	public void registerNetworkListener()
	{
	    //register the serviceImpl for net events
        NetWrapper.getDefault().registerNetworkListener(serviceImpl);
	}
	
	/**
     * Registers the encoders and decoders
     */
    public void registerEncoderAndDecoder()
    {
        //register the needed model types with the decoders and encoders
        ProtocolCodecFactory protFactory = ProtocolCodecFactory.getDefault();
        protFactory.registerDecoder(Item.ID, new ItemDecoder());
        protFactory.registerEncoder(Item.ID, new ItemEncoder());
        protFactory.registerDecoder(MobilePhoneDetail.ID, new MobilePhoneDecoder());
        protFactory.registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
        protFactory.registerDecoder(NotifierDetail.ID, new NotifierDecoder());
        protFactory.registerEncoder(NotifierDetail.ID, new NotifierEncoder());
        protFactory.registerDecoder(Patient.ID, new PatientDecoder());
        protFactory.registerEncoder(Patient.ID, new PatientEncoder());
        protFactory.registerDecoder(RosterEntry.ID, new RosterEntryDecoder());
        protFactory.registerEncoder(RosterEntry.ID, new RosterEntryEncoder());
        protFactory.registerDecoder(StaffMember.ID, new StaffMemberDecoder());
        protFactory.registerEncoder(StaffMember.ID, new StaffMemberEncoder());
        protFactory.registerDecoder(Transport.ID, new TransportDecoder());
        protFactory.registerEncoder(Transport.ID, new TransportEncoder());
        protFactory.registerDecoder(VehicleDetail.ID, new VehicleDecoder());
        protFactory.registerEncoder(VehicleDetail.ID, new VehicleEncoder()); 
    }
}
