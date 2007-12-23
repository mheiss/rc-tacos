package at.rc.tacos.client.providers;


import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class DispositionViewDispLabelProvider implements ITableLabelProvider, ITableColorProvider
{
    //define the columns
    public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_PRIORITY = 1;
    public static final int COLUMN_TRANSPORTNUMBER = 2;
    public static final int COLUMN_TERM = 3;
    public static final int COLUMN_TRANSPORT_FROM = 4;
    public static final int COLUMN_PATIENT = 5;
    public static final int COLUMN_TRANSPORT_TO = 6;
    public static final int COLUMN_AE = 7;
    public static final int COLUMN_S1 = 8;
    public static final int COLUMN_S2 = 9;
    public static final int COLUMN_S3 = 10; 
    public static final int COLUMN_S4 = 11;  
    public static final int COLUMN_S7 = 12;
    public static final int COLUMN_S8 = 13;
    public static final int COLUMN_S9 = 14;
    public static final int COLUMN_FZG = 15;
    public static final int COLUMN_T = 16;
    public static final int COLUMN_ERKR_VERL = 17;

    


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

