package at.rc.tacos.model;

import at.rc.tacos.common.AbstractMessage;

/**
 * The helo message for the communication between the servers
 * @author Michael
 */
public class Helo extends AbstractMessage
{
	//the id of the string
	public final static String ID = "at.rc.tacos.model.Helo";
	
	//properties
	private String serverIp;
	private int serverPort;
	private boolean serverPrimary;
	private boolean redirect;
	
	//CONSTRUCTORS
	/**
	 * Default class constructor
	 */
	public Helo()
	{
		super(ID);
	}

	//HASHCODE AND EQUALS
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((serverIp == null) ? 0 : serverIp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Helo other = (Helo) obj;
		if (serverIp == null) {
			if (other.serverIp != null)
				return false;
		} else if (!serverIp.equals(other.serverIp))
			return false;
		return true;
	}

	
	//GETTERS AND SETTERS
	/**
	 * @return the serverIp
	 */
	public String getServerIp() {
		return serverIp;
	}

	/**
	 * @param serverIp the serverIp to set
	 */
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the redirect
	 */
	public boolean isRedirect() {
		return redirect;
	}

	/**
	 * @param redirect the redirect to set
	 */
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	
	/**
	 * @return the serverPrimary
	 */
	public boolean isServerPrimary() {
		return serverPrimary;
	}

	/**
	 * @param serverPrimary the serverPrimary to set
	 */
	public void setServerPrimary(boolean serverPrimary) {
		this.serverPrimary = serverPrimary;
	}
}
