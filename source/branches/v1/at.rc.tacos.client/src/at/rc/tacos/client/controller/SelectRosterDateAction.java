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
package at.rc.tacos.client.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;

/**
 * This class will handle the selection events for the calendar and query the
 * database for roster entries.
 * 
 * @author Michael
 */
public class SelectRosterDateAction extends Action {

	private Date date;

	/**
	 * Default class constructor for an action.
	 * 
	 * @param date
	 *            the dateime to switch to
	 */
	public SelectRosterDateAction(Date date) {
		// mask the unused fields
		this.date = date;
	}

	@Override
	public void run() {
		// Notify the listeners that the date changed and the view filters must
		// be updated
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		ModelFactory.getInstance().getRosterEntryManager().fireRosterViewFilterChanged(cal);

		// save the date
		SessionManager.getInstance().setDisplayedDate(date.getTime());

		// format the date
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = sdf.format(date);

		// set up the filter and query the server
		QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER, strDate);
		NetWrapper.getDefault().requestListing(DayInfoMessage.ID, filter);
		NetWrapper.getDefault().requestListing(RosterEntry.ID, filter);
	}
}
