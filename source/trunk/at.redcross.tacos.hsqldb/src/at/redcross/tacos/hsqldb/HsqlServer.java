package at.redcross.tacos.hsqldb;

import java.io.File;

import org.hsqldb.server.Server;

/**
 * The {@code HsqlServer} provides a convenient way to startup the HSQL database
 * as own process.
 */
public class HsqlServer {

	public void runServer() {
		// prepare the working directory
		File file = new File("./database/tacosDev");
		if (!file.exists()) {
			file.mkdirs();
		}

		// startup HSQL
		Server server = new Server();
		server.setSilent(true);
		server.setNoSystemExit(true);
		server.setDatabaseName(0, "tacosDev");
		server.setDatabasePath(0, file.getAbsolutePath());
		server.start();
	}

	// startup an run the server
	public static void main(String[] args) {
		HsqlServer server = new HsqlServer();
		server.runServer();
	}
}
