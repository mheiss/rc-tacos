package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Sets or clears the option back transport possible for the transport
 * 
 * @author b.thek
 */
public class SetBackTransportPossibleAction extends Action implements IProgramStatus {

	// properties
	private TableViewer viewer;
	private Transport transport;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public SetBackTransportPossibleAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Rücktransport möglich");
		setToolTipText("Setzt die Option Rücktransport möglich für diesen Transport");
	}

	@Override
	public void run() {
		// get the selected transport
		ISelection selection = viewer.getSelection();
		transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		transport.setBackTransport(!transport.isBackTransport());

		// send the update message
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		if (transport.isBackTransport()) {
			return UiWrapper.getDefault().getImageRegistry().getDescriptor("vehicle.ready");
		}
		return null;
	}
}
