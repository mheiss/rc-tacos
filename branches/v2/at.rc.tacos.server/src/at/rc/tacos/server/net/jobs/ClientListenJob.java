package at.rc.tacos.server.net.jobs;

import java.io.BufferedReader;
import java.net.SocketTimeoutException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.Activator;
import at.rc.tacos.server.net.MySocket;

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
		super("ClientListenJob");
		this.socket = socket;
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
			Activator.getDefault().log("Critical error while listening to new data: "+e.getMessage(), Status.ERROR);
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
