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
	private int filterDay;
	private int filterMonth;
	private int filterYear;

	/**
	 * Default class constructor specifying the date for the filter
	 */
	public PersonalDateFilter(Calendar filterCalendar)
	{
		this.filterDay = filterCalendar.get(Calendar.DAY_OF_MONTH);
		this.filterMonth = filterCalendar.get(Calendar.MONTH);
		this.filterYear = filterCalendar.get(Calendar.YEAR);
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
		//the values of the roster entry
		int startDay,startMonth,startYear;
		int endDay,endMonth, endYear;
		
		RosterEntry entry = (RosterEntry)element;
		
		//init the start values
		Calendar entryCal = Calendar.getInstance();
		entryCal.setTimeInMillis(entry.getPlannedStartOfWork());
		startDay = entryCal.get(Calendar.DAY_OF_MONTH);
		startMonth = entryCal.get(Calendar.MONTH);
		startYear = entryCal.get(Calendar.YEAR);
		
		//init the end values
		entryCal.setTimeInMillis(entry.getPlannedEndOfWork());
		endDay = entryCal.get(Calendar.DAY_OF_MONTH);
		endMonth = entryCal.get(Calendar.MONTH);
		endYear = entryCal.get(Calendar.YEAR);
		
		//if we do not have a split entry then the date must match
		if(!entry.isSplitEntry())
		{
			if(startDay != filterDay)
				return false;
			if(startMonth != filterMonth)
				return false;
			if(startYear != filterYear)
				return false;
			//the dates math so show the entry
			return true;
		}
		
		//for a split entry the start and the end must match
		
		 if(filterDay == startDay || filterDay == endDay || (filterDay > startDay && filterDay < endDay))
	  	 return true;
		 
		if(filterDay != startDay && filterDay != endDay &! (filterDay > startDay && filterDay < endDay))
			return false;
		if(filterMonth != startMonth && filterMonth != endMonth)
			return false;
		if(filterYear != startYear && filterYear != endYear)
			return false;
		
		return true;
	}
}