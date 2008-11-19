package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.platform.model.ServiceType;

public class ServiceTypeManager extends PropertyManager 
{
    private List<ServiceType> objectList = new ArrayList<ServiceType>();

    /**
     * Default class constructor
     */
    public ServiceTypeManager() { }

    /**
     * Adds a new service type to the list
     * @param service type the service type to add
     */
    public void add(final ServiceType serviceType) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.add(serviceType);
                firePropertyChange("SERVICETYPE_ADD", null, serviceType);
            }
        }); 
    }    

    /**
     * Removes the service type from the list
     * @param service type the service type to remove
     */
    public void remove(final ServiceType serviceType) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
                objectList.remove(serviceType);
                firePropertyChange("SERVICETYPE_REMOVE", serviceType, null); 
            }
        }); 
    }
    
    
    /**
     * Updates the service type in the list
     * @param service type the service type to update
     */
    public void update(final ServiceType serviceType) 
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
            	//assert we have this service type
            	if(!objectList.contains(serviceType))
            		return;
                //get the position of the entry and update it
                int id = objectList.indexOf(serviceType);
                objectList.set(id, serviceType);
                firePropertyChange("SERVICETYPE_UPDATE", null, serviceType); 
            }
        }); 
    }
    
    /**
     * Removes all elements form the list
     */
    public void removeAllEntries()
    {
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {   
                objectList.clear();
                firePropertyChange("SERVICETYPE_CLEARED",null,null);
            }
        }); 
    }
    
    /**
     * Returns a given service by the name.
     * @param serviceName the name of the service to get
     */
    public ServiceType getServiceTypeByName(String serviceName)
    {
        //loop and search
        for(ServiceType service :objectList)
        {
            if(service.getServiceName().equalsIgnoreCase(serviceName))
                return service;
        }
        //nothing found
        return null;
    }
    
    /**
     * Returns whether or not this service type is in the list of managed object
     * @param newServiceType the serviceType to check
     */
    public boolean contains(ServiceType newServiceType)
    {
    	return objectList.contains(newServiceType);
    }

    /**
     * Converts the list to an array
     * @return the list as a array
     */
    public Object[] toArray()
    {
        return objectList.toArray();
    }
}