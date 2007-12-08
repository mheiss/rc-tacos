package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class manages the vehicles.
 * @author Michael
 */
public class VehicleManager extends DataManager 
{
    //the item list
    private List<VehicleDetail> objectList = new ArrayList<VehicleDetail>();
    
    /**
     * Default class constructor
     */
    public VehicleManager()
    {
        objectList = new ArrayList<VehicleDetail>();
    }
    
    /**
     * Create initialisation data
     */
    public void init()
    {
        objectList = new TestDataSource().vehicleList;
    }
    
    /**
     * Adds a new vehicle to the vehicle manager.
     * This class is thread save, that means adding new vehicles,
     * can be done from any thread.
     * @param vehicle the vehicle to add
     */
    public void add(final VehicleDetail vehicle) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                //add the item
                objectList.add(vehicle);
                //notify the view
                firePropertyChange("VEHICLE_ADD", null, vehicle);
            }
        }); 
    }    

    /**
     * Removes the vehicle from the list
     * @param vehicle the vehicle to remove
     */
    public void remove(VehicleDetail vehicle) 
    {
        objectList.remove(vehicle);
        firePropertyChange("VEHICLE_REMOVE", vehicle, null); 
    }
    
    /**
     * Returns the current list of vehicles 
     * @return the vehicle list
     */
    public List<VehicleDetail> getVehicleList()
    {
        return objectList;
    }
}
