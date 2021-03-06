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
 * Moves the given transport to the running transports
 * 
 * @author b.thek
 */
public class JournalMoveToRunningTransportsAction extends Action implements ITransportStatus, IProgramStatus {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public JournalMoveToRunningTransportsAction(TableViewer viewer) {
		this.viewer = viewer;
		setText("Nach laufende Transporte verschieben");
		setToolTipText("Verschiebt den Transport zu den laufenden Transporten");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		// change transport program status to 'underway'
		transport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
		transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE);

		// send the update
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}
}
