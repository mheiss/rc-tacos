package at.rc.taocs.server;

import java.io.File;
import java.net.URL;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.platform.net.ServerContext;
import at.rc.tacos.platform.services.DataSource;
import at.rc.tacos.server.net.MinaMessageServer;
import at.rc.tacos.server.tasks.TaskFactory;
import at.rc.taocs.server.impl.ServerContextImpl;

/**
 * The entry point for the application, initializes and starts the server.
 * 
 * @author Michael
 */
public class ServerApplication implements IApplication {

	// the logger
	private Logger log = LoggerFactory.getLogger(ServerApplication.class);

	// the server context
	private ServerContext serverContext;
	private MinaMessageServer messageServer;
	private TaskFactory taskFactory;

	// lock object
	private volatile Lock lock;

	@Override
	public Object start(IApplicationContext context) throws Exception {
		log.info("Starting up the server application...");
		try {
			URL platformUrl = Platform.getInstanceLocation().getURL();
			File platformFile = new File(platformUrl.toURI());
			File configFile = new File(platformFile, "conf/config.xml");

			// create and setup the server configuration
			ServerContextImpl serverContext = new ServerContextImpl(configFile);
			serverContext.loadConfiguration();

			// initialize and open the data source
			DataSource source = serverContext.getDataSource();
			source.open(serverContext.getServerConfiguration().getDbConfig());

			// initialize and open the message server
			messageServer = new MinaMessageServer();
			messageServer.start(serverContext);

			// start the tasks that should be executed
			taskFactory = new TaskFactory();
			taskFactory.setupTasks(serverContext, messageServer.getAcceptor());
//			taskFactory.scheduleTasks();
		}
		catch (Exception e) {
			log.error("Failed to read and setup the configuration", e);
			return IApplication.EXIT_OK;
		}

		// add a shutdown hook
		addShutdownHook();

		// create a new lock object and wait until the application should
		// terminate
		lock = new ReentrantLock();
		synchronized (lock) {
			lock.wait();
			stop();
		}

		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		log.info("Terminating the server application");
		try {
			// stop all running tasks
			if (taskFactory != null) {
				taskFactory.stopTasks();
			}
			// stop the message server
			if (messageServer != null) {
				log.info("Shutdown the server application");
				messageServer.stop();
			}
			// disconnect the data source
			if (serverContext != null && serverContext.getDataSource() != null) {
				log.info("Closing database connection");
				serverContext.getDataSource().shutdown();
			}
		}
		catch (Exception e) {
			log.error("Error occured during server shutdown", e);
		}
		log.info("Have a nice day :)");
	}

	/**
	 * Adds a shutdown hook to the virtual machine.
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
}
