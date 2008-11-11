package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request to remove a object from the server
 * 
 * @author Michael
 */
public class RemoveMessage<T> extends AbstractMessage<T> {

	/**
	 * Default class constructor to define the object to remove
	 * 
	 * @param object
	 *            the object to remove
	 */
	public RemoveMessage(T t) {
		objects = new ArrayList<T>();
		objects.add(t);
	}

	/**
	 * Default class constructor to define multiple objects to remove
	 * 
	 * @param objects
	 *            the objects to remove
	 */
	public RemoveMessage(List<T> objects) {
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
