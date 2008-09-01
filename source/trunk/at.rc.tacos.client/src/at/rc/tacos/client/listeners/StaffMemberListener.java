package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.StaffManager;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.StaffMember;

/**
 * This class will be notified uppon staff member changes
 * @author Michael
 */
public class StaffMemberListener extends ClientListenerAdapter
{
	private StaffManager manager = ModelFactory.getInstance().getStaffManager();

	@Override
	public void add(AbstractMessage addMessage)
	{
		manager.add((StaffMember)addMessage);
	}

	@Override
	public void update(AbstractMessage updateMessage)
	{
		manager.update((StaffMember)updateMessage);  
	}

	@Override
	public void remove(AbstractMessage removeMessage)
	{
		manager.remove((StaffMember)removeMessage);  
	}

	@Override
	public void list(List<AbstractMessage> listMessage)
	{
		for(AbstractMessage msg:listMessage)
		{
			StaffMember member = (StaffMember)msg;
			//assert we do not have this member
			if(manager.contains(member))
				manager.update(member);
			else
				manager.add(member);
		}
	}  
}
