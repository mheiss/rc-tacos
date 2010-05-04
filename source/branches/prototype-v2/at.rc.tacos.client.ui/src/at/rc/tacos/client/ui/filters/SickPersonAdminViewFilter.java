package at.rc.tacos.client.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.platform.model.SickPerson;

public class SickPersonAdminViewFilter extends ViewerFilter {

	// the criteria to filter
	private String filterLastname;
	private String filterFirstname;
	private String filterSvnr;

	/**
	 * Default class constructor for the address filter.
	 * 
	 * @param value
	 *            the street or the city to filter
	 */
	public SickPersonAdminViewFilter(String lastname, String firstname, String svnr) {
		this.filterLastname = lastname.toLowerCase();
		this.filterFirstname = firstname.toLowerCase();
		this.filterSvnr = svnr.toLowerCase();
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
		SickPerson person = (SickPerson) element;
		// setup the filter values
		String lastName = person.getLastName() != null ? person.getLastName().toLowerCase() : "";
		String firstName = person.getFirstName() != null ? person.getFirstName().toLowerCase() : "";
		String svnr = person.getSVNR() != null ? person.getSVNR().toLowerCase() : "";

		// check the last name
		if (!filterLastname.trim().isEmpty()) {
			// check the street name
			if (!lastName.contains(filterLastname) & !lastName.startsWith(filterLastname))
				return false;
		}
		// check the city
		if (!filterFirstname.isEmpty()) {
			// chekc the city name
			if (!firstName.contains(filterFirstname) & !firstName.startsWith(filterFirstname))
				return false;
		}
		if (!filterSvnr.isEmpty()) {
			// convert to string
			if (!svnr.contains(filterSvnr) & !svnr.startsWith(filterSvnr))
				return false;
		}
		// nothing matched
		return true;
	}
}
