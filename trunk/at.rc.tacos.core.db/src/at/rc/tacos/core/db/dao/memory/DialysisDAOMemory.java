package at.rc.tacos.core.db.dao.memory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.model.DialysisPatient;

public class DialysisDAOMemory implements DialysisPatientDAO
{
    //the shared instance
    private static DialysisDAOMemory instance;
    
    //the data list
    private ArrayList<DialysisPatient> dialysisList; 
	
	/**
	 * Default private class constructor
	 */
	public DialysisDAOMemory()
	{
		dialysisList = new ArrayList<DialysisPatient>();
	}
	
	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static DialysisDAOMemory getInstance()
	{
		//create new or return
		if(instance == null)
			instance = new DialysisDAOMemory();
		return instance;
	}
	
	
	@Override
	public int addDialysisPatient(DialysisPatient patient) throws SQLException 
	{
		dialysisList.add(patient);
		return dialysisList.size();
	}

	@Override
	public DialysisPatient getDialysisPatientById(int patientID) throws SQLException 
	{
        for(DialysisPatient patient:dialysisList)
        {
            if(patient.getPatientId() == patientID)
                return patient;
        }
        return null;
	}

	@Override
	public List<DialysisPatient> listDialysisPatient() throws SQLException 
	{
		return dialysisList;
	}

	@Override
	public void removeDialysisPatient(DialysisPatient patient) throws SQLException 
	{
		dialysisList.remove(patient);
	}

	@Override
	public boolean updateDialysisPatient(DialysisPatient patient) throws SQLException 
	{
		int index = dialysisList.indexOf(patient);
		dialysisList.remove(index);
		dialysisList.add(index,patient);
		return true;
	}
}
