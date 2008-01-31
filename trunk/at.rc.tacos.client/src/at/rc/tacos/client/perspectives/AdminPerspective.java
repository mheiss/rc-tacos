package at.rc.tacos.client.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.view.admin.PhoneAdminView;
import at.rc.tacos.client.view.admin.PhoneDetailAdminView;
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
        //Create a folder on the left
        IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, (float) 0.26, editorArea);
        left.addView(StaffMemberView.ID);
        left.addView(VehicleAdminView.ID);
        left.addView(PhoneAdminView.ID);
        //Create the folder on the right
        IFolderLayout right = layout.createFolder("right", IPageLayout.LEFT, (float) 0.70, editorArea);
        right.addPlaceholder(StaffDetailView.ID);
        right.addPlaceholder(VehicleDetailAdminView.ID);
        right.addPlaceholder(PhoneDetailAdminView.ID);
    }
}
