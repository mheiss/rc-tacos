package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * This is a string based simple filter
 * @author Michael
 */
public class StringViewFilter extends ViewerFilter
{
	//properties
	private String filter;
	
	/**
	 * Default class constructor
	 */
	public StringViewFilter(String filter)
	{
		this.filter = filter.toLowerCase();
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element)  
	{
		//the value to filter
		String valueToFilter  = (String)element;
		valueToFilter = valueToFilter.toLowerCase();
		
		//filter out
		if(valueToFilter.contains(filter) || valueToFilter.startsWith(filter))
			return true;
		return false;
	}
}
