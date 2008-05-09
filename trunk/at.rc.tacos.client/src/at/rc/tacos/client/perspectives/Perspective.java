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
        folder.addView(JournalView.ID);
        folder.addView(PrebookingView.ID);
        
        folder.addView(DialysisView.ID);
       
        layout.addStandaloneView(NavigationView.ID, false, IPageLayout.TOP, 0.10f, editorArea);
        layout.addStandaloneView(FilterView.ID,true, IPageLayout.LEFT, 0.11f, editorArea);
        layout.addStandaloneView(VehiclesViewTable.ID,true, IPageLayout.LEFT, 0.055f, editorArea);
        layout.addStandaloneView(VehiclesViewTableDetailed.ID, true, IPageLayout.LEFT, 0.3f, editorArea);
        layout.addStandaloneView(VehiclesView.ID,true, IPageLayout.LEFT, 0.512f, editorArea);
        layout.addStandaloneView(PersonalView.ID,false, IPageLayout.LEFT, 0.243f, editorArea);
        layout.addStandaloneView(InfoView.ID, false, IPageLayout.TOP, 0.35f, PersonalView.ID); 
       
        
        //add the shortcuts to open the views
        layout.addShowViewShortcut(UnderwayTransportsView.ID);
        layout.addShowViewShortcut(PrebookingView.ID);
        layout.addShowViewShortcut(DialysisView.ID);
        layout.addShowViewShortcut(JournalView.ID);
        
        
//        layout.getViewLayout(JournalView.ID).setCloseable(false);//doesn't work //TODO
	}
}
