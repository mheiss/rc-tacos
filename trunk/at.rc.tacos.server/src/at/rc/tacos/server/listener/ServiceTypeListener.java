package at.rc.tacos.server.listener;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeListener extends ServerListenerAdapter
{
	private ServiceTypeDAO serviceDao = DaoFactory.MYSQL.createServiceTypeDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject) throws DAOException
    {
        ServiceType serviceType = (ServiceType)addObject;
        int id = serviceDao.addServiceType(serviceType);
        if(id == -1)
        	throw new DAOException("ServiceTypeListener","Failed to add the service type:"+serviceType);
        serviceType.setId(id);
        return addObject;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	List<ServiceType> serviceList = serviceDao.listServiceTypes();
    	if(serviceList == null)
    		throw new DAOException("ServiceTypeListener","Failed to list the service types");
    	list.addAll(serviceList);
    	return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException
    {
    	ServiceType serviceType = (ServiceType)removeObject;
    	if(!serviceDao.removeServiceType(serviceType.getId()))
    		throw new DAOException("ServiceTypeListener","Failed to remove the service type: "+serviceType);
    	return serviceType;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject) throws DAOException
    {
    	ServiceType serviceType = (ServiceType)updateObject;
    	if(!serviceDao.updateServiceType(serviceType))
    		throw new DAOException("ServiceTypeListener","Failed to update the service type: "+serviceType);
    	return serviceType;
    }
}