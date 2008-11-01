package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.MobilePhoneDetail;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.MobilePhoneService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class MobilePhoneHandler implements INetHandler<MobilePhoneDetail> {

	@Service(clazz = MobilePhoneService.class)
	private MobilePhoneService phoneService;

	@Override
	public MobilePhoneDetail add(MobilePhoneDetail model) throws ServiceException, SQLException {
		int id = phoneService.addMobilePhone(model);
		if (id == -1)
			throw new ServiceException("Failed to add the mobile phone:" + model);
		model.setId(id);
		return model;
	}

	@Override
	public List<MobilePhoneDetail> execute(String command, List<MobilePhoneDetail> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<MobilePhoneDetail> get(Map<String, String> params) throws ServiceException, SQLException {
		List<MobilePhoneDetail> phoneList = phoneService.listMobilePhones();
		if (phoneList == null)
			throw new ServiceException("Failed to list the mobile phones");
		return phoneList;
	}

	@Override
	public MobilePhoneDetail remove(MobilePhoneDetail model) throws ServiceException, SQLException {
		if (!phoneService.removeMobilePhone(model.getId()))
			throw new ServiceException("Failed to remove the mobile phone:" + model);
		return model;
	}

	@Override
	public MobilePhoneDetail update(MobilePhoneDetail model) throws ServiceException, SQLException {
		if (!phoneService.updateMobilePhone(model))
			throw new ServiceException("Failed to update the mobile phone:" + model);
		return model;
	}
}
