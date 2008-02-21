package at.rc.tacos.client;

import java.util.Calendar;
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
import at.rc.tacos.client.listeners.*;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.factory.ListenerFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
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
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		plugin = this;
		//register the encoders and decoders
		NetWrapper.getDefault().registerEncoderAndDecoder();
		//load all needed images and register them
		loadAndRegisterImages();   
		registerListeners();
		backgroundTransportJob();
	}

	/**
	 * Called when the plugin is stopped.
	 * @param context lifecyle informations
	 * @throws Exception when a error occures during shutdown
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
		//Start a new job
		final Job job = new Job("TransportMonitor") 
		{
			protected IStatus run(IProgressMonitor monitor)
			{
				try 
				{
					//the current time minus 2 hours
					Calendar current = Calendar.getInstance();
					current.add(Calendar.HOUR_OF_DAY, +2);
					//check the transports
					for(Transport transport:ModelFactory.getInstance().getTransportList().getTransportList())
					{
						//check the status
						if(transport.getProgramStatus() != IProgramStatus.PROGRAM_STATUS_PREBOOKING)
							continue;
						//check the time
						if(current.getTimeInMillis() > transport.getPlannedStartOfTransport())
						{	
							transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
							NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
							
							//log
							log("Automatically moved the transport "+ transport+" to the outstanding transports",IStatus.INFO);
						}
						if (monitor.isCanceled()) 
							return Status.CANCEL_STATUS;
					}
					//check the dialysis patients
					for(DialysisPatient patient:ModelFactory.getInstance().getDialyseList().getDialysisList())
					{
						//check the time
						if(current.getTimeInMillis() < patient.getPlannedStartOfTransport())
							continue;
						
						//create a new transport for the dialysis patient if the planed times is within the next 2 hours
						Transport newTransport = new Transport();
						newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
						newTransport.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
						//the date time of the transport is the planed start of the transport
						newTransport.setDateOfTransport(patient.getPlannedStartOfTransport());
						newTransport.setTransportPriority("C");
						//set the known fields of the dialyis patient
						newTransport.setCreationTime(Calendar.getInstance().getTimeInMillis());
						newTransport.setFromStreet(patient.getFromStreet());
						newTransport.setFromCity(patient.getFromCity());
						newTransport.setToCity(patient.getToCity());
						newTransport.setToStreet(patient.getToStreet());
						newTransport.setPlannedStartOfTransport(patient.getPlannedStartOfTransport());
						newTransport.setPlannedTimeAtPatient(patient.getPlannedTimeAtPatient());
						newTransport.setAppointmentTimeAtDestination(patient.getAppointmentTimeAtDialysis());
						newTransport.setAssistantPerson(patient.isAssistantPerson());
						newTransport.setBackTransport(false);
						newTransport.setPatient(patient.getPatient());
						newTransport.setPlanedLocation(patient.getLocation());
						newTransport.setKindOfIllness("Dialyse");
						newTransport.setKindOfTransport(patient.getKindOfTransport());
						//add the transport to the database
						NetWrapper.getDefault().sendAddMessage(Transport.ID, newTransport);
						
						//log
						log("Automatically generated a transport "+newTransport+" for the dialyse patient "+patient,IStatus.INFO);

						if (monitor.isCanceled()) 
							return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				} 
				finally 
				{
					schedule(60000); // start again in a minutes
				}
			}
		};
		job.addJobChangeListener(new JobChangeAdapter() 
		{
			public void done(IJobChangeEvent event) 
			{
				if (!event.getResult().isOK())
					log("Failed to check the transports",IStatus.ERROR);
			}
		});
		job.setSystem(true);
		job.schedule(); // start as soon as possible
	}
}
