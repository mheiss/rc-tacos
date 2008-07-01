package at.rc.tacos.server.net.internal.jobs;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Calendar;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.model.OnlineUser;
import at.rc.tacos.net.MyServerSocket;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.OnlineUserManager;

/**
 * This job is responsible for the server so that the clients can connect
 * @author Michael
 */
public class ServerListenJob extends Job
{
	//properties of the server listen job
	private MyServerSocket serverSocket;
	
	/**
	 * Default class constructor 
	 */
	public ServerListenJob(MyServerSocket serverSocket)
	{
		super("ServerListenJob");
		this.serverSocket = serverSocket;
	}
	
	@Override
	public boolean belongsTo(Object family) 
	{
		return NetWrapper.SERVER_LISTEN_JOB.equals(family); 
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		//create the server socket and start listening
		try
		{
			monitor.beginTask("Warte auf Clientverbindung", IProgressMonitor.UNKNOWN);			
			while(!monitor.isCanceled())
			{
				MySocket newSocket = null;				
				try
				{
					 newSocket = serverSocket.accept();
					 newSocket.setSoTimeout(2000);
				}
				catch(SocketTimeoutException ste)
				{
					//timeout so the listen job can be canceled
				}
				
				//assert valid
				if(newSocket == null)
					continue;
				
				//create a new user object
				OnlineUser onlineUser = new OnlineUser(newSocket);
				onlineUser.setOnlineSince(Calendar.getInstance().getTimeInMillis());
				
				//add the user to the managed list
				OnlineUserManager.getInstance().addUser(onlineUser);
				
				//startup the listen job 
				ClientListenJob clientListenJob = new ClientListenJob(newSocket);
				clientListenJob.schedule();
			}
			return Status.OK_STATUS;
		}
		catch(IOException ioe)
		{
			//log the error and go on
			NetWrapper.log("IO-Error during the network listening", IStatus.ERROR,ioe.getCause());
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
