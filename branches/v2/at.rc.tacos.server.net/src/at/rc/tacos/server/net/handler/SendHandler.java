package at.rc.tacos.server.net.handler;

import java.io.PrintWriter;

import at.rc.tacos.net.MySocket;

/**
 * Sends the message to the destination socket
 * @author Michael
 */
public class SendHandler 
{
	//the properties of the handler
	private MySocket socket;
	private String message;
	
	/**
	 * Default class constructor providing the message and the socket to send
	 */
	public SendHandler(MySocket socket, String message)
	{
		this.socket = socket;
		this.message = message;
	}
	
	/**
	 * Sends the message
	 */
	public void sendMessage() throws Exception
	{
		//get the output stream to send
		PrintWriter writer = socket.getBufferedOutputStream();
		writer.println(message);
		writer.flush();
	}
}
