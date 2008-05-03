package at.rc.tacos.web.utils;

import java.util.ResourceBundle;

import at.rc.tacos.web.controller.*;

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
		if (url.equals(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("login.url"))) {
			return new LoginController();
		} else if (url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("logout.url"))) {
			return new LogoutController();
		} else if (url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("notFound.url"))) {
			return new NotFoundController();	
		} else if (url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("dutiesDay.url"))) {
			return new AddRosterEntryController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("rosterDay.url"))) {
			return new OldRosterDayController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("rosterWeek.url"))) {
			return new OldRosterWeekController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("rosterEntry.url"))) {
			return new OldRosterEntryController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("stationWeek.url"))) {
			return new OldRosterWeekController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("editProfile.url"))) {
			return new OldEditProfileController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("addUser.url"))) {
			return new OldAddUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("editUser.url"))) {
			return new OldEditUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("lockUser.url"))) {
			return new OldLockUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("printRoster.url"))) {
			return new OldPrintController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("updateEntry.url"))) {
			return new OldUpdateEntryController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("listUser.url"))) {
			return new OldListUserController();
		} else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.VIEWS_BUNDLE_PATH).getString("statisticEmployee.url"))) {
			return new OldStatisticEmployeeController();
		} else {
			return null;
		}
	}
}