package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.VehicleDetail;

/**
 * Provides sorting functions for the transport tables.
 * @author b.thek
 */
public class VehicleSorter extends ViewerSorter implements ITransportStatus
{
	//columns that are sortable
	
	public final static String VEHICLE_SORTER = "vehicle";
	public final static String DRIVER_SORTER = "driver";
	public final static String PARAMEDIC_I_SORTER = "paramedici";
	public final static String PARAMEDIC_II_SORTER = "paramedicii";
	public final static String CURRENT_STATION_SORTER = "zustortsstelle";
	public final static String VEHICLE_TYPE_SORTER = "type";


	// sort the data based on column and direction

	//column to sort
	private String column = null;
	private int dir = SWT.DOWN;

	/**
	 * Default class constructor providing a column to sort and a direction
	 * @param column the column to sort by
	 * @param dir the sorting direction
	 */
	public VehicleSorter(String column, int dir) 
	{
		super();
		this.column = column;
		this.dir = dir;
	}

	/**
	 * Compares the given object and returns the result of the comparator
	 * @param viewer the viewer containing the data
	 * @param object1 the first object to compare
	 * @param object2 the second object to compare+
	 * @return the result of the comparation 
	 */
	@Override
	public int compare(Viewer viewer, Object object1, Object object2) 
	{
		//cast to a transport
		VehicleDetail veh1 = (VehicleDetail)object1;
		VehicleDetail veh2 = (VehicleDetail)object2;
		//the sort direction
		int sortDir = 1;

		if (dir == SWT.DOWN) 
			sortDir = -1;

		//sort by the vehicle name
		if (column == VEHICLE_SORTER) 
		{
			//assert the vehicle is valid
			String v1 = veh1.getVehicleName();
			String v2 = veh2.getVehicleName();
			return v1.compareTo(v2) * sortDir;
		}
		
		//sort by the vehicle name
		if (column == VEHICLE_TYPE_SORTER) 
		{
			//assert the vehicle is valid
			String v1 = veh1.getVehicleType();
			String v2 = veh2.getVehicleType();
			return v1.compareTo(v2) * sortDir;
		}

		//sort by the driver name
		if (column == DRIVER_SORTER) 
		{
			//assert the vehicle and the driver is valid
			if(veh1.getDriver() == null)
				return -1 * sortDir;
			//assert the vehicle and the driver is valid
			if(veh2.getDriver() == null)
				return 1 * sortDir;
			String d1 = veh1.getDriver().getLastName();
			String d2 = veh2.getDriver().getLastName();
			return d1.compareTo(d2) * sortDir;
		}

		//sort by the paramedic I name
		if (column == PARAMEDIC_I_SORTER) 
		{
			//assert the vehicle and the medic is valid
			if(veh1.getFirstParamedic() == null)
				return -1 * sortDir;
			//assert the vehicle and the medic is valid
			if(veh2.getFirstParamedic() == null)
				return 1 * sortDir;
			String p1 = veh1.getFirstParamedic().getLastName();
			String p2 = veh2.getFirstParamedic().getLastName();
			return p1.compareTo(p2) * sortDir;
		}

		//sort by the paramedic II name
		if (column == PARAMEDIC_II_SORTER) 
		{
			//assert the vehicle and the medic is valid
			if(veh1.getSecondParamedic() == null)
				return -1 * sortDir;
			//assert the vehicle and the medic is valid
			if(veh2.getSecondParamedic() == null)
				return 1 * sortDir;
			String p1 = veh1.getSecondParamedic().getLastName();
			String p2 = veh2.getSecondParamedic().getLastName();
			return p1.compareTo(p2) * sortDir;
		}
	
		//sort by the station name
		if (column == CURRENT_STATION_SORTER) 
		{
			//assert valid
			if(veh1.getCurrentStation() == null)
				return -1 * sortDir;
			if(veh1.getCurrentStation() == null)
				return -1 * sortDir;
			//assert valid 
			if(veh2.getCurrentStation() == null)
				return 1 * sortDir;
			if(veh2.getCurrentStation() == null)
				return 1 * sortDir;
			//now compare
			String st1 = veh1.getCurrentStation().getLocationName();
			String st2 = veh2.getCurrentStation().getLocationName();
			return st1.compareTo(st2) * sortDir;
		}
		return 0;
	}
}
