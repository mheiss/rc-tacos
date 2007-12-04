package at.rc.tacos.web.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import at.rc.tacos.core.net.internal.WebClient;

/**
 * Servlet implementation class for Servlet: LoginController
 */
public class LoginController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet 
{
    static final long serialVersionUID = 1L;

    /**
     * Default class constructor
     */
    public LoginController() 
    {	
        super();
    }   	

    /**
     * Hanldes get requests and passes them to the post method
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        doPost(request,response);
    }  	

    /**
     * Handles post requests
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        String action = request.getParameter("action");
        
        if("login".equalsIgnoreCase(action))
        {
            //open a connection to the server
            WebClient.getInstance().connect("localhost", 4711);
            //send the login request
            String result = WebClient.getInstance().queryServer("hallo");
            request.setAttribute("result", result);
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        }
    }   	  	    
}