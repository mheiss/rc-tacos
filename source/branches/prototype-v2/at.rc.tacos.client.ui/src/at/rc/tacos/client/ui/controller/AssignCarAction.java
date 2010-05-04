package at.rc.tacos.client.ui.controller;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Assigns the in the context menu selected car to the in the table viewer
 * selected transport Move the transport from the outstanding to the underway
 * transports by setting the programStatus of the transport Set the transport
 * status 'order placed' at the time Assign the vehicle Set the user which has
 * execute this step (disposed user)
 * 
 * @author b.thek
 */
public class AssignCarAction extends Action implements IProgramStatus {

	// properties
	private TableViewer viewer;
	private VehicleDetail vehicle;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public AssignCarAction(TableViewer viewer, VehicleDetail vehicle) {
		this.viewer = viewer;
		this.vehicle = vehicle;
		setText(vehicle.getVehicleName());
		setToolTipText("Weist dem markierten Transport das im Kontextmenü ausgewählte Fahrzeug zu");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		if (selection.isEmpty()) {
			return;
		}

		// assign the car and update the transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		transport.setVehicleDetail(vehicle);
		transport.setDisposedByUsername(NetWrapper.getSession().getUsername());
		transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, Calendar.getInstance().getTimeInMillis());
		transport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);

		// send the update request
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}
}
