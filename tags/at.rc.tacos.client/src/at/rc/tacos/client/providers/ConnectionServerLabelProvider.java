package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.core.net.internal.IServerInfo;
import at.rc.tacos.core.net.internal.ServerInfo;
import at.rc.tacos.factory.ImageFactory;

public class ConnectionServerLabelProvider extends LabelProvider implements ITableLabelProvider
{
    //define the columns
    public static final int COLUMN_SERVER = 0;

    /**
     * Returns the image to use
     */
	@Override
	public Image getColumnImage(Object element, int columnIndex) 
	{
		//determine the symbol
		switch(columnIndex)
		{
		case COLUMN_SERVER: 
			return ImageFactory.getInstance().getRegisteredImage("wizard.server.server");
		default: 
			return null;
		}
	}

	/**
	 * Returns the text to render
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) 
	{
		//server info
		ServerInfo info = (ServerInfo)element;
		//determine the symbol
		switch(columnIndex)
		{
		case COLUMN_SERVER:
			String description = info.getDescription() + " ("+info.getHostName()+":"+info.getPort()+")";
			if(info.getId() == IServerInfo.PRIMARY_SERVER)
				description += " (empfohlen)";
			return description;
		default: 
			return "";
		}
	}
}
