package at.rc.tacos.client.ui.admin.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.ui.admin.view.AddressAdminView;
import at.rc.tacos.client.ui.admin.view.CompetenceAdminView;
import at.rc.tacos.client.ui.admin.view.DiseaseAdminView;
import at.rc.tacos.client.ui.admin.view.JobAdminView;
import at.rc.tacos.client.ui.admin.view.LocationAdminView;
import at.rc.tacos.client.ui.admin.view.PhoneAdminView;
import at.rc.tacos.client.ui.admin.view.ServiceTypeAdminView;
import at.rc.tacos.client.ui.admin.view.SickPersonAdminView;
import at.rc.tacos.client.ui.admin.view.StaffMemberAdminView;
import at.rc.tacos.client.ui.admin.view.VehicleAdminView;
import at.rc.tacos.client.ui.view.ResourceMenuView;

/**
 * The perspective for the administrator
 * 
 * @author Michael
 */
public class AdminPerspective implements IPerspectiveFactory {

	public static final String ID = "at.rc.tacos.client.ui.perspectives.admin";

	/**
	 * Set up the layout of the workbench
	 * 
	 * @param layout
	 *            the page layout to use
	 */
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(true);
		layout.setFixed(true);

		// the main components
		layout.addStandaloneView(ResourceMenuView.ID, false, IPageLayout.TOP, 0.10f, editorArea);

		// Create a folder on the left
		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.32f, editorArea);
		left.addView(StaffMemberAdminView.ID);
		left.addView(VehicleAdminView.ID);
		left.addView(LocationAdminView.ID);
		left.addView(PhoneAdminView.ID);
		left.addView(CompetenceAdminView.ID);
		left.addView(JobAdminView.ID);
		left.addView(ServiceTypeAdminView.ID);
		left.addView(SickPersonAdminView.ID);
		left.addView(AddressAdminView.ID);
		left.addView(DiseaseAdminView.ID);
	}
}
