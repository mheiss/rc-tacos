package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import at.rc.tacos.model.Address;

public class AddressViewSorter extends ViewerSorter 
{
	//columns that are sortable
	public final static String ZIP_SORTER = "zip";
	public final static String CITY_SORTER = "city";
	public final static String STREET_SORTER = "street";

	//column to sort
	private String column = null;
	private int dir = SWT.DOWN;

	/**
	 * Default class constructor providing a columt to sort and a direction
	 * @param column the column to sort by
	 * @param dir the sorting direction
	 */
	public AddressViewSorter(String column, int dir) 
	{
		super();
		this.column = column;
		this.dir = dir;
	}

	/**
	 * Compares the given object and returns the result of the comparator
	 * @param viewer the viewer containg the data
	 * @param object1 the first object to compare
	 * @param object2 the second object to compare+
	 * @return the result of the comparation 
	 */
	@Override
	public int compare(Viewer viewer, Object object1, Object object2) 
	{
		int returnValue = 0;

		//cast to a roster entry
		Address address1 = (Address)object1;
		Address address2 = (Address)object2;

		//sort by the name
		if (column == ZIP_SORTER) 
		{
			int zip1 = address1.getZip();
			int zip2 = address2.getZip();
			if(zip1 > zip2)
				returnValue = -1;
			if(zip1 < zip2)
				returnValue = -1;
			if(zip1 == zip2)
				returnValue = 0;
		}
		//sort by the start time of work
		if (column == CITY_SORTER) 
		{
			String city1 = address1.getCity();
			String city2 = address2.getCity();
			returnValue = city1.compareTo(city2);
		}
		//sort by the checkin time
		if(column == STREET_SORTER)
		{
			String street1 = address1.getStreet();
			String street2 = address2.getStreet();
			returnValue = street1.compareTo(street2);
		}

		//sort direction
		if (dir == SWT.DOWN) 
		{
			returnValue = returnValue * -1;
		}
		return returnValue;
	}
}
