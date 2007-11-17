package at.rc.tacos.core.service;

//rcp
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.core.xml.codec.ItemDecoder;
import at.rc.tacos.core.xml.codec.ItemEncoder;
import at.rc.tacos.core.xml.codec.MobilePhoneDecoder;
import at.rc.tacos.core.xml.codec.MobilePhoneEncoder;
import at.rc.tacos.core.xml.codec.NotifierDecoder;
import at.rc.tacos.core.xml.codec.NotifierEncoder;
import at.rc.tacos.core.xml.codec.PatientDecoder;
import at.rc.tacos.core.xml.codec.PatientEncoder;
import at.rc.tacos.core.xml.codec.ProtocolCodecFactory;
import at.rc.tacos.core.xml.codec.RosterEntryDecoder;
import at.rc.tacos.core.xml.codec.RosterEntryEncoder;
import at.rc.tacos.core.xml.codec.StaffMemberDecoder;
import at.rc.tacos.core.xml.codec.StaffMemberEncoder;
import at.rc.tacos.core.xml.codec.TransportDecoder;
import at.rc.tacos.core.xml.codec.TransportEncoder;
import at.rc.tacos.core.xml.codec.VehicleDecoder;
import at.rc.tacos.core.xml.codec.VehicleEncoder;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

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
	    //register the serviceImpl for net events
	    NetWrapper.getDefault().registerNetworkListener(serviceImpl);
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
}
