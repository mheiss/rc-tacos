package at.rc.tacos.server.net.jobs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import at.rc.tacos.common.AbstractMessageInfo;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.server.Activator;
import at.rc.tacos.server.model.OnlineUser;
import at.rc.tacos.server.modelManager.OnlineUserManager;
import at.rc.tacos.server.net.MySocket;

/**
 * This job is responsible for sending and brodcasting messages to the clients
 * @author Michael
 */
public class SendJob extends Job
{
	//the message info to send
	private AbstractMessageInfo messageInfo;
	private MySocket socket;
	private boolean brodcast;
	
	/**
	 * Default class constructor to setup a new send job.
	 */
	public SendJob(AbstractMessageInfo messageInfo,MySocket socket,boolean brodcast)
	{
		super("SendJob");
		//store the needed params
		this.messageInfo = messageInfo;
		this.socket = socket;
		this.brodcast = brodcast;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{		
		try
		{
			//the connected user
			String userId = "unknown";
			OnlineUser onlineUser = OnlineUserManager.getInstance().getUserBySocket(socket);
			if(onlineUser != null && onlineUser.getLogin() != null)
				userId = onlineUser.getLogin().getUsername();
			
			monitor.beginTask("Sending the message :"+messageInfo.getContentType() + " to the client "+userId, IProgressMonitor.UNKNOWN);
			XMLFactory factory = new XMLFactory();
			factory.setUserId(userId);
			factory.setTimestamp(messageInfo.getTimestamp());
			factory.setContentType(messageInfo.getContentType());
			factory.setQueryString(messageInfo.getQueryString());
			factory.setSequenceId(messageInfo.getSequenceId());
			factory.setFilter(messageInfo.getQueryFilter());
			String message = factory.encode(messageInfo.getMessageList());
			
			//brodcast to all clients?
			if(!brodcast)
			{
				sendMessage(socket, message);
				return Status.OK_STATUS;
			}
			
			List<OnlineUser> onlineUsers = OnlineUserManager.getInstance().getOnlineUsers();
			
			//loop over the client pool and send the message
	        synchronized(onlineUsers) 
	        {
	            ListIterator<OnlineUser> listIter = onlineUsers.listIterator();
	        	while(listIter.hasNext())
	        	{
	        		OnlineUser nextUser = listIter.next();
	                //send the message to all authenticated clients, except the web clients
	                if(nextUser.getLogin() != null &! nextUser.getLogin().isWebClient())
	                {
	                    sendMessage(nextUser.getSocket(), message);
	                }
	            }
			}
	        return Status.OK_STATUS;
		}
		catch(IOException ioe)
		{
			Activator.getDefault().log("Failed to send the message to the client:"+ioe.getMessage(), Status.ERROR);
		}
		finally
		{
			monitor.done();
		}
		return Status.OK_STATUS;
	}
	
	/**
	 * Helper method to send the message to the client
	 */
	private void sendMessage(MySocket socket,String message) throws IOException
	{
		PrintWriter writer = socket.getBufferedOutputStream();
		writer.println(message);
		writer.flush();
	}
}
