package at.rc.tacos.web.web;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.web.utils.ControllerFactory;

/**
 * This class gets all the browser requests and maps them to request handlers.
 * @author Nechan
 * @version 1.0 
 */
public class Dispatcher extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	public static final String URLS_BUNDLE_PATH = "at.rc.tacos.web.web.urls";

	/**
	 * Initializes servlet context.
	 */
	public void init() throws ServletException
	{
		super.init();
	}

	/**
	 * Calls doPost.
	 */
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * Does Dispatching. Checks relative URL. Gets controller. Forwards to View.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// Assert we have a valid session.
		final HttpSession session = request.getSession(true);
		UserSession userSession = (UserSession) session.getAttribute("userSession");

		// Assert we have a valid user session.
		if (userSession == null)
		{
			userSession = new UserSession();
			request.getSession().setAttribute("userSession", userSession);
		}

		// Get the relative Path from request URI.	
		final String relativePath = request.getRequestURI().replaceAll(request.getContextPath(), "").replaceFirst("/Dispatcher/", "").replaceFirst("/Dispatcher", "");
		System.out.println("relativePath: " + relativePath);

		ResourceBundle urls = ResourceBundle.getBundle(URLS_BUNDLE_PATH);

		final Controller controller = ControllerFactory.getController(relativePath);

		// If no URL is specified send redirect to home.do.
		if (relativePath.equals("") || relativePath.equals("/"))
			response.sendRedirect(getServletContext().getContextPath()+ "/Dispatcher/" +  urls.getString("url.rosterDay"));
		// If no controller is found redirect to notFound.do.
		else if (controller == null){
			response.sendRedirect(getServletContext().getContextPath()+ "/Dispatcher/" + urls.getString("url.notFound"));
		// If user isn't logged in redirect to login.do.
		}else if (!userSession.getLoggedIn()
				&& !relativePath.equals(urls.getString("url.login"))
				&& !relativePath.equals(urls.getString("url.error"))
				&& !relativePath.equals(urls.getString("url.notFound")))
		{
			response.sendRedirect(getServletContext().getContextPath() + "/Dispatcher/" + urls.getString("url.login"));
		}
		else
		{
			try
			{
				final Map<String, Object> params = controller.handleRequest(request, response, this.getServletContext());
				request.setAttribute("params", params);
			} 
			catch (IllegalArgumentException e)
			{
				e.printStackTrace();
				response.sendRedirect(getServletContext().getContextPath() + "/Dispatcher/"+ urls.getString("url.error"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
				response.sendRedirect(getServletContext().getContextPath() + "/Dispatcher/" + urls.getString("url.error"));
			}
		}

		//Do not forward if response is not committed
		if (!response.isCommitted())			
		{
			getServletContext().getRequestDispatcher("/" + relativePath.replaceAll(".do", "View")).forward( request, response);
		}
	}
}
