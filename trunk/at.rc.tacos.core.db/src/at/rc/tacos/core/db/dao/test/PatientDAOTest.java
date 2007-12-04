package at.rc.tacos.core.db.dao.test;

import java.util.ArrayList;
import java.util.List;
import at.rc.tacos.core.db.dao.PatientDAO;
import at.rc.tacos.model.Patient;

/**
 * Data source for patients
 * @author Michael
 */
public class PatientDAOTest implements PatientDAO
{
    //the shared instance
    private static PatientDAOTest instance;
    
    //the data list
    private ArrayList<Patient> patientList; 

    /**
     * Default class constructor
     */
    private PatientDAOTest()
    {
        patientList = new TestDataSource().patientList;
    }
    
    /**
     * Returns the shared instance
     * @return the shared instance
     */
    public static PatientDAOTest getInstance()
    {
        if (instance == null)
            instance = new PatientDAOTest();
        return instance;
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
        return null;
    }

    @Override
    public List<Patient> listPatients()
    {
        return patientList;
    }
}
