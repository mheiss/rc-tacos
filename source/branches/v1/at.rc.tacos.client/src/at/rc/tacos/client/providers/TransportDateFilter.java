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

import java.util.Calendar;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * Filters out all transports that didnt't match the current date
 * 
 * @author Michael
 */
public class TransportDateFilter extends ViewerFilter {

	// properties
	private Calendar date;

	/**
	 * Default class constructor specifying the date for the filter
	 */
	public TransportDateFilter(Calendar date) {
		this.date = date;
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
		Transport transport = (Transport) element;
		// check the transport date
		if (MyUtils.isEqualDate(transport.getDateOfTransport(), date.getTimeInMillis()))
			return true;
		// filter the element out
		return false;
	}
}
