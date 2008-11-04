package at.rc.tacos.platform.net.request;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;

import at.rc.tacos.platform.net.Constants;
import at.rc.tacos.platform.net.RequestCommand;

/**
 * The abstract base class for all requests
 * 
 * @author mheiss
 * 
 */
@SuppressWarnings("unchecked")
public abstract class AbstractRequest implements Request {

    /**
     * Sends the request to the server without waiting for the response
     * 
     * @param session
     *            the session to write the request to
     */
    @Override
    public void asnchronRequest(IoSession session) {
        if (session == null)
            throw new IllegalArgumentException("The session cannot be null");

        // set the needed base attributes and write the objects
        setupAttributes(session);
        session.write(getObjects());
    }

    /**
     * Sends the request and waits for the response of the server
     * 
     * @param session
     *            the session to write the request to
     */
    @Override
    public List<Object> synchronRequest(IoSession session) throws Exception {
        if (session == null)
            throw new IllegalArgumentException("The session cannot be null");

        // set the needed base attributes
        setupAttributes(session);

        // get the unique identifiere for the request
        String id = (String) session.getAttribute(Constants.UNIQUE_ID);
        UUID requestIdentifier = UUID.fromString(id);

        // wait for the response
        session.write(getObjects());
        session.getConfig().setUseReadOperation(true);

        // flag to determine if we have a response
        boolean interrupted = false;

        while (!interrupted) {
            // wait for the response from the server
            ReadFuture future = session.read();
            future = future.awaitUninterruptibly();

            // check the received id of the message
            id = (String) session.getAttribute(Constants.UNIQUE_ID);
            UUID responseIdentifier = UUID.fromString(id);

            // check if the identifiers are the same
            if (requestIdentifier.compareTo(responseIdentifier) != 0) {
                // identifiers are not the same so pass to the handler
                IoHandler handler = session.getHandler();
                handler.messageReceived(future.getSession(), future.getMessage());
                continue;
            }
            return (List<Object>) future.getMessage();
        }
        // cannot read the response
        return null;
    }

    /**
     * Setup the session with the needed attributes
     */
    private void setupAttributes(IoSession session) {
        session.setAttribute(Constants.UNIQUE_ID, getUniqueRequestId());
        session.setAttribute(Constants.COMMAND, getRequestCommand());
        session.setAttribute(Constants.PARAMS, getRequestParams());
    }

    /**
     * Generates a unique identifier for the request.
     * 
     * @return the unique identifier
     */
    protected String getUniqueRequestId() {
        UUID uniqueIdentifier = UUID.randomUUID();
        return uniqueIdentifier.toString();
    }

    /**
     * Returns the request command that for this request
     * 
     * @return the command to execute
     */
    public abstract RequestCommand getRequestCommand();

    /**
     * Returns the objects that should be send to the server
     * 
     * @return the objects to send
     */
    public abstract List<Object> getObjects();

    /**
     * Returns the request parameters to use.
     * 
     * @return the request parameters
     */
    public abstract Map<String, String> getRequestParams();

}
