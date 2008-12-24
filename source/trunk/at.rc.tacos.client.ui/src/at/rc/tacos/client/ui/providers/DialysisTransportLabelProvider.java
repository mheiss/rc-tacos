package at.rc.tacos.client.ui.providers;

import java.text.SimpleDateFormat;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.DialysisPatient;

public class DialysisTransportLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_STATION = 1;
	public static final int COLUMN_ABF = 2;
	public static final int COLUMN_AT_PATIENT = 3;
	public static final int COLUMN_TERM = 4;
	public static final int COLUMN_ABF_RT = 5;
	public static final int COLUMN_READY_FOR_BACKTRANSPORT = 6;
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

	// the image registry
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		DialysisPatient dia = (DialysisPatient) element;
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (dia.isLocked())
					return imageRegistry.get("resource.lock");
				else
					return null;
			case COLUMN_STAT:
				if (dia.isStationary())
					return imageRegistry.get("resource.location");
				return null;
			case COLUMN_MO:
				if (dia.isMonday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			case COLUMN_DI:
				if (dia.isTuesday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			case COLUMN_MI:
				if (dia.isWednesday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			case COLUMN_DO:
				if (dia.isThursday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			case COLUMN_FR:
				if (dia.isFriday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			case COLUMN_SA:
				if (dia.isSaturday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			case COLUMN_SO:
				if (dia.isSunday() & !dia.isStationary())
					return imageRegistry.get("transport.ok");
				return null;
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		DialysisPatient dia = (DialysisPatient) element;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		switch (columnIndex) {
			case COLUMN_LOCK:
				return null;
			case COLUMN_STATION:
				return dia.getLocation().getLocationName();
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
				else
					return "";
			case COLUMN_ABF_RT:
				if (dia.getPlannedStartOfTransport() != 0)
					return sdf.format(dia.getPlannedStartForBackTransport());
				else
					return "";
			case COLUMN_READY_FOR_BACKTRANSPORT:
				if (dia.getReadyTime() != 0)
					return sdf.format(dia.getReadyTime());
				else
					return "";
			case COLUMN_FROM:
				return dia.getFromStreet() + " / " + dia.getFromCity();
			case COLUMN_PATIENT:
				if (dia.getPatient() != null)
					return dia.getPatient().getLastname() + " " + dia.getPatient().getFirstname();
				else
					return "";
			case COLUMN_TO:
				return dia.getToStreet() + " / " + dia.getToCity();
			case COLUMN_TA:
				if (dia.getKindOfTransport() != null)
					return dia.getKindOfTransport();
				else
					return "";
			case COLUMN_STAT:
				return null;
			default:
				return null;
		}
	}
}
