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
        layout.setFixed(true);
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.TOP, 0.1f, editorArea);
        layout.getViewLayout(NavigationView.ID).setMoveable(false);
        layout.addStandaloneView(VehiclesView.ID, false, IPageLayout.LEFT, 0.4f, editorArea);
        layout.addStandaloneView(PersonalView.ID, false, IPageLayout.RIGHT,0.1f,editorArea);
        layout.addStandaloneView(FormView.ID,  false, IPageLayout.BOTTOM, 0.6f, PersonalView.ID);
        layout.addStandaloneView(View.ID,  false, IPageLayout.RIGHT, 0.4f, FormView.ID);
	}
}
