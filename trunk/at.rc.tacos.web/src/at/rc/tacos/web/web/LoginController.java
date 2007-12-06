package at.rc.tacos.web.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

        if("login".equalsIgnoreCase(action))
        {
            //the result
            List<AbstractMessage> result;
            WebClient client = WebClient.getInstance();
            //open a connection to the server
            client.connect("localhost", 4711);
            Login login = new Login("user1","P@ssw0rd");
            result = client.sendRequest("user1", Login.ID, IModelActions.LOGIN, login);
            //get the content
            if(Login.ID == client.getContentType())
            {
                Login loginResult = (Login)result.get(0);
                System.out.println("Login result: "+loginResult.isLoggedIn());
                params.put("loginResult", loginResult.isLoggedIn());
            }
        }
        
        return params;
    }   	  	    
}