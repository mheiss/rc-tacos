package at.rc.tacos.server.net.controller;

import java.net.SocketException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import at.rc.tacos.common.Message;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Helo;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.ServerManager;
import at.rc.tacos.server.net.handler.SendHandler;
import at.rc.tacos.server.net.jobs.ServerRequestJob;

public class DisoverController
{	
	//the properties
	private IProgressMonitor monitor;
	
	/**
	 * Default class constuctor passing the server to check
	 */
	public DisoverController(IProgressMonitor monitor) 
	{ 
		this.monitor = monitor;
	}
	
	/**
	 *  Tries to open a connection to the server
	 */
	public MySocket discoverServer(String host,int port)
	{	
		try
		{
			monitor.beginTask("Versuche eine Verbindung mit "+host+":"+port+" aufzubauen", 3);
			
			//setup the discover message with this server
			Message message = new Message();
			message.setUsername("SERVER");
			message.setContentType(Helo.ID);
			message.setQueryString("SERVER_DISCOVER");
			message.addMessage(NetWrapper.getDefault().getServerInfo());
			
			//create a new socket
			MySocket socket = new MySocket(host,port);
			socket.setSoTimeout(2000);
			if(!socket.isConnected())
				return null;
			
			//start the loop
			for(int i=0;i<3;i++)
			{
				monitor.subTask("Sende HELO Anfrage #"+i);
				try
				{
					//set the sequence id
					message.setSequenceId("SEQUENCE_"+i);
					
					//send the message
					SendHandler sendHandler = new SendHandler(message);
					sendHandler.sendMessage(socket);
					
					//wait for the answer
					String response = socket.getBufferedInputStream().readLine();
					if(response == null)
						throw new SocketException("Failed to read the data from the socket");
					
					//decode the response
					XMLFactory factory = new XMLFactory();
					Message responseMessage = factory.decode(response);
					if(!responseMessage.getQueryString().equalsIgnoreCase("SERVER_DISCOVER_RESPONSE"))
						throw new IllegalArgumentException("The returned content type is not matching");
					
					//update the view
					ServerManager.getInstance().primaryServerUpdate((Helo)responseMessage.getMessageList().get(0));
										
					//ok now we have successfully opened a connection to the server
					return socket;
				}
				catch(Exception e)
				{
					NetWrapper.log("Failed to send the discovery message "+message+ " to the server "+host+":"+port +"("+e.getMessage()+")", IStatus.ERROR, e.getCause());
				}
				//next try
				monitor.worked(1);
			}
			return null;
		}
		catch(Exception e)
		{
			NetWrapper.log("Unexpected error occured while trying to discover a server:"+e.getMessage(), IStatus.ERROR, e.getCause());
			//failed to discover the server
			return null;
		}
	}
	
	/**
	 * Called to do the additional task
	 */
	public void disoverComplete(MySocket socket,Helo serverInfo)
	{
		if(socket != null)
		{
			NetWrapper.log("Found primary server "+ServerManager.getInstance().getPrimaryServer(),IStatus.INFO,null);
			//Start the thread to listen to data from the server
			ServerRequestJob serverRequestJob = new ServerRequestJob(socket);
			serverRequestJob.schedule();
			//this server will be the failback
			ServerManager.getInstance().failbackServerUpdate(serverInfo);
		}
		else
		{
			//this server will be the primary server
			ServerManager.getInstance().primaryServerUpdate(serverInfo);			
			NetWrapper.log("No other running server found, this server will be the primary",IStatus.INFO,null);
		}
	}
}
