package at.rc.tacos.server.net.internal.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.net.MySocket;

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
		// TODO Auto-generated method stub
		return null;
	}

}
