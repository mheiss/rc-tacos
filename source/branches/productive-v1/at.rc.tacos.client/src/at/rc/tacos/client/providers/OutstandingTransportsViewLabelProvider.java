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

public class OutstandingTransportsViewLabelProvider implements ITableLabelProvider, ITableColorProvider, IKindOfTransport, ITableFontProvider {

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_PRIORITY = 1;
	public static final int COLUMN_RESP_STATION = 2;
	public static final int COLUMN_ABF = 3;
	public static final int COLUMN_AT_PATIENT = 4;
	public static final int COLUMN_TERM = 5;
	public static final int COLUMN_FROM = 6;
	public static final int COLUMN_PATIENT = 7;
	public static final int COLUMN_TO = 8;
	public static final int COLUMN_AUFG = 9;
	public static final int COLUMN_T = 10;
	public static final int COLUMN_ERKR_VERL = 11;
	public static final int COLUMN_NOTES = 12;

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
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Transport transport = (Transport) element;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String street;
		String city;
		String patient = "";
		String beforePriority = "";
		String afterPriority = "";
		String priority = "";

		if (transport.isBlueLight1())
			beforePriority = "!";
		if (transport.isBlueLightToGoal())
			afterPriority = "!";

		switch (columnIndex) {
			case COLUMN_LOCK:
				return null;
			case COLUMN_PRIORITY:
				if (transport.getTransportPriority().equalsIgnoreCase("A"))
					priority = "1";
				else if (transport.getTransportPriority().equalsIgnoreCase("B"))
					priority = "2";
				else if (transport.getTransportPriority().equalsIgnoreCase("C"))
					priority = "3";
				else if (transport.getTransportPriority().equalsIgnoreCase("D"))
					priority = "4";
				else if (transport.getTransportPriority().equalsIgnoreCase("E"))
					priority = "5";
				else if (transport.getTransportPriority().equalsIgnoreCase("F"))
					priority = "6";
				else if (transport.getTransportPriority().equalsIgnoreCase("G"))
					priority = "7";
				return beforePriority + priority + afterPriority;

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
					return patient;
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
			case COLUMN_AUFG:
				if (transport.getCreationTime() != 0)
					return sdf.format(transport.getCreationTime());
				else
					return "";
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
			case COLUMN_ERKR_VERL:
				if (transport.getKindOfIllness() != null)
					return transport.getKindOfIllness().getDiseaseName();
				else
					return "";
			case COLUMN_NOTES:
				return transport.getNotes();
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
		// determine the colum and return the color to render
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
		// TODO Auto-generated method stub
		return CustomColors.VEHICLE_TABLE;
	}
}
