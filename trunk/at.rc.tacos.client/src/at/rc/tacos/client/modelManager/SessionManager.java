package at.rc.tacos.client.modelManager;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.controller.ConnectionWizardAction;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.net.NetWrapper;

/**
 * Handles the authentication process and manages the 
 * @author Michael
 */
public class SessionManager extends PropertyManager
{
	//the unique id
	public final static String ID = "sessionManager";
	
	//the instance
	private static SessionManager instance;

	/**
	 * Default class constructor
	 */
	private SessionManager() { }

	/**
	 * Returns the shared instance
	 */
	public static SessionManager getInstance()
	{
		if( instance == null)
			instance = new SessionManager();
		return instance;
	}

	/**
	 * Fired when the login of the user was successfully.
	 * @param username the name of the authenticated user
	 */
	public void fireLoginSuccessfully(final String username)
	{
		//set the username in the client session
		NetWrapper.getDefault().getClientSession().setAuthenticated(username, false);
		firePropertyChange("AUTHENTICATION_SUCCESS", null, username);
	}

	/**
	 * Fired when the login was denied.
	 * @param username the name of the user who tried to authenticate
	 * @param message the replied message from the server
	 */
	public void fireLoginDenied(final String username,final String message)
	{
		firePropertyChange("AUTHENTICATION_FAILED", null, username);
	}

	/**
	 * Fired when a logout event occures.
	 */
	public void fireLogout()
	{
		NetWrapper.getDefault().getClientSession().setDeAuthenticated();
	}
	
	/**
	 * Fired when the connection to the server is lost.
	 */
	public void fireConnectionLost()
	{
        Display.getDefault().syncExec(new Runnable ()    
        {
            public void run ()       
            {
				//bring up the wizard to reconnect
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				ConnectionWizardAction connectionWizard = new ConnectionWizardAction(window);
				connectionWizard.run();
            	System.out.println("connection lost");
            }
        });
	}
	
	public void fireTransferFailed(String contentType,String queryType,AbstractMessage message)
	{
		//the message to display
		StringBuffer msg = new StringBuffer();
		msg.append("Die folgende Anforderung konnte nicht an den Server übertragen werden.\n");
		msg.append(contentType+" -> "+queryType);
		
		//show the message to the user
		MessageDialog.openError(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
				"Netzwekfehler",
				msg.toString());
		//retry
		boolean retryConfirmed = MessageDialog.openConfirm(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				"Senden wiederholen",
				"Wollen sie die Nachricht noch einmal senden?");
		if (!retryConfirmed) 
			return;
		//send the request
		if(IModelActions.ADD.equalsIgnoreCase(queryType))
			NetWrapper.getDefault().sendAddMessage(contentType, message);
		if(IModelActions.REMOVE.equalsIgnoreCase(queryType))
			NetWrapper.getDefault().sendRemoveMessage(contentType, message);
		if(IModelActions.UPDATE.equalsIgnoreCase(queryType))
			NetWrapper.getDefault().sendUpdateMessage(contentType, message);
		if(IModelActions.LIST.equalsIgnoreCase(queryType))
			NetWrapper.getDefault().requestListing(contentType, null);
	}
}
