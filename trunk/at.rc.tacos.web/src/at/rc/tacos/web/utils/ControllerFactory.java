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
		if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url._login")))
		{
			return new LoginController();
		} 
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url._logout")))
		{
			return new LogoutController();
		} 
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home")))
		{
			return new HomeController();	
		} 
		else if (url.equals(ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url._notFound")))
		{
			return new NotFoundController();	
		}
		else
		{
			return null;
		}
	}
}

