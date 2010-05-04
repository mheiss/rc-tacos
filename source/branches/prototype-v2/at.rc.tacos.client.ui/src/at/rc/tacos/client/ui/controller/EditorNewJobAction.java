package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.JobEditor;
import at.rc.tacos.client.ui.admin.editors.JobEditorInput;
import at.rc.tacos.platform.model.Job;

/**
 * Opens an editor to create a new job
 * 
 * @author Michael
 */
public class EditorNewJobAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditorNewJobAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewJobAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um eine neue Verwendung anlegen zu können.";
	}

	/**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Verwendung hinzufügen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.jobAdd");
	}

	/**
	 * Opens the editor to create the job
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new job and open the editor
			Job newJob = new Job();
			JobEditorInput input = new JobEditorInput(newJob, true);
			page.openEditor(input, JobEditor.ID);
		}
		catch (PartInitException e) {
			log.error("Failed to create a new job editor", e);
		}
	}
}
