package at.rc.tacos.platform.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * The <code>ServerInfo</code> describes a server connection
 * 
 * @author Michael
 */
public class ServerInfo {

	private int port;
	private String hostName;
	private String description;
	private boolean defaultServer;

	/**
	 * Default class constructor to setup a new instance
	 * 
	 * @param hostName
	 *            the name of the remote host
	 * @param port
	 *            the port to connect
	 * @param description
	 *            the description of the connection
	 */
	public ServerInfo(String hostName, int port, String description) {
		this.hostName = hostName;
		this.port = port;
		this.description = description;
	}

	/**
	 * Returns the human readable string for this <code>ServerInfo</code>
	 * instance.
	 * 
	 * @return the build string
	 */
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this);
		builder.append("hostname", hostName);
		builder.append("port", port);
		builder.append("description", description);
		return builder.toString();
	}

	/**
	 * Returns the generated hashCode of this <code>ServerInfo</code> instance.
	 * <p>
	 * The hashCode is based uppon the {@link ServerInfo#getHostName()} and
	 * {@link ServerInfo#getPort()}
	 * </p>
	 * 
	 * @return the generated hash code
	 */
	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder(57, 67);
		builder.append(hostName);
		builder.append(port);
		return builder.toHashCode();
	}

	/**
	 * Returns wheter or not this <code>ServerInfo</code> instance is equal to
	 * the compared object.
	 * <p>
	 * The compared fields are {@link ServerInfo#getHostName()} and
	 * {@link ServerInfo#getPort()}
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
		ServerInfo serverInfo = (ServerInfo) obj;
		EqualsBuilder builder = new EqualsBuilder();
		builder.append(hostName, serverInfo.hostName);
		builder.append(port, serverInfo.port);
		return builder.isEquals();
	}

	/**
	 * Sets this server entry as the default entry that should be preselected
	 * and preferred.
	 * 
	 * @param defaultServer
	 *            true to set this server as default
	 */
	public void setDefaultServer(boolean defaultServer) {
		this.defaultServer = defaultServer;
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return port;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * Returns whether this server entry is the default entry.
	 * 
	 * @return true if this entry is the default, otherwise false
	 */
	public boolean isDefaultServer() {
		return defaultServer;
	}
}
