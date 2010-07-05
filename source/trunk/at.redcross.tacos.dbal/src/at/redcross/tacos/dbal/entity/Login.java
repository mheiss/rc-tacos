package at.redcross.tacos.dbal.entity;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "Login")
public class Login extends EntityImpl {

	private static final long serialVersionUID = -8204373123508547368L;

	/** the default date (01.01.1970) */
	private static Date DEFAULT_DATE = null;
	static {
		Calendar DEFAULT = Calendar.getInstance();
		DEFAULT.clear();
		DEFAULT_DATE = DEFAULT.getTime();
	}

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String loginName;

	@Column
	private String password;

	@Temporal(TemporalType.DATE)
	private Date expireAt;

	@Column
	private boolean locked;

	@OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
	private SystemUser systemUser;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return loginName;
	}

	// ---------------------------------
	// Common helper methods
	// ---------------------------------
	public boolean isExpired() {
		if (expireAt == null) {
			return false;
		}
		if (DEFAULT_DATE.compareTo(expireAt) == 0) {
			return false;
		}
		Calendar currentDate = Calendar.getInstance();
		Calendar expireAt = Calendar.getInstance();
		expireAt.setTime(this.expireAt);
		return !currentDate.before(expireAt);
	}

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("alias", loginName).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(loginName).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Login rhs = (Login) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setSystemUser(SystemUser systemUser) {
		this.systemUser = systemUser;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setExpireAt(Date expireAt) {
		this.expireAt = expireAt;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public String getLoginName() {
		return loginName;
	}

	public String getPassword() {
		return password;
	}

	public Date getExpireAt() {
		return expireAt;
	}

	public boolean isLocked() {
		return locked;
	}

	public SystemUser getSystemUser() {
		return systemUser;
	}
}
