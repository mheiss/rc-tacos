package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DiseaseService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class DiseaseHandler implements INetHandler<Disease> {

	@Service(clazz = DiseaseService.class)
	private DiseaseService diseaseService;

	@Override
	public Disease add(Disease model) throws ServiceException, SQLException {
		int id = diseaseService.addDisease(model);
		if (id == -1)
			throw new ServiceException("Failed to add the disease " + model);
		// set the generated id
		model.setId(id);
		return model;
	}

	@Override
	public List<Disease> execute(String command, List<Disease> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Disease> get(Map<String, String> params) throws ServiceException, SQLException {
		List<Disease> diseaseList = diseaseService.getDiseaseList();
		// assert valid
		if (diseaseList == null)
			throw new ServiceException("Failed to list the diseases");
		return diseaseList;
	}

	@Override
	public Disease remove(Disease model) throws ServiceException, SQLException {
		if (!diseaseService.removeDisease(model.getId()))
			throw new ServiceException("Failed to remove the disease: " + model);
		return model;
	}

	@Override
	public Disease update(Disease model) throws ServiceException, SQLException {
		if (!diseaseService.updateDisease(model))
			throw new ServiceException("Failed to update the disease: " + model);
		return model;
	}

}
