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

import org.apache.commons.lang.time.DateUtils;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.RosterEntry;

/**
 * Filters out all transports that didnt't match the current date
 * 
 * @author Birgit
 */
public class PersonalDateFilter extends ViewerFilter {

	private Calendar filter;

	/**
	 * Default class constructor specifying the date for the filter
	 */
	public PersonalDateFilter(Calendar filter) {
		this.filter = DateUtils.truncate(filter, Calendar.DAY_OF_MONTH);
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
		RosterEntry entry = (RosterEntry) element;

		// init the start values
		Calendar start = Calendar.getInstance();
		start.setTimeInMillis(entry.getPlannedStartOfWork());
		start = DateUtils.truncate(start, Calendar.DAY_OF_MONTH);

		// init the end values
		Calendar end = Calendar.getInstance();
		end.setTimeInMillis(entry.getPlannedEndOfWork());
		end = DateUtils.truncate(end, Calendar.DAY_OF_MONTH);

		// if we do not have a split entry then the date must match
		if (!entry.isSplitEntry()) {
			return DateUtils.isSameDay(filter, start);
		}

		// if the entry is split up over more than one day, then check if the
		// filter date is between
		if (filter.before(start)) {
			return false;
		}
		if (filter.after(end)) {
			return false;
		}

		return true;
	}
}
