package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.StaffMemberEditor;
import at.rc.tacos.client.ui.admin.editors.StaffMemberEditorInput;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.StaffMember;

/**
 * Opens an empty editor to create a new staff member and the login data.
 * 
 * @author Michael
 */
public class EditorNewStaffAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditorNewStaffAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewStaffAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um einen Mitarbeiter anlegen zu können.";
	}

	/**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Mitarbeiter anlegen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.userAdd");
	}

	/**
	 * Opens the editor to create the staff
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new staff member and login and open the editor
			StaffMember newMember = new StaffMember();
			Login newLogin = new Login();
			StaffMemberEditorInput input = new StaffMemberEditorInput(newMember, newLogin, true);
			page.openEditor(input, StaffMemberEditor.ID);
		}
		catch (PartInitException e) {
			log.error("Failed to create a new staff member editor", e);
		}
	}
}
