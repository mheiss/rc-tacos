package at.rc.tacos.core.db.dao.mysql;

import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.CompetenceDAO;
import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.StaffMemberDAO;
import at.rc.tacos.core.db.dao.UserLoginDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.*;
import at.rc.tacos.util.MyUtils;

public class DayInfoDAOMySQLTest extends DBTestBase
{
    //the dao class
    private DayInfoDAO dayInfoDao = DaoFactory.MYSQL.createDayInfoDAO();
    private final UserLoginDAO loginDAO = DaoFactory.MYSQL.createUserDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
      
    //the needed test data for the tests
    Login login1,login2;
	MobilePhoneDetail phone1,phone2;
    Competence comp1, comp2;
    Location location1,location2;
    StaffMember member1,member2;
	DayInfoMessage dayInfo1,dayInfo2;
    
    @Before
    public void setUp() throws SQLException
    {
    	//create the test data
        login1 = new Login("user1","password1",false);
    	login2 = new Login("user2","password2",false);
    	phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
        phone2 = new MobilePhoneDetail("phone2","0664-987654321");
        comp1 = new Competence("comp1");
        comp2 = new Competence("comp2");
        location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
        location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
        member1 = new StaffMember(50100001,"fname1","lname1","user1","street1","city1",false,MyUtils.stringToTimestamp("27-01-2008",MyUtils.dateFormat),phone1,comp1,"mail1",location1);
        member2 = new StaffMember(50100002,"fname2","lname2","user2","street2","city2",true,MyUtils.stringToTimestamp("28-01-2008",MyUtils.dateFormat),phone2,comp2,"mail2",location2);
    	dayInfo1 = new DayInfoMessage("dayInfo1",MyUtils.stringToTimestamp("27-01-2008",MyUtils.dateFormat),"user1");
        dayInfo2 = new DayInfoMessage("dayInfo2",MyUtils.stringToTimestamp("28-01-2008",MyUtils.dateFormat),"user2");
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
        //assign the logins to the staff members
        login1.setUserInformation(member1);
        login1.setIslocked(false);
        login1.setAuthorization("Administrator");
        login2.setUserInformation(member2);
        login2.setIslocked(true);
        login2.setAuthorization("User");
        loginDAO.addLogin(login1);
        loginDAO.addLogin(login2);
        
        dayInfoDao.updateDayInfoMessage(dayInfo1);
        dayInfoDao.updateDayInfoMessage(dayInfo2);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        deleteTable(JobDAO.TABLE_NAME);
        deleteTable(UserLoginDAO.TABLE_NAME);
        deleteTable(StaffMemberDAO.TABLE_NAME);
        deleteTable(MobilePhoneDAO.TABLE_NAME);
        deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_NAME);
        deleteTable(DayInfoDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindByDate() throws SQLException
    {
        DayInfoMessage dayInfoMessage = dayInfoDao.getDayInfoByDate(MyUtils.stringToTimestamp("27-01-2008", MyUtils.dateFormat));   
        Assert.assertEquals("dayInfo1", dayInfoMessage.getMessage());
        Assert.assertEquals("user1", dayInfoMessage.getLastChangedBy());
    }
        
    @Test
    public void testUpdateDayInfo() throws SQLException
    {
        //create two indivdual block
        {
            DayInfoMessage dayInfoMessage = dayInfoDao.getDayInfoByDate(MyUtils.stringToTimestamp("27-01-2008", MyUtils.dateFormat));    
            dayInfoMessage.setMessage("newDayInfoMessage");
            dayInfoMessage.setLastChangedBy("user2");
            dayInfoDao.updateDayInfoMessage(dayInfoMessage);
        }
        {
            DayInfoMessage dayInfoMessage = dayInfoDao.getDayInfoByDate(MyUtils.stringToTimestamp("27-01-2008", MyUtils.dateFormat));    
            Assert.assertEquals("newDayInfoMessage", dayInfoMessage.getMessage());
            Assert.assertEquals("user2", dayInfoMessage.getLastChangedBy());
        }
    }
}
