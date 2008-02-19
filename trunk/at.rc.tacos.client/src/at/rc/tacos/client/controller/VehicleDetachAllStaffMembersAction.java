package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

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
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run()
	{
		//vehicle is not ready for action any more
		detail.setReadyForAction(false);
	    //detach the staff
		detail.setDriver(null);
		detail.setFirstParamedic(null);
		detail.setSecondParamedic(null);
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);
	}
}
