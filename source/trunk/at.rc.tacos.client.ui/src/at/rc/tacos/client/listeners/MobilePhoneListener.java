package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.client.modelManager.MobilePhoneManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.platform.model.AbstractMessage;
import at.rc.tacos.platform.model.MobilePhoneDetail;

/**
 * This class will be notified uppon mobile phone changes
 * @author Michael
 */
public class MobilePhoneListener extends ClientListenerAdapter
{
	//the mobile phone manager
	private MobilePhoneManager manager = ModelFactory.getInstance().getPhoneManager();

	@Override
	public void add(AbstractMessage addMessage)
	{
		manager.add((MobilePhoneDetail)addMessage);
	}

	@Override
	public void list(List<AbstractMessage> listMessage)
	{
		for(AbstractMessage msg:listMessage)
		{
			MobilePhoneDetail detail = (MobilePhoneDetail)msg;
			//assert we do not have this phone
			if(manager.contains(detail))
				manager.update(detail);
			else
				manager.add(detail);
		}
	}

	@Override
	public void remove(AbstractMessage removeMessage)
	{
		manager.remove((MobilePhoneDetail)removeMessage);
	}

	@Override
	public void update(AbstractMessage updateMessage)
	{
		manager.update((MobilePhoneDetail)updateMessage);
	}
}
