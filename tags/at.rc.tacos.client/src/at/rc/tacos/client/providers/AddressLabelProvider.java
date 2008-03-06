package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Address;

public class AddressLabelProvider implements ITableLabelProvider
{
	//define the columns
	public static final int COLUMN_IMAGE = 0;
	public static final int COLUMN_ZIP = 1;
	public static final int COLUMN_CITY = 2;
	public static final int COLUMN_STREET = 3;

	@Override
	public Image getColumnImage(Object object, int column) 
	{
		//determine the image
		switch(column)
		{
		case COLUMN_IMAGE: return ImageFactory.getInstance().getRegisteredImage("resource.address");
		}
		return null;
	}

	@Override
	public String getColumnText(Object object, int column) 
	{
		//the entry
		Address address = (Address)object;
		//determine the image
		switch(column)
		{
		case COLUMN_ZIP: return String.valueOf(address.getZip());
		case COLUMN_CITY: return address.getCity();
		case COLUMN_STREET: return address.getStreet();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {  }

	@Override
	public void dispose() {  }

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) 
	{
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0)  { }
}
