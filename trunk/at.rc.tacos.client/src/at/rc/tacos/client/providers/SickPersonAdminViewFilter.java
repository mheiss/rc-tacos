package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import at.rc.tacos.model.SickPerson;

public class SickPersonAdminViewFilter  extends ViewerFilter
{
	//the criteria to filter
	private String lastname;
	private String firstname;
	private String svnr;
	
	/**
	 * Default class constructor for the address filter.
	 * @param value the street or the city to filter
	 */
	public SickPersonAdminViewFilter(String lastname,String firstname,String svnr)
	{
		this.lastname = lastname;
		this.firstname = firstname;
		this.svnr = svnr;
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
		SickPerson person = (SickPerson)element;
		//check the street
		if(lastname != null &! lastname.trim().isEmpty())
		{
			//check the street name
			if(!person.getLastName().contains(lastname) &! person.getLastName().startsWith(lastname))
					return false;
		}
		//check the city
		if(firstname != null &! firstname.trim().isEmpty())
		{
			//chekc the city name
			if(!person.getFirstName().contains(firstname) &! person.getFirstName().startsWith(firstname))
				return false;
		}
		if(svnr != null &! svnr.trim().isEmpty())
		{
			//convert to string
			if(!person.getSVNR().contains(svnr) &! person.getSVNR().startsWith(svnr))
				return false;
		}
		//nothing matched
		return true;
	}
}
