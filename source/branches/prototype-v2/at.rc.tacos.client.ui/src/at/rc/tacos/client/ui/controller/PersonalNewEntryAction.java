package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.dialog.RosterEntryDialog;

public class PersonalNewEntryAction extends Action {

	@Override
	public String getId() {
		return "at.rc.tacos.client.ui.actions.PersonalNewEntryAction";
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "�ffnet ein Fenster um einen Dienstplan eintrag zu erstellen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Dienst";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("toolbar.createRoster");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run() {
		// open the editor
		Shell parent = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		// get the parent and the window shell
		RosterEntryDialog window = new RosterEntryDialog(parent);
		window.open();
	}
}
