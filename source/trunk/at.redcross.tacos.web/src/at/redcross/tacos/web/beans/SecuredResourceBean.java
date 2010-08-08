package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.web.security.WebUserDetails;

@ManagedBean(name = "securedResourceBean")
public class SecuredResourceBean {

	public boolean isAdmin() {
		return hasGroup("ROLE_ADMIN");
	}

	/** Returns whether or not the current user is in the specified group */
	private boolean hasGroup(String... groups) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// anonymous login does only have a java.lang.String principal
		if (!(auth.getPrincipal() instanceof WebUserDetails)) {
			return false;
		}
		WebUserDetails details = (WebUserDetails) auth.getPrincipal();
		for (String group : groups) {
			for (GrantedAuthority grantedAuthority : details.getAuthorities()) {
				if (grantedAuthority.getAuthority().equals(group)) {
					return true;
				}
			}
		}
		return false;
	}
}
