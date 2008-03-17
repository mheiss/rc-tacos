package at.rc.tacos.client;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import at.rc.tacos.client.modelManager.ModelFactory;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor 
{
	private static final String PERSPECTIVE_ID = "at.rc.tacos.client.perspectives.client";

	/**
     * Creates the application workbench advisor.
     * @param configurer the configuring workbench information
     * @return the configuration information for a workbench window
     */
    @Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }
    
    /**
     * Return the initial perspective used for new workbench windows.
     * @return the idenitfication of the perspective
     */
	@Override
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
        super.initialize(configurer);
        //load the model
        ModelFactory.getInstance().initalizeModel();  
    } 
}
