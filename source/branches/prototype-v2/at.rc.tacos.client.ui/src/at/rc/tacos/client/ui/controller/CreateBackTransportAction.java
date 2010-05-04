package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Creates a backtransport for the selected transport
 * 
 * @author b.thek
 */
public class CreateBackTransportAction extends Action implements IProgramStatus {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public CreateBackTransportAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Rücktransport erstellen");
		setToolTipText("Erstellt den Rücktransport zum ausgewählten Transport");
	}

	@Override
	public void run() {
		// get the selected transport
		ISelection selection = viewer.getSelection();

		// create the backtransport
		Transport selectedTransport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		Transport backTransport = Transport.createBackTransport(selectedTransport, NetWrapper.getSession().getUsername());

		// show the arrow for the back transport no longer
		selectedTransport.setBackTransport(false);

		// update the transports
		UpdateMessage<Transport> updateSelected = new UpdateMessage<Transport>(selectedTransport);
		updateSelected.asnchronRequest(NetWrapper.getSession());

		AddMessage<Transport> addBackTransport = new AddMessage<Transport>(backTransport);
		addBackTransport.asnchronRequest(NetWrapper.getSession());
	}
}
