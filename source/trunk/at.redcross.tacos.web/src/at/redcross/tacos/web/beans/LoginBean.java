package at.redcross.tacos.web.beans;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.ajax4jsf.model.KeepAlive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.security.WebUserDetails;

@KeepAlive
@ManagedBean(name = "loginBean")
public class LoginBean extends BaseBean {

	private final static Logger logger = LoggerFactory.getLogger(LoginBean.class);

	private final static long serialVersionUID = -4477225555616492426L;

	private String username;
	private String password;
	private String password2;
	private String currentPassword;

	@Override
	protected void init() throws Exception {
		// nothing to do :)
	}

	// ---------------------------------
	// Actions
	// ---------------------------------
	/** Delegate request to SpringSecurity */
	public void login(ActionEvent event) throws IOException, ServletException {
		// setup the request URL that is passed to the security check
		StringBuilder builder = new StringBuilder("/j_spring_security_check");
		builder.append("?j_username=").append(username);
		builder.append("&j_password=").append(password);

		// password field will NOT be restored
		password = null;

		// now delegate to SpringSecurity
		redirectToPage(builder.toString());
	}

	public void changePassword(ActionEvent event) {
		EntityManager manager = null;
		try {
			Login login = getLogin();
			manager = EntityManagerFactory.createEntityManager();
			// validate old password
			PasswordEncoder encoder = new ShaPasswordEncoder(256);
			currentPassword = encoder.encodePassword(currentPassword, null);
			if (!currentPassword.equals(login.getPassword())) {
				logger.error("Password missmatch");
				FacesUtils.addErrorMessage("Ihr aktuelles Kennwort ist ungültig.");
				return;
			}
			if(password == null || password.trim().isEmpty()) {
				FacesUtils.addErrorMessage("Das neue Kennwort kann nicht leer sein.");
				return;
			}
			if(!password.equals(password2)) {
				FacesUtils.addErrorMessage("Die eingegebenen Kennwörter stimmen nicht überein.");
				return;
			}
			
			// update password and persist changes
			login.setPassword(encoder.encodePassword(password, null));
			manager.merge(login);
			EntityManagerHelper.commit(manager);
			// reset state
			password = null;
			password2 = null;
			currentPassword = null;
		} catch (Exception ex) {
			logger.error("Failed to persist the groups", ex);
			FacesUtils.addErrorMessage("Die Änderungen konnten nicht gespeichert werden");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	// ---------------------------------
	// Properties
	// ---------------------------------
	public Login getLogin() {
		if (!isAuthenticated()) {
			return null;
		}
		WebUserDetails details = (WebUserDetails) getAuthentication().getPrincipal();
		return details.getLogin();
	}

	public SystemUser getUser() {
		if (!isAuthenticated()) {
			return null;
		}
		return getLogin().getSystemUser();
	}

	public boolean isAuthenticated() {
		SecurityContext context = SecurityContextHolder.getContext();
		if (context instanceof SecurityContext) {
			Authentication authentication = context.getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				return false;
			}
		}
		return true;
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setPassword(String password) {
		this.password = password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getPassword2() {
		return password2;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	// ---------------------------------
	// Private helpers
	// ---------------------------------
	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private void redirectToPage(String page) throws IOException, ServletException {
		FacesContext currentInstance = FacesContext.getCurrentInstance();
		ExternalContext context = currentInstance.getExternalContext();
		ServletRequest request = (ServletRequest) context.getRequest();
		ServletResponse response = (ServletResponse) context.getResponse();
		RequestDispatcher dispatcher = request.getRequestDispatcher(page);
		dispatcher.forward(request, response);
		currentInstance.responseComplete();
	}
}
