package at.rc.tacos.server.net.handler;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.Message;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.server.net.ServerContext;

/**
 * The error handler is respnsible for handling all errors that occur during the interaction with the client
 * @author Michael
 */
public class ErrorHandler 
{
	//properties of the handler
	private AbstractMessage exception;
	
	/**
	 * Default class Constructor passing the error message
	 */
	public ErrorHandler(AbstractMessage exception)
	{
		this.exception = exception;
	}
	
	/**
	 * Handles all exception during the interaction with the data objects or the message encoding decoding
	 */
	public void handleError()
	{
		//create a new message to send back to the client
		Message message = new Message();
		message.setSequenceId("ERROR");
		message.setUsername(ServerContext.getCurrentInstance().getSession().getUsername());
		message.setQueryString(IModelActions.SYSTEM);
		message.setContentType(SystemMessage.ID);
		//add the error message
		message.addMessage(exception);
		//add to the message queue
		ServerContext.getCurrentInstance().addMessage(message);
	}
}
