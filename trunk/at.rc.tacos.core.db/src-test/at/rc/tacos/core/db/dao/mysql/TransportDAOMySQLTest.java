package at.rc.tacos.core.db.dao.mysql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.CallerDAO;
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


public class TransportDAOMySQLTest extends DBTestBase
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


	Transport  transport1 = new Transport("vonStraﬂe1","vonStadt1",location1,MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("28-01-2008 12:00", MyUtils.timeAndDateFormat),"A",2);
	Transport  transport2 = new Transport("vonStraﬂe2","vonStadt2",location2,MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("28-01-2008 14:00", MyUtils.timeAndDateFormat),"B",2);

	CallerDetail caller1 = new CallerDetail("derCaller","0664-4143824");

	@Before
	public void setUp() 
	{
		System.out.println("setup von transportdaomysqltest");
		//insert the phones
		int phoneId1 = mobilePhoneDAO.addMobilePhone(phone1);
		int phoneId2 = mobilePhoneDAO.addMobilePhone(phone2);
		//set the inserted ids
		phone1.setId(phoneId1);
		phone2.setId(phoneId2);

		//insert the competences
		int compId1 = competenceDAO.addCompetence(comp1);
		int compId2 = competenceDAO.addCompetence(comp2);
		//set the inserted ids
		comp1.setId(compId1);
		comp2.setId(compId2);

		//insert locations
		int id1 = locationDAO.addLocation(location1);
		int id2 = locationDAO.addLocation(location2);
		//set the ids
		location1.setId(id1);
		location2.setId(id2);

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
		transport1.setCallerDetail(caller1);
		long creationTime = Calendar.getInstance().getTimeInMillis();
		transport1.setCreationTime(creationTime);
		transport2.setCreationTime(creationTime);
		transport1.setPlanedLocation(location1);
		transport2.setPlanedLocation(location2);


		int tr1id = transportDAO.addTransport(transport1);
		int tr2id = transportDAO.addTransport(transport2);
		System.out.println("Transport1 id: " +tr1id);
		System.out.println("Transport2 id: " +tr2id);
		transport1.setTransportId(tr1id);
		transport2.setTransportId(tr2id); 
		System.out.println(tr1id);
		System.out.println(tr2id);
		
		//vehicle
		veh1.setBasicStation(location1);
		veh1.setCurrentStation(location1);
		veh1.setDriver(member1);
		veh1.setMobilPhone(phone1);
		veh1.setReadyForAction(true);
		veh1.setTransportStatus(10);
		veh1.setVehicleName("Bm09");
		veh1.setVehicleType("KTW");
		
		veh2.setBasicStation(location1);
		veh2.setCurrentStation(location1);
		veh2.setDriver(member1);
		veh2.setMobilPhone(phone1);
		veh2.setReadyForAction(true);
		veh2.setTransportStatus(10);
		veh2.setVehicleName("Bm09");
		veh2.setVehicleType("KTW");
		
		boolean checkVeh1 = vehicleDAO.addVehicle(veh1);
		boolean checkVeh2 = vehicleDAO.addVehicle(veh2);
		
	}

	@After
	public void tearDown()
	{
		deleteTable(TransportDAO.TABLE_DEPENDENT_ASSIGNED_VEHICLES);
		deleteTable(TransportDAO.TABLE_NAME);
		deleteTable(MobilePhoneDAO.TABLE_NAME);
		deleteTable(LocationDAO.TABLE_NAME);
		deleteTable(CallerDAO.TABLE_NAME);
		deleteTable(UserLoginDAO.TABLE_NAME);
		deleteTable(StaffMemberDAO.TABLE_NAME);
		deleteTable(CompetenceDAO.TABLE_NAME);
	}


	@Test
	public void testInsertTransport()
	{
		//fromStreet, fromCity, plannedLocation, dateOfTransport, plannedStartOfTransport, transportPriority, direction
		Transport  transport3 = new Transport("vonStraﬂe3","vonStadt3",location1,MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("29-01-2008 14:00", MyUtils.timeAndDateFormat),"C",2);

		long dateTime1 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);
		long dateTime2 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);

		transport3.setCreationTime(dateTime1);
		transport3.setAppointmentTimeAtDestination(dateTime2);
		transport3.setAssistantPerson(true);
		transport3.setBackTransport(true);
		transport3.setBlueLightToGoal(true);
		transport3.setBrkdtAlarming(true);

		
		transport3.setCallerDetail(caller1);
		transport3.setCreatedByUsername("user1");
		transport3.setDfAlarming(true);
		transport3.setDirection(2);
		transport3.setEmergencyDoctorAlarming(true);
		transport3.setEmergencyPhone(true);
		transport3.setFeedback("feedbackNew");
		transport3.setFirebrigadeAlarming(true);
		transport3.setHelicopterAlarming(true);
		transport3.setKindOfIllness("Schlaganfall");
		transport3.setKindOfTransport("Tragsessel");
		transport3.setLongDistanceTrip(true);
		transport3.setMountainRescueServiceAlarming(true);
		transport3.setNotes("thenotes");

		Patient patient1 = new Patient("Muster","Max");
		transport3.setPatient(patient1);

		transport3.setPlannedTimeAtPatient(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat));
		transport3.setPoliceAlarming(true);
		transport3.setProgramStatus(1);
		transport3.setToCity("toCity");
		transport3.setToStreet("toStreet");
		//set transport number not possible
		transport3.setTransportPriority("C");
		//set transport year not possible

		//realLocation and vehicle is for a new transport not possible

		//insert the transport
		int trId3 = transportDAO.addTransport(transport3);
		
		System.out.println("iiiiiiiiiiiiiiiiiiiii transportid3: " +trId3);

		Transport transport4 = transportDAO.getTransportById(trId3);

		assertEquals(location1,transport4.getPlanedLocation());
		assertEquals(dateTime1,transport4.getCreationTime());
		assertEquals(dateTime2,transport4.getAppointmentTimeAtDestination());
		assertEquals(caller1,transport4.getCallerDetail());
		assertEquals("user1",transport4.getCreatedByUsername());
		assertEquals(MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat),transport4.getDateOfTransport());
		assertEquals(MyUtils.stringToTimestamp("29-01-2008 14:00", MyUtils.timeAndDateFormat),transport4.getPlannedStartOfTransport());
		assertEquals(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat),transport4.getPlannedTimeAtPatient());
		assertEquals(2,transport4.getDirection());
		assertEquals("feedbackNew",transport4.getFeedback());
		assertEquals("vonStadt3",transport4.getFromCity());
		assertEquals("vonStraﬂe3",transport4.getFromStreet());
		assertEquals("Schlaganfall",transport4.getKindOfIllness());
		assertEquals("Tragsessel",transport4.getKindOfTransport());
		assertEquals("thenotes",transport4.getNotes());
		assertEquals(patient1,transport4.getPatient());
		assertEquals("toCity",transport4.getToCity());
		assertEquals("toStreet",transport4.getToStreet());
		assertEquals("C",transport4.getTransportPriority());
		assertEquals(1,transport4.getProgramStatus());

		assertTrue(transport4.isAssistantPerson());
		assertTrue(transport4.isBackTransport());
		assertTrue(transport4.isBlueLightToGoal());
		assertTrue(transport4.isBrkdtAlarming());
		assertTrue(transport4.isDfAlarming());
		assertTrue(transport4.isEmergencyDoctorAlarming());
		assertTrue(transport4.isEmergencyPhone());
		assertTrue(transport4.isFirebrigadeAlarming());
		assertTrue(transport4.isHelicopterAlarming());
		assertTrue(transport4.isLongDistanceTrip());
		assertTrue(transport4.isMountainRescueServiceAlarming());
		assertTrue(transport4.isPoliceAlarming());
		
		
		//add a second transport
		Transport  transport7 = new Transport("vonStraﬂe2","vonStadt2",location1,MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("29-01-2008 14:00", MyUtils.timeAndDateFormat),"C",2);

		transport7.setCreationTime(dateTime1);
		transport7.setAppointmentTimeAtDestination(dateTime2);
		transport7.setAssistantPerson(true);
		transport7.setBackTransport(true);
		transport7.setBlueLightToGoal(true);
		transport7.setBrkdtAlarming(true);
		
		transport7.setCallerDetail(caller1);
		transport7.setCreatedByUsername("user2");
		transport7.setDfAlarming(true);
		transport7.setDirection(2);
		transport7.setEmergencyDoctorAlarming(true);
		transport7.setEmergencyPhone(true);
		transport7.setFeedback("feedbackNew");
		transport7.setFirebrigadeAlarming(true);
		transport7.setHelicopterAlarming(true);
		transport7.setKindOfIllness("Schlaganfall");
		transport7.setKindOfTransport("Tragsessel");
		transport7.setLongDistanceTrip(true);
		transport7.setMountainRescueServiceAlarming(true);
		transport7.setNotes("thenotes");
		Patient patient2 = new Patient("Muster","Max");
		transport7.setPatient(patient2);

		transport7.setPlannedTimeAtPatient(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat));
		transport7.setPoliceAlarming(true);
		transport7.setProgramStatus(1);
		transport7.setToCity("toCity");
		transport7.setToStreet("toStreet");
		//set transport number not possible
		transport7.setTransportPriority("C");
		//set transport year not possible

		//realLocation and vehicle is for a new transport not possible

		//insert the transport
		int trId7 = transportDAO.addTransport(transport7);
		System.out.println("iiiiiiiiiiiiiiiiiiiii transportid7: " +trId7);
		
	}


	@Test
	public void testListTransports()
	{
		long startTime = MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat);
		//set the end date to date +1
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat));
		cal.add(Calendar.DAY_OF_MONTH, 1);
		long endTime = cal.getTimeInMillis();
		List<Transport> list = new ArrayList<Transport>();
		list = transportDAO.listTransportsByDateOfTransport(startTime, endTime);
		Assert.assertEquals(2, list.size());
	}


	@Test
	public void testUpdateTranpsport()
	{
		{
			Transport transport = transportDAO.getTransportById(transport1.getTransportId());
			System.out.println("test transportid: "+transport.getTransportId());
			transport.setCreationTime(Calendar.getInstance().getTimeInMillis());
			transport.setAppointmentTimeAtDestination(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat));
			transport.setAssistantPerson(true);
			transport.setBackTransport(true);
			transport.setBlueLightToGoal(true);
			transport.setBrkdtAlarming(true);

			transport.setCallerDetail(caller1);
			transport.setCreatedByUsername("user2");
			transport.setDateOfTransport(MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat));
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

			Patient patient1 = new Patient("Max","Muster");
			transport.setPatient(patient1);

			transport.setPlanedLocation(location2);

			transport.setPlannedStartOfTransport(MyUtils.stringToTimestamp("29-01-2008 15:00", MyUtils.timeAndDateFormat));
			transport.setPlannedTimeAtPatient(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat));
			transport.setPoliceAlarming(true);
			transport.setProgramStatus(1);
			//will be set when a vehicle is assigned
			//transport.setRealLocation(location1);
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
			assertEquals(transport1.getTransportId(),transport.getTransportId());
			assertEquals(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat),transport.getAppointmentTimeAtDestination());
			assertEquals("derCaller",transport.getCallerDetail().getCallerName());
			assertEquals("user2",transport.getCreatedByUsername());
