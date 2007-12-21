package at.rc.tacos.client.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import at.rc.tacos.model.RosterEntry;

/**
 * Provides sorting functions for the personal view table.
 * @author Michael
 */
public class PersonalViewSorter extends ViewerSorter 
{
	//columns that are sortable
	public final static String NAME_SORTER = "name";
	public final static String WORKTIME_SORTER = "worktime";
	public final static String CHECKIN_SORTER = "checkin";
	public final static String CHECKOUT_SORTER = "checkout";
	public final static String SERVICE_SORTER = "service";
	public final static String JOB_SORTER = "job";
	public final static String STATION_SORTER = "station";
	public final static String VEHICLE_SORTER = "vehicle";
	
	//column to sort
	private String column = null;
    private int dir = SWT.DOWN;
    
    /**
     * Default class constructor providing a columt to sort and a direction
     * @param column the column to sort by
     * @param dir the sorting direction
     */
    public PersonalViewSorter(String column, int dir) 
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
    public int compare(Viewer viewer, Object object1, Object object2) {
        int returnValue = 0;
        
        //cast to a roster entry
        RosterEntry entry1 = (RosterEntry)object1;
        RosterEntry entry2 = (RosterEntry)object2;
        
        //sort by the name
        if (column == NAME_SORTER) 
        {
        	String name1 = entry1.getStaffMember().getLastname();
        	String name2 = entry2.getStaffMember().getLastname();
        	returnValue = name1.compareTo(name2);
        }
        //sort by the start time of work
        if (column == WORKTIME_SORTER) 
        {
        	long start1 = entry1.getPlannedStartOfWork();
        	long start2 = entry2.getPlannedStartOfWork();
        	if(start1 > start2)
        		returnValue = -1;
        	if(start1 < start2)
        		returnValue = 1;
        	if (start1 == start2)
        		returnValue =  0;
        }
        //sort by the checkin time
        if(column == CHECKIN_SORTER)
        {
        	long checkin1 = entry1.getRealStartOfWork();
        	long checkin2 = entry2.getRealStartOfWork();
        	if(checkin1 > checkin2)
        		returnValue = -1;
        	if(checkin1 < checkin2)
        		returnValue = 1;
        	if (checkin1 == checkin2)
        		returnValue =  0;
        }
        //sort by the checkout time
        if(column == CHECKOUT_SORTER)
        {
        	long checkout1 = entry1.getRealEndOfWork();
        	long checkout2 = entry2.getRealEndOfWork();
        	if(checkout1 > checkout2)
        		returnValue = -1;
        	if(checkout1 < checkout2)
        		returnValue = 1;
        	if (checkout1 == checkout2)
        		returnValue =  0;
        }
        //sort by the service
        if(column == SERVICE_SORTER)
        	returnValue = entry1.getServicetype().compareTo(entry2.getServicetype());
        //sort by the job
        if(column == JOB_SORTER)
        	returnValue = entry1.getJob().compareTo(entry2.getJob());
        //sort by the station
        if(column == STATION_SORTER)
        	returnValue = entry1.getStation().compareTo(entry2.getStation());
        if (this.dir == SWT.DOWN) {
            returnValue = returnValue * -1;
        }
        return returnValue;
    }
}
