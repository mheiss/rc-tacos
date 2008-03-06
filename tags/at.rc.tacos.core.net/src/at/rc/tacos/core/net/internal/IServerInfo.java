package at.rc.tacos.core.net.internal;

/**
 * Interface definition for the identification of the servers
 * @author Michael
 */
public interface IServerInfo 
{
	/**
	 * Identification to use for the primary server
	 */
	public final static String PRIMARY_SERVER = "server.primary";
	
	/**
	 * Identification to use for the failover server
	 */
	public final static String FAILOVER_SERVER = "server.failover";
}
