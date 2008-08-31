package at.rc.tacos.server.ui.dialogs;

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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Helo;
import at.rc.tacos.server.ui.utils.CustomUI;

public class ConnectionDialog extends Dialog
{
	//properties for the dialog
	private CLabel lHeader;
	private Label lRetries;
	private ProgressBar connectionProgress;
	private Button bAbort,bWizzard;
	
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
	public static int openDialog(Shell parentShell,Helo helo)
	{
		//create a new instance of the message dialog
		ConnectionDialog dialog = new ConnectionDialog(parentShell);
		dialog.create();

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
		composite.setBackground(CustomUI.WHITE_COLOR);

		//the header for the message text
		lHeader = new CLabel(composite,SWT.NONE);
		lHeader.setFont(CustomUI.HEADLINE_FONT);
		lHeader.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.net.down"));
		lHeader.setText("Verbindung zum Server dark verloren");
		lHeader.setBackground(CustomUI.WHITE_COLOR);

		Composite labelComposite = new Composite(composite,SWT.NONE);
		layout = new GridLayout(1,false);
		layout.marginHeight = 15;
		layout.verticalSpacing = 15;
		labelComposite.setLayout(layout);
		labelComposite.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.CENTER));
		labelComposite.setBackground(CustomUI.WHITE_COLOR);

		//setup the progress bar
		connectionProgress = new ProgressBar(labelComposite,SWT.INDETERMINATE);
		connectionProgress.setState(-1);
		connectionProgress.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//the number of retries
		lRetries = new Label(labelComposite,SWT.NONE);
		lRetries.setBackground(CustomUI.WHITE_COLOR);
		lRetries.setText("Verbindungsversuch 1/5");
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
		//add the buttons
		bAbort = new Button(parent,SWT.NONE);
		bAbort.setText("Abbrechen");
		bAbort.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bAbort.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent se) { }	
			
			@Override
			public void widgetSelected(SelectionEvent se) 
			{
				setReturnCode(0);
				close();
			}
		});

		bWizzard = new Button(parent,SWT.NONE);
		bWizzard.setText("Verbindungs Wizzard starten");
		bWizzard.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		bWizzard.addSelectionListener(new SelectionListener()
		{
			@Override
			public void widgetDefaultSelected(SelectionEvent se) { }	
			
			@Override
			public void widgetSelected(SelectionEvent se) 
			{
				setReturnCode(-1);
				close();
			}
		});

		//Set the default button
		getShell().setDefaultButton(bAbort);
	}

	/** 
	 * Handle the shell close. Set the return code to <code>SWT.DEFAULT</code>
	 * as there has been no explicit close by the user.
	 */
	protected void handleShellCloseEvent() 
	{
		super.handleShellCloseEvent();
		setReturnCode(SWT.DEFAULT);
	}
}
