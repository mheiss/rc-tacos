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
    
    //test data
    Location location1 = new Location();
    Location location2 = new Location();
    //test phone for the locations
    MobilePhoneDetail phone1 = new MobilePhoneDetail("PHONE1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("PHONE2","0664-987654321");
    
    @Before
    public void setUp() 
    {
        //create the phones
        int phoneId1 = mobilePhoneDAO.addMobilePhone(phone1);
        int phoneId2 = mobilePhoneDAO.addMobilePhone(phone2);
        //set the inserted ids
        phone1.setId(phoneId1);
        phone2.setId(phoneId2);
        
        //create the first location
        location1.setLocationName("location1");
        location1.setPhone(phone1);
        location1.setStreet("street1");
        location1.setStreetNumber("number1");
        location1.setCity("city1");
        location1.setZipcode(1);
        location1.setNotes("notes1");
        //create the second location
        location1.setLocationName("location2");
        location1.setPhone(phone2);
        location1.setStreet("street2");
        location1.setStreetNumber("number2");
        location1.setCity("city2");
        location1.setZipcode(2);
        location1.setNotes("notes2");
        
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
        Location comp = locationDao.getLocation(location1.getId()); 
        Assert.assertEquals("location1", comp.getLocationName());
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
            locationDao.updateLocation(loc);
        }
        {
            Location comp = locationDao.getLocation(location1.getId());
            Assert.assertEquals("newLocationName", comp.getLocationName());
        }
    }
}
