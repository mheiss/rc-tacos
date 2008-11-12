package at.rc.tacos.platform.net.message;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Request to execute a custom command on the server.
 * <p>
 * This type of message is typically used when no one of the standard messages types like
 * {@link AddMessage}, {@link UpdateMessage}, {@link RemoveMessage} or {@link GetMessage} is
 * appropriate for the request.
 * </p>
 * 
 * @author mheiss
 */
public abstract class ExecMessage<T> extends AbstractMessage<T> {

    /**
     * Default class constructor to setup a new exec message
     * 
     * @param command
     *            the command that should be executed by the handler
     * @param t
     *            the model class that identifies the handler to be used for the request
     */
    public ExecMessage(String command, T t) {
        super(MessageType.EXEC);
        addObject(t);
        addParameter(ATTRIBUTE_COMMAND, command);
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
