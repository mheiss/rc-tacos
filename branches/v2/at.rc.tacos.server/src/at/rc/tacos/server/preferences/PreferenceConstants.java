package at.rc.tacos.server.preferences;

/**
 * Constant definitions for plug-in preferences
 */
public class PreferenceConstants 
{
	//Network preferences
	public static final String P_CLIENT_PORT ="clientPortPreference";
	public static final String P_SERVER_PORT = "serverPortPreference";
	public static final String P_FAILOVER_HOST = "failoverHostPreference";
	public static final String P_FAILOVER_CLIENT_PORT = "failoverClientPortPreference";	
	public static final String P_FAILOVER_SERVER_PORT = "failoverServerPortPreference";
	
	//Database preferences
	public static final String P_DB_HOST = "databaseHostPreference";
	public static final String P_DB_USER = "databaseUserPreference";
	public static final String P_DB_PW = "databasePasswordPreference";
	public static final String P_DB_DRIVER = "databaseDriverPreference";
}
