package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * * Sets the transport status s6 (at station) for each transport of the day if
 * the status is not already set
 * 
 * @author b.thek
 */
public class VehicleTableAtStationAction extends Action implements ITransportStatus, IProgramStatus {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleTableAtStationAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "S6: Fahrzeug eingerückt setzen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Fahrzeug eingerückt";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("resource.vehicle");
	}

	/**
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		VehicleDetail detail = (VehicleDetail) ((IStructuredSelection) selection).getFirstElement();

		// the vehicle
		// set the vehicle status to green and update the vehicle
		if (detail.getDriver() != null && detail.isReadyForAction()) {
			detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
		}
		detail.setLastDestinationFree("");

		// update the vehicle
		UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(detail);
		updateMessage.asnchronRequest(NetWrapper.getSession());

		// request an update of all transports
		ExecMessage<VehicleDetail> execMessage = new ExecMessage<VehicleDetail>("setStatusSix", detail);
		execMessage.asnchronRequest(NetWrapper.getSession());
	}
}
