package at.rc.tacos.core.db.dao;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.model.DialysisPatient;

public interface DialysisPatientDAO 
{
    public int addDialysisPatient(DialysisPatient patient) throws SQLException;
    public boolean updateDialysisPatient(DialysisPatient patient) throws SQLException;
    public void removeDialysisPatient(DialysisPatient patient) throws SQLException;
    
    public DialysisPatient getDialysisPatientById(int patientID) throws SQLException;  
	public List<DialysisPatient> listDialysisPatient() throws SQLException;
}
