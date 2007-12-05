package at.rc.tacos.web.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import at.rc.tacos.codec.LoginDecoder;
import at.rc.tacos.codec.LoginEncoder;
import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IModelActions;
import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.factory.ProtocolCodecFactory;
import at.rc.tacos.factory.XMLFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.RosterEntry;

/**
 * Servlet implementation class for Servlet: LoginController
 */
public class LoginController extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet, Controller 
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
            XMLFactory factory = new XMLFactory();
            ProtocolCodecFactory.getDefault().registerEncoder(Login.ID, new LoginEncoder());
            ProtocolCodecFactory.getDefault().registerDecoder(Login.ID, new LoginDecoder());
            factory.setupEncodeFactory("user",RosterEntry.ID,IModelActions.ADD);
            
            Login login = new Login("user1","P@ssw0rd");
            login.setErrorMessage("test");
            
            ArrayList<AbstractMessage> list = new ArrayList<AbstractMessage>();
            list.add(login);
            String xml = factory.encode(list);
            System.out.println(xml);
            //send the login request
            String resultXML = WebClient.getInstance().queryServer(xml);
            factory.setupDecodeFactory(resultXML);
            ArrayList<AbstractMessage> result = factory.decode();
            Login loginResponse = (Login)result.get(0);
            System.out.println(loginResponse);
            
            request.setAttribute("result", result);
            request.getRequestDispatcher("jsp/login.jsp").forward(request, response);
        }
    }

	@Override
	public Map<String, Object> handleRequest(HttpServletRequest request,
			HttpServletResponse response, ServletContext context)
			throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}   	  	    
}