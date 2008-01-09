package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.model.Login;

/**
 * Servlet implementation class for Servlet: LoginController
 */
public class LoginController implements Controller 
{
	public Map<String, Object> handleRequest(HttpServletRequest request,HttpServletResponse response, ServletContext context) throws Exception
	{
		//values that will be returned to the view
		Map<String, Object> params = new HashMap<String, Object>();
		//the action to do
		String action = request.getParameter("action");

		HttpSession session = request.getSession();

		if("login".equalsIgnoreCase(action))
		{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			
			if(username.trim().isEmpty() || password.trim().isEmpty())
			{ 
				params.put("loginError", "Bitte geben Sie ihren Usernamen und Passwort ein!");
				return params;
			} 
			
			
			//the result
			WebClient client = new WebClient();
			//open a connection to the server
			client.connect("81.189.52.155", 4711);
			//client.connect("localhost", 4711);
			AbstractMessage result = client.sendLoginRequest(username, password);
			//get the content
			if(Login.ID.equalsIgnoreCase(client.getContentType()))
			{
				Login loginResult = (Login)result;
				if(loginResult.isLoggedIn())
				{
					UserSession userSession = (UserSession)session.getAttribute("userSession");
					userSession.setLoggedIn(true, username, client);
					//userSession.setLoggedIn(true, loginResult.getUsername(), client);
					response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.rosterDay")); 
				}
				
				else
				{
					params.put("loginError", "Sie haben einen falschen Usernamen oder ein falsches Passwort eingegeben. Korrigieren Sie bitte ihre Eingaben!");
				}
				
			}
		}
		if("logout".equalsIgnoreCase(action))
		{
			session.invalidate();
			params.put("logout-success", "Sie wurden erfolgreich ausgeloggt!");
		}
		return params;
	}   	  	    
}