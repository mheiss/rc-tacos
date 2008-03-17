package at.rc.tacos.core.db.dao.mysql;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;

/**
 * This is a test class to test the functionality of the staffMember and the login class.
 * The test are combined because the two daos cannot be tested alone
 * @author Walter
 */
public class LoginStaffMemberDAOMySQLTest extends DBTestBase
{
    //the dao class
    private final UserLoginDAO loginDAO = DaoFactory.MYSQL.createUserDAO();
	private final StaffMemberDAO staffMemberDAO = DaoFactory.MYSQL.createStaffMemberDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
	
	//prepare the test data
	Login login1,login2;
    MobilePhoneDetail phone1,phone2; 
    Competence comp1,comp2;
    Location location1,location2;
    StaffMember member1,member2;
    
    @Before
    public void setUp() throws SQLException
    {
    	login1 = new Login("user1","password1",false);
    	login2 = new Login("user2","password2",false);
        phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
        phone2 = new MobilePhoneDetail("phone2","0664-987654321");
        comp1 = new Competence("comp1");
        comp2 = new Competence("comp2");
        location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
        location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
        member1 = new StaffMember(50100001,"fname1","lname1","user1","street1","city1",false,"27-01-2008",phone1,comp1,"mail1",location1);
        member2 = new StaffMember(50100002,"fname2","lname2","user2","street2","city2",true,"28-01-2008",phone2,comp2,"mail2",location2);
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
        //set the login test data
        login1.setIslocked(false);
        login1.setAuthorization("Administrator");
        login2.setIslocked(true);
        login2.setAuthorization("Benutzer");
        loginDAO.addLogin(login1);
        loginDAO.addLogin(login2);
        staffMemberDAO.addStaffMember(member1);
        staffMemberDAO.addStaffMember(member2);
    }
    
