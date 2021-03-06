package at.rc.tacos.client.ui.providers;

import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import at.rc.tacos.platform.model.SickPerson;

public class SickPersonLabelProvider extends BaseLabelProvider implements ITableLabelProvider {

	// define the columns
	public static final int COLUMN_LASTNAME = 0;
	public static final int COLUMN_FIRSTNAME = 1;
	public static final int COLUMN_STREET = 2;
	public static final int COLUMN_CITY = 3;
	public static final int COLUMN_SVNR = 4;
	public static final int COLUMN_NOTES = 5;

	/**
	 * Returns the text to render.
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		SickPerson person = (SickPerson) element;
		switch (columnIndex) {
			case COLUMN_LASTNAME:
				return person.getLastName();
			case COLUMN_FIRSTNAME:
				return person.getFirstName();
			case COLUMN_STREET:
				return person.getStreetname();
			case COLUMN_CITY:
				return person.getCityname();
			case COLUMN_SVNR:
				return person.getSVNR();
			case COLUMN_NOTES:
				return person.getNotes();
			default:
				return null;
		}
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}
}
