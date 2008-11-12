package at.rc.tacos.platform.net.message;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Request to add one or more objects to the server.
 * 
 * @param <T>
 *            the model class that identifies the handler to execute the message
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
        super(MessageType.ADD);
        addObject(t);
    }

    /**
     * Default class constructor for multiple objects to add
     * 
     * @param object
     *            the objects to add
     */
    public AddMessage(List<T> objects) {
        super(MessageType.ADD);
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
