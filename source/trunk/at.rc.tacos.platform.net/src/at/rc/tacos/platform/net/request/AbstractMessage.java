package at.rc.tacos.platform.net.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.apache.mina.core.future.ReadFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;

/**
 * The abstract base class for all messages. Provides convenient methods to send the requests to the
 * server.
 * 
 * @author mheiss
 */
public abstract class AbstractMessage implements Message, Request {

    // the timeout to wait for syncron messages
    private static int TIMEOUT = 10000;

    // the properties for the message
    protected String id;
    protected Map<String, String> params;
    protected List<Object> objects;

    /**
     * Helper method to add an parameter to filter the request
     * 
     * @param key
     *            the key of the filter parameter
     * @param value
     *            the value of the filter parameter
     */
    public void addParameter(String key, String value) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        params.put(key, value);
    }

    //
    // METHODS TO SEND THE REQUEST
    //
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

        // setup and initialize the values to send
        init();

        // now send the message
        session.write(this);
    }

    /**
     * Sends the request and waits for the response of the server
     * 
     * @param session
     *            the session to write the request to
     * @throws TimeoutException
     *             if the server did not response in time
     */
    @Override
    public Message synchronRequest(IoSession session) throws Exception {
        if (session == null)
            throw new IllegalArgumentException("The session cannot be null");

        // setup and initialize the values to send
        init();

        // get the unique identifiere for the request
        UUID requestIdentifier = UUID.fromString(id);

        // wait for the response
        session.write(this);
        session.getConfig().setUseReadOperation(true);

        // flag to determine if we have a response
        boolean validResponse = false;

        while (!validResponse) {
            // wait for the response from the server
            ReadFuture future = session.read();
            if (!future.await(TIMEOUT)) {
                throw new TimeoutException(
                        "Timeout occred while waiting for a response from the server");
            }

            // check the received id of the message
            Message serverMessage = (Message) future.getMessage();
            UUID responseIdentifier = UUID.fromString(serverMessage.getId());

            // check if the identifiers are the same
            if (requestIdentifier.compareTo(responseIdentifier) != 0) {
                // identifiers are not the same so pass to the handler
                IoHandler handler = session.getHandler();
                handler.messageReceived(future.getSession(), future.getMessage());
                continue;
            }
            return serverMessage;
        }
        // cannot read the response
        throw new IllegalStateException("Failed to get the response from the server");
    }

    //
    // Helper methods
    //
    protected String generateUniqueIdentifiere() {
        UUID uniqueIdentifier = UUID.randomUUID();
        return uniqueIdentifier.toString();
    }

    /**
     * Helper method to initialize the values
     */
    private void init() {
        this.id = getId();
        this.params = getParams();
        this.objects = getObjects();
    }

    //
    // GETTER FOR THE VALUES TO SEND
    //
    /**
     * Returns the unique identifier for the message. The default implementation will generate a
     * unique identifiere if no id has been set.
     * 
     * @return the unique identifier
     */
    @Override
    public String getId() {
        if (id == null) {
            id = generateUniqueIdentifiere();
        }
        return id;
    }

    /**
     * Returns the objects that should be send to the server
     * 
     * @return the objects to send
     */
    @Override
    public abstract List<Object> getObjects();

    /**
     * Returns the request parameters to use.
     * 
     * @return the request parameters
     */
    @Override
    public abstract Map<String, String> getParams();

}
