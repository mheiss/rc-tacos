package at.rc.tacos.core.db;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.VehicleDetail;

public interface VehicleLayer 
{
	//vehicles
	List<VehicleDetail> listVehicles(String order, String orderSequence) throws SQLException;
	String addVehicle(String vehicle_ID, int phonenumber_ID, String vehicletype_ID, int primaryLocation, String note, int maxSeats, int mannedSeats) throws SQLException;
	boolean updateVehicle(String columnName, String newValue, String vehicleID) throws SQLException;
	VehicleDetail getVehicle(String vehicleID, String order, String orderSequence) throws SQLException;
}
