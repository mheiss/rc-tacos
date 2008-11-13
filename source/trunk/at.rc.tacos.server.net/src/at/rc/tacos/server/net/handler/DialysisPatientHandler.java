package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DialysisPatientService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class DialysisPatientHandler implements Handler<DialysisPatient> {

    @Service(clazz = DialysisPatientService.class)
    private DialysisPatientService dialysisPatientService;

    @Override
    public void add(ServerIoSession session, Message<DialysisPatient> message)
            throws ServiceException, SQLException {
        List<DialysisPatient> patientList = message.getObjects();
        // loop and try to add each patient object
        for (DialysisPatient patient : patientList) {
            int id = dialysisPatientService.addDialysisPatient(patient);
            if (id == -1)
                throw new ServiceException("Failed to add the dialysis patient: " + patient);
            patient.setId(id);
        }
        // send back the result
        session.write(message, patientList);
    }

    @Override
    public void get(ServerIoSession session, Message<DialysisPatient> message)
            throws ServiceException, SQLException {
        // get the params from the message
        Map<String, String> params = message.getParams();

        // query a single patient by the id
        if (params.containsKey(IFilterTypes.ID_FILTER)) {
            int id = Integer.parseInt(params.get(IFilterTypes.ID_FILTER));
            DialysisPatient patient = dialysisPatientService.getDialysisPatientById(id);
            // check the result
            if (patient == null)
                throw new ServiceException("Failed to get the dialysis patient with the id " + id);
            session.write(message, patient);
            return;
        }
        // if there is no filter -> request all
        List<DialysisPatient> patientList = dialysisPatientService.listDialysisPatient();
        if (patientList == null)
            throw new ServiceException("Failed to list the dialysis patients");
        session.write(message, patientList);
    }

    @Override
    public void remove(ServerIoSession session, Message<DialysisPatient> message)
            throws ServiceException, SQLException {
        List<DialysisPatient> patientList = message.getObjects();
        // loop and try to remove each patient object
        for (DialysisPatient patient : patientList) {
            if (!dialysisPatientService.removeDialysisPatient(patient.getId()))
                throw new ServiceException("Failed to update the dialysis patient " + patient);
        }
        session.writeBrodcast(message, patientList);
    }

    @Override
    public void update(ServerIoSession session, Message<DialysisPatient> message)
            throws ServiceException, SQLException {
        List<DialysisPatient> patientList = message.getObjects();
        // loop and try to update each patient object
        for (DialysisPatient patient : patientList) {
            if (!dialysisPatientService.updateDialysisPatient(patient))
                throw new ServiceException("Failed to update the dialysis patient " + patient);
        }
        session.writeBrodcast(message, patientList);
    }

    @Override
    public void execute(ServerIoSession session, Message<DialysisPatient> message)
            throws ServiceException, SQLException {
        // throw an execption because the 'exec' command is not implemented
        String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
        String handler = getClass().getSimpleName();
        throw new NoSuchCommandException(handler, command);
    }
}
