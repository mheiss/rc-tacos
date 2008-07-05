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
 * <p><strong>ServerJob</strong> waits for and accepts new client connections.</p>
 * @author Michael
 */
public class ClientListenJob extends Job
{
	//properties of the server listen job
	private MyServerSocket serverSocket;

	/**
	 * Default class constructor 
	 */
	public ClientListenJob(MyServerSocket serverSocket)
	{
		super(TSJ.CLIENT_LISTEN_JOB);
		this.serverSocket = serverSocket;
	}

	@Override
	public boolean belongsTo(Object family) 
	{
		return TSJ.CLIENT_LISTEN_JOB.equals(family); 
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			monitor.beginTask("Warte auf Clientverbindung", IProgressMonitor.UNKNOWN);	
			
			//loop and wait for client connections
			while(!monitor.isCanceled())
			{		
				try
				{
					//wait for the new socket
					MySocket newSocket = serverSocket.accept();
					newSocket.setSoTimeout(2000);				
					
					//start the listen job
					ClientRequestJob listenJob = new ClientRequestJob(newSocket);
					listenJob.setUser(true);
					listenJob.schedule();
				}
				catch(SocketTimeoutException ste)
				{
					//timeout just go on
					continue;
				}
			}
			//shutdown the socket
			NetWrapper.log("Client listen job cancled, shuting down ...", IStatus.INFO,null);
			NetWrapper.getDefault().serverDestroyed();
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			NetWrapper.log("IO-Error during listening for new clients:"+e.getMessage(), IStatus.ERROR,e.getCause());
			NetWrapper.getDefault().serverDestroyed();
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
