package at.rc.tacos.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportPriority;
import at.rc.tacos.common.ITransportStatus;


public class MostImportantStatusOfVehicleTest implements IProgramStatus, ITransportStatus
{
	private Transport t1 = new Transport();
	private Transport t2 = new Transport();
	
	ArrayList<Integer> list = new ArrayList<Integer>();
	
	
	@Before
	public void setUp()
	{
		
		//vehicle
		VehicleDetail v1 = new VehicleDetail();
        v1.setVehicleName("Bm01");
        v1.setVehicleType("RTW");
        v1.setVehicleNotes("notes vehicle 1");
        //location
        Location loc = new Location();
        loc.setLocationName("Bruck an der Mur");
        v1.setBasicStation(loc);
        
        v1.setReadyForAction(true);
        v1.setOutOfOrder(false);
//        v1.setTransportStatus(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT);
        
        
        //transport
        t1.setTransportId(0);
        t1.setFromStreet("Wienerstr. 46");
        t1.setFromCity("Kapfenberg");
        t1.setToStreet("LKH Bruck");
        t1.setToCity("Unf. Amb.");
        t1.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
        t1.setDirection(IDirectness.TOWARDS_BRUCK);
        t1.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
        
        t2.setTransportId(0);
        t2.setFromStreet("Wienerstr. 47");
        t2.setFromCity("Kapfenberg");
        t2.setToStreet("LKH Bruck");
        t2.setToCity("Unf. Amb.");
        t2.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
        t2.setDirection(IDirectness.TOWARDS_BRUCK);
        t2.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
	}
	
	@After
	public void tearDown()
	{
		t1.getStatusMessages().clear();
	}
	
	@Test
	public void test1()
	{
		t1.addStatus(0, new Date().getTime());
		int mismoot1 = t1.getMostImportantStatusMessageOfOneTransport();//return 0
		
		t2.addStatus(1, new Date().getTime());
		int mismoot2 = t1.getMostImportantStatusMessageOfOneTransport();//return 1
				
		list.add(mismoot1);
		list.add(mismoot2);
		
		int result = getMostImportantStatusOfOneVehicle();
		
		assertEquals(20,result);
	}
	
	@Test
	public void test2()
	{
		t1.addStatus(0, new Date().getTime());
		int mismoot1 = t1.getMostImportantStatusMessageOfOneTransport();//return 0
		
		t2.addStatus(1, new Date().getTime());
		int mismoot2 = t2.getMostImportantStatusMessageOfOneTransport();//return 1
				
		System.out.println("mismoot1: " +mismoot1);
		System.out.println("mismoot2: " +mismoot2);
		list.add(mismoot1);
		list.add(mismoot2);
		
		int result = getMostImportantStatusOfOneVehicle();
		
		assertEquals(20,result);
	}
	
	
	
	private int getMostImportantStatusOfOneVehicle()
	{
		//for a 'red' status
		if (list.contains(TRANSPORT_STATUS_START_WITH_PATIENT))
				return 10;
		else if(list.contains(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
			return TRANSPORT_STATUS_OUT_OF_OPERATION_AREA;
		//for a 'yellow' status
		else return 20;
		
		//green (30) is for a 'underway'(programstatus) vehicle not possible
		
	}
}
