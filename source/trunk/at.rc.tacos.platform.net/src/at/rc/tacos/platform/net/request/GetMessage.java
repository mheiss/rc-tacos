package at.rc.tacos.platform.net.request;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.model.RosterEntry;

public class GetMessage extends AbstractMessage {

    /**
     * Default class constructor that defines a single object type to request.
     * <p>
     * <b>Example</b> To make a request agains {@link RosterEntry} object types the parameter would
     * be <code>RosterEntry.class</code>
     * </p>
     * 
     * @param clazz
     *            the model class that identifies the handler to be used for the request
     */
    public GetMessage(Class<?> clazz) {
        objects = new ArrayList<Object>();
        objects.add(clazz.getName());
    }

    /**
     * Default class constructor that defines multiple objects to request
     * 
     * @param clazz
     *            the comma separated model types to identify the handlers for the request
     */
    public GetMessage(Class<?>... classes) {
        objects = new ArrayList<Object>();
        for (Class<?> clazz : classes) {
            objects.add(clazz.getName());
        }
    }

    @Override
    public List<Object> getObjects() {
        return objects;
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }

}
