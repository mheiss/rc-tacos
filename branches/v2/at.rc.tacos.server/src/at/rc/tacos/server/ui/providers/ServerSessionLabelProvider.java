package at.rc.tacos.server.ui.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.model.Session;
import at.rc.tacos.util.MyUtils;

public class ServerSessionLabelProvider implements ITableLabelProvider
{
	//define the columns
	public static final int COLUMN_SERVERNAME = 0;
	public static final int COLUMN_ONLINE = 1;
	public static final int COLUMN_IP = 2;

	@Override
	public String getColumnText(Object object, int column) 
	{
		//cast to to a online user
		Session session = (Session)object;
		switch(column)
		{
		case COLUMN_SERVERNAME: return session.getName();
		case COLUMN_ONLINE: return MyUtils.timestampToString(session.getOnlineSince(), MyUtils.timeAndDateFormat);
		case COLUMN_IP: return session.getSocket().getInetAddress().getCanonicalHostName();
		}
		//no column matched
		return null;
	}

	@Override
	public Image getColumnImage(Object object, int column) 
	{
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) { }

	@Override
	public void dispose() { }

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) 
	{ 
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) { }
}
