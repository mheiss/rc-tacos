package at.rc.tacos.client.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.model.SickPerson;

public class SickPersonLabelProvider implements ITableLabelProvider
{
	String sickPerson;
	
	 //define the columns
    public static final int COLUMN_LASTNAME = 0;
    public static final int COLUMN_FIRSTNAME = 1;
    public static final int COLUMN_STREET = 2;
    public static final int COLUMN_CITY = 3;
    public static final int COLUMN_SVNR = 4;
    public static final int COLUMN_NOTES = 5;
    
    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
		return null;
    }

    /**
     * Returns the text to render.
     */
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
	        case COLUMN_SVNR:
	        	if(person.getSVNR() != null)
	        		return person.getSVNR();
	        	else return null;
	        case COLUMN_NOTES:
	        	if(person.getNotes() != null)
	        		return person.getNotes();  
        }
        return null;
    }

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub
	}
}