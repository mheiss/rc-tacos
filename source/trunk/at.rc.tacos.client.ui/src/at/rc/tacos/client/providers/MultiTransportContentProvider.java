package at.rc.tacos.client.providers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.platform.model.Transport;

public class MultiTransportContentProvider implements IStructuredContentProvider 
{
	//the item list
	private List<Transport> objectList = new ArrayList<Transport>();
	
	public MultiTransportContentProvider(){}
	
	public void inputChanged(Viewer v, Object oldInput, Object newInput) 
	{
		// do nothing
	}

	public void dispose() 
	{
		// do nothing
	}

	public Object[] getElements(Object parent) 
	{
		return objectList.toArray();
	}
	
	public void addTransport(Transport transport)
	{
		objectList.add(transport);
	}
	
	public List<Transport> getObjectList()
	{
		return objectList;
	}
	
	public void removeTransport(int index)
	{
		objectList.remove(index);
	}
	
	public void removeAllTransports()
	{
		objectList.clear();
	}
}