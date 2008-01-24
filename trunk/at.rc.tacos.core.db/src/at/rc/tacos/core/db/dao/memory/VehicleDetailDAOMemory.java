package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.VehicleDetail;

/**
 * Data source for vehicles
 * @author Michael
 */
public class VehicleDetailDAOMemory implements VehicleDAO
{
    //the shared instance
    private static VehicleDetailDAOMemory instance;
    
    //the data list
    private ArrayList<VehicleDetail> vehicleList; 

    /**
     * Default class constructor
     */
    private VehicleDetailDAOMemory()
    {
        vehicleList = new ArrayList<VehicleDetail>();
        //add test data
        for(VehicleDetail vehicle:TestDataSource.getInstance().vehicleList)
        	vehicleList.add(vehicle);
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static VehicleDetailDAOMemory getInstance()
    {
        if (instance == null)
            instance = new VehicleDetailDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        vehicleList = new ArrayList<VehicleDetail>();
    }
    
    @Override
    public boolean addVehicle(VehicleDetail vehicle)
    {
        vehicleList.add(vehicle);
        return false;
    }
    
    @Override
    public boolean updateVehicle(VehicleDetail vehicle)
    {
        int index = vehicleList.indexOf(vehicle);
        vehicleList.remove(index);
        vehicleList.add(index,vehicle);
        return true;
    }

    @Override
    public boolean removeVehicle(VehicleDetail vehicle)
    {
        vehicleList.remove(vehicle);
        return true;
    }

    @Override
    public VehicleDetail getVehicleByName(String vehicleName)
    {
        for(VehicleDetail detail:vehicleList)
        {
            if(detail.getVehicleName().equalsIgnoreCase(vehicleName))
                return detail;
        }
        return null;
    }

    @Override
    public List<VehicleDetail> listVehicles()
    {
        return vehicleList;
    }    
}
