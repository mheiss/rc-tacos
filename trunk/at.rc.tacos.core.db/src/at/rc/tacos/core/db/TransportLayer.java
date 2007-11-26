package at.rc.tacos.core.db;

import java.sql.SQLException;

public interface TransportLayer 
{
	//transport
//	List<Transport> listTransports(String startDate, String endDate, String order, String orderSequence) throws SQLException;
//	List<Transport> listTransportbyID(String whereColumn, String whereValue, String orderColumn, String orderSequence) throws SQLException;
	String addTransport(String transport_ID, int direction_ID, int caller_ID, int patient_ID, int createdBy_ID, int disease_ID, String diseasenote, int priority, String feedback, String creationDate, String departure, String appointment, String appointmentPatient, boolean ambulant_stationary, boolean assistant, int employee_vehicle_ID, int transporttype_ID, int transportstate) throws SQLException;
	boolean updateTransport(String columnName, String newValue, String transport_ID) throws SQLException;
	
	
}
