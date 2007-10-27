package at.rc.tacos.client.splashHandlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.splash.AbstractSplashHandler;

import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.core.net.event.IClientLoginListener;
import at.rc.tacos.core.net.event.IClientNetEventListener;

/**
 * Interactive splash screen handler to login during the
 * splash screen is shown.
 * @since 3.3
 */
public class InteractiveSplashHandler extends AbstractSplashHandler implements IClientLoginListener
{
    //the components
    private Composite fCompositeLogin;
    private Text fTextUsername;
    private Text fTextPassword;
    private Label fLabelUsername;
    private Label fLabelPassword;
    private Button fButtonOK;
    private Button fButtonCancel;
    private Label progressLabel;
    private ProgressBar progressBar;
    private boolean fAuthenticated;

    //the login status
    private volatile int loginStatus = -1;

    //the layout
    private final static int F_LABEL_HORIZONTAL_INDENT = 190;
    private final static int F_BUTTON_WIDTH_HINT = 80;
    private final static int F_TEXT_WIDTH_HINT = 175;
    private final static int F_COLUMN_COUNT = 3;

    /**
     *  Default class constructor
     */
    public InteractiveSplashHandler() 
    {
        fCompositeLogin = null;
        fTextUsername = null;
        fTextPassword = null;
        fButtonOK = null;
        fButtonCancel = null;
        fAuthenticated = false;
    }

    /**
     * Initialize the splash window and create the ui.
     */
    public void init(final Shell splash) {
        // Store the shell
        super.init(splash);
        // Configure the shell layout
        configureUISplash();
        // Create UI
        createUI();		
        // Create UI listeners
        createUIListeners();
        // Force the splash screen to layout
        splash.layout(true);
        // Keep the splash screen visible and prevent the RCP application from 
        // loading until the close button is clicked.
        doEventLoop();
        //
        toggelCheckProgress(false);

    }

    /**
     *  The event loop which checks whether the authentication
     *  was soccessfully
     */
    private void doEventLoop() 
    {
        Shell splash = getSplash();
        while (fAuthenticated == false) 
        {
            if (splash.getDisplay().readAndDispatch() == false) 
            {
                splash.getDisplay().sleep();
            }
        }
    }
    
    /**
     * The login process failed
     */
    @Override
    public void loginFailed()
    {
        toggelCheckProgress(false);
        loginStatus = -1;
        MessageDialog.openError(
                getSplash(),
                "Anmeldung fehlgeschlagen", 
        "Benutzer und/oder Passwort falsch"); 
        
    }

    /**
     *  The login process was successfully
     */
    @Override
    public void loginSuccessfully()
    {
        toggelCheckProgress(false);
        fCompositeLogin.setVisible(false);
        fAuthenticated = true;
        loginStatus = -1; 
    }


