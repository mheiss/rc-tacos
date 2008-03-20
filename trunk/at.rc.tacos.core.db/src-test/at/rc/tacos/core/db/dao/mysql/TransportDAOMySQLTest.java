package at.rc.tacos.core.db.dao.mysql;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
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
import at.rc.tacos.model.Disease;
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

	//the test data
	MobilePhoneDetail phone1,phone2, phone3, phone4;
	Location location1,location2, location3, location4;
	VehicleDetail veh1,veh2;
	Competence comp1,comp2;	
	StaffMember member1,member2;
	Login login1,login2;
	Transport transport1,transport2;
	CallerDetail caller1;

	@Before
	public void setUp() throws SQLException
	{
		phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
		phone2 = new MobilePhoneDetail("phone2","0664-987654321");
		phone3 = new MobilePhoneDetail("phone3","0664-987345331");
		phone4 = new MobilePhoneDetail("phone4","0664-987456461");
		location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
		location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
		location3 = new Location("location3",phone3,"street3","number3",3,"city3","notes3");
		location4 = new Location("location4",phone4,"street4","number4",4,"city4","notes4");
		veh1 = new VehicleDetail("KA01","KDO",location1, location2, phone1, "vehicle notes...", true, false);
		veh2 = new VehicleDetail("KA02","KTW",location2, location1, phone2, "vehicle notes...", false, true);
		comp1 = new Competence("comp1");
		comp2 = new Competence("comp2");
		//logins and members
		member1 = new StaffMember(50100001,"fname1","lname1","user1","street1","city1",false,"27-01-2008",phone1,comp1,"mail1",location1);
		member2 = new StaffMember(50100002,"fname2","lname2","user2","street2","city2",true,"28-01-2008",phone2,comp2,"mail2",location2);
		login1 = new Login("user1","password1",false);
		login2 = new Login("user2","password2",false);
		transport1 = new Transport("vonStraﬂe1","vonStadt1",location1,MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("28-01-2008 12:00", MyUtils.timeAndDateFormat),"A",2);
		transport2 = new Transport("vonStraﬂe2","vonStadt2",location2,MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("28-01-2008 14:00", MyUtils.timeAndDateFormat),"B",2);
		caller1 = new CallerDetail("derCaller","0664-4143824");

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

		//logins 
		loginDAO.addLogin(login1);
		loginDAO.addLogin(login2);
		//staff member
		staffDAO.addStaffMember(member1);
		staffDAO.addStaffMember(member2);

		//insert transports
		transport1.setCreatedByUsername(login1.getUsername());
		transport2.setCreatedByUsername(login2.getUsername());
		transport1.setCallerDetail(caller1);
		long creationTime = Calendar.getInstance().getTimeInMillis();
		transport1.setCreationTime(creationTime);
		transport2.setCreationTime(creationTime);
		transport1.setPlanedLocation(location1);
		transport2.setPlanedLocation(location2);

		id1 = transportDAO.addTransport(transport1);
		id2 = transportDAO.addTransport(transport2);
		transport1.setTransportId(id1);
		transport2.setTransportId(id2); 

		//vehicle
		veh1.setDriver(member1);
		veh1.setReadyForAction(true);
		veh1.setTransportStatus(10);

		veh2.setDriver(member1);
		veh2.setReadyForAction(true);
		veh2.setTransportStatus(10);

		vehicleDAO.addVehicle(veh1);
		vehicleDAO.addVehicle(veh2);	
	}

	@After
	public void tearDown() throws SQLException
	{
//		deleteTable(TransportDAO.TABLE_DEPENDENT_TMP);
//		deleteTable(TransportDAO.TABLE_DEPENDENT_ASSIGNED_VEHICLES);
//		deleteTable(TransportDAO.TABLE_DEPENDENT_STATE);
//		deleteTable(TransportDAO.TABLE_DEPENDENT_SELECTED);
//		deleteTable(TransportDAO.TABLE_NAME);
//		deleteTable(MobilePhoneDAO.TABLE_NAME);
//		deleteTable(LocationDAO.TABLE_NAME);
//		deleteTable(CallerDAO.TABLE_NAME);
//		deleteTable(UserLoginDAO.TABLE_NAME);
//		deleteTable(StaffMemberDAO.TABLE_NAME);
//		deleteTable(CompetenceDAO.TABLE_NAME);
//		deleteTable(VehicleDAO.TABLE_NAME);
	}


	@Test
	public void testInsertTransport() throws SQLException
	{
		long dateTime1 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);
		long dateTime2 = MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat);
		int transportId;
		//insert the transport
		{
			Transport transport3 = new Transport("vonStraﬂe3","vonStadt3",location1,MyUtils.stringToTimestamp("29-01-2008", MyUtils.dateFormat),MyUtils.stringToTimestamp("29-01-2008 14:00", MyUtils.timeAndDateFormat),"C",2);
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
			transport3.setKindOfIllness(new Disease("Schlaganfall"));
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
			transport3.setTransportPriority("C");
			transportId = transportDAO.addTransport(transport3);
		}
		{
			//insert the transport
			Transport transport4 = transportDAO.getTransportById(transportId);
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
			assertEquals("Schlaganfall",transport4.getKindOfIllness().getDiseaseName());
			assertEquals("Tragsessel",transport4.getKindOfTransport());
			assertEquals("thenotes",transport4.getNotes());
			assertEquals("Muster",transport4.getPatient().getFirstname());
			assertEquals("Max", transport4.getPatient().getLastname());
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
		}
	}

	@Test
	public void testListRunningTransports() throws SQLException
	{
		//first update the transport state
		{
			Transport t1 = transportDAO.getTransportById(transport1.getTransportId());
			t1.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);
			Transport t2 = transportDAO.getTransportById(transport2.getTransportId());
			t2.setProgramStatus(IProgramStatus.PROGRAM_STATUS_UNDERWAY);
			GregorianCalendar cal = new GregorianCalendar();
			long time = cal.getTimeInMillis();
			t2.addStatus(1, time);
			//update them
			transportDAO.updateTransport(t1);
			transportDAO.updateTransport(t2);
		}
		{
			//request the listing
			List<Transport> list = transportDAO.listRunningTransports();
			Assert.assertEquals(2, list.size());
		}
	}
	
	public void testListPrebookedTransports() throws SQLException
	{
		//first update the transport state
		{
			Transport t1 = transportDAO.getTransportById(transport1.getTransportId());
			t1.setProgramStatus(IProgramStatus.PROGRAM_STATUS_PREBOOKING);
			Transport t2 = transportDAO.getTransportById(transport2.getTransportId());
			t2.setProgramStatus(IProgramStatus.PROGRAM_STATUS_PREBOOKING);
			//update them
			transportDAO.updateTransport(t1);
			transportDAO.updateTransport(t2);
		}
		{
			//request the listing
			List<Transport> list = transportDAO.listPrebookedTransports();
			Assert.assertEquals(2, list.size());
		}
	}
	
	public void testListArchivedTransports() throws SQLException
	{
		//first update the transport state
		{
			Transport t1 = transportDAO.getTransportById(transport1.getTransportId());
			t1.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
			Transport t2 = transportDAO.getTransportById(transport2.getTransportId());
			t2.setProgramStatus(IProgramStatus.PROGRAM_STATUS_JOURNAL);
			//update them
			transportDAO.updateTransport(t1);
			transportDAO.updateTransport(t2);
		}
		{
			long startTime = MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat);
			//set the end date to date +1
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat));
			cal.add(Calendar.DAY_OF_MONTH, 1);
			long endTime = cal.getTimeInMillis();
			//request the listing
			List<Transport> list = transportDAO.listArchivedTransports(startTime, endTime);
			Assert.assertEquals(2, list.size());
		}
	}

	@Test
	public void testUpdateTranpsport() throws SQLException
	{
		{
			Transport transport = transportDAO.getTransportById(transport1.getTransportId());
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
			transport.setKindOfIllness(new Disease("Schlaganfall"));
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

			//transport.setVehicleDetail(veh1); --> to set a vehicle is only possible in combination with generating
			//a transport number (there the vehicle is written into the database
			//set transport year not possible

			transportDAO.updateTransport(transport);
		}
		{
			Transport transport = transportDAO.getTransportById(transport1.getTransportId());
			assertEquals(transport1.getTransportId(),transport.getTransportId());
			assertEquals(MyUtils.stringToTimestamp("29-01-2008 16:00", MyUtils.timeAndDateFormat),transport.getAppointmentTimeAtDestination());
			assertEquals("derCaller",transport.getCallerDetail().getCallerName());
			assertEquals("user2",transport.getCreatedByUsername());
			assertEquals(2,transport.getDirection());
			assertEquals("feedbackNew",transport.getFeedback());
			assertEquals("fromCity1",transport.getFromCity());
			assertEquals("fromStreet1",transport.getFromStreet());
			assertEquals("Schlaganfall",transport.getKindOfIllness().getDiseaseName());
			assertEquals("mobil",transport.getKindOfTransport());
			assertEquals("thenotes",transport.getNotes());
			assertEquals("Max",transport.getPatient().getFirstname());
			assertEquals("Muster",transport.getPatient().getLastname());
			assertEquals("location2",transport.getPlanedLocation().getLocationName());
			assertEquals("toCity",transport.getToCity());
			assertEquals("toStreet",transport.getToStreet());
			assertEquals("C",transport.getTransportPriority());
		}
	}

	@Test
	public void testGenerateTransportNumber() throws SQLException
	{
		//generate a transport numberer
		transport1.setVehicleDetail(veh1);
		transport1.setYear(2008);
		transport1.getVehicleDetail().setCurrentStation(location1);
		int transportNr = transportDAO.generateTransportNumber(transport1);
		transport1.setTransportNumber(transportNr);
		System.out.println("Generated transport ID:"+transportNr);
		//get the transport again and check the transport number
		Transport tr1new = transportDAO.getTransportById(transport1.getTransportId());
		assertEquals(transportNr,tr1new.getTransportNumber());

		//second transport - same vehicle/same location -->higher transport number
		transport2.setYear(2008);
		transport2.setVehicleDetail(veh1);
		transport2.getVehicleDetail().setCurrentStation(location1);
		transportNr = transportDAO.generateTransportNumber(transport2);
		System.out.println("Generated transport ID:"+transportNr);
		transport2.setTransportNumber(transportNr);
		//get the transport again and check the transport number
		Transport tr2new = transportDAO.getTransportById(transport2.getTransportId());
		assertEquals(transportNr,tr2new.getTransportNumber());
		
		//numbers must be different
		Assert.assertTrue(tr2new.getTransportNumber() > tr1new.getTransportNumber());
	}

	@Test
	public void testGenerateTransportNumbersDifferentLocation() throws SQLException
	{	
		//generate a transport number
		transport1.setYear(2008);
		transport1.setVehicleDetail(veh1);
		transport1.getVehicleDetail().setCurrentStation(location1);
		int transportNr = transportDAO.generateTransportNumber(transport1);
		transport1.setTransportNumber(transportNr);
		System.out.println("Generated transport ID:"+transportNr);
		//get the transport again and check the transport number
		Transport tr1new = transportDAO.getTransportById(transport1.getTransportId());
		assertEquals(transportNr,tr1new.getTransportNumber());

		//second transport - same vehicle/different location 
		transport2.setYear(2008);
		transport2.setVehicleDetail(veh1);
		transport2.getVehicleDetail().setCurrentStation(location2);
		transportNr = transportDAO.generateTransportNumber(transport2);
		System.out.println("Generated transport ID:"+transportNr);
		transport2.setTransportNumber(transportNr);
		//get the transport again and check the transport number
		Transport tr2new = transportDAO.getTransportById(transport2.getTransportId());
		assertEquals(transportNr,tr2new.getTransportNumber());
		
		//the generated numbers must be the same
		assertEquals(tr1new.getTransportNumber(), tr2new.getTransportNumber());
	}

	@Test
	public void testRemoveVehicleFromTransport() throws SQLException
	{
		//remove the vehicle from the transport
		transport1.clearVehicleDetail();
		transport1.setTransportNumber(456);
		transportDAO.removeVehicleFromTransport(transport1);
		Transport tr1new = transportDAO.getTransportById(transport1.getTransportId());
		assertNull(tr1new.getVehicleDetail());
		assertEquals(0,tr1new.getTransportNumber());
	}
	
	@Test
	public void insertRemoveInsertTest() throws SQLException
	{
		//insert a new transport, location1
		transport1.setVehicleDetail(veh1);
		transport1.getVehicleDetail().setCurrentStation(location1);
		int transportNr = transportDAO.generateTransportNumber(transport1);
		transport1.setTransportNumber(transportNr);
		
		//remove the vehicle and archive the transport
		transport1.clearVehicleDetail();
		transportDAO.removeVehicleFromTransport(transport1);
		
		//add a new transport, same location -> must have the old ID
		transport2.setVehicleDetail(veh1);
		transport2.getVehicleDetail().setCurrentStation(location1);
		int transportNr1 = transportDAO.generateTransportNumber(transport2);
		
		assertEquals(transportNr, transportNr1);
	}
	
	@Test
	public void insertCancleInsertTest() throws SQLException
	{
		//insert a new transport, location1
		transport1.setVehicleDetail(veh1);
		transport1.getVehicleDetail().setCurrentStation(location1);
		int transportNr = transportDAO.generateTransportNumber(transport1);
		System.out.println("TransportID: "+transportNr);
		transport1.setTransportNumber(transportNr);
		
		//cancle the transport
		transport1.setTransportNumber(Transport.TRANSPORT_CANCLED);
		transportDAO.cancelTransport(transport1);
		
		//add a new transport, same location -> must have the old ID
		transport2.setVehicleDetail(veh1);
		transport2.getVehicleDetail().setCurrentStation(location1);
		int transportNr1 = transportDAO.generateTransportNumber(transport2);
		System.out.println("TransportID: "+transportNr1);
		assertEquals(transportNr, transportNr1);
	}
	
	@Test
	public void testTransportStati() throws SQLException
	{
		{
			//get the transport and update some values
			Transport tr1 = transportDAO.getTransportById(transport1.getTransportId());
			tr1.addStatus(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT, Calendar.getInstance().getTimeInMillis());
			tr1.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, Calendar.getInstance().getTimeInMillis());
			transportDAO.updateTransport(tr1);
		}
		{
			//now check that the params are set correct
			Transport tr1 = transportDAO.getTransportById(transport1.getTransportId());
			Assert.assertTrue(tr1.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
			Assert.assertTrue(tr1.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));	
		}
	}
	
	@Test
	public void testUpdateAssignedTransport() throws SQLException
	{
		//veh2 = new VehicleDetail("KA02","KTW",location2, location1, phone2, "vehicle notes...", false, true);
		transport1.setVehicleDetail(veh2);
		transport1.setYear(2008);
		transport1.getVehicleDetail().setCurrentStation(location1);
		transportDAO.generateTransportNumber(transport1);
		
		{
			Transport transport = transportDAO.getTransportById(transport1.getTransportId());
			VehicleDetail vehicle = transport.getVehicleDetail();
			vehicle.setDriver(member2);
			vehicle.setFirstParamedic(member2);
			vehicle.setSecondParamedic(member2);
			vehicle.setVehicleNotes("the new notes");
			transportDAO.updateTransport(transport);
		}
		Transport transport = transportDAO.getTransportById(transport1.getTransportId());
		Assert.assertEquals(transport.getVehicleDetail().getDriver().getLastName(),member2.getLastName());
		Assert.assertEquals(transport.getVehicleDetail().getFirstParamedic().getLastName(),member2.getLastName());
		Assert.assertEquals(transport.getVehicleDetail().getSecondParamedic().getLastName(),member2.getLastName());
		
	}
}