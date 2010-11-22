package at.redcross.tacos.dbal.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.envers.Audited;

@Entity
@Audited
@Table(name = "Info")
public class Info extends EntityImpl {

	private static final long serialVersionUID = 7340594759209992414L;

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(optional = false,fetch=FetchType.EAGER)
	private Location location;

	@ManyToOne(optional = false,fetch=FetchType.EAGER)
	private Category category;

	@Column
	private String shortName;

	@Column(length=4096)
	private String description;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date displayStartDate;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date displayEndDate;

	@Column
	private boolean toDelete;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
	@Override
	public String getDisplayString() {
		return String.valueOf(id);
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
		Info rhs = (Info) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------
	public void setLocation(Location location) {
		this.location = location;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDisplayStartDate(Date displayStartDate) {
		this.displayStartDate = displayStartDate;
	}

	public void setDisplayEndDate(Date displayEndDate) {
		this.displayEndDate = displayEndDate;
	}

	public void setToDelete(boolean toDelete) {
		this.toDelete = toDelete;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public Location getLocation() {
		return location;
	}

	public String getShortName() {
		return shortName;
	}

	public String getDescription() {
		return description;
	}

	public Date getDisplayStartDate() {
		return displayStartDate;
	}

	public Date getDisplayEndDate() {
		return displayEndDate;
	}

	public Boolean isToDelete() {
		return toDelete;
	}

	public Category getCategory() {
		return category;
	}
}
