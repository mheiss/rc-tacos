package at.rc.tacos.server;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - important to allocate these only in makeActions
	private IWorkbenchAction aboutAction;
	private IWorkbenchAction exitAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) 
	{
		super(configurer);
	}

	/**
	 * Creates the actions and registers them. Registering is needed to ensure that key bindings work.
	 * The corresponding commands keybindings are defined in the plugin.xml file.
	 *  Registering also provides automatic disposal of the actions when the window is closed
	 */
	protected void makeActions(final IWorkbenchWindow window) 
	{
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);
	}

	/**
	 * Fills the menu bar with the provided action items
	 */
	protected void fillMenuBar(IMenuManager menuBar) 
	{
		MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		// Add a group marker indicating where action set menus will appear.
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(helpMenu);

		// File
		fileMenu.add(exitAction);

		//Help
		helpMenu.add(aboutAction);
	}
}
