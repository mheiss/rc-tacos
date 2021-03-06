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

import at.rc.tacos.model.DialysisPatient;

/**
 * Dialysis transports
 * 
 * @author b.thek
 */
public class DialysisTransportManager extends PropertyManager {

	// the item list
	private List<DialysisPatient> objectList = new ArrayList<DialysisPatient>();

	/**
	 * Default class constructor
	 */
	public DialysisTransportManager() {
		objectList = new ArrayList<DialysisPatient>();
	}

	/**
	 * Adds a new dialysis transport to the list
	 * 
	 * @param dialysis
	 *            transport the dialysis transport to add
	 */
	public void add(final DialysisPatient dialysisPatient) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// add the item
				objectList.add(dialysisPatient);
				// notify the view
				firePropertyChange("DIALYSISPATIENT_ADD", null, dialysisPatient);
			}
		});
	}

	/**
	 * Removes the dialysis transport from the list
	 * 
	 * @param dialysis
	 *            transport the dialysis transport to remove
	 */
	public void remove(final DialysisPatient dialysisPatient) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				objectList.remove(dialysisPatient);
				firePropertyChange("DIALYSISPATIENT_REMOVE", dialysisPatient, null);
			}
		});
	}

	/**
	 * Updates the dialysis transport at the list
	 * 
	 * @param dialysis
	 *            transport the dialysis transport to update
	 */
	public void update(final DialysisPatient dialysisPatient) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				// assert we have this dialysis
				if (!objectList.contains(dialysisPatient))
					return;
				// get the position of the entry
				int id = objectList.indexOf(dialysisPatient);
				objectList.set(id, dialysisPatient);
				firePropertyChange("DIALYSISPATIENT_UPDATE", null, dialysisPatient);
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
				firePropertyChange("DIALYSISPATIENT_CLEARED", null, null);
			}
		});
	}

	/**
	 * Returns whether or not this dialysis transport is in the list of managed
	 * object
	 * 
	 * @param newDialysisTransport
	 *            the transport to check
	 */
	public boolean contains(DialysisPatient newDialysisTransport) {
		return objectList.contains(newDialysisTransport);
	}

	/**
	 * Returns a list of all dialysis patients
	 * 
	 * @return all patients managed
	 */
	public List<DialysisPatient> getDialysisList() {
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
