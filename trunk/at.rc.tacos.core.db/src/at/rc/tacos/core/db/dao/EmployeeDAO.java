package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.StaffMember;

public interface EmployeeDAO 
{
    public int addEmployee(StaffMember member);
    public void updateEmployee(StaffMember member);
    public void deleteEmployee(StaffMember member);

    public StaffMember getEmployeeById(int employeeID);
    public List<StaffMember> listEmployees();
    public List<StaffMember> listEmployeesFromLocation(String locationname);
}
