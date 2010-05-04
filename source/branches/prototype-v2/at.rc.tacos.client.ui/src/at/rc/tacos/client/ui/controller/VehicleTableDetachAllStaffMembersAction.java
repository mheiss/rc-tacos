package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Detaches all staff members of the given vehicle and sets the ready status to
 * false and the vehicle status to not available
 * 
 * @author b.thek
 */
public class VehicleTableDetachAllStaffMembersAction extends Action {

	// properties
	private TableViewer viewer;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleTableDetachAllStaffMembersAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Zieht das Personal vom Fahrzeug ab";
	}

	/**
	 * Returns the text for the context menu item
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Besatzung abziehen";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("admin.userRemove");
	}

	/**
	 * Detaches all assigned staff members from the vehicle and updates it
	 */
	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		VehicleDetail vehicleDetail = (VehicleDetail) ((IStructuredSelection) selection).getFirstElement();

		// vehicle is not ready for action any more
		vehicleDetail.setReadyForAction(false);
		vehicleDetail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		// detach the staff
		vehicleDetail.setDriver(null);
		vehicleDetail.setFirstParamedic(null);
		vehicleDetail.setSecondParamedic(null);

		// update the vehicle
		UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(vehicleDetail);
		updateMessage.asnchronRequest(NetWrapper.getSession());

	}
}
