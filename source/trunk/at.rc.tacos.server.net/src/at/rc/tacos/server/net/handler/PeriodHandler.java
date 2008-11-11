package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Period;
import at.rc.tacos.platform.net.mina.INetHandler;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.PeriodsService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;

public class PeriodHandler implements INetHandler<Period> {

	@Service(clazz = PeriodsService.class)
	private PeriodsService periodService;

	@Override
	public Period add(Period model) throws ServiceException, SQLException {
		int id = periodService.addPeriod(model);
		if (id == -1)
			throw new ServiceException("Failed to add the period record: " + model);
		model.setPeriodId(id);
		return model;
	}

	@Override
	public List<Period> execute(String command, List<Period> modelList, Map<String, String> params) throws ServiceException, SQLException {
		throw new NoSuchCommandException(command);
	}

	@Override
	public List<Period> get(Map<String, String> params) throws ServiceException, SQLException {
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
	public Period remove(Period model) throws ServiceException, SQLException {
		if (!periodService.removePeriod(model.getPeriodId()))
			throw new ServiceException("Failed to remove the period record: " + model);
		return model;
	}

	@Override
	public Period update(Period model) throws ServiceException, SQLException {
		if (!periodService.updatePeriod(model))
			throw new ServiceException("Failed to update the period record: " + model);
		return model;
	}

}
