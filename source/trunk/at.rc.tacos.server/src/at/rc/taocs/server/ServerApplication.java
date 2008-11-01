package at.rc.taocs.server;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.ServerContext;
import at.rc.tacos.server.net.MinaMessageServer;
import at.rc.taocs.server.properties.ServerProperties;

/**
 * The entry point for the application, initializes and startsup the server
 * 
 * @author Michael
 */
public class ServerApplication implements IApplication {

	// the default server port to listen
	private final static int DEFAULT_PORT = 4711;

	// the logger
	private Logger log = LoggerFactory.getLogger(ServerApplication.class);

	// the server context
	private ServerContext serverContext;
	private MinaMessageServer messageServer;

	// lock object
	private Object lock;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		int port;
		// check if an alternative port should be used
		if (context.getArguments().containsKey(ServerProperties.ARG_SERVER_PORT)) {
			String portArg = (String) context.getArguments().get(ServerProperties.ARG_SERVER_PORT);
			port = Integer.parseInt(portArg);
		}
		else {
			port = DEFAULT_PORT;
		}

		// setup the server context
		log.info("Setting up the server context");
		serverContext = new ServerContextImpl(port);

		// initialize and open the data source
		log.info("Initialize the datasource");
		try {
			DataSource source = serverContext.getDataSource();
			source.open();
		}
		catch (Exception e) {
			log.error("Failed to open a connection to the database: " + e.getMessage());
			return -1;
		}

		log.info("Starting the server application");
		try {
			messageServer = new MinaMessageServer(serverContext);
			messageServer.listen();
		}
		catch (Exception e) {
			log.error("Failed to initialize the server: " + e.getMessage());
			return -1;
		}

		log.info("Server is ready to handle client request on port " + serverContext.getServerPort());

		// add a shutdown hook
		addShutdownHook();

		// create a new lock object and wait until the application should
		// terminate
		lock = new Object();
		synchronized (lock) {
			lock.wait();
		}

		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		shutdown();
	}

	/**
	 * Add shutdown hook.
	 */
	private void addShutdownHook() {
		// create shutdown hook
		Runnable shutdownHook = new Runnable() {

			public void run() {
				synchronized (lock) {
					lock.notify();
				}
			}
		};

		// add shutdown hook
		Runtime runtime = Runtime.getRuntime();
		runtime.addShutdownHook(new Thread(shutdownHook));
	}

	/**
	 * Helper method to shutdown the server
	 */
	private void shutdown() {
		log.info("Terminating the server");
		// check if we have a server context
		if (serverContext == null)
			return;

		// assert we have a data source
		if (serverContext.getDataSource() == null)
			return;

		log.info("Closing database connection");
		try {
			// shutdown the database connection
			serverContext.getDataSource().shutdown();
		}
		catch (Exception e) {
			log.error("Failed to close the datbase connection " + e.getMessage(), e);
		}

		// assert we have a server
		if (messageServer == null)
			return;
		log.info("Shutdown the server application");
		try {
			messageServer.shutdown();
		}
		catch (Exception e) {
			log.error("Failed to shutdown the mina server application", e);
		}
		log.info("Have a nice day :)");
	}

}
