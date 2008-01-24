package at.rc.tacos.client.view.sorterAndTooltip;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

/**
 * Provides sorting functions for the transport tables.
 * @author b.thek
 */
public class TransportSorter extends ViewerSorter implements ITransportStatus
{
	//columns that are sortable
	public final static String TNR_SORTER = "name";
	public final static String PRIORITY_SORTER = "priority";
	public final static String TRANSPORT_FROM_SORTER = "transportfrom";
	public final static String PATIENT_SORTER = "patient";
	public final static String TRANSPORT_TO_SORTER = "transportto";
	public final static String KIND_OF_ILLNESS_SORTER = "kindofillness";
	public final static String AE_SORTER = "ae";
	public final static String S1_SORTER = "s1";
	public final static String S2_SORTER = "s2";
	public final static String S3_SORTER = "s3";
	public final static String S4_SORTER = "s4";
	public final static String S5_SORTER = "s5";
	public final static String S6_SORTER = "s6";
	public final static String S7_SORTER = "s7";
	public final static String S8_SORTER = "s8";
	public final static String S9_SORTER = "s9";
	public final static String VEHICLE_SORTER = "vehicle";
	public final static String DRIVER_SORTER = "driver";
	public final static String PARAMEDIC_I_SORTER = "paramedici";
	public final static String PARAMEDIC_II_SORTER = "paramedicii";
	public final static String CALLER_SORTER = "caller";
	public final static String ABF_SORTER = "abfahrt";
	public final static String TERM_SORTER = "termin";
	public final static String AT_PATIENT_SORTER = "beipatient";
	public final static String RESP_STATION_SORTER = "zustortsstelle";
	public final static String AUFG_SORTER = "aufgenommen";
	public final static String TA_SORTER = "t";
	public final static String RT_SORTER = "rt";
	
	// sort the data based on column and direction
	
	//column to sort
	private String column = null;
    private int dir = SWT.DOWN;
    
    /**
     * Default class constructor providing a column to sort and a direction
     * @param column the column to sort by
     * @param dir the sorting direction
     */
    public TransportSorter(String column, int dir) 
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
    public int compare(Viewer viewer, Object object1, Object object2) {
        int returnValue = 0;
        
        //cast to a transport
        Transport transport1 = (Transport)object1;
        Transport transport2 = (Transport)object2;
        
        //sort by the transport number
        if (column == TNR_SORTER) 
        {
        	int number1 = transport1.getTransportNumber();
        	int number2 = transport2.getTransportNumber();
        	if(number1 > number2)
        	    return -1;
        	if(number1 < number2)
        	    return 1;
        	if(number1 == number2)
        	    return 0;
        }
        //sort by the priority of the transport
        if (column == PRIORITY_SORTER) 
        {
        	String priority1 = transport1.getTransportPriority();
        	String priority2 = transport2.getTransportPriority();
        	returnValue = priority1.compareTo(priority2);
        }
        //sort by the transport from column
        if(column == TRANSPORT_FROM_SORTER)
        {
        	//TODO right to sort by the city?
        	String from1 = transport1.getFromCity();
        	String from2 = transport2.getFromCity();
        	returnValue = from1.compareTo(from2);
        }
        //sort by the patient last name
        if(column == PATIENT_SORTER)
        {
        	String patient1 = transport1.getPatient().getLastname();
        	String patient2 = transport2.getPatient().getLastname();
        	returnValue = patient1.compareTo(patient2);
        }
        
        //sort by the transport to column
        if(column == TRANSPORT_TO_SORTER)
        {
        	//TODO right to sort by the city?
        	String to1 = transport1.getToCity();
        	String to2 = transport2.getToCity();
        	returnValue = to1.compareTo(to2);
        }
        
      //sort by the kind of illness
        if(column == KIND_OF_ILLNESS_SORTER)
        {
        	String kind1 = transport1.getKindOfIllness();
        	String kind2 = transport2.getKindOfIllness();
        	returnValue = kind1.compareTo(kind2);
        }
        
        //sort by the time of the 'AE' field
        if(column == AE_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S1' field
        if(column == S1_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S2' field
        if(column == S2_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_AT_PATIENT);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_AT_PATIENT);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S3' field
        if(column == S3_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S4' field
        if(column == S4_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_AT_DESTINATION);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_AT_DESTINATION);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S5' field
        if(column == S5_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_DESTINATION_FREE);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_DESTINATION_FREE);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
      //sort by the time of the 'S6' field
        if(column == S6_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_CAR_IN_STATION);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_CAR_IN_STATION);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S7' field
        if(column == S7_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S8' field
        if(column == S8_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the time of the 'S9' field
        if(column == S9_SORTER)
        {
        	long statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_OTHER);
        	long statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_OTHER);
        	if(statustime1 > statustime2)
        		returnValue = -1;
        	if(statustime1 < statustime2)
        		returnValue = 1;
        	if (statustime1 == statustime2)
        		returnValue =  0;
        }
        
