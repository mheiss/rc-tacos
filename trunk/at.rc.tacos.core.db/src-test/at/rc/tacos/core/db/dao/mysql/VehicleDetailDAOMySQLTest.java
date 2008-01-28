package at.rc.tacos.core.db.dao.mysql;

import static org.junit.Assert.*;

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
import at.rc.tacos.model.Login;
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
    private final UserLoginDAO loginDAO = DaoFactory.MYSQL.createUserDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.MYSQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.MYSQL.createLocationDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.MYSQL.createCompetenceDAO();
	
	//prepare the test data for the unit tests
    MobilePhoneDetail phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
    MobilePhoneDetail phone2 = new MobilePhoneDetail("phone2","0664-987654321");
    //locations
    Location location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
    Location location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
    //vehciles
    VehicleDetail veh1 = new VehicleDetail("vehicle1","vehicleType1",location1);
    VehicleDetail veh2 = new VehicleDetail("vehicle2","vehicleType2",location2);
    //competences
    Competence comp1 = new Competence("comp1");
    Competence comp2 = new Competence("comp2");
    //logins
	Login login1 = new Login("user1","password1",false);
	Login login2 = new Login("user2","password2",false);
    //Staff members
    StaffMember member1 = new StaffMember(50100001,"fname1","lname1","user1","street1","city1",false,MyUtils.stringToTimestamp("27-01-2008",MyUtils.dateFormat),phone1,comp1,"mail1",location1);
    StaffMember member2 = new StaffMember(50100002,"fname2","lname2","user2","street2","city2",true,MyUtils.stringToTimestamp("28-01-2008",MyUtils.dateFormat),phone2,comp2,"mail2",location2);
      
    @Before
    public void setUp() 
    {
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
    	deleteTable(MobilePhoneDAO.TABLE_DEPENDENT_NAME);
    	deleteTable(StaffMemberDAO.TABLE_NAME);
    	deleteTable(UserLoginDAO.TABLE_NAME);
    	deleteTable(LocationDAO.TABLE_NAME);
        deleteTable(VehicleDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_NAME);
        deleteTable(CompetenceDAO.TABLE_DEPENDENT_NAME);
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
        {
        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
        	vehicle.setVehicleType("newType");
        	vehicle.setOutOfOrder(true);
        	vehicle.setReadyForAction(true);
        	vehicle.setVehicleNotes("newNotes");
        	vehicleDAO.updateVehicle(vehicle);
        }
        {
        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
        	assertEquals("vehicle1",vehicle.getVehicleName());
        	assertEquals("newType",vehicle.getVehicleType());
        	assertEquals("newNotes",vehicle.getVehicleNotes());
        	assertTrue(vehicle.isOutOfOrder());
        	assertTrue(vehicle.isReadyForAction());
        }
    }
    
    @Test
    public void testAssignDriverTest()
    {
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		vehicle.setDriver(member1);
    		vehicleDAO.updateVehicle(vehicle);
    	}
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		Assert.assertEquals(member1, vehicle.getDriver());
    		Assert.assertEquals("fname1", vehicle.getDriver().getFirstName());
    		Assert.assertEquals("lname1", vehicle.getDriver().getLastName());
    		Assert.assertEquals("user1",vehicle.getDriver().getUserName());
    	}
    }
    
    @Test
    public void testAssignPrimaryMedicTest()
    {
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		vehicle.setFirstParamedic(member1);
    		vehicleDAO.updateVehicle(vehicle);
    	}
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		Assert.assertEquals(member1, vehicle.getFirstParamedic());
    		Assert.assertEquals("fname1", vehicle.getFirstParamedic().getFirstName());
    		Assert.assertEquals("lname1", vehicle.getFirstParamedic().getLastName());
    		Assert.assertEquals("user1",vehicle.getFirstParamedic().getUserName());
    	}    	
    }
    
    @Test
    public void testAssignSecondaryMedicTest()
    {
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		vehicle.setSecondParamedic(member1);
    		vehicleDAO.updateVehicle(vehicle);
    	}
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		Assert.assertEquals(member1, vehicle.getSecondParamedic());
    		Assert.assertEquals("fname1", vehicle.getSecondParamedic().getFirstName());
    		Assert.assertEquals("lname1", vehicle.getSecondParamedic().getLastName());
    		Assert.assertEquals("user1",vehicle.getSecondParamedic().getUserName());
    	}
    }
    
    @Test
    public void testDetatchDriverTest()
    {
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		vehicle.setDriver(null);
    		vehicleDAO.updateVehicle(vehicle);
    	}
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		Assert.assertNull(vehicle.getDriver());
    	}
    }
    
    @Test
    public void testDetatchPrimaryMedicTest()
    {
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		vehicle.setFirstParamedic(null);
    		vehicleDAO.updateVehicle(vehicle);
    	}
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		Assert.assertNull(vehicle.getFirstParamedic());
    	}
    }
    
    @Test
    public void testDetatchSecondaryMedicTest()
    {
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		vehicle.setSecondParamedic(null);
    		vehicleDAO.updateVehicle(vehicle);
    	}
    	{
    		VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
    		Assert.assertNull(vehicle.getDriver());
    	}
    }
}
