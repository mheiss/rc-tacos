package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.LabelProvider;

import at.rc.tacos.platform.model.Location;

public class StationLabelProvider extends LabelProvider {

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getText(Object object) {
		Location location = (Location) object;
		return location.getLocationName();
	}
}
