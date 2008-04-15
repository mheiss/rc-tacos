package at.rc.tacos.client.providers;

import java.util.Calendar;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.model.RosterEntry;

/**
 * Filters out all transports that didnt't match the current date
 * @author Birgit
 */
public class PersonalDateFilter extends ViewerFilter
{
	//properties
	private int startDay;
	private int endDay;
	private int startYear;
	private int endYear;
	private int selectedDay;
	private Calendar cal;

	/**
	 * Default class constructor specifying the date for the filter
	 */
	public PersonalDateFilter()
	{
		Calendar selectedCal = Calendar.getInstance();
		selectedCal.setTimeInMillis(SessionManager.getInstance().getDisplayedDate());
		selectedDay = selectedCal.get(Calendar.DAY_OF_YEAR);

		
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
		cal = Calendar.getInstance();
		cal.setTimeInMillis(entry.getPlannedStartOfWork());
		startDay = cal.get(Calendar.DAY_OF_YEAR);
		startYear = cal.get(Calendar.YEAR);
		cal.setTimeInMillis(entry.getPlannedEndOfWork());
		endDay = cal.get(Calendar.DAY_OF_YEAR);
		endYear = cal.get(Calendar.YEAR);
		if(selectedDay == startDay || selectedDay == endDay || (selectedDay > startDay && selectedDay < endDay))
			return true;
		//for the year change
		if(startYear != endYear)
			return true;
		//filter the element out
		return false;
	}
}