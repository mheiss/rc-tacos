package at.rc.tacos.server.net.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.net.NetWrapper;

/**
 * Wraps the network connection into a thread to provide monitoring
 * @author Michael
 */
public class NetConnectionJob extends Job
{
	/**
	 * Default class constructor
	 */
	public NetConnectionJob()
	{
		super("NetConnectionJob");
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			//create and init the job
			monitor.beginTask("Netzwerk wird initialisiert", IProgressMonitor.UNKNOWN);
			
			//TODO: check database
			//TODO: query the 'active' servers
			//TODO: try to open a connection to the server: SEND HELO MESSAGE
			
			//try to create the server socket
			NetWrapper.getDefault().initServerSocket(monitor);
			
			//check the connection
			if(!NetWrapper.getDefault().isListening())
				return Status.CANCEL_STATUS;
			
			return Status.OK_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
