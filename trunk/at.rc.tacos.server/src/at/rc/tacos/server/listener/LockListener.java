package at.rc.tacos.server.listener;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Lock;
import at.rc.tacos.model.QueryFilter;

public class LockListener extends ServerListenerAdapter
{
	//the list of locked object
	private final static List<Lock> lockedList = Collections.synchronizedList(new ArrayList<Lock>());

	/**
	 * The request to add a new lock to the managed list of locks
	 */
	@Override
	public AbstractMessage handleAddRequest(AbstractMessage addObject,String username) throws DAOException, SQLException 
	{
		Lock lock = (Lock)addObject;
		System.out.println("New lock request");
		//check if the list already contains the lock 
		if(lockedList.contains(addObject))
		{
			int index = lockedList.indexOf(lock);
			System.out.println("The lock is existing, editing is denied");
			//check if the user is the same 
			Lock existingLock = lockedList.get(index);
			lock.setLockedBy(existingLock.getLockedBy());
		}
		else
		{
			System.out.println("The lock is not in the list of objects");
			//set the lock for this user
			lock.setHasLock(true);
			lockedList.listIterator().add(lock);
			return lock;
		}
		System.out.println("The lock was not allowed: "+lock);
		//do not allow the user to have the lock
		lock.setHasLock(false);
		return lock;
	}

	@Override
	public List<AbstractMessage> handleListingRequest(QueryFilter queryFilter) throws DAOException, SQLException 
	{
		//create a new list of abstract messages
		List<AbstractMessage> abstractLockList = new ArrayList<AbstractMessage>();
		ListIterator<Lock> iter = lockedList.listIterator();
		while(iter.hasNext())
			abstractLockList.add(iter.next());
		//return the list of current locks
		return abstractLockList;
	}

	@Override
	public AbstractMessage handleRemoveRequest(AbstractMessage removeObject) throws DAOException, SQLException 
	{
		Lock lock = (Lock)removeObject;
		System.out.println("Request to remove the lock:"+lock);
		//remove the lock from the list
		ListIterator<Lock> iter = lockedList.listIterator();
		while(iter.hasNext())
		{
			Lock listLock = iter.next();
			//check the object and remove it from the list
			if(listLock.equals(lock))
			{
				System.out.println("Removing lock:"+lock);
				iter.remove();
				lock.setHasLock(false);
				return lock;
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
