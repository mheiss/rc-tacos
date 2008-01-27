package at.rc.tacos.core.db.dao.mysql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;


public class TransportDAOMySQLTest implements IProgramStatus
{
	 private final TransportDAO transportDAO = DaoFactory.MYSQL.createTransportDAO();
	 Transport transport;
	 int transportId = 0;

	 
	 @Test
	 public void testAddTransport()
	 {
		 Location location = new Location();
		 location.setLocationName("Kapfenberg");
		 GregorianCalendar gcal = new GregorianCalendar();
		 long cal = gcal.getTimeInMillis();
		 transport = new Transport("vonStraﬂe","vonStadt",location,cal,cal,"A",2);
		
		 transportId = transportDAO.addTransport(transport);
		 
		 assertNotSame("Expected and actual value not the same", 0, transportId);
	 }

	 /**
	     * Assigns a transport to a vehicle and returns the transport number.
	     * The returned transport number is not unique and individual for each location.
	     * The transport number is calculated by the current year and the highest not cancled transport number of a station.
	     * @param transport the transport to assign
	     * @return the transport number.
	     */
	 @Test
	 public void testAssignVehicleToTransport()
	 {
//		 Location location = new Location();
//		 location.setLocationName("Kapfenberg");
//		 
//		 VehicleDetail veh = new VehicleDetail();
//		 veh.setBasicStation(location);
//		 veh.setCurrentStation(location);
//		 
//		 StaffMember staff = new StaffMember();
//		 staff.setLastName("Heiﬂ");
//		 staff.setFirstName("Michael");
//		 staff.setUserName("m.heiﬂ");
//		 
//		 veh.setDriver(staff);
//		 veh.setReadyForAction(true);
//		 veh.setTransportStatus(30);//green
//		 veh.setVehicleName("Ka04");
//		 veh.setVehicleType("RTW");
		 
		 int number = transportDAO.assignVehicleToTransport(transport);//TODO assign vehicle?
		 
		 
		 assertNotNull(number);
	 }
	 
//	 @Test
//	 public void testArchiveTransport()
//	 {
//		 
//	 }
//	 
	 
	 /**    
	     * Updates a given transport.<br>
	     * This method updates every column of the transport table except the transport number.
	     * @param transport the transport to update
	     * @return true if the update was successfull.
	     */
	 @Test
	 public void testupdateTranpsort()
	 {
		 boolean check =  transportDAO.updateTransport(transport);
		 assertTrue(check);
	 }
	 

	 @Test
	 public void testCancelTransport()
	 {
		 
		 boolean check = transportDAO.cancelTransport(transportId);
		 assertTrue(check);
	 }
	 
	 @Test
	 public void testListTransports()
	 {
		 List<Transport> list = new ArrayList<Transport>();
		 GregorianCalendar gcal = new GregorianCalendar();
		 gcal.set(GregorianCalendar.DAY_OF_MONTH, 1);
		 long date1 = gcal.getTimeInMillis();
		 gcal.set(GregorianCalendar.DAY_OF_MONTH,28);
		 long date2 = gcal.getTimeInMillis();
		 list = transportDAO.listTransports(date1, date2);
		 assertNotSame(0, list.size());
	 }
	 
	 /**
	     * Returns a list of all transports accociated with the given vehicle
	     * and with the program status <code>IProgramStatus.PROGRAM_STATUS_UNDERWAY</code>
	     * @param vehicleName the name of the vehicle to get the transports from
	     * @return the list of transports
	     */
	 @Test
	 public void testGetTransportsFromVehicle()
	 {
		 
		 Location location = new Location();
		 location.setLocationName("Kapfenberg");
		 
		 VehicleDetail veh = new VehicleDetail();
		 veh.setBasicStation(location);
		 veh.setCurrentStation(location);
		 
		 StaffMember staff = new StaffMember();
		 staff.setLastName("Heiﬂ");
		 staff.setFirstName("Michael");
		 staff.setUserName("m.heiﬂ");
		 
		 veh.setDriver(staff);
		 veh.setReadyForAction(true);
		 veh.setTransportStatus(30);//green
		 veh.setVehicleName("Ka04");
		 veh.setVehicleType("RTW");
		 List<Transport> list = new ArrayList<Transport>();
		 
		 list = transportDAO.getTransportsFromVehicle(veh.getVehicleName());
		 
		 assertNotSame(0,list.size());
	 }


	
	
}
