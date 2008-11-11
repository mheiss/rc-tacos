package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.CompetenceService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class CompetenceHandler implements INetHandler<Competence> {

	// the competence service
	@Service(clazz = CompetenceService.class)
	private CompetenceService competenceService;

	@Override
	public Competence add(Competence model) throws ServiceException, SQLException {
		int id = competenceService.addCompetence(model);
		// check for error while adding
		if (id == -1)
			throw new ServiceException("Failed to add the competence " + model + " to the database");
		model.setId(id);
		return model;
	}

	@Override
	public List<Competence> execute(String command, List<Competence> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Competence> get(Map<String, String> params) throws ServiceException, SQLException {
		List<Competence> compList = competenceService.listCompetences();
		if (compList == null)
			throw new ServiceException("Failed to list the competences, service returned null");
		return competenceService.listCompetences();
	}

	@Override
	public Competence remove(Competence model) throws ServiceException, SQLException {
		if (!competenceService.removeCompetence(model.getId()))
			throw new ServiceException("Failed to remove the competence " + model);
		return model;
	}

	@Override
	public Competence update(Competence model) throws ServiceException, SQLException {
		if (!competenceService.updateCompetence(model))
			throw new ServiceException("Failed to update the competence: " + model);
		return model;
	}

}
