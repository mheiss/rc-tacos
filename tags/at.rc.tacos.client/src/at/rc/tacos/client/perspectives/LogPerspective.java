package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.NavigationView;

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
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.TOP, 0.10f, editorArea);
        layout.addStandaloneView("org.eclipse.pde.runtime.LogView", false,IPageLayout.BOTTOM, 0.8f, editorArea);
    }
}
