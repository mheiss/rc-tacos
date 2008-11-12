package at.rc.tacos.platform.net.message;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Request to remove one or more objects from the server.
 * 
 * @param <T>
 *            the model class that identifies the handler to execute the message
 * 
 * @author Michael
 */
public class RemoveMessage<T> extends AbstractMessage<T> {

    /**
     * Default class constructor to define the object to remove.
     * 
     * @param t
     *            the model instance to remove
     */
    public RemoveMessage(T t) {
        super(MessageType.REMOVE);
        addObject(t);
    }

    /**
     * Default class constructor to define multiple objects to remove
     * 
     * @param objects
     *            the object instances to remove
     */
    public RemoveMessage(List<T> objects) {
        super(MessageType.REMOVE);
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
