package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request to update an existing object on the server
 * 
 * @author Michael
 */
public class UpdateMessage<T> extends AbstractMessage<T> {

	/**
	 * Default class constructor to define the object to update
	 * 
	 * @param object
	 *            the object to update
	 */
	public UpdateMessage(T t) {
		objects = new ArrayList<T>();
		objects.add(t);
	}

	/**
	 * Default class constructor to define multiple objects that schould be
	 * update.
	 * 
	 * @param objects
	 *            the objects to update
	 */
	public UpdateMessage(List<T> objects) {
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
