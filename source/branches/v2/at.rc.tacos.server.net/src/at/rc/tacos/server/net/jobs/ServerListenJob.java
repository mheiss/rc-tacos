package at.rc.tacos.server.net.jobs;

import java.net.SocketTimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.net.MyServerSocket;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;

/**
 * <p><strong>ServerListenJob</strong> waits for and accepts connections from other servers</p>
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
		try
		{
			monitor.beginTask("Initialisiere ServerSocket, Port: "+serverSocket.getLocalPort(),IProgressMonitor.UNKNOWN);
			//loop and wait for client connections
			while(!monitor.isCanceled())
			{		
				try
				{
					//wait for the new socket
					MySocket newSocket = serverSocket.accept();
					newSocket.setSoTimeout(2000);	
					
					//forke a new thread to handle the connection
					ServerRequestJob serverRequest = new ServerRequestJob(newSocket);
					serverRequest.schedule();
				}
				catch(SocketTimeoutException ste)
				{
					//timeout just go on
					continue;
				}
			}
			NetWrapper.log("Server listen job canceled, shuting down ...", IStatus.INFO,null);
			NetWrapper.getDefault().serverDestroyed();
			//everything went ok
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			NetWrapper.log("IO-Error during the listening for new servers: "+e.getMessage(), IStatus.ERROR,e.getCause());
			NetWrapper.getDefault().serverDestroyed();
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
