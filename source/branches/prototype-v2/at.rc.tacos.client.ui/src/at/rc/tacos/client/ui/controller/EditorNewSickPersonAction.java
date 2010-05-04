package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.SickPersonEditor;
import at.rc.tacos.client.ui.admin.editors.SickPersonEditorInput;
import at.rc.tacos.platform.model.SickPerson;

/**
 * Opens an empty editor to create a new location
 * 
 * @author Birgit
 */
public class EditorNewSickPersonAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditorNewSickPersonAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewSickPersonAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um einen Patienten zu erfassen.";
	}

	/**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Patient hinzufügen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.patientAdd");
	}

	/**
	 * Opens the editor to create the location
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new sick person and open the editor
			SickPerson newPerson = new SickPerson();
			SickPersonEditorInput input = new SickPersonEditorInput(newPerson, true);
			page.openEditor(input, SickPersonEditor.ID);
		}
		catch (PartInitException e) {
			log.error("Failed to create a new sick person editor", e);
		}
	}
}
