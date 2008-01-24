package at.rc.tacos.client.providers;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

public class UnderwayTransportsViewLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus
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
    	Transport transport = (Transport)element;
        //determine the colum and return a image if needed
        switch(columnIndex)
        {
		    case COLUMN_PATIENT:
		    	if(transport.isAssistantPerson())
		    		return ImageFactory.getInstance().getRegisteredImage("toolbar.icon.accPerson");
		    	else return null;
		    case COLUMN_TRANSPORT_TO:
		    	if(transport.isLongDistanceTrip())
		    			return ImageFactory.getInstance().getRegisteredImage("toolbar.icon.longtrip");
		    	else return null;
		       
		    default: return null;
        }     
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	Transport transport = (Transport)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        switch(columnIndex)
        {
	        case COLUMN_LOCK:return null;
	        case COLUMN_PRIORITY: return transport.getTransportPriority();
	        case COLUMN_TRANSPORTNUMBER:return String.valueOf(transport.getTransportNumber());
	        case COLUMN_TERM:if (transport.getAppointmentTimeAtDestination() != 0)
        		return sdf.format(transport.getAppointmentTimeAtDestination());
        	else return "";
	        case COLUMN_TRANSPORT_FROM:return transport.getFromStreet() +"/" +transport.getFromCity();
	        case COLUMN_PATIENT:
	        	if(transport.getPatient() != null)
        			return transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname();
        	else return "";
	        case COLUMN_TRANSPORT_TO:
	        	String label="";
	        	if(transport.getToStreet() != null)
	        		label += transport.getToStreet();
	        	if(transport.getToCity() != null)
	        		label += " / "+ transport.getToCity();
	        	return label;
			case COLUMN_AE:
				//Status 0 
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
					return sdf.format(cal.getTime());
				}
				else return null;
			case COLUMN_S1:
				//Status 1 
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
					return sdf.format(cal.getTime());
				}
				else return null;
			case COLUMN_S2:
				//Status 2
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
					return sdf.format(cal.getTime());
				}
				else return null;
			case COLUMN_S3:
				//Status 3
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
					return sdf.format(cal.getTime());
				}
				else return null;
			case COLUMN_S4:
				//Status 4 
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
					return sdf.format(cal.getTime());
				}
				else return null;
	        case COLUMN_S7: 
	        	//Status 7
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA));
					return sdf.format(cal.getTime());
				}
				else return null;
	        case COLUMN_S8:
	        	//Status 8
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA));
					return sdf.format(cal.getTime());
				}
				else return null;
	        case COLUMN_S9:
	        	//Status 9
				if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_OTHER))
				{
					cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_OTHER));
					return sdf.format(cal.getTime());
				}
				else return null;
	        case COLUMN_FZG: 
	        	if(transport.getVehicleDetail() != null)
	        	{
	        		return transport.getVehicleDetail().getVehicleName();
	        	}
	        	else return "";
	        case COLUMN_T: return transport.getKindOfTransport();
	        case COLUMN_ERKR_VERL:return transport.getKindOfIllness();
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
		Transport transport = (Transport)element;
		 switch(columnIndex)
	        {
				case COLUMN_LOCK:return null;
			    case COLUMN_PRIORITY:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_TRANSPORTNUMBER:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_TERM:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_TRANSPORT_FROM:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_PATIENT:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_TRANSPORT_TO:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_AE:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S1:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S2:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S3: 
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S4: 
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S7: 
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S8: 
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_S9:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_FZG: 
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;  	
			    case COLUMN_T: 
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    case COLUMN_ERKR_VERL:
			    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        			return CustomColors.BACKGROUND_RED;
			    	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        			return CustomColors.BACKGROUND_BLUE;
			    	else return null;
			    default: return null;
	        }
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{      
		return null;
	}
}

