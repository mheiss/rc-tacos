package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

/**
 * Detaches all staff members of the given vehicle
 * and sets the ready status to false and the vehicle status to not available
 * @author b.thek
 */
public class VehicleDetachAllStaffMembersAction extends Action
{
	//properties
	private VehicleDetail detail;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleDetachAllStaffMembersAction(VehicleDetail detail)
	{
		this.detail = detail;
	}

	/**
	 * Returns the tooltip text for the action
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() 
	{
		return "Zieht das Personal vom Fahrzeug ab";
	}

	/**
	 * Returns the text for the context menu item
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Besatzung abziehen";
	}

	/**
	 * Returns the image to use for this action.
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.userRemove");
	}

	/**
	 * Detaches all assigned staff members from the vehicle and updates it
	 */
	@Override
	public void run()
	{
		//vehicle is not ready for action any more
		detail.setReadyForAction(false);
		detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
	    //detach the staff
		detail.setDriver(null);
		detail.setFirstParamedic(null);
		detail.setSecondParamedic(null);
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
	}
}
