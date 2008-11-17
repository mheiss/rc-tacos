package at.rc.tacos.platform.services;

/**
 * Defines a simple service class that defines a service that is needed
 * 
 * @author Michael
 */
public class SecondTestService {

	@Service(clazz = String.class)
	private String string;

	public String getString() {
		return string;
	}

}
