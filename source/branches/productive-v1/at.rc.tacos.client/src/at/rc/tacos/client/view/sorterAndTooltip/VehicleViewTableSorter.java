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
package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import at.rc.tacos.model.VehicleDetail;

/**
 * Provides sorting functions for the vehicle view table.
 * 
 * @author Birgit
 */
public class VehicleViewTableSorter extends ViewerSorter {

	// columns that are sortable
	public final static String STATUS_SORTER = "job";
	public final static String VEHICLE_SORTER = "vehicle";
	public final static String NOTES_SORTER = "notes";
	public final static String LDF_SORTER = "ldf";
	public final static String DRIVER_SORTER = "driver";
	public final static String PARAMEDIC_I_SORTER = "paramedici";
	public final static String PARAMEDIC_II_SORTER = "paramedicii";

	// column to sort
	private String column = null;
	private int dir = SWT.DOWN;

	/**
	 * Default class constructor providing a columt to sort and a direction
	 * 
	 * @param column
	 *            the column to sort by
	 * @param dir
	 *            the sorting direction
	 */
	public VehicleViewTableSorter(String column, int dir) {
		super();
		this.column = column;
		this.dir = dir;
	}

	/**
	 * Compares the given object and returns the result of the comparator
	 * 
	 * @param viewer
	 *            the viewer containing the data
	 * @param object1
	 *            the first object to compare
	 * @param object2
	 *            the second object to compare
	 * @return the result of the comparison
	 */
	@Override
	public int compare(Viewer viewer, Object object1, Object object2) {
		int sortDir = 1;

		if (dir == SWT.DOWN)
			sortDir = -1;

		int returnValue = 0;

		// cast to a vehicle detail
		VehicleDetail veh1 = (VehicleDetail) object1;
		VehicleDetail veh2 = (VehicleDetail) object2;

		// sort by the vehicle name
		if (column == VEHICLE_SORTER) {
			// assert the vehicle is valid
			String v1 = veh1.getVehicleName();
			String v2 = veh2.getVehicleName();
			return v1.compareTo(v2) * sortDir;
		}

		// sort by the status
		if (column == STATUS_SORTER) {
			long status1 = veh1.getTransportStatus();
			long status2 = veh2.getTransportStatus();
			if (status1 > status2)
				returnValue = -1;
			if (status1 < status2)
				returnValue = 1;
			if (status1 == status2)
				returnValue = 0;
		}

		// sort by the notes
		if (column == NOTES_SORTER) {
			if (veh1.getVehicleNotes() == null)
				return -1 * sortDir;
			if (veh2.getVehicleNotes() == null)
				return 1 * sortDir;
			String ta1 = veh1.getVehicleNotes();
			String ta2 = veh2.getVehicleNotes();
			return ta1.compareTo(ta2) * sortDir;
		}

		// sort by the last destination free
		if (column == LDF_SORTER) {
			if (veh1.getLastDestinationFree() == null)
				return -1 * sortDir;
			if (veh2.getLastDestinationFree() == null)
				return 1 * sortDir;
			String ta1 = veh1.getLastDestinationFree();
			String ta2 = veh2.getLastDestinationFree();
			return ta1.compareTo(ta2) * sortDir;
		}

		// sort by the driver name
		if (column == DRIVER_SORTER) {
			// assert the vehicle and the driver is valid
			if (veh1 == null)
				return -1 * sortDir;
			if (veh1.getDriver() == null)
				return -1 * sortDir;
			// assert the vehicle and the driver is valid
			if (veh2 == null)
				return 1 * sortDir;
			if (veh2.getDriver() == null)
				return 1 * sortDir;
			String d1 = veh1.getDriver().getLastName();
			String d2 = veh2.getDriver().getLastName();
			return d1.compareTo(d2) * sortDir;
		}

		// sort by the paramedic I name
		if (column == PARAMEDIC_I_SORTER) {
			// assert the vehicle and the medic is valid
			if (veh1 == null)
				return -1 * sortDir;
			if (veh1.getFirstParamedic() == null)
				return -1 * sortDir;
			// assert the vehicle and the medic is valid
			if (veh2 == null)
				return 1 * sortDir;
			if (veh2.getFirstParamedic() == null)
				return 1 * sortDir;
			String p1 = veh1.getFirstParamedic().getLastName();
			String p2 = veh2.getFirstParamedic().getLastName();
			return p1.compareTo(p2) * sortDir;
		}

		// sort by the paramedic II name
		if (column == PARAMEDIC_II_SORTER) {
			// assert the vehicle and the medic is valid
			if (veh1 == null)
				return -1 * sortDir;
			if (veh1.getSecondParamedic() == null)
				return -1 * sortDir;
			// assert the vehicle and the medic is valid
			if (veh2 == null)
				return 1 * sortDir;
			if (veh2.getSecondParamedic() == null)
				return 1 * sortDir;
			String p1 = veh1.getSecondParamedic().getLastName();
			String p2 = veh2.getSecondParamedic().getLastName();
			return p1.compareTo(p2) * sortDir;
		}

		return returnValue;
	}
}
