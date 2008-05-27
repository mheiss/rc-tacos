package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.SickPerson;

public class SickPersonAdminTableLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus, IKindOfTransport
{
    //define the columns
	public static final int COLUMN_IMAGE = 0;
    public static final int COLUMN_LASTNAME = 2;
    public static final int COLUMN_FIRSTNAME = 3;
    public static final int COLUMN_SVNR = 1;
	
    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	//determine the image
		switch(columnIndex)
		{
		case COLUMN_IMAGE: return ImageFactory.getInstance().getRegisteredImage("admin.patient");
		}
		return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	SickPerson person = (SickPerson)element;
        
        switch(columnIndex)
        {
	        case COLUMN_LASTNAME: 
	        	if(person.getLastName() != null)
	        		return person.getLastName();
	        	else return null;
	        case COLUMN_FIRSTNAME:
	        	if(person.getFirstName() != null)
	        		return person.getFirstName();
	        	else return null;
	        case COLUMN_SVNR:
	        	if(person.getSVNR() != null)
	        		return person.getSVNR();
	        	else return null;
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

	@Override
	public Color getBackground(Object element, int columnIndex) 
	{
    	return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{      
		return null;
	}
}