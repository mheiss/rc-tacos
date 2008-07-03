package at.rc.tacos.server.net.internal.jobs;

/**
 * The TacosServerJob interface definition that provides the name of the running jobs
 * @author Michael
 */
public interface TSJ
{
	//INTERACTION CLIENT -> SERVER
	/**
	 * The server listen job that waits for new client connections
	 */
	public final static String CLIENT_LISTEN_JOB = "clientListenJob";
	
	/**
	 * The request job handles the client request
	 */
	public final static String CLIENT_REQUEST_JOB = "clientRequestJob";
	
	//INTERACTION SERVER -> SERVER
	/**
	 * The server listen job waits for new server connections
	 */
	public final static String SERVER_LISTEN_JOB = "serverListenJob";
	
	/**
	 * The server process job handles requests from servers
	 */
	public final static String SERVER_REQUEST_JOB = "serverRequestJob";
}
