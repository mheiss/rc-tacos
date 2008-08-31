package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.server.db.dao.ServiceTypeDAO;
import at.rc.tacos.server.db.dao.factory.DaoFactory;
import at.rc.tacos.server.net.ServerContext;

public class ServiceTypeListener extends ServerListenerAdapter
{
	//the dao source to use
	private ServiceTypeDAO serviceDao = DaoFactory.SQL.createServiceTypeDAO();
	//the logger
	private static Logger logger = Logger.getLogger(ServiceTypeListener.class);
	private String username = ServerContext.getCurrentInstance().getSession().getUsername();

	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException,SQLException
	{
		ServiceType serviceType = (ServiceType)addObject;
		int id = serviceDao.addServiceType(serviceType);
		//assert we have a valid id
		if(id == -1)
			throw new DAOException("ServiceTypeListener","Failed to add the service type:"+serviceType);
		serviceType.setId(id);
		logger.info("added by:" +username +";" +serviceType);
		return addObject;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException,SQLException
	{
		//the basic list to hold the returned result
		ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
		List<ServiceType> serviceList = new ArrayList<ServiceType>();
		if (queryFilter.containsFilterType(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER)) {
			serviceList = serviceDao.listServiceTypesByName(queryFilter.getFilterValue(IFilterTypes.SERVICETYPE_SERVICENAME_FILTER));
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
		logger.info("updated by: " +username +";" +serviceType);
		return serviceType;
	}
}