package at.rc.tacos.client.modelManager;

import java.util.Calendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;

/**
 * The model factory manages the lifecyle of all model managers.
 * Furthermore it provides methods to query the data from the server.
 * @author Michael
 */
public class ModelFactory
{
    //the shared instance
    private static ModelFactory instance;

    //the model manager to handle
    private final VehicleManager vehicleManager = new VehicleManager();
    private final StaffManager staffManager = new StaffManager();
    private final RosterEntryManager rosterEntryManager = new RosterEntryManager();
    private final TransportManager transportManager = new TransportManager();
    private final LoginManager loginManager = new LoginManager();
    private final DialysisTransportManager dialyseManager = new DialysisTransportManager();
    private final MobilePhoneManager phoneManager = new MobilePhoneManager();
    private final JobManager jobManager = new JobManager();
    private final CompetenceManager competenceManager = new CompetenceManager();
    private final LocationManager locationManager = new LocationManager();
    private final ServiceTypeManager serviceManager = new ServiceTypeManager();
    private final DiseaseManager diseaseManager = new DiseaseManager();
    private final AddressManager addressManager = new AddressManager();

    /**
     * Private class constructor.
     */
    private ModelFactory() { }

    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static ModelFactory getInstance()
    {
        if(instance == null)
            instance = new ModelFactory();
        return instance;
    }

    /**
     *  Queries the server sequentially for all needed data.
     */
    public void initalizeModel()
    {
    	//add the listeners
    	vehicleManager.init();
        //get the client connection
        org.eclipse.core.runtime.jobs.Job job = new org.eclipse.core.runtime.jobs.Job("Request data listing") 
        {
            @Override
			protected IStatus run(IProgressMonitor monitor) 
            {
                NetWrapper net = NetWrapper.getDefault();
                String now = MyUtils.timestampToString(Calendar.getInstance().getTimeInMillis(),MyUtils.timeAndDateFormat);
                //Set up a filter for the current day
                QueryFilter dateFilter = new QueryFilter();
                dateFilter.add(IFilterTypes.DATE_FILTER, now);
                net.requestListing(Location.ID, null);
                net.requestListing(Job.ID, null);
                net.requestListing(ServiceType.ID, null);
                net.requestListing(Competence.ID, null);
                net.requestListing(MobilePhoneDetail.ID, null);
                net.requestListing(RosterEntry.ID, dateFilter);
                net.requestListing(DayInfoMessage.ID, dateFilter);
                net.requestListing(VehicleDetail.ID, null);
                net.requestListing(Login.ID, null);
                net.requestListing(Disease.ID, null);
                net.requestListing(StaffMember.ID, null);
                net.requestListing(Transport.ID, dateFilter);
                net.requestListing(DialysisPatient.ID, null);
                return Status.OK_STATUS;
            }
        };
        job.setPriority(org.eclipse.core.runtime.jobs.Job.SHORT);
        job.setUser(true);
        job.schedule(); 
    }

    //GETTERS FOR THE MANAGER
    public final RosterEntryManager getRosterEntryManager()
    {
        return rosterEntryManager;
    }

    public final VehicleManager getVehicleManager()
    {
        return vehicleManager;
    }

    public final StaffManager getStaffManager()
    {
        return staffManager;
    }
    
    public final LoginManager getLoginManager()
    {
    	return loginManager;
    }

    public final TransportManager getTransportManager()
    {
        return transportManager;
    }

    public final DialysisTransportManager getDialyseManager()
    {
        return dialyseManager;
    }

    public final MobilePhoneManager getPhoneManager()
    {
        return phoneManager;
    }

    public final JobManager getJobList()
    {
        return jobManager;
    }

    public final CompetenceManager getCompetenceManager()
    {
        return competenceManager;
    }

    public final LocationManager getLocationManager()
    {
        return locationManager;
    }

    public final ServiceTypeManager getServiceManager()
    {
        return serviceManager;
    }
    
    public final DiseaseManager getDiseaseManager()
    {
    	return diseaseManager;
    }
    
    public final AddressManager getAddressManager()
    {
    	return addressManager;
    }
}
