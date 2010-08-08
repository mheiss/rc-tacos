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
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.SecuredResource;
import at.redcross.tacos.dbal.helper.SecuredResourceHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebSecurityExpressionRoot;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;

@ManagedBean(name = "securedResourceBean")
public class SecuredResourceBean {

	private final static Logger log = LoggerFactory.getLogger(SecuredResourceBean.class);

	private List<SecuredResource> cachedResources;
	private ExpressionParser parser = new SpelExpressionParser();

	@PostConstruct
	protected void init() {
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			cachedResources = SecuredResourceHelper.list(manager);
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Resource request links
	// ---------------------------------
	public boolean isAdminLinkVisible() {
		return canAccess("admin-home");
	}

	// ---------------------------------
	// Helper methods
	// ---------------------------------
	/** Returns whether or not the current user can access the given URL */
	private boolean canAccess(String prettyMappingId) {
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
		return canAccess(expression);
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
		for (SecuredResource resource : cachedResources) {
			if (resource.getResource().equals(url)) {
				return resource;
			}
		}
		return null;
	}

	/**
	 * Returns whether or not the user can access the given resource
	 */
	private boolean canAccess(Expression expression) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(auth);
		EvaluationContext ctx = new StandardEvaluationContext(root);
		return ExpressionUtils.evaluateAsBoolean(expression, ctx);
	}
}
