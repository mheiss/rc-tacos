package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Stores information about a specifiy period
 * 
 * @author Birgit
 */
public class Period {

	private int id;
	private String periodName;
	private String serviceTypeCompetence;

	/**
	 * Default class constructor
	 */
	public Period() {
		periodName = "";
		serviceTypeCompetence = "";
	}

	/**
	 * Default class constructor for a complete period object.
	 * 
	 * @param periodName
	 *            the name of the period
	 * @param serviceTypeCompetence
	 *            the name of the serviceTypeCompetence
	 */
	public Period(String periodName, String serviceTypeCompetence) {
		this();
		this.periodName = periodName;
		this.serviceTypeCompetence = serviceTypeCompetence;
	}

	/**
	 * Returns the human readable string for this <code>Period</code> instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("name", periodName);
		builder.append("serviceTypeCompetence", serviceTypeCompetence);
		return builder.toString();

	}

	/**
	 * Returns the generated hashCode of this <code>Period</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Period#getId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(41, 51);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Period</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Period#getId()}.
	 * </p>
	 * 
	 * @return true if the instance is the same otherwise false.
	 */
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
		Period period = (Period) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, period.id);
		return builder.isEquals();
	}

	/**
	 * Returns the name of the period.
	 * 
	 * @return the name of the period
	 */
	public String getPeriodName() {
		return periodName;
	}

	/**
	 * Returns the name of the serviceTypeCompetence
	 * 
	 * @return the serviceTypeCompetencename
	 */
	public String getServiceTypeCompetence() {
		return serviceTypeCompetence;
	}

	/**
	 * Sets the name of the period
	 * 
	 * @param periodName
	 *            the period
	 */
	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	/**
	 * Sets the name of the serviceTypeCompetence
	 * 
	 * @param serviceTypeCompetence
	 *            the serviceTypeCompetencename
	 */
	public void setServiceTypeCompetence(String serviceTypeCompetence) {
		this.serviceTypeCompetence = serviceTypeCompetence;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
