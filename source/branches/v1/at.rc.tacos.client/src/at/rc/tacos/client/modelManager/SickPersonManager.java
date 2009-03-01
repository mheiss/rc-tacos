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
package at.rc.tacos.client.modelManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.SickPerson;

public class SickPersonManager extends PropertyManager {

	// the list
	private List<SickPerson> objectList = new ArrayList<SickPerson>();

	/**
	 * Default class constructor
	 */
	public SickPersonManager() {
	}

	/**
	 * Adds a new sick person to the list
	 * 
	 * @param sick
	 *            person the sick person to add
	 */
	public void add(final SickPerson person) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// add the item
				objectList.add(person);
				// notify the view
				firePropertyChange("SICKPERSON_ADD", null, person);
			}
		});
	}

	/**
	 * Removes the sick person from the list
	 * 
	 * @param sick
	 *            person the sick person to remove
	 */
	public void remove(final SickPerson person) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				objectList.remove(person);
				firePropertyChange("SICKPERSON_REMOVE", person, null);
			}
		});
	}

	/**
	 * Updates the sick person in the list
	 * 
	 * @param sick
	 *            person the sick person to update
	 */
	public void update(final SickPerson person) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// assert we have this sick person
				if (!objectList.contains(person))
					return;
				// get the position of the entry
				int id = objectList.indexOf(person);
				objectList.set(id, person);
				firePropertyChange("SICKPERSON_UPDATE", null, person);
			}
		});
	}

	/**
	 * Removes all elements form the list
	 */
	public void removeAllEntries() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				objectList.clear();
				firePropertyChange("SICKPERSON_CLEARED", null, null);
			}
		});
	}

	/**
	 * Returns whether or not this sick person is in the list of managed object
	 * 
	 * @param newsickPerson
	 *            the sick person to check
	 */
	public boolean contains(SickPerson newPerson) {
		return objectList.contains(newPerson);
	}

	/**
	 * Returns a given sickPerson by the id of the sickPerson
	 * 
	 * @param sickPerson
	 *            id the id of the sickPerson to get the sickPerson
	 */
	public SickPerson getSickPersonById(int id) {
		// loop and search
		for (SickPerson person : objectList) {
			if (person.getSickPersonId() == id)
				return person;
		}
		// nothing found
		return null;
	}

	/**
	 * informs all listeners about new sick persons
	 */
	public void initViews(PropertyChangeListener listener) {
		for (SickPerson person : objectList)
			listener.propertyChange(new PropertyChangeEvent(this, "SICKPERSON_ADD", null, person));
	}

	/**
	 * Returns all sick persons in the list
	 * 
	 * @return the sick person list
	 */
	public List<SickPerson> getSickPersons() {
		return objectList;
	}

	/**
	 * Converts the list to an array
	 * 
	 * @return the list as a array
	 */
	public Object[] toArray() {
		return objectList.toArray();
	}
}
