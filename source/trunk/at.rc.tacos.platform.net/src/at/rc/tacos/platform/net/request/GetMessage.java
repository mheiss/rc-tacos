package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.RosterEntry;

public class GetMessage<T> extends AbstractMessage<T> {

	/**
	 * Default class constructor that defines a single object type to request.
	 * <p>
	 * <b>Example</b> To make a request agains {@link RosterEntry} object types
	 * the parameter would be <code>RosterEntry.class</code>
	 * </p>
	 * 
	 * @param clazz
	 *            the model class that identifies the handler to be used for the
	 *            request
	 */
	public GetMessage(T t) {
		objects = new ArrayList<T>();
		objects.add(t);
	}

	@Override
	public List<T> getObjects() {
		return objects;
	}

	@Override
	public Map<String, String> getParams() {
		return params;
	}

}