//			assertEquals(cal2,transport.getDateOfTransport());//TODO????
			assertEquals(2,transport.getDirection());
			assertEquals("feedbackNew",transport.getFeedback());
			assertEquals("fromCity1",transport.getFromCity());
			assertEquals("fromStreet1",transport.getFromStreet());
			assertEquals("Schlaganfall",transport.getKindOfIllness());
			assertEquals("mobil",transport.getKindOfTransport());
			assertEquals("thenotes",transport.getNotes());
			assertEquals("Max",transport.getPatient().getFirstname());
			assertEquals("Muster",transport.getPatient().getLastname());
			assertEquals("location2",transport.getPlanedLocation().getLocationName());
			//assertEquals("location1",transport.getRealLocation().getLocationName());
			assertEquals("toCity",transport.getToCity());
			assertEquals("toStreet",transport.getToStreet());
			assertEquals("C",transport.getTransportPriority());
		}
	}


//	@Test
//	public void testArchiveTransport()
//	{

//	}


//	@Test
//	public void testGetNewTransportNrForLocation()
//	{

//	}


//	@Test
//	public void testArchiveTransportNumber()
//	{

//	}


	@Test
	public void testAssignVehicleToTransport()
	{
		{
			Transport  transport3 = new Transport("vonStraﬂe3","vonStadt3",location1,MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("29-01-2008 14:00", MyUtils.timeAndDateFormat),"C",2);

			long dateTime1 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);
			long dateTime2 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);

			transport3.setCreationTime(dateTime1);
			transport3.setAppointmentTimeAtDestination(dateTime2);
			transport3.setAssistantPerson(true);
			transport3.setBackTransport(true);
			transport3.setBlueLightToGoal(true);
			transport3.setBrkdtAlarming(true);

			transport3.setCallerDetail(caller1);
			transport3.setCreatedByUsername("user2");
			transport3.setDfAlarming(true);
			transport3.setDirection(2);
			transport3.setEmergencyDoctorAlarming(true);
			transport3.setEmergencyPhone(true);
			transport3.setFeedback("feedbackNew");
			transport3.setFirebrigadeAlarming(true);
			transport3.setHelicopterAlarming(true);
			transport3.setKindOfIllness("Schlaganfall");
			transport3.setKindOfTransport("Tragsessel");
			transport3.setLongDistanceTrip(true);
			transport3.setMountainRescueServiceAlarming(true);
			transport3.setNotes("thenotes");

			Patient patient1 = new Patient("Muster","Max");
			transport3.setPatient(patient1);

			transport3.setPlannedTimeAtPatient(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat));
			transport3.setPoliceAlarming(true);
			transport3.setProgramStatus(1);
			transport3.setToCity("toCity");
			transport3.setToStreet("toStreet");
			//set transport number not possible
			transport3.setTransportPriority("C");
			//set transport year not possible



			//insert the transport
			int trId3 = transportDAO.addTransport(transport3);

			Transport transportNew = transportDAO.getTransportById(trId3);
			
			transportNew.setVehicleDetail(veh2);

			int trNumber0 = transportDAO.assignVehicleToTransport(transportNew);

			Transport transport5 = transportDAO.getTransportById(trId3);

			System.out.println("transportID (old)eins :..::::: " +trId3);

			System.out.println("Transportnumber: ˆˆˆˆˆˆˆˆˆˆˆˆˆˆ " +transport5.getTransportNumber());
			System.out.println("Transportnumber: ˆˆˆˆˆˆˆˆˆˆˆˆˆˆ " +trNumber0);
			
			
			assertEquals(veh2,transport5.getVehicleDetail());
			assertNotSame(0,trNumber0);
			
		}
		
		{
			Transport  transport3 = new Transport("vonStraﬂe4","vonStadt4",location1,MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("29-01-2008 14:00", MyUtils.timeAndDateFormat),"C",2);

			long dateTime1 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);
			long dateTime2 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);

			transport3.setCreationTime(dateTime1);
			transport3.setAppointmentTimeAtDestination(dateTime2);
			transport3.setAssistantPerson(true);
			transport3.setBackTransport(true);
			transport3.setBlueLightToGoal(true);
			transport3.setBrkdtAlarming(true);

			CallerDetail caller1Detail = new CallerDetail("anrufer1","0664-132435");
			transport3.setCallerDetail(caller1Detail);
			transport3.setCreatedByUsername("user1");//to differ between the two transports
			transport3.setDfAlarming(true);
			transport3.setDirection(2);
			transport3.setEmergencyDoctorAlarming(true);
			transport3.setEmergencyPhone(true);
			transport3.setFeedback("feedbackNew");
			transport3.setFirebrigadeAlarming(true);
			transport3.setHelicopterAlarming(true);
			transport3.setKindOfIllness("Schlaganfall");
			transport3.setKindOfTransport("Tragsessel");
			transport3.setLongDistanceTrip(true);
			transport3.setMountainRescueServiceAlarming(true);
			transport3.setNotes("thenotes");

			Patient patient1 = new Patient("Muster","Max");
			transport3.setPatient(patient1);

			transport3.setPlannedTimeAtPatient(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat));
			transport3.setPoliceAlarming(true);
			transport3.setProgramStatus(1);
			transport3.setToCity("toCity");
			transport3.setToStreet("toStreet");
			//set transport number not possible
			transport3.setTransportPriority("C");
			//set transport year not possible



			//insert the transport
			int trId3 = transportDAO.addTransport(transport3);

			//assign the vehicle
			VehicleDetail veh2 = new VehicleDetail();
			veh2.setBasicStation(location1);
			veh2.setCurrentStation(location1);//same location as first transport --> transport number +1
			veh2.setDriver(member1);
			veh2.setMobilPhone(phone1);
			veh2.setReadyForAction(true);
			veh2.setTransportStatus(10);
			veh2.setVehicleName("Bm09");
			veh2.setVehicleType("KTW");

			Transport transportBack = transportDAO.getTransportById(trId3);
			
			transportBack.setVehicleDetail(veh2);

			int trNumber0 = transportDAO.assignVehicleToTransport(transportBack);

			Transport transport5 = transportDAO.getTransportById(trId3);

			System.out.println("transportID (old) zwei :..::::: " +trId3);

			System.out.println("Transportnumber: ˆˆˆˆˆˆˆˆˆˆˆˆˆˆ " +transport5.getTransportNumber());
			System.out.println("Transportnumber: ˆˆˆˆˆˆˆˆˆˆˆˆˆˆ " +trNumber0);
			
			
			assertEquals(veh2,transport5.getVehicleDetail());
			assertNotSame(0,trNumber0);
		}
	}

//	@Test
//	public void testRemoveVehicleFromTransport()
//	{

//	}


//	@Test
//	public void testCancelTransport()
//	{


//	}


//	@Test
//	public void testGetTransportsFromVehicle()
//	{

//	}


//	@Test
//	public void testListArchivedTransports()
//	{

//	}

//	@Test
//	public void testAddTransportState()
//	{

//	}

//	@Test
//	public void testAssignTransportstate()
//	{

//	}

//	@Test
//	public void testRemoveTransportstate()
//	{

//	}

//	@Test
//	public void testUpdateTransportstate()
//	{

//	}

}
