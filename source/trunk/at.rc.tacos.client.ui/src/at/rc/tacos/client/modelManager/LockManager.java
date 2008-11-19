package at.rc.tacos.client.modelManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.progress.UIJob;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.Lock;

/**
 * The lock manager manages the state of all lock object on the client
 * @author Michael
 */
public class LockManager extends PropertyManager 
{
	//the list of lock object
	private List<Lock> lockList = new ArrayList<Lock>();

	//the lock status to wait for
	private static int lockStatus;
	private static Lock lastSendLock;

	//the lock stats vars
	private final static int LOCK_WAIT = 0;
	private final static int LOCK_OK = 1;
	private final static int LOCK_DENIED = 2;
	private final static int LOCK_ERROR = 3;

	/**
	 * Default class constructor
	 */
	public LockManager() { }

	/**
	 * Adds a new lock object to the managed list of locks
	 * @param newLock the lock object to add
	 */
	public void addLock(final Lock newLock)
	{
		//chek if this lock belongs to us
		if(newLock.equals(lastSendLock))
		{
			//update the lock object
			lastSendLock = newLock;
			
			//check the status
			if(newLock.isHasLock())
				lockStatus = LOCK_OK;
			else
				lockStatus = LOCK_DENIED;
		}
		
		//check if the lock is allowed
		if(!newLock.isHasLock())
			return;

		//process the lock
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				lockList.add(newLock);
				firePropertyChange("LOCK_ADD", null, newLock);
			}
		});
	}
	
	/**
	 * Updates a existing lock entry 
	 * @param updateLock the updated lock entry
	 */
	public void updateLock(final Lock updateLock)
	{
		//check and update the lock
		if(lockList.contains(updateLock))
		{
			int index = lockList.indexOf(updateLock);
			lockList.set(index, updateLock);
		}
		
		//inform the listeners
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				firePropertyChange("LOCK_ADD", null, updateLock);
			}
		});
	}

	/**
	 * Removes the lock object from the managed list of locks
	 * @param oldLock the old lock object to remove
	 */
	public void removeLock(final Lock oldLock)
	{
		Display.getDefault().syncExec(new Runnable ()    
		{
			public void run ()       
			{
				lockList.remove(oldLock);
				firePropertyChange("LOCK_REMOVE", oldLock, null);
			}
		});
	}
	
	/**
	 * Returns wheter or not the object is managed by the lock manager
	 * @param lock the lock object to test
	 * @return true if the object is already in the list of managed objects
	 */
	public boolean containsObject(Lock lock)
	{
		return lockList.contains(lock);
	}

	/**
	 * Sends the lock object to the server and waits for the response
	 */
	public static String sendLock(String contentId,String lockedId)
	{
		//get the authenticated user
		String user = SessionManager.getInstance().getLoginInformation().getUsername();

		//create a new lock object
		final Lock lock = new Lock(contentId,user,lockedId,false);

		//the start time of the lock request
		final Calendar startTime = Calendar.getInstance();

		//create a new thread to wait for the lock
		Job lockJob = new Job("Überprüfe den Status des Objektes") 
		{
			public IStatus run(IProgressMonitor monitor) 
			{
				try
				{
					//Send the request
					monitor.setTaskName("Sende Anfrage an den Server");
					NetWrapper.getDefault().sendAddMessage(Lock.ID, lock);
					lastSendLock = lock;
					lockStatus = LOCK_WAIT;
					// wait for the response
					while(lockStatus == LOCK_WAIT)
					{
						if(monitor.isCanceled())
							return Status.CANCEL_STATUS;
						monitor.setTaskName("Warte auf Antwort des Servers");
						Thread.sleep(10);

						//the response from the server must be within 2 seconds
						Calendar now = Calendar.getInstance();
						now.add(Calendar.SECOND,-2);
						if(now.after(startTime))
							return Status.CANCEL_STATUS;
					}
					return Status.OK_STATUS;
				}
				catch(Exception e)
				{
					System.out.println("Failed to wait for the server response for the lock object");
					return Status.CANCEL_STATUS;
				}
				finally
				{
					monitor.done();
				}
			}
		};
		lockJob.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void done(IJobChangeEvent event) 
			{
				//check the return code
				if(event.getResult() == Status.CANCEL_STATUS)
				{
					lockStatus = LOCK_ERROR;
				}
			}	
		});
		lockJob.setPriority(UIJob.SHORT);
		lockJob.setUser(true);
		lockJob.schedule();

		//wait until the job has finished
		try
		{
			lockJob.join();
		}
		catch(InterruptedException ie)
		{
			lockStatus = LOCK_ERROR;
		}

		//check the result of the job
		if(lockStatus == LOCK_OK)
		{
			return null;
		}
		else if(lockStatus == LOCK_DENIED)
		{
			return lastSendLock.getLockedBy();
		}
		else
		{
			//show a warning to the user
			MessageDialog.openError(
					Display.getCurrent().getActiveShell(), 
					"Sperren des Objektes fehlgeschlagen", 
					"Der Status des Eintrages kann nicht überprüft werden\nEin editieren wird nicht empfohlen");
			//return a std message
			return "<undefiniert>";	
		}
	}

	/**
	 * Sends the request to the server to remove the lock from the object
	 */
	public static void removeLock(String contentId,String lockedId)
	{
		//get the authenticated user
		String user = SessionManager.getInstance().getLoginInformation().getUsername();

		//create a new lock object
		final Lock lock = new Lock(contentId,user,lockedId,false);

		//send the request to the server
		NetWrapper.getDefault().sendRemoveMessage(Lock.ID, lock);
	}
	
	/**
	 * Returns whether or not a lock is existing for the specified content and id
	 * @param contentId the content id to check for lock
	 * @param objectId the id of the object to check
	 * @return true if the object is in the list of the locked object
	 */
	public boolean containsLock(String contentId,String objectId)
	{
		//loop and check
		for(Lock lock:lockList)
		{
			if(lock.getContentId().equalsIgnoreCase(contentId) && lock.getLockedId().equalsIgnoreCase(objectId))
				return true;
		}
		//nothing matched 
		return false;
	}
	
	//WRAPPER CLASSES TO USE INTEGER VALUES FOR THE OBJECT ID
	/**
	 * Sends the request to the server to remove the lock from the object
	 */
	public static void removeLock(String contentId,int lockedId)
	{
		removeLock(contentId,String.valueOf(lockedId));
	}
	
	/**
	 * Sends the lock object to the server and waits for the response
	 */
	public static String sendLock(String contentId,int lockedId)
	{
		return sendLock(contentId, String.valueOf(lockedId));
	}
	
	/**
	 * Returns whether or not a lock is existing fpr the specified content and id string
	 * @param contentId the content id to check for lock
	 * @param objectString the string value to identify a entry
	 * @return true if the object is in the list of the locked object
	 */
	public boolean containsLock(String contentId,int objectId)
	{
		return containsLock(contentId, String.valueOf(objectId));
	}
}
