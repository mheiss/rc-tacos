package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The base class to define additional request messages.
 * <p>
 * This is usefully when the default messages types like {@link AddMessage} ,
 * {@link UpdateMessage} , {@link RemoveMessage} or {@link GetMessage} ar not
 * sufficient.
 * <p>
 * 
 * @author mheiss
 */
public abstract class ExecMessage<T> extends AbstractMessage<T> {

	/**
	 * Default class constructor to setup a new exec message
	 * 
	 * @param clazz
	 *            the model class that identifies the handler to be used for the
	 *            request
	 */
	public ExecMessage(T t) {
		this.objects = new ArrayList<T>();
		this.objects.add(t);
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
