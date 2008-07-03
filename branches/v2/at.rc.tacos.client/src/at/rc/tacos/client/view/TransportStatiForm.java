package at.rc.tacos.client.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.swtdesigner.SWTResourceManager;

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.util.TimeValidator;
import at.rc.tacos.client.util.TransformTimeToLong;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Transport;

public class TransportStatiForm implements ITransportStatus
{
	protected Shell shell;
	Transport transport = new Transport();
	private Text aufgenommen;
	private Text auftragErteilt;
	private Text textS1;
	private Text textS2;
	private Text textS3;
	private Text textS4;
	
	private Listener exitListener;
	
	/**
     * Open the window
     */
    public void open() 
    {
    	//get the active shell
		Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		
    	//calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = shell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width)/2+workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height)/2+workbenchSize.y;
		shell.setLocation(locationX,locationY);
		
        shell.open();
    }
	
	public TransportStatiForm(Transport transport)
	{
		this.createContents();
		this.transport = transport;
		this.setContent();
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() 
	{
		shell = new Shell();
		shell.addShellListener(new ShellAdapter() {
			public void shellClosed(final ShellEvent e) 
			{
				LockManager.removeLock(Transport.ID, transport.getTransportId());
			}
		});
		shell.setSize(288, 334);
		shell.setText("Transportstati bearbeiten");
		
		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) 
			{
				LockManager.removeLock(Transport.ID, transport.getTransportId());
				shell.dispose();
			}
		};

		shell.setImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
		
		aufgenommen = new Text(shell, SWT.BORDER);
		aufgenommen.setEditable(false);
		aufgenommen.setEnabled(false);
		aufgenommen.setBounds(95, 80, 47, 21);

		auftragErteilt = new Text(shell, SWT.BORDER);
		auftragErteilt.setEditable(false);
		auftragErteilt.setEnabled(false);
		auftragErteilt.setBounds(95, 107, 47, 21);

