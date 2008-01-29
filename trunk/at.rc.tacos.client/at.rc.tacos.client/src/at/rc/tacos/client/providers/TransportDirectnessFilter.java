package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.Transport;

/**
 * Prebooking views directness filter for the tables <br>
 * The table shows only the transports that applied to the filter
 * @author b.thek
 */
public class TransportDirectnessFilter extends ViewerFilter
{
	//properties
	private int directness;

	/**
	 * Create a new ViewFilter object and pass the directness to filter
	 * @param programStatus the programStatus to show
	 */
	public TransportDirectnessFilter(int directness)
	{
		this.directness = directness;
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
		System.out.println("TransportrDirectnessFilter, Richtung:::::::::::::::::::::::::::::::::::::::::::::" +transport.getDirection());
		//check the transport
		if(transport.getDirection() == directness)
			return true;
		//filter the element out
		return false;
	}
}
