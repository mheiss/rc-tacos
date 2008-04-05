package at.rc.tacos.web.web;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
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
	public static final String URLS_BUNDLE_PATH = "at.rc.tacos.web.web.urls";
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
		final ResourceBundle urls = ResourceBundle.getBundle(URLS_BUNDLE_PATH);
		final ResourceBundle server = ResourceBundle.getBundle(SERVER_BUNDLE_PATH);
		
		//Assert we have a valid session.
		final HttpSession session = request.getSession(true);
		UserSession userSession = (UserSession) session.getAttribute("userSession");

		//Assert we have a valid user session.
		if (userSession == null) {
			userSession = new UserSession();
			request.getSession().setAttribute("userSession", userSession);
		}

		//Get the relative Path from request URI.	
		final String relativePath = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath(), "");
		//System.out.println("relativePath: " + relativePath);

		final Controller controller = ControllerFactory.getController(relativePath);

		//Redirect if request is not send over SSL connection
		if (request.getServerPort() == Integer.parseInt(server.getString("server.default.port"))) {
			response.sendRedirect(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath());
		}
		//If no URL is specified send redirect to home.do.
		else if (relativePath.equals("") || relativePath.equals("/")) response.sendRedirect(getServletContext().getContextPath()+ request.getServletPath() + urls.getString("home.url"));
		//If no controller is found redirect to notFound.do.
		else if (controller == null) {
			response.sendRedirect(getServletContext().getContextPath()+ request.getServletPath() + urls.getString("notFound.url")); 
		//If user isn't logged in redirect to login.do.
		} else if (!userSession.getLoggedIn() && !relativePath.equals(urls.getString("login.url")) && !relativePath.equals(urls.getString("error.url")) && !relativePath.equals(urls.getString("notFound.url"))) {
			response.sendRedirect(getServletContext().getContextPath() + request.getServletPath() + urls.getString("login.url") + "?url=" + relativePath);
		}
		else
		{
			try {
				final Map<String, Object> params = controller.handleRequest(request, response, this.getServletContext());
				request.setAttribute("params", params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				response.sendRedirect(getServletContext().getContextPath() + request.getServletPath() + urls.getString("error.url"));
			} catch (Exception e) {
				e.printStackTrace();
				response.sendRedirect(getServletContext().getContextPath() + request.getServletPath() + urls.getString("error.url"));
			}
		}

		//Do not forward if response is committed
		if (!response.isCommitted()) {
			if (relativePath.equals(urls.getString("login.url")) || relativePath.equals(urls.getString("error.url")) || relativePath.equals(urls.getString("notFound.url"))) {
				getServletContext().getRequestDispatcher("/WEB-INF/jsp" + relativePath.replaceAll(".do", ".jsp")).forward(request, response);
			} else getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/model.jsp?view=" + relativePath)).forward(request, response);
		}
	}
}
