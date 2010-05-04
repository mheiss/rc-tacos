package at.rc.tacos.client.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.platform.model.Address;

public class AddressAdminViewFilter extends ViewerFilter {

	// the criteria to filter
	private String street;
	private String city;
	private String number;

	/**
	 * Default class constructor to filter {@link Address} records
	 * 
	 * @param street
	 *            the name of the street
	 * @param city
	 *            the name of the city
	 * @param number
	 *            the streetnumber
	 */
	public AddressAdminViewFilter(String street, String city, String number) {
		this.street = street;
		this.city = city;
		this.number = number;
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
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		// cast to a address
		Address address = (Address) element;
		// check the street
		if (street != null & !street.trim().isEmpty()) {
			// check the street name
			if (!address.getStreet().toLowerCase().contains(street) & !address.getStreet().toLowerCase().startsWith(street))
				return false;
		}
		// check the city
		if (city != null & !city.trim().isEmpty()) {
			// chekc the city name
			if (!address.getCity().toLowerCase().contains(city) & !address.getCity().toLowerCase().startsWith(city))
				return false;
		}
		if (number != null & !number.trim().isEmpty()) {
			// convert to string
			String addNumber = String.valueOf(address.getZip());
			if (!addNumber.toLowerCase().contains(number) & !addNumber.toLowerCase().startsWith(number))
				return false;
		}
		// nothing matched
		return true;
	}
}