    /**
     *  Create the listeners for the controls
     */
    private void createUIListeners() 
    {
        // Create the OK button listeners
        fButtonCancel.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                handleButtonCancelWidgetSelected();
            }
        });
        // Create the cancel button listeners
        fButtonOK.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
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
        final String username = fTextUsername.getText();
        final String password = fTextPassword.getText();
        //hide the controls, show the progress
        toggelCheckProgress(true);
        
        //send the login request
        NetWrapper.getDefault().registerLoginListener(this);
        NetWrapper.getDefault().sendMessage(username +" + "+ password);
    }

    /**
     * Create the user interface for the login
     */
    private void createUI() 
    {
        // Create the login panel
        createUICompositeLogin();
        // Create the blank spanner
        createUICompositeBlank();
        // Create the user name label
        createUILabelUserName();
        // Create the user name text widget
        createUITextUserName();
        // Create the password label
        createUILabelPassword();
        // Create the password text widget
        createUITextPassword();
        // Create the blank label
        createUILabelBlank();
        // Create the OK button
        createUIButtonOK();
        // Create the cancel button
        createUIButtonCancel();
    }		

    /**
     * 
     */
    private void createUIButtonCancel() {
        // Create the button
        fButtonCancel = new Button(fCompositeLogin, SWT.PUSH);
        fButtonCancel.setText("Cancel"); //NON-NLS-1
        // Configure layout data
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_BUTTON_WIDTH_HINT;	
        data.verticalIndent = 10;
        fButtonCancel.setLayoutData(data);
    }

    /**
     * 
     */
    private void createUIButtonOK() 
    {
        // Create the button
        fButtonOK = new Button(fCompositeLogin, SWT.PUSH);
        fButtonOK.setText("OK"); //NON-NLS-1
        // Configure layout data
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_BUTTON_WIDTH_HINT;
        data.verticalIndent = 10;
        fButtonOK.setLayoutData(data);
    }

    /**
     * 
     */
    private void createUILabelBlank() 
    {
        Label label = new Label(fCompositeLogin, SWT.NONE);
        label.setVisible(false);
    }

    /**
     * 
     */
    private void createUITextPassword() 
    {
        // Create the text widget
        int style = SWT.PASSWORD | SWT.BORDER;
        fTextPassword = new Text(fCompositeLogin, style);
        // Configure layout data
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_TEXT_WIDTH_HINT;
        data.horizontalSpan = 2;
        fTextPassword.setLayoutData(data);		
    }

    /**
     * 
     */
    private void createUILabelPassword() 
    {
        // Create the label
        fLabelPassword = new Label(fCompositeLogin, SWT.NONE);
        fLabelPassword.setText("&Password:"); //NON-NLS-1
        // Configure layout data
        GridData data = new GridData();
        data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
        fLabelPassword.setLayoutData(data);					
    }

    /**
     * 
     */
    private void createUITextUserName()
    {
        // Create the text widget
        fTextUsername = new Text(fCompositeLogin, SWT.BORDER);
        // Configure layout data
        GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_TEXT_WIDTH_HINT;
        data.horizontalSpan = 2;
        fTextUsername.setLayoutData(data);		
    }

    /**
     * 
     */
    private void createUILabelUserName() 
    {
        // Create the label
        fLabelUsername = new Label(fCompositeLogin, SWT.NONE);
        fLabelUsername.setText("&User Name:"); //NON-NLS-1
        // Configure layout data
        GridData data = new GridData();
        data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
        fLabelUsername.setLayoutData(data);		
    }

    private void createProgressInfo() 
    {
        progressLabel = new Label(fCompositeLogin,SWT.NONE);
        progressLabel.setText("Überprüfung läuft");
        GridData data = new GridData();
        data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
        progressLabel.setLayoutData(data);
        progressLabel.setVisible(false);

        progressBar = new ProgressBar(fCompositeLogin,SWT.NONE|SWT.INDETERMINATE);
        data = new GridData(SWT.NONE, SWT.NONE, false, false);
        data.widthHint = F_TEXT_WIDTH_HINT;
        data.horizontalSpan = 2;
        progressBar.setLayoutData(data);
        progressBar.setVisible(false);
    }

    private void toggelCheckProgress(boolean state) 
    {
        //hide the controlls and the labels
        fLabelPassword.setVisible(!state);
        fLabelUsername.setVisible(!state);
        fTextPassword.setVisible(!state);
        fTextUsername.setVisible(!state);
        fButtonOK.setVisible(!state);
        fButtonCancel.setVisible(!state);
        //show the progress
        progressLabel.setVisible(state);
        progressBar.setVisible(state);
        fCompositeLogin.layout();
    }

    /**
     * 
     */
    private void createUICompositeBlank() 
    {
        Composite spanner = new Composite(fCompositeLogin, SWT.NONE);
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
        data.horizontalSpan = F_COLUMN_COUNT;
        spanner.setLayoutData(data);
    }

    /**
     * 
     */
    private void createUICompositeLogin() 
    {
        // Create the composite
        fCompositeLogin = new Composite(getSplash(), SWT.BORDER);
        GridLayout layout = new GridLayout(F_COLUMN_COUNT, false);
        layout.marginBottom = 70;
        fCompositeLogin.setLayout(layout);		
    }

    /**
     * 
     */
    private void configureUISplash() 
    {  
        // Configure layout
        FillLayout layout = new FillLayout(); 
        getSplash().setLayout(layout);
        // Force shell to inherit the splash background
        getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
    }
}
