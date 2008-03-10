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

public class JournalViewLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus
{
	//define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COlUMN_OS = 1;
	public static final int COLUMN_TRANSPORT_NUMBER = 2;
	public static final int COLUMN_PRIORITY = 3;
	public static final int COLUMN_TRANSPORT_FROM = 4;
	public static final int COLUMN_PATIENT = 5;
	public static final int COLUMN_TRANSPORT_TO = 6;
	public static final int COLUMN_ERKR_VERL = 7;
	public static final int COLUMN_AE = 8;
	public static final int COLUMN_S1 = 9;
	public static final int COLUMN_S2 = 10;
	public static final int COLUMN_S3 = 11;
	public static final int COLUMN_S4 = 12; 
	public static final int COLUMN_S5 = 13;  
	public static final int COLUMN_S6 = 14;
	public static final int COLUMN_FZG = 15;
	public static final int COLUMN_DRIVER = 16;
	public static final int COLUMN_PARAMEDIC_I = 17;
	public static final int COLUMN_PARAMEDIC_II = 18;
	public static final int COLUMN_CALLER_NAME = 19;

	@Override
	public Image getColumnImage(Object element, int columnIndex) 
	{
		Transport transport = (Transport)element;

		//determine the colum and return a image if needed
		switch(columnIndex)
		{
		case COLUMN_PATIENT:
			if(transport.isAssistantPerson())
				return ImageFactory.getInstance().getRegisteredImage("transport.assistantPerson");
			else return null;
		case COLUMN_TRANSPORT_TO:
			if(transport.isLongDistanceTrip())
				return ImageFactory.getInstance().getRegisteredImage("transport.alarming.fernfahrt");
			else return null;
		case COLUMN_TRANSPORT_FROM:
			//return when we do not have the status destination free
			if(!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
				return null;
			//display image only on a backtransport
			if(!transport.isBackTransport())
				return null;

			//the time of the s5 status 
			long s5Time = transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE);
			//calculate time window for a possible back transport
			Calendar befor4Hours = Calendar.getInstance();
			befor4Hours.add(Calendar.HOUR_OF_DAY, -4);
			if(befor4Hours.getTimeInMillis() >= s5Time)
				return ImageFactory.getInstance().getRegisteredImage("transport.backtransport");
			//no image
			return null;
			//default image -> none
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
		case COlUMN_OS: 
			if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Kapfenberg"))
				return "KA";
			else if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Bruck an der Mur"))
				return "BM";
			else if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("St. Marein"))
				return "MA";
			else if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Breitenau"))
				return "BR";
			else if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Thörl"))
				return "TH";
			else if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Turnau"))
				return "TU";
			else if(transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Bezirk: Bruck - Kapfenberg"))
				return "BE";
			else return null;
		case COLUMN_TRANSPORT_NUMBER: 
			if(transport.getTransportNumber()== Transport.TRANSPORT_CANCLED)
				return "STORNO";
			else if(transport.getTransportNumber() == Transport.TRANSPORT_FORWARD)
				return "WTGL";
			else if(transport.getTransportNumber() == Transport.TRANSPORT_NEF)
				return "NEF";
			else
				return String.valueOf(transport.getTransportNumber());
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
		case COLUMN_ERKR_VERL:
			if(transport.getKindOfIllness() != null)
				return transport.getKindOfIllness().getDiseaseName();
			else return "";
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
				if(transport.getVehicleDetail().getDriver() != null)
				{
					return transport.getVehicleDetail().getDriver().getLastName() + " " +transport.getVehicleDetail().getDriver().getFirstName();
				}
			}
			else return "";
		case COLUMN_PARAMEDIC_I:
			if(transport.getVehicleDetail() != null)
			{
				if(transport.getVehicleDetail().getFirstParamedic() != null)
				{
					return transport.getVehicleDetail().getFirstParamedic().getLastName() + " " +transport.getVehicleDetail().getFirstParamedic().getFirstName();
				}
				else return "";
			}
		case COLUMN_PARAMEDIC_II:
			if(transport.getVehicleDetail() != null)
			{
				if(transport.getVehicleDetail().getSecondParamedic() != null)
				{
					return transport.getVehicleDetail().getSecondParamedic().getLastName() + " " +transport.getVehicleDetail().getSecondParamedic().getFirstName();
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