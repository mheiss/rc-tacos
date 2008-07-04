package at.rc.tacos.server.net.controller;

import java.io.PrintWriter;
import java.net.SocketException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import at.rc.tacos.common.Message;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Helo;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.ServerManager;

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
			//setup the message and the factory
			Message message = new Message();
			message.setUsername("SERVER");
			message.setContentType(Helo.ID);
			message.setQueryString("SERVER_DISCOVER");
			
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
					
					//encode the message to a string
					XMLFactory factory = new XMLFactory();
					String xmlMessage = factory.encode(message);
					
					PrintWriter writer = socket.getBufferedOutputStream();
					writer.println(xmlMessage);
					writer.flush();
					
					//wait for the answer
					String response = socket.getBufferedInputStream().readLine();
					if(response == null)
						throw new SocketException("Failed to read the data from the socket");
					
					//decode the response
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
}
