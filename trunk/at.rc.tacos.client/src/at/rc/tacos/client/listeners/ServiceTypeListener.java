package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import at.rc.tacos.client.modelManager.ServiceTypeManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeListener extends ClientListenerAdapter
{
	//the serviceType manager
	ServiceTypeManager manager = ModelFactory.getInstance().getServiceManager();
	
	@Override
	public void add(AbstractMessage addMessage)
	{
		//cast to a serviceType and add it
        ServiceType serviceType = (ServiceType)addMessage;
        manager.add(serviceType);
	}
	
    @Override
    public void remove(AbstractMessage removeMessage)
    {
    	//cast to a serviceType and remove it
    	ServiceType serviceType = (ServiceType)removeMessage;
        manager.remove(serviceType);
    }
	
	@Override
	public void update(AbstractMessage updateMessage)
	{
		//cast to a serviceType and add it
		ServiceType serviceType = (ServiceType)updateMessage;
        manager.update(serviceType);
	}
	
    @Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
    	//remove all stored serviceType
    	manager.removeAllEntries();
        //loop and add all serviceType
        for(AbstractMessage detailObject:listMessage)
        {
        	//cast to a serviceType and add it
            ServiceType serviceType = (ServiceType)detailObject;
            manager.add(serviceType);
        }
    }
}
