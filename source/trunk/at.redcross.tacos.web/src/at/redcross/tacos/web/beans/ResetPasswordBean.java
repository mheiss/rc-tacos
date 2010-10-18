package at.redcross.tacos.web.beans;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import at.redcross.tacos.dbal.entity.RestoreLogin;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persistence.EntityManagerFactory;

@KeepAlive
@ManagedBean(name = "resetPasswordBean")
public class ResetPasswordBean extends PasswordBean {

	private static final long serialVersionUID = -5921875911157481215L;

	private final static Log logger = LogFactory.getLog(ResetPasswordBean.class);

	/** reset password by name */
	private String username;

	/** the secure token */
	private String token;

	/** the new password */
	private String password;

	/** the new password */
	private String password2;

	// ---------------------------------
	// Business methods
	// ---------------------------------
	/** process the reset request */
	public void resetPassword(ActionEvent ae) {
		setRequestSend(false);
		// check if a token is existing
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			handleRequest(manager);
		} catch (Exception ex) {
			logger.fatal("Failed to reset password for '" + username + "'", ex);
			FacesUtils.addErrorMessage("Interner Fehler beim Zurücksetzen des Passwortes");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	/** process the reset request */
	private void handleRequest(EntityManager manager) throws Exception {
		// check if a request is existing
		TypedQuery<RestoreLogin> query = manager.createQuery(RESTORE_QUERY, RestoreLogin.class);
		query.setParameter("username", username);
		List<RestoreLogin> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			FacesUtils.addErrorMessage("Es wurde keine Anfrage zum Zurücksetzen des Passwortes"
					+ " für den Benutzernamen '" + username + "' gefunden");
			return;
		}
		// check if the token is valid
		RestoreLogin restore = resultList.iterator().next();
		if (!restore.getToken().equals(token)) {
			FacesUtils.addErrorMessage("Das eingegebene Token ist nicht gültig.");
			return;
		}
		// check if the token is expired
		if (restore.getExpireAt().before(new Date())) {
			FacesUtils.addErrorMessage("Dieser Token ist leider nicht mehr gültig."
					+ " Bitte senden Sie eine erneute Anfrage um Ihr Passwort zurückzusetzen.");
			return;
		}

		// remove existing requests from this user
		manager.remove(restore);

		// calculate new hash for database and update the entry
		SystemUser user = findByUsername(manager, username);
		PasswordEncoder encoder = new ShaPasswordEncoder(256);
		user.getLogin().setPassword(encoder.encodePassword(password, null));
		manager.persist(user.getLogin());

		// commit the new password
		EntityManagerHelper.commit(manager);
		setRequestSend(true);
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setUsername(String username) {
		this.username = username;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getUsername() {
		return username;
	}

	public String getToken() {
		return token;
	}

	public String getPassword() {
		return password;
	}

	public String getPassword2() {
		return password2;
	}
}
