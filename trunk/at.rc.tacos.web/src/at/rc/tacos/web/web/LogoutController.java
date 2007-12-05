package at.rc.tacos.web.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author crusty2000
 * @version 1.1
 */
public class LogoutController implements Controller 
{
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context) throws IOException
	{
	    //values that will be returned to the view
	    Map<String, Object> params = new HashMap<String, Object>();
		HttpSession session = request.getSession();
		
		UserSession userSession = (UserSession) session.getAttribute("userSession");
		//is the user logged in?
		if(userSession.getLoggedIn())
		{
		    session.invalidate();
		    params.put("logout-success", "You have been logged out successfully!");
		    System.out.println("logut");
		}
		return params;
	}
}
