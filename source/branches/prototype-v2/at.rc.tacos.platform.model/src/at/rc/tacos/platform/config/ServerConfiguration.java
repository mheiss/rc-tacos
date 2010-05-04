package at.rc.tacos.platform.config;

/**
 * The <code>ServerConfiguration</code> contains the configuration for the
 * server
 * 
 * @author Michael
 */
public class ServerConfiguration {

	// the general server configuration
	private int serverPort;

	// database configuration
	private DatabaseConfiguration dbConfig;

	/**
	 * Default class constructor to create a new instance
	 */
	public ServerConfiguration() {
	}

	// GETTERS AND SETTERS
	public int getServerPort() {
		return serverPort;
	}

	public DatabaseConfiguration getDbConfig() {
		return dbConfig;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void setDbConfig(DatabaseConfiguration dbConfig) {
		this.dbConfig = dbConfig;
	}
}
