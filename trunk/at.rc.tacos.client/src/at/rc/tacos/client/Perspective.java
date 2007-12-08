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
        layout.setFixed(true);
        layout.addStandaloneView(VehiclesView.ID, false, IPageLayout.LEFT, 0.6f, editorArea);
        layout.addStandaloneView(PersonalView.ID, false, IPageLayout.RIGHT,0.4f,editorArea);
        layout.addStandaloneView(FormView.ID,  false, IPageLayout.BOTTOM, 0.6f, PersonalView.ID);
        layout.addStandaloneView(View.ID,  false, IPageLayout.RIGHT, 0.4f, FormView.ID);
	}
}
