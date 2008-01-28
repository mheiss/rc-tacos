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
import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.TransportDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;


public class TransportDAOMySQLTest extends DBTestBase implements IProgramStatus 
{
	private final VehicleDAO vehicleDAO = DaoFactory.MYSQL.createVehicleDetailDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final TransportDAO transportDAO = DaoFactory.MYSQL.createTransportDAO();
	private final UserLoginDAO loginDAO = DaoFactory.MYSQL.createUserDAO();
	private final StaffMemberDAO staffDAO = DaoFactory.MYSQL.createStaffMemberDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
    
    MobilePhoneDetail phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("phone2","0664-987654321");
    
    Location location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
    Location location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
    
    VehicleDetail veh1 = new VehicleDetail("veh1","vehicleType1",location1);
    VehicleDetail veh2 = new VehicleDetail("veh2","vehicleType2",location2);
    
    Competence comp1 = new Competence("comp1");
    Competence comp2 = new Competence("comp2");
    
    StaffMember member1 = new StaffMember(50100001,"fname1","lname1","user1","street1","city1",false,MyUtils.stringToTimestamp("27-01-2008",MyUtils.dateFormat),phone1,comp1,"mail1",location1);
    StaffMember member2 = new StaffMember(50100002,"fname2","lname2","user2","street2","city2",true,MyUtils.stringToTimestamp("28-01-2008",MyUtils.dateFormat),phone2,comp2,"mail2",location2);

   
    
  //logins
	Login login1 = new Login("user1","password1",false);
	Login login2 = new Login("user2","password2",false);

	
	GregorianCalendar gcal = new GregorianCalendar();
	long cal = (gcal.getTimeInMillis()-10);
	
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
        
        //insert the competences
        int compId1 = competenceDAO.addCompetence(comp1);
        int compId2 = competenceDAO.addCompetence(comp2);
        //set the inserted ids
        comp1.setId(compId1);
        comp2.setId(compId2);
        
        //staff member
        
        //login
        login1.setUserInformation(member1);
        login1.setIslocked(false);
        login1.setAuthorization("Administrator");
        login2.setUserInformation(member2);
        login2.setIslocked(true);
        login2.setAuthorization("User");
        loginDAO.addLogin(login1);
        loginDAO.addLogin(login2);
        
