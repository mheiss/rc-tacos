package at.rc.tacos.core.db.dao;

import java.util.List;
import at.rc.tacos.model.*;

public interface PatientDAO
{
	public int addPatient(Patient patient);
	public void updatePatient(Patient patient);
	public void removePatient(Patient patient);
	
	public Patient getPatientById(int patientId);
	public List<Patient> listPatients();
}
