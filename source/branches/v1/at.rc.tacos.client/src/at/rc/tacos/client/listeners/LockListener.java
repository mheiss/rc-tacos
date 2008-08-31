package at.rc.tacos.client.listeners;

import java.util.List;

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Lock;

public class LockListener extends ClientListenerAdapter
{
	//the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	@Override
	public void add(AbstractMessage addMessage) 
	{
		//cast to a lock object and pass to the manager
		Lock lock = (Lock)addMessage;
		lockManager.addLock(lock);
	}

	@Override
	public void remove(AbstractMessage removeMessage) 
	{
		//cast to a lock object and pass to the manager
		Lock lock = (Lock)removeMessage;
		lockManager.removeLock(lock);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) 
	{
		//loop and add the lock object
		for(AbstractMessage abstractLock:listMessage)
		{
			Lock lock = (Lock)abstractLock;
			if(lockManager.containsObject(lock))
				lockManager.updateLock(lock);
			else
				lockManager.addLock(lock);
		}
	}
}
