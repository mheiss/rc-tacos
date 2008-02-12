package at.rc.tacos.client.modelManager;

import java.util.Calendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import at.rc.tacos.client.util.Util;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
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

/**
 * The model factory manages the lifecyle of all model managers.
 * Furthermore it provides methods to query the data from the server.
 * This class uses the singelton approach
 * @author Michael
 */
public class ModelFactory
{
    //the shared instance
    private static ModelFactory instance;

    //the model manager to handle
    private final RosterEntryManager rosterEntryList = new RosterEntryManager();
    private final VehicleManager vehicleList = new VehicleManager();
    private final StaffManager staffList = new StaffManager();
    private final LoginManager loginList = new LoginManager();
    private final TransportManager transportList = new TransportManager();
    private final DialysisTransportManager dialyseList = new DialysisTransportManager();
    private final MobilePhoneManager phoneList = new MobilePhoneManager();
    private final JobManager jobList = new JobManager();
    private final CompetenceManager competenceList = new CompetenceManager();
    private final LocationManager locationList = new LocationManager();
    private final ServiceTypeManager serviceList = new ServiceTypeManager();

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
    public void queryInitData()
    {
        //get the client connection
        org.eclipse.core.runtime.jobs.Job job = new org.eclipse.core.runtime.jobs.Job("Request data listing") 
        {
            protected IStatus run(IProgressMonitor monitor) 
            {
                NetWrapper net = NetWrapper.getDefault();
                //Set up a filter for the current day
                QueryFilter dateFilter = new QueryFilter();
                dateFilter.add(IFilterTypes.DATE_FILTER, Util.formatDate(Calendar.getInstance().getTimeInMillis()));
                net.requestListing(Location.ID, null);
                net.requestListing(Job.ID, null);
                net.requestListing(ServiceType.ID, null);
                net.requestListing(Competence.ID, null);
                net.requestListing(MobilePhoneDetail.ID, null);
                net.requestListing(RosterEntry.ID, dateFilter);
                net.requestListing(DayInfoMessage.ID, dateFilter);
                net.requestListing(VehicleDetail.ID, null);
                net.requestListing(Login.ID, null);
                net.requestListing(StaffMember.ID, null);
                dateFilter.add(IFilterTypes.TYPE_FILTER, Transport.TRANSPORT_PROGRESS);
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
    public final RosterEntryManager getRosterEntryList()
    {
        return rosterEntryList;
    }

    public final VehicleManager getVehicleList()
    {
        return vehicleList;
    }

    public final StaffManager getStaffList()
    {
        return staffList;
    }
    
    public final LoginManager getLoginList()
    {
    	return loginList;
    }

    public final TransportManager getTransportList()
    {
        return transportList;
    }

    public final DialysisTransportManager getDialyseList()
    {
        return dialyseList;
    }

    public final MobilePhoneManager getPhoneList()
    {
        return phoneList;
    }

    public final JobManager getJobList()
    {
        return jobList;
    }

    public final CompetenceManager getCompetenceList()
    {
        return competenceList;
    }

    public final LocationManager getLocationList()
    {
        return locationList;
    }

    public final ServiceTypeManager getServiceList()
    {
        return serviceList;
    }
}
