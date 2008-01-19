package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
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
		
		//calculate time window for a possible back transport
		GregorianCalendar gcal = new GregorianCalendar();
		long now = gcal.getTimeInMillis();
		gcal.set(GregorianCalendar.HOUR_OF_DAY, GregorianCalendar.HOUR_OF_DAY-4);
		long before4Hours = gcal.getTimeInMillis();
		
		
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
		case COLUMN_TRANSPORT_FROM:
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
			{
				if(transport.isBackTransport() && ((transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE) < before4Hours)))//bug fix- change from < to >
				{
					//icon soll angezeigt werden wenn der status s5 (ziel frei) des hintransportes nicht länger als 4 stunden zurückliegt
					//TODO Problem: bug fix: this time is negative!!!
					System.out.println("------- s5: " +transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE) );
					System.out.println("----- before 4 hours: " +before4Hours);
					return ImageFactory.getInstance().getRegisteredImage("toolbar.icon.back");
				}
			}
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
        	String label="";
        	if(transport.getToStreet() != null)
        		label += transport.getToStreet();
        	if(transport.getToCity() != null)
        		label += " / "+ transport.getToCity();
        	return label;
		case COLUMN_ERKR_VERL:return transport.getKindOfIllness();
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
		case COLUMN_S5:
			//Status 5
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
			{
				cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE));
				return sdf.format(cal.getTime());
			}
			else return null;
		case COLUMN_S6:
			//Status 6 
			if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION))
			{
				cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION));
				return sdf.format(cal.getTime());
			}
			else return null;
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
		if(transport.getTransportPriority().equalsIgnoreCase("A"))
			return CustomColors.BACKGROUND_RED;
		else if(transport.getTransportPriority().equalsIgnoreCase("B"))
			return CustomColors.BACKGROUND_BLUE;
		//default 
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{      
		return null;
	}
}

