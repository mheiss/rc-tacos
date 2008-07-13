package at.rc.tacos.client.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.net.NetSource;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.ServerInfo;
import at.rc.tacos.net.MySocket;

public class ConnectionDialog extends Dialog
{
	//properties for the dialog
	private CLabel lHeader;
	private Label lRetries;
	private ProgressBar connectionProgress;
	private Button bWizzard;

	/**
	 * This return code indicates that the connection was established
	 */
	public final int RETURN_CONNECTED = 0x00;

	/**
	 * This return code indicates that the connection failed.
	 */
	public final int RETURN_FAILED = 0x01;

	/**
	 * Default class constructor for the message dialog
	 * @param parentShell
	 */
	protected ConnectionDialog(Shell parentShell) 
	{
		super(parentShell);
		setBlockOnOpen(true);
	}

	/**
	 * Convenience method to open the message dialog.
	 * 
	 */
	public static int openDialog(Shell parentShell)
	{
		//create a new instance of the message dialog
		ConnectionDialog dialog = new ConnectionDialog(parentShell);
		dialog.create();
		dialog.init();

		//return the result
		return dialog.open();
	}

	//SETUP AND CONFIG METHODS FOR THE MESSAGE BOX
	/**
	 * Initalizes and configures the shell
	 */
	@Override
	protected void configureShell(Shell newShell) 
	{
		super.configureShell(newShell);
		newShell.setText("Netzwerkverbindung wiederherstellen");
	}

