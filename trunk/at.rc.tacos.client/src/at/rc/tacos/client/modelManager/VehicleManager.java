package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
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
                //get the position of the entry
                int index = objectList.indexOf(vehicle);
                //replace by the new
                objectList.set(index, vehicle);
                //update the data binding
                firePropertyChange("VEHICLE_UPDATE",null,vehicle);
            }
        });
    }

    /**
     * Clears the list of vehicles
     */
    public void resetVehicles()
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                firePropertyChange("VEHICLE_CLEAR", null, null);
                objectList.clear();
            }
        });
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
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
     * Returns a list of all vehicles which have NOT the status <code>VehicleDetail.outOfOrder</code><br>
     * and the status the status <code>VehicleDetail.readyForAction.</code>
     * In fact this will return a list of all vehicles which can be used.
     * @return list of vehicles ready for action
     */
    public List<VehicleDetail> getReadyVehicleList()
    {
        List<VehicleDetail> filteredList = new ArrayList<VehicleDetail>();
        //loop over all vehicles
        for(VehicleDetail detail:objectList)
        {
            if(!detail.isOutOfOrder() && detail.isReadyForAction())
                filteredList.add(detail);
        }
        return filteredList;
    }
    
    /**
     * Returns a list of all vehicles which have NOT the status <code>VehicleDetail.outOfOrder</code><br>
     * and the status the status <code>VehicleDetail.readyForAction for the given station</code>
     * In fact this will return a list of all vehicles which can be used.
     * @param stationName the name of the station to get the list
     * @return list of vehicles ready for action
     */
    public List<VehicleDetail> getReadyVehicleListbyStation(String stationName)
    {
        List<VehicleDetail> filteredList = new ArrayList<VehicleDetail>();
        //loop over all vehicles
        for(VehicleDetail detail:objectList)
        {
            if(!detail.isOutOfOrder() && detail.isReadyForAction() && detail.getBasicStation().equalsIgnoreCase(stationName))
                filteredList.add(detail);
        }
        return filteredList;
    }
}
