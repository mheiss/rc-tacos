package at.rc.tacos.server.net.handler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.services.Service;
import at.rc.tacos.platform.services.dbal.AuthenticationService;
import at.rc.tacos.platform.services.exception.NoSuchCommandException;
import at.rc.tacos.platform.services.exception.ServiceException;
import at.rc.tacos.platform.services.net.INetHandler;

public class AuthenticationHandler implements INetHandler<Login> {

	@Service(clazz = AuthenticationService.class)
	private AuthenticationService authenticationService;

	@Override
	public Login add(Login model) throws ServiceException, SQLException {
		if (!authenticationService.addLogin(model)) {
			throw new ServiceException("Failed to add the login " + model + " to the database");
		}
		return model;
	}

	@Override
	public List<Login> get(Map<String, String> params) throws ServiceException, SQLException {
		List<Login> loginList = null;
		if (params.containsKey(IFilterTypes.USERNAME_FILTER)) {
			loginList = authenticationService.listLoginsAndStaffMemberByUsername(params.get(IFilterTypes.USERNAME_FILTER));
		}
		else {
			loginList = authenticationService.listLogins();
		}
		return loginList;
	}

	@Override
	public Login remove(Login model) throws ServiceException, SQLException {
		// the user will only be locked, removing is not possible
		if (!authenticationService.lockLogin(model.getUsername()))
			throw new ServiceException("Failed to lock the login: " + model);
		return model;
	}

	@Override
	public Login update(Login model) throws ServiceException, SQLException {
		// check if we have a different password
		if (model.getPassword() != null) {
			if (!authenticationService.updatePassword(model.getUsername(), model.getPassword()))
				throw new ServiceException("Failed to update the password for the login " + model);
		}
		// update the other fields
		if (!authenticationService.updateLogin(model))
			throw new ServiceException("Failed to update the login: " + model);
		// reset the password
		model.resetPassword();
		return model;
	}

	@Override
	public List<Login> execute(String command, List<Login> modelList, Map<String, String> params) throws ServiceException, SQLException {
		// check what command was send
		if ("login".equalsIgnoreCase(command)) {
			return doLogin(modelList.get(0));
		}
		else if ("logout".equalsIgnoreCase(command)) {
			return doLogout(modelList.get(0));
		}

		// unknown command so throw a exception
		throw new NoSuchCommandException(command);
	}

	/**
	 * Helper method to execute a login
	 */
	private List<Login> doLogin(Login login) throws ServiceException, SQLException {
		List<Login> loginResponse = new ArrayList<Login>();
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
		}
		else if (loginResult == AuthenticationService.LOGIN_FAILED) {
			login.setLoggedIn(false);
			login.setErrorMessage("Wrong username or password");
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
		// add the result to the response
		loginResponse.add(login);
		return loginResponse;
	}

	/**
	 * Helper method to execute a logout
	 */
	private List<Login> doLogout(Login login) {
		List<Login> logoutResponse = new ArrayList<Login>();
		login.setLoggedIn(false);
		logoutResponse.add(login);
		return logoutResponse;
	}

}
