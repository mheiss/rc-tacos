package at.rc.tacos.client.modelManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;

/**
 * This class manages the vehicles.
 * @author Michael
 */
public class VehicleManager extends PropertyManager implements PropertyChangeListener, ITransportStatus, IProgramStatus
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
        ModelFactory.getInstance().getTransportManager().addPropertyChangeListener(this);
        ModelFactory.getInstance().getRosterEntryManager().addPropertyChangeListener(this);
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
                objectList.add(vehicle);
                firePropertyChange("VEHICLE_ADD", null, vehicle);
            }
        }); 
    }    
    
    /**
     * Adds all vehicle to the vehicle manager and sends notifies the listeners.
     * @param vehicleList the list of vehicles to add
     */
    public void addAll(final List<VehicleDetail> vehicleList)
    {
    	Display.getDefault().syncExec(new Runnable()
    	{
    		public void run()
    		{
    			for(VehicleDetail detail:vehicleList)
    			{
    				objectList.add(detail);
    			}
				firePropertyChange("VEHICLE_ADD_ALL", null, vehicleList);
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
            	//assert we have this vehicle
            	if(!objectList.contains(vehicle))
            		return;
                //get the position of the entry
                int index = objectList.indexOf(vehicle);
                objectList.set(index, vehicle);
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
	 * Returns whether or not this vehicle is in the list of managed object
	 * @param newVehicle the vehicle to check
	 */
	public boolean contains(VehicleDetail newVehicle)
	{
		return objectList.contains(newVehicle);
	}
    
    /**
     *  informs all listeners about new locations
     */
    public void initViews(PropertyChangeListener listener)
    {
    	for(VehicleDetail detail:objectList)
    		listener.propertyChange(new PropertyChangeEvent(this,"VEHICLE_ADD", null, detail));
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
     * Returns the vehicle to which the staff member is currently assigned to
     * @param staffId the id of the staff to check
     * @return the vehicle or null if the staff member is not assigned to a vehicle
     */
    public VehicleDetail getVehicleOfStaff(int staffId)
    {
        for(VehicleDetail detail:objectList)
        {
            //assert valid
            if(detail.getDriver() != null)
            {
                if(detail.getDriver().getStaffMemberId() == staffId)
                    return detail;
            }
            //assert valid
            if(detail.getFirstParamedic() != null)
            {
                if(detail.getFirstParamedic().getStaffMemberId() == staffId)
                    return detail;
            }
            //assert valid
            if(detail.getSecondParamedic() != null)
            {
                if(detail.getSecondParamedic().getStaffMemberId() == staffId)
                    return detail;
            }
        }
        //no assigned vehicle
        return null;
    }
    
    /**
     * Returns the vehicle to the given vehicle name
     * @param vehicleName the vehicle name to check
     * @return the vehicle if the given vehicleName matches no vehicle
     */
    public VehicleDetail getVehicleByName(String vehicleName)
    {
        for(VehicleDetail detail:objectList)
        {
            if(detail.getVehicleName().equalsIgnoreCase(vehicleName))
                return detail;
        }
        //no vehicle
        return null;
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
    	//to get the color status for the nef vehicle (--> add action)
    	if("TRANSPORT_ADD".equalsIgnoreCase(evt.getPropertyName()))
    	{
    		VehicleDetail vehicle;
    		//the transport manager
            TransportManager transportManager = ModelFactory.getInstance().getTransportManager();
            //the added transport
            Transport transport = (Transport)evt.getNewValue();
            
            if(transport == null)
            	return;
            
            if(transport.getVehicleDetail() == null)
            	return;
            
            //TODO: abkl�ren, wozu diese Abfrage gedacht war!
//            if(!transport.getVehicleDetail().getVehicleName().equalsIgnoreCase("NEF"))
//            	return;
            
            vehicle = transport.getVehicleDetail();
            
            int index = objectList.indexOf(vehicle);
            VehicleDetail detail = objectList.get(index);
            
            if(detail == null)
            	return;
            
            //get the list of transports
            List<Transport> transportList = transportManager.getUnderwayTransportsByVehicle(detail.getVehicleName());

            this.checkVehicleColorState(transportList, detail);
    	}
        if("TRANSPORT_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
        {	
            //the transport manager
            TransportManager transportManager = ModelFactory.getInstance().getTransportManager();

            //the updated transport
            Transport transport = (Transport)evt.getNewValue();
            
            //assert valid
            if(transport == null)
                return;
            
            VehicleDetail vehicle = null;
            
            //to update the vehicle color status in case of detaching a vehicle from a transport
            if(transport.getNotes() != null)
            {
	            if(transport.getProgramStatus() == PROGRAM_STATUS_OUTSTANDING && transport.getNotes().contains("Fahrzeugabzug"))
	            {
	            	List<VehicleDetail> vehicleList = new ArrayList<VehicleDetail>();
	                vehicleList = this.getReadyVehicleList();
	                for(VehicleDetail detachedVehicle : vehicleList)
	                {
	                	//get the list of transports
	                    List<Transport> vehicleDetachedTransportList = transportManager.getUnderwayTransportsByVehicle(detachedVehicle.getVehicleName());
	                	//simplest calculation comes first ;)
	                    //green (30) is for a 'underway'(program status) vehicle not possible
	                    if(vehicleDetachedTransportList.isEmpty())
	                    {
	                    	detachedVehicle.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
	                        NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detachedVehicle);
	                        continue;
	                    }
	                    //status list
	                    ArrayList<Integer> list = new ArrayList<Integer>();
	                    //get the most important status of each transport
	                    for(Transport trList:vehicleDetachedTransportList)
	                    {
	                        int mostImportantStatus = trList.getMostImportantStatusMessageOfOneTransport();
	                        list.add(mostImportantStatus);
	                    }
	
	                    //get most important status of one vehicle (from the list)
	                    //for a 'red' status
	                    if (list.contains(TRANSPORT_STATUS_START_WITH_PATIENT) || list.contains(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
	                    {
	                    	detachedVehicle.setTransportStatus(VehicleDetail.TRANSPROT_STATUS_RED); //10
	                        NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detachedVehicle);
	                        continue;
	                    }
	                    //for a 'yellow' status
	                    detachedVehicle.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_YELLOW); //20
	                    NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detachedVehicle);	
	                    System.out.println("10");
	                    continue;
	                }	
	            }
            }
  
            //assert valid
            if(transport.getVehicleDetail() == null)
            {
            	//check the old vehicle of the transport
                if(evt.getOldValue() == null)
                	return;
                System.out.println("Checking the old transport");
                vehicle = (VehicleDetail)evt.getOldValue();
            }
            else
            	vehicle = transport.getVehicleDetail();
            
            int index = objectList.indexOf(vehicle);
            VehicleDetail detail = objectList.get(index);

            //get the list of transports
            List<Transport> transportList = transportManager.getUnderwayTransportsByVehicle(detail.getVehicleName());


            //TODO this is the reason for the automatically vehicle updates (triggered from the DateTime (SWT.CALENDAR)- Field
            //without this calculation the status green is not set correctly
            
            this.checkVehicleColorState(transportList, detail);
        }
        
        if("ROSTERENTRY_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
        {	        	        	
            //the updated entry
            RosterEntry entry = (RosterEntry)evt.getNewValue();
            //assert valid
            if(entry == null)
                return;
            
            //get the staff
            StaffMember member = entry.getStaffMember();
            if(member == null)
            	return;
                
            //check if we have a vehicle for this member
            VehicleDetail detail = getVehicleOfStaff(member.getStaffMemberId());
            if(detail == null)
            	return;
           
            //we need only to check staff members who signed out
            if(entry.getRealEndOfWork() == 0)
            	return;
            
            //don't detach the staff member from the car if there is a checked in entry
            RosterEntryManager rosterManager = ModelFactory.getInstance().getRosterEntryManager();
            RosterEntry checkedInEntry = rosterManager.getCheckedInRosterEntryByStaffId(member.getStaffMemberId());
            Calendar calToday = Calendar.getInstance();
            
            if(checkedInEntry != null && ((MyUtils.isEqualDate(calToday.getTimeInMillis(), checkedInEntry.getPlannedEndOfWork()) ||
            		(MyUtils.isEqualDate(calToday.getTimeInMillis(), checkedInEntry.getPlannedStartOfWork())))))
            	return;
            
            //detach the staff member from the car
            //driver
            if(member.equals(detail.getDriver()))
            	detail.setDriver(null);
            //paramedic
            if(member.equals(detail.getFirstParamedic()))
            	detail.setFirstParamedic(null);
            //paramedic
            if(member.equals(detail.getSecondParamedic()))
            	detail.setSecondParamedic(null);
            
            //adjust the status
            detail.setReadyForAction(false);
            detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
            NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);  
            
            //show a message box
            MessageDialog.openInformation(Display.getDefault().getActiveShell(),
            		"Information",
            		"Der Mitarbeiter "+member.getFirstName() + " " +member.getLastName()+ " hat sich abgemeldet.\n" +
            		"Er wurde vom Fahrzeug "+detail.getVehicleName() +" abgezogen");
        }
    }
    
    private void checkVehicleColorState(List<Transport> transportList, VehicleDetail detail)
    {
        //simplest calculation comes first ;)
        //green (30) is for a 'underway'(program status) vehicle not possible
        if(transportList.isEmpty())
        {
        	if(!detail.getLastDestinationFree().equalsIgnoreCase(""))
        		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_BLUE);
        	else if(detail.getLastDestinationFree().equalsIgnoreCase(""))
        		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
        	
        	if(!detail.isReadyForAction() || detail.isOutOfOrder())
        		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
            NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
            return;
        }

        //status list
        ArrayList<Integer> list = new ArrayList<Integer>();
        //get the most important status of each transport
        for(Transport trList:transportList)
        {
            int mostImportantStatus = trList.getMostImportantStatusMessageOfOneTransport();
            list.add(mostImportantStatus);
        }

        //get most important status of one vehicle (from the list)

        //for a 'red' status
        if (list.contains(TRANSPORT_STATUS_START_WITH_PATIENT) || list.contains(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
        {
            detail.setTransportStatus(VehicleDetail.TRANSPROT_STATUS_RED); //10
            NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
            return;
        }

        //for a 'yellow' status
        detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_YELLOW); //20
        
        
        //for a 'gray' status
        if(detail.isOutOfOrder() |! detail.isReadyForAction() || detail.getDriver() == null)
        	detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
        
        NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
        
    }
}	