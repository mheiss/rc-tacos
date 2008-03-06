package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import at.rc.tacos.core.net.NetSource;

/**
 * Content provider for the server selection in the connection wizard
 * @author Michael
 */
public class ConnectionServerContentProvider implements IStructuredContentProvider
{
	/**
	 * Returns the elements to display
	 */
	public Object[] getElements(Object arg0) 
	{
		return NetSource.getInstance().getServerList().toArray();
	}

	/**
	 * Cleanup the content provider
	 */
	public void dispose() { }

	public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
}
