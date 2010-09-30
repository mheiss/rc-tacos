package at.redcross.tacos.web.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.ajax4jsf.model.KeepAlive;

@KeepAlive
@ManagedBean(name = "resetPasswordBean")
public class ResetPasswordBean extends PasswordBean {

	private static final long serialVersionUID = -5921875911157481215L;

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
