package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

public class UnderwayTransportsViewLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus, IKindOfTransport, ITableFontProvider
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
    public static final int COLUMN_FZG = 12;
    public static final int COLUMN_T = 13;
    public static final int COLUMN_ERKR_VERL = 14;
	public static final int COLUMN_ANMERKUNG = 15;

	//the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

    @Override
    public Image getColumnImage(Object element, int columnIndex) 
    {
    	Transport transport = (Transport)element;
    	
    	//determine the colum and return a image if needed
		switch(columnIndex)
		{
		    case COLUMN_LOCK:
				if(lockManager.containsLock(Transport.ID, transport.getTransportId()))
					return ImageFactory.getInstance().getRegisteredImage("resource.lock18");
				else return ImageFactory.getInstance().getRegisteredImage("resource.nothing18");
		    default: return null;  
		}
    }

    @Override
    public String getColumnText(Object element, int columnIndex) 
    {
    	Transport transport = (Transport)element;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        String patient = "";
        switch(columnIndex)
        {
	        case COLUMN_PRIORITY: 
	        	if(transport.getTransportPriority().equalsIgnoreCase("A"))
	        		return "1";
	        	else if(transport.getTransportPriority().equalsIgnoreCase("B"))
	        		return "2";
	        	else if(transport.getTransportPriority().equalsIgnoreCase("C"))
	        		return "3";
	        	else if(transport.getTransportPriority().equalsIgnoreCase("D"))
	        		return "4";
	        	else if(transport.getTransportPriority().equalsIgnoreCase("E"))
	        		return "5";
	        	else if(transport.getTransportPriority().equalsIgnoreCase("F"))
	        		return "6";
	        	else if(transport.getTransportPriority().equalsIgnoreCase("G"))
	        		return "7";
	        	else return null;
	        case COLUMN_TRANSPORTNUMBER:
	        	if(transport.getTransportNumber() == Transport.TRANSPORT_NEF)
	        		return "NEF";
	        	else
	        		return String.valueOf(transport.getTransportNumber());
	        case COLUMN_TERM:if (transport.getAppointmentTimeAtDestination() != 0)
        		return sdf.format(transport.getAppointmentTimeAtDestination());
        	else return "";
	        case COLUMN_TRANSPORT_FROM:return transport.getFromStreet() +" / " +transport.getFromCity();
	        case COLUMN_PATIENT:
	        	if(transport.isAssistantPerson())
	        		patient = "+";
	        	if(transport.getPatient() != null)
        			return patient + " " +transport.getPatient().getLastname() +" " +transport.getPatient().getFirstname();
        	else return patient;
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
	        case COLUMN_FZG: 
	        	if(transport.getVehicleDetail() != null)
	        	{
	        		return transport.getVehicleDetail().getVehicleName();
	        	}
	        	else return "";
	        case COLUMN_T:
	        	if(transport.getKindOfTransport() != null)
				{
					if(transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_TRAGSESSEL))
					return "S";
					else if(transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_KRANKENTRAGE))
						return "L";
					else if(transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_GEHEND))
						return "G";
					else if(transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_ROLLSTUHL))
						return "R";
					else return "";
				}
	        	else return "";
	        case COLUMN_ERKR_VERL:
	        	if(transport.getKindOfIllness() != null)
					return transport.getKindOfIllness().getDiseaseName();
				else return "";
	        case COLUMN_ANMERKUNG:
	        	if(transport.getNotes() != null)
	        		return transport.getNotes();
	        	else return "";
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
		if(transport.getTransportPriority().equalsIgnoreCase("A"))
			return CustomColors.BACKGROUND_RED;
    	if(transport.getTransportPriority().equalsIgnoreCase("B"))
			return CustomColors.BACKGROUND_BLUE;
    	return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) 
	{      
		return null;
	}
	
	@Override
	public Font getFont(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return CustomColors.VEHICLE_TABLE;
	}
}