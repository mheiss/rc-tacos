package at.rc.tacos.platform.config;

/**
 * The <code>DatabaseConfiguration</code> contanins the database configuration
 * 
 * @author Michael
 */
public class DatabaseConfiguration {

	private String dbDriver;
	private String dbHost;
	private String dbUsername;
	private String dbPassword;

	/**
	 * Default class constructor to create a new instance.
	 */
	public DatabaseConfiguration() {
	}

	// GETTERS AND SETTERS
	public String getDbDriver() {
		return dbDriver;
	}

	public String getDbHost() {
		return dbHost;
	}

	public String getDbUsername() {
		return dbUsername;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
}
