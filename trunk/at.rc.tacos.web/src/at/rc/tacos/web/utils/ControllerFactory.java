package at.rc.tacos.web.utils;

import java.util.ResourceBundle;

import at.rc.tacos.web.web.*;

/**
 * ControllerFactory creates a handler out of the request URL.
 * 
 * @author Nechan
 * @version 1.0
 */
public class ControllerFactory {

	/**
	 * Get Controller for URL.
	 * @param url
	 * @return
	 */
	public static Controller getController(String url)
	{
		if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("login.url"))) {
			return new LoginController();
		} else if (url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("logout.url"))) {
			return new LogoutController();
		} else if (url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("notFound.url"))) {
			return new NotFoundController();	
		} else if (url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("duties.day.url"))) {
			return new DutiesDayController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("rosterDay.url"))) {
			return new RosterDayController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("rosterWeek.url"))) {
			return new RosterWeekController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("rosterEntry.url"))) {
			return new RosterEntryController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("stationWeek.url"))) {
			return new RosterWeekController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("editProfile.url"))) {
			return new EditProfileController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("addUser.url"))) {
			return new AddUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("editUser.url"))) {
			return new EditUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("lockUser.url"))) {
			return new LockUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("printRoster.url"))) {
			return new PrintController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("updateEntry.url"))) {
			return new UpdateEntryController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("listUser.url"))) {
			return new ListUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("statisticEmployee.url"))) {
			return new StatisticEmployeeController();
		} else {
			return null;
		}
	}
}