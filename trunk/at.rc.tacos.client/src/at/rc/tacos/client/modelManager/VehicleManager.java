package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.PlatformObject;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class manages the vehicles.
 * @author Michael
 */
public class VehicleManager extends PlatformObject 
{
    //the item list
    private List<VehicleDetail> objectList = new ArrayList<VehicleDetail>();
    
    /**
     * Default class constructor, initialising the list of vehicles
     */
    public VehicleManager()
    {
        objectList = new ArrayList<VehicleDetail>();
    }
    
    /**
     * Returns the current list of vehicles 
     * @return the vehicle list
     */
    public List<VehicleDetail> getVehicleList()
    {
        return objectList;
    }
    
    /**
     * Create initialisation data
     */
    public void init()
    {
        objectList = new TestDataSource().vehicleList;
    }
}
