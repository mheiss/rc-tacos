package at.rc.tacos.client.view;

//rcp
import org.eclipse.swt.SWT;
//client
import at.rc.tacos.swtdesigner.SWTResourceManager;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * GUI to manage the details of a transport
 * @author b.thek
 */
public class TransportView 
{
	private Button gehendButton;
	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TransportView window = new TransportView();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(TransportView.class, "image/tacos2.bmp"));
		shell.setSize(1075, 545);
		shell.setText("Transport");

		final DateTime dateTime = new DateTime(shell, SWT.CALENDAR);
		dateTime.setBounds(10, 10, 177, 150);

		final Group transportdatenGroup = new Group(shell, SWT.NONE);
		transportdatenGroup.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportdatenGroup.setText("Transportdaten");
		transportdatenGroup.setBounds(194, 10, 862, 150);

		final Label vonLabel = new Label(transportdatenGroup, SWT.NONE);
		vonLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		vonLabel.setText("von:");
		vonLabel.setBounds(10, 42, 25, 13);

		final Combo combo_1 = new Combo(transportdatenGroup, SWT.NONE);
		combo_1.setBounds(41, 66, 194, 21);

		final Combo combo = new Combo(transportdatenGroup, SWT.NONE);
		combo.setBounds(41, 39, 194, 21);

		final Label nachLabel = new Label(transportdatenGroup, SWT.NONE);
		nachLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachLabel.setText("nach:");
		nachLabel.setBounds(10, 69, 25, 13);

		final Label label = new Label(transportdatenGroup, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label.setText("Straße");
		label.setBounds(41, 20, 56, 13);

		final Combo combo_2 = new Combo(transportdatenGroup, SWT.NONE);
		combo_2.setBounds(241, 39, 75, 21);

		final Combo combo_3 = new Combo(transportdatenGroup, SWT.NONE);
		combo_3.setBounds(241, 66, 75, 21);

		final Label label_1 = new Label(transportdatenGroup, SWT.NONE);
		label_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_1.setText("Nr./Stock/Tür");
		label_1.setBounds(241, 20, 75, 13);

		final Combo combo_4 = new Combo(transportdatenGroup, SWT.NONE);
		combo_4.setBounds(322, 39, 111, 21);

		final Combo combo_5 = new Combo(transportdatenGroup, SWT.NONE);
		combo_5.setBounds(322, 66, 111, 21);

		final Label ortLabel = new Label(transportdatenGroup, SWT.NONE);
		ortLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ortLabel.setText("Ort");
		ortLabel.setBounds(322, 20, 25, 13);

		final Combo combo_6 = new Combo(transportdatenGroup, SWT.NONE);
		combo_6.setBounds(467, 39, 171, 21);

		final Label nachnameLabel = new Label(transportdatenGroup, SWT.NONE);
		nachnameLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachnameLabel.setText("Nachname");
		nachnameLabel.setBounds(467, 20, 56, 13);

		final Combo combo_7 = new Combo(transportdatenGroup, SWT.NONE);
		combo_7.setBounds(644, 39, 171, 21);

		final Button button = new Button(transportdatenGroup, SWT.NONE);
		button.setBounds(821, 37,32, 23);
		button.setText("...");

		final Label nachnameLabel_1 = new Label(transportdatenGroup, SWT.NONE);
		nachnameLabel_1.setBounds(644, 20, 56, 13);
		nachnameLabel_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachnameLabel_1.setText("Vorname");

		gehendButton = new Button(transportdatenGroup, SWT.RADIO);
		gehendButton.setText("gehend");
		gehendButton.setBounds(467, 67, 83, 16);

		final Button tragsesselButton = new Button(transportdatenGroup, SWT.RADIO);
		tragsesselButton.setText("Tragsessel");
		tragsesselButton.setBounds(556, 67, 83, 16);

		final Button krankentrageButton = new Button(transportdatenGroup, SWT.RADIO);
		krankentrageButton.setText("Krankentrage");
		krankentrageButton.setBounds(654, 67, 83, 16);

		final Button eigenerRollstuhlButton = new Button(transportdatenGroup, SWT.RADIO);
		eigenerRollstuhlButton.setText("Eigener Rollstuhl");
		eigenerRollstuhlButton.setBounds(753, 67, 100, 16);

		final Button begleitpersonButton = new Button(transportdatenGroup, SWT.CHECK);
		begleitpersonButton.setText("Begleitperson");
		begleitpersonButton.setBounds(465, 113, 85, 16);

		final Button button_1 = new Button(transportdatenGroup, SWT.CHECK);
		button_1.setText("Rücktransport möglich");
		button_1.setBounds(41, 93, 121, 16);

		final Button rufhilfepatientButton = new Button(transportdatenGroup, SWT.CHECK);
		rufhilfepatientButton.setText("Rufhilfepatient");
		rufhilfepatientButton.setBounds(465, 93, 85, 16);

		final Label anruferLabel = new Label(transportdatenGroup, SWT.NONE);
		anruferLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		anruferLabel.setText("Anrufer:");
		anruferLabel.setBounds(613, 95, 47, 13);

		final Text text_5 = new Text(transportdatenGroup, SWT.BORDER);
		text_5.setBounds(666, 89, 187, 21);

		final Label telefonLabel = new Label(transportdatenGroup, SWT.NONE);
		telefonLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		telefonLabel.setText("Telefon:");
		telefonLabel.setBounds(613, 115, 47, 13);

		final Text text_5_1 = new Text(transportdatenGroup, SWT.BORDER);
		text_5_1.setBounds(666, 112, 187, 21);

		final Label label_6 = new Label(transportdatenGroup, SWT.NONE);
		label_6.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_6.setText("Zuständige Ortsstelle:");
		label_6.setBounds(205, 118, 111, 13);

		final Combo combo_10 = new Combo(transportdatenGroup, SWT.NONE);
		combo_10.setItems(new String[] {"Breitenau", "Bruck an der Mur", "Kapfenberg", "St. Marein", "Thörl", "Turnau"});
		combo_10.setBounds(322, 113, 112, 21);

		final Group planungGroup = new Group(shell, SWT.NONE);
		planungGroup.setText("Zeiten/Richtung");
		planungGroup.setBounds(10, 166, 177, 182);

		final Label abfLabel = new Label(planungGroup, SWT.NONE);
		abfLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		abfLabel.setText("Abf:");
		abfLabel.setBounds(10, 37, 25, 13);

		final Label beiPatLabel = new Label(planungGroup, SWT.NONE);
		beiPatLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		beiPatLabel.setText("Pat.:");
		beiPatLabel.setBounds(10, 64, 25, 13);

		final Label terminLabel = new Label(planungGroup, SWT.NONE);
		terminLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		terminLabel.setText("Term.");
		terminLabel.setBounds(10, 91, 28, 13);

		final Text text = new Text(planungGroup, SWT.BORDER);
		text.setBounds(41, 61,41, 21);

		final Text text_1 = new Text(planungGroup, SWT.BORDER);
		text_1.setBounds(41, 88, 41, 21);

		final Text text_2 = new Text(planungGroup, SWT.BORDER);
		text_2.setBounds(40, 34, 41, 21);

		final Button mariazellButton = new Button(planungGroup, SWT.RADIO);
		mariazellButton.setBounds(104, 140,73, 16);
		mariazellButton.setText("Mariazell");

		final Button wienButton = new Button(planungGroup, SWT.RADIO);
		wienButton.setBounds(104, 115,73, 16);
		wienButton.setText("Wien");

		final Button leobenButton = new Button(planungGroup, SWT.RADIO);
		leobenButton.setBounds(104, 90,73, 16);
		leobenButton.setText("Leoben");

		final Button grazButton = new Button(planungGroup, SWT.RADIO);
		grazButton.setBounds(104, 65,73, 16);
		grazButton.setText("Graz");

		final Button bezirkButton = new Button(planungGroup, SWT.RADIO);
		bezirkButton.setBounds(104, 32,71, 16);
		bezirkButton.setText("Bezirk");

		final Label label_2 = new Label(planungGroup, SWT.SEPARATOR);
		label_2.setBounds(84, 24,13, 148);

		final Button fernfahrtButton = new Button(planungGroup, SWT.CHECK);
		fernfahrtButton.setText("Fernfahrt");
		fernfahrtButton.setBounds(10, 140, 73, 16);

		final Group patientenzustandGroup = new Group(shell, SWT.NONE);
		patientenzustandGroup.setText("Patientenzustand");
		patientenzustandGroup.setBounds(194, 166, 679, 182);

		final Combo combo_8 = new Combo(patientenzustandGroup, SWT.NONE);
		combo_8.setBounds(10, 42, 282, 21);

		final Text text_3 = new Text(patientenzustandGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		text_3.setBounds(10, 88, 282, 84);

		final Label erkrankungverletzungLabel = new Label(patientenzustandGroup, SWT.NONE);
		erkrankungverletzungLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		erkrankungverletzungLabel.setText("Erkrankung/Verletzung");
		erkrankungverletzungLabel.setBounds(10, 24, 127, 13);

		final Label anmerkungenLabel = new Label(patientenzustandGroup, SWT.NONE);
		anmerkungenLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		anmerkungenLabel.setText("Anmerkungen");
		anmerkungenLabel.setBounds(10, 69, 127, 13);

		final Text text_4 = new Text(patientenzustandGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		text_4.setBounds(358, 42, 311, 130);

		final Label label_3 = new Label(patientenzustandGroup, SWT.NONE);
		label_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_3.setText("Rückmeldung");
		label_3.setBounds(358, 24, 163, 13);

		final Button bd1Button = new Button(patientenzustandGroup, SWT.CHECK);
		bd1Button.setText("BD 1");
		bd1Button.setBounds(246, 20, 43, 16);

		final Button bd2Button = new Button(patientenzustandGroup, SWT.CHECK);
		bd2Button.setText("BD 2");
		bd2Button.setBounds(626, 22, 43, 16);

		final Combo combo_9 = new Combo(patientenzustandGroup, SWT.NONE);
		combo_9.setBounds(228, 65, 62, 21);

		final Label label_4 = new Label(patientenzustandGroup, SWT.NONE);
		label_4.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_4.setText("Priorität:");
		label_4.setBounds(180, 69, 43, 13);

		final Group planungGroup_1 = new Group(shell, SWT.NONE);
		planungGroup_1.setBounds(879, 166, 177, 182);
		planungGroup_1.setText("Alarmierung");

		final Button notarztButton = new Button(planungGroup_1, SWT.CHECK);
		notarztButton.setText("Notarzt");
		notarztButton.setBounds(10, 24, 85, 16);

		final Button rthButton = new Button(planungGroup_1, SWT.CHECK);
		rthButton.setText("RTH");
		rthButton.setBounds(10, 46, 85, 16);

		final Button dfButton = new Button(planungGroup_1, SWT.CHECK);
		dfButton.setText("DF/Insp.");
		dfButton.setBounds(10, 68, 73, 16);

		final Button brkdtButton = new Button(planungGroup_1, SWT.CHECK);
		brkdtButton.setText("BRKDT");
		brkdtButton.setBounds(10, 90, 85, 16);

		final Button feuerwehrButton = new Button(planungGroup_1, SWT.CHECK);
		feuerwehrButton.setText("Feuerwehr");
		feuerwehrButton.setBounds(10, 112, 73, 16);

		final Button polizeiButton = new Button(planungGroup_1, SWT.CHECK);
		polizeiButton.setText("Polizei");
		polizeiButton.setBounds(10, 134, 85, 16);

		final Button bergrettungButton = new Button(planungGroup_1, SWT.CHECK);
		bergrettungButton.setText("Bergrettung");
		bergrettungButton.setBounds(10, 156, 85, 16);

		final DateTime dateTime_1 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1.setBounds(97, 129, 73, 21);

		final DateTime dateTime_1_1 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1_1.setBounds(97, 107, 73, 21);

		final DateTime dateTime_1_2 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1_2.setBounds(97, 85, 73, 21);

		final DateTime dateTime_1_3 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1_3.setBounds(97, 63, 73, 21);

		final DateTime dateTime_1_4 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1_4.setBounds(97, 19, 73, 21);

		final DateTime dateTime_1_5 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1_5.setBounds(97, 41, 73, 21);

		final DateTime dateTime_1_6 = new DateTime(planungGroup_1, SWT.TIME);
		dateTime_1_6.setBounds(97, 151, 73, 21);

		final Label label_5 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label_5.setBounds(10, 354, 1046, 13);

		final Group transportdetailsGroup = new Group(shell, SWT.NONE);
		transportdetailsGroup.setText("Transportdetails");
		transportdetailsGroup.setBounds(10, 373, 177, 128);

		final Label transportnumemmerLabel = new Label(transportdetailsGroup, SWT.NONE);
		transportnumemmerLabel.setBounds(10, 27,47, 13);
		transportnumemmerLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel.setText("Trsp.Nr.:");

		final Text text_5_2 = new Text(transportdetailsGroup, SWT.BORDER);
		text_5_2.setBounds(63, 24,98, 21);

		final Label transportnumemmerLabel_1 = new Label(transportdetailsGroup, SWT.NONE);
		transportnumemmerLabel_1.setBounds(10, 54,50, 13);
		transportnumemmerLabel_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_1.setText("Ortsstelle:");

		final Text text_5_2_1 = new Text(transportdetailsGroup, SWT.BORDER);
		text_5_2_1.setBounds(63, 51,98, 21);

		final Text text_5_2_1_1 = new Text(transportdetailsGroup, SWT.BORDER);
		text_5_2_1_1.setBounds(63, 78, 98, 21);

		final Label transportnumemmerLabel_1_1 = new Label(transportdetailsGroup, SWT.NONE);
		transportnumemmerLabel_1_1.setBounds(10, 81, 50, 13);
		transportnumemmerLabel_1_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_1_1.setText("Fahrzeug:");

		final Group personalAmFahrzeugGroup = new Group(shell, SWT.NONE);
		personalAmFahrzeugGroup.setText("Personal am Fahrzeug");
		personalAmFahrzeugGroup.setBounds(194, 373, 289, 128);

		final Text text_5_2_2 = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		text_5_2_2.setBounds(76, 24, 203, 21);

		final Text text_5_2_3 = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		text_5_2_3.setBounds(76, 51, 203, 21);

		final Text text_5_2_4 = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		text_5_2_4.setBounds(76, 78, 203, 21);

		final Label transportnumemmerLabel_2 = new Label(personalAmFahrzeugGroup, SWT.NONE);
		transportnumemmerLabel_2.setBounds(10, 27, 47, 13);
		transportnumemmerLabel_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_2.setText("Fahrer:");

		final Label transportnumemmerLabel_3 = new Label(personalAmFahrzeugGroup, SWT.NONE);
		transportnumemmerLabel_3.setBounds(10, 54, 61, 13);
		transportnumemmerLabel_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_3.setText("Sanitäter I:");

		final Label transportnumemmerLabel_4 = new Label(personalAmFahrzeugGroup, SWT.NONE);
		transportnumemmerLabel_4.setBounds(10, 81, 61, 13);
		transportnumemmerLabel_4.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_4.setText("Sanitäter II:");

		final Group statusmeldungenGroup = new Group(shell, SWT.NONE);
		statusmeldungenGroup.setText("Statusmeldungen");
		statusmeldungenGroup.setBounds(489, 373, 354, 128);

		final Text text_2_1 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1.setBounds(47, 24, 41, 21);

		final Label transportnumemmerLabel_5 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5.setBounds(15, 26, 31, 13);
		transportnumemmerLabel_5.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5.setText("Aufg.:");

		final Text text_2_1_1 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_1.setBounds(47, 51, 41, 21);

		final Label transportnumemmerLabel_5_1 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_1.setBounds(15, 58, 31, 13);
		transportnumemmerLabel_5_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_1.setText("AE:");

		final Text text_2_1_2 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2.setBounds(135, 24, 41, 21);

		final Label transportnumemmerLabel_5_2 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2.setBounds(113, 26, 31, 13);
		transportnumemmerLabel_5_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2.setText("S1:");

		final Text text_2_1_2_1 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_1.setBounds(135, 51, 41, 21);

		final Text text_2_1_2_2 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_2.setBounds(135, 78, 41, 21);

		final Label transportnumemmerLabel_5_2_1 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_1.setBounds(113, 59, 31, 13);
		transportnumemmerLabel_5_2_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_1.setText("S2:");

		final Label transportnumemmerLabel_5_2_2 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_2.setBounds(113, 86, 31, 13);
		transportnumemmerLabel_5_2_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_2.setText("S3:");

		final Label transportnumemmerLabel_5_2_3 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_3.setBounds(198, 27, 18, 13);
		transportnumemmerLabel_5_2_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3.setText("S4:");

		final Text text_2_1_2_3 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_3.setBounds(217, 24, 41, 21);

		final Text text_2_1_2_3_1 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_3_1.setBounds(217, 51, 41, 21);

		final Text text_2_1_2_3_2 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_3_2.setBounds(217, 78, 41, 21);

		final Label transportnumemmerLabel_5_2_3_1 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_3_1.setBounds(198, 54, 18, 13);
		transportnumemmerLabel_5_2_3_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_1.setText("S5:");

		final Label transportnumemmerLabel_5_2_3_2 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_3_2.setBounds(198, 81, 18, 13);
		transportnumemmerLabel_5_2_3_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_2.setText("S6:");

		final Text text_2_1_2_3_3 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_3_3.setBounds(301, 24, 41, 21);

		final Text text_2_1_2_3_3_1 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_3_3_1.setBounds(301, 51, 41, 21);

		final Text text_2_1_2_3_3_2 = new Text(statusmeldungenGroup, SWT.BORDER);
		text_2_1_2_3_3_2.setBounds(301, 78, 41, 21);

		final Label transportnumemmerLabel_5_2_3_3 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_3_3.setBounds(280, 27, 18, 13);
		transportnumemmerLabel_5_2_3_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_3.setText("S7:");

		final Label transportnumemmerLabel_5_2_3_4 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_3_4.setBounds(280, 54, 18, 13);
		transportnumemmerLabel_5_2_3_4.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_4.setText("S8:");

		final Label transportnumemmerLabel_5_2_3_5 = new Label(statusmeldungenGroup, SWT.NONE);
		transportnumemmerLabel_5_2_3_5.setBounds(280, 81, 18, 13);
		transportnumemmerLabel_5_2_3_5.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_5.setText("S9:");

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(SWTResourceManager.getImage(TransportView.class, "image/LAN Warning.ico"));
		abbrechenButton.setBounds(960, 478, 96, 23);
		abbrechenButton.setText("Abbrechen");

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(858, 478, 96, 23);
		okButton.setText("OK");
		//
	}

}
