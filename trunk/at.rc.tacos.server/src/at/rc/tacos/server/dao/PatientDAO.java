package at.rc.tacos.server.dao;

import java.util.ArrayList;
import at.rc.tacos.model.Patient;

/**
 * Data source for patients
 * @author Michael
 */
public class PatientDAO
{
    //the shared instance
    private static PatientDAO instance;
    //the data list
    private ArrayList<Patient> patientList; 

    /**
     * Default private class constructor
     */
    private PatientDAO()
    {
        //create the list
        patientList = new ArrayList<Patient>();
        //load dummy data
        Patient p1 = new Patient(0,"Patient1","Patient1");
        Patient p2 = new Patient(0,"Patient2","Patient2");
        Patient p3 = new Patient(0,"Patient3","Patient3");
        patientList.add(p1);
        patientList.add(p2);
        patientList.add(p3);
    }

    /**
     * Creates and returns the shared instance
     */
    public static PatientDAO getInstance()
    {
        if( instance == null)
            instance = new PatientDAO();
        return instance;
    }
    
    /**
     * Returns the data list
     * @return the list of data
     */
    public ArrayList<Patient> getList()
    {
        return patientList;
    }
}
