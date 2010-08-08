package at.redcross.tacos.web.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

public class WebAuthenticationVoter implements AccessDecisionVoter {

	@Override
	public int vote(Authentication authentication, Object object, Collection<ConfigAttribute> attributes) {
		// we make no decision if no principal is provided
		if (!(authentication.getPrincipal() instanceof WebUserDetails)) {
			return ACCESS_ABSTAIN;
		}
		// we make no decision if the principal is NO super user
		WebUserDetails principal = (WebUserDetails) authentication.getPrincipal();
		if (!principal.getLogin().isSuperUser()) {
			return ACCESS_ABSTAIN;
		}

		// super user detected
		return ACCESS_GRANTED;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}
}
