package at.rc.tacos.server.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.server.ui.views.OnlineUsersView;

/**
 * Defines the perspective for the main server view
 * @author Michael
 */
public class Perspective implements IPerspectiveFactory 
{
	public void createInitialLayout(IPageLayout layout) 
	{
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView(OnlineUsersView.ID,  false, IPageLayout.LEFT, 1.0f, editorArea);
	}
}
