package at.redcross.tacos.web.config;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;

import at.redcross.tacos.web.utils.XmlFile;

/**
 * The {@code MailSender} provides a simple way to send a eMail using the system
 * configuration. The configuration is taken from the settings store.
 */
public class MailProvider {

	/**
	 * Initializes and configures the given mail object using the available
	 * system properties.
	 * 
	 * @param mail
	 *            the mail object to configure
	 * @return the initialized object that is ready to be send
	 */
	public static <T extends Email> T configureMail(T mail) throws Exception {
		return initMail(mail);
	}

	/** Initializes and returns the given mail object */
	private static <T extends Email> T initMail(T email) throws Exception {
		SystemSettings settings = XmlFile.read(SettingsStore.getInstance().getSettings());
		email.setSmtpPort(settings.getSmtpPort());
		email.setAuthenticator(new DefaultAuthenticator(settings.getSmtpUser(), settings
				.getSmtpPassword()));
		email.setDebug(false);
		email.setFrom(settings.getSmtpFromMail(), settings.getSmtpFromUser());
		email.setHostName(settings.getSmtpHost());
		email.setTLS(settings.isSmtpTsl());
		return email;
	}
}
