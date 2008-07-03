package at.rc.tacos.server.net.jobs;

import java.io.BufferedReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.ServerManager;

/**
 * <p><strong>ServerRequestJob</strong> handles all requests from other servers
 * @author Michael
 */
public class ServerRequestJob extends Job
{
	//properties
	private MySocket socket;
	
	/**
	 * Default class constructor
	 */
	public ServerRequestJob(MySocket socket)
	{
		super(TSJ.SERVER_REQUEST_JOB);
		this.socket = socket;
	}
	
	@Override
	public boolean belongsTo(Object family) 
	{
		return TSJ.SERVER_REQUEST_JOB.equals(family);
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			while(!monitor.isCanceled())
			{
				try
				{
					//wait for new data on the input stream
					BufferedReader in = socket.getBufferedInputStream();
					String newData = in.readLine();
					if(newData == null)
						throw new SocketException("Connection to the server lost.");
				}
				catch(SocketTimeoutException ste)
				{
					//timeout, just go on . ..
				}
				
			}
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			//log the error
			NetWrapper.log("Critical error while listening to data from the server: "+e.getMessage(), Status.ERROR,e.getCause());
			ServerManager.getInstance().failbackServerUpdate(null);
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}

}
