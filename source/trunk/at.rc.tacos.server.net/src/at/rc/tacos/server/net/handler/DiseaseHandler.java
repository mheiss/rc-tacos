package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.DiseaseService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class DiseaseHandler implements Handler<Disease> {

	@Service(clazz = DiseaseService.class)
	private DiseaseService diseaseService;

	@Override
	public void add(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = message.getObjects();
		// loop and try to add each object
		for (Disease disease : diseaseList) {
			int id = diseaseService.addDisease(disease);
			if (id == -1)
				throw new ServiceException("Failed to add the disease " + disease);
			// set the generated id
			disease.setId(id);
		}
		session.writeBrodcast(message, diseaseList);
	}

	@Override
	public void get(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {

		// request the listing of all disease objects
		List<Disease> diseaseList = diseaseService.getDiseaseList();
		if (diseaseList == null)
			throw new ServiceException("Failed to list the diseases");

		// send the list back
		session.write(message, diseaseList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = message.getObjects();
		// loop and try to remove each object
		for (Disease disease : diseaseList) {
			if (!diseaseService.removeDisease(disease.getId()))
				throw new ServiceException("Failed to remove the disease: " + disease);
		}
		session.writeBrodcast(message, diseaseList);
	}

	@Override
	public void update(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		List<Disease> diseaseList = message.getObjects();
		// loop and try to update each object
		for (Disease disease : diseaseList) {
			if (!diseaseService.updateDisease(disease))
				throw new ServiceException("Failed to update the disease: " + disease);
		}
		session.writeBrodcast(message, diseaseList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Disease> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
