package at.rc.tacos.server.dbal.dao.sqlserver;

import java.sql.SQLException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.server.dbal.DBTestBase;
import at.rc.tacos.server.dbal.dao.SickPersonDAO;
import at.rc.tacos.server.dbal.factory.DaoFactory;

public class SickPersonDAOSQLTest extends DBTestBase
{
    //the dao class
	private SickPersonDAO personDao = DaoFactory.SQL.createSickPersonDAO();
    
	private SickPerson person1;
	private SickPerson person2;
    
    @Before
    public void setUp() throws SQLException
    {
    	person1 = new SickPerson("Hans", "Maier");
    	person2 = new SickPerson("Franz", "Huber");
    	
    	int id1 = personDao.addSickPerson(person1);
    	int id2 = personDao.addSickPerson(person2);
    	
    	person1.setSickPersonId(id1);
    	person2.setSickPersonId(id2);
    }
    
    @After
    public void tearDown() throws SQLException
    {
        deleteTable(SickPersonDAO.TABLE_NAME);
    }
    
    @Test
    public void testRemoveSickPerson() throws SQLException
    {
    	personDao.removeSickPerson(person1.getSickPersonId());
    	List<SickPerson> list = personDao.getSickPersonList("ube");
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testListSickPerson() throws SQLException
    {
        List<SickPerson> list = personDao.getSickPersonList("mai");
        Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testUpdateLocation() throws SQLException
    {
        //create two individual blocks
        {
        	SickPerson person = personDao.getSickPerson(person1.getSickPersonId());
        	person.setCityname("new city");
        	person.setFirstName("newFirstname");
        	person.setSickPersonId(person1.getSickPersonId());
     
            personDao.updateSickPerson(person);
        }
        {
        	SickPerson person = personDao.getSickPerson(person1.getSickPersonId());
        	Assert.assertEquals("new city", person.getCityname());
        	Assert.assertEquals("newFirstname", person.getFirstName());
        }
    }
}
