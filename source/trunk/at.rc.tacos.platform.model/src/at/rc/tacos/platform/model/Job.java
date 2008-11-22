package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Defins a single job for a {@link StaffMember}
 * 
 * @author Michael
 */
public class Job {

	private int id;
	private String jobName;

	public static final String JOB_LEITSTELLENDISPONENT = "Leitstellendisponent";
	public static final String JOB_FAHRER = "Fahrer";
	public static final String JOB_SANITAETER = "Sanitäter";

	/**
	 * Default class constructor
	 */
	public Job() {
		id = -1;
		jobName = "";
	}

	/**
	 * Default class constructor for new job
	 * 
	 * @param jobName
	 *            the name of the new job
	 */
	public Job(String jobName) {
		this.jobName = jobName;
	}

	/**
	 * Returns the human readable string for this <code>Job</code> instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("name", jobName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>Job</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link Job#getId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(29, 49);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>Job</code> instance is equal to the
	 * compared object.
	 * <p>
	 * The compared fields are {@link Job#getId()}.
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
		Job job = (Job) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, job.id);
		return builder.isEquals();
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the internal unique id of the job.
	 * 
	 * @return the id the id of the database
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the job
	 * 
	 * @return the name of the job
	 */
	public String getJobName() {
		return jobName;
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
	 * Sets the name of the job
	 * 
	 * @param jobName
	 *            the jobName to set
	 */
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
}
