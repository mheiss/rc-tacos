package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Period;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.ServerIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.PeriodsService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class PeriodHandler implements Handler<Period> {

	@Service(clazz = PeriodsService.class)
	private PeriodsService periodService;

	@Override
	public void add(ServerIoSession session, Message<Period> message) throws ServiceException, SQLException {
		int id = periodService.addPeriod(model);
		if (id == -1)
			throw new ServiceException("Failed to add the period record: " + model);
		model.setPeriodId(id);
		return model;
	}

	@Override
	public void get(ServerIoSession session, Message<Period> message) throws ServiceException, SQLException {
		List<Period> periodList = new ArrayList<Period>();
		// if there is no filter -> request all
		if (params == null || params.isEmpty()) {
			throw new ServiceException("Listing of all period records is denied");
		}
		if (params.containsKey(IFilterTypes.SERVICETYPE_COMPETENCE_FILTER)) {
			final String serviceTypeCompetenceFilter = params.get(IFilterTypes.SERVICETYPE_COMPETENCE_FILTER);
			periodList = periodService.getPeriodListByServiceTypeCompetence(serviceTypeCompetenceFilter);
			if (periodList == null) {
				throw new ServiceException("Failed to list the periods by serviceTypeCompetence: " + serviceTypeCompetenceFilter);
			}
		}
		return periodList;
	}

	@Override
	public void remove(ServerIoSession session, Message<Period> message) throws ServiceException, SQLException {
		if (!periodService.removePeriod(model.getPeriodId()))
			throw new ServiceException("Failed to remove the period record: " + model);
		return model;
	}

	@Override
	public void update(ServerIoSession session, Message<Period> message) throws ServiceException, SQLException {
		if (!periodService.updatePeriod(model))
			throw new ServiceException("Failed to update the period record: " + model);
		return model;
	}

	@Override
	public void execute(ServerIoSession session, Message<Period> message) throws ServiceException, SQLException {
		// throw an execption because the 'exec' command is not implemented
		String command = message.getParams().get(AbstractMessage.ATTRIBUTE_COMMAND);
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}
}
