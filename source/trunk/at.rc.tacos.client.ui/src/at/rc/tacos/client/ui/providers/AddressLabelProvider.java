package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.Address;

public class AddressLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	// define the columns
	public static final int COLUMN_IMAGE = 0;
	public static final int COLUMN_ZIP = 1;
	public static final int COLUMN_CITY = 2;
	public static final int COLUMN_STREET = 3;

	// the image registry
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public Image getColumnImage(Object object, int column) {
		// determine the image
		switch (column) {
			case COLUMN_IMAGE:
				return imageRegistry.get("resource.address");
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object object, int column) {
		Address address = (Address) object;
		switch (column) {
			case COLUMN_ZIP:
				return String.valueOf(address.getZip());
			case COLUMN_CITY:
				return address.getCity();
			case COLUMN_STREET:
				return address.getStreet();
			default:
				return null;

		}
	}
}
