package at.rc.tacos.server.net.jobs;

import java.sql.SQLException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.AbstractMessageInfo;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.common.IServerListener;
import at.rc.tacos.factory.ServerListenerFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.DAOException;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.Logout;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.server.Activator;
import at.rc.tacos.server.model.OnlineUser;
import at.rc.tacos.server.modelManager.OnlineUserManager;
import at.rc.tacos.server.net.MySocket;

/**
 * This job is responsible for the process of the received data
 * @author Michael
 */
public class ProccessDataJob extends Job
{
	//properties
	private String xmlString;
	private MySocket socket;
	private boolean authenticated;

	/**
	 * Default class constructor for the data to process
	 * @param newData the received message
	 */
	public ProccessDataJob(MySocket socket,String xmlString)
	{
		super("ProccessDataJob");
		this.socket = socket;
		this.xmlString = xmlString;
		this.authenticated = false;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) 
	{
		//get the online user by the socket
		OnlineUser user = OnlineUserManager.getInstance().getUserBySocket(socket);
		if(user != null)
			authenticated = true;
		
		try
		{
			monitor.beginTask("Processing the received data stream",IProgressMonitor.UNKNOWN);
			//set up the factory and decode decode
			XMLFactory xmlFactory = new XMLFactory();
			xmlFactory.setupDecodeFactory(xmlString);
			List<AbstractMessage> objectList = xmlFactory.decode();
			if(objectList == null)
				throw new Exception("Failed to decode the data");

			//get the type of the item
			final String sequenceId = xmlFactory.getSequenceId();
			final String contentType = xmlFactory.getContentType();
			final String queryString = xmlFactory.getQueryString();
			final QueryFilter queryFilter = xmlFactory.getQueryFilter();

			final ServerListenerFactory factory = ServerListenerFactory.getInstance();
			final IServerListener listener = factory.getListener(contentType);

			//the client is not authenticated, no login request -> not accepted
			if(!authenticated &! Login.ID.equalsIgnoreCase(contentType))
			{
				Activator.getDefault().log("Client not authenticated, login first",IStatus.WARNING);
				SystemMessage system = new SystemMessage("Client not authenticated, login first",SystemMessage.TYPE_INFO);
				//setup the message info container
				AbstractMessageInfo info = new AbstractMessageInfo();
				info.setSequenceId(sequenceId);
				info.setContentType(SystemMessage.ID);
				info.setQueryString(IModelActions.SYSTEM);
				info.setMessage(system);
				//send back to the client
				SendJob sendJob = new SendJob(info,socket,false);
				sendJob.schedule();
				return Status.OK_STATUS;
			}

			//do we have a handler?
			if(listener == null)
			{
				Activator.getDefault().log("No listener found for the message type: "+contentType,Status.ERROR);
				//notify the sender
				SystemMessage system = new SystemMessage("No listener found for the message type: "+contentType,SystemMessage.TYPE_INFO);
				//setup the message info container
				AbstractMessageInfo info = new AbstractMessageInfo();
				info.setSequenceId(sequenceId);
				info.setContentType(SystemMessage.ID);
				info.setQueryString(IModelActions.SYSTEM);
				info.setMessage(system);
				//send back to the client
				SendJob sendJob = new SendJob(info,socket,false);
				sendJob.schedule();
				return Status.OK_STATUS;
			}

			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId(sequenceId);
			info.setContentType(contentType);
			info.setQueryString(queryString);

			//login request
			if(IModelActions.LOGIN.equalsIgnoreCase(queryString))
			{
				//get the result message
				Login loginResult = (Login)listener.handleLoginRequest(objectList.get(0));
				//add the client to the authenticated list
				if(loginResult.isLoggedIn())
				{
					user.setLogin(loginResult);
					Activator.getDefault().log("Login successful for user "+loginResult.getUsername(),Status.OK);
				}
				//send back
				info.setMessage(loginResult);
				//send back to the client
				SendJob sendJob = new SendJob(info,socket,false);
				sendJob.schedule();
				return Status.OK_STATUS;
			}
			//logout request
			if(IModelActions.LOGOUT.equalsIgnoreCase(queryString))
			{
				Logout logoutResult = (Logout)listener.handleLogoutRequest(objectList.get(0));
				//remove the client form the list of authenticated clients if successfully
				if(logoutResult.isLoggedOut())
				{
					Activator.getDefault().log("Logout from user: "+logoutResult.getUsername()+". Have a nice day :)",Status.OK);
					//send back to the client
					info.setMessage(logoutResult);
					//send back to the client
					SendJob sendJob = new SendJob(info,socket,false);
					sendJob.schedule();
					user.setLogin(null);
				}
				OnlineUserManager.getInstance().removeUser(user);
				return Status.OK_STATUS;
			}
			//add request
			if(IModelActions.ADD.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultAddMessage = listener.handleAddRequest(objectList.get(0), user.getLogin().getUsername());
				info.setMessage(resultAddMessage);
				//send back to the all clients
				SendJob sendJob = new SendJob(info,socket,true);
				sendJob.schedule();
				return Status.OK_STATUS;
			}
			//add all request
			if(IModelActions.ADD_ALL.equalsIgnoreCase(queryString))
			{
				List<AbstractMessage> resultAddList = listener.handleAddAllRequest(objectList);
				info.setMessageList(resultAddList);
				//send back to all client
				SendJob sendJob = new SendJob(info,socket,true);
				sendJob.schedule();
				return Status.OK_STATUS;
			}
			//remove request
			if(IModelActions.REMOVE.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultRemoveMessage = listener.handleRemoveRequest(objectList.get(0));
				info.setMessage(resultRemoveMessage);
				//send back to all client
				SendJob sendJob = new SendJob(info,socket,true);
				sendJob.schedule();
				return Status.OK_STATUS;
			}
			//update request
			if(IModelActions.UPDATE.equalsIgnoreCase(queryString))
			{
				AbstractMessage resultUpdateMessage = listener.handleUpdateRequest(objectList.get(0), user.getLogin().getUsername());
				info.setMessage(resultUpdateMessage);
				//send back to all client
				SendJob sendJob = new SendJob(info,socket,true);
				sendJob.schedule();
				return Status.OK_STATUS;
			}
			//listing request
			if(IModelActions.LIST.equalsIgnoreCase(queryString))
			{
				List<AbstractMessage> resultMessageList = listener.handleListingRequest(queryFilter);
				info.setMessageList(resultMessageList);
				//send back to the client
				SendJob sendJob = new SendJob(info,socket,false);
				sendJob.schedule();
				return Status.OK_STATUS;
			}
			return Status.OK_STATUS;
		}
		//catch all sql errors that occured during the operations with the listener classes
		catch(SQLException sqle)
		{
			Activator.getDefault().log("SQL-Error: "+sqle.getMessage(),Status.ERROR);
			SystemMessage system = new SystemMessage("SQL-Error:"+sqle.getMessage(),SystemMessage.TYPE_ERROR);
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId("SERVER");
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.SYSTEM);
			info.setMessage(system);
			//send back to the client
			SendJob sendJob = new SendJob(info,socket,false);
			sendJob.schedule();
			return Status.OK_STATUS;
		}
		//catch all error during the operations with the dao listener classes
		catch(DAOException daoe)
		{
			Activator.getDefault().log("DAO-Exception: "+daoe.getMessage(),Status.ERROR);
			SystemMessage system = new SystemMessage(daoe.getMessage(),SystemMessage.TYPE_ERROR);
			//setup the message info container
			AbstractMessageInfo info = new AbstractMessageInfo();
			info.setSequenceId("SERVER");
			info.setContentType(SystemMessage.ID);
			info.setQueryString(IModelActions.SYSTEM);
			info.setMessage(system);
			//send back to the client
			SendJob sendJob = new SendJob(info,socket,false);
			sendJob.schedule();
			return Status.OK_STATUS; 
		}
		catch(Exception e)
		{
			//log the error
			Activator.getDefault().log("Cannot decode the message send by the client: "+user.getLogin().getUsername(),Status.ERROR);
			Activator.getDefault().log("The request will be aborted:"+e.getMessage(),Status.ERROR);

			//create a new fatal error message
			SystemMessage system = new SystemMessage("Ausnahmefehler: Die zuletzt gesendete Anforderung kann nicht bearbeitet werden",SystemMessage.TYPE_ERROR);
			//setup the message
			AbstractMessageInfo error = new AbstractMessageInfo();
			error.setSequenceId("ERROR");
			error.setContentType(SystemMessage.ID);
			error.setQueryString(IModelActions.SYSTEM);
			error.setMessage(system);
			//send back to the client
			SendJob sendJob = new SendJob(error,socket,false);
			sendJob.schedule();
			return Status.OK_STATUS;
		}
		finally
		{
			monitor.done();
		}
	}
}
