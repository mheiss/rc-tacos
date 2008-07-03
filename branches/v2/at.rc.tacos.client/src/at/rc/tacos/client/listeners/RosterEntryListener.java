package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.RosterEntryManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.RosterEntry;

/**
 * This class will be notified uppon roster entry updates
 * @author Michael
 */
public class RosterEntryListener extends ClientListenerAdapter
{
	//the roster entry manager
	private RosterEntryManager manager = ModelFactory.getInstance().getRosterEntryManager();

	@Override
	public void add(AbstractMessage addMessage)
	{
		manager.add((RosterEntry)addMessage);
	}

	@Override
	public void update(AbstractMessage updateMessage) 
	{
		manager.update((RosterEntry)updateMessage);
	}

	@Override
	public void remove(AbstractMessage removeMessage)
	{
		manager.remove((RosterEntry)removeMessage);
	}

	@Override
	public void list(List<AbstractMessage> listMessage)
	{       
		for(AbstractMessage msg:listMessage)
		{
			RosterEntry entry = (RosterEntry)msg;
			//assert we do not have this entry
			if(manager.contains(entry))
				manager.update(entry);
			else
				manager.add(entry);
		}
	}  
}
