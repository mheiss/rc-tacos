package at.rc.tacos.client.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.ui.view.InfoView;
import at.rc.tacos.client.ui.view.PersonalView;
import at.rc.tacos.client.ui.view.ResourceMenuView;
import at.rc.tacos.client.ui.view.VehiclesViewTableDetailed;

/**
 * This is the standard perspective for the employees to work
 * 
 * @author Michael
 */
public class ResourcePerspective implements IPerspectiveFactory {

	public static final String ID = "at.rc.tacos.client.ui.perspectives.main";

	/**
	 * Set up the layout of the workbench
	 * 
	 * @param layout
	 *            the page layout to use
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);

		layout.addStandaloneView(ResourceMenuView.ID, false, IPageLayout.TOP, 0.10f, editorArea);
		layout.addStandaloneView(VehiclesViewTableDetailed.ID, false, IPageLayout.LEFT, 0.3f, editorArea);
		layout.addStandaloneView(PersonalView.ID, false, IPageLayout.LEFT, 0.243f, editorArea);
		layout.addStandaloneView(InfoView.ID, false, IPageLayout.TOP, 0.35f, PersonalView.ID);
	}
}
