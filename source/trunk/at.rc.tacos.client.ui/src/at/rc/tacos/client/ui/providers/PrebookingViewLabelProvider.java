package at.rc.tacos.client.ui.providers;

import java.text.SimpleDateFormat;

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
import at.rc.tacos.platform.model.Transport;

public class PrebookingViewLabelProvider extends BaseLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_RESP_STATION = 1;
	public static final int COLUMN_ABF = 2;
	public static final int COLUMN_AT_PATIENT = 3;
	public static final int COLUMN_TERM = 4;
	public static final int COLUMN_FROM = 5;
	public static final int COLUMN_PATIENT = 6;
	public static final int COLUMN_TO = 7;
	public static final int COLUMN_T = 8;
	public static final int Column_NOTES = 9;

	// the image factory
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (transport.isLocked()) {
					return imageRegistry.get("resource.lock");
				}
				return imageRegistry.get("empty.image24");
			case COLUMN_TO:
				if (transport.isLongDistanceTrip()) {
					return imageRegistry.get("transport.alarming.fernfahrt");
				}
				return null;
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		switch (columnIndex) {
			case COLUMN_RESP_STATION:
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Kapfenberg"))
					return "KA";
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Bruck an der Mur"))
					return "BM";
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("St. Marein"))
					return "MA";
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Breitenau"))
					return "BR";
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Thörl"))
					return "TH";
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Turnau"))
					return "TU";
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Bezirk: Bruck - Kapfenberg"))
					return "BE";
				return null;
			case COLUMN_ABF:
				if (transport.getPlannedStartOfTransport() != 0)
					return sdf.format(transport.getPlannedStartOfTransport());
				return "";
			case COLUMN_AT_PATIENT:
				if (transport.getPlannedTimeAtPatient() != 0)
					return sdf.format(transport.getPlannedTimeAtPatient());
				return "";
			case COLUMN_TERM:
				if (transport.getAppointmentTimeAtDestination() != 0)
					return sdf.format(transport.getAppointmentTimeAtDestination());
				return "";
			case COLUMN_FROM:
				return transport.getFromStreet() + " / " + transport.getFromCity();
			case COLUMN_PATIENT:
				String patient = transport.isAssistantPerson() ? "+" : "";
				if (transport.getPatient() != null) {
					return patient + " " + transport.getPatient().getLastname() + " " + transport.getPatient().getFirstname();
				}
				return "";
			case COLUMN_TO:
				String street = transport.getToStreet() == null ? "" : transport.getToStreet();
				String city = transport.getToCity() == null ? "" : transport.getToCity();
				return street + " / " + city;
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
			case Column_NOTES:
				return transport.getNotes();
			default:
				return null;
		}
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		// determine the colum and return a image if needed
		if (transport.getTransportPriority().equalsIgnoreCase("A"))
			return CustomColors.BACKGROUND_RED;
		else if (transport.getTransportPriority().equalsIgnoreCase("B"))
			return CustomColors.BACKGROUND_BLUE;
		// default color, nothing
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return CustomColors.APPLICATION_DATA_FONT;
	}
}
