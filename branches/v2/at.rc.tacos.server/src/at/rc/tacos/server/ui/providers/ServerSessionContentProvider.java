package at.rc.tacos.server.ui.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.server.net.manager.SessionManager;

public class ServerSessionContentProvider implements IStructuredContentProvider
{
	@Override
	public Object[] getElements(Object arg0) 
	{
		return SessionManager.getInstance().toArray();
	}

	@Override
	public void dispose() { }

	@Override
	public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
