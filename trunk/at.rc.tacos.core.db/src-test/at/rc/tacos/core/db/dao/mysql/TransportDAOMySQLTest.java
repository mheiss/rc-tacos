package at.rc.tacos.core.db.dao.mysql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;


public class TransportDAOMySQLTest extends DBTestBase implements IProgramStatus 
{
	private final VehicleDAO vehicleDAO = DaoFactory.MYSQL.createVehicleDetailDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final TransportDAO transportDAO = DaoFactory.MYSQL.createTransportDAO();
    
    MobilePhoneDetail phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("phone2","0664-987654321");
    
    Location location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
    Location location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
    
    VehicleDetail veh1 = new VehicleDetail("veh1","vehicleType1",location1);
    VehicleDetail veh2 = new VehicleDetail("veh2","vehicleType2",location2);
	
	GregorianCalendar gcal = new GregorianCalendar();
	long cal = gcal.getTimeInMillis();
	Transport  transport1 = new Transport("vonStraﬂe1","vonStadt1",location1,cal,cal,"A",2);
	Transport  transport2 = new Transport("vonStraﬂe2","vonStadt2",location1,cal,cal,"B",2);
	
	
	
    @Before
    public void setUp() 
    {
    	//insert the phones
        int phoneId1 = mobilePhoneDAO.addMobilePhone(phone1);
        int phoneId2 = mobilePhoneDAO.addMobilePhone(phone2);
        //set the inserted ids
        phone1.setId(phoneId1);
        phone2.setId(phoneId2);
        //insert locations
        int id1 = locationDAO.addLocation(location1);
        int id2 = locationDAO.addLocation(location2);
        //set the ids
        location1.setId(id1);
        location2.setId(id2);
        //insert transports
        int tr1id = transportDAO.addTransport(transport1);
        int tr2id = transportDAO.addTransport(transport2);
        transport1.setTransportId(tr1id);
        transport2.setTransportId(tr2id);        
    }
    
    @After
    public void tearDown()
    {
    	deleteTable(MobilePhoneDAO.TABLE_NAME);
    	deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(TransportDAO.TABLE_NAME);
    }
    


	

	 @Test
	 public void testListTransports()
	 {
		 
		 List<Transport> list = transportDAO.listTransports(cal, cal);
         Assert.assertEquals(2, list.size());
	 }
	 
	 
	 @Test
	 public void testUpdateTranpsort()
	 {
		 {
			 Transport transport = transportDAO.g
	        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
	        	vehicle.setVehicleType("newType");
	        	vehicle.setOutOfOrder(true);
	        	vehicle.setReadyForAction(true);
	        	vehicle.setVehicleNotes("newNotes");
	        	vehicleDAO.updateVehicle(vehicle);
	        }
	        {
	        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
	        	assertEquals("vehicle1",vehicle.getVehicleName());
	        	assertEquals("newType",vehicle.getVehicleType());
	        	assertEquals("newNotes",vehicle.getVehicleType());
	        	assertTrue(vehicle.isOutOfOrder());
	        	assertTrue(vehicle.isReadyForAction());
	        }
	 }
	 

	 @Test
	 public void testArchiveTransport()
	 {
		 
	 }
	 
	 
	 @Test
	 public void testGetNewTransportNrForLocation()
	 {
		 
	 }
	 

	 @Test
	 public void testArchiveTransportNumber()
	 {
		 
	 }
	 

	 @Test
	 public void testAssignVehicleToTransport()
	 {
		 

	 }
	 
	 @Test
	 public void testRemoveVehicleFromTransport()
	 {
		 
	 }
	 
	 
	 @Test
	 public void testCancelTransport()
	 {
		 
		
	 }
	 
	 
	 @Test
	 public void testGetTransportsFromVehicle()
	 {
		 
	 }
	 
	 
	 @Test
	 public void testListArchivedTransports()
	 {
		 
	 }

	 @Test
	 public void testAddTransportState()
	 {
		 
	 }
	 
	 @Test
	 public void testAssignTransportstate()
	 {
		 
	 }
	 
	 @Test
	 public void testRemoveTransportstate()
	 {
		 
	 }
	 
	 @Test
	 public void testUpdateTransportstate()
	 {
		 
	 }
	
}
