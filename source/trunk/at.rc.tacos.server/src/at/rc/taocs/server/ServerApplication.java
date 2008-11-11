package at.rc.taocs.server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.platform.services.ServerContext;
import at.rc.tacos.server.net.MinaMessageServer;
import at.rc.taocs.server.properties.ServerProperties;

/**
 * The entry point for the application, initializes and starts the server.
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
		log.info("Starting up the server application...");
		int port;
		// get the arguments passed to the application
		Map<String, String> argMap = getApplicationArguments(context);
		if (argMap.containsKey(ServerProperties.ARG_SERVER_PORT)) {
			String portArg = (String) argMap.get(ServerProperties.ARG_SERVER_PORT);
			port = Integer.parseInt(portArg);
		}
		else {
			port = DEFAULT_PORT;
		}

		// setup the server context
		serverContext = new ServerContextImpl(port);

		// initialize and open the data source
		try {
			DataSource source = serverContext.getDataSource();
			source.open();
		}
		catch (Exception e) {
			log.error("Failed to open a connection to the database: " + e.getMessage());
			return -1;
		}

		try {
			messageServer = new MinaMessageServer();
			messageServer.start(serverContext);
		}
		catch (Exception e) {
			log.error("Failed to initialize the server: " + e.getMessage());
			return -1;
		}

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
		log.info("Terminating the server application");
		// assert we have a data source
		if (serverContext != null && serverContext.getDataSource() != null) {
			log.info("Closing database connection");
			try {
				// shutdown the database connection
				serverContext.getDataSource().shutdown();
			}
			catch (Exception e) {
				log.error("Failed to close the datbase connection " + e.getMessage(), e);
			}
		}
		// assert we have a server
		if (messageServer != null) {
			log.info("Shutdown the server application");
			try {
				messageServer.stop();
			}
			catch (Exception e) {
				log.error("Failed to shutdown the mina server application", e);
			}
		}
		log.info("Have a nice day :)");
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
	 * Reads and parses the <code>application.args</code> from the
	 * {@linkplain IApplicationContext} and returns a {@linkplain HashMap} that
	 * contains the arguments. The arguments must be passed to the application
	 * as key value pairs (<b>key=value</b>). The key and the value arguments
	 * are converted to lower case in the resulting map.
	 * 
	 * @param context
	 *            the application context containing the map
	 * @return the <code>application.args</code> arguments that are passed to
	 *         the context
	 */
	private Map<String, String> getApplicationArguments(IApplicationContext context) {
		// create and setup a new map
		Map<String, String> appArgs = new HashMap<String, String>();
		// assert we have arguments in the context
		if (!context.getArguments().containsKey("application.args")) {
			return appArgs;
		}

		// loop and try to parse the passed args
		for (String argStr : (String[]) context.getArguments().get("application.args")) {
			// the argument is passed as single string so try to split
			String[] arg = argStr.split("=");
			// check if we have a key and a value
			if (arg.length != 2) {
				log.warn("Cannot parse the application argument '" + argStr + "'. Arguments must be in the form key=value");
				continue;
			}
			String key = arg[0].trim().toLowerCase();
			String value = arg[1].trim().toLowerCase();

			// check that the arguments are valid
			if (key.length() < 1 || value.length() < 1) {
				// ignore this argument
				log.warn("Cannot parse the passed argument '" + argStr + "'. The key or the value is empty");
				continue;
			}
			appArgs.put(key, value);
		}

		return appArgs;
	}
}
