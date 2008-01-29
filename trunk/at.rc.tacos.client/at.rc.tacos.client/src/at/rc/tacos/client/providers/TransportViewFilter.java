package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.Transport;

/**
 * Transport views filter for the tables <br>
 * The table shows only the transports that applied to the filter
 * @author b.thek
 */
public class TransportViewFilter extends ViewerFilter
{
	//properties
	private int programStatus;

	/**
	 * Create a new ViewFilter object and pass the programStatus to filter
	 * @param programStatus the programStatus to show
	 */
	public TransportViewFilter(int programStatus)
	{
		this.programStatus = programStatus;
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
		//cast the element
		Transport transport = (Transport)element;
		//check the transport
		if(transport.getProgramStatus() == programStatus)
			return true;
		//filter the element out
		return false;
	}
}
