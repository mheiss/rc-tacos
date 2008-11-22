package at.rc.tacos.client.net.handler;

import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.handler.MessageType;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * The <code>LocationHandler</code> manages the locally chached {@link Login}
 * instances.
 * 
 * @author Michael
 */
public class LoginHandler implements Handler<Login> {

	private List<Login> loginList = Collections.synchronizedList(new LinkedList<Login>());
	private Logger log = LoggerFactory.getLogger(LoginHandler.class);

	@Override
	public void add(MessageIoSession session, Message<Login> message) throws SQLException, ServiceException {
		synchronized (loginList) {
			loginList.addAll(message.getObjects());
		}
	}

	@Override
	public void execute(MessageIoSession session, Message<Login> message) throws SQLException, ServiceException {
		log.debug(MessageType.EXEC + " called but currently not implemented");
	}

	@Override
	public void get(MessageIoSession session, Message<Login> message) throws SQLException, ServiceException {
		synchronized (loginList) {
			loginList.clear();
			loginList.addAll(message.getObjects());
		}
	}

	@Override
	public void remove(MessageIoSession session, Message<Login> message) throws SQLException, ServiceException {
		synchronized (loginList) {
			loginList.removeAll(message.getObjects());
		}
	}

	@Override
	public void update(MessageIoSession session, Message<Login> message) throws SQLException, ServiceException {
		synchronized (loginList) {
			for (Login updatedLogin : message.getObjects()) {
				if (!loginList.contains(loginList)) {
					continue;
				}
				int index = loginList.indexOf(updatedLogin);
				loginList.set(index, updatedLogin);
			}
		}
	}

	/**
	 * Returns the first <code>Login</code> instance that exactly matches the
	 * string returned by {@link Login#getUsername()}.
	 * 
	 * @param username
	 *            the name of the <code>Login</code> instance to search
	 * @return the matched <code>Login</code> or null if nothing found
	 */
	public Login getLoginByUsername(String username) {
		synchronized (loginList) {
			for (Login login : loginList) {
				if (login.getUsername().equalsIgnoreCase(username))
					return login;
			}
			// nothing found
			return null;
		}
	}

	/**
	 * Returns a new array containing the managed <code>Login</code> instances.
	 * 
	 * @return an array containing the <code>Login</code> instances.
	 */
	public Login[] toArray() {
		return loginList.toArray(new Login[loginList.size()]);
	}

}
