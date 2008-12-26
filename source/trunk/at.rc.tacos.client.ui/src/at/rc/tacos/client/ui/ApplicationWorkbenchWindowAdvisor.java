package at.rc.tacos.client.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.net.message.ExecMessage;

/**
 * This class is used to control the status line, toolbar, title, window size,
 * and other things can be customize.
 * 
 * @author Michael
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	private Logger log = LoggerFactory.getLogger(ApplicationWorkbenchWindowAdvisor.class);

	/**
	 * Creates the application workbench advisor.
	 * 
	 * @param configurer
	 *            the configuring workbench information
	 */
	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/**
	 * Handles close events of the workbench window
	 */
	@Override
	public boolean preWindowShellClose() {
		// confirm quit
		boolean confirm = MessageDialog.openConfirm(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Schlieﬂen best‰tigen",
				"Wollen Sie das Leitstellenprogramm wirklich beenden?");
		if (!confirm)
			return false;

		// send the logout request
		Login currentLogin = NetWrapper.getSession().getLogin();
		ExecMessage<Login> execMessage = new ExecMessage<Login>("doLogout", currentLogin);
		execMessage.asnchronRequest(NetWrapper.getSession());

		log.debug("Sending logout message");
		return true;
	}

	/**
	 * Creates the action bar.
	 * 
	 * @param configurer
	 *            the configuration action bar information
	 * @return the configuration information for a action bar
	 */
	@Override
	public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/**
	 * Called in the constructor of the workbench window
	 */
	@Override
	public void preWindowOpen() {
		// get access to the configuration interface
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1024, 768));
		configurer.setTitle("Time and Coordination System");
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setShowProgressIndicator(true);
		configurer.setShowPerspectiveBar(false);
	}

	/**
	 * Called after the window was created
	 */
	@Override
	public void postWindowCreate() {
		// start the application maximized
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		IWorkbenchWindow window = configurer.getWindow();
		window.getShell().setMaximized(true);
	}
}
