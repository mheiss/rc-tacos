package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import at.rc.tacos.model.VehicleDetail;

/**
 * Provides sorting functions for the vehicle view table.
 * @author Birgit
 */
public class VehicleViewTableSorter extends ViewerSorter 
{
	//columns that are sortable
	public final static String STATUS_SORTER = "job";
	public final static String VEHICLE_SORTER = "vehicle";
	
	//column to sort
	private String column = null;
	private int dir = SWT.DOWN;
    
    /**
     * Default class constructor providing a columt to sort and a direction
     * @param column the column to sort by
     * @param dir the sorting direction
     */
    public VehicleViewTableSorter(String column, int dir) 
    {
        super();
        this.column = column;
        this.dir = dir;
    }
    
    /**
     * Compares the given object and returns the result of the comparator
     * @param viewer the viewer containing the data
     * @param object1 the first object to compare
     * @param object2 the second object to compare
     * @return the result of the comparison
     */
    @Override
	public int compare(Viewer viewer, Object object1, Object object2) 
    {
    	int sortDir = 1;

		if (dir == SWT.DOWN) 
			sortDir = -1;
		
        int returnValue = 0;
        
        //cast to a vehicle detail
        VehicleDetail veh1 = (VehicleDetail)object1;
        VehicleDetail veh2 = (VehicleDetail)object2;

        
        //sort by the vehicle name
		if (column == VEHICLE_SORTER) 
		{
			//assert the vehicle is valid
			String v1 = veh1.getVehicleName();
			String v2 = veh2.getVehicleName();
			return v1.compareTo(v2) * sortDir;
		}
        
        //sort by the status
        if (column == STATUS_SORTER) 
        {
        	long status1 = veh1.getTransportStatus();
        	long status2 = veh2.getTransportStatus();
        	if(status1 > status2)
        		returnValue = -1;
        	if(status1 < status2)
        		returnValue = 1;
        	if (status1 == status2)
        		returnValue =  0;
        }
        return returnValue;
    }
}
