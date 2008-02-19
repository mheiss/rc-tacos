package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;
import at.rc.tacos.client.util.CustomColors;

public class PrebookingViewLabelProvider implements ITableLabelProvider, ITableColorProvider
{
	//define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_RESP_STATION = 1;
	public static final int COLUMN_ABF = 2;
	public static final int COLUMN_AT_PATIENT = 3;
	public static final int COLUMN_TERM = 4;
	public static final int COLUMN_FROM = 5;
	public static final int COLUMN_PATIENT = 6;
	public static final int COLUMN_TO = 7;
	public static final int COLUMN_T = 8;  

	@Override
	public Image getColumnImage(Object element, int columnIndex) 
	{
		Transport transport = (Transport)element;
		//determine the colum and return a image if needed
		switch(columnIndex)
		{
		case COLUMN_LOCK: return null;
		case COLUMN_RESP_STATION: return null;
		case COLUMN_ABF:return null;
		case COLUMN_AT_PATIENT:return null;
		case COLUMN_TERM:return null;
		case COLUMN_FROM:return null;
		case COLUMN_PATIENT:
			if(transport.isAssistantPerson())
				return ImageFactory.getInstance().getRegisteredImage("transport.assistantPerson");
			else return null;
		case COLUMN_TO:
			if(transport.isLongDistanceTrip())
				return ImageFactory.getInstance().getRegisteredImage("transport.alarming.fernfahrt");
			else return null;
		case COLUMN_T:return null;
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
		case COLUMN_LOCK: return null;
		case COLUMN_RESP_STATION: return "station";//transport.getPlanedLocation().getLocationName();
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
		case COLUMN_PATIENT:
			if(transport.getPatient() != null)
				return transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname();
			else return "";
		case COLUMN_TO:
			if(transport.getToStreet() == null)
				street = "";
			else street = transport.getToStreet();
			if(transport.getToCity() == null)
				city = "";
			else city = transport.getToCity();
			return street +"/" +city;
		case COLUMN_T:return transport.getKindOfTransport();
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
		//determine the colum and return a image if needed
		if(transport.getTransportPriority().equalsIgnoreCase("A"))
			return CustomColors.BACKGROUND_RED;
		else if(transport.getTransportPriority().equalsIgnoreCase("B"))
			return CustomColors.BACKGROUND_BLUE;
		//default color, nothing
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{		
		return null;
	}
}

