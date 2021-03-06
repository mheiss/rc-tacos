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

import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;

/**
 * Personal view filter for the table. <br>
 * The table shows only the entries that apply to the filter
 * 
 * @author Michael
 */
public class PersonalViewFilter extends ViewerFilter {

	// properties
	private Location location;

	/**
	 * Create a new ViewFilter object and pass the station to filter
	 * 
	 * @param station
	 *            the station to show
	 */
	public PersonalViewFilter(Location location) {
		this.location = location;
	}

	/**
	 * Returns whether or not the object should be filtered out.
	 * 
	 * @param viewer
	 *            the viewer
	 * @param parentElement
	 *            the parent element
	 * @param element
	 *            the element to check
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		// cast the element
		RosterEntry entry = (RosterEntry) element;

		// check the entry
		if (entry.getStation().equals(location))
			return true;

		// filter the element out
		return false;
	}
}