    @After
    public void tearDown() throws SQLException
    {
    	deleteTable(StaffMemberDAO.TABLE_NAME);
        deleteTable(UserLoginDAO.TABLE_NAME);
        deleteTable(MobilePhoneDAO.TABLE_NAME);
        deleteTable(MobilePhoneDAO.TABLE_DEPENDENT_NAME);
        deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_DEPENDENT_NAME);
    }
    
    @Test
    public void testGetLoginAndStaffMember() throws SQLException
    {
        Login login = loginDAO.getLoginAndStaffmember("user1");
        Assert.assertEquals("user1",login.getUsername());
        Assert.assertEquals("Administrator", login.getAuthorization());
        Assert.assertEquals(false, login.isIslocked());
        Assert.assertEquals(member1,login.getUserInformation());
    }
    
    @Test
    public void testCheckLogin() throws SQLException
    {
        int result = loginDAO.checkLogin("user1", "password1");
        Assert.assertEquals(UserLoginDAO.LOGIN_SUCCESSFULL, result);
    }
    
    @Test
    public void testCheckWrongPassword() throws SQLException
    {
        int result = loginDAO.checkLogin("user1", "password2");
        Assert.assertEquals(UserLoginDAO.LOGIN_FAILED, result); 
    }
    
    @Test
    public void testCheckLoginLocked() throws SQLException
    {
        int result = loginDAO.checkLogin("user2", "password2");
        Assert.assertEquals(UserLoginDAO.LOGIN_DENIED, result); 
    }
    
    @Test
    public void testStaffMemberUpdate() throws SQLException
    {
    	{
    		StaffMember staffMember = staffMemberDAO.getStaffMemberByUsername("user1");
    		staffMember.setFirstName("newFName");
    		staffMember.setLastName("newLName");
    		staffMember.setStreetname("newStreetName");
    		staffMember.setCityname("newCityName");
    		staffMember.setEMail("newEMail");
    		staffMember.setPrimaryLocation(location2);
    		staffMember.setMale(true);
    		staffMember.setBirthday("27-01-2008");
    		staffMemberDAO.updateStaffMember(staffMember);
    	}
    	{
    		StaffMember staffMember = staffMemberDAO.getStaffMemberByUsername("user1");
    		Assert.assertEquals("user1", staffMember.getUserName());
            Assert.assertEquals("newFName", staffMember.getFirstName());
            Assert.assertEquals("newLName", staffMember.getLastName());
            Assert.assertEquals("newStreetName", staffMember.getStreetname());
            Assert.assertEquals("newCityName", staffMember.getCityname());
            Assert.assertEquals(true, staffMember.isMale());
            Assert.assertEquals("newEMail",staffMember.getEMail());
            Assert.assertEquals(location2, staffMember.getPrimaryLocation());
    	}
    }
    
    @Test
    public void testUpdateLogin() throws SQLException
    {
        {
            Login login = loginDAO.getLoginAndStaffmember("user1");
            login.setAuthorization("newAdministrator");
            login.setIslocked(true);
            loginDAO.updateLogin(login);
        }
        {
            Login login = loginDAO.getLoginAndStaffmember("user1");
            Assert.assertEquals(login.getUsername(),"user1");
            Assert.assertEquals("newAdministrator", login.getAuthorization());
            Assert.assertEquals(true, login.isIslocked());
        }
    }
    
    @Test
    public void testRemoveLogin() throws SQLException
    {
        //TODO: Implement the test to chek whether the login is locked
    }
    
    @Test
    public void testCompetenceList() throws SQLException
    {
    	List<Competence> compList = competenceDAO.listCompetencesOfStaffMember(member1.getStaffMemberId());
    	Assert.assertEquals(1, compList.size());
    	Assert.assertEquals("comp1", compList.get(0).getCompetenceName());
    }
    
    @Test
    public void testMobilePhoneList() throws SQLException
    {
    	List<MobilePhoneDetail> phoneList = mobilePhoneDAO.listMobilePhonesOfStaffMember(member1.getStaffMemberId());
    	Assert.assertEquals(1, phoneList.size());
    	Assert.assertEquals("phone1", phoneList.get(0).getMobilePhoneName());
    }
    
    @Test
    public void testListStaffMember() throws SQLException
    {
        List<StaffMember> list = staffMemberDAO.getAllStaffMembers();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testListStaffMemberByLocation() throws SQLException
    {
        List<StaffMember> list = staffMemberDAO.getStaffMembersFromLocation(location1.getId());
        Assert.assertEquals(1, list.size()); 
    }
    
    @Test
    public void testGetStaffMemberById() throws SQLException
    {
        StaffMember sm = staffMemberDAO.getStaffMemberByID(member1.getStaffMemberId());
        Assert.assertEquals(50100001, sm.getStaffMemberId());
        Assert.assertEquals("fname1", sm.getFirstName());
        Assert.assertEquals("lname1", sm.getLastName());
        Assert.assertEquals("user1",sm.getUserName());
        Assert.assertEquals("street1", sm.getStreetname());
        Assert.assertEquals("city1", sm.getCityname());
        Assert.assertEquals(false, sm.isMale());
        Assert.assertEquals("27-01-2008", sm.getBirthday());
        Assert.assertEquals(1, sm.getPhonelist().size());
        Assert.assertEquals(phone1, sm.getPhonelist().get(0));
        Assert.assertEquals(1, sm.getCompetenceList().size());
        Assert.assertEquals(comp1, sm.getCompetenceList().get(0));
        Assert.assertEquals("mail1", sm.getEMail());
        Assert.assertEquals(location1, sm.getPrimaryLocation());
    }
    
    @Test
    public void testGetStaffMemberByUserName() throws SQLException
    {
        StaffMember sm = staffMemberDAO.getStaffMemberByUsername("user1");
        Assert.assertEquals(member1.getStaffMemberId(), sm.getStaffMemberId());
        Assert.assertEquals("fname1", sm.getFirstName());
        Assert.assertEquals("lname1", sm.getLastName());
        Assert.assertEquals("user1",sm.getUserName());
        Assert.assertEquals("street1", sm.getStreetname());
        Assert.assertEquals("city1", sm.getCityname());
        Assert.assertEquals(false, sm.isMale());
        Assert.assertEquals("27-01-2008", sm.getBirthday());
        Assert.assertEquals(1, sm.getPhonelist().size());
        Assert.assertEquals(phone1, sm.getPhonelist().get(0));
        Assert.assertEquals(1, sm.getCompetenceList().size());
        Assert.assertEquals(comp1, sm.getCompetenceList().get(0));
        Assert.assertEquals("mail1", sm.getEMail());
        Assert.assertEquals(location1, sm.getPrimaryLocation());
    }
}