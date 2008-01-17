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
		if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.login")))
		{
			return new LoginController();
		} 
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.notFound")))
		{
			return new NotFoundController();	
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterDay")))
		{
			return new RosterDayController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterWeek")))
		{
			return new RosterWeekController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterEntry")))
		{
			return new RosterController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.stationWeek")))
		{
			return new StationController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.userProfile")))
		{
			return new ProfileController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.addUser")))
		{
			return new AddUserController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.editProfile")))
		{
			return new EditProfileController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.deleteProfile")))
		{
			return new DeleteProfileController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.printRoster")))
		{
			return new PrintController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.deleteEntryPopUp")))
		{
			return new PopUpController();
		}
		else
		{
			return null;
		}
	}
}