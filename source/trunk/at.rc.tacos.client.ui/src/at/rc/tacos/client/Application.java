package at.rc.tacos.client;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

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
    	//startup the workbench
        Display display = PlatformUI.createDisplay();
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