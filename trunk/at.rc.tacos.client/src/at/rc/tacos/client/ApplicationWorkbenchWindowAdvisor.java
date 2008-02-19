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
     * Creates the application workbench advisor.
     * @param configurer the configuring workbench information
     * @return the configuration information for a workbench window
     */
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        super(configurer);
    }

    /**
     * Creates the action bar.
     * @param configurer the configuration action bar information
     * @return the configuration information for a action bar
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
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(true);
        configurer.setShowProgressIndicator(false);   
        configurer.setShowPerspectiveBar(false); 
    }
}
