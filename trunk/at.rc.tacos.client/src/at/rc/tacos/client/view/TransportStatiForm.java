package at.rc.tacos.client.view;

import java.util.GregorianCalendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
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


//	/**
//	 * Launch the application
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			TransportStatiForm window = new TransportStatiForm();
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	/**
//	 * Open the window
//	 */
//	public void open() 
//	{
//		final Display display = Display.getDefault();
//		createContents();
//		shell.open();
//		shell.layout();
//		while (!shell.isDisposed()) {
//			if (!display.readAndDispatch())
//				display.sleep();
//		}
//	}
	
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
		//
	}
	
	public void setContent()
	{
		GregorianCalendar gcal = new GregorianCalendar();
		
		//receive time
        gcal.setTimeInMillis(transport.getReceiveTime());
        String receiveTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
		aufgenommen.setText(receiveTime);
	
		//transport stati
		//s0, Auftrag erteilt
		long s0Long = transport.getStatusMessages().get(TRANSPORT_STATUS_ORDER_PLACED).getStatus();
		if(s0Long != 0)
		{
			gcal.setTimeInMillis(s0Long);
			String s0s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			auftragErteilt.setText(s0s);
		}
		else
			auftragErteilt.setText("");
		
	
		//s1 Fahrzeug unterwegs
		long s1Long = transport.getStatusMessages().get(TRANSPORT_STATUS_ON_THE_WAY).getStatus();
		if(s1Long != 0)
		{
			gcal.setTimeInMillis(s1Long);
			String s1s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s1.setText(s1s);
		}
		else
			s1.setText("");
		
		//s2 Ankunft bei Patient
		long s2Long = transport.getStatusMessages().get(TRANSPORT_STATUS_AT_PATIENT).getStatus();
		if(s2Long != 0)
		{
			gcal.setTimeInMillis(s2Long);
			String s2s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s2.setText(s2s);
		}
		else
			s2.setText("");
		
		//s3 Abfahrt mit Patient
		long s3Long = transport.getStatusMessages().get(TRANSPORT_STATUS_START_WITH_PATIENT).getStatus();
		if(s3Long != 0)
		{
			gcal.setTimeInMillis(s3Long);
			String s3s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s3.setText(s3s);
		}
		else
			s3.setText("");
		
		//s4 Ankunft Ziel
		long s4Long = transport.getStatusMessages().get(TRANSPORT_STATUS_AT_DESTINATION).getStatus();
		if(s4Long != 0)
		{
			gcal.setTimeInMillis(s4Long);
			String s4s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s4.setText(s4s);
		}
		else
			s4.setText("");
		
		//s5 Ziel frei
		long s5Long = transport.getStatusMessages().get(TRANSPORT_STATUS_DESTINATION_FREE).getStatus();
		if(s5Long != 0)
		{
			gcal.setTimeInMillis(s5Long);
			String s5s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s5.setText(s5s);
		}
		else
			s5.setText("");
		
		//s6 Eingerückt
		long s6Long = transport.getStatusMessages().get(TRANSPORT_STATUS_CAR_IN_STATION).getStatus();
		if(s6Long != 0)
		{
			gcal.setTimeInMillis(s6Long);
			String s6s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s6.setText(s6s);
		}
		else
			s6.setText("");
		
		//s7 Verlässt Einsatzgebiet
		long s7Long = transport.getStatusMessages().get(TRANSPORT_STATUS_OUT_OF_OPERATION_AREA).getStatus();
		if(s7Long != 0)
		{
			gcal.setTimeInMillis(s7Long);
			String s7s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s7.setText(s7s);
		}
		else
			s7.setText("");
		
		//s8 Zurück im Einsatzgebiet
		long s8Long = transport.getStatusMessages().get(TRANSPORT_STATUS_BACK_IN_OPERATION_AREA).getStatus();
		if(s8Long != 0)
		{
			gcal.setTimeInMillis(s8Long);
			String s8s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s8.setText(s8s);
		}
		else
			s8.setText("");
		
		//s9 Sonderstatus
		long s9Long = transport.getStatusMessages().get(TRANSPORT_STATUS_OTHER).getStatus();
		if(s9Long != 0)
		{
			gcal.setTimeInMillis(s9Long);
			String s9s = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
			s9.setText(s9s);
		}
		else
			s1.setText("");
	
	}

}
