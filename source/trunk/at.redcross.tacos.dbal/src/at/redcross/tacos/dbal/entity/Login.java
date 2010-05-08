package at.redcross.tacos.dbal.entity;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Login")
public class Login {

	@Id
	@Column(unique = true)
	private String alias;

	@Column
	private String password;

	@Temporal(TemporalType.DATE)
	private Calendar expireAt;

	@Column
	private boolean invalidLogout;

	@Column
	private boolean passwordExpired;

	@OneToOne(mappedBy = "login")
	private SystemUser systemUser;

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setExpireAt(Calendar expireAt) {
		this.expireAt = expireAt;
	}

	public void setInvalidLogout(boolean invalidLogout) {
		this.invalidLogout = invalidLogout;
	}

	public void setPasswordExpired(boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getAlias() {
		return alias;
	}

	public String getPassword() {
		return password;
	}

	public Calendar getExpireAt() {
		return expireAt;
	}

	public boolean isInvalidLogout() {
		return invalidLogout;
	}

	public boolean isPasswordExpired() {
		return passwordExpired;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
}
