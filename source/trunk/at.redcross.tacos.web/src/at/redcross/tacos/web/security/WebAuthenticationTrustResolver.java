package at.redcross.tacos.web.security;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;

public class WebAuthenticationTrustResolver implements AuthenticationTrustResolver {

	@Override
	public boolean isAnonymous(Authentication authentication) {
		// no authentication -> principal is 'anonymous user'
		return authentication.getPrincipal() instanceof String;
	}

	@Override
	public boolean isRememberMe(Authentication authentication) {
		return false;
	}
}
