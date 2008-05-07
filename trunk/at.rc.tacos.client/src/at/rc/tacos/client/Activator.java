package at.rc.tacos.client;

import java.util.Calendar;
import java.util.Random;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import at.rc.tacos.client.controller.CreateBackTransportFromDialysis;
import at.rc.tacos.client.controller.CreateTransportFromDialysis;
import at.rc.tacos.client.listeners.*;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.codec.AddressDecoder;
import at.rc.tacos.codec.AddressEncoder;
import at.rc.tacos.codec.CallerDecoder;
import at.rc.tacos.codec.CallerEncoder;
import at.rc.tacos.codec.CompetenceDecoder;
import at.rc.tacos.codec.CompetenceEncoder;
import at.rc.tacos.codec.DayInfoMessageDecoder;
import at.rc.tacos.codec.DayInfoMessageEncoder;
import at.rc.tacos.codec.DialysisDecoder;
import at.rc.tacos.codec.DialysisEncoder;
import at.rc.tacos.codec.DiseaseDecoder;
import at.rc.tacos.codec.DiseaseEncoder;
import at.rc.tacos.codec.JobDecoder;
import at.rc.tacos.codec.JobEncoder;
import at.rc.tacos.codec.LocationDecoder;
import at.rc.tacos.codec.LocationEncoder;
import at.rc.tacos.codec.LoginDecoder;
import at.rc.tacos.codec.LoginEncoder;
import at.rc.tacos.codec.LogoutDecoder;
import at.rc.tacos.codec.LogoutEncoder;
import at.rc.tacos.codec.MobilePhoneDecoder;
import at.rc.tacos.codec.MobilePhoneEncoder;
import at.rc.tacos.codec.PatientDecoder;
import at.rc.tacos.codec.PatientEncoder;
import at.rc.tacos.codec.RosterEntryDecoder;
import at.rc.tacos.codec.RosterEntryEncoder;
import at.rc.tacos.codec.ServiceTypeDecoder;
import at.rc.tacos.codec.ServiceTypeEncoder;
import at.rc.tacos.codec.StaffMemberDecoder;
import at.rc.tacos.codec.StaffMemberEncoder;
import at.rc.tacos.codec.SystemMessageDecoder;
import at.rc.tacos.codec.SystemMessageEncoder;
import at.rc.tacos.codec.TransportDecoder;
import at.rc.tacos.codec.TransportEncoder;
import at.rc.tacos.codec.VehicleDecoder;
import at.rc.tacos.codec.VehicleEncoder;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.model.Address;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;


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
		factory.registerListener(at.rc.tacos.model.Job.ID,new JobListener());
		factory.registerListener(Location.ID, new LocationListener());
		factory.registerListener(ServiceType.ID, new ServiceTypeListener());
		factory.registerListener(Disease.ID, new DiseaseListener());
		factory.registerListener(Address.ID, new AddressListener());
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
		protFactory.registerDecoder(at.rc.tacos.model.Job.ID, new JobDecoder());
		protFactory.registerEncoder(at.rc.tacos.model.Job.ID, new JobEncoder());
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

	/**
	 * Starts a background thread that checks every minutes the status of the transports.
	 * When a prebooked transport is within the next 2 hours then the thread updates the 
	 * transport and moves it to the outstanding transports by changing the status and 
	 * sending a update request
	 */
	protected void backgroundTransportJob()
	{
		final Random rand = new Random(Calendar.getInstance().getTimeInMillis());
		//Start a new job
		final Job job = new Job("TransportMonitor") 
		{
			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try 
				{
					//the current time minus 2 hours
					Calendar current = Calendar.getInstance();
					current.add(Calendar.HOUR_OF_DAY, +2);
					//check the transports
					for(Transport transport:ModelFactory.getInstance().getTransportManager().getTransportList())
					{
						//check the status
						if(transport.getProgramStatus() != IProgramStatus.PROGRAM_STATUS_PREBOOKING)
							continue;
						//check the time
						if(current.getTimeInMillis() > transport.getPlannedStartOfTransport())
						{	
							transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
							NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
							log("Automatically moved the transport "+ transport+" to the outstanding transports",IStatus.INFO);
						}
					}
					//check the (to) dialysis patients
					for(DialysisPatient patient:ModelFactory.getInstance().getDialyseManager().getDialysisList())
					{
						Calendar currentDialysis = Calendar.getInstance();
						//first check: do we have already generated a transport for today?
						if(MyUtils.isEqualDate(patient.getLastTransportDate(),currentDialysis.getTimeInMillis()))
							continue;

						//after the date check we can add 2 hours
						currentDialysis.add(Calendar.HOUR_OF_DAY, +2);
						//second check: is the day correct?
						int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
						switch(day)
						{
						case Calendar.MONDAY: 
							if(!patient.isMonday())
								continue;
							break;
						case Calendar.TUESDAY:
							if(!patient.isTuesday())
								continue;
							break;
						case Calendar.WEDNESDAY:
							if(!patient.isWednesday())
								continue;
							break;
						case Calendar.FRIDAY:
							if(!patient.isFriday())
								continue;
							break;
						case Calendar.SATURDAY:
							if(!patient.isSaturday())
								continue;
							break;
						case Calendar.SUNDAY:
							if(!patient.isSunday())
								continue;
							break;
						default:
							continue;
						}
						//construct a calendar object with the start time (HH:mm)
						Calendar patientCal = Calendar.getInstance();
						patientCal.setTimeInMillis(patient.getPlannedStartOfTransport());
						//now add the current year,month and day
						patientCal.set(Calendar.YEAR, currentDialysis.get(Calendar.YEAR));
						patientCal.set(Calendar.MONTH, currentDialysis.get(Calendar.MONTH));
						patientCal.set(Calendar.DAY_OF_MONTH, currentDialysis.get(Calendar.DAY_OF_MONTH));

						//third check: is within the next two hour?
						if(currentDialysis.getTimeInMillis() > patientCal.getTimeInMillis())
						{
							//set the last generated transport date to now
							patient.setLastTransportDate(Calendar.getInstance().getTimeInMillis());
							NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, patient);
							//create and run the action
							CreateTransportFromDialysis createAction = new CreateTransportFromDialysis(patient,currentDialysis);
							createAction.run();
						}
					}
					
					//check the (from) dialysis patients
					for(DialysisPatient patient:ModelFactory.getInstance().getDialyseManager().getDialysisList())
					{
						Calendar currentDialysis = Calendar.getInstance();
						//first check: do we have already generated a back transport for today?
						if(MyUtils.isEqualDate(patient.getLastBackTransporDate(),currentDialysis.getTimeInMillis()))
							continue;

						//after the date check we can add 2 hours
						currentDialysis.add(Calendar.HOUR_OF_DAY, +2);
						//second check: is the day correct?
						int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
						switch(day)
						{
						case Calendar.MONDAY: 
							if(!patient.isMonday())
								continue;
							break;
						case Calendar.TUESDAY:
							if(!patient.isTuesday())
								continue;
							break;
						case Calendar.WEDNESDAY:
							if(!patient.isWednesday())
								continue;
							break;
						case Calendar.FRIDAY:
							if(!patient.isFriday())
								continue;
							break;
						case Calendar.SATURDAY:
							if(!patient.isSaturday())
								continue;
							break;
						case Calendar.SUNDAY:
							if(!patient.isSunday())
								continue;
							break;
						default:
							continue;
						}
						//construct a calendar object with the start time (HH:mm)
						Calendar patientCal = Calendar.getInstance();
						patientCal.setTimeInMillis(patient.getPlannedStartForBackTransport());
						//now add the current year,month and day
						patientCal.set(Calendar.YEAR, currentDialysis.get(Calendar.YEAR));
						patientCal.set(Calendar.MONTH, currentDialysis.get(Calendar.MONTH));
						patientCal.set(Calendar.DAY_OF_MONTH, currentDialysis.get(Calendar.DAY_OF_MONTH));

						//third check: is within the next two hour?
						if(currentDialysis.getTimeInMillis() > patientCal.getTimeInMillis())
						{
							//set the last generated transport date to now
							patient.setLastBackTransportDate(Calendar.getInstance().getTimeInMillis());
							NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, patient);
							//create and run the action
//							CreateTransportFromDialysis createAction = new CreateTransportFromDialysis(patient,currentDialysis);
							CreateBackTransportFromDialysis createAction = new CreateBackTransportFromDialysis(patient, currentDialysis);
							createAction.run();
						}
					}
					return Status.OK_STATUS;
				} 
				finally 
				{
					// start again in a minute plus a random time so that different clients use different times
					schedule(60000+rand.nextInt(30000)); 
				}
			}
		};
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
