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

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.core.net.NetSource;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.core.net.internal.ServerInfo;
import at.rc.tacos.model.Login;

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
	private boolean loginResponse;

	/**
	 * Default class constructor for a new wizzard
	 */
	public ConnectionWizard() 
	{
		super();
		setNeedsProgressMonitor(true);
		SessionManager.getInstance().addPropertyChangeListener(this);
		//not authenticated
		loginResponse = false;
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
	public void init(IWorkbench workbench, IStructuredSelection selection) 
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
		if(getContainer().getCurrentPage() == infoPage && NetSource.getInstance().getConnection() != null)
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
		//skip and exit, if we are authenticated
		if(NetWrapper.getDefault().isAuthenticated())
			return true;
		//reset the login status
		loginResponse = false;
		//start a thread
		try
		{
			getContainer().run(true, true, new IRunnableWithProgress() 
			{
				public void run(IProgressMonitor monitor)
				{
					monitor.beginTask("Verbindung zum Server " + selectedServer.getDescription()+" wird hergestellt", IProgressMonitor.UNKNOWN);
					//connect to the new server and login
					NetSource.getInstance().openConnection(selectedServer);
					monitor.done();
				}
			});

			//if we have a connection try to login
			if(NetSource.getInstance().getConnection() == null)
			{
				Display.getCurrent().beep();
				loginPage.setErrorMessage("Verbindung zum Server kann nicht hergestellt werden");
				MessageDialog.openError(
						PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
						"Serverfehler",
						"Verbindung zum Server kann nicht hergestellt werden.\n" +
				"Bitte versuchen sie es erneut oder wählen einen anderen Server.");
				//exit, we have no connection
				return false;
			}
			//start the monitor jobs and try to login
			NetWrapper.getDefault().init();
			NetWrapper.getDefault().sendLoginMessage(new Login(username,password,false));
			getContainer().run(true, true, new IRunnableWithProgress() 
			{
				public void run(IProgressMonitor monitor) throws InterruptedException
				{
					monitor.beginTask("Sende Anmeldeinformationen zum Server", IProgressMonitor.UNKNOWN);
					//sleep for some time, until we got the response from the server
					while (!loginResponse) 
						Thread.sleep(100);
					monitor.done();
				}
			});
		}
		catch (InvocationTargetException ite) 
		{
			System.out.println("Failed to connect and login to the server");
			ite.printStackTrace();
			return false;
		}
		catch (InterruptedException e) 
		{
			System.out.println("Thread interrupted");
			return false;
		}
		//check the login status
		if(!NetWrapper.getDefault().isAuthenticated())
		{
			loginPage.setErrorMessage("Anmeldung fehlgeschlagen.\n" +
			"Bitte überprüfen Sie den angegebenen Benutzernamen und das Passwort");
			Display.getCurrent().beep();
			MessageDialog.openError(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(),
					"Anmeldung fehlgeschlagen",
					"Bitte überprüfen Sie den angegebenen Benutzernamen und das Passwort");
			return false;
		}
		else
		{
			Display.getCurrent().beep();
			MessageDialog.openInformation(
					PlatformUI.getWorkbench().getDisplay().getActiveShell(), 
					"Login Erfolgreich",
					"Sie haben erfolgreich eine Verbindung zum Server hergestellt");
			//request data from server
			ModelFactory.getInstance().initalizeModel();
			return true;
		}
	} 

	@Override
	public boolean performCancel() 
	{
	    //Close the wizard if a connection is established
	    if(NetSource.getInstance().getConnection() != null)
	        return true;
		Display.getCurrent().beep();
		MessageDialog.openError(
				PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				"Assistent abbrechen",
				"Dieser Assistent kann nicht abgebrochen werden.");
		return false;
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

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		//connection to the server was successfully
		if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(evt.getPropertyName()))
			loginResponse = true;
		if("AUTHENTICATION_FAILED".equalsIgnoreCase(evt.getPropertyName()))
			loginResponse = true;
	}
}

