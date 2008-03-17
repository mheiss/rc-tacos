package at.rc.tacos.client;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.core.net.internal.IServerInfo;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication 
{
    /**
     * Start the application and create the main workbench
     * @param context the context used to init the application
     * @return the exit code of the application
     */
    public Object start(IApplicationContext context) 
    {
        Display display = PlatformUI.createDisplay();
        //connect to the server
        NetWrapper.getDefault().connectNetwork(IServerInfo.PRIMARY_SERVER);
        //get the network status
        System.out.println("Connection status:"+NetWrapper.getDefault().isConnected());
        if(!NetWrapper.getDefault().isConnected())
        {
            display.beep();
            //show an error message
            if(!MessageDialog.openQuestion(display.getActiveShell(), 
                    "Verbindungsfehler", 
                    "Verbindung zum primären Server nicht möglich.\n" +
                    "Soll eine Verbindung zum Backup Server hergestellt werden?"))
                return IApplication.EXIT_OK;
            else
            {
                //connect to the server
                NetWrapper.getDefault().connectNetwork(IServerInfo.FAILOVER_SERVER);
                System.out.println("Connection status:"+NetWrapper.getDefault().isConnected());
                //get the network status
                if(!NetWrapper.getDefault().isConnected())
                {
                    display.beep();
                    //show an error message
                    MessageDialog.openError(display.getActiveShell(), 
                            "Verbindungsfehler", 
                            "Verbindung zum primären und zum Backup Server nicht möglich.\n" +
                            "Applikation wird beendet");
                    return IApplication.EXIT_OK;
                }
            }
        }
        //try to load workbench
        try 
        {
            ApplicationWorkbenchAdvisor adv = new ApplicationWorkbenchAdvisor();
            //create the workbench
            int returnCode = PlatformUI.createAndRunWorkbench(display,adv );
            if (returnCode == PlatformUI.RETURN_RESTART) 
                return IApplication.EXIT_RESTART;
            return IApplication.EXIT_OK;
        } 
        finally 
        {
            display.dispose();
        }
    }

    /**
     * Stops the application and close the workbench
     */
    public void stop() 
    {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        if (workbench == null)
            return;
        final Display display = workbench.getDisplay();
        display.syncExec(new Runnable() 
        {
            public void run() {
                if (!display.isDisposed())
                    workbench.close();
            }
        });
    }
}
