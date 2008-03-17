package at.rc.tacos.client.listeners;

import java.util.ArrayList;
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
    @Override
    public void add(AbstractMessage addMessage)
    {
        ModelFactory.getInstance().getRosterEntryManager().add((RosterEntry)addMessage);
    }
    
	@Override
	public void update(AbstractMessage updateMessage) 
	{
		ModelFactory.getInstance().getRosterEntryManager().update((RosterEntry)updateMessage);
	}
	
	@Override
	public void remove(AbstractMessage removeMessage)
	{
	    ModelFactory.getInstance().getRosterEntryManager().remove((RosterEntry)removeMessage);
	}

	@Override
    public void list(ArrayList<AbstractMessage> listMessage)
    {
        RosterEntryManager manager = ModelFactory.getInstance().getRosterEntryManager();
        manager.removeAllEntries();
       
        for(AbstractMessage msg:listMessage)
        {
            RosterEntry entry = (RosterEntry)msg;
            manager.add(entry);
        }
    }  
}
