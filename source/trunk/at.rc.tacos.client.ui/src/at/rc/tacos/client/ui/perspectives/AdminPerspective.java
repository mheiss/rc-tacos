package at.rc.tacos.client.ui.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import at.rc.tacos.client.ui.view.NavigationView;

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
		layout.addStandaloneView(NavigationView.ID, false, IPageLayout.TOP, 0.10f, editorArea);

		// Create a folder on the left
//		IFolderLayout left = layout.createFolder("left", IPageLayout.LEFT, 0.32f, editorArea);
//		left.addView(StaffMemberAdminView.ID);
//		left.addView(VehicleAdminView.ID);
//		left.addView(LocationAdminView.ID);
//		left.addView(PhoneAdminView.ID);
//		left.addView(CompetenceAdminView.ID);
//		left.addView(JobAdminView.ID);
//		left.addView(ServiceTypeAdminView.ID);
//		left.addView(SickPersonAdminView.ID);
//		left.addView(AddressAdminView.ID);
//		left.addView(DiseaseAdminView.ID);
	}
}
