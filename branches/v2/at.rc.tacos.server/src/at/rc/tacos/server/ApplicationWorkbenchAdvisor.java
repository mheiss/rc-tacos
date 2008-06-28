package at.rc.tacos.server;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "at.rc.tacos.server.ui.perspective.perspective";

	/**
     * Creates the application workbench advisor.
     * @param configurer the configuring workbench information
     * @return the configuration information for a workbench window
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
     * Initializes the application.
     * The loading of the needed data before the startup is done here
     */
    @Override
    public void initialize(IWorkbenchConfigurer configurer)
    {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);
    } 
}
