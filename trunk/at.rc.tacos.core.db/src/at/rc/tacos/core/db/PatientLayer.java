package at.rc.tacos.core.db;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.*;

public interface PatientLayer
{
	//patients
	Integer addPatient(String firstname, String lastname, boolean sex, String birthday, int from_city_street_Streetnumber_ID, int to_city_street_streetnumber_ID) throws SQLException;
	Patient getPatient(String whereValue, String order, String orderSequence) throws SQLException;
	boolean updatePatient(String columnName, String newValue, String patientID) throws SQLException;
	List<Patient> listPatients(String order, String orderSequence) throws SQLException;
}
