package at.rc.tacos.web.utils;

import java.util.ResourceBundle;

import at.rc.tacos.web.web.*;


/**
 * ControllerFactory creates a handler out of the request URL.
 * 
 * @author PayerM
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
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home")))
		{
			return new HomeController();	
		} 
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.notFound")))
		{
			return new NotFoundController();	
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterWeek")))
		{
			return new RosterWeekController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterDay")))
		{
			return new RosterDayController();
		}
		else
		{
			return null;
		}
	}
}

