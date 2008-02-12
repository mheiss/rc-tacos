package at.rc.tacos.client.splashHandlers;

//rcp
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.splash.*;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.model.Login;

/**
 * Interactive splash screen handler to login during the
 * splash screen is shown.
 * @since 3.3
 */
public class InteractiveSplashHandler extends AbstractSplashHandler implements PropertyChangeListener
{
	//the components
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

	//the login status
	int loginStatus;

	//position of the composite
	private final static int MARING_LEFT = 180;
	private final static int MARGIN_TOP = 40;

	//the layout
	private final static int F_BUTTON_WIDTH_HINT = 80;
	private final static int F_TEXT_WIDTH_HINT = 175;

	/**
	 *  Default class constructor
	 */
	public InteractiveSplashHandler() 
	{
		//init the fields
		fTextUsername = null;
		fTextPassword = null;
		fButtonOK = null;
		fButtonCancel = null;
		fAuthenticated = false;
		//register the listener
		SessionManager.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * Called to cleanup
	 */
	@Override
	public void dispose() 
	{
		SessionManager.getInstance().removePropertyChangeListener(this);
		super.dispose();
	}

	/**
	 * Initialize the splash window and create the ui.
	 * @param splash the shell to creat the splash screen
	 */
	public void init(final Shell splash) 
	{
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
	 *  The event loop which checks whether the authentication
	 *  was soccessfully
	 */
	private void doEventLoop() 
	{
		Shell splash = getSplash();
		while (!fAuthenticated) 
		{
			if (!splash.getDisplay().readAndDispatch()) 
			{
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
		toggelCheckProgress(false);
		fCompositeLogin.setVisible(false);
		fAuthenticated = true;
		loginStatus = -1;
	}
	
	/**
	 * Fired to indicate that the login is locked and so failed
	 */
	private void loginDenied()
	{
		toggelCheckProgress(false);
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(
				getSplash(),
				"Anmeldung fehlgeschlagen",
				"Ihr Account ist gesperrt, bitte wenden Sie sich an ihren Administrator.");
	}

	/**
	 * Fired to indicate that the login failed
	 */
	private void loginFailure() 
	{
		toggelCheckProgress(false);
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(
				getSplash(),
				"Anmeldung fehlgeschlagen",
		"Bitte überprüfen Sie den angegebenen Benutzernamen und das Passwort");
	}

	/**
	 *  Create the listeners for the controls
	 */
	private void createUIListeners() 
	{
		// Create the cancel button listeners
		fButtonCancel.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(SelectionEvent e) 
			{
				handleButtonCancelWidgetSelected();
			}
		});
		// Create the OK button listeners
		fButtonOK.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e) 
			{
				handleButtonOKWidgetSelected();
			}
		}); 
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
		try
		{
			//create a new login object
			Login login = new Login(username,password,false);
			//send the login request
			NetWrapper.getDefault().sendLoginMessage(login);
			//hide the controls, show the progress
			toggelCheckProgress(true);
		}
		catch(IllegalArgumentException iae)
		{
			Display.getCurrent().beep();
			//show the warning message
			MessageDialog.openInformation(
					getSplash(),
					"Anmeldung",
			"Sie müssen einen Benutzernamen und ein Passwort eingeben um sich anzumelden");
		}
	}

	/**
	 * Set the splash screen image
	 */
	private void configureUISplash() 
	{  
		// Configure layout
		FillLayout layout = new FillLayout(); 
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	/**
	 * Creates the ui controlls
	 */
	private void createUIControllComposite()
	{
		GridData data;
		// Create the composite
		fCompositeLogin = new Composite(getSplash(), SWT.BORDER);
		GridLayout layout = new GridLayout(3, false);
		layout.marginLeft = MARING_LEFT;
		layout.marginTop = MARGIN_TOP;
		fCompositeLogin.setLayout(layout);

		// the label for the username
		fLabelUsername = new Label(fCompositeLogin, SWT.NONE);
		fLabelUsername.setText("&User Name:"); 

		// the text widget for the username
		fTextUsername = new Text(fCompositeLogin, SWT.BORDER);
		// configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextUsername.setLayoutData(data);  

		// the label for the password
		fLabelPassword = new Label(fCompositeLogin, SWT.NONE);
		fLabelPassword.setText("&Password:"); //NON-NLS-1

		// the text widget for the password
		fTextPassword = new Text(fCompositeLogin, SWT.PASSWORD | SWT.BORDER);
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextPassword.setLayoutData(data); 

		//empty label
		Label empty = new Label(fCompositeLogin,SWT.NONE);
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
		fButtonCancel.setText("Cancel"); 
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;   
		data.verticalIndent = 10;
		fButtonCancel.setLayoutData(data);

		//init values
		fTextUsername.setText("user3");
		fTextPassword.setText("P@ssw0rd");
	}

	/**
	 * Creates the ui controlls
	 */
	private void createProgressComposite()
	{       
		GridData data;
		// label for the progress
		progressLabel = new Label(fCompositeLogin,SWT.NONE);
		progressLabel.setText("Überprüfung läuft");
		data = new GridData();
		data.verticalIndent = 20;
		progressLabel.setLayoutData(data);
		progressLabel.setVisible(false);

		//the progress bar
		progressBar = new ProgressBar(fCompositeLogin,SWT.NONE|SWT.INDETERMINATE);
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		data.verticalIndent = 20;
		progressBar.setLayoutData(data);
		progressBar.setVisible(false); 
	}

	private void toggelCheckProgress(boolean state) 
	{
		//hide the controlls and the labels
		fTextPassword.setEnabled(!state);
		fTextUsername.setEnabled(!state);
		fButtonOK.setEnabled(!state);
		// show the progress
		progressLabel.setVisible(state);
		progressBar.setVisible(state);
		//update
		fCompositeLogin.layout();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		//connection to the server was successfully
		if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(evt.getPropertyName()))
		{
			loginStatus = 1;
		}
		if("AUTHENTICATION_FAILED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the login object
			Login login = (Login)evt.getNewValue();
			if(login.isIslocked())
				loginStatus = 2;
			else
				loginStatus = 3;
		}
	}
}
