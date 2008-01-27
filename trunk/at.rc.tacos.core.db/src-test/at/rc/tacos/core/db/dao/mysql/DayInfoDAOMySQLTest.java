package at.rc.tacos.core.db.dao.mysql;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.DayInfoDAO;
import at.rc.tacos.core.db.dao.JobDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.util.MyUtils;

public class DayInfoDAOMySQLTest extends DBTestBase
{
    //the dao class
    private DayInfoDAO dayInfoDao = DaoFactory.MYSQL.createDayInfoDAO();
       
    @Before
    public void setUp() 
    {
        DayInfoMessage dayInfo1 = new DayInfoMessage("dayInfo1",MyUtils.stringToTimestamp("27-01-2008",MyUtils.dateFormat),"user1");
        DayInfoMessage dayInfo2 = new DayInfoMessage("dayInfo2",MyUtils.stringToTimestamp("28-01-2008",MyUtils.dateFormat),"user2");
        //add the data
        dayInfoDao.updateDayInfoMessage(dayInfo1);
        dayInfoDao.updateDayInfoMessage(dayInfo2);
    }
    
    @After
    public void tearDown()
    {
        deleteTable(JobDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindByDate()
    {
        DayInfoMessage dayInfoMessage = dayInfoDao.getDayInfoByDate(MyUtils.stringToTimestamp("27-01-2008", MyUtils.dateFormat));   
        Assert.assertEquals("dayInfo1", dayInfoMessage.getMessage());
        Assert.assertEquals("user1", dayInfoMessage.getLastChangedBy());
    }
        
    @Test
    public void testUpdateDayInfo()
    {
        //create two indivdual block
        {
            DayInfoMessage dayInfoMessage = dayInfoDao.getDayInfoByDate(MyUtils.stringToTimestamp("27-01-2008", MyUtils.dateFormat));    
            dayInfoMessage.setMessage("newDayInfoMessage");
            dayInfoMessage.setLastChangedBy("newUser");
            dayInfoDao.updateDayInfoMessage(dayInfoMessage);
        }
        {
            DayInfoMessage dayInfoMessage = dayInfoDao.getDayInfoByDate(MyUtils.stringToTimestamp("27-01-2008", MyUtils.dateFormat));    
            Assert.assertEquals("newDayInfoMessage", dayInfoMessage.getMessage());
            Assert.assertEquals("newUser", dayInfoMessage.getLastChangedBy());
        }
    }
}
