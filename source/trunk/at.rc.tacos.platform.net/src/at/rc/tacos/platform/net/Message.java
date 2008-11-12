package at.rc.tacos.platform.net;

import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.handler.MessageType;

/**
 * Provides the request informations.
 * 
 * @author Michael
 */
public interface Message<M> {

    public String getId();

    public MessageType getMessageType();

    public abstract Map<String, String> getParams();

    public abstract List<M> getObjects();

}
