package at.redcross.tacos.web.security;

import java.util.Collection;

import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebSecurityExpressionHandler;

public class WebExpressionVoter implements AccessDecisionVoter {

	private WebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		FilterInvocation fi = (FilterInvocation) object;
		EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, fi);

		WebExpressionConfigAttribute attribute = findConfigAttribute(attributes);
		if (attribute == null) {
			return ACCESS_ABSTAIN;
		}

		boolean evaluationResult = ExpressionUtils
				.evaluateAsBoolean(attribute.getExpression(), ctx);
		return evaluationResult ? ACCESS_GRANTED : ACCESS_DENIED;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.isAssignableFrom(FilterInvocation.class);
	}

	private WebExpressionConfigAttribute findConfigAttribute(Collection<ConfigAttribute> attributes) {
		for (ConfigAttribute attribute : attributes) {
			if (attribute instanceof WebExpressionConfigAttribute) {
				return (WebExpressionConfigAttribute) attribute;
			}
		}
		return null;
	}

}
