package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.CompetenceEditor;
import at.rc.tacos.client.ui.admin.editors.CompetenceEditorInput;
import at.rc.tacos.platform.model.Competence;

/**
 * Opens an empty editor to insert a new competence.
 * 
 * @author Michael
 */
public class EditorNewCompetenceAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditorNewCompetenceAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewCompetenceAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um eine Kompetenz anzulegen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Kompetenz hinzufügen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.competenceAdd");
	}

	/**
	 * Opens the editor to create the competence
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new competence and open the editor
			Competence newCompetence = new Competence();
			CompetenceEditorInput input = new CompetenceEditorInput(newCompetence, true);
			page.openEditor(input, CompetenceEditor.ID);
		}
		catch (PartInitException e) {
			log.error("Failed to create a new competence editor", e);
		}
	}
}
