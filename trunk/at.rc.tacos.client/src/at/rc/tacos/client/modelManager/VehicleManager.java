package at.rc.tacos.client.modelManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class manages the vehicles.
 * @author Michael
 */
public class VehicleManager extends PropertyManager implements PropertyChangeListener, ITransportStatus
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
     * Initalizes the vehicle manager and adds the needed listeners
     */
    protected void init()
    {
    	ModelFactory.getInstance().getTransportList().addPropertyChangeListener(this);
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
     * @param location the location object to get the list from
     * @return list of vehicles ready for action
     */
    public List<VehicleDetail> getReadyVehicleListbyLocation(Location location)
    {
        List<VehicleDetail> filteredList = new ArrayList<VehicleDetail>();
        //loop over all vehicles
        for(VehicleDetail detail:objectList)
        {
            if(!detail.isOutOfOrder() && detail.isReadyForAction() && detail.getBasicStation().equals(location))
                filteredList.add(detail);
        }
        return filteredList;
    }
    
    /**
     * Returns the NEF- Vehicle
     * @return NEF- vehicle detail
     */
    public VehicleDetail getNEFVehicle()
    {
    	for(VehicleDetail detail : objectList)
    	{
    		if(detail.getVehicleName().equalsIgnoreCase("NEF"))
    			return detail;
    	}
    	return null;
    }

    /**
     * Loops over the vehicles and updates the most important transport status 
     * based on the assigned transports of the vehicle.<br>
     * This method is triggered each time a transport is updated.
     */
	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{		
		if("TRANSPORT_UPDATE".equalsIgnoreCase(evt.getPropertyName()) 
				|| "TRANSPORT_REMOVE".equalsIgnoreCase(evt.getPropertyName())
				|| "TRANSPORT_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "TRANSPORT_CLEARED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//The transport manager
			TransportManager transportManager = ModelFactory.getInstance().getTransportList();
			//loop over each vehicle
	    	for(VehicleDetail detail:objectList)
	    	{
	    		System.out.println("iiiiiimvvvvvvvVehicleManager, propertyChange");
	    		ArrayList<Integer> list = new ArrayList<Integer>();
	    		
	    		//get the list of transports
	    		List<Transport> transportList = transportManager.getTransportsByVehicle(detail.getVehicleName());
	    		//simplest calculation comes first ;)
	    		if(transportList.isEmpty())
	    		{
	    			System.out.println("VVVVVVVehicleManager, propertychange, im if tranpsortList.isEmpty, vehDetail: " + detail.getVehicleName());
	    			detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
	    			continue;
	    		}
	    		//get the most important status of each transport
	    		for(Transport transport:transportList)
	    		{
	    			System.out.println("iiiiiiiiiiiiiiiiimForTransport : transportlist, transport.fromStreet: " +transport.getFromStreet());
	    			int mostImportantStatus = transport.getMostImportantStatusMessageOfOneTransport();
	    			list.add(mostImportantStatus);
	    		}
	    		
	    		//get most important status of one vehicle (from the list)
    			//for a 'red' status
    			if (list.contains(TRANSPORT_STATUS_START_WITH_PATIENT) || list.contains(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
    				detail.setTransportStatus(VehicleDetail.TRANSPROT_STATUS_RED); //10
    			//for a 'yellow' status
    			else 
    				detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_YELLOW); //20
	    			
	    		//green (30) is for a 'underway'(program status) vehicle not possible	
    			NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
	    	}		
		}	
	}
}
