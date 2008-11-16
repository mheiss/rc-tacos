package at.rc.tacos.server.net;

import java.util.Arrays;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.services.dbal.AddressService;
import at.rc.tacos.server.net.handler.AddressHandler;

/**
 * Test cases for the {@link AddressHandler} to test the related
 * {@link AddressService}
 * 
 * @author Michael
 */
public class AddressHandlerTests extends BaseTestcase {

	@BeforeClass
	public static void setup() throws Exception {
		startServer();
		startClient();
	}

	@Test
	public void addAddress() throws Exception {
		// create a new address record and add it
		Address adr = new Address(1234, "city", "street");
		AddMessage<Address> addAddress = new AddMessage<Address>(adr);
		Message<Address> response = addAddress.synchronRequest(getSession());

		Address adr1 = response.getObjects().get(0);

		// check the response
		Assert.assertEquals(1, response.getObjects().size());
		Assert.assertEquals(1234, adr1.getZip());
		Assert.assertEquals("city", adr1.getCity());
		Assert.assertEquals("street", adr1.getStreet());
	}

	@Test
	public void addRollbackAddress() throws Exception {
		// create one valid and one invalid record
		Address adr = new Address(1235, "city", "street");

		AddMessage<Address> addAddress = new AddMessage<Address>(Arrays.asList(new Address[] { adr, null }));
		Message<Address> response = addAddress.synchronRequest(getSession());

		// check the response
		Assert.assertEquals(1, response.getObjects().size());
	}

	@AfterClass
	public static void shutdown() throws Exception {
		stopClient();
		stopServer();
	}
}
