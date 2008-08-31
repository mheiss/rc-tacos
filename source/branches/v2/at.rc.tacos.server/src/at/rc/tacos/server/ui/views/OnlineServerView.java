package at.rc.tacos.server.ui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Helo;
import at.rc.tacos.server.net.NetWrapper;
import at.rc.tacos.server.net.ServerManager;
import at.rc.tacos.server.ui.utils.CustomUI;
import at.rc.tacos.util.MyUtils;

public class OnlineServerView extends ViewPart implements PropertyChangeListener
{
	//the view id 
	public static final String ID = "at.rc.tacos.server.ui.views.OnlineServerView";
	
	//the table viewer
	private FormToolkit toolkit;
	private Form form;
	
	//the primary server
	private Section primarySection;
	private CLabel primaryStatusImage;
	private CLabel primaryStatusText;
	private CLabel primaryStatusDesc;
	private CLabel primaryServerInfo;
	
	//the second server
	private Section secondSection;
	private CLabel secondStatusImage;
	private CLabel secondStatusText;
	private CLabel secondStatusDesc;
	private CLabel secondServerInfo;
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		form.setText("Aktive Server");
		form.setImage(ImageFactory.getInstance().getRegisteredImage("server.view.servers"));
		toolkit.decorateFormHeading(form);

		//the client
		Composite body = form.getBody();
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 1;
		layout.minNumColumns = 1;
		body.setLayout(layout);

		//setup the status section for the server
		createPrimarySection(body);
		createSecondarySection(body);
		
		//listen to server changes
		ServerManager.getInstance().addPropertyChangeListener(this);
	}

	/**
	 * Called when the view is destroyed
	 */
	@Override
	public void dispose()
	{
		ServerManager.getInstance().removePropertyChangeListener(this);
	}
	
	/**
	 * Creates the primary server status section
	 */
	private void createPrimarySection(Composite parent)
	{
		//create the section
		primarySection = toolkit.createSection(parent,Section.TITLE_BAR | Section.EXPANDED);
		primarySection.setText("Primärer Server");	

		//the client of the section
		Composite client = toolkit.createComposite(primarySection);
		client.setLayout(new GridLayout(2,false));
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		//the status image in the left column
		primaryStatusImage = new CLabel(client,SWT.NONE);
		primaryStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.offline"));
		//the status text
		primaryStatusText = new CLabel(client,SWT.NONE);
		primaryStatusText.setText("Primärer Server offline");
		primaryStatusText.setForeground(CustomUI.RED_COLOR);
		primaryStatusText.setFont(CustomUI.HEADLINE_FONT);
		//the description below
		primaryStatusDesc = new CLabel(client,SWT.NONE);
		primaryStatusDesc.setText("Keine Verbindungen zum Primären Server möglich.");
		primaryStatusDesc.setFont(CustomUI.DESCRIPTION_FONT);
		
		primaryServerInfo = new CLabel(client,SWT.NONE);
		primaryServerInfo.setText("-");
		primaryServerInfo.setFont(CustomUI.DESCRIPTION_FONT);
		
		//the image should span vertical 3 columns
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.verticalSpan = 4;
		primaryStatusImage.setLayoutData(gd);

		//set the client for the section
		primarySection.setClient(client);
	}
	
	/**
	 * Creates the failback server status section
	 */
	private void createSecondarySection(Composite parent)
	{
		//create the section
		secondSection = toolkit.createSection(parent,Section.TITLE_BAR | Section.EXPANDED);
		secondSection.setText("Failback Server");	

		//the client of the section
		Composite client = toolkit.createComposite(secondSection);
		client.setLayout(new GridLayout(2,false));
		client.setLayoutData(new GridData(GridData.FILL_BOTH));

		//the status image in the left column
		secondStatusImage = new CLabel(client,SWT.NONE);
		secondStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.offline"));
		//the status text
		secondStatusText = new CLabel(client,SWT.NONE);
		secondStatusText.setText("Failback Server offline");
		secondStatusText.setForeground(CustomUI.RED_COLOR);
		secondStatusText.setFont(CustomUI.HEADLINE_FONT);
		//the description below
		secondStatusDesc = new CLabel(client,SWT.NONE);
		secondStatusDesc.setText("Keine Verbindungen zum Failback Server möglich.");
		secondStatusDesc.setFont(CustomUI.DESCRIPTION_FONT);
		
		secondServerInfo = new CLabel(client,SWT.NONE);
		secondServerInfo.setText("-");
		secondServerInfo.setFont(CustomUI.DESCRIPTION_FONT);
		
		//the image should span vertical 3 columns
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.verticalSpan = 4;
		secondStatusImage.setLayoutData(gd);

		//set the client for the section
		secondSection.setClient(client);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		
	}

	/**
	 * Listen to updates
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent event) 
	{
		final String eventName = event.getPropertyName();
		//the updates should run in the ui thread
		Display.getDefault().syncExec(new Runnable()
		{
			@Override
			public void run() 
			{
				//UPDATE OF THE PRIMARY SERVER
				if(ServerManager.PRIMARY_ONLINE.equalsIgnoreCase(eventName))
				{
					//get the new server
					Helo server = (Helo)event.getNewValue();
					
					//setup the status text
					String text = "Primärer Server online";
					text += NetWrapper.getDefault().getServerInfo().equals(server) ? " (dieser Server)" : "";
					
					//update the view
					primaryStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.online"));
					primaryStatusText.setText(text);
					primaryStatusDesc.setText("Primärer Server nimmt Verbindungen am Port "+server.getServerPort()+ " entgegen");
					primaryServerInfo.setText("Serverinformation: "+server);
					//refresch
					primarySection.layout(true);
				}
				if(ServerManager.PRIMARY_OFFLINE.equalsIgnoreCase(eventName))
				{
					//update the view
					primaryStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.offline"));
					primaryStatusText.setText("Primärer Server offline");
					primaryStatusDesc.setText("Keine Verbinung mit derm Server möglich.");
					primaryServerInfo.setText("-");
					//refresch
					primarySection.layout(true);
				}
				
				//UPDATE OF THE FAILBACK SERVER
				if(ServerManager.SECONDARY_ONLINE.equalsIgnoreCase(eventName))
				{
					//get the new server
					Helo server = (Helo)event.getNewValue();
					
					//setup the status text
					String text = "Failback Server online";
					System.out.println(server.getServerIp());
					text += NetWrapper.getDefault().getServerInfo().equals(server) ? " (dieser Server)" : "";
					
					//update the view
					secondStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.online"));
					secondStatusText.setText(text);
					secondStatusDesc.setText("Failback Server leitet alle Anfrage zum primären Server weiter");		
					secondServerInfo.setText("Serverinformation: " +server);
					//refresch
					secondSection.layout(true);
				}
				if(ServerManager.SECONDARY_OFFLINE.equalsIgnoreCase(eventName))
				{
					//update the view
					secondStatusImage.setImage(ImageFactory.getInstance().getRegisteredImage("server.status.offline"));
					secondStatusText.setText("Failback Server offline");
					secondStatusDesc.setText("Keine Verbindungen zum Failback Server möglich.");	
					secondServerInfo.setText("Verbindung am "+ MyUtils.timestampToString(Calendar.getInstance().getTimeInMillis(), MyUtils.timeAndDateFormat)+" getrennt");
					//refresch
					secondSection.layout(true);
				}
			}
		});
	}
}
