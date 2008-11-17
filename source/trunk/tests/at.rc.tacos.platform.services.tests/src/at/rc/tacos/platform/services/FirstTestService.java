package at.rc.tacos.platform.services;

/**
 * Simple test class that defines a service that is needed by this class.
 * 
 * @author Michael
 */
public class FirstTestService {

	@Service(clazz = SecondTestService.class)
	private SecondTestService secondService;

	/**
	 * Returns the dependend service
	 */

	public SecondTestService getSecondService() {
		return secondService;
	}
}
