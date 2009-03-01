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

/**
 * This is a string based simple filter
 * 
 * @author Michael
 */
public class StringViewFilter extends ViewerFilter {

	// properties
	private String filter;

	/**
	 * Default class constructor
	 */
	public StringViewFilter(String filter) {
		this.filter = filter.toLowerCase();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		// the value to filter
		String valueToFilter = (String) element;
		valueToFilter = valueToFilter.toLowerCase();

		// filter out
		if (valueToFilter.contains(filter) || valueToFilter.startsWith(filter))
			return true;
		return false;
	}
}
