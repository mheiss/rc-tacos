package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.model.VehicleDetail;

/**
 * Data source for vehicles
 * @author Michael
 */
public class VehicleDetailDAOTest implements VehicleDAO
{
    //the shared instance
    private static VehicleDetailDAOTest instance;
    
    //the data list
    private ArrayList<VehicleDetail> vehicleList; 

    /**
     * Default class constructor
     */
    private VehicleDetailDAOTest()
    {
        vehicleList = new ArrayList<VehicleDetail>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static VehicleDetailDAOTest getInstance()
    {
        if (instance == null)
            instance = new VehicleDetailDAOTest();
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
    public int addVehicle(VehicleDetail vehicle)
    {
        vehicleList.add(vehicle);
        return vehicleList.indexOf(vehicle);
    }
    
    @Override
    public void updateVehicle(VehicleDetail vehicle)
    {
        int index = vehicleList.indexOf(vehicle);
        vehicleList.remove(index);
        vehicleList.add(index,vehicle);
    }

    @Override
    public void removeVehicle(VehicleDetail vehicle)
    {
        vehicleList.remove(vehicle);
    }

    @Override
    public VehicleDetail getVehicleById(int vehicleId)
    {
        for(VehicleDetail detail:vehicleList)
        {
            if(detail.getVehicleId() == vehicleId)
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
