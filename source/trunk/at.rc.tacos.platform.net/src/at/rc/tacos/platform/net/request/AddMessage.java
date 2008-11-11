package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request to add a object to the server
 * 
 * @author mheiss
 */
public class AddMessage<T> extends AbstractMessage<T> {

	/**
	 * Default class constructor for a single object to add
	 * 
	 * @param object
	 *            the object to add
	 */
	public AddMessage(T t) {
		objects = new ArrayList<T>();
		objects.add(t);
	}

	/**
	 * Default class constructor for multiple objects to add
	 * 
	 * @param object
	 *            the objects to add
	 */
	public AddMessage(List<T> objects) {
		this.objects = objects;
	}

	@Override
	public List<T> getObjects() {
		return objects;
	}

	@Override
	public Map<String, String> getParams() {
		return null;
	}
}
