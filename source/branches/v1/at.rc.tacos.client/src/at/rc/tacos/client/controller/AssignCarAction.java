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

import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * Assigns the in the context menu selected car to the in the table viewer
 * selected transport Move the transport from the outstanding to the underway
 * transports by setting the programStatus of the transport Set the transport
 * status 'order placed' at the time Assign the vehicle Set the user which has
 * execute this step (disposed user)
 * 
 * @author b.thek
 */
public class AssignCarAction extends Action implements IProgramStatus {

	// properties
	private TableViewer viewer;
	private VehicleDetail vehicle;

	/**
	 * Default class constructor.
	 * 
	 * @param viewer
	 *            the table viewer
	 */
	public AssignCarAction(TableViewer viewer, VehicleDetail vehicle) {
		this.viewer = viewer;
		this.vehicle = vehicle;
		setText(vehicle.getVehicleName());
		setToolTipText("Weist dem markierten Transport das im Kontextmenü ausgewählte Fahrzeug zu");
	}

	@Override
	public void run() {
		// the selection
		ISelection selection = viewer.getSelection();
		// get the selected transport
		Transport transport = (Transport) ((IStructuredSelection) selection).getFirstElement();
		// open the editor
		transport.setVehicleDetail(vehicle);
		transport.setDisposedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
		GregorianCalendar cal = new GregorianCalendar();
		long now = cal.getTimeInMillis();
		transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, now);
		transport.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
		NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
	}
}
