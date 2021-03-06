package at.rc.tacos.client.ui.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Display;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.filters.AddressAdminViewFilter;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.message.GetMessage;

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
		GetMessage<Address> getMessage = new GetMessage<Address>(new Address());

		// setup the parameters
		if (strStreet != null & !strStreet.isEmpty()) {
			getMessage.addParameter(IFilterTypes.SEARCH_STRING_STREET, strStreet);
		}
		if (strCity != null & !strCity.isEmpty()) {
			getMessage.addParameter(IFilterTypes.SEARCH_STRING_CITY, strCity);
		}
		if (strZip != null & !strZip.isEmpty()) {
			getMessage.addParameter(IFilterTypes.SEARCH_STRING_ZIP, strZip);
		}

		// request the listing
		getMessage.asnchronRequest(NetWrapper.getSession());

		// assert we have a valid viewer to show the results
		if (viewer == null) {
			return Status.OK_STATUS;
		}
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
