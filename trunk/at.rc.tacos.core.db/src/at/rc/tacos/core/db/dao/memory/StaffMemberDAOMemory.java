package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.TestDataSource;

/**
 * Data source for staff members
 * @author Michael
 */
public class StaffMemberDAOMemory implements StaffMemberDAO
{
    //the shared instance
    private static StaffMemberDAOMemory instance;
    
    //the data list
    private ArrayList<StaffMember> staffList; 
    
    /**
     * Default class constructor
     */
    private StaffMemberDAOMemory()
    {
        staffList = new ArrayList<StaffMember>();
        for(StaffMember member:TestDataSource.getInstance().staffList)
        	staffList.add(member);
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static StaffMemberDAOMemory getInstance()
    {
        if (instance == null)
            instance = new StaffMemberDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        staffList = new ArrayList<StaffMember>();
    }

    @Override
    public List<StaffMember> getAllStaffMembers()
    {
        return staffList;
    }

    @Override
    public StaffMember getStaffMemberByID(int id)
    {
        return staffList.get(id);
    }

    @Override
    public StaffMember getStaffMemberByUsername(String username)
    {
    	System.out.println("Request staff member for :"+username);
    	for(StaffMember member:staffList)
    	{
    		System.out.println("Checking: "+member.getUserName());
    		if(member.getUserName().equalsIgnoreCase(username))
    			return member;
    	}
    	//nothing found
    	return null;
    }

    @Override
    public List<StaffMember> getStaffMembersFromLocation(int locationId)
    {
    	List<StaffMember> filteredList = new ArrayList<StaffMember>();
    	//loop and filter
    	for(StaffMember member:staffList)
    	{
    		if(member.getPrimaryLocation().getId() == locationId)
    			filteredList.add(member);
    	}
    	//return the list
        return filteredList;
    }
    
    
    // METHODS ONLY FOR TESTING PURPOSE !!!
    
    /**
     * Adds a new staff member to the list
     */
    public int addStaffMember(StaffMember member)
    {
    	staffList.add(member);
    	return staffList.size();
    }
    
    /**
     * Updates the staff member
     */
    public boolean updateStaffMember(StaffMember member)
    {
    	int index = staffList.indexOf(member);
    	staffList.set(index, member);
    	return true;
    }
    
    /**
     * Removes the staff member
     */
    public boolean removeStaffMember(int id)
    {
    	if(staffList.remove(id) != null)
    		return true;
    	//Nothing removed
    	return false;
    }

	@Override
	public boolean updateCompetenceList(StaffMember staff) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateMobilePhoneList(StaffMember staff) {
		// TODO Auto-generated method stub
		return false;
	}
}
