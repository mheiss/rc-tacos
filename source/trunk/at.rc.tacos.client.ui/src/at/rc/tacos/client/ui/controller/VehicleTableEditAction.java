package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.dialog.VehicleDetailDialog;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * Opens a form to edit the vehicle
 * 
 * @author b.thek
 */
public class VehicleTableEditAction extends Action {

	private TableViewer viewer;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleTableEditAction(TableViewer viewer) {
		this.viewer = viewer;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "Editiert ein vorhandenes Fahrzeug";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Fahrzeug bearbeiten";
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
		// get the selected entry
		ISelection selection = viewer.getSelection();
		VehicleDetail vehicle = (VehicleDetail) ((IStructuredSelection) selection).getFirstElement();

		// get the active shell
		Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();

		// create and open the dialog
		VehicleDetailDialog vehicleDetailDialog = new VehicleDetailDialog(parent, vehicle);
		vehicleDetailDialog.open();
	}
}
