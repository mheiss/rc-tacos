package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.model.StaffMember;

/**
 * Data source for staff members
 * @author Michael
 */
public class StaffMemberDAOMemory implements EmployeeDAO
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
    public int addEmployee(StaffMember member)
    {
        staffList.add(member);
        return staffList.indexOf(member);
    }
    
    @Override
    public void updateEmployee(StaffMember member)
    {
        int index = staffList.indexOf(member);
        staffList.remove(index);
        staffList.add(index,member);
    }

    @Override
    public void deleteEmployee(StaffMember member)
    {
        staffList.remove(member);
    }

    @Override
    public StaffMember getEmployeeById(int employeeID)
    {
        for(StaffMember member:staffList)
        {
            if(member.getPersonId() == employeeID)
                return member;
        }
        return null;
    }

    @Override
    public List<StaffMember> listEmployees()
    {
        return staffList;
    }

    @Override
    public List<StaffMember> listEmployeesFromLocation(String locationname)
    {
        return staffList;
    }
}
