package at.rc.tacos.client.ui.providers;

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
import at.rc.tacos.platform.model.VehicleDetail;

public class VehicleViewTableDetailLabelProvider extends BaseLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_READY = 1;
	public static final int COLUMN_VEHICLE_NAME = 2;
	public static final int COLUMN_STATUS = 3;
	public static final int COLUMN_DRIVER = 4;
	public static final int COLUMN_MEDIC_I = 5;
	public static final int COLUMN_MEDIC_II = 6;
	public static final int COLUMN_PHONE = 7;
	public static final int COLUMN_STATION = 8;
	public static final int COLUMN_OUTOFORDER = 9;
	public static final int COLUMN_NOTES = 10;
	public static final int COLUMN_LAST_DESTINATION_FREE = 11;

	// the image registry
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		VehicleDetail vehicle = (VehicleDetail) element;
		// determine the column and return a image if needed
		switch (columnIndex) {
			case COLUMN_LOCK:
				if (vehicle.isLocked())
					return imageRegistry.get("resource.lock");
				return imageRegistry.get("empty.image24");
			case COLUMN_READY:
				if (vehicle.isReadyForAction())
					return imageRegistry.get("vehicle.ready");
				return null;
			case COLUMN_STATUS:
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_GREEN)
					return imageRegistry.get("vehicle.status.green");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_NA)
					return imageRegistry.get("vehicle.status.na");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_YELLOW)
					return imageRegistry.get("vehicle.status.yellow");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPROT_STATUS_RED)
					return imageRegistry.get("vehicle.status.red");
				if (vehicle.getTransportStatus() == VehicleDetail.TRANSPORT_STATUS_BLUE)
					return imageRegistry.get("vehicle.status.blue");
				return null;
			case COLUMN_PHONE:
				if (!vehicle.getMobilePhone().getMobilePhoneName().equalsIgnoreCase(vehicle.getVehicleName()))
					return imageRegistry.get("vehicle.phone");
				return null;
			case COLUMN_STATION:
				if (!vehicle.getBasicStation().getLocationName().equalsIgnoreCase(vehicle.getCurrentStation().getLocationName()))
					return imageRegistry.get("vehicle.house");
				return null;
			case COLUMN_OUTOFORDER:
				if (vehicle.isOutOfOrder())
					imageRegistry.get("vehicle.repair");
				return null;
			case COLUMN_NOTES:
				if (vehicle.getVehicleNotes() == null) {
					return null;
				}
				if (vehicle.getVehicleNotes().isEmpty()) {
					return null;
				}
				return imageRegistry.get("vehicle.notes");
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		VehicleDetail detail = (VehicleDetail) element;
		switch (columnIndex) {
			case COLUMN_VEHICLE_NAME:
				return detail.getVehicleName();
			case COLUMN_DRIVER:
				if (detail.getDriver() != null)
					return detail.getDriver().getLastName() + " " + detail.getDriver().getFirstName();
				return null;
			case COLUMN_MEDIC_I:
				if (detail.getFirstParamedic() != null)
					return detail.getFirstParamedic().getLastName() + " " + detail.getFirstParamedic().getFirstName();
				return null;
			case COLUMN_MEDIC_II:
				if (detail.getSecondParamedic() != null)
					return detail.getSecondParamedic().getLastName() + " " + detail.getSecondParamedic().getFirstName();
				return null;
			case COLUMN_LAST_DESTINATION_FREE:
				if (detail.getLastDestinationFree() != null)
					return detail.getLastDestinationFree();
				return null;
		}
		return null;
	}

	@Override
	public Color getBackground(Object element, int columnIndex) {
		return null;
	}

	@Override
	public Color getForeground(Object element, int columnIndex) {
		VehicleDetail detail = (VehicleDetail) element;
		if (detail.isOutOfOrder())
			return CustomColors.COLOR_GREY;
		return null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return CustomColors.APPLICATION_DATA_FONT;
	}
}
