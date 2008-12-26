package at.rc.tacos.client.ui.controller;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.VehicleHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.util.MyUtils;

/**
 * Sets the given transport status
 * 
 * @author b.thek
 */
public class SetTransportStatusAction extends Action {

	private TableViewer viewer;
	private int status;
	private Transport transport;

	/**
	 * Constructor to set the given transport status
	 * 
	 * @param viewer
	 *            the table viewer
	 * @param status
	 *            the transport status to set
	 */
	public SetTransportStatusAction(TableViewer viewer, int status, String shownAs) {
		this.viewer = viewer;
		this.status = status;
		setText(shownAs);
		setToolTipText("Setzt den Transportstatus " + " " + shownAs);
	}

	@Override
	public void run() {
		// get the selected transport
		ISelection selection = viewer.getSelection();
		transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		transport.addStatus(status, Calendar.getInstance().getTimeInMillis());

		if (status == ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE) {
			// get the vehicle handler
			VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper.getHandler(VehicleDetail.class);
			String now = MyUtils.timestampToString(Calendar.getInstance().getTimeInMillis(), MyUtils.timeFormat);
			VehicleDetail vehicle = vehicleHandler.getVehicleByName(transport.getVehicleDetail().getVehicleName());
			vehicle.setLastDestinationFree(now + " " + transport.getToStreet() + "/" + transport.getToCity());
			transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
			// update the vehicle
			UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(vehicle);
			updateMessage.asnchronRequest(NetWrapper.getSession());
		}
		// update the transport
		UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// get the selected transport
		ISelection selection = viewer.getSelection();
		transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		if (transport.getStatusMessages().containsKey(status)) {
			return UiWrapper.getDefault().getImageRegistry().getDescriptor("vehicle.ready");
		}
		return null;
	}
}
