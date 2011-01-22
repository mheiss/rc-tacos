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
@Table(name = "CarCareEntry")
public class CarCareEntry extends EntityImpl {

	private static final long serialVersionUID = 2274945878385792887L;

	@Id
	@GeneratedValue
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Car car;

	@Column
	private String type;

	@Column
	private String description;

	@Column
	private String status;

	@Column
	private String doneFrom;

	@Column
	private Date executedOn;

	// ---------------------------------
	// EntityImpl
	// ---------------------------------
    @Override
    public Object getOid() {
        return id;
    }
	
	@Override
	public String getDisplayString() {
		return description;
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
		CarCareEntry rhs = (CarCareEntry) obj;
		return new EqualsBuilder().append(id, rhs.id).isEquals();
	}

	// ---------------------------------
	// Setters for the properties
	// ---------------------------------

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDoneFrom(String doneFrom) {
		this.doneFrom = doneFrom;
	}

	public void setExecutedOn(Date executedOn) {
		this.executedOn = executedOn;
	}

	// ---------------------------------
	// Getters for the properties
	// ---------------------------------
	public long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Car getCar() {
		return car;
	}

	public String getType() {
		return type;
	}

	public String getStatus() {
		return status;
	}

	public String getDoneFrom() {
		return doneFrom;
	}

	public Date getExecutedOn() {
		return executedOn;
	}
}
