package at.rc.tacos.client.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.ui.view.FilterView;
import at.rc.tacos.client.ui.view.NavigationView;
import at.rc.tacos.client.ui.view.PrebookingView;

public class TransportPrebookingPerspective implements IPerspectiveFactory {

	public static final String ID = "at.rc.tacos.client.ui.perspectives.transportPrebooking";

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
		// the main components
		layout.addStandaloneView(NavigationView.ID, true, IPageLayout.TOP, 0.10f, editorArea);
		layout.addStandaloneView(PrebookingView.ID, true, IPageLayout.RIGHT, 0.12f, editorArea);
		layout.addStandaloneView(FilterView.ID, true, IPageLayout.LEFT, 0.85f, editorArea);
	}
}
