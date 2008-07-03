package at.rc.tacos.client.providers;

import java.util.Calendar;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.Transport;
import at.rc.tacos.util.MyUtils;

/**
 * Filters out all transports that didnt't match the current date
 * @author Michael
 */
public class TransportDateFilter extends ViewerFilter
{
	//properties
	private Calendar date;
	
	/**
	 * Default class constructor specifying the date for the filter
	 */
	public TransportDateFilter(Calendar date)
	{
		this.date = date;
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
		Transport transport = (Transport)element;
		//check the transport date
		if(MyUtils.isEqualDate(transport.getDateOfTransport(), date.getTimeInMillis()))
			return true;
		//filter the element out
		return false;
	}
}