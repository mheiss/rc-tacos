package at.rc.tacos.server.net.handler;

import java.io.PrintWriter;
import java.util.ListIterator;

import at.rc.tacos.common.Message;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Session;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.ServerContext;
import at.rc.tacos.server.net.SessionManager;

public class SendHandler 
{
	//the properties
	private Message message;
	private XMLFactory factory = new XMLFactory();
	
	/**
	 * Default class constructor for the send handler
	 */
	public SendHandler(Message message)
	{
		this.message = message;
	}
	
	/**
	 * Brodcasts the message to all clients
	 */
	public void brodcastMessage() throws Exception
	{
		//encode the message to xml
		String xmlMessage = factory.encode(message);
		
		//get all currently connected sockets
		ListIterator<Session> sessionIter = SessionManager.getInstance().getClientSessions().listIterator();
		while(sessionIter.hasNext())
		{
			Session nextSession = sessionIter.next();
			//do not brodcast messages if the session is not authenicated
			if(!nextSession.isAuthenticated())
				continue;
			
			//check for a valid session
			if(ServerContext.getCurrentInstance() != null)
			{
				//do not brodcast messages to web clients
				if(nextSession.getLogin().isWebClient() &! ServerContext.getCurrentInstance().equals(nextSession))
					continue;
			}
				
			//send the message to the client
			PrintWriter writer = nextSession.getSocket().getBufferedOutputStream();
			writer.println(xmlMessage);
			writer.flush();
		}
	}
	
	/**
	 * Sends the message to the destination socket
	 */
	public void sendMessage(MySocket socket) throws Exception
	{
		//encode the message to xml
		String xmlMessage = factory.encode(message);
		PrintWriter writer = socket.getBufferedOutputStream();
		writer.println(xmlMessage);
		writer.flush();
	}
}
