package at.rc.tacos.server.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.core.db.DbWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.server.db.jobs.DbConnectionJob;
import at.rc.tacos.server.db.jobs.DbShutdownJob;
import at.rc.tacos.server.ui.utils.CustomUI;
import at.rc.tacos.server.ui.utils.MyViewUtils;

public class ServerStatusView extends ViewPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.server.ui.views.ServerStatusView";

	//the properties of the view
	private FormToolkit toolkit;
	private Form form;

	//the components for the network status
	private Section netSection;
	private CLabel netStatusImage;
	private CLabel netStatusText;
	private CLabel netStatusDesc;
	//the hyperlinks to control the status
	private ImageHyperlink netControlLink;

	//the components for the database status
	private Section dbSection;
	private CLabel dbStatusImage;
	private CLabel dbStatusText;
	private CLabel dbStatusDesc;
	//the hyperlinks to control the status
	private ImageHyperlink dbControlLink;

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(Composite parent) 
	{
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setText("Serverstatus");
		form.setImage(ImageFactory.getInstance().getRegisteredImage("server.view.net"));
		toolkit.decorateFormHeading(form);

		//the client
		Composite body = form.getBody();
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		layout.minNumColumns = 2;
		body.setLayout(layout);

		//setup the status section for the server
		createServerSection(body);
		//setup the staut section for the database
		createDatabaseSection(body);

		//add listeners to be notified uppon changes
		DbWrapper.getDefault().addPropertyChangeListener(this);
	}

	/**
	 * Dispose the view and cleanup the used listeners
	 */
	@Override
	public void dispose() 
	{
		DbWrapper.getDefault().removePropertyChangeListener(this);
	}

	/**
	 * Creates the server status section
	 */
	private void createServerSection(Composite parent)
	{
		//create the section
		netSection = toolkit.createSection(parent,Section.TITLE_BAR | Section.EXPANDED);
		netSection.setText("Server-Netzwerkverbindung");	

		//the client of the section
		Composite client = toolkit.createComposite(netSection);
		client.setLayout(new GridLayout(2,false));
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		//the status image in the left column
		netStatusImage = new CLabel(client,SWT.NONE);
		netStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.net.down"));
		//the status text
		netStatusText = new CLabel(client,SWT.NONE);
		netStatusText.setText("Server gestoppt");
		netStatusText.setForeground(CustomUI.RED_COLOR);
		netStatusText.setFont(CustomUI.HEADLINE_FONT);
		//the description below
		netStatusDesc = new CLabel(client,SWT.NONE);
		netStatusDesc.setText("Keine Verbindungen zum Server möglich");
		netStatusDesc.setFont(CustomUI.DESCRIPTION_FONT);
		//the start controll
		netControlLink = new ImageHyperlink(client,SWT.NONE);
		netControlLink.setText("Server starten");
		netControlLink.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.start"));
		netControlLink.setForeground(CustomUI.BLUE_COLOR);
		netControlLink.setFont(CustomUI.LINK_FONT);
		netControlLink.setUnderlined(true);
		netControlLink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) {
				//start the job to open a connection to the database
			}
			@Override
			public void linkEntered(HyperlinkEvent e) {
				netControlLink.setForeground(CustomUI.HOVER_COLOR);
			}
			@Override
			public void linkExited(HyperlinkEvent e) {
				netControlLink.setForeground(CustomUI.BLUE_COLOR);
			}
		});

		//the image should span vertical 3 columns
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.verticalSpan = 3;
		netStatusImage.setLayoutData(gd);

		//set the client for the section
		netSection.setClient(client);
	}

	/**
	 * Creates the database status section
	 */
	private void createDatabaseSection(Composite parent)
	{
		//create the section
		dbSection = toolkit.createSection(parent, Section.TITLE_BAR | Section.EXPANDED);
		dbSection.setText("Server-Datenbankverbindung");

		//the client of the section
		Composite client = toolkit.createComposite(dbSection);
		client.setLayout(new GridLayout(2,false));
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		//the status image in the left column
		dbStatusImage = new CLabel(client,SWT.NONE);
		dbStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.db.down"));
		//the status text
		dbStatusText = new CLabel(client,SWT.NONE);
		dbStatusText.setText("Datenbank gestoppt");
		dbStatusText.setForeground(CustomUI.RED_COLOR);
		dbStatusText.setFont(CustomUI.HEADLINE_FONT);
		//the description below
		dbStatusDesc = new CLabel(client,SWT.NONE);
		dbStatusDesc.setText("Keine Verbindung zur Datenbank");
		dbStatusDesc.setFont(CustomUI.DESCRIPTION_FONT);
		//the start controll
		dbControlLink = new ImageHyperlink(client,SWT.NONE);
		dbControlLink.setText("Datenbankverbindung herstellen");
		dbControlLink.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.start"));
		dbControlLink.setForeground(CustomUI.BLUE_COLOR);
		dbControlLink.setFont(CustomUI.LINK_FONT);
		dbControlLink.setUnderlined(true);
		dbControlLink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) {	

				//check if we have a connection to the server
				if(DbWrapper.getDefault().isConnected())
				{
					DbShutdownJob dbJob = new DbShutdownJob();
					dbJob.setUser(true);
					dbJob.schedule();
					dbJob.addJobChangeListener(new JobChangeAdapter()
					{
						@Override
						public void done(IJobChangeEvent event) {						
							//check if the status is ok
							if(event.getResult() == Status.OK_STATUS)
								return;

							//show a error message 
							MyViewUtils.showError("Verbindungsfehler","Die Datenbankverbindung konnte nicht getrennt werden");
						}
					});
				}
				else
				{
					//create the connection job
					DbConnectionJob dbJob = new DbConnectionJob();
					dbJob.setUser(true);
					dbJob.schedule();
					dbJob.addJobChangeListener(new JobChangeAdapter()
					{
						@Override
						public void done(IJobChangeEvent event) {						
							//check if the status is ok
							if(event.getResult() == Status.OK_STATUS)
								return;

							//show a error message 
							MyViewUtils.showError("Verbindungsfehler","Es konnte keine Verbindung zur Datenbank hergestellt werden");
						}
					});
				}
			}

			@Override
			public void linkEntered(HyperlinkEvent e) {
				dbControlLink.setForeground(CustomUI.HOVER_COLOR);
			}
			@Override
			public void linkExited(HyperlinkEvent e) {
				dbControlLink.setForeground(CustomUI.BLUE_COLOR);
			}
		});

		//the image should span vertical 3 columns
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.verticalSpan = 3;
		dbStatusImage.setLayoutData(gd);

		//set the client for the section
		dbSection.setClient(client);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() 
	{
		form.setFocus();
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) 
	{
		final String eventName = event.getPropertyName();
		//the updates should run in the ui thread
		Display.getDefault().syncExec(new Runnable()
		{
			@Override
			public void run() {
				//check if the database connection is established
				if(DbWrapper.DB_CONNECTION_OPENED.equalsIgnoreCase(eventName))
				{
					//update the database status 
					dbStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.db.up"));
					dbStatusText.setText("Datenbankverbindung aufgebaut");
					dbStatusDesc.setText("Verbindung zur Datenbank wurde erfolgreich hergestellt");
					//change the control link
					dbControlLink.setText("Datenbankverbindung stoppen");
					dbControlLink.setForeground(CustomUI.BLUE_COLOR);
					dbControlLink.setEnabled(true);
					dbControlLink.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.pause"));
					//update the view
					dbSection.layout(true);
				}
				if(DbWrapper.DB_CONNECTION_CLOSED.equalsIgnoreCase(eventName))
				{
					dbStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.db.down"));
					dbStatusText.setText("Datenbank gestoppt");
					dbStatusDesc.setText("Keine Verbindung zur Datenbank");
					//change the control link
					dbControlLink.setText("Datenbankverbindung herstellen");
					dbControlLink.setForeground(CustomUI.BLUE_COLOR);
					dbControlLink.setEnabled(true);
					dbControlLink.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.start"));
					//update the view
					dbSection.layout(true);
				}
			}
		});
	}


}
