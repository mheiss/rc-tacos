package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import at.rc.tacos.client.wizard.ConnectionWizard;

/**
 * This action brings up the connection wizard to reconnect and 
 * login to a server
 * @author Michael
 */
public class ConnectionWizardAction extends Action 
{
	//the id
	public final static String ID = "at.rc.tacos.client.controller.connectionWizard";
	
	private IWorkbenchWindow window;

	
	/**
	 * Default class constructor creating the wizzard
	 */
	public ConnectionWizardAction(IWorkbenchWindow window) 
	{
		this.window = window;
	}
	
	/**
	 * Retruns the text to show in the toolbar
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Server Verbindung";
	}

	/**
	 * Callback method to run the wizzard
	 */
	public void run() 
	{
		//Create the wizzard and open the window
		ConnectionWizard wizard = new ConnectionWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.create();
		dialog.open();
	}

	/**
	 * Returns the identification of the action
	 * @return the id
	 */
	@Override
	public String getId() 
	{
		return ID;
	}
}