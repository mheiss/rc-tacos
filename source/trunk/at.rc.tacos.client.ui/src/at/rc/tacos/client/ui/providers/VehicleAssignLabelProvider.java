package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.platform.model.VehicleDetail;

public class VehicleAssignLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

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
}
