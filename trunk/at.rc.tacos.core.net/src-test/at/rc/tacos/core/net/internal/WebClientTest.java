package at.rc.tacos.core.net.internal;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.SystemMessage;
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
    private AbstractMessage result;
    private List<AbstractMessage> resultList;
    
    @Before
    public void setUp()
    {
        client = new WebClient();
        client.connect("localhost", 4711);
        resultList = new ArrayList<AbstractMessage>();
    }
    
    @Test
    public void testLoginSuccess()
    {
        result = client.sendLoginRequest("user3","P@ssw0rd");
        Login loginResponse = (Login)result;
        Assert.assertTrue(loginResponse.isLoggedIn());
    }
    
    @Test
    public void testLoginFailed()
    {
        result = client.sendLoginRequest("wrongUser","wrongPassword");
        Login loginResponse = (Login)result;
        Assert.assertFalse(loginResponse.isLoggedIn());
    }
    
    @Test
    public void testUnauthenticatedRequest()
    {
        resultList = client.sendListingRequest(RosterEntry.ID, null);
        Assert.assertEquals(SystemMessage.ID, client.getContentType());
    }
    
    @Test
    public void testTransportListing()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(Transport.ID, null);
        Assert.assertEquals(Transport.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size());
    }
    
    @Test
    public void testRosterEntryListing()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(RosterEntry.ID, null);
        Assert.assertEquals(RosterEntry.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size());
    }
    
    @Test
    public void testStaffMemberListing()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(StaffMember.ID, null);
        Assert.assertEquals(StaffMember.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size());
    }
       
    @Test
    public void testMobilePhoneDetailListing()
    {
        //login
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(MobilePhoneDetail.ID, null);
        Assert.assertEquals(MobilePhoneDetail.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size()); 
    }
    
    @Test
    public void testNotifierDetailListing()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(CallerDetail.ID, null);
        Assert.assertEquals(CallerDetail.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size()); 
    }
    
    @Test
    public void testPatientListing()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(Patient.ID, null);
        Assert.assertEquals(Patient.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size()); 
    }
    
    @Test
    public void testVehicleListing()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        resultList = client.sendListingRequest(VehicleDetail.ID, null);
        Assert.assertEquals(VehicleDetail.ID, client.getContentType());
        Assert.assertEquals(3, resultList.size()); 
    }
    
    @Test
    public void testGetStaffMemberById()
    {
        client.sendLoginRequest("user3","P@ssw0rd");
        //send the request for the listing
        QueryFilter filter = new QueryFilter(IFilterTypes.ID_FILTER,"1");
        resultList = client.sendListingRequest(StaffMember.ID, filter);
        Assert.assertEquals(StaffMember.ID, client.getContentType());
        Assert.assertEquals(1, resultList.size()); 
    }
    
//    @Test
//    public void testAddItem()
//    {
//        client.sendLoginRequest("testUser","P@ssw0rd");
//        Item item = new Item("JUNIT-Test");
//        //send the request for the listing
//        result = client.sendAddRequest(Item.ID, item);
//        Assert.assertEquals(Item.ID, client.getContentType());
//    }
//    
//    @Test
//    public void testRemoveItem()
//    {
//        client.sendLoginRequest("testUser","P@ssw0rd");
//        Item item = new Item("JUNIT-Test");
//        //send the request for the listing
//        result = client.sendRemoveRequest(Item.ID, item);
//        Assert.assertEquals(Item.ID, client.getContentType());
//    }
}
