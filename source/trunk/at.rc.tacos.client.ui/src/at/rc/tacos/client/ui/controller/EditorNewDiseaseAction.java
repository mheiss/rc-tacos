package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.DiseaseEditor;
import at.rc.tacos.client.ui.admin.editors.DiseaseEditorInput;
import at.rc.tacos.platform.model.Disease;

/**
 * Opens a new editor to create a disease
 * 
 * @author Michael
 */
public class EditorNewDiseaseAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditorNewDiseaseAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewDiseaseAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um eine Erkrankung anzulegen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Erkrankung hinzufügen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.diseaseAdd");
	}

	/**
	 * Opens the editor to create the competence
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new competence and open the editor
			Disease newDisease = new Disease();
			DiseaseEditorInput input = new DiseaseEditorInput(newDisease, true);
			page.openEditor(input, DiseaseEditor.ID);
		}
		catch (PartInitException e) {
			log.error("Failed to create a new disease editor", e);
		}
	}
}
