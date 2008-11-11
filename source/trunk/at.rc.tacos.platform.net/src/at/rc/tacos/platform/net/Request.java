package at.rc.tacos.platform.net;

import org.apache.mina.core.session.IoSession;

/**
 * This interface encapsulates all the client commands.
 * 
 * @author mheiss
 */
public interface Request<M> {

	/**
	 * Sends the request to the server and wait for the response
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
