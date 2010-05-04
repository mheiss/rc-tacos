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
package at.rc.tacos.client.listeners;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.VehicleManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.VehicleDetail;

/**
 * This class will be notified uppon vehicle detail changes
 * 
 * @author Michael
 */
public class VehicleDetailListener extends ClientListenerAdapter {

	// the vehicle manager
	VehicleManager manager = ModelFactory.getInstance().getVehicleManager();

	@Override
	public void add(AbstractMessage addMessage) {
		// cast to a vehicle and add it
		VehicleDetail detail = (VehicleDetail) addMessage;
		manager.add(detail);
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		// cast to a vehicle and remove it
		VehicleDetail detail = (VehicleDetail) removeMessage;
		manager.remove(detail);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		// cast to a vehicle and add it
		VehicleDetail detail = (VehicleDetail) updateMessage;
		manager.update(detail);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		// conert to a list of vehicles
		List<VehicleDetail> vehicleList = new ArrayList<VehicleDetail>();
		for (AbstractMessage object : listMessage)
			vehicleList.add((VehicleDetail) object);
		manager.addAll(vehicleList);
	}
}
