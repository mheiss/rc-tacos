package at.rc.tacos.core.db.dao.sqlserver;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.DialysisPatientDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.util.MyUtils;

public class DialysePatientDAOSQLTest extends DBTestBase
{
	//the needed dao classes
	private DialysisPatientDAO dialyseDAO = DaoFactory.SQL.createDialysisPatientDAO();
	private LocationDAO locationDAO = DaoFactory.SQL.createLocationDAO();
	private MobilePhoneDAO mobilePhoneDAO = DaoFactory.SQL.createMobilePhoneDAO(); 
	
	//test data
	private Location location1;
	private MobilePhoneDetail phone1;
	private DialysisPatient patient1,patient2;
	
	@Before
	public void setUp() throws SQLException
	{
		 //test phone for the locations
        phone1 = new MobilePhoneDetail("phone1","0664-123456789");
        location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
        //insert the test data and set the generated ids
        int phoneId1 = mobilePhoneDAO.addMobilePhone(phone1);
        phone1.setId(phoneId1);
        //insert test data
        int id1 = locationDAO.addLocation(location1);
        location1.setId(id1);
		
		Calendar cal = Calendar.getInstance();
		//create the patient
		patient1 = new DialysisPatient();
		//the patient details
		patient1.setPatient(new Patient("max","mustermann"));
		patient1.setKindOfTransport("liegend");
		patient1.setInsurance("keine");
		patient1.setLocation(location1);
		patient1.setFromStreet("fromStreet");
		patient1.setFromCity("fromCitry");
		patient1.setToStreet("toStreet");
		patient1.setToCity("toCity");
		//the time values
		patient1.setPlannedStartOfTransport(cal.getTimeInMillis());
		patient1.setPlannedTimeAtPatient(cal.getTimeInMillis());
		patient1.setAppointmentTimeAtDialysis(cal.getTimeInMillis());
		patient1.setReadyTime(cal.getTimeInMillis());
		patient1.setPlannedStartForBackTransport(cal.getTimeInMillis());
		//insert the patient
		int id = dialyseDAO.addDialysisPatient(patient1);
		patient1.setId(id);
		
		//second patient
		patient2 = new DialysisPatient();
		//the patient details
		patient2.setPatient(new Patient("max","mustermann"));
		patient2.setKindOfTransport("gehend");
		patient2.setInsurance("keine");
		patient2.setLocation(location1);
		patient2.setFromStreet("fromStreet");
		patient2.setFromCity("fromCitry");
		patient2.setToStreet("toStreet");
		patient2.setToCity("toCity");
		//the time values
		patient2.setPlannedStartOfTransport(cal.getTimeInMillis());
		patient2.setPlannedTimeAtPatient(cal.getTimeInMillis());
		patient2.setAppointmentTimeAtDialysis(cal.getTimeInMillis());
		patient2.setReadyTime(cal.getTimeInMillis());
		patient2.setPlannedStartForBackTransport(cal.getTimeInMillis());
		//insert the patient
		id = dialyseDAO.addDialysisPatient(patient2);
		patient2.setId(id);
	}
	
	@After
	public void tearDown() throws SQLException
	{
		deleteTable(DialysisPatientDAO.TABLE_NAME);
		deleteTable(DialysisPatientDAO.TABLE_DEPENDENT);
		deleteTable(MobilePhoneDAO.TABLE_NAME);
		deleteTable(LocationDAO.TABLE_NAME);
	}
	
	@Test
	public void testListPatients() throws SQLException
	{
		List<DialysisPatient> list = dialyseDAO.listDialysisPatient();
		Assert.assertEquals(2, list.size());
	}
	
	@Test
	public void testUpdatePatient() throws SQLException
	{
		{
			//get the patient by id
			DialysisPatient patient = dialyseDAO.getDialysisPatientById(patient1.getId());
			patient.getPatient().setFirstname("newFirst");
			patient.getPatient().setLastname("newLast");
			patient.setAssistantPerson(true);
			patient.setMonday(true);
			patient.setInsurance("new");
			dialyseDAO.updateDialysisPatient(patient);
		}
		{
			DialysisPatient patient = dialyseDAO.getDialysisPatientById(patient1.getId());
			Assert.assertEquals("newFirst", patient.getPatient().getFirstname());
			Assert.assertEquals("newLast", patient.getPatient().getLastname());
			Assert.assertEquals("new", patient.getInsurance());
			Assert.assertTrue(patient.isAssistantPerson());
			Assert.assertTrue(patient.isMonday());
		}
	}
	
	@Test
	public void testRemovePatient() throws SQLException
	{
		{
			DialysisPatient patient = dialyseDAO.getDialysisPatientById(patient1.getId());
			dialyseDAO.removeDialysisPatient(patient.getId());
		}
		{
			List<DialysisPatient> list = dialyseDAO.listDialysisPatient();
			Assert.assertEquals(1, list.size());
		}
	}
	
	@Test
	public void testSetLastTransport() throws SQLException
	{
		{
			DialysisPatient patient = dialyseDAO.getDialysisPatientById(patient1.getId());
			patient.setLastTransportDate(MyUtils.stringToTimestamp("22-02-2008",MyUtils.dateFormat));
			dialyseDAO.updateDialysisPatient(patient);
		}
		{
			DialysisPatient patient = dialyseDAO.getDialysisPatientById(patient1.getId());
			Assert.assertEquals(patient1.getId(),patient.getId());
			Assert.assertEquals("22-02-2008", MyUtils.timestampToString(patient.getLastTransportDate(), MyUtils.dateFormat));
		}
	}
}
