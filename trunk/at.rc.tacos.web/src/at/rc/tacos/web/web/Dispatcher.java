package at.rc.tacos.web.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.web.utils.ControllerFactory;

/**
 * @author PayerM
 * @version 1.0 This class gets all the browser requests and maps them to
 *          request handlers.
 *          -added XML Support for Dispatcher (write out directly to client browser)
 */
public class Dispatcher extends HttpServlet {

	private static final long serialVersionUID = 1L;
	public static final String URLS_BUNDLE_PATH = "tacosdp.web.urls";
	public static final String ACTIONS_BUNDLE_PATH = "tacosdp.web.actions";
	public static final String MESSAGES_BUNDLE_PATH = "tacosdp.messages.messages";

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
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * Does Dispatching. Checks relative URL. Gets controller. Forwards to View.
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		// Assert we have a valid session.
		final HttpSession session = request.getSession(true);

		UserSession userSession = (UserSession) session
				.getAttribute("userSession");

		// Assert we have a valid user session.
		if (userSession == null)
		{
			userSession = new UserSession();
			request.getSession().setAttribute("userSession", userSession);
		}

		// Get the relative Path from request URI.	
		final String relativePath = request.getRequestURI().replaceAll(request.getContextPath(), "").replaceFirst("/Dispatcher/", "").replaceFirst("/Dispatcher", "");
		System.out.println("relativePath: " + relativePath);

		//relativePathStrings needed for XML generating
		final String strUserExport = ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString("url.user._export");
		final String strCategoryExport = ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString("url.category._export");
		final String strBookingExport = ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString("url.booking._export");
		
		final Controller controller = ControllerFactory
				.getController(relativePath);
				
		// If no URL is specified send redirect to home.do.
		if (relativePath.equals("") || relativePath.equals("/"))
		{
				
			response.sendRedirect(getServletContext().getContextPath()
					+ "/Dispatcher/"
					+ ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString(
							"url.home"));
		}

		// If no controller is found redirect to notFound.do.
		else if (controller == null)
		{
			response.sendRedirect(getServletContext().getContextPath()
					+ "/Dispatcher/"
					+ ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString(
							"url._notFound"));
		}

		// If user isn't logged in redirect to login.do.
		else if (!userSession.getLoggedIn()
				&& !relativePath.equals(ResourceBundle.getBundle(
						URLS_BUNDLE_PATH).getString("url._login"))
				&& !relativePath.equals(ResourceBundle.getBundle(
						URLS_BUNDLE_PATH).getString("url._error"))
				&& !relativePath.equals(ResourceBundle.getBundle(
						URLS_BUNDLE_PATH).getString("url._notFound"))
				&& !relativePath.equals(ResourceBundle.getBundle(
						URLS_BUNDLE_PATH).getString("url.user.registration")))
		{
			response.sendRedirect(getServletContext().getContextPath()
					+ "/Dispatcher/"
					+ ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString(
							"url._login"));
		}
		
		/**
		 * Next three diversions:
		 * -if the relativePath equals with the specified view, generate XML
		 * -Export of userList, categoryList, bookingList
		 */
		//generating XML - userExport.do
		else if(relativePath.equals(strUserExport))
		{
			try
			{	
				//Set the content type for XML
				response.setContentType("application/xml");
				
				//Get the map value (XML - String)
				Map<String, Object> usersxml = controller.handleRequest(
						request, response, this.getServletContext());
			
				//Convert to string
				String usersxmlkey = (String)usersxml.get("usersxmlkey");
				System.out.println("usersxmlkey:" + usersxmlkey);
				
				//Write out userList directly to client browser - XML
				PrintWriter out =  response.getWriter();				
				out.write(usersxmlkey);
				out.flush();
				
			
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//generating XML - categoryExport.do
		else if(relativePath.equals(strCategoryExport))
		{
			try
			{	
				//Set the content type for XML
				response.setContentType("application/xml");
				
				//Get the map value (XML - String)
				Map<String, Object> categoriesxml = controller.handleRequest(
						request, response, this.getServletContext());
				
				//Convert to String
				String categoriesxmlkey = (String)categoriesxml.get("categoriesxmlkey");
				System.out.println("categoriesxmlkey:" + categoriesxmlkey);
				
				//Write out categoryList directly to client browser - XML
				PrintWriter out =  response.getWriter();				
				out.write(categoriesxmlkey);
				out.flush();			
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//generating XML - bookingExport.do
		else if(relativePath.equals(strBookingExport))
		{
			try
			{	
				//Set the content type for XML
				response.setContentType("application/xml");
				
				//Get the map value (XML - String)
				Map<String, Object> bookingsxml = controller.handleRequest(
						request, response, this.getServletContext());
				
				//Convert to String
				String bookingsxmlkey = (String)bookingsxml.get("bookingsxmlkey");
				System.out.println("bookingsxmlkey:" + bookingsxmlkey);
				
				//Write out bookingList directly to client browser - XML
				PrintWriter out =  response.getWriter();				
				out.write(bookingsxmlkey);
				out.flush();			
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		else
		{
			try
			{
				final Map<String, Object> params = controller.handleRequest(
						request, response, this.getServletContext());
				request.setAttribute("params", params);
			} catch (IllegalArgumentException e)
			{
				response.sendRedirect(getServletContext().getContextPath()
						+ "/Dispatcher/"
						+ ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString(
								"url._error") + "?action=" + ResourceBundle.getBundle(ACTIONS_BUNDLE_PATH).getString("action._error.validationError"));
			} catch (Exception e)
			{
				response.sendRedirect(getServletContext().getContextPath()
						+ "/Dispatcher/"
						+ ResourceBundle.getBundle(URLS_BUNDLE_PATH).getString(
								"url._error") + "?action=" + ResourceBundle.getBundle(ACTIONS_BUNDLE_PATH).getString("action._error.internalServerError"));
			}
		}
		
		//Do not forward, if XML should be generated or if response is not committed
		if (!response.isCommitted()
				&& !relativePath.equals(strUserExport) 
				&& !relativePath.equals(strCategoryExport)
				&& !relativePath.equals(strBookingExport))			
		{
			getServletContext().getRequestDispatcher(
					"/" + relativePath.replaceAll(".do", "View")).forward(
					request, response);
		}
	}
}
