package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The service types for a {@link RosterEntry}
 * 
 * @author Michael
 */
public class ServiceType {

	// properties
	private int id;
	private String serviceName;

	public final static String SERVICETYPE_FREIWILLIG = "Freiwillig";
	public final static String SERVICETYPE_HAUPTAMTLICH = "Hauptamtlich";
	public final static String SERIVCETYPE_ZIVILDIENER = "Zivildiener";

	/**
	 * Default class constructor
	 */
	public ServiceType() {
		id = -1;
		serviceName = "";
	}

	/**
	 * Default class constructor for a service type
	 * 
	 * @param serviceName
	 *            the name of the service
	 */
	public ServiceType(String serviceName) {
		this();
		this.serviceName = serviceName;
	}

	/**
	 * Returns the human readable string for this <code>ServiceType</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("id", id);
		builder.append("serviceName", serviceName);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>ServiceType</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link ServiceType#getId()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(45, 55);
		builder.append(id);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>ServiceType</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link ServiceType#getId()}
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
		ServiceType serviceType = (ServiceType) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(id, serviceType.id);
		return builder.isEquals();
	}

	// GETTERS AND SETTERS
	/**
	 * Returns the internal unique id of the service type.
	 * 
	 * @return the id the id of the database
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of the service
	 * 
	 * @return the service name
	 */
	public String getServiceName() {
		return serviceName;
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
	 * Sets the name of the service
	 * 
	 * @param serviceName
	 *            the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}
