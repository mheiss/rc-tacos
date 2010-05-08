package at.redcross.tacos.web.faces;

import java.lang.reflect.Method;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.mapping.UrlMapping;

/**
 * The {@code FacesUtils} contains common static helper methods.
 */
public class FacesUtils {

	private static Log log = LogFactory.getLog(FacesUtils.class);

	/**
	 * Returns the current {@code ServletContext}
	 * 
	 * @return the servlet context
	 */
	public static ServletContext getServletContext() {
		ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
		return (ServletContext) ext.getContext();
	}

	/**
	 * Returns a save string from the given object
	 */
	public static String saveString(Object object) {
		if (object == null) {
			return "";
		}
		return object.toString();
	}

	/**
	 * Returns the current session or attempts to creates a new one.
	 * 
	 * @return current session
	 */
	public static HttpSession getCurrentSession() {
		Object object = FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		return (HttpSession) object;
	}

	/**
	 * Returns the specified attribute from the session
	 * 
	 * @param key
	 *            the key of the attribute to get from the session
	 * @return the session object or null if nothing found object or null if
	 *         nothing found
	 */
	public static Object getSessionAttribute(String key) {
		HttpSession session = getCurrentSession();
		if (session == null)
			return null;
		return session.getAttribute(key);
	}

	/**
	 * Returns the specified attribute from the session and converts it to a
	 * string. The returned value will <b>never</b> be {@code null}.
	 * 
	 * @param key
	 *            the key of the attribute to get from the session
	 * @return the value mapped to the string or an empty string if the key is
	 *         not existing
	 */
	public static String getStringSessionAttribute(String key) {
		HttpSession session = getCurrentSession();
		if (session == null)
			return "";
		return FacesUtils.saveString(session.getAttribute(key));
	}

	/**
	 * Sets the specified attribute in the current session
	 * 
	 * @param key
	 *            the identifier to store the attribute
	 * @param attribute
	 *            the attribute to set
	 */
	public static void setSessionAttribute(String key, Object attribute) {
		HttpSession session = getCurrentSession();
		session.setAttribute(key, attribute);
	}

	/**
	 * Returns the specified request parameter out of the request map.
	 * 
	 * @param name
	 *            the name of the parameter to get
	 * @return the value of the parameter
	 */
	public static String getRequestParameter(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getExternalContext().getRequestParameterMap().get(name);
	}

