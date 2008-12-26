package at.rc.tacos.client.ui.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.dialog.VehicleForm;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * Opens a form to edit the vehicle
 * 
 * @author b.thek
 */
public class VehicleTableEditAction extends Action {

	private Logger log = LoggerFactory.getLogger(VehicleTableEditAction.class);

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
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		VehicleDetail vehicle = (VehicleDetail) ((IStructuredSelection) selection).getFirstElement();

		// check if the object is currenlty locked
		if (vehicle.isLocked()) {
			boolean forceEdit = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Information: Eintrag wird bearbeitet",
					"Das Fahrzeug das Sie bearbeiten möchten wird bereits von " + vehicle.getLockedBy() + " bearbeitet\n"
							+ "Ein gleichzeitiges Bearbeiten kann zu unerwarteten Fehlern führen!\n\n"
							+ "Möchten Sie den Eintrag trotzdem bearbeiten?");
			if (!forceEdit)
				return;
			// log the override of the lock
			String username = NetWrapper.getSession().getUsername();
			log.warn("Der Eintrag " + vehicle + " wird trotz Sperrung durch " + vehicle.getLockedBy() + " von " + username + " bearbeitet");
		}

		// get the active shell
		Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();

		// create the window
		VehicleForm window = new VehicleForm(parent, vehicle);
		window.create();

		// get the shell and resize
		Shell myShell = window.getShell();
		myShell.setSize(500, 620);

		// calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = myShell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width) / 2 + workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height) / 2 + workbenchSize.y;
		myShell.setLocation(locationX, locationY);

		// now open the window
		myShell.open();
	}
}
