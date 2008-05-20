package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Transport;

public class DialysisTransportLabelProvider implements ITableLabelProvider, ITableColorProvider
{
	//define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_STATION = 1;
	public static final int COLUMN_ABF = 2;
	public static final int COLUMN_AT_PATIENT = 3;
	public static final int COLUMN_TERM = 4;
	public static final int COLUMN_ABF_RT = 5;
	public static final int COLUMN_READY_FOR_BACKTRANSPORT =6;
	public static final int COLUMN_FROM = 7;
	public static final int COLUMN_PATIENT = 8;
	public static final int COLUMN_TO = 9;
	public static final int COLUMN_MO = 10;
	public static final int COLUMN_DI = 11;
	public static final int COLUMN_MI = 12;
	public static final int COLUMN_DO = 13;
	public static final int COLUMN_FR = 14;
	public static final int COLUMN_SA = 15;
	public static final int COLUMN_SO = 16;
	public static final int COLUMN_TA = 17;
	public static final int COLUMN_STAT = 18;

	//the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	@Override
	public Image getColumnImage(Object element, int columnIndex) 
	{
		DialysisPatient dia = (DialysisPatient)element;

		switch(columnIndex)
		{
			case COLUMN_LOCK:
				if(lockManager.containsLock(DialysisPatient.ID, dia.getId()))
		    		return ImageFactory.getInstance().getRegisteredImage("resource.lock");
				else return null;
			case COLUMN_STAT:
				if(dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("resource.location");
				return null;
			case COLUMN_MO:
				if(dia.isMonday() &! dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
			case COLUMN_DI:
				if(dia.isTuesday() &!dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
			case COLUMN_MI:
				if(dia.isWednesday() &!dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
			case COLUMN_DO:
				if(dia.isThursday() &! dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
			case COLUMN_FR:
				if(dia.isFriday() &! dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
			case COLUMN_SA:
				if(dia.isSaturday() &! dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
			case COLUMN_SO:
				if(dia.isSunday() &! dia.isStationary())
					return ImageFactory.getInstance().getRegisteredImage("transport.ok");
				return null;
				//no image by default
			default:
				return null;
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
		case COLUMN_STATION: return dia.getLocation().getLocationName();
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
				return sdf.format(dia.getPlannedStartForBackTransport());
			else return "";
		case COLUMN_READY_FOR_BACKTRANSPORT:
			if (dia.getReadyTime() != 0)
				return sdf.format(dia.getReadyTime());
			else return "";
		case COLUMN_FROM: return dia.getFromStreet() +"/" +dia.getFromCity();
		case COLUMN_PATIENT:
			if(dia.getPatient() != null)
				return dia.getPatient().getLastname() +" " +dia.getPatient().getFirstname();
			else return "";
		case COLUMN_TO: return dia.getToStreet() + "/"+ dia.getToCity();
		case COLUMN_TA:
			if(dia.getKindOfTransport() != null)
				return dia.getKindOfTransport();
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

