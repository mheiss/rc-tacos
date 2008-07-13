package at.rc.tacos.client.dialogs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import at.rc.tacos.client.Activator;
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
	private Button bWizzard,bRetry;

	/**
	 * This return code indicates that the connection was established
	 */
	public final int RETURN_CONNECTED = 0x00;

	/**
	 * This return code indicates that the wizard should be started
	 */
	public final int RETURN_WIZARD = 0x01;

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
		//the info for the job
		final ServerInfo server = NetSource.getInstance().getCurrentServer();
		String username = SessionManager.getInstance().getLoginInformation().getUsername();
		String password = SessionManager.getInstance().getLoginInformation().getPassword();

		//replace the name of the server in the label
		String text = lHeader.getText().replace("%SERVER%", server.getHostName());
		lHeader.setText(text);

		//startup the connection job
		ConnectionJob job = new ConnectionJob(server,username,password);
		job.setSystem(true);
		job.addJobChangeListener(new JobChangeAdapter()
		{
			@Override
			public void aboutToRun(IJobChangeEvent event) 
			{
				Display.getDefault().syncExec(new Runnable() 
				{
					@Override
					public void run() 
					{
						bRetry.setEnabled(false);
						lRetries.setForeground(CustomColors.COLOR_BLACK);

					}
				});
			}

			@Override
			public void done(final IJobChangeEvent event) 
			{
				Display.getDefault().syncExec(new Runnable() 
				{
					@Override
					public void run() 
					{
						//assert valid dialog
						if(getShell() == null)
							return;

						//enable the button again
						bRetry.setEnabled(true);

						//check the result of the job
						if(event.getResult() == Status.OK_STATUS)
						{
							//close the dialog
							setReturnCode(RETURN_CONNECTED);
							ConnectionDialog.this.close();
						}
						else
						{
							lRetries.setText("Verbindung mit dem Server "+server.getDescription()+ " konnte nicht hergestellt werden");
							lRetries.setForeground(CustomColors.RED_COLOR);
						}
					}
				});
			}
		});
		job.schedule();
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
		//the button to retry the connection
		bRetry = new Button(parent,SWT.NONE);
		bRetry.setText("Verbindungsversuch wiederholen");
		bRetry.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bRetry.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) {
				//initialize the dialog again and start the job
				init();
			}
		});

		//the button to start the wizard
		bWizzard = new Button(parent,SWT.NONE);
		bWizzard.setText("Netzwerkverbindungs Assistent starten");
		bWizzard.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bWizzard.addSelectionListener(new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent se) 
			{
				try
				{
					//first stop the thread that wants to reconnect to the server
					Job.getJobManager().cancel(ConnectionJob.JOB_FAMILY);
					for(Job job:Job.getJobManager().find(ConnectionJob.ASYNC_FINISH))
						job.join();

					//set the return code to start the wizard 
					setReturnCode(RETURN_WIZARD);

					//close the dialog
					close();
				}
				catch(Exception e)
				{
					//show a error message for the user
					MessageDialog.openError(getShell(), 
							"Netzwerkverbindungsassistent",
					"Der Assistent zum Wiederherstellen der Netzwerkverbindung konnte nicht gestartet werden");
					//log the message
					Activator.getDefault().log("Failed to start the network wizzard: "+e.getMessage(), IStatus.ERROR);
				}
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
		setReturnCode(RETURN_WIZARD);
	}

	class ConnectionJob extends Job
	{
		//properties of the job
		private ServerInfo serverInfo;	

		//the login information to login again
		private String username,password;

		//the job identification
		public final static String JOB_FAMILY = "connectionJob";

		/**
		 * Default class constructor
		 */
		public ConnectionJob(ServerInfo serverInfo,String username,String password) 
		{
			super("Verbindung zum Server wiederherstellen");
			//the server to connect to
			this.serverInfo = serverInfo;
			this.username = username;
			this.password = password;
		}

		@Override
		public boolean belongsTo(Object family) 
		{
			return JOB_FAMILY.equals(family);
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) 
		{
			try
			{
				//we try to connect to the server for 5 times
				for(int i=1;i<6;i++)
				{
					MySocket socket = NetSource.getInstance().openConnection(serverInfo);
					//check if we have a connection
					if(socket != null)
						break;

					updateStatusLabel("Verbindungsversuch "+i+" von 5");

					//wait for 5 seconds and try it again
					for(int wait=0;wait < 10;wait++)
					{
						//check if the job is canceled
						if(monitor.isCanceled())
							return Status.CANCEL_STATUS;
						//sleep for some time
						Thread.sleep(500);
					}
				}
				//check if we have a connection
				if(NetSource.getInstance().getConnection() == null)
					return Status.CANCEL_STATUS;

				//start the monitor jobs and try to login
				NetWrapper.getDefault().init();
				updateStatusLabel("Sende die Login Informationen an den Server");
				NetWrapper.getDefault().sendLoginMessage(new Login(username,password,false));

				//try to login to the server
				updateStatusLabel("Warte auf Anwort vom Server");
				while(!SessionManager.getInstance().isAuthenticated())
				{
					//check if the job is canceled
					if(monitor.isCanceled())
						return Status.CANCEL_STATUS;
					//sleep for some time
					Thread.sleep(100);
				}

				//successfully authenticated
				return Status.OK_STATUS;
			}
			catch(Exception e)
			{
				//log the exception and return
				NetWrapper.log("Failed to reconnect to the server "+serverInfo+". Cause:"+e.getMessage(), IStatus.ERROR, e.getCause());
				return Status.CANCEL_STATUS;
			}
			finally
			{
				//set the progress to done
				monitor.done();
			}
		}

		/**
		 * Helper method to update the status label of the dialog
		 */
		private void updateStatusLabel(final String message)
		{
			//update the label
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					ConnectionDialog.this.lRetries.setText(message);
				}
			});
		}
	}
}