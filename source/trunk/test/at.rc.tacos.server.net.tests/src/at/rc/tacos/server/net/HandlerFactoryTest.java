package at.rc.tacos.server.net;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.services.net.HandlerFactory;
import at.rc.tacos.platform.services.net.INetHandler;

/**
 * Test cases for the handler factory
 * 
 * @author Michael
 */
public class HandlerFactoryTest {

	private HandlerFactory handlerFactory;

	@Before
	public void setup() {
		handlerFactory = new HandlerFactoryImpl();
	}

	@Test
	public void getAddressHandlerTest() throws Exception {
		INetHandler<Address> addressHandler = handlerFactory.getTypeSaveHandler(new Address());
		Assert.assertNotNull(addressHandler);
	}

	@Test
	public void relectionHandlerTest() throws Exception {
		// create a new class with the name
		String addressClazzName = Address.class.getName();
		Class<?> clazz = Class.forName(addressClazzName);
		// get the handler instance
		INetHandler<Object> addressHandler = handlerFactory.getTypeSaveHandler((Object) clazz.newInstance());
		Assert.assertNotNull(addressHandler);
	}

}
