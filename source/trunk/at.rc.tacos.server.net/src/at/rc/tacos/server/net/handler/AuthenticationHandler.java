package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Lockable;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.exception.NoSuchCommandException;
import at.rc.tacos.platform.net.handler.Handler;
import at.rc.tacos.platform.net.message.AbstractMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.AuthenticationService;
import at.rc.tacos.platform.services.dbal.LockableService;
import at.rc.tacos.platform.services.exception.ServiceException;

public class AuthenticationHandler implements Handler<Login> {

	private Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

	@Service(clazz = AuthenticationService.class)
	private AuthenticationService authenticationService;

	@Service(clazz = LockableService.class)
	private LockableService lockableService;

	@Override
	public void add(MessageIoSession session, Message<Login> message) throws ServiceException, SQLException {
		List<Login> loginList = message.getObjects();
		// add the login records to the database
		for (Login login : loginList) {
			if (!authenticationService.addLogin(login)) {
				throw new ServiceException("Failed to add the login " + login + " to the database");
			}
		}
		session.writeResponseBrodcast(message, loginList);
	}

	@Override
	public void get(MessageIoSession session, Message<Login> message) throws ServiceException, SQLException {
		// get the params from the message
		Map<String, String> params = message.getParams();
		List<Login> loginList = new ArrayList<Login>();

		// query the service for the result list
		if (params.containsKey(IFilterTypes.USERNAME_FILTER)) {
			loginList = authenticationService.listLoginsAndStaffMemberByUsername(params.get(IFilterTypes.USERNAME_FILTER));
		}
		else {
			loginList = authenticationService.listLogins();
		}
		// assert valid
		if (loginList == null)
			throw new ServiceException("Failed to list the login records.");

		// check if we have locks for the login records
		for (Login login : loginList) {
			Lockable lockable = lockableService.getLock(login);
			if (lockable == null) {
				continue;
			}
			login.setLocked(lockable.isLocked());
			login.setLockedBy(lockable.getLockedBy());
		}

		// write back the result
		session.writeResponse(message, loginList);
	}

	@Override
	public void remove(MessageIoSession session, Message<Login> message) throws ServiceException, SQLException {
		List<Login> loginList = message.getObjects();
		// loop and remove each login record
		for (Login login : loginList) {
			// the user will only be locked, removing is not possible
			if (!authenticationService.lockLogin(login.getUsername()))
				throw new ServiceException("Failed to lock the login: " + login);
			login.setIslocked(true);
			// remove the lockable object
			lockableService.removeLock(login);
		}
		session.writeResponseBrodcast(message, loginList);
	}

	@Override
	public void update(MessageIoSession session, Message<Login> message) throws ServiceException, SQLException {
		List<Login> loginList = message.getObjects();
		// loop and update each login record
		for (Login login : loginList) {
			// check if we have a different password
			if (login.getPassword() != null) {
				if (!authenticationService.updatePassword(login.getUsername(), login.getPassword()))
					throw new ServiceException("Failed to update the password for the login " + login);
			}
			// update the other fields
			if (!authenticationService.updateLogin(login))
				throw new ServiceException("Failed to update the login: " + login);
			// reset the password
			login.resetPassword();
			// update the lockable object
			lockableService.removeLock(login);
		}
		session.writeResponseBrodcast(message, loginList);
	}

	@Override
	public void execute(MessageIoSession session, Message<Login> message) throws ServiceException, SQLException {
		// get the params from the message
		Map<String, String> params = message.getParams();
		String command = params.get(AbstractMessage.ATTRIBUTE_COMMAND);

		// check what command was send
		if ("doLogin".equalsIgnoreCase(command)) {
			doLogin(session, message);
			return;
		}
		if ("doLogout".equalsIgnoreCase(command)) {
			doLogout(session, message);
			return;
		}
		// update the locks
		if ("doLock".equalsIgnoreCase(command)) {
			lockableService.addAllLocks(message.getObjects());
			return;
		}
		if ("doUnlock".equalsIgnoreCase(command)) {
			lockableService.removeAllLocks(message.getObjects());
			return;
		}

		// throw an execption because the 'exec' command is not implemented
		String handler = getClass().getSimpleName();
		throw new NoSuchCommandException(handler, command);
	}

	/**
	 * Helper method to execute a login
	 */
	private void doLogin(MessageIoSession session, Message<Login> message) throws ServiceException, SQLException {
		// try to get the login
		Login login = message.getObjects().get(0);
		if (login == null)
			throw new ServiceException("Cannot execute the login request without a login object.(login = null)");

		// check the password and the user
		String username = login.getUsername();
		String password = login.getPassword();
		boolean isWebClient = login.isWebClient();
		// check agains the database
		int loginResult = authenticationService.checkLogin(username, password, isWebClient);
		// for security reset the password
		login.resetPassword();
		if (loginResult == AuthenticationService.LOGIN_SUCCESSFULL) {
			// get the infos out of the database
			login = authenticationService.getLoginAndStaffmember(username);
			if (login == null)
				throw new ServiceException("Failed to get the login and staff details for the given login");
			// login was successfully
			login.setWebClient(isWebClient);
			login.setLoggedIn(true);
			// set the login to true
			session.setLoggedIn(login);
			logger.info("Authenticating session " + session + " -> " + login.getUsername());
		}
		else if (loginResult == AuthenticationService.LOGIN_FAILED) {
			login.setLoggedIn(false);
			login.setErrorMessage("Der Benutzername oder das Passwort ist falsch.");
		}
		else if (loginResult == AuthenticationService.LOGIN_DENIED) {
			login.setLoggedIn(false);
			login.setIslocked(true);
			login.setErrorMessage("Ihr Account ist gesperrt, bitte kontaktieren Sie den Administrator.");
		}
		else if (loginResult == AuthenticationService.LOGIN_NO_DISPONENT) {
			login.setLoggedIn(false);
			login.setErrorMessage("Ihr Account ist nicht freigeschaltet, bitte wenden Sie sich an den Leitstellenleiter.");
		}
		else {
			login.setLoggedIn(false);
			login.setErrorMessage("Unexpected error occured");
			throw new ServiceException("Failed to check the login for the username: " + username);
		}
		// send the login result back
		session.writeResponse(message, login);
	}

	/**
	 * Helper method to execute a logout
	 */
	private void doLogout(MessageIoSession session, Message<Login> message) throws ServiceException {
		// try to get the login
		Login login = message.getObjects().get(0);
		if (login == null)
			throw new ServiceException("Cannot execute the logout request without a login object.(login = null)");
		login.setLoggedIn(false);

		// write the message back and reset the stat of the session
		session.writeResponse(message, login);
		session.reinitialize();
	}

}
