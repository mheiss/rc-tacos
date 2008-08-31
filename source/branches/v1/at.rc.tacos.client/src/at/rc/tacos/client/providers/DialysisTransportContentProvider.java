package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.client.modelManager.DialysisTransportManager;
import at.rc.tacos.client.modelManager.ModelFactory;


public class DialysisTransportContentProvider implements IStructuredContentProvider 
{
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
		DialysisTransportManager manager = ModelFactory.getInstance().getDialyseManager();
		//return the elements
		return manager.toArray();
	}
}