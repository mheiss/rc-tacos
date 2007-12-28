package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.StatusMessages;
import at.rc.tacos.model.Transport;


public class JournalViewLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus
{
    //define the columns
    public static final int COLUMN_LOCK = 0;
    public static final int COLUMN_TRANSPORT_NUMBER = 1;
    public static final int COLUMN_PRIORITY = 2;
    public static final int COLUMN_TRANSPORT_FROM = 3;
    public static final int COLUMN_PATIENT = 4;
    public static final int COLUMN_TRANSPORT_TO = 5;
    public static final int COLUMN_ERKR_VERL = 6;
    public static final int COLUMN_AE = 7;
    public static final int COLUMN_S1 = 8;
    public static final int COLUMN_S2 = 9;
    public static final int COLUMN_S3 = 10;
    public static final int COLUMN_S4 = 11; 
    public static final int COLUMN_S5 = 12;  
    public static final int COLUMN_S6 = 13;
    public static final int COLUMN_FZG = 14;
    public static final int COLUMN_DRIVER = 15;
    public static final int COLUMN_PARAMEDIC_I = 16;
    public static final int COLUMN_PARAMEDIC_II = 17;
    public static final int COLUMN_CALLER_NAME = 18;

    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	Transport transport = (Transport)element;
        //determine the colum and return a image if needed
        switch(columnIndex)
        {
		    case COLUMN_PATIENT:
		    	if(transport.isAccompanyingPerson())
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
        String street;
        String city;
        
        switch(columnIndex)
        {
	        //define the columns
	        case COLUMN_LOCK: return null;
	        case COLUMN_TRANSPORT_NUMBER: return transport.getTransportNumber();
	        case COLUMN_PRIORITY: return transport.getTransportPriority();
	        case COLUMN_TRANSPORT_FROM: return transport.getFromStreet() +"/" +transport.getFromCity();
	        case COLUMN_PATIENT:
	        	if(transport.getPatient() != null)
	        		return transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname();
	        	else return "";
	        case COLUMN_TRANSPORT_TO:
	        	if(transport.getToStreet() == null)
	        		street = "";
	        	else street = transport.getToStreet();
	        	if(transport.getToCity() == null)
	        		city = "";
	        	else city = transport.getToCity();
	        	return street +"/" +city;
	        case COLUMN_ERKR_VERL:return transport.getKindOfIllness();
	        case COLUMN_AE:
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        case COLUMN_S1:
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        case COLUMN_S2:
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_AT_PATIENT).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        case COLUMN_S3:
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_START_WITH_PATIENT).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        case COLUMN_S4:
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_AT_DESTINATION).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        case COLUMN_S5:  
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_DESTINATION_FREE).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        case COLUMN_S6:
	        	if(transport.getStatusMessages().size()>0)
	        	{
		        	if(transport.getStatusMessages().get(TRANSPORT_STATUS_CAR_IN_STATION).getStatus() != 0)
		        		return sdf.format(transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus());
		        	else 
		        		return "";
	        	}
	        	else return "";
	        	
	        case COLUMN_FZG:
	        	if(transport.getVehicleDetail() != null)
	        	{
	        		return transport.getVehicleDetail().getVehicleName();
	        	}
	        	else return "";
	        case COLUMN_DRIVER:
	        	if(transport.getVehicleDetail() != null)
	        	{
		        	if(transport.getVehicleDetail().getDriverName() != null)
		        	{
		        		return transport.getVehicleDetail().getDriverName().getLastName() + " " +transport.getVehicleDetail().getDriverName().getFirstName();
		        	}
	        	}
	        	else return "";
	        case COLUMN_PARAMEDIC_I:
	        	if(transport.getVehicleDetail() != null)
	        	{
		        	if(transport.getVehicleDetail().getParamedicIName() != null)
		        	{
		        		return transport.getVehicleDetail().getParamedicIName().getLastName() + " " +transport.getVehicleDetail().getParamedicIName().getFirstName();
		        	}
		        	else return "";
	        	}
	        case COLUMN_PARAMEDIC_II:
	        	if(transport.getVehicleDetail() != null)
	        	{
		        	if(transport.getVehicleDetail().getParamedicIIName() != null)
		        	{
		        		return transport.getVehicleDetail().getParamedicIIName().getLastName() + " " +transport.getVehicleDetail().getParamedicIIName().getFirstName();
		        	}
		        	else return "";
	        	}
	        case COLUMN_CALLER_NAME:
	        	if(transport.getCallerDetail() != null)
	        	{
	        		return transport.getCallerDetail().getCallerName();
	        	}
	        	else return "";
        
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
		Transport transport = (Transport)element;
		//define the columns
		switch(columnIndex)
		{
		    case COLUMN_LOCK: return null;
		    case COLUMN_TRANSPORT_NUMBER: 
		    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
        			return CustomColors.BACKGROUND_RED;
        	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
        			return CustomColors.BACKGROUND_BLUE;
        	else return null;
		    case COLUMN_PRIORITY: 
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
		    case COLUMN_ERKR_VERL:
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
		    case COLUMN_S5:  
		    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
        			return CustomColors.BACKGROUND_RED;
        	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
        			return CustomColors.BACKGROUND_BLUE;
        	else return null;
		    case COLUMN_S6:
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
		    	
		    case COLUMN_DRIVER:
		    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
        			return CustomColors.BACKGROUND_RED;
        	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
        			return CustomColors.BACKGROUND_BLUE;
        	else return null;
		    case COLUMN_PARAMEDIC_I:
		    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
        			return CustomColors.BACKGROUND_RED;
        	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
        			return CustomColors.BACKGROUND_BLUE;
        	else return null;
		    case COLUMN_PARAMEDIC_II:
		    	if(transport.getTransportPriority().equalsIgnoreCase("A"))
        			return CustomColors.BACKGROUND_RED;
        	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
        			return CustomColors.BACKGROUND_BLUE;
        	else return null;
		    case COLUMN_CALLER_NAME:
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

