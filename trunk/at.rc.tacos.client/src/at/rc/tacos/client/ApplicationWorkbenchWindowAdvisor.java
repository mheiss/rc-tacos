package at.rc.tacos.client;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.*;

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
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setTitle("Time and Coordination System");
        configurer.setShowCoolBar(false);    //ToolBar
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(true);   
    }
}
