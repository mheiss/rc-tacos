package at.rc.tacos.client.wizard;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.core.net.internal.MyClient;
import at.rc.tacos.core.net.internal.ServerInfo;

public class ConnectionWizard extends Wizard implements INewWizard, PropertyChangeListener
{
	//the pages
	private ConnectionInfoPage infoPage;
	private ConnectionServerPage serverPage;
	private ConnectionLoginPage loginPage;

	// the workbench instance
	protected IWorkbench workbench;

	//the properties
	private ServerInfo selectedServer;
	private String username;
	private String password;

	//authentication status
	private boolean fAuthenticated;
	private int loginStatus;

	/**
	 * Default class constructor for a new wizzard
	 */
	public ConnectionWizard() 
	{
		super();
		setNeedsProgressMonitor(true);
		SessionManager.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * Cleanup and exit
	 */
	@Override
	public void dispose()
	{
		SessionManager.getInstance().removePropertyChangeListener(this);
	}

	/**
	 * Initializes the wizard.
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection seöection) 
	{
		//just save the workbench
		this.workbench = workbench;
	}

	/**
	 * Callback method to add the needed pages
	 */
	@Override
	public void addPages() 
	{
		//create the pages
		infoPage = new ConnectionInfoPage();
		serverPage = new ConnectionServerPage(this);
		loginPage = new ConnectionLoginPage(this);
		//add the pages
		addPage(infoPage);
		addPage(serverPage);
		addPage(loginPage);		
	}

	/**
	 * Returns wheter or not the wizard can be finished.
	 * @return true if the wizard can be finished
	 */
	@Override
	public boolean canFinish() 
	{
		//the wizzard can be finished at the first page if we have a connection
		if(getContainer().getCurrentPage() == infoPage && !NetWrapper.getDefault().isConnected())
			return true;
		//the wizard can be finished if we have a valid login
		if(username != null && password != null)
			return true;
		//no connection
		return false;
	}

	@Override
	public boolean performFinish() 
	{
		//connect to the new server and login
		NetWrapper.getDefault().connectNetwork(selectedServer.getId());
		//do we have a connection
		if(NetWrapper.getDefault().getClientSession() == null)
		{
			System.out.println("Failed to connect to the server");
			//show the message to the user
			MessageDialog.openError(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
					"Verbindungsfehler",
			"Verbindung zum Server ist nicht möglich");
			return false;
		}
		//if we have a connection try to login
		if(NetWrapper.getDefault().getClientSession().getConnection() != null)
		{
			//get the connection
			MyClient connection = NetWrapper.getDefault().getClientSession().getConnection();
			try
			{
				getContainer().run(true, false, new IRunnableWithProgress() 
				{
					public void run(IProgressMonitor monitor)
					{
						monitor.beginTask("Sende Anmeldeinformationen zum Server", IProgressMonitor.UNKNOWN);
						while (!fAuthenticated) 
						{
							if (!PlatformUI.getWorkbench().getDisplay().readAndDispatch()) 
							{
								//check the status
								if( loginStatus == 1 )
									loginSuccessfully();
								else if(loginStatus == 2 ) 
									loginFailed();  
								//sleep
								PlatformUI.getWorkbench().getDisplay().sleep();
							}
						}
						monitor.done();
					}
				});
			}
			catch (InvocationTargetException ite) 
			{
				System.out.println("Failed to run the login thread");
				return false;
			}
			catch (InterruptedException e) 
			{
				System.out.println("Login thread interrupted");
				return false;
			}
		} 
		//everything is ok
		return true;
	}

	/**
	 * Sets the selected server to use for the new connection
	 */
	public void setNewServer(ServerInfo newServer)
	{
		this.selectedServer = newServer;
	}

	/**
	 * Sets the username and password to login to the server.
	 * @param username the username to authenticate
	 * @param password the password for the user
	 */
	public void setLoginData(String username,String password)
	{
		this.username = username;
		this.password = password;

	}

	/**
	 * Returns the selected server from the server page
	 * @return the server to connect to
	 */
	public ServerInfo getNewServer()
	{
		return selectedServer;
	}

	/**
	 * Fired uppon a successfull login
	 */
	private void loginSuccessfully()
	{
		fAuthenticated = true;
		loginStatus = -1;
	}

	/**
	 * Fired uppon a failed login
	 */
	private void loginFailed()
	{
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				"Anmeldung fehlgeschlagen",
		"Bitte überprüfen Sie den angegebenen Benutzernamen und das Passwort");
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		//connection to the server was successfully
		if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(evt.getPropertyName()))
		{
			loginStatus = 1;
		}
		if("AUTHENTICATION_FAILED".equalsIgnoreCase(evt.getPropertyName()))
		{
			loginStatus = 2;
		}
	}
}

