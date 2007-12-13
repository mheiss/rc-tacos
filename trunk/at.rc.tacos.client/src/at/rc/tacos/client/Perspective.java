package at.rc.tacos.client;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import at.rc.tacos.client.view.*;

/**
 * This class defines the initial layout for the views.
 * @author Michael
 */
public class Perspective implements IPerspectiveFactory 
{
    /**
     * Set up the layout of the workbench
     * @param layout the page layout to use
     */
	public void createInitialLayout(IPageLayout layout) 
	{
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(false);
        //the main components
        layout.addStandaloneView(VehiclesView.ID,false, IPageLayout.LEFT, 0.45f, editorArea);
        layout.addStandaloneView(PersonalView.ID,false, IPageLayout.RIGHT, 0.45f, editorArea);
        layout.addStandaloneView(InfoView.ID, false, IPageLayout.TOP, 0.35f, PersonalView.ID);
	}
}