	/**
	 * Initializes the dialog with the default values
	 */
	protected void init()
	{
		ServerInfo info = NetSource.getInstance().getCurrentServer();

		//replace the nam of the server in the label
		String text = lHeader.getText().replace("%SERVER%", info.getHostName());
		lHeader.setText(text);

		//the info for the job
		ServerInfo server = NetSource.getInstance().getCurrentServer();
		String username = SessionManager.getInstance().getLoginInformation().getUsername();
		String password = SessionManager.getInstance().getLoginInformation().getPassword();

		//startup the connection job
		ConnectionJob job = new ConnectionJob(server,username,password);
		job.setSystem(true);
		job.schedule();
		job.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void done(IJobChangeEvent event) {
				//check the result of the job
				if(event.getResult() == Status.CANCEL_STATUS)
					setReturnCode(RETURN_FAILED);
				else
					setReturnCode(RETURN_CONNECTED);
				//close the dialog
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						ConnectionDialog.this.close();
					}
				});
			}
		});
	}

	/**
	 * Creates the content for this dialog
	 */
	@Override
	protected Control createDialogArea(Composite parent) 
	{
		//setup the composite
		Composite composite = new Composite(parent,SWT.NONE);
		GridLayout layout = new GridLayout(1,false);
		layout.marginWidth = 15;
		layout.marginHeight = 15;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setBackground(CustomColors.HEADING_COLOR);

		//the header for the message text
		lHeader = new CLabel(composite,SWT.NONE);
		lHeader.setFont(CustomColors.HEADER_FONT);
		lHeader.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.net.down"));
		lHeader.setText("Verbindung zum Server %SERVER% verloren");
		lHeader.setBackground(CustomColors.HEADING_COLOR);

		Composite labelComposite = new Composite(composite,SWT.NONE);
		layout = new GridLayout(1,false);
		layout.marginHeight = 15;
		layout.verticalSpacing = 15;
		labelComposite.setLayout(layout);
		labelComposite.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.CENTER));
		labelComposite.setBackground(CustomColors.HEADING_COLOR);

		//setup the progress bar
		connectionProgress = new ProgressBar(labelComposite,SWT.INDETERMINATE);
		connectionProgress.setState(-1);
		connectionProgress.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		//the number of retries
		lRetries = new Label(labelComposite,SWT.NONE);
		lRetries.setBackground(CustomColors.HEADING_COLOR);
		lRetries.setText("Verbindungsversuch 1 von 5");
		lRetries.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		//progress should span over two columns
		GridData data = new GridData();
		data.horizontalSpan = 2;
		data.verticalAlignment = GridData.VERTICAL_ALIGN_CENTER;
		lHeader.setLayoutData(data);
		return composite;
	}


	@Override
	protected Control createButtonBar(Composite parent) 
	{
		//create a new composite that holds the buttons
		Composite buttonBar = new Composite(parent,SWT.CENTER);
		GridLayout layout = new GridLayout(2,true);
		layout.marginLeft = 15;
		layout.marginWidth = 15;
		layout.marginHeight = 15;
		layout.verticalSpacing = 10;
		buttonBar.setLayout(layout);
		buttonBar.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_CENTER));

		//Cceate the button bar
		createButtonsForButtonBar(buttonBar);

		return buttonBar;
	}

	/**
	 * Create the buttons for the button bar
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) 
	{
		bWizzard = new Button(parent,SWT.NONE);
		bWizzard.setText("Netzwerkverbindungs Wizzard starten");
		bWizzard.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bWizzard.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent se) { }	

			@Override
			public void widgetSelected(SelectionEvent se) 
			{
				setReturnCode(RETURN_FAILED);
				close();
			}
		});

		//Set the default button
		getShell().setDefaultButton(bWizzard);
	}

	/** 
	 * Handle the shell close. Set the return code to <code>SWT.DEFAULT</code>
	 * as there has been no explicit close by the user.
	 */
	protected void handleShellCloseEvent() 
	{
		super.handleShellCloseEvent();
		setReturnCode(RETURN_FAILED);
	}

	class ConnectionJob extends Job implements PropertyChangeListener
	{
		//properties of the job
		private ServerInfo serverInfo;	

		//the login information to login again
		private String username,password;

		//the login response
		private boolean loginResponse;

		public ConnectionJob(ServerInfo serverInfo,String username,String password) 
		{
			super("Verbindung zum Server wiederherstellen");
			//the server to connect to
			this.serverInfo = serverInfo;
			this.username = username;
			this.password = password;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				//add a listener for the network messages
				SessionManager.getInstance().addPropertyChangeListener(this);

				//we try to connect to the server for 5 times
				for(int i=1;i<6;i++)
				{
					MySocket socket = NetSource.getInstance().openConnection(serverInfo);
					//check if we have a connection
					if(socket != null)
						break;

					final int progress = i;

					//update the label
					Display.getDefault().syncExec(new Runnable() {
						@Override
						public void run() {
							ConnectionDialog.this.lRetries.setText("Verbindungsversuch "+progress+" von 5");
						}
					});

					//wait for 5 seconds and try it again
					Thread.sleep(5000);				
				}
				//check if we have a connection
				if(NetSource.getInstance().getConnection() == null)
					return Status.CANCEL_STATUS;

				//start the monitor jobs and try to login
				NetWrapper.getDefault().init();
				NetWrapper.getDefault().sendLoginMessage(new Login(username,password,false));

				//try to login to the server
				while(!loginResponse)
				{
					Thread.sleep(100);
				}

				//check the login status
				if(!NetWrapper.getDefault().isAuthenticated())
				{
					return Status.CANCEL_STATUS;
				}
				else
				{
					return Status.OK_STATUS;
				}
			}
			catch(Exception e)
			{
				//log the exception and return
				NetWrapper.log("Failed to reconnect to the server "+serverInfo+". Cause:"+e.getMessage(), IStatus.ERROR, e.getCause());
				return Status.CANCEL_STATUS;
			}
			finally
			{
				//remove the listener again
				SessionManager.getInstance().removePropertyChangeListener(this);
				//set the progress to done
				monitor.done();
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent event) 
		{
			//connection to the server was successfully
			if("AUTHENTICATION_SUCCESS".equalsIgnoreCase(event.getPropertyName()))
				loginResponse = true;
			if("AUTHENTICATION_FAILED".equalsIgnoreCase(event.getPropertyName()))
				loginResponse = true;
		}
	}
}