package at.rc.tacos.server.ui.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import at.rc.tacos.model.Session;

public class ClientFilter extends ViewerFilter
{
	@Override
	public boolean select(Viewer viewer, Object parent, Object object) 
	{
		Session session = (Session)object;
		//check if the session is a client
		if(session.getType() == Session.SESSION_SERVER)
			return false;

		return true;
	}
}
