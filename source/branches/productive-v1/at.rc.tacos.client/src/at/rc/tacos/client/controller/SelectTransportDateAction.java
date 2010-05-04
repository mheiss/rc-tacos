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

import org.eclipse.jface.action.Action;

import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.Transport;

/**
 * This class will handle the selection events for the calendar and query the
 * database for transports.
 * 
 * @author b.thek
 */
public class SelectTransportDateAction extends Action {

	private Calendar cal;

	/**
	 * Default class constructor for an action.
	 * 
	 * @param date
	 *            the date to switch to
	 */
	public SelectTransportDateAction(Calendar cal) {
		// mask the unused fields
		this.cal = cal;
	}

	@Override
	public void run() {
		// Notify the listeners that the date changed and the view filters must
		// be updated
		ModelFactory.getInstance().getTransportManager().fireTransportViewFilterChanged(cal);

		// format the date
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		String strDate = sdf.format(cal.getTime());

		// set up the filter and query the server
		QueryFilter filter = new QueryFilter(IFilterTypes.DATE_FILTER, strDate);
		// query transports in progress and journal transports
		NetWrapper.getDefault().requestListing(Transport.ID, filter);
	}
}
