package at.rc.tacos.client.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.client.controller.UpdateTransportAction;
import at.rc.tacos.client.util.TimeValidator;
import at.rc.tacos.client.util.TransformTimeToLong;
import at.rc.tacos.common.ITransportStatus;
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
	private Text textS5;
	private Text textS6;
	private Text textS7;
	private Text textS8;
	private Text textS9;
	
	private Listener exitListener;
	
	/**
     * Open the window
     */
    public void open() 
    {
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
		shell.setSize(533, 428);
		shell.setText("Transportstati bearbeiten");
		
		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) 
			{
				shell.dispose();
			}
		};

		aufgenommen = new Text(shell, SWT.BORDER);
		aufgenommen.setEditable(false);
		aufgenommen.setEnabled(false);
		aufgenommen.setBounds(82, 33, 80, 25);

		auftragErteilt = new Text(shell, SWT.BORDER);
		auftragErteilt.setEditable(false);
		auftragErteilt.setEnabled(false);
		auftragErteilt.setBounds(82, 64, 80, 25);

		textS1 = new Text(shell, SWT.BORDER);
		textS1.setBounds(223, 33, 80, 25);

		textS2 = new Text(shell, SWT.BORDER);
		textS2.setBounds(223, 64, 80, 25);

		textS3 = new Text(shell, SWT.BORDER);
		textS3.setBounds(223, 95, 80, 25);

		textS4 = new Text(shell, SWT.BORDER);
		textS4.setBounds(223, 126, 80, 25);

		textS5 = new Text(shell, SWT.BORDER);
		textS5.setBounds(223, 157, 80, 25);
		textS5.setEditable(false);

		textS6 = new Text(shell, SWT.BORDER);
		textS6.setBounds(223, 188, 80, 25);

		textS7 = new Text(shell, SWT.BORDER);
		textS7.setBounds(223, 219, 80, 25);

		textS8 = new Text(shell, SWT.BORDER);
		textS8.setBounds(223, 250, 80, 25);

		textS9 = new Text(shell, SWT.BORDER);
		textS9.setBounds(223, 281, 80, 25);

		final Label aeLabel = new Label(shell, SWT.NONE);
		aeLabel.setText("Aufgenommen");
		aeLabel.setBounds(10, 45, 79, 13);

		final Label aeLabel_1 = new Label(shell, SWT.NONE);
		aeLabel_1.setText("Auftrag erteilt");
		aeLabel_1.setBounds(10, 76, 79, 13);

		final Label sLabel = new Label(shell, SWT.NONE);
		sLabel.setText("S1");
		sLabel.setBounds(192, 45, 25, 13);

		final Label s2Label = new Label(shell, SWT.NONE);
		s2Label.setText("S2");
		s2Label.setBounds(192, 76, 25, 13);

		final Label s3Label = new Label(shell, SWT.NONE);
		s3Label.setText("S3");
		s3Label.setBounds(192, 107, 25, 13);

		final Label s4Label = new Label(shell, SWT.NONE);
		s4Label.setText("S4");
		s4Label.setBounds(192, 138, 25, 13);

		final Label s5Label = new Label(shell, SWT.NONE);
		s5Label.setText("S5");
		s5Label.setBounds(192, 169, 25, 13);

		final Label s6Label = new Label(shell, SWT.NONE);
		s6Label.setText("S6");
		s6Label.setBounds(192, 200, 25, 13);

		final Label s7Label = new Label(shell, SWT.NONE);
		s7Label.setText("S7");
		s7Label.setBounds(192, 231, 25, 13);

		final Label s8Label = new Label(shell, SWT.NONE);
		s8Label.setText("S8");
		s8Label.setBounds(192, 262, 25, 13);

		final Label s9Label = new Label(shell, SWT.NONE);
		s9Label.setText("S9");
		s9Label.setBounds(192, 293, 25, 13);

		final Label fahrzeugUnterwegsLabel = new Label(shell, SWT.NONE);
		fahrzeugUnterwegsLabel.setText("Fahrzeug unterwegs");
		fahrzeugUnterwegsLabel.setBounds(309, 45, 117, 13);

		final Label ankunftBeiPatientLabel = new Label(shell, SWT.NONE);
		ankunftBeiPatientLabel.setText("Ankunft bei Patient");
		ankunftBeiPatientLabel.setBounds(309, 76, 98, 13);

		final Label abfahrtMitPatientLabel = new Label(shell, SWT.NONE);
		abfahrtMitPatientLabel.setText("Abfahrt mit Patient");
		abfahrtMitPatientLabel.setBounds(309, 107, 98, 13);

		final Label ankunftZielLabel = new Label(shell, SWT.NONE);
		ankunftZielLabel.setText("Ankunft Ziel");
		ankunftZielLabel.setBounds(309, 138, 98, 13);

		final Label zielFreiLabel = new Label(shell, SWT.NONE);
		zielFreiLabel.setText("Ziel frei");
		zielFreiLabel.setBounds(309, 169, 98, 13);

		final Label label = new Label(shell, SWT.NONE);
		label.setText("Fahrzeug eingerückt");
		label.setBounds(309, 200, 98, 13);

		final Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("Fahrzeug verlässt Einsatzgebiet");
		label_1.setBounds(309, 231, 162, 13);

		final Label label_2 = new Label(shell, SWT.NONE);
		label_2.setText("Fahrzeug ist zurück im Einsatzgebiet");
		label_2.setBounds(309, 262, 195, 13);

		final Label sonderstatusLabel = new Label(shell, SWT.NONE);
		sonderstatusLabel.setText("Sonderstatus");
		sonderstatusLabel.setBounds(309, 293, 98, 13);

		final Label label_3 = new Label(shell, SWT.NONE);
		label_3.setText("Label");
		label_3.setBounds(10, 129, 152, 177);

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setText("Abbrechen");
		abbrechenButton.setBounds(426, 358, 89, 23);
		abbrechenButton.addListener(SWT.Selection, exitListener);

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setText("OK");
		okButton.setBounds(337, 358, 89, 23);
		//TODO - implement reaction to ok and cancel  and  validate the entries!
		
		okButton.addListener(SWT.Selection, new Listener()
		{
			String s1;
			String s2;
			String s3;
			String s4;
			String s5;
			String s6;
			String s7;
			String s8;
			String s9;
			
			long s1Long;
			long s2Long;
			long s3Long;
			long s4Long;
			long s5Long;
			long s6Long;
			long s7Long;
			long s8Long;
			long s9Long;	
			
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
            	if(!s2.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_AT_PATIENT, s2Long);
            	if(!s3.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_START_WITH_PATIENT,s3Long);
            	if(!s4.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_AT_DESTINATION, s4Long);
            	if(!s5.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_DESTINATION_FREE,s5Long);
            	if(!s6.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_CAR_IN_STATION, s6Long);
            	if(!s7.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA,s7Long);
            	if(!s8.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA,s8Long);
            	if(!s9.equalsIgnoreCase(""))
            		transport.addStatus(TRANSPORT_STATUS_OTHER, s9Long);
            	
            	//create and run the update action
                UpdateTransportAction updateAction = new UpdateTransportAction(transport);
                updateAction.run();
                
                shell.close();
				
			}
			
			private void getContentOfAllFields()
			{
				s1 = textS1.getText();
				s2 = textS2.getText();
				s3 = textS3.getText();
				s4 = textS4.getText();
				s5 = textS5.getText();
				s6 = textS6.getText();
				s7 = textS7.getText();
				s8 = textS8.getText();
				s9 = textS9.getText();
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
					System.out.println("TransportForm, checkFormatOfTransportStatusTimeFields- die zurückgegebene Zeit für s1 über tv.getTime(): " +tv.getTime());
					System.out.println("TransportForm, checkFormatOfTransportStatusTimeFields- die zurückgegebene Zeit für s1: " +s1);
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
				
				if(s5 != null)
				{
					tv.checkTime(s5,"S5");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s5 = tv.getTime();
				}
				
				if(s6 != null)
				{
					tv.checkTime(s6,"S6");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s6 = tv.getTime();
				}
				
				if(s7 != null)
				{
					tv.checkTime(s7,"S7");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s7 = tv.getTime();
				}
				
				if(s8 != null)
				{
					tv.checkTime(s8,"S8");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s8 = tv.getTime();
				}
				
				if(s9 != null)
				{
					tv.checkTime(s9,"S9");
					formatOfTransportStati = formatOfTransportStati + " " +tv.getCheckStatus();
					s9 = tv.getTime();	
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
				if(s5 != null)
				{
					s5Long = tttl.transform(s5);
				}
				if(s6 != null)
				{
					s6Long = tttl.transform(s6);
				}
				if(s7 != null)
				{
					s7Long = tttl.transform(s7);
				}
				if(s8 != null)
				{
					s8Long = tttl.transform(s8);
				}
				if(s9 != null)
				{
					s9Long = tttl.transform(s9);
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
    	//Status 5
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE));
			textS5.setText(sdf.format(cal.getTime()));
		}
    	//Status 6
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION));
			textS6.setText(sdf.format(cal.getTime()));
		}
    	//Status 7
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA));
			textS7.setText(sdf.format(cal.getTime()));
		}
    	//Status 8
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA));
			textS8.setText(sdf.format(cal.getTime()));
		}
    	//Status 9
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA));
			textS9.setText(sdf.format(cal.getTime()));
		} 
	}
}
