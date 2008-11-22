package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines a single disease.
 * 
 * @author Michael
 */
public class Disease {

	private int id;
	private String diseaseName;

	/**
	 * Default class constructor
	 */
	public Disease() {
		id = -1;
		diseaseName = "";
	}

	/**
	 * Default class constructor for a new instance
	 * 
	 * @param diseaseName
	 *            the name of the new disease
	 */
	public Disease(String diseaseName) {
		this();
		this.diseaseName = diseaseName;
	}

	/**
	 * Returns the human readable string for this <code>Disease</code> instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("diseaseName", diseaseName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Disease</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Disease#getId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(27, 47);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Disease</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Disease#getId()}.
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
		Disease disease = (Disease) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, disease.id);
		return builder.isEquals();
	}

	// GETTERS AND SETTERS

	/**
	 * Returns the id of the disease
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the disease
	 * 
	 * @return the diseaseName
	 */
	public String getDiseaseName() {
		return diseaseName;
	}

	/**
	 * Sets the id of the disease
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the name of the disease
	 * 
	 * @param diseaseName
	 *            the diseaseName to set
	 */
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
}
