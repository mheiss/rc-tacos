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

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.ServiceTypeManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.ServiceType;

public class ServiceTypeListener extends ClientListenerAdapter {

	// the serviceType manager
	ServiceTypeManager manager = ModelFactory.getInstance().getServiceManager();

	@Override
	public void add(AbstractMessage addMessage) {
		// cast to a serviceType and add it
		ServiceType serviceType = (ServiceType) addMessage;
		manager.add(serviceType);
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		// cast to a serviceType and remove it
		ServiceType serviceType = (ServiceType) removeMessage;
		manager.remove(serviceType);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		// cast to a serviceType and add it
		ServiceType serviceType = (ServiceType) updateMessage;
		manager.update(serviceType);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		// loop and add all serviceType
		for (AbstractMessage detailObject : listMessage) {
			// cast to a serviceType and add it
			ServiceType serviceType = (ServiceType) detailObject;
			// assert we do not have this service type
			if (manager.contains(serviceType))
				manager.update(serviceType);
			else
				manager.add(serviceType);
		}
	}
}
