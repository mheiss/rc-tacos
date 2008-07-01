package at.rc.tacos.server.net.internal.jobs;

import java.io.BufferedReader;
import java.net.SocketTimeoutException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;

/**
 * This job is responsible for the interaction with the connected clients.
 * @author Michael
 */
public class ClientListenJob extends Job
{
	//the connected socket
	private MySocket socket;
	
	/**
	 * Default class constructor
	 */
	public ClientListenJob(MySocket socket)
	{
		super(NetWrapper.CLIENT_LISTEN_JOB);
		this.socket = socket;
	}
	
	@Override
	public boolean belongsTo(Object family) 
	{
		return NetWrapper.CLIENT_LISTEN_JOB.equals(family); 
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			monitor.beginTask("Listening to new data on the network", IProgressMonitor.UNKNOWN);
					
			//wait for new data on the input stream
			BufferedReader in = socket.getBufferedInputStream();
			String newData = in.readLine();
			
			//assert valid
			if(newData == null)
				return Status.OK_STATUS;
			
			//start the job to proccess the data
			ProccessDataJob processDataJob = new ProccessDataJob(socket,newData);
			processDataJob.schedule();
			
			return Status.OK_STATUS;
		}
		catch(SocketTimeoutException timeout)
		{
			//timeout, just go on . ..
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			NetWrapper.log("Critical error while listening to new data: "+e.getMessage(), Status.ERROR,e.getCause());
			return Status.CANCEL_STATUS;
		}
		finally
		{
			//startover the job when it is not cancled
			if(!monitor.isCanceled())
				schedule();
			monitor.done();
		}
	}
}
