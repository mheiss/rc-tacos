package at.rc.tacos.platform.net.request;

import java.util.List;

import org.apache.mina.core.session.IoSession;

/**
 * Defines the reqest that can be send to the server
 * 
 * @author mheiss
 * 
 */
public interface Request {

    /**
     * Sends the request to the server and wait for the response
     * 
     * @param session
     *            the session to send the request
     * @return the response from the server
     */
    public List<Object> synchronRequest(IoSession session) throws Exception;

    /**
     * Sends the request to the server without waiting for the response.
     * 
     * @param session
     *            the session to send the request
     */
    public void asnchronRequest(IoSession session);

}
