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

import at.rc.tacos.client.modelManager.MobilePhoneManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.MobilePhoneDetail;

/**
 * This class will be notified uppon mobile phone changes
 * 
 * @author Michael
 */
public class MobilePhoneListener extends ClientListenerAdapter {

	// the mobile phone manager
	private MobilePhoneManager manager = ModelFactory.getInstance().getPhoneManager();

	@Override
	public void add(AbstractMessage addMessage) {
		manager.add((MobilePhoneDetail) addMessage);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		for (AbstractMessage msg : listMessage) {
			MobilePhoneDetail detail = (MobilePhoneDetail) msg;
			// assert we do not have this phone
			if (manager.contains(detail))
				manager.update(detail);
			else
				manager.add(detail);
		}
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		manager.remove((MobilePhoneDetail) removeMessage);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		manager.update((MobilePhoneDetail) updateMessage);
	}
}