        //sort by the vehicle name
        if (column == VEHICLE_SORTER) 
        {
        	String v1 = transport1.getVehicleDetail().getVehicleName();
        	String v2 = transport2.getVehicleDetail().getVehicleName();
        	returnValue = v1.compareTo(v2);
        }
        
        //sort by the driver name
        if (column == DRIVER_SORTER) 
        {
        	String d1 = transport1.getVehicleDetail().getDriver().getLastName();
        	String d2 = transport2.getVehicleDetail().getDriver().getLastName();
        	returnValue = d1.compareTo(d2);
        }
        
        //sort by the paramedic I name
        if (column == PARAMEDIC_I_SORTER) 
        {
        	String p1 = transport1.getVehicleDetail().getFirstParamedic().getLastName();
        	String p2 = transport2.getVehicleDetail().getFirstParamedic().getLastName();
        	returnValue = p1.compareTo(p2);
        }
        
        //sort by the paramedic II name
        if (column == PARAMEDIC_II_SORTER) 
        {
        	String p1 = transport1.getVehicleDetail().getSecondParamedic().getLastName();
        	String p2 = transport2.getVehicleDetail().getSecondParamedic().getLastName();
        	returnValue = p1.compareTo(p2);
        }
        
        //sort by the caller name
        if (column == CALLER_SORTER) 
        {
        	String c1 = transport1.getCallerDetail().getCallerName();
        	String c2 = transport2.getCallerDetail().getCallerName();
        	returnValue = c1.compareTo(c2);
        }
        
        //sort by the start time of the transport
        if (column == ABF_SORTER) 
        {
        	long start1 = transport1.getPlannedStartOfTransport();
        	long start2 = transport2.getPlannedStartOfTransport();
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
        	long atPatient1 = transport1.getPlannedTimeAtPatient();
        	long atPatient2 = transport2.getPlannedTimeAtPatient();
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
        	long term1 = transport1.getAppointmentTimeAtDestination();
        	long term2 = transport2.getAppointmentTimeAtDestination();
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
        	String st1 = transport1.getVehicleDetail().getCurrentStation().getLocationName();
        	String st2 = transport2.getVehicleDetail().getCurrentStation().getLocationName();
        	returnValue = st1.compareTo(st2);
        }
        
        //sort by the received time
        if (column == AUFG_SORTER) 
        {
        	long aufg1 = transport1.getCreationTime();
        	long aufg2 = transport2.getCreationTime();
        	if(aufg1 > aufg2)
        		returnValue = -1;
        	if(aufg1 < aufg2)
        		returnValue = 1;
        	if (aufg1 == aufg2)
        		returnValue =  0;
        }
        
        //sort by the kind of transport (g,s,l,r)
        if (column == TA_SORTER) 
        {
        	String ta1 = transport1.getKindOfTransport();
        	String ta2 = transport2.getKindOfTransport();
        	returnValue = ta1.compareTo(ta2);
        }
        
        //sort by the notes of transport
        if (column == TA_SORTER) 
        {
        	String n1 = transport1.getKindOfTransport();
        	String n2 = transport2.getKindOfTransport();
        	returnValue = n1.compareTo(n2);
        }
        
        

        
        if (this.dir == SWT.DOWN) {
            returnValue = returnValue * -1;
        }
        
        return returnValue;
    }
}