        //insert transports
        transport1.setCreatedByUsername(login1.getUsername());
        transport2.setCreatedByUsername(login2.getUsername());
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
        deleteTable(UserLoginDAO.TABLE_NAME);
        deleteTable(StaffMemberDAO.TABLE_NAME);
    }
    


	

	 @Test
	 public void testListTransports()
	 {
		 
		 List<Transport> list = transportDAO.listTransports(cal, cal);
         Assert.assertEquals(2, list.size());
	 }
	 
	 
	 @Test
	 public void testUpdateTranpsport()
	 {
		 {
			 Transport transport = transportDAO.getTransportById(transport1.getTransportId());
			 long cal2 = gcal.getTimeInMillis();
			 transport.setCreationTime(cal2);
			 transport.setAppointmentTimeAtDestination(cal2);
			 transport.setAssistantPerson(true);
			 transport.setBackTransport(true);
			 transport.setBlueLightToGoal(true);
			 transport.setBrkdtAlarming(true);
			 
			 CallerDetail caller1Detail = new CallerDetail("anrufer1","0664-132435");
			 transport.setCallerDetail(caller1Detail);
			 transport.setCreatedByUsername("crUser");
			 transport.setCreationTime(cal2);
			 transport.setDateOfTransport(cal2);
			 transport.setDfAlarming(true);
			 transport.setDirection(2);
			 transport.setEmergencyDoctorAlarming(true);
			 transport.setEmergencyPhone(true);
			 transport.setFeedback("feedbackNew");
			 transport.setFirebrigadeAlarming(true);
			 transport.setFromCity("fromCity1");
			 transport.setFromStreet("fromStreet1");
			 transport.setHelicopterAlarming(true);
			 transport.setKindOfIllness("Schlaganfall");
			 transport.setKindOfTransport("mobil");
			 transport.setLongDistanceTrip(true);
			 transport.setMountainRescueServiceAlarming(true);
			 transport.setNotes("thenotes");
			 
			 Patient patient1 = new Patient("Muster","Max");
			 transport.setPatient(patient1);
			 
			 Location planedLocation = new Location("Bruck1",phone1,"street1","241",8601,"BruckCity1", "theNotes1");
			 transport.setPlanedLocation(planedLocation);
			 Location realLocation = new Location("Bruck2",phone2,"street2","242",8602,"BruckCity2", "theNotes2");
			 
			 transport.setPlannedStartOfTransport(cal2);
			 transport.setPlannedTimeAtPatient(cal2);
			 transport.setPoliceAlarming(true);
			 transport.setProgramStatus(1);
			 transport.setRealLocation(realLocation);
			 transport.setToCity("toCity");
			 transport.setToStreet("toStreet");
			 //set transport number not possible
			 transport.setTransportPriority("C");
			 
			 transport.setVehicleDetail(veh1);
			 //set transport year not possible
	        
			 transportDAO.updateTransport(transport);
	       
	      }
	      {
	    	  Transport transport = transportDAO.getTransportById(transport1.getTransportId());
	    	  
	    	 
	        assertNotSame(cal,transport.getAppointmentTimeAtDestination());
	        assertEquals("anrufer1",transport.getCallerDetail().getCallerName());
	        assertEquals("crUser",transport.getCreatedByUsername());
	        assertNotSame(cal,transport.getCreationTime());
//	        assertEquals(cal2,transport.getDateOfTransport());//TODO????
	        assertEquals(2,transport.getDirection());
	        assertEquals("feedbackNew",transport.getFeedback());
	        assertEquals("fromCity1",transport.getFromCity());
	        assertEquals("fromStreet1",transport.getFromStreet());
	        assertEquals("Schlaganfall",transport.getKindOfIllness());
	        assertEquals("mobil",transport.getKindOfTransport());
	        assertEquals("thenotes",transport.getNotes());
	        assertEquals("Muster",transport.getPatient().getLastname());
	        assertEquals("Bruck1",transport.getPlanedLocation().getLocationName());
	        assertNotSame(cal,transport.getPlannedStartOfTransport());
	        assertNotSame(cal,transport.getPlannedTimeAtPatient());
	        assertEquals("Bruck2",transport.getRealLocation().getLocationName());
	        assertEquals("toCity",transport.getToCity());
	        assertEquals("toStreet",transport.getToStreet());
	        assertEquals("C",transport.getTransportPriority());
	        //vehicle detail
	        
	        
	   
	      }
	 }
	 

//	 @Test
//	 public void testArchiveTransport()
//	 {
//		 
//	 }
//	 
//	 
//	 @Test
//	 public void testGetNewTransportNrForLocation()
//	 {
//		 
//	 }
//	 
//
//	 @Test
//	 public void testArchiveTransportNumber()
//	 {
//		 
//	 }
//	 
//
//	 @Test
//	 public void testAssignVehicleToTransport()
//	 {
//		 
//
//	 }
//	 
//	 @Test
//	 public void testRemoveVehicleFromTransport()
//	 {
//		 
//	 }
//	 
//	 
//	 @Test
//	 public void testCancelTransport()
//	 {
//		 
//		
//	 }
//	 
//	 
//	 @Test
//	 public void testGetTransportsFromVehicle()
//	 {
//		 
//	 }
//	 
//	 
//	 @Test
//	 public void testListArchivedTransports()
//	 {
//		 
//	 }
//
//	 @Test
//	 public void testAddTransportState()
//	 {
//		 
//	 }
//	 
//	 @Test
//	 public void testAssignTransportstate()
//	 {
//		 
//	 }
//	 
//	 @Test
//	 public void testRemoveTransportstate()
//	 {
//		 
//	 }
//	 
//	 @Test
//	 public void testUpdateTransportstate()
//	 {
//		 
//	 }
	
}
