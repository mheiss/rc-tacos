package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
	public void add(ServerIoSession session, Message<DialysisPatient> message) throws ServiceException, SQLException {
		int id = dialysisPatientService.addDialysisPatient(model);
		if (id == -1)
			throw new ServiceException("Failed to add the dialysis patient: " + model);
		model.setId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<DialysisPatient> message) throws ServiceException, SQLException {
		List<DialysisPatient> patientList = new ArrayList<DialysisPatient>();

		// if there is no filter -> request all
		if (params == null || params.isEmpty()) {
			patientList = dialysisPatientService.listDialysisPatient();
			if (patientList == null)
				throw new ServiceException("Failed to list the dialysis patients");
			patientList.addAll(patientList);
		}
		else if (params.containsKey(IFilterTypes.ID_FILTER)) {
			int id = Integer.parseInt(params.get(IFilterTypes.ID_FILTER));
			DialysisPatient patient = dialysisPatientService.getDialysisPatientById(id);
			// check the result
			if (patient == null)
				throw new ServiceException("Failed to get the dialysis patient with the id " + id);
			patientList.add(patient);
		}
		// return the list
		return patientList;
	}

	@Override
	public void remove(ServerIoSession session, Message<DialysisPatient> message) throws ServiceException, SQLException {
		if (!dialysisPatientService.removeDialysisPatient(model.getId()))
			throw new ServiceException("Failed to update the dialysis patient " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<DialysisPatient> message) throws ServiceException, SQLException {
		if (!dialysisPatientService.updateDialysisPatient(model))
			throw new ServiceException("Failed to update the dialysis patient " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<DialysisPatient> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
