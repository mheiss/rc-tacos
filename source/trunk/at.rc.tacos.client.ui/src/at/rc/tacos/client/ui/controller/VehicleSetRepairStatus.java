package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchWindow;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.message.UpdateMessage;

/**
 * Sets the status of the vehicle to out of order
 * 
 * @author Michael
 */
public class VehicleSetRepairStatus extends Action {

	// properties
	private VehicleDetail detail;
	private IWorkbenchWindow window;

	/**
	 * Default class constructor specifying the vehicle
	 * 
	 * @param window
	 *            the active workbench window
	 * @param detail
	 *            the vehicle to update
	 */
	public VehicleSetRepairStatus(IWorkbenchWindow window, VehicleDetail detail) {
		this.window = window;
		this.detail = detail;
	}

	/**
	 * Sets the status and runs the action
	 */
	@Override
	public void run() {
		boolean confirm = MessageDialog.openConfirm(window.getShell(), "Setzen des Status best‰tigen", "Wollen sie das Fahrzeug "
				+ detail.getVehicleName() + " wirklich auﬂer Dienst stellen?");
		// check
		if (!confirm)
			return;
		// set the status
		detail.setReadyForAction(false);
		detail.setOutOfOrder(true);
		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);

		// update the vehicle
		UpdateMessage<VehicleDetail> updateMessage = new UpdateMessage<VehicleDetail>(detail);
		updateMessage.asnchronRequest(NetWrapper.getSession());
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Setzt den Status 'Auﬂer Dienst'";
	}

	/**
	 * Returns the text for the context menu item
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Status Auﬂer Dienst";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return UiWrapper.getDefault().getImageRegistry().getDescriptor("resource.vehicleOutOfOrder");
	}
}
