package at.rc.tacos.client;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor 
{
	private static final String PERSPECTIVE_ID = "at.rc.tacos.client.perspective";

	/**
     * Creates the application workbench advisor.
     */
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }
    
    /**
     * Return the initial perspective used for new workbench windows.
     * @return the idenitfication of the perspective
     */
	public String getInitialWindowPerspectiveId() 
	{
		return PERSPECTIVE_ID;
	}

    /**
     * Initializes the application.<br>
     * The loading of the needed data before the startup is done here
     */
    @Override
    public void initialize(IWorkbenchConfigurer configurer)
    {
        // TODO connect to the server here
        // TODO Load the model data here
        super.initialize(configurer);
    } 
}
