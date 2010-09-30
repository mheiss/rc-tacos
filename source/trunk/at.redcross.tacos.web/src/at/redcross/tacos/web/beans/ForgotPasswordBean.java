package at.redcross.tacos.web.beans;

import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;

import at.redcross.tacos.dbal.entity.Login;
import at.redcross.tacos.dbal.entity.RestoreLogin;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.utils.StringUtils;

@KeepAlive
@ManagedBean(name = "forgotPasswordBean")
public class ForgotPasswordBean extends PasswordBean {

	private static final long serialVersionUID = 8569615171346893598L;

	/** cached query string for MAIL */
	private final static String MAIL_QUERY = "from SystemUser user where user.address.email like :email";

	/** cached query string for USERNAME */
	private final static String USER_QUERY = "from Login login where login.loginName like :username";

	/** cached remove existing entries */
	private final static String CLEAR_QUERY = "delete from RestoreLogin rl where rl.username like :username";

	/** reset password by mail */
	private String email;

	/** reset password by name */
	private String username;

	/** flag if the operation was successfully */
	private boolean requestSend = false;

	// ---------------------------------
	// Business methods
	// ---------------------------------
	/** sends the request by mail */
	public void requestPassword(ActionEvent ae) {
		requestSend = false;
		// validate that something is given
		if (StringUtils.saveString(email).isEmpty() && StringUtils.saveString(username).isEmpty()) {
			String msg = "Um fortzufahren geben Sie Ihren Benutzernamen oder Ihre E-Mail an.";
			FacesUtils.addErrorMessage(msg);
			return;
		}
		// validate that the user is existing
		EntityManager manager = null;
		try {
			SystemUser user = null;
			manager = EntityManagerFactory.createEntityManager();
			if (!StringUtils.saveString(email).isEmpty()) {
				user = findByMail(manager, email);
				if (user == null) {
					String message = "Es gibt leider keinen Benutzer mit der E-Mail-Adresse '"
							+ email + "'";
					FacesUtils.addErrorMessage(message);
					return;
				}
			}
			if (!StringUtils.saveString(username).isEmpty()) {
				user = findByUsername(manager, username);
				if (user == null) {
					String message = "Der angegebene Benutzername '" + username
							+ "' wurde nicht gefunden.";
					FacesUtils.addErrorMessage(message);
					return;
				}
			}
			// remove existing requests from this user
			Query query = manager.createQuery(CLEAR_QUERY);
			query.setParameter("username", user.getLogin().getLoginName());
			EntityTransaction transaction = manager.getTransaction();
			transaction.begin();
			query.executeUpdate();
			transaction.commit();

			// create the new entry
			RestoreLogin restoreLogin = new RestoreLogin();
			restoreLogin.setUsername(user.getLogin().getLoginName());
			restoreLogin.setExpireAt(DateUtils.addHours(new Date(), 72));
			restoreLogin.setToken(RandomStringUtils.random(16, true, true));
			manager.persist(restoreLogin);

			EntityManagerHelper.commit(manager);
			requestSend = true;
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	/** Returns the system-user object using the given query */
	private SystemUser findByUsername(EntityManager manager, String username) {
		TypedQuery<Login> query = manager.createQuery(USER_QUERY, Login.class);
		query.setParameter("username", username);
		List<Login> logins = query.getResultList();
		if (logins.isEmpty()) {
			return null;
		}
		return logins.iterator().next().getSystemUser();
	}

	/** Returns the system-user object using the given query */
	private SystemUser findByMail(EntityManager manager, String email) {
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
	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public boolean isRequestSend() {
		return requestSend;
	}
}
