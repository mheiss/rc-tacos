/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.splashHandlers;

// rcp
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Login;

/**
 * Interactive splash screen handler to login during the splash screen is shown.
 * 
 * @since 3.3
 */
public class InteractiveSplashHandler extends AbstractSplashHandler implements PropertyChangeListener {

	// the components
	private Text fTextUsername;
	private Text fTextPassword;
	private Label fLabelUsername;
	private Label fLabelPassword;
	private Button fButtonOK;
	private Button fButtonCancel;
	private Label progressLabel;
	private ProgressBar progressBar;
	private boolean fAuthenticated;
	private Composite fCompositeLogin;

	// the login status
	private int loginStatus;
	private Login login;

	// position of the composite
	private final static int MARING_LEFT = 180;
	private final static int MARGIN_TOP = 40;

	// the layout
	private final static int F_BUTTON_WIDTH_HINT = 80;
	private final static int F_TEXT_WIDTH_HINT = 175;

	/**
	 * Default class constructor
	 */
	public InteractiveSplashHandler() {
		// init the fields
		fTextUsername = null;
		fTextPassword = null;
		fButtonOK = null;
		fButtonCancel = null;
		fAuthenticated = false;
		// register the listener
		SessionManager.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * Called to cleanup
	 */
	@Override
	public void dispose() {
		SessionManager.getInstance().removePropertyChangeListener(this);
		super.dispose();
	}

	/**
	 * Initialize the splash window and create the ui.
	 * 
	 * @param splash
	 *            the shell to creat the splash screen
	 */
	@Override
	public void init(final Shell splash) {
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		configureUISplash();
		// Create UI
		createUIControllComposite();
		createProgressComposite();
		// Create UI listeners
		createUIListeners();
		// Force the splash screen to layout
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from
		// loading until the close button is clicked.
		doEventLoop();
	}

	/**
	 * The event loop which checks whether the authentication was soccessfully
	 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (!fAuthenticated) {
			if (!splash.getDisplay().readAndDispatch()) {
				// check the status
				if (loginStatus == 1)
					loginSuccess();
				else if (loginStatus == 2)
					loginDenied();
				else if (loginStatus == 3)
					loginFailure();
				// sleep
				splash.getDisplay().sleep();
			}
		}
	}

	/**
	 * Fired to indicate a successully login
	 */
	private void loginSuccess() {
		toggelCheckProgress(false);
		fCompositeLogin.setVisible(false);
		fAuthenticated = true;
		loginStatus = -1;
	}

	/**
	 * Fired to indicate that the login is locked and so failed
	 */
	private void loginDenied() {
		toggelCheckProgress(false);
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(getSplash(), "Anmeldung fehlgeschlagen", login.getErrorMessage());
	}

	/**
	 * Fired to indicate that the login failed
	 */
	private void loginFailure() {
		toggelCheckProgress(false);
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(getSplash(), "Anmeldung fehlgeschlagen", login.getErrorMessage());
	}

	/**
	 * Create the listeners for the controls
	 */
	private void createUIListeners() {
		// Create the cancel button listeners
		fButtonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});
		// Create the OK button listeners
		fButtonOK.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonOKWidgetSelected();
			}
		});
	}

	/**
	 * Method to hanle the cancel button. The application will shutdown.
	 */
	private void handleButtonCancelWidgetSelected() {
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);
	}

	/**
	 * Method to handle the ok button
	 */
	private void handleButtonOKWidgetSelected() {
		// get the login values
		final String username = fTextUsername.getText();
		final String password = fTextPassword.getText();
		// try to login
		try {
			// create a new login object
			Login login = new Login(username, password, false);
			// send the login request
			NetWrapper.getDefault().sendLoginMessage(login);
			// hide the controls, show the progress
			toggelCheckProgress(true);
		}
		catch (IllegalArgumentException iae) {
			Display.getCurrent().beep();
			// show the warning message
			MessageDialog.openInformation(getSplash(), "Anmeldung", "Sie m�ssen einen Benutzernamen und ein Passwort eingeben um sich anzumelden");
		}
	}

	/**
	 * Set the splash screen image
	 */
	private void configureUISplash() {
		// Configure layout
		FillLayout layout = new FillLayout();
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	/**
	 * Creates the ui controlls
	 */
	private void createUIControllComposite() {
		GridData data;
		// Create the composite
		fCompositeLogin = new Composite(getSplash(), SWT.BORDER);
		fCompositeLogin.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent te) {
				// check for a pressed entry key
				if (te.keyCode == 13) {
					te.doit = false;
					handleButtonOKWidgetSelected();
				}
			}

		});
		GridLayout layout = new GridLayout(3, false);
		layout.marginLeft = MARING_LEFT;
		layout.marginTop = MARGIN_TOP;
		fCompositeLogin.setLayout(layout);

		// the label for the username
		fLabelUsername = new Label(fCompositeLogin, SWT.NONE);
		fLabelUsername.setText("&Benutzername:");
		// the text widget for the username
		fTextUsername = new Text(fCompositeLogin, SWT.BORDER);

		// configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextUsername.setLayoutData(data);
		fTextUsername.setFocus();
		// the label for the password
		fLabelPassword = new Label(fCompositeLogin, SWT.NONE);
		fLabelPassword.setText("&Passwort:"); // NON-NLS-1

		// the text widget for the password
		fTextPassword = new Text(fCompositeLogin, SWT.PASSWORD | SWT.BORDER);
		fTextPassword.addFocusListener(new FocusAdapter() {

			@Override
			public void focusGained(FocusEvent e) {
				fTextPassword.setSelection(0, fTextPassword.getText().length());
			}
		});
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextPassword.setLayoutData(data);

		// empty label
		Label empty = new Label(fCompositeLogin, SWT.NONE);
		empty.setText("");

		// Create the button
		fButtonOK = new Button(fCompositeLogin, SWT.PUSH);
		fButtonOK.setText("OK");
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;
		data.verticalIndent = 10;
		fButtonOK.setLayoutData(data);

		// Create the button
		fButtonCancel = new Button(fCompositeLogin, SWT.PUSH);
		fButtonCancel.setText("Abbrechen");
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;
		data.verticalIndent = 10;
		fButtonCancel.setLayoutData(data);
	}

	/**
	 * Creates the ui controlls
	 */
	private void createProgressComposite() {
		GridData data;
		// label for the progress
		progressLabel = new Label(fCompositeLogin, SWT.NONE);
		progressLabel.setText("�berpr�fung l�uft");
		data = new GridData();
		data.verticalIndent = 20;
		progressLabel.setLayoutData(data);
		progressLabel.setVisible(false);

		// the progress bar
		progressBar = new ProgressBar(fCompositeLogin, SWT.NONE | SWT.INDETERMINATE);
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		data.verticalIndent = 20;
		progressBar.setLayoutData(data);
		progressBar.setVisible(false);
	}

	private void toggelCheckProgress(boolean state) {
		// hide the controlls and the labels
		fTextPassword.setEnabled(!state);
		fTextUsername.setEnabled(!state);
		fButtonOK.setEnabled(!state);
		// show the progress
		progressLabel.setVisible(state);
		progressBar.setVisible(state);
		// update
		fCompositeLogin.layout();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// connection to the server was successfully
		if ("AUTHENTICATION_SUCCESS".equalsIgnoreCase(evt.getPropertyName())) {
			loginStatus = 1;
		}
		if ("AUTHENTICATION_FAILED".equalsIgnoreCase(evt.getPropertyName())) {
			// get the login object
			login = (Login) evt.getNewValue();
			if (login.isIslocked())
				loginStatus = 2;
			else
				loginStatus = 3;
		}
	}
}
