package at.rc.tacos.server.net;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.Message;
import at.rc.tacos.model.Session;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.net.MySocket;
import at.rc.tacos.server.net.handler.ErrorHandler;
import at.rc.tacos.server.net.handler.MessageHandler;
import at.rc.tacos.server.net.handler.SendHandler;

/**
 * <p><strong>ServerContext</strong> 
 * contains all of the per-connection state information related to the
 * processing of Server requests.
 *
 * <p>A {@link ServerContext} instance is associated with a particular
 * connection at the creation of the connection. </p>
 * @author Michael
 */
public class ServerContext 
{
	// properties of the context
	private Session session;

	// the list of messages to send
	private List<Message> response; 

	/**
	 * Default class constructor
	 */
	protected ServerContext()
	{
		response = new ArrayList<Message>();
	}

	//STATIC METHODS
	/**
	 * <p>The <code>ThreadLocal</code> variable used to record the
	 * {@link ServerContext} instance for each processing thread.</p>
	 */
	private static ThreadLocal<ServerContext> instance = new ThreadLocal<ServerContext>() {
		protected ServerContext initialValue() { return null; }
	};

	/**
	 * <p>Returns the {@link ServerContext} for the request that is beeing proccessed by the current thread.</p>
	 */
	public static ServerContext getCurrentInstance()
	{
		return instance.get();
	}

	/**
	 * <p>Set the {@link ServerContext} instance for the request that is
	 * being processed by the current thread.</p>
	 *
	 * @param context The {@link FacesContext} instance for the current
	 * thread, or <code>null</code> if this thread no longer has a
	 * <code>ServerContext</code> instance.
	 *
	 */
	protected static void setCurrentInstance(ServerContext context) 
	{
		if (context == null) 
			instance.remove();
		else
			instance.set(context);
	}

	//COMMON METHODS
	/**
	 * Adds a new {@link Message} to the list of response list
	 * @param newMessage the message to add
	 */
	public void addMessage(Message newMessage)
	{
		response.add(newMessage);
	}

	//LIFECYCLE METHODS OF THE CLIENT REQUEST	
	/**
	 * Releases any resources accociated with the {@link ServerContext} and closes the socket connection.
	 */
	public void release()
	{
		try
		{
			//inform the session manager about the disconnect
			SessionManager.getInstance().removeSession(session);

			//get the socket and close it
			MySocket socket = session.getSocket();
			socket.cleanup();
		}
		catch(Exception e)
		{
			NetWrapper.log("Failed to close the socket for the client: "+session.getUsername(), IStatus.ERROR, e.getCause());
			e.printStackTrace();
		}
		finally
		{
			//remove this instance
			instance.remove();
		}	
	}

	/**
	 * <p> Handles the new request from the client connection </p>
	 * @param xmlData the received data from the socket
	 */
	public void handleRequest(String xmlData)
	{
		try
		{
			//setup the handler to proccess the message
			MessageHandler handler = new MessageHandler(xmlData);
			handler.handleRequest();
		}
		//catch all sql errors that occured during the operations with the listener classes
		catch(SQLException sqle)
		{
			NetWrapper.log("SQL-Error: "+sqle.getMessage(),Status.ERROR,sqle.getCause());
			SystemMessage system = new SystemMessage("SQL-Error:"+sqle.getMessage(),SystemMessage.TYPE_ERROR);
			//process the message
			ErrorHandler handler = new ErrorHandler(system);
			handler.handleError();
		}
		//catch all error during the operations with the dao listener classes
		catch(DAOException daoe)
		{
			NetWrapper.log("DAO-Exception: "+daoe.getMessage(),Status.ERROR,daoe.getCause());
			SystemMessage system = new SystemMessage(daoe.getMessage(),SystemMessage.TYPE_ERROR);
			//process the message
			ErrorHandler handler = new ErrorHandler(system);
			handler.handleError();
		}
		//catch all other errors
		catch(Exception e)
		{
			NetWrapper.log("Cannot proccess the message send by the client: "+session.getUsername(),Status.ERROR,e.getCause());
			//create a new fatal error message
			SystemMessage system = new SystemMessage("Ausnahmefehler: Die zuletzt gesendete Anforderung kann nicht bearbeitet werden",SystemMessage.TYPE_ERROR);
			//process the message
			ErrorHandler handler = new ErrorHandler(system);
			handler.handleError();
		}

		//now send the message back to the client
		Iterator<Message> responseIter = response.listIterator();
		while(responseIter.hasNext())
		{
			//the next message to send
			Message nextMessage = responseIter.next();
			String queryString = nextMessage.getQueryString();
			try
			{
				SendHandler handler = new SendHandler(nextMessage);

				//send the message back to the source if the content type is system
				if(SystemMessage.ID.equalsIgnoreCase(nextMessage.getContentType())
						|| IModelActions.LOGIN.equalsIgnoreCase(queryString)
						|| IModelActions.LOGOUT.equalsIgnoreCase(queryString)
						|| IModelActions.LIST.equalsIgnoreCase(queryString)) {
					handler.sendMessage(ServerContext.getCurrentInstance().getSession().getSocket());
					//go to the next message
					continue;
				}

				//brodcast the following types of messages to all connected clients
				if(IModelActions.ADD.equalsIgnoreCase(queryString)
						|| IModelActions.ADD_ALL.equalsIgnoreCase(queryString)
						|| IModelActions.UPDATE.equalsIgnoreCase(queryString)
						|| IModelActions.REMOVE.equalsIgnoreCase(queryString)) {
					handler.brodcastMessage();
				}
			}
			catch(Exception e)
			{
				NetWrapper.log("Failed to send the message "+nextMessage +" to the client: "+e.getMessage(), IStatus.ERROR, e.getCause());
			}
			finally
			{
				//remove the package from the client list
				responseIter.remove();
			}
		}
	}

	//GETTERS AND SETTERS
	/**
	 * Sets the session object for this context
	 */
	public void setSession(Session session)
	{
		this.session = session;
	}

	/**
	 * Returns the current session object accociated with the context
	 */
	public Session getSession()
	{
		return session;
	}

	/**
	 * Returns the list of messages that must be send to the client
	 */
	public List<Message> getResponse()
	{
		return response;
	}
}
