package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "SecuredAction")
public class SecuredAction extends EntityImpl {

	private static final long serialVersionUID = -755171555871540438L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String actionExpression;

	@Column(nullable = false)
	private String access;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return actionExpression;
	}

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("actionExpression", actionExpression).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).hashCode();
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
		SecuredAction rhs = (SecuredAction) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setAccess(String access) {
		this.access = access;
	}

	public void setActionExpression(String actionExpression) {
		this.actionExpression = actionExpression;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public String getAccess() {
		return access;
	}

	public String getActionExpression() {
		return actionExpression;
	}
}
