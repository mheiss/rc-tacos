package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "Equipment")
public class Equipment extends EntityImpl {

	private static final long serialVersionUID = 9142841798539867173L;

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String name;

	@Column
	private int inventoryNumber;

	@Column
	private int actualNumber;

	@Column
	private int theoreticalNumber;

	@Column(length = 1024)
	private String notes;

	@Column
	private Date expirationDate;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	private Location location;

	@Column
	private boolean toDelete;

	@Column
	private String type;

	@Column
	private Date registrationDate;

	@Column
	private Date lastChangeDate;

	@Column
	private Date lastInventoryDate;

	@Column
	private Date inspectionDate;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public Object getOid() {
		return id;
	}

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
		Equipment rhs = (Equipment) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setName(String name) {
		this.name = name;
	}

	public void setInventoryNumber(int inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public void setActualNumber(int actualNumber) {
		this.actualNumber = actualNumber;
	}

	public void setTheoreticalNumber(int theoreticalNumber) {
		this.theoreticalNumber = theoreticalNumber;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
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

	public void setLastChangeDate(Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	public void setLastInventoryDate(Date lastInventoryDate) {
		this.lastInventoryDate = lastInventoryDate;
	}

	public void setInspectionDate(Date inspectionDate) {
		this.inspectionDate = inspectionDate;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public String getName() {
		return name;
	}

	public int getInventoryNumber() {
		return inventoryNumber;
	}

	public int getActualNumber() {
		return actualNumber;
	}

	public int getTheoreticalNumber() {
		return theoreticalNumber;
	}

	public String getNotes() {
		return notes;
	}

	public Date getExpirationDate() {
		return expirationDate;
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

	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	public Date getLastInventoryDate() {
		return lastInventoryDate;
	}

	public Date getInspectionDate() {
		return inspectionDate;
	}
}
