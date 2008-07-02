package at.rc.tacos.server.net.internal.jobs;

import java.net.SocketTimeoutException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.net.MyServerSocket;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;

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
		super(TSJ.SERVER_LISTEN_JOB);
		this.serverSocket = serverSocket;
	}

	@Override
	public boolean belongsTo(Object family) 
	{
		return TSJ.SERVER_LISTEN_JOB.equals(family); 
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		//create the server socket and start listening
		try
		{
			monitor.beginTask("Warte auf Clientverbindung", IProgressMonitor.UNKNOWN);	
			
			//inform the controller about the startup of the server
			NetWrapper.getDefault().startServer();
			
			//loop and manage the client connections
			while(!monitor.isCanceled())
			{		
				try
				{
					//wait for the new socket
					MySocket newSocket = serverSocket.accept();
					newSocket.setSoTimeout(2000);				
					
					//start the listen job
					ClientListenJob listenJob = new ClientListenJob(newSocket);
					listenJob.setUser(true);
					listenJob.schedule();
				}
				catch(SocketTimeoutException ste)
				{
					//timeout just go on
					continue;
				}
			}
			//do additional tasks
			NetWrapper.getDefault().stopServer();
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			//log the error
			NetWrapper.log("IO-Error during the network listening:"+e.getMessage(), IStatus.ERROR,e.getCause());
			//do additional tasks
			NetWrapper.getDefault().stopServer();
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