	/**
	 * Searches and returns the given bean. If the bean is not existing in the
	 * current context then it will be created. The name of the bean must be a
	 * valid el expression.
	 * 
	 * @param beanName
	 *            the name of the bean e.g.: myBean
	 * @return the created and initialised bean
	 */
	public static Object lookupBean(String beanName) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		ELResolver elResolver = elContext.getELResolver();
		return elResolver.getValue(elContext, null, beanName);
	}

	/**
	 * Resets the managed bean based on the name. The method that will do the
	 * real initialisation work must either be {@linkplain PostConstruct
	 * annotated} or must have a method called <tt>prettyInit</tt> that takes no
	 * parameters.
	 * 
	 * @param beanName
	 *            the bean name of the managed bean to be reset
	 */
	public static void initBean(String beanName) throws Exception {
		Object bean = lookupBean(beanName);
		if (bean == null) {
			log.warn("Cannot reset bean '" + beanName + "'. No such bean exists");
		}
		for (Method m : bean.getClass().getDeclaredMethods()) {
			if (m.isAnnotationPresent(PostConstruct.class)) {
				m.invoke(bean, (Object[]) null);
			}
			if (m.getName().equalsIgnoreCase("prettyInit")) {
				m.invoke(bean, (Object[]) null);
			}
		}
	}

	/**
	 * Logs the current error and redirect to the 'error.faces' page
	 */
	public static void redirectError(Throwable t) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		try {
			// log the error message and the request URL
			PrettyContext prettyContext = PrettyContext.getCurrentInstance();
			PrettyConfig prettyConfig = prettyContext.getConfig();

			String requestedUri = prettyContext.getOriginalRequestUrl();
			UrlMapping requestedMapping = prettyConfig.getMappingForUrl(requestedUri);
			String prettyId = (requestedMapping != null) ? requestedMapping.getId() : null;
			if (log.isErrorEnabled()) {
				String message = "Failed to process request from %1$s (%2$s)";
				log.error(String.format(message, requestedUri, prettyId), t);
			}

			// redirect the user to the error page
			UrlMapping mapping = prettyConfig.getMappingById("error");
			StringBuffer target = new StringBuffer();
			target.append(externalContext.getRequestContextPath());
			target.append(mapping.getPattern());
			target.append("?cameFrom=" + requestedUri.replaceFirst("/", ""));
			facesContext.responseComplete();
			externalContext.redirect(target.toString());
		}
		catch (Exception e) {
			log.fatal("Failed to redirect to the error page", e);
		}
	}

	/**
	 * Evaluates the given {@link ValueExpression} and return the current value
	 * of the object.
	 */
	public static Object getValueExpression(String valueExpression) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ValueExpression expression = createValueExpression(valueExpression, Object.class);
		return expression.getValue(facesContext.getELContext());
	}

	/**
	 * Creates and returns a new {@link ValueExpression}.
	 * 
	 * @param valueExpression
	 *            the expression to create
	 * @param valueType
	 *            the return type, typically String.class
	 * @return the created <code>MethodExpression</code>
	 */
	public static ValueExpression createValueExpression(String valueExpression, Class<?> valueType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createValueExpression(facesContext.getELContext(), valueExpression, valueType);
	}

	/**
	 * Creates and returns a new {@link MethodExpression} that can be used to as
	 * actions for a link.
	 * 
	 * @param actionExpression
	 *            the expression to create
	 * @param returnType
	 *            the return type, typically String.class
	 * @return the created <code>MethodExpression</code>
	 */
	public static MethodExpression createActionExpression(String actionExpression, Class<?> returnType) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getApplication().getExpressionFactory().createMethodExpression(facesContext.getELContext(), actionExpression, returnType,
				new Class[0]);
	}

	/**
	 * Creates a new {@link FacesMessage} with the severity
	 * {@link FacesMessage#SEVERITY_ERROR} and adds it to the current
	 * {@link FacesContext}
	 * 
	 * @param messageText
	 *            the message to display
	 */
	public static void addErrorMessage(String messageText) {
		addErrorMessage(messageText, null);
	}

	/**
	 * Creates a new {@link FacesMessage} with the severity
	 * {@link FacesMessage#SEVERITY_ERROR} and adds it to the current
	 * {@link FacesContext}
	 * 
	 * @param t
	 *            the exception that occurred
	 */
	public static void addErrorMessage(Throwable t) {
		StringBuilder builder = new StringBuilder();
		for (StackTraceElement element : t.getStackTrace()) {
			builder.append("at  ");
			builder.append(element.getClassName());
			builder.append(".").append(element.getMethodName()).append(" ( ");
			if (element.getFileName() != null) {
				builder.append(element.getFileName()).append(":");
				builder.append(element.getLineNumber());
			}
			else {
				builder.append("Unknown Source");
			}
			builder.append(" ) ");
			builder.append("\n\r");
		}
		String cause = t.getCause() != null ? t.getCause().getClass().getSimpleName() : " (not available) ";
		addErrorMessage(cause + " - " + t.getMessage(), builder.toString());
	}

	/**
	 * Creates a new {@link FacesMessage} with the severity
	 * {@link FacesMessage#SEVERITY_ERROR} and adds it to the current
	 * {@link FacesContext}
	 * 
	 * @param messageText
	 *            the message to display
	 * @param detailText
	 *            the detailed error description
	 */
	public static void addErrorMessage(String messageText, String detailText) {
		FacesMessage message = new FacesMessage();
		message.setSummary(messageText);
		message.setDetail(detailText);
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		FacesContext.getCurrentInstance().addMessage(null, message);
		log.error(messageText);
	}
}
