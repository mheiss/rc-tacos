package at.rc.tacos.client.ui.filters;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.platform.model.SickPerson;

public class PatientViewFilter extends ViewerFilter
{
	//the filter property
	private String filterText;
	
	/**
	 * Default class constructor
	 */
	public PatientViewFilter(String filterText)
	{
		this.filterText = filterText;
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
		//cast to a patient
		SickPerson person = (SickPerson)element;
		
		//assert valid filter
		if(filterText != null &!filterText.trim().isEmpty())
		{
			//check the lastname
			String lastName = person.getLastName().toLowerCase();
			if(lastName.contains(filterText) || lastName.startsWith(filterText))
				return true;
			//chec the first name
			String firstName = person.getFirstName().toLowerCase();
			if(firstName.contains(filterText) || firstName.startsWith(filterText))
				return true;
		}
		//nothing matched, filter out
		return false;
	}
}
