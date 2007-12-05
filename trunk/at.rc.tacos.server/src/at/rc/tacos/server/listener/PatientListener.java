package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.PatientDAO;
import at.rc.tacos.model.Patient;
import at.rc.tacos.server.dao.DaoService;

/**
 * This class will be notified uppon Patient changes
 * @author Michael
 */
public class PatientListener extends ServerListenerAdapter
{
    private PatientDAO patientDao = DaoService.getInstance().getFactory().createPatientDAO();
    
    /**
     * Add a patient
     */
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        Patient patient = (Patient)addObject;
        int id = patientDao.addPatient(patient);
        patient.setPatientId(id);
        return patient;
    }

    /**
     * Listing of all patients
     */
    @Override
    public ArrayList<AbstractMessage> handleListingRequest()
    {
        ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
        list.addAll(patientDao.listPatients());
        return list;
    }

    /**
     * Patient removed
     */
    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
        Patient patient = (Patient)removeObject;
        patientDao.removePatient(patient);
        return patient;
    }

    /**
     * Update a patient
     */
    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
        Patient patient = (Patient)updateObject;
        patientDao.updatePatient(patient);
        return patient;
    }
}