		textS1 = new Text(shell, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		textS1.setBounds(95, 134, 47, 21);

		textS2 = new Text(shell, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		textS2.setBounds(95, 161, 47, 21);

		textS3 = new Text(shell, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		textS3.setBounds(95, 188, 47, 21);

		textS4 = new Text(shell, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		textS4.setBounds(95, 215, 47, 21);

		final Label aeLabel = new Label(shell, SWT.NONE);
		aeLabel.setText("Aufgenommen");
		aeLabel.setBounds(10, 83, 79, 13);

		final Label aeLabel_1 = new Label(shell, SWT.NONE);
		aeLabel_1.setText("Auftrag erteilt");
		aeLabel_1.setBounds(10, 110, 79, 13);

		final Label sLabel = new Label(shell, SWT.NONE);
		sLabel.setText("S1");
		sLabel.setBounds(64, 137, 25, 13);

		final Label s2Label = new Label(shell, SWT.NONE);
		s2Label.setText("S2");
		s2Label.setBounds(64, 164, 25, 13);

		final Label s3Label = new Label(shell, SWT.NONE);
		s3Label.setText("S3");
		s3Label.setBounds(64, 191, 25, 13);

		final Label s4Label = new Label(shell, SWT.NONE);
		s4Label.setText("S4");
		s4Label.setBounds(64, 218, 25, 13);

		final Label fahrzeugUnterwegsLabel = new Label(shell, SWT.NONE);
		fahrzeugUnterwegsLabel.setText("Fahrzeug unterwegs");
		fahrzeugUnterwegsLabel.setBounds(148, 137, 117, 13);

		final Label ankunftBeiPatientLabel = new Label(shell, SWT.NONE);
		ankunftBeiPatientLabel.setText("Ankunft bei Patient");
		ankunftBeiPatientLabel.setBounds(148, 164, 98, 13);

		final Label abfahrtMitPatientLabel = new Label(shell, SWT.NONE);
		abfahrtMitPatientLabel.setText("Abfahrt mit Patient");
		abfahrtMitPatientLabel.setBounds(148, 191, 98, 13);

		final Label ankunftZielLabel = new Label(shell, SWT.NONE);
		ankunftZielLabel.setText("Ankunft Ziel");
		ankunftZielLabel.setBounds(148, 218, 98, 13);

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setText("Abbrechen");
		abbrechenButton.setBounds(181, 267, 89, 23);
		abbrechenButton.addListener(SWT.Selection, exitListener);

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setText("OK");
		okButton.setBounds(86, 267, 89, 23);
		
		okButton.addListener(SWT.Selection, new Listener()
		{
			String s1;
			String s2;
			String s3;
			String s4;
			
			long s1Long;
			long s2Long;
			long s3Long;
			long s4Long;
			
			String formatOfTime;
			String formatOfTransportStati = "";	

			@Override
			public void handleEvent(Event event) 
			{
				formatOfTime = "";
				
				//get content of all fields
				this.getContentOfAllFields();
				
				//validating
				if(!this.checkFormatOfTransportStatusTimeFields().trim().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event,formatOfTime, "Format von Statuszeiten falsch: ");	
					return;
				}
				
				this.transformTransportStatiToLong();
				
            	if(!s1.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_ON_THE_WAY, s1Long);
            	else
            		transport.removeStatus(TRANSPORT_STATUS_ON_THE_WAY);
            	if(!s2.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_AT_PATIENT, s2Long);
            	else
            		transport.removeStatus(TRANSPORT_STATUS_AT_PATIENT);
            	if(!s3.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_START_WITH_PATIENT,s3Long);
            	else
            		transport.removeStatus(TRANSPORT_STATUS_START_WITH_PATIENT);
            	if(!s4.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_AT_DESTINATION, s4Long);
            	else
            		transport.removeStatus(TRANSPORT_STATUS_AT_DESTINATION);
        
            	NetWrapper.getDefault().sendUpdateMessage(Transport.ID, transport);
            	    
            	LockManager.removeLock(Transport.ID, transport.getTransportId());
                shell.close();
			}
			
			private void getContentOfAllFields()
			{
				s1 = textS1.getText();
				s2 = textS2.getText();
				s3 = textS3.getText();
				s4 = textS4.getText();
			}
			
			//checks the time against a valid format, returns a String with the not valid times and the ":" if needed (1234 --> 12:34)
			private String checkFormatOfTransportStatusTimeFields()
			{
				TimeValidator tv = new TimeValidator();
				
				if(s1 != null)
				{
					tv.checkTime(s1,"S1");
					formatOfTransportStati = tv.getCheckStatus();
					s1 = tv.getTime();
				}
				
				if(s2 != null)
				{
					tv.checkTime(s2,"S2");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s2 = tv.getTime();
				}
				
				if(s3 != null)
				{
					tv.checkTime(s3,"S3");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s3 = tv.getTime();
				}
				
				if(s4 != null)
				{
					tv.checkTime(s4,"S4");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s4 = tv.getTime();
				}	
				return formatOfTransportStati;
			}
			
			private void transformTransportStatiToLong()
			{
				TransformTimeToLong tttl = new TransformTimeToLong();
				
				if(s1 != null)
				{
					s1Long = tttl.transform(s1);
				}
				if(s2 != null)
				{
					s2Long = tttl.transform(s2);
				}
				if(s3 != null)
				{
					s3Long = tttl.transform(s3);
				}
				if(s4 != null)
				{
					s4Long = tttl.transform(s4);
				}
			}
			
			private void displayMessageBox(Event event, String fields, String message)
			{
				 MessageBox mb = new MessageBox(shell, 0);
			     mb.setText(message);
			     mb.setMessage(fields);
			     mb.open();
			     if(event.type == SWT.Close) event.doit = false;
			}	
		});

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackground(SWTResourceManager.getColor(255, 255, 255));
		composite.setBounds(0, 0, 280, 64);

		final Label transportstatiLabel = new Label(composite, SWT.NONE);
		transportstatiLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		transportstatiLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		transportstatiLabel.setText("Transport");
		transportstatiLabel.setBounds(10, 10, 89, 20);

		final Label transportstatiBearbeitenLabel = new Label(composite, SWT.NONE);
		transportstatiBearbeitenLabel.setBackground(SWTResourceManager.getColor(255, 255, 255));
		transportstatiBearbeitenLabel.setText("Transportstati bearbeiten");
		transportstatiBearbeitenLabel.setBounds(10, 36, 140, 13);

		final Label label = new Label(composite, SWT.NONE);
		label.setBounds(207, 0,73, 64);
		label.setBackgroundImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
	}
	
	public void setContent()
	{
        //formatter for the date and time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
		
		//receive time
        cal.setTimeInMillis(transport.getCreationTime());
		aufgenommen.setText(sdf.format(cal.getTime()));
		
    	if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
			auftragErteilt.setText(sdf.format(cal.getTime()));
		}
		//Status 0 
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
			textS1.setText(sdf.format(cal.getTime()));
		}
		//Status 2
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
			textS2.setText(sdf.format(cal.getTime()));
		}       
		//Status 3
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
			textS3.setText(sdf.format(cal.getTime()));
		}
		//Status 4 
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
			textS4.setText(sdf.format(cal.getTime()));
		}
	}
}
