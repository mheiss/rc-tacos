package at.rc.tacos.core.db.dao.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.model.StaffMember;

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
    public Integer addStaffMember(StaffMember staffMember, String pwdHash) throws SQLException
    {
        staffList.add(staffMember);
        return staffList.size();
    }
    
    @Override
    public boolean updateStaffMember(StaffMember staffMember) throws SQLException
    {
        int index = staffList.indexOf(staffMember);
        staffList.remove(index);
        staffList.add(index,staffMember);
        
        return true;
    }

    @Override
    public boolean deleteStaffMember(StaffMember member) throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public List<StaffMember> getAllStaffMembers() throws SQLException
    {
        return staffList;
    }

    @Override
    public StaffMember getStaffMemberByID(int id) throws SQLException
    {
        return staffList.get(id);
    }

    @Override
    public StaffMember getStaffMemberByUsername(String username) throws SQLException
    {
        return staffList.get(0);
    }

    @Override
    public List<StaffMember> getStaffMembersFromLocation(String locationname) throws SQLException
    {
        return staffList;
    }
}
