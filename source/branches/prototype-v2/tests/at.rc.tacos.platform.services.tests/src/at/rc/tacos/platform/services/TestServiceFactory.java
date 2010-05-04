package at.rc.tacos.platform.services;

import at.rc.tacos.platform.services.exception.ServiceException;

public class TestServiceFactory implements ServiceFactory {

	@Override
	public Object getService(String modelClazz) throws ServiceException {
		if (FirstTestService.class.getName().equalsIgnoreCase(modelClazz))
			return new FirstTestService();
		if (SecondTestService.class.getName().equalsIgnoreCase(modelClazz))
			return new SecondTestService();
		if (String.class.getName().equalsIgnoreCase(modelClazz))
			return new String("Resolved :)");
		return null;
	}
}
