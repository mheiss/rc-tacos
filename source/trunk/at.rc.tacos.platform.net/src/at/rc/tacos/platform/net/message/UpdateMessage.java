package at.rc.tacos.platform.net.message;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Request to update one or more existing objects on the server.
 * 
 * @param <T>
 *            the model class that identifies the handler to execute the message
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
        super(MessageType.UPDATE);
        addObject(t);
    }

    /**
     * Default class constructor to define multiple objects that schould be update.
     * 
     * @param objects
     *            the objects to update
     */
    public UpdateMessage(List<T> objects) {
        super(MessageType.UPDATE);
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
