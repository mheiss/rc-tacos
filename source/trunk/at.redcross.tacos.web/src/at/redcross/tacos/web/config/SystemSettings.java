package at.redcross.tacos.web.config;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * The {@code SystemSettings} provides access to the global system configuration
 * settings.
 */
@XStreamAlias("systemSettings")
public class SystemSettings implements Serializable {

	private static final long serialVersionUID = 8690368855777808625L;

	/** the current software version */
	private String tacosVersion;

	/** SMTP port */
	private int smtpPort;

	/** SMTP host */
	private String smtpHost;

	/** SMTP username */
	private String smtpUser;

	/** SMTP password */
	private String smtpPassword;

	/** SMTP from address */
	private String smtpFromMail;

	/** SMTP from address */
	private String smtpFromUser;

	/** SMTP use encryption */
	private boolean smtpTsl;

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getTacosVersion() {
		return tacosVersion;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public String getSmtpUser() {
		return smtpUser;
	}

	public String getSmtpPassword() {
		return smtpPassword;
	}

	public String getSmtpFromMail() {
		return smtpFromMail;
	}

	public String getSmtpFromUser() {
		return smtpFromUser;
	}

	public boolean isSmtpTsl() {
		return smtpTsl;
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setTacosVersion(String tacosVersion) {
		this.tacosVersion = tacosVersion;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public void setSmtpUser(String smtpUser) {
		this.smtpUser = smtpUser;
	}

	public void setSmtpPassword(String smtpPassword) {
		this.smtpPassword = smtpPassword;
	}

	public void setSmtpFromUser(String smtpFromUser) {
		this.smtpFromUser = smtpFromUser;
	}

	public void setSmtpFromMail(String smtpFromMail) {
		this.smtpFromMail = smtpFromMail;
	}

	public void setSmtpTsl(boolean smtpTsl) {
		this.smtpTsl = smtpTsl;
	}

}
