package at.rc.tacos.client;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.*;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.model.Login;

/**
 * This class is used to control the status line, toolbar, title, 
 * window size, and other things can be customize.
 * @author Michael
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor 
{   
    /**
     * Creates the application workbench advisor.
     * @param configurer the configuring workbench information
     * @return the configuration information for a workbench window
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        super(configurer);
    }

    /**
     * Handles close events of the workbench window
     */
	@Override
	public boolean preWindowShellClose() 
	{
	    //confirm quit
	    boolean confirm = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
	            "Schlie�en best�tigen",
	            "Wollen Sie das Leitstellenprogramm wirklich beenden?");
	    if(!confirm)
	        return false;
		//check if we have a user info
		Login login = SessionManager.getInstance().getLoginInformation();
		if(login == null)
			return true;
		
		//try to close the connection
		NetWrapper.getDefault().requestNetworkStop(false);
		
		//Close the connection
		System.out.println("Have a nice day :)");

		return true;	
	}

	/**
     * Creates the action bar.
     * @param configurer the configuration action bar information
     * @return the configuration information for a action bar
     */
    @Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) 
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    /**
     * Called in the constructor of the workbench window
     */
    @Override
	public void preWindowOpen() 
    {        
        //get access to the configuration interface
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setTitle("Time and Coordination System");
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowProgressIndicator(true);   
        configurer.setShowPerspectiveBar(false);
    }

	/**
	 * Called after the window was created
	 */
	@Override
	public void postWindowCreate() 
	{
        //start the application maximized
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        IWorkbenchWindow window = configurer.getWindow();
        window.getShell().setMaximized(true);
	}
}