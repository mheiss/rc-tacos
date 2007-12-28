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
import at.rc.tacos.model.Transport;
import at.rc.tacos.client.util.CustomColors;

public class OutstandingTransportsViewLabelProvider implements ITableLabelProvider, ITableColorProvider
{
    //define the columns
    public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_PRIORITY = 1;
    public static final int COLUMN_RESP_STATION = 2;
    public static final int COLUMN_ABF = 3;
    public static final int COLUMN_AT_PATIENT = 4;
    public static final int COLUMN_TERM = 5;
    public static final int COLUMN_FROM = 6;
    public static final int COLUMN_PATIENT = 7;
    public static final int COLUMN_TO = 8;
    public static final int COLUMN_AUFG = 9;
    public static final int COLUMN_T = 10;  
    public static final int COLUMN_ERKR_VERL = 11;
    public static final int COLUMN_NOTES = 12;
    


    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	Transport transport = (Transport)element;
        //determine the colum and return a image if needed
        switch(columnIndex)
        {
        
	        case COLUMN_LOCK: return null;
	        case COLUMN_PRIORITY: return null;
	        case COLUMN_RESP_STATION: return null;
	        case COLUMN_ABF:return null;
	        case COLUMN_AT_PATIENT:return null;
	        case COLUMN_TERM:return null;
	        case COLUMN_FROM:return null;
	        case COLUMN_PATIENT:return null;
	        case COLUMN_TO:return null;
	        case COLUMN_AUFG:return null;
	        case COLUMN_T:return null;
	        case COLUMN_ERKR_VERL:return null;
	        case COLUMN_NOTES:return null;
	        default: return null;
        }
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	Transport transport = (Transport)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        switch(columnIndex)
        {
	        case COLUMN_LOCK: return null;
	        case COLUMN_PRIORITY: return transport.getTransportPriority();
	        case COLUMN_RESP_STATION: return transport.getResponsibleStation();
	        case COLUMN_ABF:
	        	if (transport.getPlannedStartOfTransport() != 0)
	        		return sdf.format(transport.getPlannedStartOfTransport());
	        	else 
	        		return "";
	        case COLUMN_AT_PATIENT:
	        	if (transport.getPlannedTimeAtPatient() != 0)
	        		return sdf.format(transport.getPlannedTimeAtPatient());
	        	else
	        		return "";
	        case COLUMN_TERM:
	        	if (transport.getAppointmentTimeAtDestination() != 0)
	        		return sdf.format(transport.getAppointmentTimeAtDestination());
	        	else return "";
	        case COLUMN_FROM:return transport.getFromStreet() +"/" +transport.getFromCity();
	        case COLUMN_PATIENT:return transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname();
	        case COLUMN_TO:return transport.getToStreet() +"/" +transport.getToCity();
	        case COLUMN_AUFG:
	        	if (transport.getReceiveTime() != 0)
	        		return sdf.format(transport.getReceiveTime());
	        	else return "";
	        case COLUMN_T:return transport.getKindOfTransport();
	        case COLUMN_ERKR_VERL:return transport.getKindOfIllness();
	        case COLUMN_NOTES:return transport.getDiseaseNotes();
	        default: return null;
        }
        
        
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
		return CustomColors.GREY_COLOR;
	}
}

