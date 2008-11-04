package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.net.RequestCommand;

/**
 * Request to add a object to the server
 * 
 * @author mheiss
 * 
 */
public class AddRequest extends DefaultRequest {

    // the attributes for the add request
    private List<Object> objects = new ArrayList<Object>();

    /**
     * Default class constructor for a single object to add
     * 
     * @param object
     *            the object to add
     */
    public AddRequest(Object object) {
        objects.add(object);
    }

    /**
     * Default class constructor for multiple objects to add
     * 
     * @param object
     *            the objects to add
     */
    public AddRequest(List<Object> objects) {
        this.objects = objects;
    }

    @Override
    public List<Object> getObjects() {
        return objects;
    }

    @Override
    public RequestCommand getRequestCommand() {
        return RequestCommand.ADD;
    }

    @Override
    public Map<String, String> getRequestParams() {
        return null;
    }
}
