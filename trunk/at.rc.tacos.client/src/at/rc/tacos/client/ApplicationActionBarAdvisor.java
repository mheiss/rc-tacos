package at.rc.tacos.client;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.*;
import org.eclipse.ui.application.*;
import org.eclipse.ui.actions.ActionFactory.*;

import at.rc.tacos.client.controller.ConnectionWizardAction;
import at.rc.tacos.client.perspectives.SwitchToAdminPerspective;
import at.rc.tacos.client.perspectives.SwitchToClientPerspective;
import at.rc.tacos.client.perspectives.SwitchToLogPerspective;


/**
 * An action bar advisor is responsible for creating, adding, and disposing of the
 * actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor 
{
	// Actions - allocated in the make actions method
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private ConnectionWizardAction conWizard;
	private SwitchToClientPerspective switchToClient;
	private SwitchToAdminPerspective switchToAdmin;
	private SwitchToLogPerspective switchToLog;
	private IContributionItem viewList;

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
	@Override
	protected void makeActions(final IWorkbenchWindow window) 
	{
		// Creates the actions and registers them.
		exitAction = ActionFactory.QUIT.create(window);
		aboutAction = ActionFactory.ABOUT.create(window);
		conWizard = new ConnectionWizardAction(window);
		switchToClient = new SwitchToClientPerspective();
		switchToAdmin = new SwitchToAdminPerspective();
		switchToLog = new SwitchToLogPerspective();
		viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		register(aboutAction);
		register(exitAction);
		register(conWizard);
	}

	/**
	 *  Called to fill the menu bar with the main menus for the window.
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) 
	{
		//the file menu
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		fileMenu.add(exitAction);
		
		//the admin sub menu
		MenuManager adminMenu = new MenuManager("&Administation");
		adminMenu.add(switchToLog);
		adminMenu.add(switchToAdmin);
		
		//window menu
		MenuManager windowMenu = new MenuManager("&Window");
		windowMenu.add(adminMenu);
		windowMenu.add(new Separator());
		windowMenu.add(switchToClient);
		 adminMenu.add(viewList);

		//help menu
		MenuManager helpMenu = new MenuManager("&Help");
		helpMenu.add(aboutAction);
		helpMenu.add(conWizard);

		//add the manager to the main menu
		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);
		
	}

	/**
	 *  Called to fill the cool bar with the main toolbars for the window.
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) 
	{
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));  
	}
}
