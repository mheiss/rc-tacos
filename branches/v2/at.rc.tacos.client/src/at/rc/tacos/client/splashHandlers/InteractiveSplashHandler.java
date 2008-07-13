package at.rc.tacos.client.splashHandlers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.splash.*;

import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetSource;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.ServerInfo;

/**
 * Interactive splash screen handler to login during the
 * splash screen is shown.
 * @since 3.3
 */
public class InteractiveSplashHandler extends AbstractSplashHandler implements PropertyChangeListener
{
	//the components
	private Text textUsername;
	private Text textPassword;
	private ComboViewer comboViewer;
	private Button buttonOK;
	private Button buttonCancel;
	private ProgressBar progressBar;

	//the composite to draw on
	private Composite compositeLogin;
	private Composite compositeFormular;
	private Composite compositeProgress;

	//the login status
	private boolean fAuthenticated;
	private int loginStatus;
	private Login login;
	
	//the typed login information
	private String username,password;

	//the layout
	private final static int F_BUTTON_WIDTH_HINT = 80;
	private final static int F_TEXT_WIDTH_HINT = 160;

	/**
	 *  Default class constructor
	 */
	public InteractiveSplashHandler() 
	{
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
	@Override
	public void init(final Shell splash) 
	{
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		getSplash().setLayout(new FillLayout());
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);

		//setup the base composite for the splash
		compositeLogin = new Composite(getSplash(), SWT.BORDER);
		compositeLogin.addTraverseListener(new TraverseListener()
		{
			@Override
			public void keyTraversed(TraverseEvent te) 
			{
				//check for a pressed entry key
				if(te.keyCode == 13)
				{
					te.doit = false;
					handleButtonOKWidgetSelected();
				}
			}
		});

		//setup the layout for the login screen
		GridLayout layout = new GridLayout(1, false);
		layout.marginTop = 10;
		layout.marginLeft = 180;
		compositeLogin.setLayout(layout);
		compositeLogin.setLayoutData(new GridData(GridData.FILL_BOTH));

		//header label
		Label headerLabel = new Label(compositeLogin,SWT.CENTER);
		headerLabel.setText("TACOS-Login");
		headerLabel.setFont(CustomColors.HEADER_FONT);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		headerLabel.setLayoutData(data);

		// Create UI formular
		createLoginForm();	

		// Force a redraw
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from loading
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
		fAuthenticated = true;
		loginStatus = -1;
	}

	/**
	 * Fired to indicate that the login is locked and so failed
	 */
	private void loginDenied()
	{
		//show the form
		toggelFormProgress(false);
		//display the result
		loginStatus = -1;
		Display.getCurrent().beep();
		MessageDialog.openError(
				getSplash(),
				"Anmeldung fehlgeschlagen",
				login.getErrorMessage());
	}

	/**
	 * Fired to indicate that the login failed
	 */
	private void loginFailure() 
	{
		//show the form
		toggelFormProgress(false);

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
		username = textUsername.getText();
		password = textPassword.getText();
		final ServerInfo info = (ServerInfo)((IStructuredSelection)comboViewer.getSelection()).getFirstElement();

		//show the progress bar
		toggelFormProgress(true);

		//try to open a connection
		if(NetSource.getInstance().openConnection(info) == null)
		{
			Display.getDefault().beep();
			MessageDialog.openQuestion(Display.getDefault().getActiveShell(), 
					"Verbindungsfehler", 
					"Verbindung zum "+ info.getDescription() + " nicht möglich.\n");
			//show the form again
			toggelFormProgress(false);
			return;
		}
		
		//init the network
		NetWrapper.getDefault().init();

		//try to login
		try
		{
			//create a new login object
			Login login = new Login(username,password,false);
			//send the login request
			NetWrapper.getDefault().sendLoginMessage(login);
		}
		catch(IllegalArgumentException iae)
		{
			Display.getDefault().beep();
			//show the warning message
			MessageDialog.openInformation(
					getSplash(),
					"Anmeldung",
			"Sie müssen einen Benutzernamen und ein Passwort eingeben um sich anzumelden");
			//show the form again
			toggelFormProgress(false);
		}
	}

