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
public class VehicleManager extends PropertyManager 
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
    public void remove(final VehicleDetail vehicle) 
    {
    	 Display.getDefault().syncExec(new Runnable ()    
         {
             public void run ()       
             {
		        objectList.remove(vehicle);
		        firePropertyChange("VEHICLE_REMOVE", vehicle, null); 
             }
         });
    }
    
    /**
     * Updates the vehicle in the list
     */
    public void update(final VehicleDetail vehicle)
    {
    	Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            { 
            	System.out.println("Manager: Update message");
            	//get the position of the entry
            	int index = objectList.indexOf(vehicle);
            	//We must reuse the vehicle we have in the memory and
            	//update all the vales
            	//This is because we use DataBinding to reflect the
            	//status of the vehicle
            	VehicleDetail update = objectList.get(index);
            	update.setVehicleId(vehicle.getVehicleId());
            	update.setVehicleName(vehicle.getVehicleName());
            	update.setVehicleType(vehicle.getVehicleType());
            	update.setVehicleNotes(vehicle.getVehicleNotes());
            	update.setBasicStation(vehicle.getBasicStation());
            	update.setCurrentStation(vehicle.getCurrentStation());
            	update.setDriverName(vehicle.getDriverName());
            	update.setParamedicIName(vehicle.getParamedicIName());
            	update.setParamedicIIName(vehicle.getParamedicIIName());
            	update.setMobilPhone(vehicle.getMobilePhone());
            	update.setMostImportantTransportStatus(vehicle.getMostImportantTransportStatus());
            	update.setOutOfOrder(vehicle.isOutOfOrder());
            	update.setReadyForAction(vehicle.isReadyForAction());
            	//at least update the images
            	update.updateImages();
            	//replace by the new
            	objectList.set(index, vehicle);
            	//we do not fire change event here, the values are updated by the data binding
            }
        });
    }
    
    /**
     * Clears the list of vehicles
     */
    public void resetVehicles()
    {
    	objectList.clear();
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
