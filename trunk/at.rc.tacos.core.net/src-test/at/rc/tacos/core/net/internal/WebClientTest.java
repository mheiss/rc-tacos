package at.rc.tacos.core.net.internal;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.model.Item;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.NotifierDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.SystemMessage;
import at.rc.tacos.model.TestDataSource;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * Unit test for the web client
 * @author Michael
 */
public class WebClientTest
{
    //test object
    private WebClient client;
    private List<AbstractMessage> result;
    
    @Before
    public void setUp()
    {
        client = new WebClient();
        client.connect("81.189.52.155", 4711);
        result = new ArrayList<AbstractMessage>();
    }
    
    @After
    public void tearDown()
    {
        client.quit();
    }
    
    @Test
    public void testLoginSuccess()
    {
        //create a login object
        Login login = new Login("user1","P@ssw0rd");
        result = client.sendRequest("user1", Login.ID, IModelActions.LOGIN, login);
        Login loginResponse = (Login)result.get(0);
        Assert.assertTrue(loginResponse.isLoggedIn());
    }
    
    @Test
    public void testLoginFailed()
    {
        //create a login object
        Login login = new Login("user1","wrongPwd");
        result = client.sendRequest("user1", Login.ID, IModelActions.LOGIN, login);
        Login loginResponse = (Login)result.get(0);
        Assert.assertFalse(loginResponse.isLoggedIn());
    }
    
    @Test
    public void testUnauthenticatedRequest()
    {
        RosterEntry entry = new TestDataSource().rosterList.get(0);
        result = client.sendRequest("user1", RosterEntry.ID, IModelActions.LIST, entry);
        Assert.assertEquals(SystemMessage.ID, client.getContentType());
    }
    
    @Test
    public void testTransportListing()
    {
        //login
        Login login = new Login("user1","P@ssw0rd");
        client.sendRequest("user1", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user1", Transport.ID, IModelActions.LIST, null);
        Assert.assertEquals(Transport.ID, client.getContentType());
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void testRosterEntryListing()
    {
        //login
        Login login = new Login("user2","P@ssw0rd");
        client.sendRequest("user2", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user2", RosterEntry.ID, IModelActions.LIST, null);
        Assert.assertEquals(RosterEntry.ID, client.getContentType());
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void testStaffMemberListing()
    {
        //login
        Login login = new Login("user3","P@ssw0rd");
        client.sendRequest("user3", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user3", StaffMember.ID, IModelActions.LIST, null);
        Assert.assertEquals(StaffMember.ID, client.getContentType());
        Assert.assertEquals(5, result.size());
    }
    
    @Test
    public void testItemListing()
    {
        //login
        Login login = new Login("user1","P@ssw0rd");
        client.sendRequest("user1", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user1", Item.ID, IModelActions.LIST, null);
        Assert.assertEquals(Item.ID, client.getContentType());
        Assert.assertEquals(3, result.size());
    }
    
    @Test
    public void testMobilePhoneDetailListing()
    {
        //login
        Login login = new Login("user2","P@ssw0rd");
        client.sendRequest("user2", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user2", MobilePhoneDetail.ID, IModelActions.LIST, null);
        Assert.assertEquals(MobilePhoneDetail.ID, client.getContentType());
        Assert.assertEquals(3, result.size()); 
    }
    
    @Test
    public void testNotifierDetailListing()
    {
        //login
        Login login = new Login("user3","P@ssw0rd");
        client.sendRequest("user3", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user3", NotifierDetail.ID, IModelActions.LIST, null);
        Assert.assertEquals(NotifierDetail.ID, client.getContentType());
        Assert.assertEquals(3, result.size()); 
    }
    
    @Test
    public void testPatientListing()
    {
        //login
        Login login = new Login("user1","P@ssw0rd");
        client.sendRequest("user1", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user1", Patient.ID, IModelActions.LIST, null);
        Assert.assertEquals(Patient.ID, client.getContentType());
        Assert.assertEquals(3, result.size()); 
    }
    
    @Test
    public void testVehicleListing()
    {
        //login
        Login login = new Login("user2","P@ssw0rd");
        client.sendRequest("user2", Login.ID, IModelActions.LOGIN, login);
        //send the request for the listing
        result = client.sendRequest("user2", VehicleDetail.ID, IModelActions.LIST, null);
        Assert.assertEquals(VehicleDetail.ID, client.getContentType());
        Assert.assertEquals(3, result.size()); 
    }
    
//    @Test
//    public void testAddStaffMember()
//    {
//        //login
//        Login login = new Login("user2","P@ssw0rd");
//        client.sendRequest("user2", Login.ID, IModelActions.LOGIN, login);
//        //send the add request
//        StaffMember member = new StaffMember("Staff4","Staff4","nick.staff4");
//        result = client.sendRequest("user2", StaffMember.ID, IModelActions.ADD, member);
//        Assert.assertEquals(StaffMember.ID, client.getContentType());
//    }
    
//    @Test
//    public void testRemoveStaffMember()
//    {
//        //login
//        Login login = new Login("user2","P@ssw0rd");
//        client.sendRequest("user2", Login.ID, IModelActions.LOGIN, login);
//        //send the add request
//        StaffMember member = new StaffMember("Staff4","Staff4","nick.staff4");
//        member.setPersonId(3);
//        result = client.sendRequest("user2", StaffMember.ID, IModelActions.REMOVE, member);
//        Assert.assertEquals(StaffMember.ID, client.getContentType());
//        System.out.println(result.get(0));
//    }
}
