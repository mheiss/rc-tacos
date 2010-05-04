package at.rc.tacos.client;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.impl.ClientContextImpl;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.ApplicationWorkbenchAdvisor;
import at.rc.tacos.client.ui.UiWrapper;

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
	public Object start(IApplicationContext context) {
		// setup the client context
		try {
			URL platformUrl = Platform.getInstanceLocation().getURL();
			File platformFile = new File(platformUrl.toURI());
			File configFile = new File(platformFile, "conf/config.xml");

			// create and setup the configuration
			ClientContextImpl clientContext = new ClientContextImpl(configFile);
			clientContext.loadConfiguration();

			// setup the and initialize the network and ui
			NetWrapper.getInstance().init(clientContext);
			UiWrapper.getDefault().init(clientContext);
		}
		catch (Exception e) {
			log.error("Failed to read and setup the configuration", e);
			return IApplication.EXIT_OK;
		}

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
}
