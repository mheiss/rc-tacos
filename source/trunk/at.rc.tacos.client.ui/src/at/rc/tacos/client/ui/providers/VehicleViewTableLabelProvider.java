package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.VehicleDetail;

public class VehicleViewTableLabelProvider extends BaseLabelProvider implements ITableLabelProvider, ITableFontProvider {

	// define the columns
	public static final int COLUMN_LOCK = 0;
	public static final int COLUMN_NAME = 1;
	public static final int COLUMN_STATUS = 2;

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
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		VehicleDetail detail = (VehicleDetail) element;
		switch (columnIndex) {
			case COLUMN_NAME:
				return detail.getVehicleName();
		}
		return null;
	}

	@Override
	public Font getFont(Object element, int columnIndex) {
		return CustomColors.VEHICLE_TABLE;
	}
}
