package at.rc.tacos.platform.config;

import java.util.ArrayList;
import java.util.List;

import at.rc.tacos.platform.model.ServerInfo;

/**
 * The <code>ClientConfiguration</code> contains the configuration for the
 * client.
 * 
 * @author Michael
 */
public class ClientConfiguration {

	// the servers to connect to
	private List<ServerInfo> serverList;

	/**
	 * Default class constructor to setup a new instance.
	 */
	public ClientConfiguration() {
		this.serverList = new ArrayList<ServerInfo>();
	}

	/**
	 * Adds a new <code>ServerInfo</code> to this configuration.
	 * 
	 * @param serverInfo
	 *            the new server config to add
	 */
	public void addServer(ServerInfo serverInfo) {
		serverList.add(serverInfo);
	}

	// GETTERS AND SETTERS
	public void setServerList(List<ServerInfo> serverList) {
		this.serverList = serverList;
	}

	public List<ServerInfo> getServerList() {
		return serverList;
	}
}
