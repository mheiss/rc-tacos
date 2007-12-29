package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Transport;
import at.rc.tacos.client.util.CustomColors;

public class DialysisTransportLabelProvider implements ITableLabelProvider, ITableColorProvider
{
    //define the columns
    public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_ABF = 1;
    public static final int COLUMN_AT_PATIENT = 2;
    public static final int COLUMN_TERM = 3;
    public static final int COLUMN_ABF_RT = 4;
    public static final int COLUMN_READY_FOR_BACKTRANSPORT =5;
    public static final int COLUMN_FROM = 6;
    public static final int COLUMN_PATIENT = 7;
    public static final int COLUMN_MO = 8;
    public static final int COLUMN_DI = 9;
    public static final int COLUMN_MI = 10;
    public static final int COLUMN_DO = 11;
    public static final int COLUMN_FR = 12;
    public static final int COLUMN_SA = 13;
    public static final int COLUMN_SO = 14;
    public static final int COLUMN_STAT = 15;
    
    


    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	DialysisPatient dia = (DialysisPatient)element;
        
        switch(columnIndex)
        {
	        case COLUMN_LOCK: return null;
	        case COLUMN_ABF:
	        case COLUMN_AT_PATIENT:
	        case COLUMN_TERM:
	        case COLUMN_ABF_RT:	        	
	        case COLUMN_READY_FOR_BACKTRANSPORT:
	        	
	        case COLUMN_FROM:
	        case COLUMN_PATIENT:
	        case COLUMN_STAT:
	        	if(dia.isStationary())
	        		return ImageFactory.getInstance().getRegisteredImage("toolbar.icon.stationary");
	        	else return null;
	        	
	        	//TODO!!!!!!!!!!!!!!!!!!
	        case COLUMN_MO:
//	        	if(dia.Montag && !(dia.isStationary()))
//	        		return ImageFactory.getInstance().getRegisteredImage("toolbar.icon.yes");
//	        	else if(dia.Montag && (dia.isStationary()))
	        		return ImageFactory.getInstance().getRegisteredImage("toolbar.icon.no");
	        case COLUMN_DI:
	        case COLUMN_MI:
	        case COLUMN_DO:
	        case COLUMN_FR:
	        case COLUMN_SA:
	        case COLUMN_SO:
	       
	        default: return null;
        }
    	
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	DialysisPatient dia = (DialysisPatient)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        
        
        switch(columnIndex)
        {
	        case COLUMN_LOCK: return null;

	        case COLUMN_ABF:
	        	if (dia.getPlannedStartOfTransport() != 0)
	        		return sdf.format(dia.getPlannedStartOfTransport());
	        	else 
	        		return "";
	        case COLUMN_AT_PATIENT:
	        	if (dia.getPlannedTimeAtPatient() != 0)
	        		return sdf.format(dia.getPlannedTimeAtPatient());
	        	else
	        		return "";
	        case COLUMN_TERM:
	        	if (dia.getAppointmentTimeAtDialysis() != 0)
	        		return sdf.format(dia.getAppointmentTimeAtDialysis());
	        	else return "";
	        case COLUMN_ABF_RT:
	        	if (dia.getPlannedStartOfTransport() != 0)
	        		return sdf.format(dia.getplannedStartForBackTransport());
	        	else return "";
	        	
	        case COLUMN_READY_FOR_BACKTRANSPORT:
	        	if (dia.getreadyTime() != 0)
	        		return sdf.format(dia.getreadyTime());
	        	else return "";
	        case COLUMN_FROM:return dia.getFromStreet() +"/" +dia.getFromCity();
	        case COLUMN_PATIENT:
	        	if(dia.getLastname() != null)
	        			return dia.getLastname() +" " +dia.getFirstname();
	        	else return "";
	        case COLUMN_STAT:
	        	return null;
	        
	        
	       
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

