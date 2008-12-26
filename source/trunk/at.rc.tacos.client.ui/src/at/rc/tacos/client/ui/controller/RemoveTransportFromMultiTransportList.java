package at.rc.tacos.client.ui.controller;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.Transport;

public class RemoveTransportFromMultiTransportList extends Action {

	private List<Transport> transports;
	private TableViewer viewer;
	private int index;

	public RemoveTransportFromMultiTransportList(List<Transport> transports, TableViewer viewer, int index) {
		this.transports = transports;
		this.viewer = viewer;
		this.index = index;
	}

	/**
	 * Returns the tool tip text for the action
	 * 
	 * @return the tool tip text
	 */
	@Override
	public String getToolTipText() {
		return "Löscht den markierten Transport aus der Liste der zu speichernden Transporte";
	}

	/**
	 * Returns the text to show in the tool bar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Löschen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("toolbar.createTransport");
	}

	/**
	 * Shows the abut dialog of the application
	 */
	@Override
	public void run() {
		transports.remove(index);
		viewer.refresh();
	}
}
