package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import at.rc.tacos.model.DialysisPatient;

/**
 * Provides sorting functions for the dialysis transport table.
 * @author b.thek
 */
public class DialysisTransportSorter extends ViewerSorter 
{

	//columns that are sort able
	public final static String TRANSPORT_FROM_SORTER = "transportfrom";
	public final static String TRANSPORT_TO_SORTER = "transportto";
	public final static String PATIENT_SORTER = "patient";
	public final static String ABF_SORTER = "abfahrt";
	public final static String TERM_SORTER = "termin";
	public final static String AT_PATIENT_SORTER = "beipatient";
	public final static String RESP_STATION_SORTER = "zustortsstelle";
	public final static String TA_SORTER = "t";
	public final static String RT_SORTER = "rt";
	public final static String READY_SORTER = "ready";
	
	
	//column to sort
	private String column = null;
    private int dir = SWT.DOWN;
    
    /**
     * Default class constructor providing a columt to sort and a direction
     * @param column the column to sort by
     * @param dir the sorting direction
     */
    public DialysisTransportSorter(String column, int dir) 
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
        DialysisPatient dia1 = (DialysisPatient)object1;
        DialysisPatient dia2 = (DialysisPatient)object2;
        
      
        //sort by the transport from column
        if(column == TRANSPORT_FROM_SORTER)
        {
        	//TODO right to sort by the city?
        	String from1 = dia1.getFromCity();
        	String from2 = dia2.getFromCity();
        	returnValue = from1.compareTo(from2);
        }
        
      //sort by the transport to column
        if(column == TRANSPORT_TO_SORTER)
        {
        	//TODO right to sort by the city?
        	String from1 = dia1.getToCity();
        	String from2 = dia2.getToCity();
        	returnValue = from1.compareTo(from2);
        }
        
        //sort by the patient last name
        if(column == PATIENT_SORTER)
        {
        	String patient1 = dia1.getPatient().getLastname();
        	String patient2 = dia2.getPatient().getLastname();
        	returnValue = patient1.compareTo(patient2);
        }
        
        
        
        //sort by the start time of the transport
        if (column == ABF_SORTER) 
        {
        	long start1 = dia1.getPlannedStartOfTransport();
        	long start2 = dia2.getPlannedStartOfTransport();
        	if(start1 > start2)
        		returnValue = -1;
        	if(start1 < start2)
        		returnValue = 1;
        	if (start1 == start2)
        		returnValue =  0;
        }
        
        //sort by the "at patient"- time
        if (column == AT_PATIENT_SORTER) 
        {
        	long atPatient1 = dia1.getPlannedTimeAtPatient();
        	long atPatient2 = dia2.getPlannedTimeAtPatient();
        	if(atPatient1 > atPatient2)
        		returnValue = -1;
        	if(atPatient1 < atPatient2)
        		returnValue = 1;
        	if (atPatient1 == atPatient2)
        		returnValue =  0;
        }
        
        //sort by the term time
        if (column == TERM_SORTER) 
        {
        	long term1 = dia1.getAppointmentTimeAtDialysis();
        	long term2 = dia2.getAppointmentTimeAtDialysis();
        	if(term1 > term2)
        		returnValue = -1;
        	if(term1 < term2)
        		returnValue = 1;
        	if (term1 == term2)
        		returnValue =  0;
        }
        
        //sort by the rt time
        if (column == RT_SORTER) 
        {
        	long term1 = dia1.getPlannedStartForBackTransport();
        	long term2 = dia2.getPlannedStartForBackTransport();
        	if(term1 > term2)
        		returnValue = -1;
        	if(term1 < term2)
        		returnValue = 1;
        	if (term1 == term2)
        		returnValue =  0;
        }
        
        //sort by the ready time
        if (column == READY_SORTER) 
        {
        	long term1 = dia1.getReadyTime();
        	long term2 = dia2.getReadyTime();
        	if(term1 > term2)
        		returnValue = -1;
        	if(term1 < term2)
        		returnValue = 1;
        	if (term1 == term2)
        		returnValue =  0;
        }
        
        //sort by the station name
        if (column == RESP_STATION_SORTER) 
        {
        	String st1 = dia1.getLocation().getLocationName();
        	String st2 = dia2.getLocation().getLocationName();
        	returnValue = st1.compareTo(st2);
        }
        
        //sort by the kind of transport (g,s,l,r)
        if (column == TA_SORTER) 
        {
        	String ta1 = dia1.getKindOfTransport();
        	String ta2 = dia2.getKindOfTransport();
        	returnValue = ta1.compareTo(ta2);
        }
        
        //sort by the notes of transport
        if (column == TA_SORTER) 
        {
        	String n1 = dia1.getKindOfTransport();
        	String n2 = dia2.getKindOfTransport();
        	returnValue = n1.compareTo(n2);
        }
        
        

        
        if (this.dir == SWT.DOWN) {
            returnValue = returnValue * -1;
        }
        
        return returnValue;
    }
}
