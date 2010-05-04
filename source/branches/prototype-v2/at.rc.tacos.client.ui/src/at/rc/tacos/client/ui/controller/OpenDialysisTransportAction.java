package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.dialog.DialysisForm;

public class OpenDialysisTransportAction extends Action {

	@Override
	public String getId() {
		return "at.rc.tacos.client.ui.actions.OpenDialysisTransportAction";
	}

	/**
	 * Returns the tool tip text for the action
	 * 
	 * @return the tool tip text
	 */
	@Override
	public String getToolTipText() {
		return "Öffnet ein Fenster um einen neuen Dialyseeintrag zu erstellen";
	}

	/**
	 * Returns the text to show in the tool bar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Dialyse";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("toolbar.createTransportDialyse");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run() {
		DialysisForm window = new DialysisForm();
		window.open();
	}
}
