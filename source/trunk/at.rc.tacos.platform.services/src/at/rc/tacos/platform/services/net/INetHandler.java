package at.rc.tacos.platform.services.net;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import at.rc.tacos.platform.services.exception.ServiceException;

/**
 * Defines the methods that the concret handler classes must implement. We use
 * genericcs here in order to get type save handler classes.
 * 
 * @author Michael
 */
public interface INetHandler<M> {

	/**
	 * Notification that a new object has been added
	 * 
	 * @param model
	 *            the object to add
	 * @reuturn the object that was added
	 */
	public M add(M m) throws ServiceException, SQLException;

	/**
	 * Notification that a object has been removed
	 * 
	 * @param model
	 *            the object that was removed
	 * @return the object that was removed
	 */
	public M remove(M m) throws ServiceException, SQLException;

	/**
	 * Notification that a object has been updated
	 * 
	 * @param model
	 *            the object that has been updated
	 * @return the updated object
	 */
	public M update(M m) throws ServiceException, SQLException;

	/**
	 * Notification that a request for one ore more object was made. The
	 * optional parameters give some hints which objects schould be returned.
	 * 
	 * @param params
	 *            the request parameters as simple map
	 * @return the list of requested objects
	 */
	public List<M> get(Map<String, String> params) throws ServiceException, SQLException;

	/**
	 * Generalizes method to execute a custom command with optinal parameters
	 * can give some hints how the command should be executed.
	 * 
	 * @param command
	 *            the command that should be executed
	 * @param modeList
	 *            the list of objects send with this request
	 * @param params
	 *            the request parameters as simple map of strings
	 * @return the result of the command operation
	 */
	public List<M> execute(String command, List<M> list, Map<String, String> params) throws ServiceException, SQLException;

}
