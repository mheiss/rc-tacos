package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.VehicleForm;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

public class VehicleEditAction extends Action
{
	//properties
	private VehicleDetail detail;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleEditAction(VehicleDetail detail)
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
		return "Editiert ein vorhandenes Fahrzeug";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Fahrzeug besetzen";
	}

	/**
	 * Returns the image to use for this action.
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.vehicle");
	}

	/**
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run()
	{
	    //open the editor
        Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		VehicleForm window = new VehicleForm(parent,detail);
		window.open();
	}
}
