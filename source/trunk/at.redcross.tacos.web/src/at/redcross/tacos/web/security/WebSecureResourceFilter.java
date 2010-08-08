package at.redcross.tacos.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import at.redcross.tacos.dbal.entity.SecuredResource;
import at.redcross.tacos.dbal.helper.SecuredResourceHelper;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.persitence.EntityManagerFactory;

public class WebSecureResourceFilter implements FilterInvocationSecurityMetadataSource {

	private ExpressionParser parser = new SpelExpressionParser();

	@Override
	public Collection<ConfigAttribute> getAttributes(Object filter) throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) filter;
		String url = filterInvocation.getRequestUrl();
		List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>();

		// get the roles for requested page from the database
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();

			// direct resource hit?
			if (handleUrl(manager, attributes, url)) {
				return attributes;
			}

			// go through all parts of the URL
			String resource = url;
			if (!resource.endsWith("/")) {
				resource = resource + "/";
			}
			while (!resource.isEmpty()) {
				resource = resource.substring(0, resource.lastIndexOf("/"));
				if (handleUrl(manager, attributes, resource + "/**")) {
					return attributes;
				}
			}
			return attributes;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	private boolean handleUrl(EntityManager manager, List<ConfigAttribute> attributes, String url) {
		List<SecuredResource> resources = SecuredResourceHelper.getByName(manager, url);
		if (!resources.isEmpty()) {
			addAccess(attributes, resources);
			return true;
		}
		return false;
	}

	/** Appends all groups in the given resources to the given list */
	private void addAccess(List<ConfigAttribute> attributes, List<SecuredResource> resources) {
		for (SecuredResource resource : resources) {
			if (resource.isExpression()) {
				Expression ex = parser.parseExpression(resource.getAccess());
				attributes.add(new WebExpressionConfigAttribute(ex));
			} else {
				attributes.addAll(SecurityConfig.createSingleAttributeList(resource.getAccess()));
			}
		}
	}
}
