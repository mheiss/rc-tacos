/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.controller;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.VehicleDetail;

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
		return ImageFactory.getInstance().getRegisteredImageDescriptor("admin.userRemove");
	}

	/**
	 * Detaches all assigned staff members from the vehicle and updates it
	 */
	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected entry
		VehicleDetail vehicle = (VehicleDetail) ((IStructuredSelection) selection).getFirstElement();

		// vehicle is not ready for action any more
		vehicle.setReadyForAction(false);
		vehicle.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_NA);
		// detach the staff
		vehicle.setDriver(null);
		vehicle.setFirstParamedic(null);
		vehicle.setSecondParamedic(null);
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, vehicle);
	}
}
