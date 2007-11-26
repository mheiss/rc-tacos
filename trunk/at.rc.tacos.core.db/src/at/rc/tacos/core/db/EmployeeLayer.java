package at.rc.tacos.core.db;

import java.sql.SQLException;

public interface EmployeeLayer 
{
	//Employees
//	List<Employees> listEmployees(String order, String orderSequence) throws SQLException;
//	List<Employees> listEmployeesFromLocation(String locationname, String order, String orderSequence) throws SQLException;
//	Employees getEmployee(int employeeID) throws SQLException;
	Integer addEmployee(int primaryLocation, String firstname, String lastname, boolean sex, String birthday, String email, int city_street_streetnumber_ID, String username) throws SQLException;
	boolean updateEmployee(String columnName, String newValue, String employeeID) throws SQLException;
}
