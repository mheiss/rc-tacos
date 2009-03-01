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

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.VehicleDetail;

public class VehicleAssignLabelProvider implements ITableLabelProvider, ITableColorProvider, ITransportStatus {

	// define the columns
	public static final int COLUMN_STATION = 0;
	public static final int COLUMN_NAME = 1;
	public static final int COlUMN_TYPE = 2;
	public static final int COLUMN_DRIVER = 3;
	public static final int COLUMN_MEDIC_I = 4;
	public static final int COLUMN_MEDIC_II = 5;

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		VehicleDetail vehicle = (VehicleDetail) element;

		switch (columnIndex) {
			// define the columns
			case COLUMN_STATION:
				if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("Kapfenberg"))
					return "KA";
				else if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("Bruck an der Mur"))
					return "BM";
				else if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("St. Marein"))
					return "MA";
				else if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("Breitenau"))
					return "BR";
				else if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("Thörl"))
					return "TH";
				else if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("Turnau"))
					return "TU";
				else if (vehicle.getCurrentStation().getLocationName().equalsIgnoreCase("Bezirk: Bruck - Kapfenberg"))
					return "BE";
				else
					return null;
			case COLUMN_NAME:
				return vehicle.getVehicleName();
			case COlUMN_TYPE:
				return vehicle.getVehicleType();
			case COLUMN_DRIVER:
				if (vehicle.getDriver() != null)
					return vehicle.getDriver().getLastName() + " " + vehicle.getDriver().getFirstName();
			case COLUMN_MEDIC_I:
				if (vehicle.getFirstParamedic() != null)
					return vehicle.getFirstParamedic().getLastName() + " " + vehicle.getFirstParamedic().getFirstName();
			case COLUMN_MEDIC_II:
				if (vehicle.getSecondParamedic() != null)
					return vehicle.getSecondParamedic().getLastName() + " " + vehicle.getSecondParamedic().getFirstName();
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
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		return null;
	}
}
