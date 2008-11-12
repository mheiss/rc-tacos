package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DiseaseService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class DiseaseHandler implements Handler<Disease> {

	@Service(clazz = DiseaseService.class)
	private DiseaseService diseaseService;

	@Override
	public void add(ServerIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		int id = diseaseService.addDisease(model);
		if (id == -1)
			throw new ServiceException("Failed to add the disease " + model);
		// set the generated id
		model.setId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = diseaseService.getDiseaseList();
		// assert valid
		if (diseaseList == null)
			throw new ServiceException("Failed to list the diseases");
		return diseaseList;
	}

	@Override
	public void remove(ServerIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		if (!diseaseService.removeDisease(model.getId()))
			throw new ServiceException("Failed to remove the disease: " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		if (!diseaseService.updateDisease(model))
			throw new ServiceException("Failed to update the disease: " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
