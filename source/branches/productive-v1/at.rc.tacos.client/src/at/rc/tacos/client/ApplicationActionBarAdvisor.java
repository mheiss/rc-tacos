/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ContributionItemFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import at.rc.tacos.client.controller.ConnectionWizardAction;
import at.rc.tacos.client.controller.OpenDialysisTransportAction;
import at.rc.tacos.client.controller.OpenEmergencyTransportAction;
import at.rc.tacos.client.controller.OpenTransportAction;
import at.rc.tacos.client.controller.PersonalNewEntryAction;
import at.rc.tacos.client.perspectives.SwitchToAdminPerspective;
import at.rc.tacos.client.perspectives.SwitchToClientPerspective;
import at.rc.tacos.client.perspectives.SwitchToLogPerspective;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	// Actions - allocated in the make actions method
	private IWorkbenchAction exitAction;
	private IWorkbenchAction aboutAction;
	private ConnectionWizardAction conWizard;
	private SwitchToClientPerspective switchToClient;
	private SwitchToAdminPerspective switchToAdmin;
	private SwitchToLogPerspective switchToLog;
	private IContributionItem viewList;
	private PersonalNewEntryAction personalNewEntryAction;
	private OpenEmergencyTransportAction openEmergencyTransportAction;
	private OpenTransportAction openTransportAction;
	private OpenDialysisTransportAction openDialysisTransportAction;

	/**
	 * Default class constructor.
	 * 
	 * @param configurer
	 *            the configuration Interface
	 */
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	/**
	 * Called to create the actions used in the fill methods.
	 */
	@Override
	protected void makeActions(final IWorkbenchWindow window) {
		// Creates the actions and registers them.
		exitAction = ActionFactory.QUIT.create(window);
		exitAction.setAccelerator(SWT.CTRL + 'I');
		aboutAction = ActionFactory.ABOUT.create(window);
		conWizard = new ConnectionWizardAction(window);
		switchToClient = new SwitchToClientPerspective();
		switchToClient.setAccelerator(SWT.CTRL + 'R');
		switchToAdmin = new SwitchToAdminPerspective();
		switchToLog = new SwitchToLogPerspective();
		viewList = ContributionItemFactory.VIEWS_SHORTLIST.create(window);
		personalNewEntryAction = new PersonalNewEntryAction();
		personalNewEntryAction.setAccelerator(SWT.CTRL + 'D');
		openEmergencyTransportAction = new OpenEmergencyTransportAction();
		openEmergencyTransportAction.setAccelerator(SWT.CTRL + 'O');
		openTransportAction = new OpenTransportAction();
		openTransportAction.setAccelerator(SWT.CTRL + 'T');
		openDialysisTransportAction = new OpenDialysisTransportAction();
		openDialysisTransportAction.setAccelerator(SWT.CTRL + 'L');
		register(aboutAction);
		register(exitAction);
		register(conWizard);
	}

	/**
	 * Called to fill the menu bar with the main menus for the window.
	 */
	@Override
	protected void fillMenuBar(IMenuManager menuBar) {
		// the file menu
		MenuManager fileMenu = new MenuManager("&Datei");
		fileMenu.add(exitAction);

		MenuManager newMenu = new MenuManager("&Anlegen");
		newMenu.add(personalNewEntryAction);
		newMenu.add(openEmergencyTransportAction);
		newMenu.add(openTransportAction);
		newMenu.add(openDialysisTransportAction);

		// the admin sub menu
		MenuManager adminMenu = new MenuManager("&Administation");
		adminMenu.add(switchToLog);
		adminMenu.add(switchToAdmin);

		// window menu
		MenuManager windowMenu = new MenuManager("&Fenster", "viewsShortlist");
		windowMenu.add(adminMenu);
		windowMenu.add(new Separator());
		windowMenu.add(switchToClient);
		windowMenu.add(viewList);

		// help menu
		MenuManager helpMenu = new MenuManager("&Hilfe");
		helpMenu.add(aboutAction);
		helpMenu.add(conWizard);

		// add the manager to the main menu
		menuBar.add(fileMenu);
		menuBar.add(newMenu);
		menuBar.add(windowMenu);
		menuBar.add(helpMenu);
	}

	/**
	 * Called to fill the cool bar with the main toolbars for the window.
	 */
	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {
		IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(toolbar, "main"));
	}
}
