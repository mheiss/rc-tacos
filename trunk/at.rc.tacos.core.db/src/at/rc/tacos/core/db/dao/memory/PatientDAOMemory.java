package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.PatientDAO;
import at.rc.tacos.model.Patient;

/**
 * Data source for patients
 * @author Michael
 */
public class PatientDAOMemory implements PatientDAO
{
    //the shared instance
    private static PatientDAOMemory instance;
    
    //the data list
    private ArrayList<Patient> patientList; 

    /**
     * Default class constructor
     */
    private PatientDAOMemory()
    {
        patientList = new ArrayList<Patient>();
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static PatientDAOMemory getInstance()
    {
        if (instance == null)
            instance = new PatientDAOMemory();
        return instance;
    }
    
    /**
     * Cleans up the data of the list
     */
    public void reset()
    {
        patientList = new ArrayList<Patient>();
    }

    @Override
    public int addPatient(Patient patient)
    {
        patientList.add(patient);
        return patientList.indexOf(patient);
    }
    
    @Override
    public void updatePatient(Patient patient)
    {
        int index = patientList.indexOf(patient);
        patientList.remove(index);
        patientList.add(index,patient);
    }
    
    @Override
    public void removePatient(Patient patient)
    {
        patientList.remove(patient);
        
    }

    @Override
    public Patient getPatientById(int patientId)
    {
        for(Patient patient:patientList)
        {
            if(patient.getPatientId() == patientId)
                return patient;
        }
        return null;
    }

    @Override
    public List<Patient> listPatients()
    {
        return patientList;
    }
}
