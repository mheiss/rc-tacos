package at.rc.tacos.client;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.*;
import org.eclipse.ui.application.*;
import org.eclipse.ui.actions.ActionFactory.*;

import at.rc.tacos.client.controller.ConnectionWizardAction;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor 
{
	// Actions - allocated in the maka actions method
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private ConnectionWizardAction conWizard;

	/**
	 * Default class constructor.
	 * @param configurer the configuration Interface
	 */
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) 
	{
		super(configurer);
	}

	/**
	 * Called to create the actions used in the fill methods.
	 */
	protected void makeActions(final IWorkbenchWindow window) 
	{
		// Creates the actions and registers them.
		exitAction = ActionFactory.QUIT.create(window);
		aboutAction = ActionFactory.ABOUT.create(window);
		conWizard = new ConnectionWizardAction(window);

		register(aboutAction);
		register(exitAction);
		register(conWizard);
	}

	/**
	 *  Called to fill the menu bar with the main menus for the window.
	 */
	protected void fillMenuBar(IMenuManager menuBar) 
	{
		//the file menu
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		fileMenu.add(exitAction);

		//help menu
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
		helpMenu.add(aboutAction);

		//Server connection 
		MenuManager conMenu = new MenuManager("&Verbindung", "Verbindung");
		conMenu.add(conWizard);

		//add the manager to the main menu
		menuBar.add(fileMenu);
		menuBar.add(helpMenu);
		menuBar.add(conMenu);
	}

	/**
	 *  Called to fill the cool bar with the main toolbars for the window.
	 */
	protected void fillCoolBar(ICoolBarManager coolBar) 
	{
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));  
		toolbar.add(exitAction);
		toolbar.add(aboutAction);
	}

	/**
	 * Called to fill the status line with the main status line contributions for the window
	 */
	protected void fillStatusLine(IStatusLineManager statusLine)
	{
		super.fillStatusLine(statusLine);
	}
}
