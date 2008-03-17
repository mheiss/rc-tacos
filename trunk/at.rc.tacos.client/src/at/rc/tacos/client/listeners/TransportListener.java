package at.rc.tacos.client.listeners;

import java.util.ArrayList;

import org.eclipse.core.runtime.IStatus;

import at.rc.tacos.client.Activator;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Transport;

/**
 * This class will be notified upon roster entry updates
 * @author Michael
 */
public class TransportListener extends ClientListenerAdapter
{
    TransportManager manager = ModelFactory.getInstance().getTransportManager();
    
    @Override
    public void add(AbstractMessage addMessage)
    {
        manager.add((Transport)addMessage);
    }
    
	@Override
	public void update(AbstractMessage updateMessage) 
	{
		manager.update((Transport)updateMessage);
	}
	
	@Override
	public void remove(AbstractMessage removeMessage)
	{
	    manager.remove((Transport)removeMessage);
	}

	@Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        manager.removeAllEntries();  
        Activator.getDefault().log("Received "+listMessage.size()+ " of transports", IStatus.INFO);
        //add all
        for(AbstractMessage msg:listMessage)
        {
            Transport transport = (Transport)msg;
            manager.add(transport);
        }
    }  
}
