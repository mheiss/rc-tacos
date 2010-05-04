package at.rc.tacos.platform.net;

import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;

/**
 * This interface encapsulates all the requests.
 * 
 * @author mheiss
 */
public interface Request<M> {

    /**
     * The timeout in {@link TimeUnit#MILLISECONDS} to wait for synchron operations to end.
     */
    public static final int TIMEOUT = 3000;

    /**
     * Sends the request to the server and wait for the response.
     * 
     * @param session
     *            the session to send the request
     * @return the response from the server
     */
    public Message<M> synchronRequest(IoSession session) throws Exception;

    /**
     * Sends the request to the server without waiting for the response.
     * 
     * @param session
     *            the session to send the request
     */
    public void asnchronRequest(IoSession session);

}
