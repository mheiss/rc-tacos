package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Request to add a object to the server
 * 
 * @author mheiss
 */
public class AddMessage extends AbstractMessage {

    /**
     * Default class constructor for a single object to add
     * 
     * @param object
     *            the object to add
     */
    public AddMessage(Object object) {
        objects = new ArrayList<Object>();
        objects.add(object);
    }

    /**
     * Default class constructor for multiple objects to add
     * 
     * @param object
     *            the objects to add
     */
    public AddMessage(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public List<Object> getObjects() {
        return objects;
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }
}
