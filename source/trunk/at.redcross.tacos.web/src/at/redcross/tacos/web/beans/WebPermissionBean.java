package at.redcross.tacos.web.beans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.SecuredAction;
import at.redcross.tacos.dbal.entity.SecuredResource;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.helper.SecuredActionHelper;
import at.redcross.tacos.dbal.helper.SecuredResourceHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebAuthenticationTrustResolver;
import at.redcross.tacos.web.security.WebAuthenticationVoter;
import at.redcross.tacos.web.security.WebSecurityExpressionRoot;
import at.redcross.tacos.web.security.WebUserDetails;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;

@ManagedBean(name = "permissionBean")
public class WebPermissionBean {

	private final static Logger log = LoggerFactory.getLogger(WebPermissionBean.class);

	/** the available resources */
	private List<SecuredResource> securedResources;

	/** the available actions */
	private List<SecuredAction> securedActions;

	/** parser for the action / resource expressions */
	private transient ExpressionParser parser;

	@PostConstruct
	protected void init() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			securedResources = SecuredResourceHelper.list(manager);
			securedActions = SecuredActionHelper.list(manager);
			parser = new SpelExpressionParser();
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Principal requests
	// ---------------------------------
	public SystemUser getSystemUser() {
		WebUserDetails principal = getPrincipal();
		if (principal == null) {
			return null;
		}
		return principal.getLogin().getSystemUser();
	}

	public WebUserDetails getPrincipal() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		if (principal instanceof WebUserDetails) {
			return (WebUserDetails) principal;
		}
		return null;
	}

	// ---------------------------------
	// Resource requests
	// ---------------------------------
	public boolean isAuthorizedToAccessAdminArea() {
		return canAccessResource("admin-home");
	}

	public boolean isAuthorizedToAccessRosterArea() {
		return canAccessResource("roster-dayOverview");
	}

	// ---------------------------------
	// Action requests
	// ---------------------------------
	public boolean isAuthorizedToDeleteRoster() {
		return canExecuteAction("roster-deleteEntry");
	}

	public boolean isAuthorizedToEditRoster() {
		return canExecuteAction("roster-editEntry");
	}

	public boolean isAuthorizedToAssignCar() {
		return canExecuteAction("roster-assignCar");
	}
	
	public boolean isAuthorizedToCreateOutdatedRoster() {
		return canExecuteAction("roster-createOutdated");
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	/** Returns whether or not the current user can execute the given action */
	private boolean canExecuteAction(String actionName) {
		// if no restriction is defined we permit the execution
		String actionExpression = findActionByName(actionName);
		if (actionExpression == null) {
			return true;
		}
		Expression expression = getExpressionByAction(actionExpression);
		return evaluateExpression(expression);
	}

	/** Returns whether or not the current user can access the given URL */
	private boolean canAccessResource(String prettyMappingId) {
		String mappedUrl = findMappingById(prettyMappingId);
		if (mappedUrl == null) {
			log.error("Mapping for '" + prettyMappingId + "' not found");
			return false;
		}
		Expression expression = getExpressionByUrl(mappedUrl);
		if (expression == null) {
			log.error("Expression for '" + mappedUrl + "' not found");
			return false;
		}
		return evaluateExpression(expression);
	}

	/** Returns the first matching action based on the given name */
	private String findActionByName(String actionName) {
		for (SecuredAction securedAction : securedActions) {
			if (securedAction.getName().equals(actionName)) {
				return securedAction.getActionExpression();
			}
		}
		return null;
	}

	/** Returns the first matching resource based on the given page */
	private String findMappingById(String prettyMappingId) {
		PrettyConfig prettyConfig = PrettyContext.getCurrentInstance().getConfig();
		for (PrettyUrlMapping mapping : prettyConfig.getMappings()) {
			if (mapping.getId().equals(prettyMappingId)) {
				return mapping.getPattern();
			}
		}
		return null;
	}

	/** Finds the according expression based on the action */
	private Expression getExpressionByAction(String actionExpression) {
		return parser.parseExpression(actionExpression);
	}

	/** Finds the according expression based on the resource */
	private Expression getExpressionByUrl(String urlResource) {
		SecuredResource resource = getResourceByUrl(urlResource);
		if (resource != null) {
			return parser.parseExpression(resource.getAccess());
		}

		// go through all parts of the URL
		String tmp = urlResource;
		if (!tmp.endsWith("/")) {
			tmp = tmp + "/";
		}
		while (!tmp.isEmpty()) {
			tmp = tmp.substring(0, tmp.lastIndexOf("/"));
			resource = getResourceByUrl(tmp + "/**");
			if (resource != null) {
				return parser.parseExpression(resource.getAccess());
			}
		}
		return null;
	}

	/** Finds the best matching secured resource according to the given URL */
	private SecuredResource getResourceByUrl(String url) {
		for (SecuredResource resource : securedResources) {
			if (resource.getResource().equals(url)) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Evaluates the given expression and returns whether or not the user has
	 * the given permissions. The expression will be evaluated against the
	 * currently authenticated user principal.
	 * 
	 * @param expression
	 *            the expression to evaluate
	 * @return {@code true} it the user has the given permission or {@code
	 *         false} if not
	 */
	private boolean evaluateExpression(Expression expression) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// check for super user
		AccessDecisionVoter voter = new WebAuthenticationVoter();
		if (voter.vote(auth, null, null) == AccessDecisionVoter.ACCESS_GRANTED) {
			return true;
		}
		WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(auth);
		root.setTrustResolver(new WebAuthenticationTrustResolver());
		EvaluationContext ctx = new StandardEvaluationContext(root);
		return ExpressionUtils.evaluateAsBoolean(expression, ctx);
	}
}