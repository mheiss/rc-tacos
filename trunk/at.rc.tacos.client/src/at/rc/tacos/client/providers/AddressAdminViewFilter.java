package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.Address;

public class AddressAdminViewFilter  extends ViewerFilter
{
	//the criteria to filter
	private String street;
	private String city;
	private String number;
	
	/**
	 * Default class constructor for the address filter.
	 * @param value the street or the city to filter
	 */
	public AddressAdminViewFilter(String street,String city,String number)
	{
		this.street = street;
		this.city = city;
		this.number = number;
	}
	
	/**
	 * Returns whether or not the object should be filtered or not.
	 * @param viewer the viewer
	 * @param parentElement the parent element
	 * @param element the element to check
	 */
	@Override
	public boolean select(Viewer arg0, Object parentElement, Object element) 
	{
		//cast to a address
		Address address = (Address)element;
		//check the street
		if(street != null &! street.trim().isEmpty())
		{
			//check the street name
			if(!address.getStreet().contains(street) &! address.getStreet().startsWith(street))
					return false;
		}
		//check the city
		if(city != null &! city.trim().isEmpty())
		{
			//chekc the city name
			if(!address.getCity().contains(city) &! address.getCity().startsWith(city))
				return false;
		}
		if(number != null &! number.trim().isEmpty())
		{
			//convert to string
			String addNumber = String.valueOf(address.getZip());
			if(!addNumber.contains(number) &! addNumber.startsWith(number))
				return false;
		}
		//nothing matched
		return true;
	}
}
