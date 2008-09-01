package at.rc.tacos.client;

import java.util.Calendar;
import java.util.Random;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.client.jobs.TransportJob;
import at.rc.tacos.client.listeners.AddressListener;
import at.rc.tacos.client.listeners.CompetenceListener;
import at.rc.tacos.client.listeners.DialysisPatientListener;
import at.rc.tacos.client.listeners.DiseaseListener;
import at.rc.tacos.client.listeners.JobListener;
import at.rc.tacos.client.listeners.LocationListener;
import at.rc.tacos.client.listeners.LockListener;
import at.rc.tacos.client.listeners.MobilePhoneListener;
import at.rc.tacos.client.listeners.NotifyDetailListener;
import at.rc.tacos.client.listeners.PatientListener;
import at.rc.tacos.client.listeners.RosterEntryListener;
import at.rc.tacos.client.listeners.ServiceTypeListener;
import at.rc.tacos.client.listeners.SessionListener;
import at.rc.tacos.client.listeners.SickPersonListener;
import at.rc.tacos.client.listeners.StaffMemberListener;
import at.rc.tacos.client.listeners.TransportListener;
import at.rc.tacos.client.listeners.VehicleDetailListener;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.platform.codec.AddressDecoder;
import at.rc.tacos.platform.codec.AddressEncoder;
import at.rc.tacos.platform.codec.CallerDecoder;
import at.rc.tacos.platform.codec.CallerEncoder;
import at.rc.tacos.platform.codec.CompetenceDecoder;
import at.rc.tacos.platform.codec.CompetenceEncoder;
import at.rc.tacos.platform.codec.DayInfoMessageDecoder;
import at.rc.tacos.platform.codec.DayInfoMessageEncoder;
import at.rc.tacos.platform.codec.DialysisDecoder;
import at.rc.tacos.platform.codec.DialysisEncoder;
import at.rc.tacos.platform.codec.DiseaseDecoder;
import at.rc.tacos.platform.codec.DiseaseEncoder;
import at.rc.tacos.platform.codec.JobDecoder;
import at.rc.tacos.platform.codec.JobEncoder;
import at.rc.tacos.platform.codec.LocationDecoder;
import at.rc.tacos.platform.codec.LocationEncoder;
import at.rc.tacos.platform.codec.LockDecoder;
import at.rc.tacos.platform.codec.LockEncoder;
import at.rc.tacos.platform.codec.LoginDecoder;
import at.rc.tacos.platform.codec.LoginEncoder;
import at.rc.tacos.platform.codec.LogoutDecoder;
import at.rc.tacos.platform.codec.LogoutEncoder;
import at.rc.tacos.platform.codec.MobilePhoneDecoder;
import at.rc.tacos.platform.codec.MobilePhoneEncoder;
import at.rc.tacos.platform.codec.PatientDecoder;
import at.rc.tacos.platform.codec.PatientEncoder;
import at.rc.tacos.platform.codec.RosterEntryDecoder;
import at.rc.tacos.platform.codec.RosterEntryEncoder;
import at.rc.tacos.platform.codec.ServiceTypeDecoder;
import at.rc.tacos.platform.codec.ServiceTypeEncoder;
import at.rc.tacos.platform.codec.SickPersonDecoder;
import at.rc.tacos.platform.codec.SickPersonEncoder;
import at.rc.tacos.platform.codec.StaffMemberDecoder;
import at.rc.tacos.platform.codec.StaffMemberEncoder;
import at.rc.tacos.platform.codec.SystemMessageDecoder;
import at.rc.tacos.platform.codec.SystemMessageEncoder;
import at.rc.tacos.platform.codec.TransportDecoder;
import at.rc.tacos.platform.codec.TransportEncoder;
import at.rc.tacos.platform.codec.VehicleDecoder;
import at.rc.tacos.platform.codec.VehicleEncoder;
import at.rc.tacos.platform.factory.ListenerFactory;
import at.rc.tacos.platform.factory.ProtocolCodecFactory;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.DayInfoMessage;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.Logout;
import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.model.Patient;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.SystemMessage;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;


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

	/**
	 * The constructor
	 */
	public Activator() { }

	/**
	 * Called when the plugin is started
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during startup
	 */
	@Override
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
		//load all needed images and register them
		loadAndRegisterImages();   
		registerListeners();
		registerEncoderAndDecoder();
	}
	
	/**
	 * Initalize the client
	 */
	public void init()
	{
		backgroundTransportJob();
	}

	/**
	 * Called when the plugin is stopped.
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during shutdown
	 */
	@Override
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
	private void registerListeners()
	{
		ListenerFactory factory = ListenerFactory.getDefault();
		//register the listeners
		factory.registerListener(MobilePhoneDetail.ID, new MobilePhoneListener());
		factory.registerListener(CallerDetail.ID, new NotifyDetailListener());
		factory.registerListener(Patient.ID, new PatientListener());
		factory.registerListener(RosterEntry.ID, new RosterEntryListener());
		factory.registerListener(StaffMember.ID, new StaffMemberListener());
		factory.registerListener(Transport.ID, new TransportListener());
		factory.registerListener(VehicleDetail.ID, new VehicleDetailListener());
		factory.registerListener(Login.ID, new SessionListener());
		factory.registerListener(Logout.ID, new SessionListener());
		factory.registerListener(SessionManager.ID, new SessionListener());
		factory.registerListener(SystemMessage.ID, new SessionListener());
		factory.registerListener(DialysisPatient.ID, new DialysisPatientListener());
		factory.registerListener(DayInfoMessage.ID, new SessionListener());
		factory.registerListener(Competence.ID, new CompetenceListener());
		factory.registerListener(at.rc.tacos.platform.model.Job.ID,new JobListener());
		factory.registerListener(Location.ID, new LocationListener());
		factory.registerListener(ServiceType.ID, new ServiceTypeListener());
		factory.registerListener(Disease.ID, new DiseaseListener());
		factory.registerListener(Address.ID, new AddressListener());
		factory.registerListener(Lock.ID, new LockListener());
		factory.registerListener(SickPerson.ID, new SickPersonListener());
	}

	/**
	 * Convenience method to registers the encoders and decoders.
	 */
	public void registerEncoderAndDecoder()
	{
		//register the needed model types with the decoders and encoders
		ProtocolCodecFactory protFactory = ProtocolCodecFactory.getDefault();
		protFactory.registerDecoder(MobilePhoneDetail.ID, new MobilePhoneDecoder());
		protFactory.registerEncoder(MobilePhoneDetail.ID, new MobilePhoneEncoder());
		protFactory.registerDecoder(CallerDetail.ID, new CallerDecoder());
		protFactory.registerEncoder(CallerDetail.ID, new CallerEncoder());
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
		protFactory.registerDecoder(Login.ID, new LoginDecoder());
		protFactory.registerEncoder(Login.ID, new LoginEncoder());
		protFactory.registerDecoder(Logout.ID, new LogoutDecoder());
		protFactory.registerEncoder(Logout.ID, new LogoutEncoder());
		protFactory.registerDecoder(SystemMessage.ID, new SystemMessageDecoder());
		protFactory.registerEncoder(SystemMessage.ID, new SystemMessageEncoder());
		protFactory.registerDecoder(DialysisPatient.ID, new DialysisDecoder());
		protFactory.registerEncoder(DialysisPatient.ID, new DialysisEncoder());
		protFactory.registerDecoder(DayInfoMessage.ID, new DayInfoMessageDecoder());
		protFactory.registerEncoder(DayInfoMessage.ID, new DayInfoMessageEncoder());
		protFactory.registerDecoder(at.rc.tacos.platform.model.Job.ID, new JobDecoder());
		protFactory.registerEncoder(at.rc.tacos.platform.model.Job.ID, new JobEncoder());
		protFactory.registerDecoder(Location.ID, new LocationDecoder());
		protFactory.registerEncoder(Location.ID, new LocationEncoder());
		protFactory.registerDecoder(Competence.ID, new CompetenceDecoder());
		protFactory.registerEncoder(Competence.ID, new CompetenceEncoder());
		protFactory.registerDecoder(ServiceType.ID, new ServiceTypeDecoder());
		protFactory.registerEncoder(ServiceType.ID, new ServiceTypeEncoder());
		protFactory.registerDecoder(Disease.ID,new DiseaseDecoder());
		protFactory.registerEncoder(Disease.ID, new DiseaseEncoder());
		protFactory.registerDecoder(Address.ID, new AddressDecoder());
		protFactory.registerEncoder(Address.ID, new AddressEncoder());
		protFactory.registerDecoder(Lock.ID, new LockDecoder());
		protFactory.registerEncoder(Lock.ID,new LockEncoder());
		protFactory.registerDecoder(SickPerson.ID, new SickPersonDecoder());
		protFactory.registerEncoder(SickPerson.ID, new SickPersonEncoder());
	}

	/**
	 * Loads all image files from the image.properties 
	 * and registers them in the application.<br>
	 * The images can be accessed through the key value of the
	 * properties file.
	 */
	private void loadAndRegisterImages()
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
			Activator.getDefault().log("Please check the images and the properties file", IStatus.ERROR);
			System.out.println("Failed to load the images files");
			System.out.println("Please check the images and the properties file");
			npe.printStackTrace();
		}
	}

	/**
	 * Logs the given message
	 * @param message the message
	 * @param type the type of the message
	 */
	public void log(String message,int type)
	{
		Status status = new Status(type,Activator.PLUGIN_ID,message); 
		getLog().log(status);
	}

	
	protected void backgroundTransportJob()
	{
		final Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		TransportJob job = new TransportJob();
		job.addJobChangeListener(new JobChangeAdapter() 
		{
			@Override
			public void done(IJobChangeEvent event) 
			{
				if (!event.getResult().isOK())
					log("Failed to check the transports",IStatus.ERROR);
			}
		});
		job.setSystem(true);
		// start after a random time
		job.schedule(rand.nextInt(30000)); 
	}
}
