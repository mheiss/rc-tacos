package at.rc.tacos.client.providers;

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
        	String number1 = transport1.getTransportNumber();
        	String number2 = transport2.getTransportNumber();
        	returnValue = number1.compareTo(number2);
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_AT_PATIENT).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_AT_PATIENT).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_AT_DESTINATION).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_AT_DESTINATION).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_DESTINATION_FREE).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_DESTINATION_FREE).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_CAR_IN_STATION).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_CAR_IN_STATION).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA).getStatus();
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
        	int statustime1 = transport1.getStatusMessages().get(TRANSPORT_STATUS_OTHER).getStatus();
        	int statustime2 = transport2.getStatusMessages().get(TRANSPORT_STATUS_OTHER).getStatus();
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
        	String d1 = transport1.getVehicleDetail().getDriverName().getLastName();
        	String d2 = transport2.getVehicleDetail().getDriverName().getLastName();
        	returnValue = d1.compareTo(d2);
        }
        
        //sort by the paramedic I name
        if (column == PARAMEDIC_I_SORTER) 
        {
        	String p1 = transport1.getVehicleDetail().getParamedicIName().getLastName();
        	String p2 = transport2.getVehicleDetail().getParamedicIName().getLastName();
        	returnValue = p1.compareTo(p2);
        }
        
        //sort by the paramedic II name
        if (column == PARAMEDIC_II_SORTER) 
        {
        	String p1 = transport1.getVehicleDetail().getParamedicIIName().getLastName();
        	String p2 = transport2.getVehicleDetail().getParamedicIIName().getLastName();
        	returnValue = p1.compareTo(p2);
        }
        
        //sort by the caller name
        if (column == CALLER_SORTER) 
        {
        	String c1 = transport1.getCallerDetail().getCallerName();
        	String c2 = transport2.getCallerDetail().getCallerName();
        	returnValue = c1.compareTo(c2);
        }

        
        if (this.dir == SWT.DOWN) {
            returnValue = returnValue * -1;
        }
        
        return returnValue;
    }
}
