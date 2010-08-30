package at.redcross.tacos.web.faces;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;

/**
 * The {@code FacesUtils} contains common static helper methods.
 */
public class FacesUtils {

	private static Log log = LogFactory.getLog(FacesUtils.class);

	// ---------------------------------
	// Message Helper Methods
	// ---------------------------------
	public static FacesMessage createErrorMessage(String summary) {
		FacesMessage message = new FacesMessage(summary);
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		return message;
	}

	// ---------------------------------
	// Application Helper Methods
	// ---------------------------------
	public static String pretty(String url) {
		return "pretty:" + url;
	}

	public static String getContextRoot() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		return facesContext.getExternalContext().getRequestContextPath();
	}

	// ---------------------------------
	// Servlet utility methods
	// ---------------------------------
	public static HttpServletRequest getHttpServletRequest() {
		ExternalContext context = getExternalContext();
		return (HttpServletRequest) context.getRequest();
	}

	public static HttpServletResponse getHttpServletResponse() {
		ExternalContext context = getExternalContext();
		return (HttpServletResponse) context.getResponse();
	}

	public static HttpSession getSession() {
		ExternalContext context = getExternalContext();
		HttpSession session = (HttpSession) context.getSession(false);
		return session;
	}

	// ---------------------------------
	// GetAttribute Helper Methods
	// ---------------------------------
	public static String getRequestParameter(final String param) {
		ExternalContext context = getExternalContext();
		return context.getRequestParameterMap().get(param);
	}

	/** Returns the value of the component in the given form */
	public static Object getComponentValue(FacesContext context, String formId, String componentId) {
		UIComponent component = context.getViewRoot().findComponent(formId + ":" + componentId);
		UIInput input = (UIInput) component;
		Object submittedValue = input.getSubmittedValue();
		Converter converter = input.getConverter();
		if (converter != null) {
			return converter.getAsObject(context, component, (String) submittedValue);
		}
		return submittedValue;
	}

	public static Object getSessionAttribute(final String key) {
		return getExternalContext().getSessionMap().get(key);
	}

	public static void setSessionAttribute(final String key, final Object value) {
		getExternalContext().getSessionMap().put(key, value);
	}

	// ---------------------------------
	// Faces Object helpers
	// ---------------------------------
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static ExternalContext getExternalContext() {
		FacesContext faces = getFacesContext();
		ExternalContext context = faces.getExternalContext();
		return context;
	}

	/**
	 * Searches and returns the given bean. If the bean is not existing in the
	 * current context then it will be created. The name of the bean must be a
	 * valid el expression.
	 * 
	 * @param beanName
	 *            the name of the bean e.g.: myBean
	 * @param <T>
	 *            the type of the bean
	 * @return the created bean
	 */
	@SuppressWarnings("unchecked")
	public static <T> T lookupBean(Class<T> type) {
		if (!(type instanceof Class<?>)) {
			throw new IllegalArgumentException("A bean class must be specified");
		}
		Class<T> clazz = (Class<T>) type;
		ManagedBean managedBean = clazz.getAnnotation(ManagedBean.class);
		if (managedBean == null) {
			throw new IllegalArgumentException("'" + type + "' is not a managed bean");
		}
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ELContext elContext = facesContext.getELContext();
		ELResolver elResolver = elContext.getELResolver();
		return (T) elResolver.getValue(elContext, null, managedBean.name());
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

			// make the URL relative
			String requestedUri = prettyContext.getOriginalRequestUrl();
			requestedUri = requestedUri.replace(externalContext.getRequestContextPath(), "");
			requestedUri = requestedUri.replaceFirst("/", "");

			PrettyUrlMapping requestedMapping = prettyConfig.getMappingForUrl(requestedUri);
			String prettyId = (requestedMapping != null) ? requestedMapping.getId() : null;
			if (log.isErrorEnabled()) {
				String message = "Failed to process request from %1$s (%2$s)";
				log.error(String.format(message, requestedUri, prettyId), t);
			}
			ExceptionResolver resolver = new ExceptionResolver(t);

			// redirect the user to the error page
			PrettyUrlMapping mapping = prettyConfig.getMappingById("error");
			StringBuffer target = new StringBuffer();
			target.append(externalContext.getRequestContextPath());
			target.append(mapping.getPattern());
			target.append("?cameFrom=" + requestedUri);
			target.append("&errorCode=" + resolver.resolve());
			facesContext.responseComplete();
			externalContext.redirect(target.toString());
		} catch (Exception e) {
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
		return facesContext.getApplication().getExpressionFactory().createValueExpression(
				facesContext.getELContext(), valueExpression, valueType);
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
		return facesContext.getApplication().getExpressionFactory().createMethodExpression(
				facesContext.getELContext(), actionExpression, returnType, new Class[0]);
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
			} else {
				builder.append("Unknown Source");
			}
			builder.append(" ) ");
			builder.append("\n\r");
		}
		String cause = t.getCause() != null ? t.getCause().getClass().getSimpleName()
				: " (not available) ";
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
	}
}
