package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.model.Address;

/**
 * We use the lazy content provider to handle large amount of data.
 * @author Michael
 */
public class AddressContentProvider implements IStructuredContentProvider
{
	//properties
	private Address[] elements;

	@Override
	public Object[] getElements(Object arg0) 
	{
		return elements;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
		this.elements = (Address[]) newInput;
	}
	
	@Override
	public void dispose() {}
}
