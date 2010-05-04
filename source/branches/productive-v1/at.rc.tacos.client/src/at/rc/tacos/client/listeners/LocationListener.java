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

import java.util.List;

import at.rc.tacos.client.modelManager.LocationManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Location;

public class LocationListener extends ClientListenerAdapter {

	// the location manager
	LocationManager manager = ModelFactory.getInstance().getLocationManager();

	@Override
	public void add(AbstractMessage addMessage) {
		// cast to a location and add it
		Location location = (Location) addMessage;
		manager.add(location);
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		// cast to a location and remove it
		Location location = (Location) removeMessage;
		manager.remove(location);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		// cast to a location and add it
		Location location = (Location) updateMessage;
		manager.update(location);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		// loop and add all location
		for (AbstractMessage detailObject : listMessage) {
			// cast to a location and add it
			Location location = (Location) detailObject;
			// assert we do not have this location
			if (manager.contains(location))
				manager.update(location);
			else
				manager.add(location);
		}
	}
}
