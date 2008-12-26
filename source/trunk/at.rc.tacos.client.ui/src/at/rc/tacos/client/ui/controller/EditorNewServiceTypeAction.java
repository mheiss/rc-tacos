package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.admin.editors.ServiceTypeEditor;
import at.rc.tacos.client.ui.admin.editors.ServiceTypeEditorInput;
import at.rc.tacos.platform.model.ServiceType;

/**
 * Opens a new editor to create a service type
 * 
 * @author Michael
 */
public class EditorNewServiceTypeAction extends Action {

	private Logger log = LoggerFactory.getLogger(EditorNewServiceTypeAction.class);

	private IWorkbenchWindow window;

	/**
	 * Default class constructor for creating the editor
	 */
	public EditorNewServiceTypeAction(IWorkbenchWindow window) {
		this.window = window;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnen ein neues Fenster um ein Dienstverhältnis anzulegen.";
	}

	/**
	 * Returns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Dienstverhältnis hinzufügen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.serviceTypeAdd");
	}

	/**
	 * Opens the editor to create the service type
	 */
	@Override
	public void run() {
		IWorkbenchPage page = window.getActivePage();
		try {
			// create a new service type and open the editor
			ServiceType newServiceType = new ServiceType();
			ServiceTypeEditorInput input = new ServiceTypeEditorInput(newServiceType, true);
			page.openEditor(input, ServiceTypeEditor.ID);
		}
		catch (PartInitException e) {
			log.error("Failed to create a new serviceType editor", e);
		}
	}
}
