package at.rc.tacos.web.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.core.net.internal.WebClient;
import at.rc.tacos.web.utils.ControllerFactory;

public class Dispatcher extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	public static final String VIEWS_BUNDLE_PATH = "at.rc.tacos.web.web.views";
	private static final String SERVER_BUNDLE_PATH = "at.rc.tacos.web.web.server";
	public static final String NET_BUNDLE_PATH = "at.rc.tacos.web.net.net";

	/**
	 * Initializes servlet context.
	 */
	@Override
	public void init() throws ServletException
	{
		super.init();
		if (getServletContext().getAttribute("client") == null) {
			getServletContext().setAttribute("client", new WebClient());
		}
	}

	/**
	 * Calls doPost.
	 */
	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * Does Dispatching. Checks relative URL. Gets controller. Forwards to View.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		final ResourceBundle views = ResourceBundle.getBundle(VIEWS_BUNDLE_PATH);
		final ResourceBundle server = ResourceBundle.getBundle(SERVER_BUNDLE_PATH);
		
		//Assert we have a valid session
		final HttpSession session = request.getSession(true);
		UserSession userSession = (UserSession) session.getAttribute("userSession");

		//Assert we have a valid user session
		if (userSession == null) {
			userSession = new UserSession();
			session.setAttribute("userSession", userSession);
		}

		//Get the relative Path from request URL
		final String relativePath = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath(), "");

		//Find out if login is required for request URL
		boolean loginRequired = true;
		final Set<String> set1 = views.keySet();
		for (final Iterator<String> it = set1.iterator(); it.hasNext();) {
			String key = it.next();
			if (key.contains(".loginRequired")) {
				String value = views.getString(key);
				if (value.equalsIgnoreCase("false")) {
					key = key.replaceFirst("loginRequired", "url");
					if (views.getString(key).equalsIgnoreCase(relativePath)) {
						loginRequired = false;
					}
				}
			}
		}
		
		//Find out if template is required for request URL
		boolean usesTemplate = false;
		String template = "";
		final Set<String> set2 = views.keySet();
		for (final Iterator<String> it = set2.iterator(); it.hasNext();) {
			String key1 = it.next();
			if (key1.contains(".template")) {
				String key2 = key1.replaceFirst("template", "url");
				if (views.getString(key2).equalsIgnoreCase(relativePath)) {
					usesTemplate = true;
					template = views.getString(key1);
				}		
			}
		}
		
		final Controller controller = ControllerFactory.getController(relativePath);
		
		//Redirect if request is not send over SSL connection
		if (request.getServerPort() == Integer.parseInt(server.getString("server.default.port"))) {
			response.sendRedirect(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + relativePath);
		}
		//If no URL is specified send redirect to home.do.
		else if (relativePath.equals("") || relativePath.equals("/")) response.sendRedirect(getServletContext().getContextPath()+ request.getServletPath() + views.getString("home.url"));
		//If no controller is found redirect to notFound.do.
		else if (controller == null) {
			response.sendRedirect(getServletContext().getContextPath()+ request.getServletPath() + views.getString("notFound.url")); 
		//If user isn't logged in and login is required for specified URL redirect to login.do.
		} else if (!userSession.getLoggedIn() && loginRequired) {
			response.sendRedirect(getServletContext().getContextPath() + request.getServletPath() + views.getString("login.url") + "?url=" + relativePath);
		}
		else
		{
			try {
				final Map<String, Object> params = controller.handleRequest(request, response, this.getServletContext());
				request.setAttribute("params", params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				response.sendRedirect(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url"));
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url"));
			}
		}

		//Do not forward if response is committed
		if (!response.isCommitted()) {
			//Differentiate if View uses model.jsp or not
			if (usesTemplate) {
				getServletContext().getRequestDispatcher(response.encodeURL(template + "?view=" + relativePath)).forward(request, response);
			} else {
				getServletContext().getRequestDispatcher("/WEB-INF/jsp" + relativePath.replaceAll(".do", ".jsp")).forward(request, response);
			}
		}
	}
}
