package at.rc.tacos.client.splashHandlers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
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
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.platform.model.Login;

/**
 * Interactive splash screen handler to login during the
 * splash screen is shown.
 * @since 3.3
 */
public class InteractiveSplashHandler extends AbstractSplashHandler implements PropertyChangeListener
{
	//the components
	private StackLayout stackLayout;
	private Composite client;
	private Composite loginComposite;
	private Composite progressComposite;
	
	private Text fTextUsername;
	private Text fTextPassword;
	
	//the login status
	private int loginStatus;
	private Login login;
	private boolean fAuthenticated;
	
	/**
	 *  Default class constructor
	 */
	public InteractiveSplashHandler() {
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
	 * @param splash the shell to creat the splash screen
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
	public void createUIControlls(Composite parent)
	{
		client = new Composite(parent,SWT.BORDER);
		stackLayout = new StackLayout();
		client.setLayout(stackLayout);
		client.addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent te) {
				//check for a pressed entry key
				if(te.keyCode == 13) {
					te.doit = false;
					handleButtonOKWidgetSelected();
				}
			}
		});
		
		//create the composite that holds the login controls
		loginComposite = new Composite(client,SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginLeft = 180;
		layout.marginTop = 40;
		loginComposite.setLayout(layout);

		// the label for the username
		Label fLabelUsername = new Label(loginComposite, SWT.NONE);
		fLabelUsername.setText("&Benutzername:"); 
		// the text widget for the username
		fTextUsername = new Text(loginComposite, SWT.BORDER);
		
		// configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = 80;
		data.horizontalSpan = 2;
		fTextUsername.setLayoutData(data);  
		fTextUsername.setFocus();
		// the label for the password
		Label fLabelPassword = new Label(loginComposite, SWT.NONE);
		fLabelPassword.setText("&Passwort:"); //NON-NLS-1

		// the text widget for the password
		fTextPassword = new Text(loginComposite, SWT.PASSWORD | SWT.BORDER);
		fTextPassword.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent e) 
			{
				fTextPassword.setSelection(0, fTextPassword.getText().length());
			}
		});
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = 175;
		data.horizontalSpan = 2;
		fTextPassword.setLayoutData(data); 

		//empty label
		Label empty = new Label(loginComposite,SWT.NONE);
		empty.setText("");

		// Create the button
		Button fButtonOK = new Button(loginComposite, SWT.PUSH);
		fButtonOK.setText("OK"); 
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = 80;
		data.verticalIndent = 10;
		fButtonOK.setLayoutData(data); 
		fButtonOK.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonOKWidgetSelected();
			}
		}); 

		// Create the button
		Button fButtonCancel = new Button(loginComposite, SWT.PUSH);
		fButtonCancel.setText("Abbrechen"); 
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = 80;   
		data.verticalIndent = 10;
		fButtonCancel.setLayoutData(data);
		fButtonCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});

		//create the progress view for the update progress
		progressComposite = new Composite(client,SWT.NONE);
		layout = new GridLayout(1,false);
		layout.marginLeft = 220;
		layout.marginTop = 65;
		progressComposite.setLayout(layout);
		progressComposite.setLayoutData(new GridData(GridData.FILL_BOTH,GridData.FILL_BOTH));

		// label for the progress
		Label progressLabel = new Label(progressComposite,SWT.CENTER);
		progressLabel.setText("Überprüfung läuft....");

		//the progress bar
		ProgressBar progressBar = new ProgressBar(progressComposite,SWT.NONE | SWT.INDETERMINATE);
		progressBar.setFocus();
		
		//set the controll to be visible on top
		stackLayout.topControl = loginComposite;
	}

	/**
	 *  The event loop which checks whether the authentication
	 *  was soccessfully
	 */
	private void doEventLoop() 
	{
		Shell splash = getSplash();
		while (!fAuthenticated) {
			if (!splash.getDisplay().readAndDispatch()) {
				//check the status
				if( loginStatus == 1 )
					loginSuccess();
				else if(loginStatus == 2 ) 
					loginDenied();
				else if(loginStatus == 3)
					loginFailure();  
				//sleep
				splash.getDisplay().sleep();
			}
		}
	}

	/**
	 * Fired to indicate a successully login
	 */
	private void loginSuccess() 
	{
		stackLayout.topControl = null;
		client.layout(true);
		fAuthenticated = true;
		loginStatus = -1;
	}

	/**
	 * Fired to indicate that the login is locked and so failed
	 */
	private void loginDenied()
	{
		//change the widget
		stackLayout.topControl = loginComposite;
		client.layout(true);
		//show the an error message
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(
				getSplash(),
				"Anmeldung nicht erlaubt",
				login.getErrorMessage());
	}

	/**
	 * Fired to indicate that the login failed
	 */
	private void loginFailure() 
	{
		//change the widget
		stackLayout.topControl = loginComposite;
		client.layout(true);
		//show the an error message
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(
				getSplash(),
				"Anmeldung fehlgeschlagen",
				login.getErrorMessage());
	}

	/**
	 * Method to hanle the cancel button.
	 * The application will shutdown.
	 */
	private void handleButtonCancelWidgetSelected() 
	{
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);     
	}

	/**
	 * Method to handle the ok button
	 */
	private void handleButtonOKWidgetSelected() 
	{
		//get the login values
		final String username = fTextUsername.getText();
		final String password = fTextPassword.getText();
		//try to login
		try {
			//create a new login object
			Login login = new Login(username,password,false);
			//send the login request
			NetWrapper.getDefault().sendLoginMessage(login);
			//show the progress composite
			stackLayout.topControl = progressComposite;
			client.layout(true);
		}
		catch(IllegalArgumentException iae)
		{
			Display.getCurrent().beep();
			//show the warning message
			MessageDialog.openInformation(getSplash(),"Anmeldung",
			"Sie müssen einen Benutzernamen und ein Passwort eingeben um sich anzumelden");
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		//connection to the server was successfully
		if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(evt.getPropertyName())) {
			loginStatus = 1;
		}
		if("AUTHENTICATION_FAILED".equalsIgnoreCase(evt.getPropertyName())) {
			//get the login object
			login = (Login)evt.getNewValue();
			if(login.isIslocked())
				loginStatus = 2;
			else
				loginStatus = 3;
		}
	}
}
