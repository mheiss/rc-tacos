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
package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.SickPerson;

public class PatientViewFilter extends ViewerFilter {

	// the filter property
	private String filterText;

	/**
	 * Default class constructor
	 */
	public PatientViewFilter(String filterText) {
		this.filterText = filterText;
	}

	/**
	 * Returns whether or not the object should be filtered or not.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param parentElement
	 *            the parent element
	 * @param element
	 *            the element to check
	 */
	@Override
	public boolean select(Viewer arg0, Object parentElement, Object element) {
		// cast to a patient
		SickPerson person = (SickPerson) element;

		// assert valid filter
		if (filterText != null & !filterText.trim().isEmpty()) {
			// check the lastname
			String lastName = person.getLastName().toLowerCase();
			if (lastName.contains(filterText) || lastName.startsWith(filterText))
				return true;
			// chec the first name
			String firstName = person.getFirstName().toLowerCase();
			if (firstName.contains(filterText) || firstName.startsWith(filterText))
				return true;
		}
		// nothing matched, filter out
		return false;
	}
}
