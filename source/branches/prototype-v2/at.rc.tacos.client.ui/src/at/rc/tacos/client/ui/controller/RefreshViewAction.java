package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.net.message.GetMessage;

/**
 * This action sends a listing request to the server to update the displayed
 * data
 * 
 * @author Michael
 */
public class RefreshViewAction<T> extends Action {

	private GetMessage<T> getMessage;

	/**
	 * Default class constructor to execute a get message.
	 * 
	 * @param getMessage
	 *            the message to send to the server
	 */
	public RefreshViewAction(GetMessage<T> getMessage) {
		this.getMessage = getMessage;
	}

	/**
	 * Runs the action
	 */
	@Override
	public void run() {
		getMessage.asnchronRequest(NetWrapper.getSession());
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Ansicht aktualiseren";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("resource.refresh");
	}
}