	/**
	 * Creates the ui controlls
	 */
	private void createLoginForm()
	{
		compositeFormular = new Composite(compositeLogin,SWT.NONE);
		GridLayout loginFormLayout = new GridLayout(2,false);
		loginFormLayout.marginTop = 20;
		compositeFormular.setLayout(loginFormLayout);

		// the label for the username
		Label labelUsername = new Label(compositeFormular, SWT.NONE);
		labelUsername.setText("&Benutzername: "); 
		//the textfield for the username
		textUsername = new Text(compositeFormular, SWT.BORDER);
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		textUsername.setLayoutData(data);  
		textUsername.setFocus();

		// the label for the password
		Label labelPassword = new Label(compositeFormular, SWT.NONE);
		labelPassword.setText("&Passwort: "); 
		// the text widget for the password
		textPassword = new Text(compositeFormular, SWT.PASSWORD | SWT.BORDER);
		textPassword.addFocusListener(new FocusAdapter()
		{
			@Override
			public void focusGained(FocusEvent e) 
			{
				textPassword.setSelection(0, textPassword.getText().length());
			}
		});
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		textPassword.setLayoutData(data); 

		//create the server label
		Label labelServer = new Label(compositeFormular,SWT.NONE);
		labelServer.setText("&Server: ");
		comboViewer = new ComboViewer(compositeFormular,SWT.READ_ONLY);
		comboViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public Object[] getElements(Object arg0) {
				return NetSource.getInstance().getServerList().toArray();
			}
			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		comboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				//cast to a server info object
				ServerInfo info = (ServerInfo)element;
				return info.getDescription();
			}
		});
		comboViewer.setInput(NetSource.getInstance().getServerList());
		comboViewer.getCombo().select(0);
		data = new GridData();
		data.widthHint = F_TEXT_WIDTH_HINT -15;
		comboViewer.getCombo().setLayoutData(data);

		// Create the button
		buttonOK = new Button(compositeFormular, SWT.PUSH);
		buttonOK.setText("OK"); 
		buttonOK.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				handleButtonOKWidgetSelected();

			}
		});
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;
		data.verticalIndent = 10;
		buttonOK.setLayoutData(data); 

		// Create the button
		buttonCancel = new Button(compositeFormular, SWT.PUSH);
		buttonCancel.setText("Abbrechen"); 
		buttonCancel.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				handleButtonCancelWidgetSelected();
			}
		}); 
		// Configure layout data
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;   
		data.verticalIndent = 10;
		buttonCancel.setLayoutData(data);
	}

	/**
	 * Creates the ui controlls
	 */
	private void createProgressComposite()
	{       
		compositeProgress = new Composite(compositeLogin,SWT.NONE);
		GridLayout progressLayout = new GridLayout(2,false);
		progressLayout.marginTop = 20;
		progressLayout.marginLeft = 50;
		compositeProgress.setLayout(progressLayout);

		// label for the progress
		Label progressLabel = new Label(compositeProgress,SWT.CENTER);
		progressLabel.setText("Überprüfung läuft");
		GridData data = new GridData();
		data.verticalIndent = 20;
		progressLabel.setLayoutData(data);

		//the progress bar
		progressBar = new ProgressBar(compositeProgress,SWT.NONE|SWT.INDETERMINATE);
		data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		data.verticalIndent = 20;
		progressBar.setLayoutData(data);
	}

	private void toggelFormProgress(boolean state) 
	{
		//the progress should be visible
		if(state)
		{
			compositeFormular.dispose();
			createProgressComposite();
		}
		else
		{
			compositeProgress.dispose();
			createLoginForm();
		}
		//force redraw
		compositeLogin.layout(true);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		//connection to the server was successfully
		if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(evt.getPropertyName()))
		{
			loginStatus = 1;
			//store the login info for later usage
			SessionManager.getInstance().getLoginInformation().setUsername(username);
			SessionManager.getInstance().getLoginInformation().setPassword(password);
		}
		if("AUTHENTICATION_FAILED".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the login object
			login = (Login)evt.getNewValue();
			if(login.isIslocked())
				loginStatus = 2;
			else
				loginStatus = 3;
		}
	}
}
