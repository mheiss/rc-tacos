package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class LogPerspective implements IPerspectiveFactory 
{
	public static final String ID = "at.rc.tacos.client.perspectives.log";
	
	 /**
     * Set up the layout of the workbench
     * @param layout the page layout to use
     */
    public void createInitialLayout(IPageLayout layout) 
    {
    	String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        layout.addStandaloneView("org.eclipse.pde.runtime.LogView", false,IPageLayout.TOP, 1f, editorArea);
    }
}
