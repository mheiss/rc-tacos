package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI (form) to manage the details of a dialysis patient
 * @author b.thek
 *
 */

public class DialysisView {

	private Button abbrechenButton;
	private Button okButton;
	private Group transportdatenGroup;
	private Group patientenzustandGroup;
	private Group planungGroup;
	private Button begleitpersonButton;
	private Combo comboZustOrtsstelle;
	private Button button_1;
	private Button eigenerRollstuhlButton;
	private Button krankentrageButton;
	private Button tragsesselButton;
	private Button button;
	private Combo comboNachOrt;
	private Combo comboNachNr;
	private Combo comboNachStrasse;
	private Combo comboVorname;
	private Combo comboNachname;
	private Combo comboVonOrt;
	private Combo comboVonNr;
	private Combo comboVonStrasse;
	private Button sonntagButton;
	private Button samstagButton;
	private Button freitagButton;
	private Button donnerstagButton;
	private Button mittwochButton;
	private Button dienstagButton;
	private Button montagButton;
	private Text textRT;
	private Text textTermin;
	private Text textBeiPat;
	private Text textAbf;
	private Button gehendButton;
	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DialysisView window = new DialysisView();
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
		shell.setLayout(new FormLayout());
		shell.setImage(SWTResourceManager.getImage(DialysisView.class, "/image/Tacos_LOGO.jpg"));
		shell.setSize(1083, 223);
		shell.setText("Dialysetransport");

		transportdatenGroup = new Group(shell, SWT.NONE);
		final FormData fd_transportdatenGroup = new FormData();
		fd_transportdatenGroup.right = new FormAttachment(0, 1067);
		fd_transportdatenGroup.left = new FormAttachment(0, 205);
		transportdatenGroup.setLayoutData(fd_transportdatenGroup);
		transportdatenGroup.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportdatenGroup.setText("Transportdaten");

		final Label vonLabel = new Label(transportdatenGroup, SWT.NONE);
		vonLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		vonLabel.setText("von:");
		vonLabel.setBounds(10, 42, 25, 13);

		comboNachStrasse = new Combo(transportdatenGroup, SWT.NONE);
		comboNachStrasse.setBounds(41, 66, 194, 21);

		comboVonStrasse = new Combo(transportdatenGroup, SWT.NONE);
		comboVonStrasse.setBounds(41, 39, 194, 21);

		final Label nachLabel = new Label(transportdatenGroup, SWT.NONE);
		nachLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachLabel.setText("nach:");
		nachLabel.setBounds(10, 69, 25, 13);

		final Label label = new Label(transportdatenGroup, SWT.NONE);
		label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label.setText("Straße");
		label.setBounds(41, 20, 56, 13);

		comboVonNr = new Combo(transportdatenGroup, SWT.NONE);
		comboVonNr.setBounds(241, 39, 75, 21);

		comboNachNr = new Combo(transportdatenGroup, SWT.NONE);
		comboNachNr.setBounds(241, 66, 75, 21);

		final Label label_1 = new Label(transportdatenGroup, SWT.NONE);
		label_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_1.setText("Nr./Stock/Tür");
		label_1.setBounds(241, 20, 75, 13);

		comboVonOrt = new Combo(transportdatenGroup, SWT.NONE);
		comboVonOrt.setBounds(322, 39, 111, 21);

		comboNachOrt = new Combo(transportdatenGroup, SWT.NONE);
		comboNachOrt.setBounds(322, 66, 111, 21);

		final Label ortLabel = new Label(transportdatenGroup, SWT.NONE);
		ortLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ortLabel.setText("Ort");
		ortLabel.setBounds(322, 20, 25, 13);

		comboNachname = new Combo(transportdatenGroup, SWT.NONE);
		comboNachname.setBounds(467, 39, 171, 21);

		final Label nachnameLabel = new Label(transportdatenGroup, SWT.NONE);
		nachnameLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachnameLabel.setText("Nachname");
		nachnameLabel.setBounds(467, 20, 56, 13);

		comboVorname = new Combo(transportdatenGroup, SWT.NONE);
		comboVorname.setBounds(644, 39, 171, 21);

		button = new Button(transportdatenGroup, SWT.NONE);
		button.setBounds(821, 37,32, 23);
		button.setText("...");

