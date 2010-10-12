package at.redcross.tacos.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.web.faces.FacesUtils;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.config.PrettyConfig;
import com.ocpsoft.pretty.config.PrettyUrlMapping;

public abstract class PasswordBean implements Serializable {

	private static final long serialVersionUID = -209015194454141136L;
	
	/** query string to get user by MAIL */
	protected final static String MAIL_QUERY = "from SystemUser user where user.address.email like :email";

	/** query string to get user by USERNAME */
	protected final static String USER_QUERY = "from Login login where login.loginName like :username";

	/** query to remove existing restore login entry */
	protected final static String CLEAR_QUERY = "delete from RestoreLogin rl where rl.username like :username";
	
	/** query to get restore login by username */
	protected final static String RESTORE_QUERY = "from RestoreLogin rl where rl.username like :username";
	
	
	/** flag if the operation was successfully */
	private boolean requestSend = false;

	// ---------------------------------
	// Business methods
	// ---------------------------------
	/** abort and redirect to the login page */
	public void abortRequest(ActionEvent ae) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		try {
			PrettyContext prettyContext = PrettyContext.getCurrentInstance();
			PrettyConfig prettyConfig = prettyContext.getConfig();
			PrettyUrlMapping mapping = prettyConfig.getMappingById("login");
			facesContext.responseComplete();
			externalContext
					.redirect(externalContext.getRequestContextPath() + mapping.getPattern());
		} catch (Exception e) {
			FacesUtils.redirectError(e);
		}
	}
	
	/** Returns the system-user object using the given query */
	protected SystemUser findByUsername(EntityManager manager, String username) {
		TypedQuery<Login> query = manager.createQuery(USER_QUERY, Login.class);
		query.setParameter("username", username);
		List<Login> logins = query.getResultList();
		if (logins.isEmpty()) {
			return null;
		}
		return logins.iterator().next().getSystemUser();
	}

	/** Returns the system-user object using the given query */
	protected SystemUser findByMail(EntityManager manager, String email) {
		TypedQuery<SystemUser> query = manager.createQuery(MAIL_QUERY, SystemUser.class);
		query.setParameter("email", email);
		List<SystemUser> users = query.getResultList();
		if (users.isEmpty()) {
			return null;
		}
		return users.iterator().next();
	}
	
	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setRequestSend(boolean requestSend) {
		this.requestSend = requestSend;
	}
	
	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public boolean isRequestSend() {
		return requestSend;
	}
}
