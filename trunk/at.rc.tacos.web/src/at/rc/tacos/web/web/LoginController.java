package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
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
        	System.out.println(username);
        	System.out.println(password);
            //the result
            List<AbstractMessage> result;
            WebClient client = WebClient.getInstance();
            //open a connection to the server
            client.connect("81.189.52.155", 4711);
            Login login = new Login(username,password);
            login.setErrorMessage("nix");
            result = client.sendRequest(username, Login.ID, IModelActions.LOGIN, login);
            //get the content
            if(Login.ID.equalsIgnoreCase(client.getContentType()))
            {
                Login loginResult = (Login)result.get(0);
                if(loginResult.isLoggedIn())
                {
                	UserSession userSession = (UserSession)session.getAttribute("userSession");
                	userSession.setLoggedIn(true, "user", client);
                	response.sendRedirect(context.getContextPath() + "/Dispatcher/" + ResourceBundle.getBundle(Dispatcher.URLS_BUNDLE_PATH).getString("url.home")); 
                }
                else
                {
                	params.put("loginError", loginResult.getErrorMessage());
                }
            }
        }
        if("logout".equalsIgnoreCase(action))
        {
        	session.invalidate();
		    params.put("logout-success", "You have been logged out successfully!");
		    System.out.println("logut");
        }
        return params;
    }   	  	    
}