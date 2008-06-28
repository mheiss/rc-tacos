package at.rc.tacos.server.net.jobs;

import java.io.IOException;
import java.util.Calendar;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.Activator;
import at.rc.tacos.server.model.OnlineUser;
import at.rc.tacos.server.modelManager.OnlineUserManager;
import at.rc.tacos.server.net.MyServerSocket;
import at.rc.tacos.server.net.MySocket;

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
	public ServerListenJob()
	{
		super("ServerListenJob");
		setName("Warte auf Clientverbindungen");
		setSystem(true);
		setPriority(Job.DECORATE);
	}
	
	/**
	 * Initialize the server socket
	 * @throws IOException when the socket cannot be established
	 */
	public void init() throws IOException
	{
		serverSocket = new MyServerSocket(4711);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		//create the server socket and start listening
		try
		{
			//assert valid
			if(serverSocket == null)
				throw new IllegalStateException("The server socket is not initialized");
			
			//accept the new server socket
			MySocket newSocket = serverSocket.accept();
			
			//create a new user object
			OnlineUser onlineUser = new OnlineUser(newSocket);
			onlineUser.setOnlineSince(Calendar.getInstance().getTimeInMillis());
			
			//add the user to the managed list
			OnlineUserManager.getInstance().addUser(onlineUser);
			
			//startup the listen job 
			ClientListenJob clientListenJob = new ClientListenJob(newSocket);
			clientListenJob.schedule();
			
			return Status.OK_STATUS;
		}
		catch(IOException ioe)
		{
			//log the error and go on
			Activator.getDefault().log("IO-Error during the network listening", IStatus.ERROR);
			return Status.OK_STATUS;
		}
		finally
		{
			//restart the listen job again if it is not canceled
			if(!monitor.isCanceled())
				schedule();
			monitor.done();
		}
	}
}
