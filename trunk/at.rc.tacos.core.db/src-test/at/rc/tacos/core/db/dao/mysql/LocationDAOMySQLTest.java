package at.rc.tacos.core.db.dao.mysql;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.core.db.dao.LocationDAO;
import at.rc.tacos.core.db.dao.MobilePhoneDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;

public class LocationDAOMySQLTest extends DBTestBase
{
    //the dao class
    private LocationDAO locationDao = DaoFactory.MYSQL.createLocationDAO();
    private MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO(); 
    
    //test phone for the locations
    MobilePhoneDetail phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("phone2","0664-987654321");
    //test data
    Location location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
    Location location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
    
    @Before
    public void setUp() 
    {
        //create the phones
        int phoneId1 = mobilePhoneDAO.addMobilePhone(phone1);
        int phoneId2 = mobilePhoneDAO.addMobilePhone(phone2);
        //set the inserted ids
        phone1.setId(phoneId1);
        phone2.setId(phoneId2);
              
        //insert test data
        int id1 = locationDao.addLocation(location1);
        int id2 = locationDao.addLocation(location2);
        //set the ids
        location1.setId(id1);
        location2.setId(id2);
    }
    
    @After
    public void tearDown()
    {
        deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(MobilePhoneDAO.TABLE_NAME);
    }
    
    @Test
    public void testFindById()
    {
        Location loc = locationDao.getLocation(location1.getId()); 
        Assert.assertEquals(location1.getId(), loc.getId());
        Assert.assertEquals("location1", loc.getLocationName());
        Assert.assertEquals(phone1, loc.getPhone());
        Assert.assertEquals("street1", loc.getStreet());
        Assert.assertEquals("number1", loc.getStreetNumber());
        Assert.assertEquals("city1", loc.getCity());
        Assert.assertEquals(1, loc.getZipcode());
        Assert.assertEquals("notes1", loc.getNotes());
    }
    
    @Test
    public void testFindByName()
    {
        Location loc = locationDao.getLocationByName("location1");
        Assert.assertEquals(location1.getId(), loc.getId());
        Assert.assertEquals("location1", loc.getLocationName());
        Assert.assertEquals(phone1, loc.getPhone());
        Assert.assertEquals("street1", loc.getStreet());
        Assert.assertEquals("number1", loc.getStreetNumber());
        Assert.assertEquals("city1", loc.getCity());
        Assert.assertEquals(1, loc.getZipcode());
        Assert.assertEquals("notes1", loc.getNotes());
    }
    
    @Test
    public void testRemoveLocation()
    {
        locationDao.removeLocation(location1.getId());
        //list all
        List<Location> list = locationDao.listLocations();
        Assert.assertEquals(1, list.size());
    }

    @Test
    public void testListLocation()
    {
        List<Location> list = locationDao.listLocations();
        Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testUpdateLocation()
    {
        //create two indivdual block
        {
            Location loc = locationDao.getLocation(location1.getId());   
            loc.setLocationName("newLocationName");
            loc.setPhone(phone2);
            loc.setStreet("newStreet");
            loc.setStreetNumber("newNumber");
            loc.setCity("newCity");
            loc.setZipcode(3);
            loc.setNotes("newNotes");
            locationDao.updateLocation(loc);
        }
        {
            Location loc = locationDao.getLocation(location1.getId());
            Assert.assertEquals(location1.getId(), loc.getId());
            Assert.assertEquals("newLocationName", loc.getLocationName());
            Assert.assertEquals(phone2, loc.getPhone());
            Assert.assertEquals("newStreet", loc.getStreet());
            Assert.assertEquals("newNumber", loc.getStreetNumber());
            Assert.assertEquals("newCity", loc.getCity());
            Assert.assertEquals(3, loc.getZipcode());
            Assert.assertEquals("newNotes", loc.getNotes());
        }
    }
}
