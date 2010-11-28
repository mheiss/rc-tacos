package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "ServiceType")
public class ServiceType extends EntityImpl {

	private static final long serialVersionUID = 7780994907233444353L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column
	private String shortName;

	@Column
	private boolean signInOut = true;

	@Column(length = 255)
	private String description;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return name;
	}

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("name", name).toString();
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
		ServiceType rhs = (ServiceType) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setName(String name) {
		this.name = name;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setSignInOut(boolean signInOut) {
		this.signInOut = signInOut;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return shortName;
	}

	public String getDescription() {
		return description;
	}

	public boolean isSignInOut() {
		return signInOut;
	}
}
