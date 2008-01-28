package at.rc.tacos.core.db.dao.mysql;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.util.MyUtils;

/**
 * This is a test class to test the functionality of the vehicle detail 
 * @author Michael
 */
public class VehicleDetailDAOMySQLTest extends DBTestBase
{
    //the dao class
    private final VehicleDAO vehicleDAO = DaoFactory.MYSQL.createVehicleDetailDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
    
    MobilePhoneDetail phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("phone2","0664-987654321");
    
    Location location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
    Location location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
    
    VehicleDetail veh1 = new VehicleDetail("veh1","vehicleType1",location1);
    VehicleDetail veh2 = new VehicleDetail("veh2","vehicleType2",location2);
      
    
    @Before
    public void setUp() 
    {
        //insert the phones
        int phoneId1 = mobilePhoneDAO.addMobilePhone(phone1);
        int phoneId2 = mobilePhoneDAO.addMobilePhone(phone2);
        //set the inserted ids
        phone1.setId(phoneId1);
        phone2.setId(phoneId2);
        //insert locations
        int id1 = locationDAO.addLocation(location1);
        int id2 = locationDAO.addLocation(location2);
        //set the ids
        location1.setId(id1);
        location2.setId(id2);
        //insert vehicles
        veh1.setBasicStation(location1);
        veh2.setBasicStation(location2); 
        veh1.setCurrentStation(location1);
        veh2.setCurrentStation(location2);
        veh1.setMobilPhone(phone1);
        veh2.setMobilPhone(phone2);
        vehicleDAO.addVehicle(veh1);
        vehicleDAO.addVehicle(veh2);
        
    }
    
    @After
    public void tearDown()
    {
        deleteTable(MobilePhoneDAO.TABLE_NAME);
        deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(VehicleDAO.TABLE_NAME);
    }
    
    
    @Test
    public void testGetVehicleByName()
    {
    	VehicleDetail vehNew = vehicleDAO.getVehicleByName(veh1.getVehicleName());
    	assertEquals(veh1.getVehicleName(),vehNew.getVehicleName());
    	assertEquals(veh1.getVehicleType(),vehNew.getVehicleType());
    }
    
    @Test
    public void testListVehicles()
    {
    	 List<VehicleDetail> list = vehicleDAO.listVehicles();
         Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveVehicle()
    {
    	 vehicleDAO.removeVehicle(veh1);
         //list all
         List<VehicleDetail> list = vehicleDAO.listVehicles();
         Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testUpdateVehicle()
    {
    	//create two individual block
        {
        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("veh1");
        	System.out.println("..........1  " +vehicle.getVehicleName());
//        	vehicle.setVehicleName("vehNew");
        	vehicle.setVehicleType("newType");
        	vehicleDAO.updateVehicle(vehicle);
        }
        {
        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("veh1");
        	System.out.println("..........2  " +vehicle.getVehicleName());
        	assertEquals("veh1",vehicle.getVehicleName());
        	assertEquals("newType",vehicle.getVehicleType());
        }
    }
}
