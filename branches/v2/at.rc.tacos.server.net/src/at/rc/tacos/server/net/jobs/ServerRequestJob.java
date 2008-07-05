package at.rc.tacos.server.net.jobs;

import java.io.BufferedReader;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.common.Message;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Helo;
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
			//loop and wait for data to handle
			while(!monitor.isCanceled())
			{
				try
				{
					//wait for new data on the input stream
					BufferedReader in = socket.getBufferedInputStream();
					String newData = in.readLine();
					if(newData == null)
						throw new SocketException("Connection to the server lost.");
					
					//Decode the message
					XMLFactory factory = new XMLFactory();
					Message receivedMessage = factory.decode(newData);
					
					//check the received message from the server
					if(receivedMessage.getQueryString().equalsIgnoreCase("SERVER_DISCOVER"))
					{
						//send back the information about this server
						Message message = new Message();
						message.setUsername("SERVER");
						message.setContentType(Helo.ID);
						message.setQueryString("SERVER_DISCOVER_RESPONSE");
						message.addMessage(NetWrapper.getDefault().getServerInfo());
						
						//encode to string
						String responseXml = factory.encode(message);
						
						socket.getBufferedOutputStream().println(responseXml);
						socket.getBufferedOutputStream().flush();
						
						//we have a failback server found
						ServerManager.getInstance().failbackServerUpdate((Helo)receivedMessage.getMessageList().get(0));
					}
				}
				catch(SocketTimeoutException ste)
				{
					//timeout, just go on . ..
				}
			}
			//shutdown the server connection
			socket.cleanup();
			return Status.OK_STATUS;
		}
		catch(Exception e)
		{
			NetWrapper.log("Critical network error detected: "+e.getMessage(), Status.ERROR,e.getCause());
			NetWrapper.getDefault().serverFailure(socket);
			return Status.CANCEL_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}

}
