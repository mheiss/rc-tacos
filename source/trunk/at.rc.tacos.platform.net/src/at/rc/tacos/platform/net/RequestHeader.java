package at.rc.tacos.platform.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the header for the request object
 * 
 * @author mheiss
 */
public class RequestHeader {

    private String id;
    private RequestCommand command;
    private Map<String, String> params;

    /**
     * Default clas constructor
     */
    public RequestHeader() {
        this.params = new HashMap<String, String>();
    }

    /**
     * Default class constructor for a new request header.
     * 
     * @param id
     *            the unique identifiere for a request
     * @param command
     *            the request command
     */
    public RequestHeader(String id, RequestCommand command) {
        this.id = id;
        this.command = command;
        this.params = new HashMap<String, String>();
       
    }

    // COMMON METHODS
    public void addParam(String key, String value) {
        params.put(key, value);
    }

    // DEFAULT GETTERS AND SETTERS
    public String getId() {
        return id;
    }

    public RequestCommand getCommand() {
        return command;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCommand(RequestCommand command) {
        this.command = command;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
