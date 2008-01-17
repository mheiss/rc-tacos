package at.rc.tacos.client.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.Transport;

public class TransportStatiForm implements ITransportStatus
{
	protected Shell shell;
	Transport transport = new Transport();
	private Text aufgenommen;
	private Text auftragErteilt;
	private Text s1;
	private Text s2;
	private Text s3;
	private Text s4;
	private Text s5;
	private Text s6;
	private Text s7;
	private Text s8;
	private Text s9;
	
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

		aufgenommen = new Text(shell, SWT.BORDER);
		aufgenommen.setEditable(false);
		aufgenommen.setEnabled(false);
		aufgenommen.setBounds(82, 33, 80, 25);

		auftragErteilt = new Text(shell, SWT.BORDER);
		auftragErteilt.setEditable(false);
		auftragErteilt.setEnabled(false);
		auftragErteilt.setBounds(82, 64, 80, 25);

		s1 = new Text(shell, SWT.BORDER);
		s1.setBounds(223, 33, 80, 25);

		s2 = new Text(shell, SWT.BORDER);
		s2.setBounds(223, 64, 80, 25);

		s3 = new Text(shell, SWT.BORDER);
		s3.setBounds(223, 95, 80, 25);

		s4 = new Text(shell, SWT.BORDER);
		s4.setBounds(223, 126, 80, 25);

		s5 = new Text(shell, SWT.BORDER);
		s5.setBounds(223, 157, 80, 25);

		s6 = new Text(shell, SWT.BORDER);
		s6.setBounds(223, 188, 80, 25);

		s7 = new Text(shell, SWT.BORDER);
		s7.setBounds(223, 219, 80, 25);

		s8 = new Text(shell, SWT.BORDER);
		s8.setBounds(223, 250, 80, 25);

		s9 = new Text(shell, SWT.BORDER);
		s9.setBounds(223, 281, 80, 25);

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

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setText("OK");
		okButton.setBounds(337, 358, 89, 23);
	}
	
	public void setContent()
	{
        //formatter for the date and time
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
		
		//receive time
        cal.setTimeInMillis(transport.getReceiveTime());
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
			s1.setText(sdf.format(cal.getTime()));
		}
		//Status 2
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
			s2.setText(sdf.format(cal.getTime()));
		}       
		//Status 3
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
			s3.setText(sdf.format(cal.getTime()));
		}
		//Status 4 
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
			s4.setText(sdf.format(cal.getTime()));
		}
    	//Status 5
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE));
			s5.setText(sdf.format(cal.getTime()));
		}
    	//Status 6
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION));
			s6.setText(sdf.format(cal.getTime()));
		}
    	//Status 7
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA));
			s7.setText(sdf.format(cal.getTime()));
		}
    	//Status 8
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA));
			s8.setText(sdf.format(cal.getTime()));
		}
    	//Status 9
		if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
		{
			cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA));
			s9.setText(sdf.format(cal.getTime()));
		} 
	}
}
