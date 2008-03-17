package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;

/**
 * This class manages the staff members
 * @author Michael
 */
public class StaffManager extends PropertyManager
{
    //the item list
    private List<StaffMember> objectList = new ArrayList<StaffMember>();

    /**
     * Adds a new staffMember to the list
     * @param staffMember the staff member to add
     */
    public void add(final StaffMember staffMember) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.add(staffMember);
                firePropertyChange("STAFF_ADD", null, staffMember);
            }
        }); 
    }    

    /**
     * Removes the staff member from the list
     * @param staffMember the member to remove
     */
    public void remove(final StaffMember staffMember) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(staffMember);
                firePropertyChange("STAFF_REMOVE", staffMember, null); 
            }
        }); 
    }


    /**
     * Updates the staff member in the list
     * @param staffMember the member to update
     */
    public void update(final StaffMember staffMember) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
            	//assert we have this staff member
            	if(!objectList.contains(staffMember))
            		return;
                //get the position of the entry
                int id = objectList.indexOf(staffMember);
                objectList.set(id, staffMember);
                firePropertyChange("STAFF_UPDATE", null, staffMember); 
            }
        }); 
    }
    
    /**
     * Removes all elements and resets the list
     */
    public void removeAllElements()
    {
        objectList.clear();
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                firePropertyChange("STAFF_CLEARED", null, null); 
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
     * Returns a list with all staff members
     * @return a staff list
     */
    public List<StaffMember> getStaffList()
    {
        return objectList;
    }
    
    /**
     * Returns a list of all staff members that are not assigned to a vehicle
     * @return list of staff members with no vehicle
     */
    public List<StaffMember> getUnassignedStaffList()
    {
    	VehicleManager vehicleManager = ModelFactory.getInstance().getVehicleManager();
    	List<StaffMember> filteredList = new ArrayList<StaffMember>();
    	for(StaffMember member:objectList)
    	{
    		//check if a vehicle is assigned
    		if(vehicleManager.getVehicleOfStaff(member.getStaffMemberId()) != null)
    			continue;
    		filteredList.add(member);//add if the staffMember is not assigned to a vehicle
    	}
    	return filteredList;
    }
    
    /**
     * Returns the staff member accociated with this username
     * @param username the username to get the staff member
     */
    public StaffMember getStaffMemberByUsername(String username)
    {
    	for(StaffMember member:objectList)
    	{
    		if(member.getUserName().equals(username))
    			return member;
    	}
    	//Nothing found
    	return null;
    }
    
    /**
     * Returns a list of all staff members that are not assigned to a vehicle and checked in by location
     * @return list of staff members with no vehicle
     */
    public List<StaffMember> getUnassignedCheckedInStaffListByLocation(Location location)
    {
    	VehicleManager vehicleManager = ModelFactory.getInstance().getVehicleManager();
    	List<StaffMember> filteredList = new ArrayList<StaffMember>();
    	RosterEntryManager rosterManager = ModelFactory.getInstance().getRosterEntryManager();
    	rosterManager.getCheckedInRosterEntriesByLocation(location);
    	List<StaffMember> staffMemberList = new ArrayList<StaffMember>();
    	for(StaffMember member:objectList)
    	{
    		for(RosterEntry entry : rosterManager.getCheckedInRosterEntriesByLocation(location))
    		{
    			if (entry.getStaffMember().getStaffMemberId() == member.getStaffMemberId())
    			staffMemberList.add(member);
    		}
    	}
    	for(StaffMember member:staffMemberList)
    	{
    		//check if a vehicle is assigned
    		if(vehicleManager.getVehicleOfStaff(member.getStaffMemberId()) != null)
    			continue;
    		filteredList.add(member);//add if the staffMember is not assigned to a vehicle
    	}
    	return filteredList;
    }
}
