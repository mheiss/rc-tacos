package at.rc.tacos.core.db.dao.test;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.EmployeeDAO;
import at.rc.tacos.model.StaffMember;

/**
 * Data source for staff members
 * @author Michael
 */
public class StaffMemberDAOTest implements EmployeeDAO
{
    //the shared instance
    private static StaffMemberDAOTest instance;
    
    //the data list
    private ArrayList<StaffMember> staffList; 
    
    /**
     * Default class constructor
     */
    private StaffMemberDAOTest()
    {
        staffList = new TestDataSource().staffList;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static StaffMemberDAOTest getInstance()
    {
        if (instance == null)
            instance = new StaffMemberDAOTest();
        return instance;
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
