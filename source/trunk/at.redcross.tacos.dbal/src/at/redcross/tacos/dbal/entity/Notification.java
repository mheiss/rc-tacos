package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "Notification")
public class Notification extends EntityImpl {

	private static final long serialVersionUID = 4666281164163377333L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	@Temporal(TemporalType.DATE)
	private Date entryDate;

	@Column(length=4096)
	private String notes;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return notes;
	}

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id).toString();
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
		Notification rhs = (Notification) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public Date getDate() {
		return entryDate;
	}

	public String getNotes() {
		return notes;
	}

}
