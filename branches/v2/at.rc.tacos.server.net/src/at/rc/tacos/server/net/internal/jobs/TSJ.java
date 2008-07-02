package at.rc.tacos.server.net.internal.jobs;

/**
 * The TacosServerJob interface definition that provides the name of the running jobs
 * @author Michael
 */
public interface TSJ
{
	/**
	 * The server listen job that waits for new client connections
	 */
	public final static String SERVER_LISTEN_JOB = "serverListenJob";
	
	/**
	 * The listening job that waits for new data
	 */
	public final static String CLIENT_LISTEN_JOB = "clientListenJob";
}
