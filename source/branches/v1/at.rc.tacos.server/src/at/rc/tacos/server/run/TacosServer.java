package at.rc.tacos.server.run;

import org.apache.log4j.Logger;

import at.rc.tacos.core.db.DataSource;
import at.rc.tacos.core.net.internal.MyServer;
import at.rc.tacos.server.controller.ClientHandler;
import at.rc.tacos.server.controller.ServerController;

/**
 * Tacos server main entry point to startup the application from the command
 * line.
 * 
 * @author Michael
 */
public class TacosServer {
	// the logging instance
	private static Logger logger = Logger.getRootLogger();

	// the server object
	private MyServer myServer = null;

	public TacosServer(int port) {
		ServerController.getDefault();
		// listen for client request
		myServer = new MyServer(port);
		myServer.addNetListener(new ClientHandler());
	}

	/**
	 * Starts the server thread to listen for client requests.
	 */
	private void startServer() {
		// start the server thread to listen to client connections
		Thread t = new Thread(myServer);
		t.start();
	}

	/**
	 * The main method to read the configuration and start the server
	 * 
	 * @param args -
	 *            not used
	 */
	public static void main(String[] args) {
		int port;

		try {
			// the first command line argument is the port
			if (args.length != 1) {
				throw new IllegalArgumentException(
						"The first argument must be the port where the server should listen to");
			}
			port = Integer.parseInt(args[0]);

			// start the server
			logger.info("TACOS-Server Build: 03.11.2008");
			logger.info("Open a connection to the database server");
			// try to get a connection to the database
			if (DataSource.getInstance().getConnection() == null) {
				logger.error("Failed to connect to the database");
				logger.error("Shuting down the server");
				System.exit(1);
			}
			logger.info("Listening for client request at port: " + port);
			TacosServer server = new TacosServer(port);
			server.startServer();

		} catch (NumberFormatException nfe) {
			logger.info("Port number must be a integer");
			logger.info(nfe.getMessage());
		}
	}
}
