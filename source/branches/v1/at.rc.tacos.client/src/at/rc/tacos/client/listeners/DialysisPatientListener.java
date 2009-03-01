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

import at.rc.tacos.client.modelManager.DialysisTransportManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.model.DialysisPatient;

public class DialysisPatientListener extends ClientListenerAdapter {

	DialysisTransportManager manager = ModelFactory.getInstance().getDialyseManager();

	@Override
	public void add(AbstractMessage addMessage) {
		manager.add((DialysisPatient) addMessage);
	}

	@Override
	public void update(AbstractMessage updateMessage) {
		manager.update((DialysisPatient) updateMessage);
	}

	@Override
	public void remove(AbstractMessage removeMessage) {
		manager.remove((DialysisPatient) removeMessage);
	}

	@Override
	public void list(List<AbstractMessage> listMessage) {
		// add the entries
		for (AbstractMessage msg : listMessage) {
			DialysisPatient patient = (DialysisPatient) msg;
			// assert we do not have this patient
			if (manager.contains(patient))
				manager.update(patient);
			else
				manager.add(patient);
		}
	}
}
