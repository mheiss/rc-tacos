package at.rc.tacos.client.ui.splash;

import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.StackLayout;
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
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.splash.AbstractSplashHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.custom.FieldEntry;
import at.rc.tacos.client.ui.dialog.ServerManageDialog;
import at.rc.tacos.client.ui.providers.LoginComboLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.client.ui.utils.FontUtils;
import at.rc.tacos.client.ui.utils.MessageUtils;
import at.rc.tacos.client.ui.validators.StringValidator;
import at.rc.tacos.platform.config.ClientConfiguration;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.ServerInfo;
import at.rc.tacos.platform.net.ClientContext;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.xstream.XStream2;

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
	private ComboViewer serverViewer;
	private FieldEntry userEntry;
	private FieldEntry pwdEntry;
	private Button buttonOk, buttonCancel, buttonModify;
	private Hyperlink modifyLink;

	// the login status
	private volatile boolean fAuthenticated;

	// layout options
	private final static int LABEL_WIDTH = 100;
	private final static int INPUT_WIDTH = 120;

	// the client configuration
	private final ClientContext context = UiWrapper.getDefault().getClientContext();

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
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = 25;
		layout.marginLeft = 190;
		loginComposite.setLayout(layout);

		// setup the decorators
		FieldDecoration userDecError = new FieldDecoration(FieldEntry.DECORATOR_ERROR, "Bitte geben Sie ihren Benutzernamen ein");
		FieldDecoration pwdDecError = new FieldDecoration(FieldEntry.DECORATOR_ERROR, "Bitte geben Sie ihr Passwort ein");

		// entry for the username
		userEntry = new FieldEntry(loginComposite, new StringValidator(), userDecError);
		userEntry.setRequired();
		userEntry.setLabel("&Benutzername:");
		userEntry.formatLabel(SWT.NONE, LABEL_WIDTH);
		userEntry.formatText(SWT.NONE, INPUT_WIDTH);
		userEntry.setFocus();

		// entry for the password
		pwdEntry = new FieldEntry(loginComposite, new StringValidator(), pwdDecError);
		pwdEntry.setRequired();
		pwdEntry.setLabel("&Passwort:");
		pwdEntry.formatLabel(SWT.NONE, LABEL_WIDTH);
		pwdEntry.formatText(SWT.NONE, INPUT_WIDTH);

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

		serverViewer = new ComboViewer(new CCombo(serverComposite, SWT.BORDER));
		data = new GridData();
		data.widthHint = INPUT_WIDTH + 5;
		serverViewer.getCCombo().setLayoutData(data);
		serverViewer.getCCombo().setEnabled(false);
		serverViewer.setLabelProvider(new LoginComboLabelProvider());
		serverViewer.setContentProvider(new ArrayContentProvider());
		serverViewer.setInput(context.getClientConfiguration().getServerList());

		// select the server that has the 'default' flag
		for (ServerInfo serverInfo : context.getClientConfiguration().getServerList()) {
			if (!serverInfo.isDefaultServer()) {
				continue;
			}
			// select as default
			serverViewer.setSelection(new StructuredSelection(serverInfo));
			break;
		}

		// the composite for the server selection
		Composite modifyComposite = new Composite(serverComposite, SWT.NONE);
		modifyComposite.setLayout(new GridLayout(3, false));
		data = new GridData();
		data.horizontalSpan = 2;
		modifyComposite.setLayoutData(data);

		Label serverSpacer = new Label(modifyComposite, SWT.NONE);
		data = new GridData();
		data.widthHint = LABEL_WIDTH;
		serverSpacer.setLayoutData(data);

		buttonModify = new Button(modifyComposite, SWT.CHECK);
		buttonModify.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handleModifyButtonSelected();
			}
		});

		modifyLink = new Hyperlink(modifyComposite, SWT.NONE);
		modifyLink.setText("Konfiguration anpassen");
		modifyLink.setUnderlined(false);
		modifyLink.setForeground(CustomColors.COLOR_GREY);
		modifyLink.setEnabled(false);
		modifyLink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				handleModifyHyperlink();
			}
		});

		// create the controls
		Composite controlComposite = new Composite(loginComposite, SWT.NONE);
		FillLayout fillLaoyut = new FillLayout();
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
		fAuthenticated = true;
		// save the user in the current session
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
		if (serverViewer.getSelection().isEmpty()) {
			MessageUtils.showSyncErrorMessage("Server auswählen", "Bitte wählen Sie einen Server aus");
			return;
		}
		IStructuredSelection selection = (IStructuredSelection) serverViewer.getSelection();
		ServerInfo serverInfo = (ServerInfo) selection.getFirstElement();

		stackLayout.topControl = progressComposite;
		client.layout(true);

		// try to open a connection to the selected server
		try {
			InetSocketAddress socketAddr = new InetSocketAddress(serverInfo.getHostName(), serverInfo.getPort());
			NetWrapper.getInstance().openConnection(socketAddr);
		}
		catch (Exception e) {
			loginFailure("Die Verbindung zum Server '" + serverInfo.getHostName() + ":" + serverInfo.getPort() + "' kann nicht hergestellt werden");
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

	private void handleModifyButtonSelected() {
		boolean enable = buttonModify.getSelection();
		if (enable) {
			modifyLink.setEnabled(true);
			serverViewer.getCCombo().setEnabled(true);
			modifyLink.setUnderlined(true);
			modifyLink.setForeground(CustomColors.COLOR_BLUE);
		}
		else {
			modifyLink.setEnabled(false);
			serverViewer.getCCombo().setEnabled(false);
			modifyLink.setUnderlined(false);
			modifyLink.setForeground(CustomColors.COLOR_GREY);
		}
	}

	private void handleModifyHyperlink() {
		// get server list
		ClientConfiguration configuration = context.getClientConfiguration();

		// show the dialog to edit the configuration
		ServerManageDialog dialog = new ServerManageDialog(getSplash(), configuration.getServerList());
		if (dialog.open() == Dialog.OK) {
			List<ServerInfo> updatedList = dialog.getServers();
			configuration.setServerList(updatedList);
			// persist the configuration
			try {
				XStream2 xStream = new XStream2();
				xStream.extToXML(configuration, new FileOutputStream(context.getConfigurationFile()));
			}
			catch (Exception e) {
				log.error("Failed to persist the updated configuration", e);
				MessageUtils.showSyncErrorMessage("Konfigurationsfehler", "Fehler beim speichern der Konfiguration");
			}
		}
		// update the list
		serverViewer.refresh();
		// select the server that has the 'default' flag
		for (ServerInfo serverInfo : context.getClientConfiguration().getServerList()) {
			if (!serverInfo.isDefaultServer()) {
				continue;
			}
			// select as default
			serverViewer.setSelection(new StructuredSelection(serverInfo));
			break;
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
			ExecMessage<Login> loginMessage = new ExecMessage<Login>("doLogin", login);
			Message<Login> loginResult = loginMessage.synchronRequest(NetWrapper.getSession());
			return loginResult.getFirstElement();
		}
	}
}
