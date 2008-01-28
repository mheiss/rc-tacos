package at.rc.tacos.core.db.dao.mysql;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.RosterDAO;
import at.rc.tacos.core.db.dao.ServiceTypeDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.util.MyUtils;

public class RosterDAOMySQLTest extends DBTestBase
{
	//the dao class
	private final RosterDAO rosterDao = DaoFactory.MYSQL.createRosterEntryDAO();
    private final UserLoginDAO loginDAO = DaoFactory.MYSQL.createUserDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
	private final ServiceTypeDAO serviceTypeDao = DaoFactory.MYSQL.createServiceTypeDAO();
	private final JobDAO jobDao = DaoFactory.MYSQL.createJobDAO();
	
	//prepare the test data
	Login login1 = new Login("user1","password1",false);
	Login login2 = new Login("user2","password2",false);
    MobilePhoneDetail phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("phone2","0664-987654321");
    Competence comp1 = new Competence("comp1");
    Competence comp2 = new Competence("comp2");
    Job job1 = new Job("job1");
    Job job2 = new Job("job2");
    ServiceType serviceType1 = new ServiceType("serviceType1");
    ServiceType serviceType2 = new ServiceType("serviceType2");
    Location location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
    Location location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
    StaffMember member1 = new StaffMember(50100001,"fname1","lname1","user1","street1","city1",false,MyUtils.stringToTimestamp("27-01-2008",MyUtils.dateFormat),phone1,comp1,"mail1",location1);
    StaffMember member2 = new StaffMember(50100002,"fname2","lname2","user2","street2","city2",true,MyUtils.stringToTimestamp("28-01-2008",MyUtils.dateFormat),phone2,comp2,"mail2",location2);
    RosterEntry entry1 = new RosterEntry(member1,serviceType1,job1,location1,MyUtils.stringToTimestamp("28-01-2008 10:00", MyUtils.timeAndDateFormat),MyUtils.stringToTimestamp("28-01-2008 15:00", MyUtils.timeAndDateFormat));
    RosterEntry entry2 = new RosterEntry(member2,serviceType2,job2,location2,MyUtils.stringToTimestamp("28-01-2008 12:00", MyUtils.timeAndDateFormat),MyUtils.stringToTimestamp("28-01-2008 18:00", MyUtils.timeAndDateFormat));
    
	@Before
	public void setUp()
	{
		//insert test data
        int serviceId1 = serviceTypeDao.addServiceType(serviceType1);
        int serviceId2 = serviceTypeDao.addServiceType(serviceType2);
        serviceType1.setId(serviceId1);
        serviceType2.setId(serviceId2);
		
		//insert the jobs
		int jobId1 = jobDao.addJob(job1);
        int jobId2 = jobDao.addJob(job2);
        job1.setId(jobId1);
        job2.setId(jobId2);
        
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
        int locId1 = locationDAO.addLocation(location1);
        int locId2 = locationDAO.addLocation(location2);
        //set the ids
        location1.setId(locId1);
        location2.setId(locId2);
        
        //assign the logins to the staff members
        login1.setUserInformation(member1);
        login2.setUserInformation(member2);
        loginDAO.addLogin(login1);
        loginDAO.addLogin(login2);
        
        //set the user who create the entry
        entry1.setCreatedByUsername("user1");
        entry2.setCreatedByUsername("user2");
        
        //add the roster entries       
        int id1 = rosterDao.addRosterEntry(entry1);
        int id2 = rosterDao.addRosterEntry(entry2);
        //Set the ids
        entry1.setRosterId(id1);
        entry2.setRosterId(id2);
	}
	
	@After
	public void tearDown()
	{
        deleteTable(UserLoginDAO.TABLE_NAME);
        deleteTable(StaffMemberDAO.TABLE_NAME);
        deleteTable(MobilePhoneDAO.TABLE_NAME);
        deleteTable(MobilePhoneDAO.TABLE_DEPENDENT_NAME);
        deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_DEPENDENT_NAME);
        deleteTable(JobDAO.TABLE_NAME);
        deleteTable(ServiceTypeDAO.TABLE_NAME);
	}
	
	@Test
	public void testListRosterEntriesByDate()
	{
		long startDate = MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat);
		//add one day
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTimeInMillis(MyUtils.stringToTimestamp("28-01-2008", MyUtils.dateFormat));
		endCalendar.add(Calendar.DAY_OF_MONTH, 1);
		long endDate = endCalendar.getTimeInMillis();
		
		//request the listing
		List<RosterEntry> listing = rosterDao.listRosterEntryByDate(startDate, endDate);
		Assert.assertEquals(2,listing.size());
		System.out.println(MyUtils.timestampToString(listing.get(0).getPlannedStartOfWork(),MyUtils.timeAndDateFormat));
	}
	
	@Test
	public void testGetRosterEntryById()
	{
		RosterEntry entry = rosterDao.getRosterEntryById(entry1.getRosterId());
		Assert.assertEquals(entry1.getRosterId(), entry.getRosterId());
		Assert.assertEquals(member1, entry.getStaffMember());
		Assert.assertEquals(job1,entry.getJob());
		Assert.assertEquals(serviceType1,entry.getServicetype());
		Assert.assertEquals(location1,entry.getStation());
		Assert.assertEquals("user1", entry.getCreatedByUsername());
	}
	
	@Test
	public void testUpdateRosterEntry()
	{
		{
			RosterEntry entry = rosterDao.getRosterEntryById(entry1.getRosterId());
			entry.setStandby(true);
			entry.setRosterNotes("newNotes");
			rosterDao.updateRosterEntry(entry);
		}
		{
			RosterEntry entry = rosterDao.getRosterEntryById(entry1.getRosterId());
			Assert.assertTrue(entry.getStandby());
			Assert.assertEquals("newNotes", entry.getRosterNotes());	
		}
	}
}
