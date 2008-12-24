package at.rc.tacos.client.ui.providers;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.iface.IKindOfTransport;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Transport;

public class UnderwayTransportsViewLabelProvider extends BaseLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

	// define the columns
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

	// the image registry
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (transport.isLocked())
					return imageRegistry.get("resource.lock");
				return imageRegistry.get("image.empty24");
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar calendar = Calendar.getInstance();
		switch (columnIndex) {
			case COLUMN_PRIORITY:
				// setup the priority string
				String beforePriority = transport.isBlueLight1() ? "!" : "";
				String afterPriority = transport.isBlueLightToGoal() ? "!" : "";

				if (transport.getTransportPriority().equalsIgnoreCase("A"))
					return beforePriority + "1" + afterPriority;
				if (transport.getTransportPriority().equalsIgnoreCase("B"))
					return beforePriority + "2" + afterPriority;
				if (transport.getTransportPriority().equalsIgnoreCase("C"))
					return beforePriority + "3" + afterPriority;
				if (transport.getTransportPriority().equalsIgnoreCase("D"))
					return beforePriority + "4" + afterPriority;
				if (transport.getTransportPriority().equalsIgnoreCase("E"))
					return beforePriority + "5" + afterPriority;
				if (transport.getTransportPriority().equalsIgnoreCase("F"))
					return beforePriority + "6" + afterPriority;
				if (transport.getTransportPriority().equalsIgnoreCase("G"))
					return beforePriority + "7" + afterPriority;
				return beforePriority + "?" + afterPriority;
			case COLUMN_TRANSPORTNUMBER:
				if (transport.getTransportNumber() == Transport.TRANSPORT_NEF) {
					return "NEF";
				}
				return String.valueOf(transport.getTransportNumber());
			case COLUMN_TERM:
				if (transport.getAppointmentTimeAtDestination() != 0) {
					return sdf.format(transport.getAppointmentTimeAtDestination());
				}
				return null;
			case COLUMN_TRANSPORT_FROM:
				return transport.getFromStreet() + " / " + transport.getFromCity();
			case COLUMN_PATIENT:
				String patient = transport.isAssistantPerson() ? "+" : "";
				if (transport.getPatient() != null) {
					return patient + " " + transport.getPatient().getLastname() + " " + transport.getPatient().getFirstname();
				}
				return patient;
			case COLUMN_TRANSPORT_TO:
				String label = "";
				if (transport.getToStreet() != null) {
					label += transport.getToStreet();
				}
				if (transport.getToCity() != null) {
					label += " / " + transport.getToCity();
				}
				return label;
			case COLUMN_AE:
				// Status 0
				if (!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED)) {
					return null;
				}
				calendar.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
				return sdf.format(calendar.getTime());
			case COLUMN_S1:
				// Status 1
				if (!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY)) {
					return null;
				}
				calendar.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
				return sdf.format(calendar.getTime());
			case COLUMN_S2:
				// Status 2
				if (!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT)) {
					return null;
				}
				calendar.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
				return sdf.format(calendar.getTime());
			case COLUMN_S3:
				// Status 3
				if (!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT)) {
					return null;
				}
				calendar.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
				return sdf.format(calendar.getTime());
			case COLUMN_S4:
				// Status 4
				if (!transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION)) {
					return null;
				}
				calendar.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
				return sdf.format(calendar.getTime());
			case COLUMN_FZG:
				if (transport.getVehicleDetail() == null) {
					return null;
				}
				return transport.getVehicleDetail().getVehicleName();
			case COLUMN_T:
				if (transport.getKindOfTransport() == null) {
					return null;
				}
				if (transport.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_TRAGSESSEL))
					return "S";
				if (transport.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_KRANKENTRAGE))
					return "L";
				if (transport.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_GEHEND))
					return "G";
				if (transport.getKindOfTransport().equalsIgnoreCase(IKindOfTransport.TRANSPORT_KIND_ROLLSTUHL))
					return "R";
				return "";
			case COLUMN_ERKR_VERL:
				if (transport.getKindOfIllness() == null) {
					return null;
				}
				return transport.getKindOfIllness().getDiseaseName();
			case COLUMN_ANMERKUNG:
				return transport.getNotes();
			default:
				return null;
		}
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		if (transport.getTransportPriority().equalsIgnoreCase("A"))
			return CustomColors.BACKGROUND_RED;
		if (transport.getTransportPriority().equalsIgnoreCase("B"))
			return CustomColors.BACKGROUND_BLUE;
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return CustomColors.VEHICLE_TABLE;
	}
}
