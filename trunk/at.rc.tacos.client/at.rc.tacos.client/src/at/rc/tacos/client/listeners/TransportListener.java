package at.rc.tacos.client.listeners;

import java.util.ArrayList;
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
    @Override
    public void add(AbstractMessage addMessage)
    {
        ModelFactory.getInstance().getTransportManager().add((Transport)addMessage);
    }
    
	@Override
	public void update(AbstractMessage updateMessage) 
	{
		System.out.println("Updating the transport (in Listener): "+(Transport)updateMessage);
		ModelFactory.getInstance().getTransportManager().update((Transport)updateMessage);
	}
	
	@Override
	public void remove(AbstractMessage removeMessage)
	{
	    ModelFactory.getInstance().getTransportManager().remove((Transport)removeMessage);
	}

	@Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        TransportManager manager = ModelFactory.getInstance().getTransportManager();
        manager.removeAllEntries();
       
        for(AbstractMessage msg:listMessage)
        {
            Transport transport = (Transport)msg;
            manager.add(transport);
        }
    }  
}
