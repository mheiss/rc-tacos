package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.Transport;

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
	public void list(List<AbstractMessage> listMessage)
	{
		manager.removeAllEntries();  
		//add all
		for(AbstractMessage msg:listMessage)
		{
			Transport transport = (Transport)msg;
			//assert we do not have this transport
			if(manager.contains(transport))
				manager.update(transport);
			else
				manager.add(transport);
		}
	}  
}
