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

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * Sets the transport status s6 (at station) for each transport of the day if
 * the status is not already set
 * 
 * @author b.thek
 */
public class VehicleAtStationAction extends Action implements ITransportStatus, IProgramStatus {

	private List<Transport> objectList = new ArrayList<Transport>();

	// properties
	private VehicleDetail detail;

	/**
	 * Default class constructor for editing a vehicle
	 */
	public VehicleAtStationAction(VehicleDetail detail) {
		this.detail = detail;
	}

	/**
	 * Returns the tooltip text for the action
	 * 
	 * @return the tooltip text
	 */
	@Override
	public String getToolTipText() {
		return "S6: Fahrzeug eingerückt setzen";
	}

	/**
	 * Retruns the text to show in the toolbar
	 * 
	 * @return the text to render
	 */
	@Override
	public String getText() {
		return "Fahrzeug eingerückt";
	}

	/**
	 * Returns the image to use for this action.
	 * 
	 * @return the image to use
	 */
	@Override
	public ImageDescriptor getImageDescriptor() {
		return ImageFactory.getInstance().getRegisteredImageDescriptor("resource.vehicle");
	}

	/**
	 * Shows the view to edit a vehicle
	 */
	@Override
	public void run() {
		objectList = ModelFactory.getInstance().getTransportManager().getJournalTransportsByVehicleAndStatusSix(detail.getVehicleName());

		// set the vehicle status to green and update the vehicle
		if (detail.getDriver() != null && detail.isReadyForAction()) {
			detail.setTransportStatus(VehicleDetail.TRANSPORT_STATUS_GREEN);
		}
		detail.setLastDestinationFree("");
		NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, detail);

		// create a timestamp for the transport state S6
		GregorianCalendar gcal = new GregorianCalendar();
		long timestamp = gcal.getTimeInMillis();
		// set the transport status S6 for each to the list given transport and
		// update the transport
		for (Transport transport : objectList) {
			transport.addStatus(TRANSPORT_STATUS_CAR_IN_STATION, timestamp);
			NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
		}
	}
}
