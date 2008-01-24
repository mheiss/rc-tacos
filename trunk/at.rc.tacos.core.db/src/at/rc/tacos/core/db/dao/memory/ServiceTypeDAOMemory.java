package at.rc.tacos.core.db.dao.memory;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeDAOMemory implements ServiceTypeDAO
{
	//the shared instance
	private static ServiceTypeDAOMemory instance;
	
	//the service list
	List<ServiceType> serviceList;
	
	/**
	 * The default class constructor
	 */
	private ServiceTypeDAOMemory()
	{
		serviceList = new ArrayList<ServiceType>();
	}
	
	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static ServiceTypeDAOMemory getInstance()
	{
		if(instance == null)
			instance = new ServiceTypeDAOMemory();
		return instance;
	}
	
	@Override
	public int addServiceType(ServiceType serviceType) 
	{
		serviceList.add(serviceType);
		return serviceList.size();
	}

	@Override
	public ServiceType getServiceTypeId(int id) 
	{
		return serviceList.get(id);
	}

	@Override
	public List<ServiceType> listServiceTypes() 
	{
		return serviceList;
	}

	@Override
	public boolean removeServiceType(int id) 
	{
		if(serviceList.remove(id) != null)
			return true;
		//nothing removed
		return false;
	}

	@Override
	public boolean updateServiceType(ServiceType serviceType) 
	{
		int index = serviceList.indexOf(serviceType);
		serviceList.set(index, serviceType);
		return true;
	}
}
