package at.rc.tacos.server.ui.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.server.modelManager.OnlineUserManager;

/**
 * The content provider that provides the data for the view
 * @author Michael
 */
public class OnlineUserContentProvider implements IStructuredContentProvider
{
	@Override
	public Object[] getElements(Object arg0) 
	{
		return OnlineUserManager.getInstance().toArray();
	}

	@Override
	public void dispose() { }

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
