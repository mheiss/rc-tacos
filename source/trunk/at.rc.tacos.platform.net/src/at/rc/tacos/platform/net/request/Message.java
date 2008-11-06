package at.rc.tacos.platform.net.request;

import java.util.List;
import java.util.Map;

/**
 * Defines the message that can be send to the server
 * 
 * @author Michael
 */
public interface Message {

    public String getId();

    public abstract Map<String, String> getParams();

    public abstract List<Object> getObjects();

}
