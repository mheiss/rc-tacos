package at.rc.tacos.server.ui.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import at.rc.tacos.server.ui.views.OnlineServerView;
import at.rc.tacos.server.ui.views.OnlineUsersView;
import at.rc.tacos.server.ui.views.ServerStatusView;

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
		layout.addStandaloneView(ServerStatusView.ID,  false, IPageLayout.TOP, 0.4f, editorArea);
		layout.addStandaloneView(OnlineUsersView.ID,  false, IPageLayout.LEFT, 0.5f, editorArea);
		layout.addStandaloneView(OnlineServerView.ID,  false, IPageLayout.RIGHT, 0.5f, editorArea);
	}
}
