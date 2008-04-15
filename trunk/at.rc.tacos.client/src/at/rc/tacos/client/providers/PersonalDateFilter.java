package at.rc.tacos.client.providers;

import java.util.Calendar;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import at.rc.tacos.model.RosterEntry;

/**
 * Filters out all transports that didnt't match the current date
 * @author Birgit
 */
public class PersonalDateFilter extends ViewerFilter
{
	//properties
	private Calendar nextDay;
	private Calendar currentDay;

	/**
	 * Default class constructor specifying the date for the filter
	 */
	public PersonalDateFilter(Calendar date)
	{
		this.currentDay = date;
		//reset the hours and minutes and seconds
		this.currentDay.set(Calendar.HOUR_OF_DAY, 0);
		this.currentDay.set(Calendar.MINUTE, 0);
		this.currentDay.set(Calendar.MILLISECOND, 0);
		//the next day
		nextDay = currentDay;
		nextDay.add(Calendar.DAY_OF_MONTH, 1);
	}
	
	/**
	 * Returns whether or not the object should be filtered out.
	 * @param viewer the viewer
	 * @param parentElement the parent element
	 * @param element the element to check
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) 
	{
		//cast the element
		RosterEntry entry = (RosterEntry)element;
		//check the roster entry date
		if(entry.getPlannedStartOfWork() > currentDay.getTimeInMillis() || entry.getPlannedEndOfWork() < nextDay.getTimeInMillis())
			return true;
		//filter the element out
		return false;
	}
}