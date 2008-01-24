package at.rc.tacos.model;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportPriority;

public class TransportTest implements IProgramStatus
{
	private Transport t1 = new Transport();
	
	@Before
	public void setUp()
	{
        t1.setTransportId(0);
        t1.setFromStreet("Wienerstr. 46");
        t1.setFromCity("Kapfenberg");
        t1.setToStreet("LKH Bruck");
        t1.setToCity("Unf. Amb.");
        t1.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
        t1.setDirection(IDirectness.TOWARDS_BRUCK);
        t1.setProgramStatus(PROGRAM_STATUS_UNDERWAY);
	}
	
	@After
	public void tearDown()
	{
		t1.getStatusMessages().clear();
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS0()
	{
		t1.addStatus(0, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(0,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS1()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(1,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS2()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(2,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS3()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(3,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS4()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		t1.addStatus(4, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(4,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS5()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		t1.addStatus(4, new Date().getTime());
		t1.addStatus(5, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(5,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS6()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		t1.addStatus(4, new Date().getTime());
		t1.addStatus(5, new Date().getTime());
		t1.addStatus(6, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(6,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS7()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		t1.addStatus(4, new Date().getTime());
		t1.addStatus(5, new Date().getTime());
		t1.addStatus(6, new Date().getTime());
		t1.addStatus(7, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(7,mismoot);		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS8()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		t1.addStatus(4, new Date().getTime());
		t1.addStatus(5, new Date().getTime());
		t1.addStatus(6, new Date().getTime());
		t1.addStatus(7, new Date().getTime());
		t1.addStatus(8, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(4,mismoot);//return the highest status of S0 to S4		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS8Sec()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		//S4 not set
		t1.addStatus(5, new Date().getTime());
		t1.addStatus(6, new Date().getTime());
		t1.addStatus(7, new Date().getTime());
		t1.addStatus(8, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(3,mismoot);//return the highest status of S0 to S4		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS8Tert()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		//S3 not set
		//S4 not set
		t1.addStatus(5, new Date().getTime());
		t1.addStatus(6, new Date().getTime());
		t1.addStatus(7, new Date().getTime());
		t1.addStatus(8, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(2,mismoot);//return the highest status of S0 to S4		
	}
	
	@Test
	public void testMostImportantStatusMessageOfOneTransportS9()
	{
		t1.addStatus(0, new Date().getTime());
		t1.addStatus(1, new Date().getTime());
		t1.addStatus(2, new Date().getTime());
		t1.addStatus(3, new Date().getTime());
		t1.addStatus(4, new Date().getTime());
		t1.addStatus(5, new Date().getTime());
		t1.addStatus(6, new Date().getTime());
		t1.addStatus(7, new Date().getTime());
		t1.addStatus(8, new Date().getTime());
		t1.addStatus(9, new Date().getTime());
		int mismoot = t1.getMostImportantStatusMessageOfOneTransport();
		 
		assertEquals(9,mismoot);		
	}
	
	
	
	
	
}
