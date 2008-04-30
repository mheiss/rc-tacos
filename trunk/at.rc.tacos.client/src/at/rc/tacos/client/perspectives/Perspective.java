package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import at.rc.tacos.client.view.*;

/**
 * This is the standard perspective for the employees to work
 * @author Michael
 */
public class Perspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.client";
    
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
        IFolderLayout folder = layout.createFolder(TransportFolder.ID, IPageLayout.TOP, 0.10f, editorArea);
        folder.addView(UnderwayTransportsView.ID);
        folder.addView(PrebookingView.ID);
        folder.addView(JournalView.ID);
        folder.addView(DialysisView.ID);
       
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.TOP, 0.10f, editorArea);
        layout.addStandaloneView(VehiclesViewTable.ID,false, IPageLayout.LEFT, 0.07f, editorArea);
        layout.addStandaloneView(VehiclesView.ID,false, IPageLayout.LEFT, 0.50f, editorArea);
        layout.addStandaloneView(PersonalView.ID,false, IPageLayout.LEFT, 0.25f, editorArea);
        layout.addStandaloneView(InfoView.ID, false, IPageLayout.TOP, 0.35f, PersonalView.ID); 
        
        //add the shortcuts to open the views
        layout.addShowViewShortcut(UnderwayTransportsView.ID);
        layout.addShowViewShortcut(PrebookingView.ID);
        layout.addShowViewShortcut(DialysisView.ID);
        layout.addShowViewShortcut(JournalView.ID);
	}
}
