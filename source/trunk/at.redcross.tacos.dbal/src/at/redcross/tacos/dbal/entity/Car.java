package at.redcross.tacos.dbal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
	private String carName;

	@Column
	private boolean standby;
	
	@Column
	private String notes;
	
	@OneToOne
	private Location location;


	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return carName;
	}

	// ---------------------------------
	// Common helper methods
	// ---------------------------------
	

	// ---------------------------------
	// Object related methods
	// ---------------------------------
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("carName", carName).toString();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(carName).hashCode();
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
	public void setCarName(String carName) {
		this.carName = carName;
	}
	
	public void setStandby(boolean standby) {
		this.standby = standby;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}
	public String getCarName() {
		return carName;
	}

	public boolean isStandby() {
		return standby;
	}

	public String getNotes() {
		return notes;
	}

	public Location getLocation() {
		return location;
	}

}
