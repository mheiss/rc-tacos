package at.rc.tacos.server.listener;

import java.util.ArrayList;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeListener extends ServerListenerAdapter
{
	private ServiceTypeDAO serviceDao = DaoFactory.MYSQL.createServiceTypeDAO();
	
    @Override
    public AbstractMessage handleAddRequest(AbstractMessage addObject)
    {
        ServiceType serviceType = (ServiceType)addObject;
        int id = serviceDao.addServiceType(serviceType);
        serviceType.setId(id);
        return addObject;
    }

    @Override
    public ArrayList<AbstractMessage> handleListingRequest(QueryFilter queryFilter)
    {
    	ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
    	list.addAll(serviceDao.listServiceTypes());
    	return list;
    }

    @Override
    public AbstractMessage handleRemoveRequest(AbstractMessage removeObject)
    {
    	ServiceType serviceType = (ServiceType)removeObject;
    	serviceDao.removeServiceType(serviceType.getId());
    	return serviceType;
    }

    @Override
    public AbstractMessage handleUpdateRequest(AbstractMessage updateObject)
    {
    	ServiceType serviceType = (ServiceType)updateObject;
    	serviceDao.updateServiceType(serviceType);
    	return serviceType;
    }
}