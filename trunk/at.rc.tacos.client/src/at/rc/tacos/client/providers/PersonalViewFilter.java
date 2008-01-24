package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.RosterEntry;

/**
 * Personal view filter for the table. <br>
 * The table shows only the entries that apply to the filter
 * @author Michael
 */
public class PersonalViewFilter extends ViewerFilter
{
	//properties
	private String station;

	/**
	 * Create a new ViewFilter object and pass the station to filter
	 * @param station the station to show
	 */
	public PersonalViewFilter(String station)
	{
		this.station = station;
	}

	/**
	 * Returns whether or not the obkect should be filtered or not.
	 * @param viewer the viewer
	 * @param parentElement the parent element
	 * @param element the element to check
	 */
	@Override
	public boolean select(Viewer arg0, Object parentElement, Object element) 
	{
		//cast the element
		RosterEntry entry = (RosterEntry)element;
		//check the entry
		if(entry.getStation().getLocationName().equalsIgnoreCase(station))
			return true;
		//filter the element out
		return false;
	}
}
