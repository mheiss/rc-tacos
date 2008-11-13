package at.rc.tacos.server.net;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.HandlerFactory;

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
		Handler<Address> addressHandler = handlerFactory.getHandler(new Address());
		Assert.assertNotNull(addressHandler);
	}

	@Test
	public void relectionHandlerTest() throws Exception {
		// create a new class with the name
		String addressClazzName = Address.class.getName();
		Class<?> clazz = Class.forName(addressClazzName);
		// get the handler instance
		Handler<Object> addressHandler = handlerFactory.getHandler((Object) clazz.newInstance());
		Assert.assertNotNull(addressHandler);
	}

}
