package at.rc.tacos.client.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.ui.view.FilterView;
import at.rc.tacos.client.ui.view.PrebookingView;
import at.rc.tacos.client.ui.view.TransportMenuView;

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
		layout.addStandaloneView(TransportMenuView.ID, false, IPageLayout.TOP, 0.10f, editorArea);
		layout.addStandaloneView(FilterView.ID, false, IPageLayout.LEFT, 0.15f, editorArea);
		layout.addStandaloneView(PrebookingView.ID, false, IPageLayout.RIGHT, 0.75f, editorArea);
	}
}
