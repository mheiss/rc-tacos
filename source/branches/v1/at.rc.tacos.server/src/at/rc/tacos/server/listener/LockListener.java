package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Lock;
import at.rc.tacos.model.QueryFilter;

public class LockListener extends ServerListenerAdapter
{
	//the list of locked object
	private final static List<Lock> lockedList = Collections.synchronizedList(new ArrayList<Lock>());
	
	//the logger
	private static Logger logger = Logger.getLogger(LockListener.class);

	/**
	 * The request to add a new lock to the managed list of locks
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject,String username) throws DAOException, SQLException 
	{
		Lock lock = (Lock)addObject;
		logger.debug("New lock request: "+lock);
		//check if the list already contains the lock 
		if(lockedList.contains(lock))
		{
			int index = lockedList.indexOf(lock);
			logger.debug("The lock is existing, editing is denied");
			//set the username and return the message
			Lock existingLock = lockedList.get(index);
			lock.setLockedBy(existingLock.getLockedBy());
		}
		else
		{
			logger.debug("The lock is not in the list of objects, editing is granted");
			//set the lock for this user
			lock.setHasLock(true);
			synchronized (lockedList) 
			{
				lockedList.listIterator().add(lock);
			}
			return lock;
		}
		logger.debug("The lock was not allowed: "+lock);
		//do not allow the user to have the lock
		lock.setHasLock(false);
		return lock;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
		//create a new list of abstract messages
		List<AbstractMessage> abstractLockList = new ArrayList<AbstractMessage>();
		synchronized (lockedList) 
		{
			ListIterator<Lock> iter = lockedList.listIterator();
			while(iter.hasNext())
				abstractLockList.add(iter.next());
		}
		
		//return the list of current locks
		return abstractLockList;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException 
	{
		Lock lock = (Lock)removeObject;
		logger.debug("Request to remove the lock:"+lock);
		//remove the lock from the list
		synchronized(lockedList) 
		{
			ListIterator<Lock> iter = lockedList.listIterator();
			while(iter.hasNext())
			{
				Lock listLock = iter.next();
				//check the object and remove it from the list
				if(listLock.equals(lock))
				{
					logger.debug("Removing lock:"+lock);
					iter.remove();
					lock.setHasLock(false);
					return lock;
				}
			}
		}
		//the lock has not been removed
		lock.setHasLock(true);
		return lock;
	}
	
	/**
	 * Returns the list of managed lock objects
	 */
	public List<Lock> getLockObjects()
	{
		return lockedList;
	}
}
