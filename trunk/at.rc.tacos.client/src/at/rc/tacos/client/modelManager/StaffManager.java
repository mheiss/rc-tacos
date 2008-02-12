package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.swt.widgets.Display;
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
                //add the item
                objectList.add(staffMember);
                //notify the view
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
}
