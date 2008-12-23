package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defines a competence of a {@link StaffMember}.
 * 
 * @author Michael
 */
public class Competence extends Lockable {

	private int id;
	private String competenceName;

	public static final String FUNCTION_HA = "_HA";
	public static final String FUNCTION_ZD = "_ZD";
	public static final String FUNCTION_LS = "_LS";
	public static final String COMPETENCE_NAME_VOLUNTEER = "Volontär";

	/**
	 * Default class constructor
	 */
	public Competence() {
		competenceName = "";
	}

	/**
	 * Default class constructor for a competence
	 * 
	 * @param competenceName
	 *            the name of the competence to create
	 */
	public Competence(String competenceName) {
		this.competenceName = competenceName;
	}

	// METHODS
	/**
	 * Returns the human readable string for this <code>Competence</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("competenceName", competenceName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Competence</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Competence#getId()}.
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(21, 41);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Competence</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link Competence#getId()}
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
		Competence competence = (Competence) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, competence.id);
		return builder.isEquals();
	}

	// LOCKABLE IMPLEMENTATION
	@Override
	public int getLockedId() {
		return id;
	}

	@Override
	public Class<?> getLockedClass() {
		return Competence.class;
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the internal unique id of the competence.
	 * 
	 * @return the id the id of the database
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the competence
	 * 
	 * @return the name of the competence
	 */
	public String getCompetenceName() {
		return competenceName;
	}

	/**
	 * Sets the unique number of the job.
	 * 
	 * @param id
	 *            the unique id for the job
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Sets the name of the competence
	 * 
	 * @param competenceName
	 *            the competenceName to set
	 */
	public void setCompetenceName(String competenceName) {
		this.competenceName = competenceName;
	}
}
