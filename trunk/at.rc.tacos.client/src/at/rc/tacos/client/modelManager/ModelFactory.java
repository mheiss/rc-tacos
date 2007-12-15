package at.rc.tacos.client.modelManager;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
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
    
    /**
     * Private class constructor.
     */
    private ModelFactory()
    {
        itemList = new ItemManager();
        rosterEntryList = new RosterEntryManager();
        vehicleList = new VehicleManager();
        staffList = new StaffManager();
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
        //get the client connectio
        Job job = new Job("Request data listing") 
        {
            protected IStatus run(IProgressMonitor monitor) 
            {
                NetWrapper net = NetWrapper.getDefault();
                net.requestListing(Item.ID, null);
                net.requestListing(VehicleDetail.ID, null);
                net.requestListing(RosterEntry.ID, null);
                net.requestListing(StaffMember.ID, null);
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
}
