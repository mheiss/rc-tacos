package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Moves the given transport to the outstanding transports
 * 
 * @author b.thek
 */
public class MoveToOutstandingTransportsAction extends Action implements ITransportStatus, IProgramStatus {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public MoveToOutstandingTransportsAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Nach offene Transporte verschieben");
		setToolTipText("Verschiebt den Transport zu den offenen Transporten");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		// change transport program status to 'outstanding'
		transport.getStatusMessages().clear();
		transport.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);
		// set the transport number to 0 - then the server will write the
		// transport number into the
		// tmptransports table and the number will be assigned to the next
		// transport of the same location
		if (transport.getTransportNumber() < 0)
			transport.setTransportNumber(0);
		// remove the vehicle to release the transport number
		transport.clearVehicleDetail();

		// send the update
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
		updateMessage.asnchronRequest(NetWrapper.getSession());
		;
	}
}
