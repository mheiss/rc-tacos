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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Display;

import at.rc.tacos.model.Disease;

public class DiseaseManager extends PropertyManager {

	// the list
	private List<Disease> objectList = new ArrayList<Disease>();

	/**
	 * Default class constructor
	 */
	public DiseaseManager() {
	}

	/**
	 * Adds a new disease to the list
	 * 
	 * @param disease
	 *            the disease to add
	 */
	public void add(final Disease disease) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// add the item
				objectList.add(disease);
				// notify the view
				firePropertyChange("DISEASE_ADD", null, disease);
			}
		});
	}

	/**
	 * Removes the disease from the list
	 * 
	 * @param disease
	 *            the disease to remove
	 */
	public void remove(final Disease disease) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				objectList.remove(disease);
				firePropertyChange("DISEASE_REMOVE", disease, null);
			}
		});
	}

	/**
	 * Updates the disease in the list
	 * 
	 * @param disease
	 *            the disease to update
	 */
	public void update(final Disease disease) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// assert we have this disease
				if (!objectList.contains(disease))
					return;
				// get the position of the entry
				int id = objectList.indexOf(disease);
				objectList.set(id, disease);
				firePropertyChange("DISEASE_UPDATE", null, disease);
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
				firePropertyChange("DISEASE_CLEARED", null, null);
			}
		});
	}

	/**
	 * Returns whether or not this disease is in the list of managed object
	 * 
	 * @param newDisease
	 *            the disease to check
	 */
	public boolean contains(Disease newDisease) {
		return objectList.contains(newDisease);
	}

	/**
	 * Returns a given disease by the name
	 * 
	 * @param diseaseName
	 *            the name of the disease to get
	 */
	public Disease getDiseaseByName(String diseaseName) {
		// loop and search
		for (Disease disease : objectList) {
			if (disease.getDiseaseName().equalsIgnoreCase(diseaseName))
				return disease;
		}
		// nothing found
		return null;
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
