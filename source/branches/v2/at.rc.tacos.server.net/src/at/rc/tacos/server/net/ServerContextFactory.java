package at.rc.tacos.server.net;

/**
 * <p><strong>ServerContextFactory</strong> is a factory object that creates
 * and returns new {@link ServerContext} instances, initialized
 * for the processing of the specified connection and response objects.
 * @author Michael
 */
public class ServerContextFactory 
{
	//factory properties
	private static ServerContextFactory instance;

	/**
	 * The shared instance
	 */
	private ServerContextFactory() { }

	/**
	 * Returns the shared instance
	 */
	public static ServerContextFactory getInstance()
	{
		if(instance == null)
			instance = new ServerContextFactory();
		return instance;
	}
	
	/**
	 * Creates and returns a new server context object
	 */
	public ServerContext getServerContext()
	{
		//create a new server context object and set the instance
		ServerContext context = new ServerContext();
		ServerContext.setCurrentInstance(context);
		//return the instance
		return ServerContext.getCurrentInstance();
	}
}
