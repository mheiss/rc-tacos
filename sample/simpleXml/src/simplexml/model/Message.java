package simplexml.model;

import java.util.Map;

/**
 * Defines the header for the message object
 * @author mheiss
 */
public class Message {
    private String command;
    private String contentClazz;
    private Map<String,String> params;
    
    /**
     * Default class constructor for a new message header.
     * @param command the message command
     * @param contentClazz the fully qualified class name of the content object
     */
    public Message(String command,String contentClazz) {
        this.command = command;
        this.contentClazz = contentClazz;
    }

}
