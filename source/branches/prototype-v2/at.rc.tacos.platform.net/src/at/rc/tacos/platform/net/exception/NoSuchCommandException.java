package at.rc.tacos.platform.net.exception;

import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * This exception is thrown is the requested command is unknown or not implemented.
 * 
 * @author Michael
 */
public class NoSuchCommandException extends ServiceException {

    private static final long serialVersionUID = 1L;

    public NoSuchCommandException(String handler, String command) {
        super("The handler '" + handler + "' could not execute the command '" + command
                + "', command is unknown");
    }

}
