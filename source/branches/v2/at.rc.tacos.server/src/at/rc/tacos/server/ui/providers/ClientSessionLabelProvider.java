package at.rc.tacos.server.ui.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Session;
import at.rc.tacos.util.MyUtils;

/**
 * The label provider defines how the online user view information will be displayed
 * @author Michael
 */
public class ClientSessionLabelProvider implements ITableLabelProvider
{
	//define the columns
	public static final int COLUMN_IMAGE = 0;
	public static final int COLUMN_USER = 1;
	public static final int COLUMN_ONLINE = 2;
	public static final int COLUMN_IP = 3;

	@Override
	public Image getColumnImage(Object object, int column) 
	{
		//cast to to a online user
		Session user = (Session)object;
		switch(column)
		{
		//show a image depending on the connection type
		case COLUMN_IMAGE: 
			//check if the user is authenticated
			if(!user.isAuthenticated())
				return null;
			//return the web image or the control center image
			if(user.getLogin().isWebClient())
				return ImageFactory.getInstance().getRegisteredImage("server.user.userControlCenterOnline");
			else
				return ImageFactory.getInstance().getRegisteredImage("server.user.userWebOnline");
		}

		//no column matched
		return null;
	}
	
	@Override
	public String getColumnText(Object object, int column) 
	{
		//cast to to a online user
		Session user = (Session)object;
		switch(column)
		{
		case COLUMN_USER: return user.getUsername();
		case COLUMN_ONLINE: return MyUtils.timestampToString(user.getOnlineSince(), MyUtils.timeAndDateFormat);
		case COLUMN_IP: return user.getSocket().getInetAddress().getCanonicalHostName();
		}
		//no column matched
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
