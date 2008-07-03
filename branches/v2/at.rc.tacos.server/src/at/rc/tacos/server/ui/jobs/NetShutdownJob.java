package at.rc.tacos.server.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.server.net.NetWrapper;

/**
 * Wrapps the shtudown process into a thread to provide monitoring
 * @author Michael
 *
 */
public class NetShutdownJob extends Job
{
	/**
	 * Default class constructor
	 */
	public NetShutdownJob()
	{
		super("NetShutdownJob");
	}
	
	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			monitor.beginTask("Netzwerkverbindung beenden", IProgressMonitor.UNKNOWN);
			
			//Shutdown the network
			NetWrapper.getDefault().shutdownServer(monitor);
			
			//check if the connection is closed
			if(NetWrapper.getDefault().isListening())
				return Status.CANCEL_STATUS;
		
			return Status.OK_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}

}
