package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import at.rc.tacos.client.view.admin.StaffDetailView;
import at.rc.tacos.client.view.admin.StaffMemberView;
import at.rc.tacos.client.view.admin.VehicleAdminView;
import at.rc.tacos.client.view.admin.VehicleDetailAdminView;

/**
 * The perspective for the administrator
 * @author Michael
 */
public class AdminPerspective implements IPerspectiveFactory 
{
    public static final String ID = "at.rc.tacos.client.perspectives.admin";
    
    /**
     * Set up the layout of the workbench
     * @param layout the page layout to use
     */
    public void createInitialLayout(IPageLayout layout) 
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);
        layout.addStandaloneView(StaffMemberView.ID,false, IPageLayout.LEFT, 0.25f, editorArea);
        layout.addStandaloneView(StaffDetailView.ID,false, IPageLayout.RIGHT, 0.65f, editorArea);
        layout.addStandaloneView(VehicleDetailAdminView.ID,false, IPageLayout.LEFT, 0.25f, editorArea);
        layout.addStandaloneView(VehicleAdminView.ID,false, IPageLayout.RIGHT, 0.65f, editorArea);
    }
}
