package at.rc.tacos.client.ui.splash;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.custom.FieldEntry;
import at.rc.tacos.client.ui.utils.FontUtils;
import at.rc.tacos.client.ui.utils.MessageUtils;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.message.ExecMessage;

/**
 * Interactive splash screen handler to login during the splash screen is shown.
 */
public class LoginSplashHandler extends AbstractSplashHandler {

	private Logger log = LoggerFactory.getLogger(LoginSplashHandler.class);

	// the components
	private StackLayout stackLayout;
	private Composite client;
	private Composite loginComposite;
	private Composite progressComposite;

	// the input controls
	private CCombo serverCombo;
	private FieldEntry userEntry;
	private FieldEntry pwdEntry;
	private Button buttonOk, buttonCancel;

	// the login status
	private volatile boolean fAuthenticated;

	// layout options
	private final static int LABEL_WIDTH = 100;
	private final static int INPUT_WIDTH = 120;

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
		splash.setLayout(new FillLayout());
		splash.setBackgroundMode(SWT.INHERIT_DEFAULT);

		// Create UI
		createUIControlls(splash);

		// Force the splash screen to layout
		splash.layout(true);

		// Keep the splash visible and block the rcp application to load
		doEventLoop();
	}

	/**
	 * Creates the layout for the login dialog
	 */
	public void createUIControlls(Composite parent) {
		client = new Composite(parent, SWT.BORDER);
		stackLayout = new StackLayout();
		client.setLayout(stackLayout);
		client.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent te) {
				// check for a pressed entry key
				if (te.keyCode == 13) {
					te.doit = false;
					handleButtonOKWidgetSelected();
				}
			}
		});

		// create the composite that holds the login controls
		loginComposite = new Composite(client, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginTop = 10;
		layout.marginLeft = 190;
		loginComposite.setLayout(layout);

		CLabel header = new CLabel(loginComposite, SWT.CENTER);
		header.setText("TACOS-Login");
		header.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		header.setFont(new Font(Display.getCurrent(), "Arial", 14, SWT.BOLD));

		// decorator for the username
		FieldDecoration userDecError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		userDecError.setDescription("Bitte geben Sie ihren Benutzernamen ein");
		// entry for the username
		userEntry = new FieldEntry(loginComposite, new LoginInputValidator(), userDecError);
		userEntry.setRequired();
		userEntry.setLabel("&Benutzername:");
		userEntry.formatLabel(SWT.NONE, LABEL_WIDTH);
		userEntry.formatText(SWT.FILL, INPUT_WIDTH);
		userEntry.setFocus();

		// decorator for the password
		FieldDecoration pwdDecError = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		pwdDecError.setDescription("Bitte geben Sie ihr Passwort ein");
		// entry for the password
		pwdEntry = new FieldEntry(loginComposite, new LoginInputValidator(), pwdDecError);
		pwdEntry.setRequired();
		pwdEntry.setLabel("&Passwort:");
		pwdEntry.formatLabel(SWT.NONE, LABEL_WIDTH);
		pwdEntry.formatText(SWT.FILL, INPUT_WIDTH);

		// server selection
		Composite serverComposite = new Composite(loginComposite, SWT.NONE);
		serverComposite.setLayout(new GridLayout(2, false));

		// server combo
		Label serverLabel = new Label(serverComposite, SWT.NONE);
		serverLabel.setText("Server: ");
		serverLabel.setFont(FontUtils.getBold(serverLabel.getFont()));
		GridData data = new GridData();
		data.widthHint = LABEL_WIDTH;
		serverLabel.setLayoutData(data);

		serverCombo = new CCombo(serverComposite, SWT.BORDER);
		data = new GridData();
		data.widthHint = INPUT_WIDTH + 5;
		serverCombo.setLayoutData(data);

		// create the controls
		Composite controlComposite = new Composite(loginComposite, SWT.NONE);
		FillLayout fillLaoyut = new FillLayout();
		fillLaoyut.marginHeight = 10;
		controlComposite.setLayout(fillLaoyut);
		controlComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// create the button
		buttonOk = new Button(controlComposite, SWT.PUSH);
		buttonOk.setText("OK");
		buttonOk.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonOKWidgetSelected();
			}
		});

		// create the button
		buttonCancel = new Button(controlComposite, SWT.PUSH);
		buttonCancel.setText("Abbrechen");
		buttonCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});

		// create the progress view for the update progress
		progressComposite = new Composite(client, SWT.NONE);
		layout = new GridLayout(1, false);
		layout.marginLeft = 220;
		layout.marginTop = 65;
		progressComposite.setLayout(layout);
		progressComposite.setLayoutData(new GridData(GridData.FILL_BOTH, GridData.FILL_BOTH));

		// label for the progress
		Label progressLabel = new Label(progressComposite, SWT.CENTER);
		progressLabel.setText("Überprüfung läuft....");

		// the progress bar
		ProgressBar progressBar = new ProgressBar(progressComposite, SWT.NONE | SWT.INDETERMINATE);
		progressBar.setFocus();

		// set the controll to be visible on top
		stackLayout.topControl = loginComposite;
	}

	/**
	 * Do the event loop and peek and process messages
	 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (!fAuthenticated) {
			if (!splash.getDisplay().readAndDispatch()) {
				splash.getDisplay().sleep();
			}
		}
	}

	/**
	 * Fired to indicate a successully login
	 * 
	 * @param login
	 *            the login object that was successfull
	 */
	private void loginSuccess(Login login) {
		// fAuthenticated = true;
		MessageUtils.showSyncErrorMessage("Anmeldung erfolgreich", login.getUsername());
		NetWrapper.getSession().setLoggedIn(login);
	}

	/**
	 * Fired to indicate that the login is locked and so failed
	 * 
	 * @param errorMessage
	 *            the error that occured
	 */
	private void loginDenied(String errorMessage) {
		stackLayout.topControl = loginComposite;
		client.layout(true);
		// show the an error message
		Display.getDefault().beep();
		MessageUtils.showSyncErrorMessage("Anmeldung nicht erlaubt", errorMessage);
	}

	/**
	 * Fired to indicate that the login failed
	 * 
	 * @param errorMessage
	 *            the error that occured
	 */
	private void loginFailure(String errorMessage) {
		// change the widget
		stackLayout.topControl = loginComposite;
		client.layout(true);
		// show the an error message
		Display.getDefault().beep();
		MessageUtils.showSyncErrorMessage("Anmeldung fehlgeschlagen", errorMessage);
	}

	/**
	 * Handles the exit button event and terminates the application.
	 */
	private void handleButtonCancelWidgetSelected() {
		Platform.endSplash();
		System.exit(0);
	}

	/**
	 * Handles the login button event and requests the login on the server
	 */
	private void handleButtonOKWidgetSelected() {
		// check if the input is valid
		if (!userEntry.isValid() | !pwdEntry.isValid()) {
			Display.getDefault().beep();
			return;
		}

		stackLayout.topControl = progressComposite;
		client.layout(true);

		// try to open a connection to the selected server
		InetSocketAddress addr = NetWrapper.getInstance().getClientContext().getINetSocketList().get(0);
		try {
			NetWrapper.getInstance().openConnection(addr);
		}
		catch (Exception e) {
			loginFailure("Die Verbindung zum Server '" + addr.getHostName() + "' kann nicht hergestellt werden");
			return;
		}

		// execute the login request
		ExecutorService executor = Executors.newSingleThreadExecutor();
		final String username = userEntry.getText();
		final String password = pwdEntry.getText();
		Future<Login> future = executor.submit(new LoginExecutor(username, password));
		try {
			while (!future.isDone()) {
				if (!getSplash().getDisplay().readAndDispatch()) {
					getSplash().getDisplay().sleep();
				}
			}
			Login login = future.get();
			if (login.isIslocked()) {
				loginDenied(login.getErrorMessage());
				return;
			}
			if (!login.isLoggedIn()) {
				loginFailure(login.getErrorMessage());
				return;
			}
			// seems that the login is ok
			loginSuccess(login);
		}
		catch (Exception e) {
			log.error("Anmeldung fehlgeschlagen", e);
			loginFailure("Die Anmeldung konnte nicht ausgeführt werden");
		}
		// cleanup
		executor.shutdown();
	}

	private class LoginInputValidator implements IInputValidator {

		@Override
		public String isValid(String newText) {
			newText.trim();
			// try to validate the text
			if (newText.isEmpty()) {
				return "Die Eingabe besteht nur aus Leerzeichen";
			}
			return null;
		}
	}

	private class LoginExecutor implements Callable<Login> {

		private String name;
		private String pwd;

		/**
		 * Creates a new instance of the login executor
		 * 
		 * @param name
		 *            the username for the login request
		 * @param pws
		 *            the password to use
		 */
		public LoginExecutor(String name, String pwd) {
			this.name = name;
			this.pwd = pwd;
		}

		@Override
		public Login call() throws Exception {
			// create a new login object
			Login login = new Login(name, pwd, false);
			// send the login request
			ExecMessage<Login> loginMessage = new ExecMessage<Login>("login", login);
			Message<Login> loginResult = loginMessage.synchronRequest(NetWrapper.getSession());
			return loginResult.getFirstElement();
		}
	}
}
