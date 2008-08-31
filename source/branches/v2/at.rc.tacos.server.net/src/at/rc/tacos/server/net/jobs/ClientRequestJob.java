package at.rc.tacos.server.net.jobs;

import java.io.BufferedReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.model.Session;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.ServerContext;
import at.rc.tacos.server.net.ServerContextFactory;

/**
 * This job is responsible for the interaction with the connected clients.
 * @author Michael
 */
public class ClientRequestJob extends Job
{
	//the connected socket
	private MySocket socket;

	/**
	 * Default class constructor
	 */
	public ClientRequestJob(MySocket socket)
	{
		super(TSJ.CLIENT_REQUEST_JOB);
		this.socket = socket;
	}

	@Override
	public boolean belongsTo(Object family) 
	{
		return TSJ.CLIENT_REQUEST_JOB.equals(family); 
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		try
		{
			//create a new server context and pass the socket
			ServerContext context = ServerContextFactory.getInstance().getServerContext();
			context.setSession(new Session(socket));
			
			//inform the net controller about the new session
			NetWrapper.getDefault().sessionCreated(socket);
			
			//start the listen job
			monitor.beginTask("Listening to new data on the network", IProgressMonitor.UNKNOWN);
			while(!monitor.isCanceled())
			{	
				try
				{
					//wait for new data on the input stream
					BufferedReader in = socket.getBufferedInputStream();
					String newData = in.readLine();
					if(newData == null)
						throw new SocketException("Connection to the client "+ServerContext.getCurrentInstance().getSession().getLogin() +" lost.");
					
					//pass the data to the new handler
					ServerContext.getCurrentInstance().handleRequest(newData);
				}
				catch(SocketTimeoutException timeout)
				{
					//timeout, just go on . ..
				}
			}
			//inform about the session end
			NetWrapper.getDefault().sessionDestroyed(socket);
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			//log the error
			NetWrapper.log("Critical error while listening to new data: "+e.getMessage(), Status.ERROR,e.getCause());	
			
			//destroy the session
			NetWrapper.getDefault().sessionDestroyed(socket);
	
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
