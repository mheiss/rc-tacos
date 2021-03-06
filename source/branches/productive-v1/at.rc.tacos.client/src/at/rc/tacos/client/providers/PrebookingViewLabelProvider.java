/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.providers;

import java.text.SimpleDateFormat;

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
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

public class PrebookingViewLabelProvider implements ITableLabelProvider, ITableColorProvider, IKindOfTransport, ITableFontProvider {

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

	// the lock manager
	private LockManager lockManager = ModelFactory.getInstance().getLockManager();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		// determine the colum and return a image if needed
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (lockManager.containsLock(Transport.ID, transport.getTransportId()))
					return ImageFactory.getInstance().getRegisteredImage("resource.lock18");
				else
					return ImageFactory.getInstance().getRegisteredImage("resource.nothing18");
			case COLUMN_TO:
				if (transport.isLongDistanceTrip())
					return ImageFactory.getInstance().getRegisteredImage("transport.alarming.fernfahrt");
				else
					return null;
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String street;
		String city;
		String patient = "";

		switch (columnIndex) {

			case COLUMN_RESP_STATION:
				if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Kapfenberg"))
					return "KA";
				else if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Bruck an der Mur"))
					return "BM";
				else if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("St. Marein"))
					return "MA";
				else if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Breitenau"))
					return "BR";
				else if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Th�rl"))
					return "TH";
				else if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Turnau"))
					return "TU";
				else if (transport.getPlanedLocation().getLocationName().equalsIgnoreCase("Bezirk: Bruck - Kapfenberg"))
					return "BE";
				else
					return null;
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
				else
					return "";
			case COLUMN_FROM:
				return transport.getFromStreet() + " / " + transport.getFromCity();
			case COLUMN_PATIENT:
				if (transport.isAssistantPerson())
					patient = "+";
				if (transport.getPatient() != null)
					return patient + " " + transport.getPatient().getLastname() + " " + transport.getPatient().getFirstname();
				else
					return "";
			case COLUMN_TO:
				if (transport.getToStreet() == null)
					street = "";
				else
					street = transport.getToStreet();
				if (transport.getToCity() == null)
					city = "";
				else
					city = transport.getToCity();
				return street + " / " + city;
			case COLUMN_T:
				if (transport.getKindOfTransport() != null) {
					if (transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_TRAGSESSEL))
						return "S";
					else if (transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_KRANKENTRAGE))
						return "L";
					else if (transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_GEHEND))
						return "G";
					else if (transport.getKindOfTransport().equalsIgnoreCase(TRANSPORT_KIND_ROLLSTUHL))
						return "R";
					else
						return "";
				}
				else
					return "";
			case Column_NOTES:
				if (transport.getNotes() != null)
					return transport.getNotes();
				else
					return "";
			default:
				return null;
		}
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
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
		// TODO Auto-generated method stub
		return CustomColors.VEHICLE_TABLE;
	}
}
