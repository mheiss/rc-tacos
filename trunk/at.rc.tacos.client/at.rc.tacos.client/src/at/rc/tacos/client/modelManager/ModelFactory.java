package at.rc.tacos.client.modelManager;

import java.util.Calendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.client.util.Util;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
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
    private ItemManager itemList;
    private RosterEntryManager rosterEntryList;
    private VehicleManager vehicleList;
    private StaffManager staffList;
    private TransportManager transportList;
    private DialysisTransportManager dialysisList;
    private MobilePhoneManager phoneList;

    /**
     * Private class constructor.
     */
    private ModelFactory()
    {
        itemList = new ItemManager();
        rosterEntryList = new RosterEntryManager();
        vehicleList = new VehicleManager();
        staffList = new StaffManager();
        transportList = new TransportManager();
        dialysisList = new DialysisTransportManager();
        phoneList = new MobilePhoneManager();
    }
    
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
        Job job = new Job("Request data listing") 
        {
            protected IStatus run(IProgressMonitor monitor) 
            {
                NetWrapper net = NetWrapper.getDefault();
                //Set up a filter for the current day
                QueryFilter dateFilter = new QueryFilter();
                dateFilter.add(IFilterTypes.DATE_FILTER, Util.formatDate(Calendar.getInstance().getTimeInMillis()));
                net.requestListing(MobilePhoneDetail.ID, null);
                net.requestListing(RosterEntry.ID, dateFilter);
                net.requestListing(DayInfoMessage.ID, dateFilter);
                net.requestListing(VehicleDetail.ID, null);
                net.requestListing(StaffMember.ID, null);
                net.requestListing(Transport.ID, null);
                net.requestListing(DialysisPatient.ID, null);
                return Status.OK_STATUS;
            }
        };
        job.setPriority(Job.SHORT);
        job.setUser(true);
        job.schedule(); 
    }
    
    
    //GETTERS FOR THE MANAGERS
    /**
     * Returns the manager responsible for the items
     * @return the item manager
     */
    public ItemManager getItemManager()
    {
        return itemList;
    }
    
    /**
     * Returns the manager responsible for the roster entries
     * @return the roster manager
     */
    public RosterEntryManager getRosterManager()
    {
        return rosterEntryList;
    }
    
    /**
     * Returns the manager responsible for the vehicles
     * @return the vehicle manager
     */
    public VehicleManager getVehicleManager()
    {
        return vehicleList;
    }
    
    /**
     * Returns the manager respnosible for the staff
     * @return the staff manager
     */
    public StaffManager getStaffManager()
    {
        return staffList;
    }
    
    /**
     * Returns the manager responsible for the transports
     * @return the transport manager
     */
    public TransportManager getTransportManager()
    {
        return transportList;
    }
    
    /**
     * Returns the manager responsible for the dialysis transports
     * @return the dialysis transport manager
     */
    public DialysisTransportManager getDialysisTransportManager()
    {
        return dialysisList;
    }
    
    /**
     * Returns the manager responsible for the mobile phones
     * @return the phone manager
     */
    public MobilePhoneManager getMobilePhoneManager()
    {
        return phoneList;
    }
}
