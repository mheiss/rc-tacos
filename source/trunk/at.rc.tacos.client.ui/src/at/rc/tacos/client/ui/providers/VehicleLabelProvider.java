package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.VehicleDetail;

public class VehicleLabelProvider extends LabelProvider {

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		VehicleDetail detail = (VehicleDetail) object;
		return detail.getVehicleName() + " " + detail.getVehicleType();
	}

}
