package at.rc.tacos.client.ui.controller;

import java.util.Calendar;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.VehicleHandler;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportPriority;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.AddMessage;

/**
 * The action that is triggered by creating a new transport if priority A (NEF)
 * is choosen.
 * 
 * @author b.thek
 */
public class DuplicatePriorityATransportAction extends Action {

	private Transport transport;
	private VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper.getHandler(VehicleDetail.class);

	/**
	 * Creates a new TransportAction.
	 * 
	 * @param entry
	 *            the new transport
	 */
	public DuplicatePriorityATransportAction(Transport transport) {
		this.transport = transport;
	}

	@Override
	public void run() {
		String username = NetWrapper.getSession().getUsername();

		// copy the transport
		Transport newTransport = Transport.createTransport(transport, username);
		newTransport.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_EMERGENCY_DOCTOR_INTERNAL);
		newTransport.setDisposedByUsername(username);

		// assign NEF vehicle
		newTransport.setVehicleDetail(vehicleHandler.getNEFVehicle());
		newTransport.setTransportNumber(Transport.TRANSPORT_NEF);

		// set the underway status
		newTransport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, Calendar.getInstance().getTimeInMillis());
		newTransport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_UNDERWAY);

		// add the new transport
		AddMessage<Transport> addMessage = new AddMessage<Transport>(newTransport);
		addMessage.asnchronRequest(NetWrapper.getSession());
	}
}
