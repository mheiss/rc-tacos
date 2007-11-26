package at.rc.tacos.core.db;

import java.sql.SQLException;

public interface EmployeeVehicleLayer
{
	//employee_vehicle
	Integer addEmployeeVehicle(String vehicle_ID, int driver_ID, int medic1_ID, int medic2_ID, String note, int currentLocation) throws SQLException;
	boolean updateEmployeeVehicle(String columnName, String newValue, String employeeVehicleID) throws SQLException;
//	List<EmployeeVehicle> listEmployeeVehicles(String order, String orderSequence) throws SQLException;
//	List<EmployeeVehicle> listEmployeeVehiclesByID(String whereColum, String whereValue, String order, String orderSequence) throws SQLException;
}
