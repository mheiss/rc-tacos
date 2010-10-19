package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Entity
@Table(name = "Car")
public class Car extends EntityImpl {

	private static final long serialVersionUID = 3458978488903275473L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column
	private boolean outOfOrder;

	@Column
	private String notes;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Location location;

	@Column
	private boolean toDelete;

	@Column
	private String type;

	@Column
	private Date registrationDate;

	@OneToOne(optional = true, fetch = FetchType.EAGER)
	private CarDetail detail;

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
		Car rhs = (Car) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setName(String name) {
		this.name = name;
	}

	public void setOutOfOrder(boolean outOfOrder) {
		this.outOfOrder = outOfOrder;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setToDelete(boolean toDelete) {
		this.toDelete = toDelete;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public CarDetail getDetail() {
		return detail;
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

	public boolean isOutOfOrder() {
		return outOfOrder;
	}

	public String getNotes() {
		return notes;
	}

	public Location getLocation() {
		return location;
	}

	public boolean isToDelete() {
		return toDelete;
	}

	public String getType() {
		return type;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setDetail(CarDetail detail) {
		this.detail = detail;
	}
}
