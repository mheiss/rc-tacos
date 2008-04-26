package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.view.VehiclesSelectForm;
import at.rc.tacos.factory.ImageFactory;

public class VehicleOpenAction extends Action
{
	/**
	 * Default class constructor for selecting a vehicle to edit
	 */
	public VehicleOpenAction() { }


	/**
	 * Returns the tooltip text for the action
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() 
	{
		return "Öffnen einen Dialog um ein Fahrzeug auszuwählen das verwaltet werden soll";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * @return the text to render
	 */
	@Override
	public String getText()
	{
		return "Fahrzeug verwalten";
	}

	/**
	 * Returns the image to use for this action.
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() 
	{
		return ImageFactory.getInstance().getRegisteredImageDescriptor("toolbar.manageVehicle");
	}

	/**
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run()
	{
		//get the active shell
		Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();

		//create the window
		VehiclesSelectForm window = new VehiclesSelectForm(parent);
		window.getShell().setVisible(false);
		window.create();

		//get the shell and resize
		Shell myShell = window.getShell();
		myShell.setSize(600, 400);

		//calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = myShell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width)/2+workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height)/2+workbenchSize.y;
		myShell.setLocation(locationX,locationY);

		//now open it
		myShell.open();
		myShell.setVisible(false);
	}
}
