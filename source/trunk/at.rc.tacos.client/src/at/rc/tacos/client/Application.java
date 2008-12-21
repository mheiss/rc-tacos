package at.rc.tacos.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.impl.ClientConfiguration;
import at.rc.tacos.client.impl.ClientContextImpl;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.ApplicationWorkbenchAdvisor;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.platform.model.ServerInfo;
import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.xstream.XStream2;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

	private Logger log = LoggerFactory.getLogger(Application.class);

	/**
	 * Start the application and create the main workbench
	 * 
	 * @param context
	 *            the context used to init the application
	 * @return the exit code of the application
	 */
	public Object start(IApplicationContext context) throws Exception {
		// load the configuration
		ClientConfiguration clientConfig = loadConfiguration();
		// create a default configuration for testing
		if (clientConfig == null) {
			clientConfig = new ClientConfiguration();
			clientConfig.addServer(new ServerInfo("localhost", 4711, "Primärer Server"));
			clientConfig.addServer(new ServerInfo("localhost", 4712, "Backup Server"));
			saveConfiguration(clientConfig);
		}

		// setup the and initialize the network and ui
		ClientContext clientContext = new ClientContextImpl(clientConfig.getServerList());
		NetWrapper.getInstance().init(clientContext);
		UiWrapper.getDefault().init(clientContext);

		// startup the workbench
		Display display = PlatformUI.createDisplay();
		try {
			ApplicationWorkbenchAdvisor adv = new ApplicationWorkbenchAdvisor();
			// create the workbench
			int returnCode = PlatformUI.createAndRunWorkbench(display, adv);
			if (returnCode == PlatformUI.RETURN_RESTART)
				return IApplication.EXIT_RESTART;
			return IApplication.EXIT_OK;
		}
		finally {
			display.dispose();
		}
	}

	/**
	 * Stops the application and close the workbench
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {

			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}

	/**
	 * Helper method that loads the configuration form the persisted file
	 * 
	 * @return the configuration or null in case of an error
	 */
	private ClientConfiguration loadConfiguration() {
		try {
			URL platformUrl = Platform.getInstanceLocation().getURL();
			File platformFile = new File(platformUrl.toURI());
			File configFile = new File(platformFile, "conf/config.xml");
			// check the config file
			if (!configFile.exists()) {
				log.warn("The configuration file 'config.xml' cannot be found in the workspace");
				return null;
			}
			// try to load the configuration
			XStream2 xStream = new XStream2();
			return xStream.extFromXML(new FileInputStream(configFile), ClientConfiguration.class);
		}
		catch (Exception e) {
			log.error("Failed to read and setup the configuration", e);
			return null;
		}
	}

	private void saveConfiguration(ClientConfiguration configuration) {
		try {
			URL platformUrl = Platform.getInstanceLocation().getURL();
			File platformFile = new File(platformUrl.toURI());
			File configFile = new File(platformFile, "conf/config.xml");
			// now save the configuration
			XStream2 xStream = new XStream2();
			xStream.extToXML(configuration, new FileOutputStream(configFile));
		}
		catch (Exception e) {
			log.error("Failed to save and setup the configuration", e);
		}
	}
}
