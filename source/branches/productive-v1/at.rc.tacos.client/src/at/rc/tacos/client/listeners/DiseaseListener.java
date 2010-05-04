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

import at.rc.tacos.client.modelManager.DiseaseManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.Disease;

/**
 * Listens to changes of diseases
 * 
 * @author Michael
 */
public class DiseaseListener extends ClientListenerAdapter {

	// the disease manager
	private DiseaseManager manager = ModelFactory.getInstance().getDiseaseManager();

	@Override
	public void add(AbstractMessage addMessage) {
		Disease disease = (Disease) addMessage;
		manager.add(disease);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		// loop and add
		for (AbstractMessage listObject : listMessage) {
			Disease disease = (Disease) listObject;
			// assert we do not have this disease
			if (manager.contains(disease))
				manager.update(disease);
			else
				manager.add(disease);
		}
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		// cast and remove
		Disease disease = (Disease) removeMessage;
		manager.remove(disease);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		// cast and update
		Disease disease = (Disease) updateMessage;
		manager.update(disease);
	}
}