		final Label nachnameLabel_1 = new Label(transportdatenGroup, SWT.NONE);
		nachnameLabel_1.setBounds(644, 20, 56, 13);
		nachnameLabel_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachnameLabel_1.setText("Vorname");

		gehendButton = new Button(transportdatenGroup, SWT.RADIO);
		gehendButton.setText("gehend");
		gehendButton.setBounds(467, 67, 83, 16);

		tragsesselButton = new Button(transportdatenGroup, SWT.RADIO);
		tragsesselButton.setText("Tragsessel");
		tragsesselButton.setBounds(556, 67, 83, 16);

		krankentrageButton = new Button(transportdatenGroup, SWT.RADIO);
		krankentrageButton.setText("Krankentrage");
		krankentrageButton.setBounds(654, 67, 83, 16);

		eigenerRollstuhlButton = new Button(transportdatenGroup, SWT.RADIO);
		eigenerRollstuhlButton.setText("Eigener Rollstuhl");
		eigenerRollstuhlButton.setBounds(753, 67, 100, 16);

		begleitpersonButton = new Button(transportdatenGroup, SWT.CHECK);
		begleitpersonButton.setText("Begleitperson");
		begleitpersonButton.setBounds(465, 113, 85, 16);

		final Label label_6 = new Label(transportdatenGroup, SWT.NONE);
		label_6.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_6.setText("Zuständige Ortsstelle:");
		label_6.setBounds(205, 118, 111, 13);

		comboZustOrtsstelle = new Combo(transportdatenGroup, SWT.NONE);
		comboZustOrtsstelle.setItems(new String[] {"Breitenau", "Bruck an der Mur", "Kapfenberg", "St. Marein", "Thörl", "Turnau"});
		comboZustOrtsstelle.setBounds(322, 113, 112, 21);

		button_1 = new Button(transportdatenGroup, SWT.CHECK);
		button_1.setText("stationär");
		button_1.setBounds(41, 118, 85, 16);

		planungGroup = new Group(shell, SWT.NONE);
		final FormData fd_planungGroup = new FormData();
		fd_planungGroup.bottom = new FormAttachment(transportdatenGroup, 182, SWT.TOP);
		fd_planungGroup.top = new FormAttachment(transportdatenGroup, 0, SWT.TOP);
		fd_planungGroup.right = new FormAttachment(0, 100);
		fd_planungGroup.left = new FormAttachment(0, 10);
		planungGroup.setLayoutData(fd_planungGroup);
		planungGroup.setText("Zeiten");

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

		textBeiPat = new Text(planungGroup, SWT.BORDER);
		textBeiPat.setBounds(41, 61,41, 21);

		textTermin = new Text(planungGroup, SWT.BORDER);
		textTermin.setBounds(41, 88, 41, 21);

		textAbf = new Text(planungGroup, SWT.BORDER);
		textAbf.setBounds(41, 34, 41, 21);

		final Label terminLabel_1 = new Label(planungGroup, SWT.NONE);
		terminLabel_1.setBounds(10, 118, 28, 13);
		terminLabel_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		terminLabel_1.setText("RT:");

		textRT = new Text(planungGroup, SWT.BORDER);
		textRT.setBounds(41, 115, 41, 21);
		planungGroup.setTabList(new Control[] {textAbf, textBeiPat, textTermin, textRT});

		patientenzustandGroup = new Group(shell, SWT.NONE);
		fd_transportdatenGroup.bottom = new FormAttachment(patientenzustandGroup, 150, SWT.TOP);
		fd_transportdatenGroup.top = new FormAttachment(patientenzustandGroup, 0, SWT.TOP);
		transportdatenGroup.setTabList(new Control[] {comboVonStrasse, comboVonNr, comboVonOrt, comboNachname, comboVorname, comboNachStrasse, comboNachNr, comboNachOrt, gehendButton, tragsesselButton, krankentrageButton, eigenerRollstuhlButton, button_1, comboZustOrtsstelle, begleitpersonButton});
		patientenzustandGroup.setLayout(new FormLayout());
		final FormData fd_patientenzustandGroup = new FormData();
		fd_patientenzustandGroup.right = new FormAttachment(transportdatenGroup, -5, SWT.LEFT);
		fd_patientenzustandGroup.left = new FormAttachment(0, 104);
		fd_patientenzustandGroup.bottom = new FormAttachment(planungGroup, 182, SWT.TOP);
		fd_patientenzustandGroup.top = new FormAttachment(planungGroup, 0, SWT.TOP);
		patientenzustandGroup.setLayoutData(fd_patientenzustandGroup);
		patientenzustandGroup.setText("Wochentage");

		montagButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_montagButton = new FormData();
		fd_montagButton.top = new FormAttachment(0, 5);
		fd_montagButton.left = new FormAttachment(0, 5);
		montagButton.setLayoutData(fd_montagButton);
		montagButton.setText("Montag");

		dienstagButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_dienstagButton = new FormData();
		fd_dienstagButton.top = new FormAttachment(montagButton, 5, SWT.BOTTOM);
		fd_dienstagButton.left = new FormAttachment(montagButton, 0, SWT.LEFT);
		dienstagButton.setLayoutData(fd_dienstagButton);
		dienstagButton.setText("Dienstag");

		mittwochButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_mittwochButton = new FormData();
		fd_mittwochButton.top = new FormAttachment(dienstagButton, 5, SWT.BOTTOM);
		fd_mittwochButton.left = new FormAttachment(dienstagButton, 0, SWT.LEFT);
		mittwochButton.setLayoutData(fd_mittwochButton);
		mittwochButton.setText("Mittwoch");

		donnerstagButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_donnerstagButton = new FormData();
		fd_donnerstagButton.top = new FormAttachment(mittwochButton, 5, SWT.BOTTOM);
		fd_donnerstagButton.left = new FormAttachment(0, 5);
		donnerstagButton.setLayoutData(fd_donnerstagButton);
		donnerstagButton.setText("Donnerstag");

		freitagButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_freitagButton = new FormData();
		fd_freitagButton.top = new FormAttachment(donnerstagButton, 5, SWT.BOTTOM);
		fd_freitagButton.left = new FormAttachment(donnerstagButton, 0, SWT.LEFT);
		freitagButton.setLayoutData(fd_freitagButton);
		freitagButton.setText("Freitag");

		samstagButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_samstagButton = new FormData();
		fd_samstagButton.top = new FormAttachment(freitagButton, 5, SWT.BOTTOM);
		fd_samstagButton.left = new FormAttachment(0, 5);
		samstagButton.setLayoutData(fd_samstagButton);
		samstagButton.setText("Samstag");

		sonntagButton = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_sonntagButton = new FormData();
		fd_sonntagButton.top = new FormAttachment(samstagButton, 5, SWT.BOTTOM);
		fd_sonntagButton.left = new FormAttachment(0, 5);
		sonntagButton.setLayoutData(fd_sonntagButton);
		sonntagButton.setText("Sonntag");
		patientenzustandGroup.setTabList(new Control[] {montagButton, dienstagButton, mittwochButton, donnerstagButton, freitagButton, samstagButton, sonntagButton});

		abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.top = new FormAttachment(patientenzustandGroup, -23, SWT.BOTTOM);
		fd_abbrechenButton.bottom = new FormAttachment(patientenzustandGroup, 0, SWT.BOTTOM);
		fd_abbrechenButton.left = new FormAttachment(transportdatenGroup, -96, SWT.RIGHT);
		fd_abbrechenButton.right = new FormAttachment(transportdatenGroup, 0, SWT.RIGHT);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setImage(SWTResourceManager.getImage(DialysisView.class, "/image/LAN Warning.ico"));
		abbrechenButton.setText("Abbrechen");

		okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.top = new FormAttachment(abbrechenButton, -23, SWT.BOTTOM);
		fd_okButton.bottom = new FormAttachment(abbrechenButton, 0, SWT.BOTTOM);
		fd_okButton.left = new FormAttachment(abbrechenButton, -101, SWT.LEFT);
		fd_okButton.right = new FormAttachment(abbrechenButton, -5, SWT.LEFT);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");
		shell.setTabList(new Control[] {planungGroup, patientenzustandGroup, transportdatenGroup, okButton, abbrechenButton});
		//
	}

}
