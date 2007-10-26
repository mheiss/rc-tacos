package at.rc.tacos.client;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.ui.*;
import org.eclipse.ui.actions.*;
import org.eclipse.ui.application.*;
import org.eclipse.ui.actions.ActionFactory.*;

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
        // Registering is needed to ensure that key bindings work.
        exitAction = ActionFactory.QUIT.create(window);
        register(exitAction);

        aboutAction = ActionFactory.ABOUT.create(window);
        register(aboutAction);
    }
    
    /**
     *  Called to fill the menu bar with the main menus for the window.
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
       
        // Help
        helpMenu.add(aboutAction);
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
