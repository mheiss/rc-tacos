package at.rc.tacos.client;

//rcp
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
//net
import at.rc.tacos.core.net.*;
import at.rc.tacos.core.net.internal.MyClient;

/**
 * This class is used to control the status line, toolbar, title, 
 * window size, and other things can be customize.
 * @author Michael
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor 
{
    /**
     * Default class constructor
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        super(configurer);
    }

    /**
     * Creates the action bar.
     */
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) 
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    /**
     * Called in the constructor of the workbench window
     */
    public void preWindowOpen() 
    {
        //get access to the configuration interface
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(800, 600));
        configurer.setShowCoolBar(true);    //ToolBar
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);
        
        //check the network connection
        MyClient client = NetSource.getInstance().getConnection();
        
        //the primary server
        if(client == null)
        {
            //nothing is ok, no connection 
            MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                    "Error: No network connection", 
                    "Not connection to a server, application my be limited useable");
        }
        else if (!NetSource.getInstance().isPrimaryServerConnected())
        {
            //show a warning, but go on
            MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
                    "Warning: Using failback server", 
                    "The failback server is in use, make sure to get the primary server back up.");
        }
    }
}
