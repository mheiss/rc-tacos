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
     */
	public void createInitialLayout(IPageLayout layout) 
	{
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(false);
        layout.addStandaloneView(FormView.ID,  true, IPageLayout.TOP, 0.5f, editorArea);
        layout.addStandaloneView(VehiclesView.ID, true, IPageLayout.LEFT,0.0f,editorArea);
        layout.addStandaloneView(PersonalView.ID, true, IPageLayout.RIGHT,0.0f,editorArea);
        layout.addStandaloneView(View.ID,  true, IPageLayout.BOTTOM, 0.35f, editorArea);
	}
}
