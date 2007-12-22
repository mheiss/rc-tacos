package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.client.util.CustomColors;

public class DispositionViewOffLabelProvider implements ITableLabelProvider, ITableColorProvider
{
    //define the columns
    public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_PRIORITY = 1;
    public static final int COLUMN_STATION = 2;
    public static final int COLUMN_ABF = 3;
    public static final int COLUMN_ANK = 4;
    public static final int COLUMN_TERM = 5;
    public static final int COLUMN_TRANSPORT_FROM = 6;
    public static final int COLUMN_PATIENT = 7;
    public static final int COLUMN_TRANSPORT_TO = 8;
    public static final int COLUMN_AUFG = 9;
    public static final int COLUMN_T = 10; 
    public static final int COLUMN_ERKR_VERL = 11;  
    public static final int COLUMN_ANMERKUNG = 12;


    


    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
        return null;
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
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
	public Color getBackground(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{      
		return null;
	}
}

