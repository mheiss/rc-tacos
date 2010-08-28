package at.redcross.tacos.web.beans;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.web.security.WebAuthenticationTrustResolver;
import at.redcross.tacos.web.security.WebSecurityExpressionRoot;

public abstract class SecuredBean extends BaseBean {

	private static final long serialVersionUID = 8405693389750879680L;

	/** parses the given expression (used during validation) */
	protected ExpressionParser parser;

	@Override
	protected void init() throws Exception {
		parser = new SpelExpressionParser();
	}

	/** Returns a new context to validate expressions */
	protected EvaluationContext getEvaluationContext() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebSecurityExpressionRoot root = new WebSecurityExpressionRoot(auth);
		root.setTrustResolver(new WebAuthenticationTrustResolver());
		return new StandardEvaluationContext(root);
	}

	/** Returns whether or not the given string is a valid expression */
	protected boolean isValidExpression(String expressionString) {
		try {
			EvaluationContext evaluationContext = getEvaluationContext();
			Expression expression = parser.parseExpression(expressionString);
			ExpressionUtils.evaluateAsBoolean(expression, evaluationContext);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

}
