package at.rc.tacos.core.db.dao.sqlserver;

import static org.junit.Assert.*;

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
import at.rc.tacos.core.db.dao.VehicleDAO;
import at.rc.tacos.core.db.dao.factory.DaoFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;

/**
 * This is a test class to test the functionality of the vehicle detail 
 * @author Michael
 */
public class VehicleDetailDAOSQLTest extends DBTestBase
{
    //the dao class
    private final VehicleDAO vehicleDAO = DaoFactory.SQL.createVehicleDetailDAO();
    private final StaffMemberDAO staffDAO = DaoFactory.SQL.createStaffMemberDAO();
    private final UserLoginDAO loginDAO = DaoFactory.SQL.createUserDAO();
	private final MobilePhoneDAO mobilePhoneDAO = DaoFactory.SQL.createMobilePhoneDAO();
	private final LocationDAO locationDAO = DaoFactory.SQL.createLocationDAO();
	private final CompetenceDAO competenceDAO = DaoFactory.SQL.createCompetenceDAO();
	
	//prepare the test data for the unit tests
    MobilePhoneDetail phone1,phone2;
    Location location1,location2;
    VehicleDetail veh1,veh2;
    Competence comp1,comp2;
	Login login1,login2;
    StaffMember member1,member2;
    @Before
    public void setUp() throws SQLException
    {
        phone1 = new MobilePhoneDetail("phone1","0664-123456789"); 
        phone2 = new MobilePhoneDetail("phone2","0664-987654321");
        location1 = new Location("location1",phone1,"street1","number1",1,"city1","notes1");
        location2 = new Location("location2",phone2,"street2","number2",2,"city2","notes2");
        veh1 = new VehicleDetail("vehicle1","vehicleType1",location1, location2, phone1, "vehicle notes...", true, false);
        veh2 = new VehicleDetail("vehicle2","vehicleType2",location2, location1, phone1, "vehicle notes...", false, true);
        comp1 = new Competence("comp1");
        comp2 = new Competence("comp2");
        login1 = new Login("user1","password1",false);
    	login2 = new Login("user2","password2",false);
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
        //assign the logins to the staff members
        login1.setUserInformation(member1);
        login1.setIslocked(false);
        login1.setAuthorization("Administrator");
        login2.setUserInformation(member2);
        login2.setIslocked(true);
        login2.setAuthorization("Benutzer");
        loginDAO.addLogin(login1);
        loginDAO.addLogin(login2);
        staffDAO.addStaffMember(member1);
        staffDAO.addStaffMember(member2);
        
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
    public void tearDown() throws SQLException
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
    public void testGetVehicleByName() throws SQLException
    {
    	VehicleDetail vehNew = vehicleDAO.getVehicleByName(veh1.getVehicleName());
    	assertEquals(veh1.getVehicleName(),vehNew.getVehicleName());
    	assertEquals(veh1.getVehicleType(),vehNew.getVehicleType());
    }
    
    @Test
    public void testListVehicles() throws SQLException
    {
    	 List<VehicleDetail> list = vehicleDAO.listVehicles();
         Assert.assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveVehicle() throws SQLException
    {
    	 vehicleDAO.removeVehicle(veh1);
         //list all
         List<VehicleDetail> list = vehicleDAO.listVehicles();
         Assert.assertEquals(1, list.size());
    }
    
    @Test
    public void testUpdateVehicle() throws SQLException
    {
        {
        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
        	vehicle.setVehicleType("newType");
        	vehicle.setOutOfOrder(true);
        	vehicle.setReadyForAction(true);
        	vehicle.setVehicleNotes("newNotes");
        	vehicle.setLastDestinationFree("letztes Ziel frei");
        	vehicleDAO.updateVehicle(vehicle);
        }
        {
        	VehicleDetail vehicle = vehicleDAO.getVehicleByName("vehicle1");
        	assertEquals("vehicle1",vehicle.getVehicleName());
        	assertEquals("newType",vehicle.getVehicleType());
        	assertEquals("newNotes",vehicle.getVehicleNotes());
        	assertEquals("letztes Ziel frei", vehicle.getLastDestinationFree());
        	assertTrue(vehicle.isOutOfOrder());
        	assertTrue(vehicle.isReadyForAction());
        }
    }
    
    @Test
    public void testAssignDriverTest() throws SQLException
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
    public void testAssignPrimaryMedicTest() throws SQLException
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
    public void testAssignSecondaryMedicTest() throws SQLException
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
    public void testDetatchDriverTest() throws SQLException
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
    public void testDetatchPrimaryMedicTest() throws SQLException
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
    public void testDetatchSecondaryMedicTest() throws SQLException
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