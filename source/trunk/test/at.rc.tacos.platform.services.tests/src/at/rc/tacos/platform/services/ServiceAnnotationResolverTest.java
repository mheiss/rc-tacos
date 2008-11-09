package at.rc.tacos.platform.services;

import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ServiceAnnotationResolverTest {

	private ServiceFactory serviceFactory;

	@Before
	public void setup() {
		serviceFactory = new TestServiceFactory();
	}

	@Test
	public void resolveServiceTest() throws Exception {
		// create a new instance of the service
		FirstTestService firstService = new FirstTestService();

		// try to resolve the dependen services
		ServiceAnnotationResolver resolver = new ServiceAnnotationResolver(serviceFactory);
		List<Object> resolvedList = resolver.resolveAnnotations(firstService);

		// now try to get the second service
		SecondTestService secondService = firstService.getSecondService();
		String message = secondService.getString();

		// check the resoluts
		Assert.assertNotNull(secondService);
		Assert.assertNotNull(message);
		Assert.assertEquals(3, resolvedList.size());
		Assert.assertEquals("Resolved :)", message);
	}

}
