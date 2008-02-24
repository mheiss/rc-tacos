package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

/**
 * All transports
 * @author b.thek
 */
public class TransportManager extends PropertyManager implements ITransportStatus, IProgramStatus
{
	//the item list
	private List<Transport> objectList = new ArrayList<Transport>();
	//the date of the transports displayed in the transports view
	private Calendar displayedDate;

	/**
	 * Default class constructor
	 */
	public TransportManager() { }

	/**
	 * Adds a new transport to the list
	 * @param transport the transport to add
	 */
	public void add(final Transport transport) 
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				//add the item if we do not have it
				if(!objectList.contains(transport))
					objectList.add(transport);
				//notify the view
				firePropertyChange("TRANSPORT_ADD", null, transport);
			}
		}); 
	}    

	/**
	 * Removes the transport from the list
	 * @param transport the transport to remove
	 */
	public void remove(final Transport transport) 
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				objectList.remove(transport);
				firePropertyChange("TRANSPORT_REMOVE", transport, null); 
			}
		}); 
	}


	/**
	 * Updates the transport at the list
	 * @param transport the transport to update
	 */
	public void update(final Transport transport) 
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{  	
				//get the position of the entry
				int id = objectList.indexOf(transport);
				objectList.set(id, transport);
				firePropertyChange("TRANSPORT_UPDATE", null, transport); 
			}
		}); 
	}

	/**
	 * Removes all elements from the list EXCEPT the transports from the current day.
	 */
	public void removeAllEntries()
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{   
				//keep all transports form the current day
				Iterator<Transport> iter = objectList.iterator();
				while(iter.hasNext())
				{
					Transport transport = iter.next();
					//check the date
					if(!MyUtils.isEqualDate(transport.getDateOfTransport(),Calendar.getInstance().getTimeInMillis()))
					{
						//savely remove the element from the iterator
						iter.remove();
					}
				}
				objectList.clear();
				firePropertyChange("TRANSPORT_CLEARED",null,null);
			}
		}); 
	}

	/**
	 * Returns a list of all transports in the system.
	 * @return all managed transports
	 */
	public List<Transport> getTransportList()
	{
		return objectList;
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
	 * Informs the views that the selected date in the transport view filter has changed.
	 * @param newDate the newDate to display
	 */
	public void fireTransportViewFilterChanged(Calendar newDate)
	{
		this.displayedDate = newDate;
		//fire a property change event to notify the viewers that the date changed
		firePropertyChange("TRANSPORT_DATE_CHANGED",null,newDate);
	}

	/**
	 * Returns the date of the currently displayed transports choosen by the filter view.
	 * @return displayDate the displayed date
	 */
	public Calendar getDisplayedDate()
	{
		return displayedDate;
	}
	
	/**
	 * Returns a list of all transports that are assigned to this vehicle.
	 * @param vehicleName the name of the vehicle to list the transports
	 * @return transport list filtered by vehicle
	 */
	public List<Transport> getTransportsByVehicle(String vehicleName)
	{
		//the result list
		List<Transport> filteredList = new ArrayList<Transport>();
		//loop
		for(Transport transport:objectList)
		{
			//get the vehicle
			VehicleDetail assignedVehicle = transport.getVehicleDetail();
			//assert valid
			if(assignedVehicle == null)
				continue;
			//check the vehicle
			if(assignedVehicle.getVehicleName().equals(vehicleName))
				filteredList.add(transport);
		}
		return filteredList;
	}
	
	/**
	 * Returns a list of the transports with the program status 'journal' which are assigned
	 * to this vehicle and have no set transport status S6 yet.
	 * @param vehicleName the name of the vehicle to list the transports
	 * @return transort list filtered by vehicle, program status 'journal' and without transport status S6
	 */
	public List<Transport> getJournalTransportsByVehicleAndStatusSix(String vehicleName)
	{
		//the result list
		List<Transport> filteredList = new ArrayList<Transport>();
		System.out.println("TransportManager, getJournalTransportsBy......, objectlist size: " +objectList.size());
		//loop
		for(Transport transport:objectList)
		{
			//get the vehicle
			VehicleDetail vehicle = transport.getVehicleDetail();
			int programStatus = transport.getProgramStatus();
			//assert valid
			if(vehicle == null)
				continue;
			//check the vehicle
			if(vehicle.getVehicleName().equalsIgnoreCase(vehicleName) && programStatus == PROGRAM_STATUS_JOURNAL )
			{
				filteredList.add(transport);
			}
		}
		return filteredList;
	}
}


