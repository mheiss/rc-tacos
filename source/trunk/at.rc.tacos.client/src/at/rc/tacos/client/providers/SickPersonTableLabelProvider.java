package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.platform.iface.IKindOfTransport;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.SickPerson;

public class SickPersonTableLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus, IKindOfTransport
{
    //define the columns
    public static final int COLUMN_LASTNAME = 0;
    public static final int COLUMN_FIRSTNAME = 1;
    public static final int COLUMN_STREET = 2;
    public static final int COLUMN_CITY = 3;
    public static final int COLUMN_SEX = 4;
    public static final int COLUMN_SVNR = 5;
    public static final int COLUMN_TA = 6;
    public static final int COLUMN_NOTES = 7;
	
    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
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
	        case COLUMN_STREET:
	        	if(person.getStreetname() != null)
	        		return person.getStreetname();
	        	else return null;
	        case COLUMN_CITY:
	        	if(person.getCityname() != null)
	        		return person.getCityname();
	        	else return null;
	        case COLUMN_SEX:
	        	if(person.isMale())
	        		return "Frau";
	        	else
	        		return "Herr";
	        case COLUMN_SVNR:
	        	if(person.getSVNR() != null)
	        		return person.getSVNR();
	        	else return null;
	        case COLUMN_TA:
	        	if(person.getKindOfTransport() != null)
				{
					if(person.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_TRAGSESSEL))
					return "S";
					else if(person.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_KRANKENTRAGE))
						return "L";
					else if(person.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_GEHEND))
						return "G";
					else if(person.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_ROLLSTUHL))
						return "R";
					else return "";
				}
	        case COLUMN_NOTES:
	        	if(person.getNotes() != null)
	        		return person.getNotes();
	        	else return "";
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