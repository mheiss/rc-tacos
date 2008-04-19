package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeListener extends ServerListenerAdapter
{
	//the dao source to use
	private ServiceTypeDAO serviceDao = DaoFactory.SQL.createServiceTypeDAO();

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
	{
		ServiceType serviceType = (ServiceType)addObject;
		int id = serviceDao.addServiceType(serviceType);
		//assert we have a valid id
		if(id == -1)
			throw new DAOException("ServiceTypeListener","Failed to add the service type:"+serviceType);
		serviceType.setId(id);
		return addObject;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		//the basic list to hold the returned result
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<ServiceType> serviceList = new ArrayList<ServiceType>();
		if (queryFilter.containsFilterType(IFilterTypes.SERVICETYPE_FILTER)) {
			serviceList = serviceDao.listServiceTypesByName(queryFilter.getFilterValue(IFilterTypes.SERVICETYPE_FILTER));
		} else {
			serviceList = serviceDao.listServiceTypes();
		}

		list.addAll(serviceList);
		return list;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException,SQLException
	{
		ServiceType serviceType = (ServiceType)removeObject;
		if(!serviceDao.removeServiceType(serviceType.getId()))
			throw new DAOException("ServiceTypeListener","Failed to remove the service type: "+serviceType);
		return serviceType;
	}

	@Override
	public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException,SQLException
	{
		ServiceType serviceType = (ServiceType)updateObject;
		if(!serviceDao.updateServiceType(serviceType))
			throw new DAOException("ServiceTypeListener","Failed to update the service type: "+serviceType);
		return serviceType;
	}
}