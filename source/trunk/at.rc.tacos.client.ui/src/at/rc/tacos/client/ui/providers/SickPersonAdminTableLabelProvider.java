package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.SickPerson;

public class SickPersonAdminTableLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	// define the columns
	public static final int COLUMN_IMAGE = 0;
	public static final int COLUMN_SVNR = 1;
	public static final int COLUMN_LASTNAME = 2;
	public static final int COLUMN_FIRSTNAME = 3;

	// the image registry
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		// determine the image
		switch (columnIndex) {
			case COLUMN_IMAGE:
				return imageRegistry.get("admin.patient");
			default:
				return null;
		}
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		SickPerson person = (SickPerson) element;

		switch (columnIndex) {
			case COLUMN_LASTNAME:
				return person.getLastName();
			case COLUMN_FIRSTNAME:
				return person.getFirstName();
			case COLUMN_SVNR:
				return person.getSVNR();
			default:
				return null;
		}
	}
}
