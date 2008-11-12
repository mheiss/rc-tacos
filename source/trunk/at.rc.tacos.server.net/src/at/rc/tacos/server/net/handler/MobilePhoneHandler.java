package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;

import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class MobilePhoneHandler implements Handler<MobilePhoneDetail> {

	@Service(clazz = MobilePhoneService.class)
	private MobilePhoneService phoneService;

	@Override
	public void add(ServerIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
		int id = phoneService.addMobilePhone(model);
		if (id == -1)
			throw new ServiceException("Failed to add the mobile phone:" + model);
		model.setId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
		List<MobilePhoneDetail> phoneList = phoneService.listMobilePhones();
		if (phoneList == null)
			throw new ServiceException("Failed to list the mobile phones");
		return phoneList;
	}

	@Override
	public void remove(ServerIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
		if (!phoneService.removeMobilePhone(model.getId()))
			throw new ServiceException("Failed to remove the mobile phone:" + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
		if (!phoneService.updateMobilePhone(model))
			throw new ServiceException("Failed to update the mobile phone:" + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<MobilePhoneDetail> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
