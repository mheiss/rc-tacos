package at.redcross.tacos.web.security;

import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

public class WebExpressionConfigAttribute implements ConfigAttribute {

	private static final long serialVersionUID = -3846838394392901404L;

	private final Expression expression;

	public WebExpressionConfigAttribute(Expression expression) {
		this.expression = expression;
	}

	@Override
	public String getAttribute() {
		return null;
	}

	public Expression getExpression() {
		return expression;
	}
}
