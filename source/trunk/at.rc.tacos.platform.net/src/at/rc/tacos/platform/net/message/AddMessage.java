package at.rc.tacos.platform.net.message;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Request to add one or more objects to the server.
 * 
 * @param <E>
 *            the model class that identifies the handler to execute the message
 * @author mheiss
 */
public class AddMessage<E> extends AbstractMessage<E> {

	/**
	 * Default class constructor for a single object to add
	 * 
	 * @param object
	 *            the object to add
	 */
	public AddMessage(E o) {
		super(MessageType.ADD);
		addObject(o);
	}

	/**
	 * Default class constructor for multiple objects to add
	 * 
	 * @param object
	 *            the objects to add
	 */
	public AddMessage(List<E> objects) {
		super(MessageType.ADD);
		this.objects = objects;
	}

	@Override
	public List<E> getObjects() {
		return objects;
	}

	@Override
	public Map<String, String> getParams() {
		return null;
	}
}
