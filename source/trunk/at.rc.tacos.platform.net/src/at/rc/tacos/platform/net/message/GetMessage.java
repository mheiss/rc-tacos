package at.rc.tacos.platform.net.message;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Request to get a listing of objects from the server. The request can be parametrized using the
 * {@link AbstractMessage#addParameter(String, String)} method.
 * 
 * @author mheiss
 * 
 * @param <T>
 *            the model class that identifies the handler to execute the message
 */
public class GetMessage<T> extends AbstractMessage<T> {

    /**
     * Default class constructor to identify the handler to execute the message
     * 
     * @param t
     *            the model class that identifies the handler to be used for the request
     */
    public GetMessage(T t) {
        super(MessageType.GET);
        addObject(t);
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
