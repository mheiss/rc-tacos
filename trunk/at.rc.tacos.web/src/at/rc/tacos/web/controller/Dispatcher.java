package at.rc.tacos.web.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.rc.tacos.common.AbstractMessage;
import at.rc.tacos.common.IFilterTypes;
import at.rc.tacos.model.DayInfoMessage;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.QueryFilter;
import at.rc.tacos.web.session.DefaultFormValues;
import at.rc.tacos.web.session.UserSession;

/**
 * Dispatcher (Front Controller):
 * This class is responsible for URL resolving, security, forwarding to view and loads template.
 * @author Payer Martin
 * @version 1.0
 * TODO: Refresh fixen
 */
public class Dispatcher extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	public static final String VIEWS_BUNDLE_PATH = "at.rc.tacos.web.config.urls";
	public static final String SERVER_BUNDLE_PATH = "at.rc.tacos.web.config.server";
	public static final String NET_BUNDLE_PATH = "at.rc.tacos.web.config.net";
	public static final String FILEUPLOAD_BUNDLE_PATH = "at.rc.tacos.web.config.fileupload";

	public void init() throws ServletException {
		
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
		final ResourceBundle netBundle = ResourceBundle.getBundle(Dispatcher.NET_BUNDLE_PATH);
		
		//Set charakter encoding
		request.setCharacterEncoding("ISO-8859-1");
		
		String localAddr = request.getLocalAddr();
		String localName = request.getLocalName();
		int localPort = request.getLocalPort();
		String remoteAdr = request.getRemoteAddr();
		String remoteHost = request.getRemoteHost();
		String remoteUser = request.getRemoteUser();
		int remotePort = request.getRemotePort();
		String serverName = request.getServerName();
		int serverPort = request.getServerPort();
		String servletPath = request.getServletPath();
		String requestUrl = request.getRequestURL().toString();
		String requestUri = request.getRequestURI();
		String requestContextPath = request.getContextPath();
		String pathInfo = request.getPathInfo();
		String pathTranslated = request.getPathTranslated();	
		String servletContextPath = getServletContext().getContextPath();
		String servletContextContextName = getServletContext().getServletContextName();
		String servletContextServerInfo = getServletContext().getServerInfo();
		
		System.out.println("localAddr: " + localAddr + "\n");
		System.out.println("localName: " + localName + "\n");
		System.out.println("localPort: " + localPort + "\n");
		System.out.println("remoteAdr: " + remoteAdr + "\n");
		System.out.println("remoteHost: " + remoteHost + "\n");
		System.out.println("remoteUser: " + remoteUser + "\n");
		System.out.println("remotePort: " + remotePort + "\n");
		System.out.println("serverName: " + serverName + "\n"); 
		System.out.println("serverPort: " + serverPort + "\n");
		System.out.println("servletPath: " + servletPath + "\n");
		System.out.println("requestUrl: " + requestUrl + "\n");
		System.out.println("requestUri: " + requestUri + "\n");
		System.out.println("requestContextPath: " + requestContextPath + "\n");
		System.out.println("pathInfo: " + pathInfo + "\n");
		System.out.println("pathTranslated: " + pathTranslated + "\n");		
		System.out.println("servletContextPath: " + servletContextPath + "\n");
		System.out.println("servletContextContextName: " + servletContextContextName + "\n");
		System.out.println("servletContextServerInfo: " + servletContextServerInfo + "\n");
		
		//Assert we have a valid session
		final HttpSession session = request.getSession(true);
		UserSession userSession = (UserSession) session.getAttribute("userSession");

		//Assert that there is valid user session
		if (userSession == null) {
			userSession = new UserSession();
			final DefaultFormValues defaultFormValues = new DefaultFormValues();
			userSession.setDefaultFormValues(defaultFormValues);
			session.setAttribute("userSession", userSession);
		}
		
		//Assert that there is a valid connection to server
		if (userSession.getConnection().getSocket() == null) {
			boolean serverListening = false;
			serverListening = userSession.getConnection().connect(netBundle.getString("server.host"), Integer.parseInt(netBundle.getString("server.port")));
			if (!serverListening) {
				serverListening = userSession.getConnection().connect(netBundle.getString("failover.host"), Integer.parseInt(netBundle.getString("failover.port")));
			}
			if (!serverListening) throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
			
		}
		//Check if the request came from an internal IP
		if (Pattern.matches(server.getString("server.reverseProxy.address.pattern"), request.getRemoteAddr())) {
			userSession.setInternalSession(false);
		} else if (Pattern.matches(server.getString("server.network.address.pattern"), request.getRemoteAddr())) {
			userSession.setInternalSession(true);
		} else {
			userSession.setInternalSession(false);
		}
		//request.setAttribute("userSession", userSession);
		System.out.println("internal: " + userSession.isInternalSession() + "\n");
				
		//Do some actions if user is logged in
		if (userSession.getLoggedIn()) {
			// Get current login data
			final QueryFilter loginUsernameF = new QueryFilter();
			loginUsernameF.add(IFilterTypes.USERNAME_FILTER, userSession.getLoginInformation().getUsername());
			final List<AbstractMessage> loginList = userSession.getConnection().sendListingRequest(Login.ID, loginUsernameF);
			if (!Login.ID.equalsIgnoreCase(userSession.getConnection().getContentType())) {
				throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
			}
			final Login login = (Login)loginList.get(0);
			userSession.setLoginInformation(login);	
			
		}
		
		//Get the relative Path from request URL
		final String relativePath = request.getRequestURI().replace(request.getContextPath(), "").replace(request.getServletPath(), "");
		final String relativePathPrefix = relativePath.replaceFirst("/", "").replaceFirst("\\.do", "");		
		
		System.out.println("relativePath: " + relativePath + "\n");
		System.out.println("relativePathPrefix: " + relativePathPrefix + "\n");
		
		final Set<String> set = views.keySet();
		boolean urlFound = false;
		boolean loginRequired = true;
		boolean controllerFound = false;
		String controllerClassName = null;
		boolean viewFound = false;
		String viewPath = null;
		boolean templateFound = false;
		String templatePath = null;
		boolean useCache = true;
		int refresh = -1;
		String viewTitle = null;
		String viewHeaderTitle = null;
		String js = null;
		String css = null;
		
		//Redirect if request is not send over SSL connection
		if (request.getServerPort() == Integer.parseInt(server.getString("server.default.port"))) {
			final Enumeration<Object> e = request.getParameterNames();
			String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + relativePath;
			int i = 0;
			while (e.hasMoreElements()) {
				final String parameterName = (String)e.nextElement();
				final String parameterValue = request.getParameter(parameterName);
				if (i == 0) {
					url += "?" + parameterName + "=" + parameterValue;
				} else {
					url += "&" + parameterName + "=" + parameterValue;
				}
				i++;
			}
			System.out.println("Redirect: " + response.encodeRedirectURL(url));
			System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
			response.sendRedirect(response.encodeRedirectURL(url));
			
			/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
			getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/

		}
		//If no URL is specified send redirect to home.do.
		else if (relativePath.equals("") || relativePath.equals("/")) {

			System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("personnelData.url")));
			System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
			response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("personnelData.url")));
			
			/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("addRosterEntry.url"));
			getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
		} else {
			//Get login requirement view and template
			for (final Iterator<String> it = set.iterator(); it.hasNext();) {
				final String key = it.next();
				final String prefix = key.replaceAll("\\..*", "");
				if (prefix.equals(relativePathPrefix) && key.contains(".url")) {
					urlFound = true;
				} else if (prefix.equals(relativePathPrefix) && key.contains(".loginRequired")) {
					if (views.getString(key).equalsIgnoreCase("false")) {
						loginRequired = false;
					}
				} else if (prefix.equals(relativePathPrefix) && key.contains(".controller")) {
					controllerFound = true;
					controllerClassName = views.getString(key);
				} else if (prefix.equals(relativePathPrefix) && key.contains(".view")) {
					viewFound = true;
					viewPath = views.getString(key).trim();
				} else if (prefix.equals(relativePathPrefix) && key.contains(".template")) {
					templateFound = true;
					templatePath = views.getString(key).trim();
				} else if (prefix.equals(relativePathPrefix) && key.contains(".useCache")) {
					if (views.getString(key).equalsIgnoreCase("false")) {
						useCache = false;
					}
				} else if (prefix.equals(relativePathPrefix) && key.contains(".refresh")) {
					refresh = Integer.parseInt(views.getString(key).trim());	
				} else if (prefix.equals(relativePathPrefix) && key.contains(".title")) {
					viewTitle = views.getString(key);
				} else if (prefix.equals(relativePathPrefix) && key.contains(".htitle")) {
					viewHeaderTitle = views.getString(key);
				} else if (prefix.equals(relativePathPrefix) && key.contains(".js")) {
					js = views.getString(key).trim();
				} else if (prefix.equals(relativePathPrefix) && key.contains(".css")) {
					css = views.getString(key).trim();
				}
			}			
			if (urlFound == false || controllerFound == false) {
				System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("notFound.url")));
				System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
				response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath()+ request.getServletPath() + views.getString("notFound.url")));
				
				/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("notFound.url")));
				getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
			} else {
				if (loginRequired == true && !userSession.getLoggedIn()) {
					final Enumeration<Object> e = request.getParameterNames();
					String url = server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("login.url") + "?savedUrl=" + relativePath;
					while (e.hasMoreElements()) {
						final String parameterName = (String)e.nextElement();
						final String parameterValue = request.getParameter(parameterName);
						url += "&" + parameterName + "=" + parameterValue;
					}
					System.out.println("Redirect: " + response.encodeRedirectURL(url));
					System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
					response.sendRedirect(response.encodeRedirectURL(url));
					
					/*request.setAttribute("redirectUrl", response.encodeRedirectURL(url));
					getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
				} else {
					try {
						
						final Controller controller = (Controller)Class.forName(controllerClassName).newInstance();
						final Map<String, Object> params = controller.handleRequest(request, response, this.getServletContext());
						
						//Forward to view if response is not commited and view is found in views.properties
						if (!response.isCommitted() && viewFound) {
							request.setAttribute("params", params);
							
							//Prevent proxy and browser caching
							request.setAttribute("useCache", useCache);
							if (!useCache) { 
								response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
								response.setHeader("Pragma","no-cache"); //HTTP 1.0
								response.setDateHeader ("Expires", -1);
							}
							
							//Add refresh value
							request.setAttribute("refresh", refresh);
							
							if (userSession.getLoggedIn()) {						
								//Get message of the day and write to request context
								final SimpleDateFormat formatDateForServer = new SimpleDateFormat("dd-MM-yyyy");
								Date messageOfTheDayDate = userSession.getDefaultFormValues().getDefaultDate();
								if (messageOfTheDayDate == null) {
									messageOfTheDayDate = new Date();
								}
								final QueryFilter dateFilter = new QueryFilter();
								dateFilter.add(IFilterTypes.DATE_FILTER, formatDateForServer.format(messageOfTheDayDate));
								final List<AbstractMessage> messageOfTheDayList = userSession.getConnection().sendListingRequest(DayInfoMessage.ID, dateFilter);
								if (!DayInfoMessage.ID.equalsIgnoreCase(userSession.getConnection().getContentType())) {
									throw new IllegalArgumentException("Error: Error at connection to Tacos server occoured.");
								}
								DayInfoMessage message = null;
								if (messageOfTheDayList != null) {
									if (messageOfTheDayList.size() > 0) {
										message = (DayInfoMessage)messageOfTheDayList.get(0);
									}
								}
								request.setAttribute("messageOfTheDay", message);						
							}
							
							//Differentiate if View uses model.jsp or not
							if (templateFound) {
								request.setAttribute("title", viewTitle);
								request.setAttribute("htitle", viewHeaderTitle);
								request.setAttribute("view", viewPath);
								request.setAttribute("js", js);
								request.setAttribute("css", css);
								request.setAttribute("refreshUrl", relativePath);
								System.out.println("Forward: " + response.encodeURL(templatePath));
								System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
								getServletContext().getRequestDispatcher(response.encodeURL(templatePath)).forward(request, response);
							} else {
								System.out.println("Forward: " + response.encodeURL(viewPath));
								System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
								getServletContext().getRequestDispatcher(response.encodeURL(viewPath)).forward(request, response);
							}
						}
					} catch (InstantiationException e1) {
						e1.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (IllegalAccessException e1) {
						e1.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (ClassNotFoundException e1) {
						e1.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					} catch (Exception e) {
						e.printStackTrace();
						System.out.println("Redirect: " + response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						System.out.println("\n+++++++++++++++++++++++++++++++++++++++\n");
						response.sendRedirect(response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						
						/*request.setAttribute("redirectUrl", response.encodeRedirectURL(server.getString("server.https.prefix") + request.getServerName() + ":" + server.getString("server.secure.port") + getServletContext().getContextPath() + request.getServletPath() + views.getString("error.url")));
						getServletContext().getRequestDispatcher(response.encodeURL("/WEB-INF/jsp/redirect.jsp")).forward(request, response);*/
					}
				}

			}
		}
	}
}
