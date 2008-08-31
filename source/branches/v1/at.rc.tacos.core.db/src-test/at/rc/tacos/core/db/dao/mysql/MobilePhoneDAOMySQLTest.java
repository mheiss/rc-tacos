package at.rc.tacos.core.db.dao.mysql;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.MobilePhoneDetail;

public class MobilePhoneDAOMySQLTest extends DBTestBase
{
    //the dao class
    private MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
    
    //test data
    MobilePhoneDetail phone1,phone2;
    
    @Before
    public void setUp() throws SQLException
    {
    	phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
        phone2 = new MobilePhoneDetail("phone2","0664-987654321");
        //insert test data
        int id1 = mobilePhoneDAO.addMobilePhone(phone1);
        int id2 = mobilePhoneDAO.addMobilePhone(phone2);
        phone1.setId(id1);
        phone2.setId(id2);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        deleteTable(MobilePhoneDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindByName() throws SQLException
    {
        MobilePhoneDetail MobilePhone = mobilePhoneDAO.getMobilePhoneByName("phone1");
        Assert.assertEquals("phone1", MobilePhone.getMobilePhoneName());
        Assert.assertEquals("0664-123456789", MobilePhone.getMobilePhoneNumber());
    }
    
    @Test
    public void testRemoveMobilePhone() throws SQLException
    {
        mobilePhoneDAO.removeMobilePhone(phone1.getId());
        //list all
        List<MobilePhoneDetail> list = mobilePhoneDAO.listMobilePhones();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testListMobilePhone() throws SQLException
    {
        List<MobilePhoneDetail> list = mobilePhoneDAO.listMobilePhones();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testUpdateMobilePhone() throws SQLException
    {
        //create two indivdual block
        {
            MobilePhoneDetail phone = mobilePhoneDAO.getMobilePhoneByName("phone1");
            phone.setMobilePhoneName("newMobilePhoneName");
            phone.setMobilePhoneNumber("0664-555555");
            mobilePhoneDAO.updateMobilePhone(phone);
        }
        {
            MobilePhoneDetail phone = mobilePhoneDAO.getMobilePhoneByName("newMobilePhoneName");
            Assert.assertEquals("newMobilePhoneName", phone.getMobilePhoneName());
            Assert.assertEquals("0664-555555", phone.getMobilePhoneNumber());
        }
    }
}
