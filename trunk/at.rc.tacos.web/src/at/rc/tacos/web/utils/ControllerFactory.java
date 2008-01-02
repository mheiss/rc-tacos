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
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home")))
		{
			return new HomeController();	
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
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.station")))
		{
			return new StationController();
		}
		else if(url.equalsIgnoreCase(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.stats")))
		{
			return new StatsController();
		}
		else
		{
			return null;
		}
	}
}