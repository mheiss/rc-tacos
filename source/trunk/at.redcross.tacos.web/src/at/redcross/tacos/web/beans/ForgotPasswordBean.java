package at.redcross.tacos.web.beans;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.ajax4jsf.model.KeepAlive;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.SimpleEmail;

import at.redcross.tacos.dbal.entity.RestoreLogin;
import at.redcross.tacos.dbal.entity.SystemUser;
import at.redcross.tacos.dbal.manager.EntityManagerHelper;
import at.redcross.tacos.web.config.MailProvider;
import at.redcross.tacos.web.faces.FacesUtils;
import at.redcross.tacos.web.persitence.EntityManagerFactory;
import at.redcross.tacos.web.utils.StringUtils;

import com.ocpsoft.pretty.PrettyContext;

@KeepAlive
@ManagedBean(name = "forgotPasswordBean")
public class ForgotPasswordBean extends PasswordBean {

	private static final long serialVersionUID = 8569615171346893598L;

	private final static Log logger = LogFactory.getLog(ForgotPasswordBean.class);

	/** reset password by mail */
	private String email;

	/** reset password by name */
	private String username;

	// ---------------------------------
	// Business methods
	// ---------------------------------
	/** sends the request by mail */
	public void requestPassword(ActionEvent ae) {
		setRequestSend(false);
		// validate that something is given
		if (StringUtils.saveString(email).isEmpty() && StringUtils.saveString(username).isEmpty()) {
			String msg = "Um fortzufahren geben Sie Ihren Benutzernamen oder Ihre E-Mail an.";
			FacesUtils.addErrorMessage(msg);
			return;
		}
		EntityManager manager = null;
		try {
			manager = EntityManagerFactory.createEntityManager();
			handleRequest(manager);
		} catch (Exception ex) {
			logger.fatal("Failed to reset password '" + username + " | " + email + "'", ex);
			FacesUtils.addErrorMessage("Interner Fehler beim Zurücksetzen des Passwortes");
		} finally {
			manager = EntityManagerHelper.close(manager);
		}
	}

	/** process the reset request */
	private void handleRequest(EntityManager manager) throws Exception {
		SystemUser user = null;
		if (!StringUtils.saveString(email).isEmpty()) {
			user = findByMail(manager, email);
			if (user == null) {
				String message = "Es konnte keinen Benutzer mit der E-Mail Adresse '" + email
						+ "' gefunden werden";
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

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		PrettyContext prettyContext = PrettyContext.getCurrentInstance();

		// send the mail to the user
		SimpleEmail mail = MailProvider.configureMail(new SimpleEmail());
		mail.addTo(user.getAddress().getEmail());
		mail.setSubject("TACOS - Anfrage zum Zurücksetzen des Passwortes");

		StringBuilder builder = new StringBuilder();
		builder.append("Hallo ");
		builder.append(user.getFirstName() + " " + user.getLastName() + ",");
		builder.append("\n\r");
		builder.append("Wir haben eine Anfrage zum Zurücksetzen Ihres Passwortes erhalten. ");
		builder.append("Sollten Sie diese Anfrage abgeschickt haben, ");
		builder.append("dann folgen Sie bitte den nachfolgenden Anweisungen.\n");
		builder.append("Wenn sie nicht wollen das Ihr Passwort zurückgesetzt wird, ");
		builder.append("dann ignorieren Sie einfach diese Nachricht.");
		builder.append("\n\n");
		builder.append("Bitte klicken Sie auf den folgenden Link um Ihr Passwort zurückzusetzen:");
		builder.append("\n\n");
		builder.append(externalContext.getRequestScheme()).append("://");
		builder.append(externalContext.getRequestServerName()).append(":");
		builder.append(externalContext.getRequestServerPort());
		builder.append(externalContext.getRequestContextPath());
		builder.append(prettyContext.getConfig().getMappingById("resetPassword").getPattern());
		builder.append("?username=" + restoreLogin.getUsername());
		builder.append("&token=" + restoreLogin.getToken());

		mail.setMsg(builder.toString());
		mail.send();

		EntityManagerHelper.commit(manager);
		setRequestSend(true);
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
}
