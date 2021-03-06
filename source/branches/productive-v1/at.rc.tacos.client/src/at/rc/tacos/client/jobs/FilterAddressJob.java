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
package at.rc.tacos.client.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.providers.AddressAdminViewFilter;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Address;
import at.rc.tacos.model.QueryFilter;

/**
 * The filter job to execute the query of the address data
 */
public class FilterAddressJob extends Job {

	// the properties
	private String strStreet, strCity, strZip;
	private StructuredViewer viewer;

	/**
	 * The time in milliseconds between two keystrokes
	 */
	public static final int INTERVAL_KEY_PRESSED = 1000;

	/**
	 * Default class construcotor
	 * 
	 * @param viewer
	 *            the viewer to update and apply the filter
	 */
	public FilterAddressJob(StructuredViewer viewer) {
		super("filterAddressJob");
		this.viewer = viewer;
		strStreet = new String("");
		strCity = new String("");
		strZip = new String("");
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// assert valid text
		if (strStreet.length() < 1 && strCity.length() < 1)
			return Status.OK_STATUS;

		// setup the filter to send to the server
		QueryFilter queryFilter = new QueryFilter();
		// add the filter value if at least on char is entered
		if (strStreet != null & !strStreet.isEmpty())
			queryFilter.add(IFilterTypes.SEARCH_STRING_STREET, strStreet);
		if (strCity != null & !strCity.isEmpty())
			queryFilter.add(IFilterTypes.SEARCH_STRING_CITY, strCity);
		if (strZip != null & !strZip.isEmpty())
			queryFilter.add(IFilterTypes.SEARCH_STRING_ZIP, strZip);

		// send a request to the server to list all matching address records
		NetWrapper.getDefault().requestListing(Address.ID, queryFilter);

		// assert we have a valid viewer to show the results
		if (viewer != null) {
			// apply the filter
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					// get the values and create the filter
					viewer.resetFilters();
					// create new filter and apply
					AddressAdminViewFilter filter = new AddressAdminViewFilter(strStreet, strCity, strZip);
					viewer.addFilter(filter);
				}
			});
		}

		return Status.OK_STATUS;
	}

	/**
	 * @param strStreet
	 *            the strStreet to set
	 */
	public void setStrStreet(String strStreet) {
		this.strStreet = strStreet.trim().toLowerCase();
	}

	/**
	 * @param strCity
	 *            the strCity to set
	 */
	public void setStrCity(String strCity) {
		this.strCity = strCity.trim().toLowerCase();
	}

	/**
	 * @return the strZip
	 */
	public void setStrZip(String strZip) {
		this.strZip = strZip.trim().toLowerCase();
	}
}
