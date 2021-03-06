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
import at.rc.tacos.client.modelManager.TransportManager;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Transport;

/**
 * This class will be notified upon roster entry updates
 * 
 * @author Michael
 */
public class TransportListener extends ClientListenerAdapter {

	TransportManager manager = ModelFactory.getInstance().getTransportManager();

	@Override
	public void add(AbstractMessage addMessage) {
		manager.add((Transport) addMessage);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		manager.update((Transport) updateMessage);
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		manager.remove((Transport) removeMessage);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		manager.removeAllEntries();
		// add all
		for (AbstractMessage msg : listMessage) {
			Transport transport = (Transport) msg;
			// assert we do not have this transport
			if (manager.contains(transport))
				manager.update(transport);
			else
				manager.add(transport);
		}
	}
}
