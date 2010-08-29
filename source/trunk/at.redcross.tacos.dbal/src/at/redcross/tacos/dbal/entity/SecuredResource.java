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
@Table(name = "SecuredResource")
public class SecuredResource extends EntityImpl {

	private static final long serialVersionUID = 3737367770088547120L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String resource;

	@Column(nullable = false)
	private String access;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return resource;
	}

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("resource", resource).toString();
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
		SecuredResource rhs = (SecuredResource) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setResource(String resource) {
		this.resource = resource;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public String getResource() {
		return resource;
	}

	public String getAccess() {
		return access;
	}
}
