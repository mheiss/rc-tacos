package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DialysisPatientService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class DialysisPatientHandler implements INetHandler<DialysisPatient> {

	@Service(clazz = DialysisPatientService.class)
	private DialysisPatientService dialysisPatientService;

	@Override
	public DialysisPatient add(DialysisPatient model) throws ServiceException, SQLException {
		int id = dialysisPatientService.addDialysisPatient(model);
		if (id == -1)
			throw new ServiceException("Failed to add the dialysis patient: " + model);
		model.setId(id);
		return model;
	}

	@Override
	public List<DialysisPatient> execute(String command, List<DialysisPatient> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<DialysisPatient> get(Map<String, String> params) throws ServiceException, SQLException {
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
	public DialysisPatient remove(DialysisPatient model) throws ServiceException, SQLException {
		if (!dialysisPatientService.removeDialysisPatient(model.getId()))
			throw new ServiceException("Failed to update the dialysis patient " + model);
		return model;
	}

	@Override
	public DialysisPatient update(DialysisPatient model) throws ServiceException, SQLException {
		if (!dialysisPatientService.updateDialysisPatient(model))
			throw new ServiceException("Failed to update the dialysis patient " + model);
		return model;
	}

}
