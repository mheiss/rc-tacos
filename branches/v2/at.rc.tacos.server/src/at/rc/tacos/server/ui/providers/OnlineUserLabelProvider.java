package at.rc.tacos.server.ui.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.OnlineUser;
import at.rc.tacos.util.MyUtils;

/**
 * The label provider defines how the online user view information will be displayed
 * @author Michael
 */
public class OnlineUserLabelProvider implements ITableLabelProvider
{
	//define the columns
	public static final int COLUMN_IMAGE = 0;
	public static final int COLUMN_USER = 1;
	public static final int COLUMN_ONLINE = 2;

	@Override
	public Image getColumnImage(Object object, int column) 
	{
		//cast to to a online user
		OnlineUser user = (OnlineUser)object;
		switch(column)
		{
		//show a image depending on the connection type
		case COLUMN_IMAGE: 
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
		OnlineUser user = (OnlineUser)object;
		switch(column)
		{
		case COLUMN_USER: return user.getLogin().getUsername();
		case COLUMN_ONLINE: return MyUtils.timestampToString(user.getOnlineSince(), MyUtils.dateFormat);
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
