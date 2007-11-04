package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI (form) to manage the transport details
 * one form for all kinds of transports (not necessary are blanked out)
 * @author b.thek
 *
 */
public class TransportDetailView {

	private Group transportdetailsGroup;
	private Text textSaniII;
	private Text textSnaniI;
	private Text textFahrer;
	private Button bergrettungButton;
	private Button polizeiButton;
	private Button feuerwehrButton;
	private Button brkdtButton;
	private Button dfButton;
	private Button rthButton;
	private Button notarztButton;
	private Button bd2Button;
	private Text textRueckmeldung;
	private Button bd1Button;
	private Text textAnmerkungen;
	private Combo comboPrioritaet;
	private Combo comboErkrankungVerletzung;
	private Button fernfahrtButton;
	private Button mariazellButton;
	private Button wienButton;
	private Button leobenButton;
	private Button grazButton;
	private Button bezirkButton;
	private Text textTermin;
	private Text textBeiPat;
	private Text textAbf;
	private Combo comboZustaendigeOrtsstelle;
	private Text textTelefonAnrufer;
	private Text textAnrufer;
	private Button button_1;
	private Button begleitpersonButton;
	private Button rufhilfepatientButton;
	private Button eigenerRollstuhlButton;
	private Button krankentrageButton;
	private Button tragsesselButton;
	private Combo comboNachOrt;
	private Combo comboNachNr;
	private Combo comboNachStrasse;
	private Combo comboVorname;
	private Combo comboNachname;
	private Combo comboVonOrt;
	private Combo comboVonNr;
	private Combo comboVonStrasse;
	private Text textFahrzeug;
	private Text textOrtsstelle;
	private Text textTransportNummer;
	private Group group;
	private Button buttonVormerkung;
	private Button buttonAlles;
	private Button buttonNotfall;
	private Button okButton;
	private Button abbrechenButton;
	private Group statusmeldungenGroup;
	private Group personalAmFahrzeugGroup;
	private Group planungGroup_1;
	private Group patientenzustandGroup;
	private Group planungGroup;
	private Group transportdatenGroup;
	private DateTime dateTime;
	private Button gehendButton;
	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			TransportDetailView window = new TransportDetailView();
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
		shell.setRegion(null);
		shell.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/Tacos_LOGO.jpg"));
		shell.setSize(1075, 545);
		shell.setText("Transport");

		dateTime = new DateTime(shell, SWT.CALENDAR);
		final FormData fd_dateTime = new FormData();
		fd_dateTime.bottom = new FormAttachment(0, 160);
		fd_dateTime.top = new FormAttachment(0, 10);
		fd_dateTime.right = new FormAttachment(0, 187);
		fd_dateTime.left = new FormAttachment(0, 10);
		dateTime.setLayoutData(fd_dateTime);

		transportdatenGroup = new Group(shell, SWT.NONE);
		transportdatenGroup.setLayout(new FormLayout());
		final FormData fd_transportdatenGroup = new FormData();
		fd_transportdatenGroup.bottom = new FormAttachment(0, 160);
		fd_transportdatenGroup.top = new FormAttachment(0, 10);
		fd_transportdatenGroup.right = new FormAttachment(0, 1056);
		fd_transportdatenGroup.left = new FormAttachment(0, 194);
		transportdatenGroup.setLayoutData(fd_transportdatenGroup);
		transportdatenGroup.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportdatenGroup.setText("Transportdaten");

		final Label vonLabel = new Label(transportdatenGroup, SWT.NONE);
		vonLabel.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData fd_vonLabel = new FormData();
		fd_vonLabel.bottom = new FormAttachment(0, 42);
		fd_vonLabel.top = new FormAttachment(0, 29);
		fd_vonLabel.right = new FormAttachment(0, 32);
		fd_vonLabel.left = new FormAttachment(0, 7);
		vonLabel.setLayoutData(fd_vonLabel);
		vonLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		vonLabel.setText("von:");

		comboNachStrasse = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboNachStrasse = new FormData();
		fd_comboNachStrasse.bottom = new FormAttachment(0, 74);
		fd_comboNachStrasse.top = new FormAttachment(0, 53);
		fd_comboNachStrasse.right = new FormAttachment(0, 232);
		fd_comboNachStrasse.left = new FormAttachment(0, 38);
		comboNachStrasse.setLayoutData(fd_comboNachStrasse);

		comboVonStrasse = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboVonStrasse = new FormData();
		fd_comboVonStrasse.bottom = new FormAttachment(0, 47);
		fd_comboVonStrasse.top = new FormAttachment(0, 26);
		fd_comboVonStrasse.right = new FormAttachment(0, 232);
		fd_comboVonStrasse.left = new FormAttachment(0, 38);
		comboVonStrasse.setLayoutData(fd_comboVonStrasse);

		final Label nachLabel = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_nachLabel = new FormData();
		fd_nachLabel.bottom = new FormAttachment(0, 69);
		fd_nachLabel.top = new FormAttachment(0, 56);
		fd_nachLabel.right = new FormAttachment(0, 32);
		fd_nachLabel.left = new FormAttachment(0, 7);
		nachLabel.setLayoutData(fd_nachLabel);
		nachLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachLabel.setText("nach:");

		final Label label = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(0, 20);
		fd_label.top = new FormAttachment(0, 7);
		fd_label.right = new FormAttachment(0, 94);
		fd_label.left = new FormAttachment(0, 38);
		label.setLayoutData(fd_label);
		label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label.setText("Straße");

		comboVonNr = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboVonNr = new FormData();
		fd_comboVonNr.bottom = new FormAttachment(0, 47);
		fd_comboVonNr.top = new FormAttachment(0, 26);
		fd_comboVonNr.right = new FormAttachment(0, 313);
		fd_comboVonNr.left = new FormAttachment(0, 238);
		comboVonNr.setLayoutData(fd_comboVonNr);

		comboNachNr = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboNachNr = new FormData();
		fd_comboNachNr.bottom = new FormAttachment(0, 74);
		fd_comboNachNr.top = new FormAttachment(0, 53);
		fd_comboNachNr.right = new FormAttachment(0, 313);
		fd_comboNachNr.left = new FormAttachment(0, 238);
		comboNachNr.setLayoutData(fd_comboNachNr);

		final Label label_1 = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_label_1 = new FormData();
		fd_label_1.bottom = new FormAttachment(0, 20);
		fd_label_1.top = new FormAttachment(0, 7);
		fd_label_1.right = new FormAttachment(0, 313);
		fd_label_1.left = new FormAttachment(0, 238);
		label_1.setLayoutData(fd_label_1);
		label_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_1.setText("Nr./Stock/Tür");

		comboVonOrt = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboVonOrt = new FormData();
		fd_comboVonOrt.bottom = new FormAttachment(0, 47);
		fd_comboVonOrt.top = new FormAttachment(0, 26);
		fd_comboVonOrt.right = new FormAttachment(0, 430);
		fd_comboVonOrt.left = new FormAttachment(0, 319);
		comboVonOrt.setLayoutData(fd_comboVonOrt);

		comboNachOrt = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboNachOrt = new FormData();
		fd_comboNachOrt.bottom = new FormAttachment(0, 74);
		fd_comboNachOrt.top = new FormAttachment(0, 53);
		fd_comboNachOrt.right = new FormAttachment(0, 430);
		fd_comboNachOrt.left = new FormAttachment(0, 319);
		comboNachOrt.setLayoutData(fd_comboNachOrt);

		final Label ortLabel = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_ortLabel = new FormData();
		fd_ortLabel.bottom = new FormAttachment(0, 20);
		fd_ortLabel.top = new FormAttachment(0, 7);
		fd_ortLabel.right = new FormAttachment(0, 344);
		fd_ortLabel.left = new FormAttachment(0, 319);
		ortLabel.setLayoutData(fd_ortLabel);
		ortLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ortLabel.setText("Ort");

		comboNachname = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboNachname = new FormData();
		fd_comboNachname.bottom = new FormAttachment(0, 47);
		fd_comboNachname.top = new FormAttachment(0, 26);
		fd_comboNachname.right = new FormAttachment(0, 635);
		fd_comboNachname.left = new FormAttachment(0, 464);
		comboNachname.setLayoutData(fd_comboNachname);

		final Label nachnameLabel = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_nachnameLabel = new FormData();
		fd_nachnameLabel.bottom = new FormAttachment(0, 20);
		fd_nachnameLabel.top = new FormAttachment(0, 7);
		fd_nachnameLabel.right = new FormAttachment(0, 520);
		fd_nachnameLabel.left = new FormAttachment(0, 464);
		nachnameLabel.setLayoutData(fd_nachnameLabel);
		nachnameLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachnameLabel.setText("Nachname");

		comboVorname = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboVorname = new FormData();
		fd_comboVorname.bottom = new FormAttachment(0, 47);
		fd_comboVorname.top = new FormAttachment(0, 26);
		fd_comboVorname.right = new FormAttachment(0, 812);
		fd_comboVorname.left = new FormAttachment(0, 641);
		comboVorname.setLayoutData(fd_comboVorname);

		final Button buttonPatientendatenPruefen = new Button(transportdatenGroup, SWT.NONE);
		final FormData fd_buttonPatientendatenPruefen = new FormData();
		fd_buttonPatientendatenPruefen.bottom = new FormAttachment(0, 47);
		fd_buttonPatientendatenPruefen.top = new FormAttachment(0, 24);
		fd_buttonPatientendatenPruefen.right = new FormAttachment(0, 850);
		fd_buttonPatientendatenPruefen.left = new FormAttachment(0, 818);
		buttonPatientendatenPruefen.setLayoutData(fd_buttonPatientendatenPruefen);
		buttonPatientendatenPruefen.setText("...");

		final Label nachnameLabel_1 = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_nachnameLabel_1 = new FormData();
		fd_nachnameLabel_1.bottom = new FormAttachment(0, 20);
		fd_nachnameLabel_1.top = new FormAttachment(0, 7);
		fd_nachnameLabel_1.right = new FormAttachment(0, 697);
		fd_nachnameLabel_1.left = new FormAttachment(0, 641);
		nachnameLabel_1.setLayoutData(fd_nachnameLabel_1);
		nachnameLabel_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		nachnameLabel_1.setText("Vorname");

		gehendButton = new Button(transportdatenGroup, SWT.RADIO);
		final FormData fd_gehendButton = new FormData();
		fd_gehendButton.bottom = new FormAttachment(0, 70);
		fd_gehendButton.top = new FormAttachment(0, 54);
		fd_gehendButton.right = new FormAttachment(0, 547);
		fd_gehendButton.left = new FormAttachment(0, 464);
		gehendButton.setLayoutData(fd_gehendButton);
		gehendButton.setText("gehend");

		tragsesselButton = new Button(transportdatenGroup, SWT.RADIO);
		final FormData fd_tragsesselButton = new FormData();
		fd_tragsesselButton.bottom = new FormAttachment(0, 70);
		fd_tragsesselButton.top = new FormAttachment(0, 54);
		fd_tragsesselButton.right = new FormAttachment(0, 636);
		fd_tragsesselButton.left = new FormAttachment(0, 553);
		tragsesselButton.setLayoutData(fd_tragsesselButton);
		tragsesselButton.setText("Tragsessel");

		krankentrageButton = new Button(transportdatenGroup, SWT.RADIO);
		final FormData fd_krankentrageButton = new FormData();
		fd_krankentrageButton.bottom = new FormAttachment(0, 70);
		fd_krankentrageButton.top = new FormAttachment(0, 54);
		fd_krankentrageButton.right = new FormAttachment(0, 734);
		fd_krankentrageButton.left = new FormAttachment(0, 651);
		krankentrageButton.setLayoutData(fd_krankentrageButton);
		krankentrageButton.setText("Krankentrage");

		eigenerRollstuhlButton = new Button(transportdatenGroup, SWT.RADIO);
		final FormData fd_eigenerRollstuhlButton = new FormData();
		fd_eigenerRollstuhlButton.bottom = new FormAttachment(0, 70);
		fd_eigenerRollstuhlButton.top = new FormAttachment(0, 54);
		fd_eigenerRollstuhlButton.right = new FormAttachment(0, 850);
		fd_eigenerRollstuhlButton.left = new FormAttachment(0, 750);
		eigenerRollstuhlButton.setLayoutData(fd_eigenerRollstuhlButton);
		eigenerRollstuhlButton.setText("Eigener Rollstuhl");

		begleitpersonButton = new Button(transportdatenGroup, SWT.CHECK);
		final FormData fd_begleitpersonButton = new FormData();
		fd_begleitpersonButton.bottom = new FormAttachment(0, 116);
		fd_begleitpersonButton.top = new FormAttachment(0, 100);
		fd_begleitpersonButton.right = new FormAttachment(0, 583);
		fd_begleitpersonButton.left = new FormAttachment(0, 462);
		begleitpersonButton.setLayoutData(fd_begleitpersonButton);
		begleitpersonButton.setToolTipText("Begleitperson");
		begleitpersonButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		begleitpersonButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_BeglPerson.gif"));

		button_1 = new Button(transportdatenGroup, SWT.CHECK);
		final FormData fd_button_1 = new FormData();
		fd_button_1.bottom = new FormAttachment(0, 96);
		fd_button_1.top = new FormAttachment(0, 80);
		fd_button_1.right = new FormAttachment(0, 159);
		fd_button_1.left = new FormAttachment(0, 38);
		button_1.setLayoutData(fd_button_1);
		button_1.setText("Rücktransport möglich");

		rufhilfepatientButton = new Button(transportdatenGroup, SWT.CHECK);
		final FormData fd_rufhilfepatientButton = new FormData();
		fd_rufhilfepatientButton.bottom = new FormAttachment(0, 96);
		fd_rufhilfepatientButton.top = new FormAttachment(0, 80);
		fd_rufhilfepatientButton.right = new FormAttachment(0, 547);
		fd_rufhilfepatientButton.left = new FormAttachment(0, 462);
		rufhilfepatientButton.setLayoutData(fd_rufhilfepatientButton);
		rufhilfepatientButton.setText("Rufhilfepatient");

		final Label anruferLabel = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_anruferLabel = new FormData();
		fd_anruferLabel.bottom = new FormAttachment(0, 95);
		fd_anruferLabel.top = new FormAttachment(0, 82);
		fd_anruferLabel.right = new FormAttachment(0, 657);
		fd_anruferLabel.left = new FormAttachment(0, 610);
		anruferLabel.setLayoutData(fd_anruferLabel);
		anruferLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		anruferLabel.setText("Anrufer:");

		textAnrufer = new Text(transportdatenGroup, SWT.BORDER);
		final FormData fd_textAnrufer = new FormData();
		fd_textAnrufer.bottom = new FormAttachment(0, 97);
		fd_textAnrufer.top = new FormAttachment(0, 76);
		fd_textAnrufer.right = new FormAttachment(0, 850);
		fd_textAnrufer.left = new FormAttachment(0, 663);
		textAnrufer.setLayoutData(fd_textAnrufer);

		final Label telefonLabel = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_telefonLabel = new FormData();
		fd_telefonLabel.bottom = new FormAttachment(0, 115);
		fd_telefonLabel.top = new FormAttachment(0, 102);
		fd_telefonLabel.right = new FormAttachment(0, 657);
		fd_telefonLabel.left = new FormAttachment(0, 610);
		telefonLabel.setLayoutData(fd_telefonLabel);
		telefonLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		telefonLabel.setText("Telefon:");

		textTelefonAnrufer = new Text(transportdatenGroup, SWT.BORDER);
		final FormData fd_textTelefonAnrufer = new FormData();
		fd_textTelefonAnrufer.bottom = new FormAttachment(0, 120);
		fd_textTelefonAnrufer.top = new FormAttachment(0, 99);
		fd_textTelefonAnrufer.right = new FormAttachment(0, 850);
		fd_textTelefonAnrufer.left = new FormAttachment(0, 663);
		textTelefonAnrufer.setLayoutData(fd_textTelefonAnrufer);

		final Label label_6 = new Label(transportdatenGroup, SWT.NONE);
		final FormData fd_label_6 = new FormData();
		fd_label_6.bottom = new FormAttachment(0, 118);
		fd_label_6.top = new FormAttachment(0, 105);
		fd_label_6.right = new FormAttachment(0, 313);
		fd_label_6.left = new FormAttachment(0, 202);
		label_6.setLayoutData(fd_label_6);
		label_6.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_6.setText("Zuständige Ortsstelle:");

		comboZustaendigeOrtsstelle = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboZustaendigeOrtsstelle = new FormData();
		fd_comboZustaendigeOrtsstelle.bottom = new FormAttachment(0, 121);
		fd_comboZustaendigeOrtsstelle.top = new FormAttachment(0, 100);
		fd_comboZustaendigeOrtsstelle.right = new FormAttachment(0, 431);
		fd_comboZustaendigeOrtsstelle.left = new FormAttachment(0, 319);
		comboZustaendigeOrtsstelle.setLayoutData(fd_comboZustaendigeOrtsstelle);
		comboZustaendigeOrtsstelle.setItems(new String[] {"Breitenau", "Bruck an der Mur", "Kapfenberg", "St. Marein", "Thörl", "Turnau"});
		transportdatenGroup.setTabList(new Control[] {comboVonStrasse, comboVonNr, comboVonOrt, comboNachname, comboVorname, comboNachStrasse, comboNachNr, comboNachOrt, gehendButton, tragsesselButton, krankentrageButton, eigenerRollstuhlButton, button_1, comboZustaendigeOrtsstelle, rufhilfepatientButton, begleitpersonButton, textAnrufer, textTelefonAnrufer});

		planungGroup = new Group(shell, SWT.NONE);
		planungGroup.setLayout(new FormLayout());
		final FormData fd_planungGroup = new FormData();
		fd_planungGroup.bottom = new FormAttachment(0, 348);
		fd_planungGroup.top = new FormAttachment(0, 166);
		fd_planungGroup.right = new FormAttachment(0, 187);
		fd_planungGroup.left = new FormAttachment(0, 10);
		planungGroup.setLayoutData(fd_planungGroup);
		planungGroup.setText("Zeiten/Richtung");

		final Label abfLabel = new Label(planungGroup, SWT.NONE);
		final FormData fd_abfLabel = new FormData();
		fd_abfLabel.bottom = new FormAttachment(0, 37);
		fd_abfLabel.top = new FormAttachment(0, 24);
		fd_abfLabel.right = new FormAttachment(0, 32);
		fd_abfLabel.left = new FormAttachment(0, 7);
		abfLabel.setLayoutData(fd_abfLabel);
		abfLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		abfLabel.setText("Abf:");

		final Label beiPatLabel = new Label(planungGroup, SWT.NONE);
		final FormData fd_beiPatLabel = new FormData();
		fd_beiPatLabel.bottom = new FormAttachment(0, 64);
		fd_beiPatLabel.top = new FormAttachment(0, 51);
		fd_beiPatLabel.right = new FormAttachment(0, 32);
		fd_beiPatLabel.left = new FormAttachment(0, 7);
		beiPatLabel.setLayoutData(fd_beiPatLabel);
		beiPatLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		beiPatLabel.setText("Pat.:");

		final Label terminLabel = new Label(planungGroup, SWT.NONE);
		final FormData fd_terminLabel = new FormData();
		fd_terminLabel.bottom = new FormAttachment(0, 91);
		fd_terminLabel.top = new FormAttachment(0, 78);
		fd_terminLabel.right = new FormAttachment(0, 35);
		fd_terminLabel.left = new FormAttachment(0, 7);
		terminLabel.setLayoutData(fd_terminLabel);
		terminLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		terminLabel.setText("Term.");

		textBeiPat = new Text(planungGroup, SWT.BORDER);
		final FormData fd_textBeiPat = new FormData();
		fd_textBeiPat.bottom = new FormAttachment(0, 69);
		fd_textBeiPat.top = new FormAttachment(0, 48);
		fd_textBeiPat.right = new FormAttachment(0, 79);
		fd_textBeiPat.left = new FormAttachment(0, 38);
		textBeiPat.setLayoutData(fd_textBeiPat);
		textBeiPat.setToolTipText("Geplante Ankunftszeit beim Patienten");

		textTermin = new Text(planungGroup, SWT.BORDER);
		final FormData fd_textTermin = new FormData();
		fd_textTermin.bottom = new FormAttachment(0, 96);
		fd_textTermin.top = new FormAttachment(0, 75);
		fd_textTermin.right = new FormAttachment(0, 79);
		fd_textTermin.left = new FormAttachment(0, 38);
		textTermin.setLayoutData(fd_textTermin);
		textTermin.setToolTipText("Termin am Zielort");

		textAbf = new Text(planungGroup, SWT.BORDER);
		final FormData fd_textAbf = new FormData();
		fd_textAbf.bottom = new FormAttachment(0, 42);
		fd_textAbf.top = new FormAttachment(0, 21);
		fd_textAbf.right = new FormAttachment(0, 78);
		fd_textAbf.left = new FormAttachment(0, 37);
		textAbf.setLayoutData(fd_textAbf);
		textAbf.setToolTipText("Abfahrt des Fahrzeuges von der Ortsstelle");

		mariazellButton = new Button(planungGroup, SWT.RADIO);
		final FormData fd_mariazellButton = new FormData();
		fd_mariazellButton.bottom = new FormAttachment(0, 143);
		fd_mariazellButton.top = new FormAttachment(0, 127);
		fd_mariazellButton.right = new FormAttachment(0, 174);
		fd_mariazellButton.left = new FormAttachment(0, 101);
		mariazellButton.setLayoutData(fd_mariazellButton);
		mariazellButton.setText("Mariazell");

		wienButton = new Button(planungGroup, SWT.RADIO);
		final FormData fd_wienButton = new FormData();
		fd_wienButton.bottom = new FormAttachment(0, 118);
		fd_wienButton.top = new FormAttachment(0, 102);
		fd_wienButton.right = new FormAttachment(0, 174);
		fd_wienButton.left = new FormAttachment(0, 101);
		wienButton.setLayoutData(fd_wienButton);
		wienButton.setText("Wien");

		leobenButton = new Button(planungGroup, SWT.RADIO);
		final FormData fd_leobenButton = new FormData();
		fd_leobenButton.bottom = new FormAttachment(0, 93);
		fd_leobenButton.top = new FormAttachment(0, 77);
		fd_leobenButton.right = new FormAttachment(0, 174);
		fd_leobenButton.left = new FormAttachment(0, 101);
		leobenButton.setLayoutData(fd_leobenButton);
		leobenButton.setText("Leoben");

		grazButton = new Button(planungGroup, SWT.RADIO);
		final FormData fd_grazButton = new FormData();
		fd_grazButton.bottom = new FormAttachment(0, 68);
		fd_grazButton.top = new FormAttachment(0, 52);
		fd_grazButton.right = new FormAttachment(0, 174);
		fd_grazButton.left = new FormAttachment(0, 101);
		grazButton.setLayoutData(fd_grazButton);
		grazButton.setText("Graz");

		bezirkButton = new Button(planungGroup, SWT.RADIO);
		final FormData fd_bezirkButton = new FormData();
		fd_bezirkButton.bottom = new FormAttachment(0, 35);
		fd_bezirkButton.top = new FormAttachment(0, 19);
		fd_bezirkButton.right = new FormAttachment(0, 172);
		fd_bezirkButton.left = new FormAttachment(0, 101);
		bezirkButton.setLayoutData(fd_bezirkButton);
		bezirkButton.setText("Bezirk");

		final Label label_2 = new Label(planungGroup, SWT.SEPARATOR);
		final FormData fd_label_2 = new FormData();
		fd_label_2.bottom = new FormAttachment(0, 159);
		fd_label_2.top = new FormAttachment(0, 11);
		fd_label_2.right = new FormAttachment(0, 94);
		fd_label_2.left = new FormAttachment(0, 81);
		label_2.setLayoutData(fd_label_2);

		fernfahrtButton = new Button(planungGroup, SWT.CHECK);
		final FormData fd_fernfahrtButton = new FormData();
		fd_fernfahrtButton.bottom = new FormAttachment(0, 143);
		fd_fernfahrtButton.top = new FormAttachment(0, 127);
		fd_fernfahrtButton.right = new FormAttachment(0, 80);
		fd_fernfahrtButton.left = new FormAttachment(0, 7);
		fernfahrtButton.setLayoutData(fd_fernfahrtButton);
		fernfahrtButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_Fernfahrt.gif"));
		fernfahrtButton.setToolTipText("Fernfahrten sind lt. RKT deklariert");
		planungGroup.setTabList(new Control[] {textAbf, textBeiPat, textTermin, fernfahrtButton, bezirkButton, grazButton, leobenButton, wienButton, mariazellButton});

		patientenzustandGroup = new Group(shell, SWT.NONE);
		patientenzustandGroup.setLayout(new FormLayout());
		final FormData fd_patientenzustandGroup = new FormData();
		fd_patientenzustandGroup.bottom = new FormAttachment(0, 348);
		fd_patientenzustandGroup.top = new FormAttachment(0, 166);
		fd_patientenzustandGroup.right = new FormAttachment(0, 873);
		fd_patientenzustandGroup.left = new FormAttachment(0, 194);
		patientenzustandGroup.setLayoutData(fd_patientenzustandGroup);
		patientenzustandGroup.setText("Patientenzustand");

		comboErkrankungVerletzung = new Combo(patientenzustandGroup, SWT.NONE);
		final FormData fd_comboErkrankungVerletzung = new FormData();
		fd_comboErkrankungVerletzung.bottom = new FormAttachment(0, 50);
		fd_comboErkrankungVerletzung.top = new FormAttachment(0, 29);
		fd_comboErkrankungVerletzung.right = new FormAttachment(0, 289);
		fd_comboErkrankungVerletzung.left = new FormAttachment(0, 7);
		comboErkrankungVerletzung.setLayoutData(fd_comboErkrankungVerletzung);

		textAnmerkungen = new Text(patientenzustandGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		final FormData fd_textAnmerkungen = new FormData();
		fd_textAnmerkungen.bottom = new FormAttachment(0, 159);
		fd_textAnmerkungen.top = new FormAttachment(0, 75);
		fd_textAnmerkungen.right = new FormAttachment(0, 289);
		fd_textAnmerkungen.left = new FormAttachment(0, 7);
		textAnmerkungen.setLayoutData(fd_textAnmerkungen);

		final Label erkrankungverletzungLabel = new Label(patientenzustandGroup, SWT.NONE);
		final FormData fd_erkrankungverletzungLabel = new FormData();
		fd_erkrankungverletzungLabel.bottom = new FormAttachment(0, 24);
		fd_erkrankungverletzungLabel.top = new FormAttachment(0, 11);
		fd_erkrankungverletzungLabel.right = new FormAttachment(0, 134);
		fd_erkrankungverletzungLabel.left = new FormAttachment(0, 7);
		erkrankungverletzungLabel.setLayoutData(fd_erkrankungverletzungLabel);
		erkrankungverletzungLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		erkrankungverletzungLabel.setText("Erkrankung/Verletzung");

		final Label anmerkungenLabel = new Label(patientenzustandGroup, SWT.NONE);
		final FormData fd_anmerkungenLabel = new FormData();
		fd_anmerkungenLabel.bottom = new FormAttachment(0, 69);
		fd_anmerkungenLabel.top = new FormAttachment(0, 56);
		fd_anmerkungenLabel.right = new FormAttachment(0, 134);
		fd_anmerkungenLabel.left = new FormAttachment(0, 7);
		anmerkungenLabel.setLayoutData(fd_anmerkungenLabel);
		anmerkungenLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		anmerkungenLabel.setText("Anmerkungen");

		textRueckmeldung = new Text(patientenzustandGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
		final FormData fd_textRueckmeldung = new FormData();
		fd_textRueckmeldung.bottom = new FormAttachment(0, 159);
		fd_textRueckmeldung.top = new FormAttachment(0, 29);
		fd_textRueckmeldung.right = new FormAttachment(0, 666);
		fd_textRueckmeldung.left = new FormAttachment(0, 355);
		textRueckmeldung.setLayoutData(fd_textRueckmeldung);

		final Label label_3 = new Label(patientenzustandGroup, SWT.NONE);
		final FormData fd_label_3 = new FormData();
		fd_label_3.bottom = new FormAttachment(0, 24);
		fd_label_3.top = new FormAttachment(0, 11);
		fd_label_3.right = new FormAttachment(0, 518);
		fd_label_3.left = new FormAttachment(0, 355);
		label_3.setLayoutData(fd_label_3);
		label_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_3.setText("Rückmeldung");

		bd1Button = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_bd1Button = new FormData();
		fd_bd1Button.bottom = new FormAttachment(0, 23);
		fd_bd1Button.top = new FormAttachment(0, 7);
		fd_bd1Button.right = new FormAttachment(0, 286);
		fd_bd1Button.left = new FormAttachment(0, 243);
		bd1Button.setLayoutData(fd_bd1Button);
		bd1Button.setToolTipText("Sondersignal zum Einsatzort");
		bd1Button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		bd1Button.setText("BD 1");

		bd2Button = new Button(patientenzustandGroup, SWT.CHECK);
		final FormData fd_bd2Button = new FormData();
		fd_bd2Button.bottom = new FormAttachment(0, 25);
		fd_bd2Button.top = new FormAttachment(0, 9);
		fd_bd2Button.right = new FormAttachment(0, 666);
		fd_bd2Button.left = new FormAttachment(0, 623);
		bd2Button.setLayoutData(fd_bd2Button);
		bd2Button.setToolTipText("Sondersignal auf dem Weg zum Transportziel");
		bd2Button.setText("BD 2");

		comboPrioritaet = new Combo(patientenzustandGroup, SWT.NONE);
		comboPrioritaet.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), E (NEF extern)");
		comboPrioritaet.setItems(new String[] {"A", "B", "C", "D", "E", "F", "G"});
		comboPrioritaet.setData("newKey", null);
		final FormData fd_comboPrioritaet = new FormData();
		fd_comboPrioritaet.bottom = new FormAttachment(0, 73);
		fd_comboPrioritaet.top = new FormAttachment(0, 52);
		fd_comboPrioritaet.right = new FormAttachment(0, 287);
		fd_comboPrioritaet.left = new FormAttachment(0, 225);
		comboPrioritaet.setLayoutData(fd_comboPrioritaet);

		final Label label_4 = new Label(patientenzustandGroup, SWT.NONE);
		label_4.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		final FormData fd_label_4 = new FormData();
		fd_label_4.left = new FormAttachment(0, 160);
		fd_label_4.bottom = new FormAttachment(0, 69);
		fd_label_4.top = new FormAttachment(0, 56);
		fd_label_4.right = new FormAttachment(0, 220);
		label_4.setLayoutData(fd_label_4);
		label_4.setForeground(SWTResourceManager.getColor(128, 128, 128));
		label_4.setText("Priorität:");
		patientenzustandGroup.setTabList(new Control[] {comboErkrankungVerletzung, comboPrioritaet, textAnmerkungen, bd1Button, textRueckmeldung, bd2Button});

		planungGroup_1 = new Group(shell, SWT.NONE);
		planungGroup_1.setLayout(new FormLayout());
		final FormData fd_planungGroup_1 = new FormData();
		fd_planungGroup_1.bottom = new FormAttachment(0, 348);
		fd_planungGroup_1.top = new FormAttachment(0, 166);
		fd_planungGroup_1.right = new FormAttachment(0, 1056);
		fd_planungGroup_1.left = new FormAttachment(0, 879);
		planungGroup_1.setLayoutData(fd_planungGroup_1);
		planungGroup_1.setText("Alarmierung");

		notarztButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_notarztButton = new FormData();
		fd_notarztButton.bottom = new FormAttachment(0, 27);
		fd_notarztButton.top = new FormAttachment(0, 11);
		fd_notarztButton.right = new FormAttachment(0, 92);
		fd_notarztButton.left = new FormAttachment(0, 7);
		notarztButton.setLayoutData(fd_notarztButton);
		notarztButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_NEF.gif"));
		notarztButton.setToolTipText("Externer Notarzt für diesen Transport alarmiert");

		rthButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_rthButton = new FormData();
		fd_rthButton.bottom = new FormAttachment(0, 49);
		fd_rthButton.top = new FormAttachment(0, 33);
		fd_rthButton.right = new FormAttachment(0, 92);
		fd_rthButton.left = new FormAttachment(0, 7);
		rthButton.setLayoutData(fd_rthButton);
		rthButton.setToolTipText("Hubschrauber");
		rthButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_Hubi.gif"));

		dfButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_dfButton = new FormData();
		fd_dfButton.bottom = new FormAttachment(0, 71);
		fd_dfButton.top = new FormAttachment(0, 55);
		fd_dfButton.right = new FormAttachment(0, 80);
		fd_dfButton.left = new FormAttachment(0, 7);
		dfButton.setLayoutData(fd_dfButton);
		dfButton.setToolTipText("DF/Inspektionsdienst");
		dfButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_Rotlicht.gif"));

		brkdtButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_brkdtButton = new FormData();
		fd_brkdtButton.bottom = new FormAttachment(0, 93);
		fd_brkdtButton.top = new FormAttachment(0, 77);
		fd_brkdtButton.right = new FormAttachment(0, 92);
		fd_brkdtButton.left = new FormAttachment(0, 7);
		brkdtButton.setLayoutData(fd_brkdtButton);
		brkdtButton.setToolTipText("Bezirksrettungskommandant");
		brkdtButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_Rotlicht.gif"));

		feuerwehrButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_feuerwehrButton = new FormData();
		fd_feuerwehrButton.bottom = new FormAttachment(0, 115);
		fd_feuerwehrButton.top = new FormAttachment(0, 99);
		fd_feuerwehrButton.right = new FormAttachment(0, 80);
		fd_feuerwehrButton.left = new FormAttachment(0, 7);
		feuerwehrButton.setLayoutData(fd_feuerwehrButton);
		feuerwehrButton.setToolTipText("Feuerwehr");
		feuerwehrButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_FF.gif"));
		feuerwehrButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});

		polizeiButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_polizeiButton = new FormData();
		fd_polizeiButton.bottom = new FormAttachment(0, 137);
		fd_polizeiButton.top = new FormAttachment(0, 121);
		fd_polizeiButton.right = new FormAttachment(0, 92);
		fd_polizeiButton.left = new FormAttachment(0, 7);
		polizeiButton.setLayoutData(fd_polizeiButton);
		polizeiButton.setToolTipText("Polizei");
		polizeiButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_Polizei.gif"));

		bergrettungButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_bergrettungButton = new FormData();
		fd_bergrettungButton.bottom = new FormAttachment(0, 159);
		fd_bergrettungButton.top = new FormAttachment(0, 143);
		fd_bergrettungButton.right = new FormAttachment(0, 92);
		fd_bergrettungButton.left = new FormAttachment(0, 7);
		bergrettungButton.setLayoutData(fd_bergrettungButton);
		bergrettungButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/O_Bergrettung.gif"));
		bergrettungButton.setToolTipText("Bergrettung");
		bergrettungButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});

		final DateTime dateTimePolizei = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimePolizei = new FormData();
		fd_dateTimePolizei.bottom = new FormAttachment(0, 137);
		fd_dateTimePolizei.top = new FormAttachment(0, 116);
		fd_dateTimePolizei.right = new FormAttachment(0, 167);
		fd_dateTimePolizei.left = new FormAttachment(0, 94);
		dateTimePolizei.setLayoutData(fd_dateTimePolizei);
		dateTimePolizei.setToolTipText("Zeit zu der die Polizei Box zum letzten Mal angehakt wurde");

		final DateTime dateTimeFW = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimeFW = new FormData();
		fd_dateTimeFW.bottom = new FormAttachment(0, 115);
		fd_dateTimeFW.top = new FormAttachment(0, 94);
		fd_dateTimeFW.right = new FormAttachment(0, 167);
		fd_dateTimeFW.left = new FormAttachment(0, 94);
		dateTimeFW.setLayoutData(fd_dateTimeFW);
		dateTimeFW.setToolTipText("Zeit zu der die Feuerwehr Box zum letzten Mal angehakt wurde");

		final DateTime dateTimeBRKDT = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimeBRKDT = new FormData();
		fd_dateTimeBRKDT.bottom = new FormAttachment(0, 93);
		fd_dateTimeBRKDT.top = new FormAttachment(0, 72);
		fd_dateTimeBRKDT.right = new FormAttachment(0, 167);
		fd_dateTimeBRKDT.left = new FormAttachment(0, 94);
		dateTimeBRKDT.setLayoutData(fd_dateTimeBRKDT);
		dateTimeBRKDT.setToolTipText("Zeit zu der die BRKDT Box zum letzten Mal angehakt wurde");

		final DateTime dateTimeDF = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimeDF = new FormData();
		fd_dateTimeDF.bottom = new FormAttachment(0, 71);
		fd_dateTimeDF.top = new FormAttachment(0, 50);
		fd_dateTimeDF.right = new FormAttachment(0, 167);
		fd_dateTimeDF.left = new FormAttachment(0, 94);
		dateTimeDF.setLayoutData(fd_dateTimeDF);
		dateTimeDF.setToolTipText("Zeit zu der das DF/Insp. Feld zum letzten Mal angehakt wurde");

		final DateTime dateTimeNotarzt = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimeNotarzt = new FormData();
		fd_dateTimeNotarzt.bottom = new FormAttachment(0, 27);
		fd_dateTimeNotarzt.top = new FormAttachment(0, 6);
		fd_dateTimeNotarzt.right = new FormAttachment(0, 167);
		fd_dateTimeNotarzt.left = new FormAttachment(0, 94);
		dateTimeNotarzt.setLayoutData(fd_dateTimeNotarzt);
		dateTimeNotarzt.setToolTipText("Zeit zu der die Notarzt Box zum letzten Mal angehakt wurde");

		final DateTime dateTimeRTH = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimeRTH = new FormData();
		fd_dateTimeRTH.bottom = new FormAttachment(0, 49);
		fd_dateTimeRTH.top = new FormAttachment(0, 28);
		fd_dateTimeRTH.right = new FormAttachment(0, 167);
		fd_dateTimeRTH.left = new FormAttachment(0, 94);
		dateTimeRTH.setLayoutData(fd_dateTimeRTH);
		dateTimeRTH.setToolTipText("Zeit zu der die RTH Box zum letzten Mal angehakt wurde");

		final DateTime dateTimeBergrettung = new DateTime(planungGroup_1, SWT.TIME);
		final FormData fd_dateTimeBergrettung = new FormData();
		fd_dateTimeBergrettung.bottom = new FormAttachment(0, 159);
		fd_dateTimeBergrettung.top = new FormAttachment(0, 138);
		fd_dateTimeBergrettung.right = new FormAttachment(0, 167);
		fd_dateTimeBergrettung.left = new FormAttachment(0, 94);
		dateTimeBergrettung.setLayoutData(fd_dateTimeBergrettung);
		dateTimeBergrettung.setToolTipText("Zeit zu der die Bergrettung Box zum letzten Mal angehakt wurde");
		planungGroup_1.setTabList(new Control[] {notarztButton, rthButton, dfButton, brkdtButton, feuerwehrButton, polizeiButton, bergrettungButton});

		final Label label_5 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		final FormData fd_label_5 = new FormData();
		fd_label_5.bottom = new FormAttachment(0, 367);
		fd_label_5.top = new FormAttachment(0, 354);
		fd_label_5.right = new FormAttachment(0, 1056);
		fd_label_5.left = new FormAttachment(0, 10);
		label_5.setLayoutData(fd_label_5);

		transportdetailsGroup = new Group(shell, SWT.NONE);
		transportdetailsGroup.setLayout(new FormLayout());
		final FormData fd_transportdetailsGroup = new FormData();
		fd_transportdetailsGroup.bottom = new FormAttachment(0, 501);
		fd_transportdetailsGroup.top = new FormAttachment(0, 373);
		fd_transportdetailsGroup.right = new FormAttachment(0, 187);
		fd_transportdetailsGroup.left = new FormAttachment(0, 10);
		transportdetailsGroup.setLayoutData(fd_transportdetailsGroup);
		transportdetailsGroup.setText("Transportdetails");

		final Label transportnumemmerLabel = new Label(transportdetailsGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel = new FormData();
		fd_transportnumemmerLabel.bottom = new FormAttachment(0, 27);
		fd_transportnumemmerLabel.top = new FormAttachment(0, 14);
		fd_transportnumemmerLabel.right = new FormAttachment(0, 54);
		fd_transportnumemmerLabel.left = new FormAttachment(0, 7);
		transportnumemmerLabel.setLayoutData(fd_transportnumemmerLabel);
		transportnumemmerLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel.setText("Trsp.Nr.:");

		textTransportNummer = new Text(transportdetailsGroup, SWT.BORDER);
		final FormData fd_textTransportNummer = new FormData();
		fd_textTransportNummer.bottom = new FormAttachment(0, 32);
		fd_textTransportNummer.top = new FormAttachment(0, 11);
		fd_textTransportNummer.right = new FormAttachment(0, 158);
		fd_textTransportNummer.left = new FormAttachment(0, 60);
		textTransportNummer.setLayoutData(fd_textTransportNummer);

		final Label transportnumemmerLabel_1 = new Label(transportdetailsGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_1 = new FormData();
		fd_transportnumemmerLabel_1.bottom = new FormAttachment(0, 54);
		fd_transportnumemmerLabel_1.top = new FormAttachment(0, 41);
		fd_transportnumemmerLabel_1.right = new FormAttachment(0, 57);
		fd_transportnumemmerLabel_1.left = new FormAttachment(0, 7);
		transportnumemmerLabel_1.setLayoutData(fd_transportnumemmerLabel_1);
		transportnumemmerLabel_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_1.setText("Ortsstelle:");

		textOrtsstelle = new Text(transportdetailsGroup, SWT.BORDER);
		final FormData fd_textOrtsstelle = new FormData();
		fd_textOrtsstelle.bottom = new FormAttachment(0, 59);
		fd_textOrtsstelle.top = new FormAttachment(0, 38);
		fd_textOrtsstelle.right = new FormAttachment(0, 158);
		fd_textOrtsstelle.left = new FormAttachment(0, 60);
		textOrtsstelle.setLayoutData(fd_textOrtsstelle);

		textFahrzeug = new Text(transportdetailsGroup, SWT.BORDER);
		final FormData fd_textFahrzeug = new FormData();
		fd_textFahrzeug.bottom = new FormAttachment(0, 86);
		fd_textFahrzeug.top = new FormAttachment(0, 65);
		fd_textFahrzeug.right = new FormAttachment(0, 158);
		fd_textFahrzeug.left = new FormAttachment(0, 60);
		textFahrzeug.setLayoutData(fd_textFahrzeug);

		final Label transportnumemmerLabel_1_1 = new Label(transportdetailsGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_1_1 = new FormData();
		fd_transportnumemmerLabel_1_1.bottom = new FormAttachment(0, 81);
		fd_transportnumemmerLabel_1_1.top = new FormAttachment(0, 68);
		fd_transportnumemmerLabel_1_1.right = new FormAttachment(0, 57);
		fd_transportnumemmerLabel_1_1.left = new FormAttachment(0, 7);
		transportnumemmerLabel_1_1.setLayoutData(fd_transportnumemmerLabel_1_1);
		transportnumemmerLabel_1_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_1_1.setText("Fahrzeug:");
		transportdetailsGroup.setTabList(new Control[] {textTransportNummer, textOrtsstelle, textFahrzeug});

		personalAmFahrzeugGroup = new Group(shell, SWT.NONE);
		personalAmFahrzeugGroup.setLayout(new FormLayout());
		final FormData fd_personalAmFahrzeugGroup = new FormData();
		fd_personalAmFahrzeugGroup.bottom = new FormAttachment(0, 501);
		fd_personalAmFahrzeugGroup.top = new FormAttachment(0, 373);
		fd_personalAmFahrzeugGroup.right = new FormAttachment(0, 483);
		fd_personalAmFahrzeugGroup.left = new FormAttachment(0, 194);
		personalAmFahrzeugGroup.setLayoutData(fd_personalAmFahrzeugGroup);
		personalAmFahrzeugGroup.setText("Personal am Fahrzeug");

		textFahrer = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		final FormData fd_textFahrer = new FormData();
		fd_textFahrer.bottom = new FormAttachment(0, 32);
		fd_textFahrer.top = new FormAttachment(0, 11);
		fd_textFahrer.right = new FormAttachment(0, 276);
		fd_textFahrer.left = new FormAttachment(0, 73);
		textFahrer.setLayoutData(fd_textFahrer);

		textSnaniI = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		final FormData fd_textSnaniI = new FormData();
		fd_textSnaniI.bottom = new FormAttachment(0, 59);
		fd_textSnaniI.top = new FormAttachment(0, 38);
		fd_textSnaniI.right = new FormAttachment(0, 276);
		fd_textSnaniI.left = new FormAttachment(0, 73);
		textSnaniI.setLayoutData(fd_textSnaniI);

		textSaniII = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		final FormData fd_textSaniII = new FormData();
		fd_textSaniII.bottom = new FormAttachment(0, 86);
		fd_textSaniII.top = new FormAttachment(0, 65);
		fd_textSaniII.right = new FormAttachment(0, 276);
		fd_textSaniII.left = new FormAttachment(0, 73);
		textSaniII.setLayoutData(fd_textSaniII);

		final Label transportnumemmerLabel_2 = new Label(personalAmFahrzeugGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_2 = new FormData();
		fd_transportnumemmerLabel_2.bottom = new FormAttachment(0, 27);
		fd_transportnumemmerLabel_2.top = new FormAttachment(0, 14);
		fd_transportnumemmerLabel_2.right = new FormAttachment(0, 54);
		fd_transportnumemmerLabel_2.left = new FormAttachment(0, 7);
		transportnumemmerLabel_2.setLayoutData(fd_transportnumemmerLabel_2);
		transportnumemmerLabel_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_2.setText("Fahrer:");

		final Label transportnumemmerLabel_3 = new Label(personalAmFahrzeugGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_3 = new FormData();
		fd_transportnumemmerLabel_3.bottom = new FormAttachment(0, 54);
		fd_transportnumemmerLabel_3.top = new FormAttachment(0, 41);
		fd_transportnumemmerLabel_3.right = new FormAttachment(0, 68);
		fd_transportnumemmerLabel_3.left = new FormAttachment(0, 7);
		transportnumemmerLabel_3.setLayoutData(fd_transportnumemmerLabel_3);
		transportnumemmerLabel_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_3.setText("Sanitäter I:");

		final Label transportnumemmerLabel_4 = new Label(personalAmFahrzeugGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_4 = new FormData();
		fd_transportnumemmerLabel_4.bottom = new FormAttachment(0, 81);
		fd_transportnumemmerLabel_4.top = new FormAttachment(0, 68);
		fd_transportnumemmerLabel_4.right = new FormAttachment(0, 68);
		fd_transportnumemmerLabel_4.left = new FormAttachment(0, 7);
		transportnumemmerLabel_4.setLayoutData(fd_transportnumemmerLabel_4);
		transportnumemmerLabel_4.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_4.setText("Sanitäter II:");
		personalAmFahrzeugGroup.setTabList(new Control[] {textFahrer, textSnaniI, textSaniII});

		statusmeldungenGroup = new Group(shell, SWT.NONE);
		statusmeldungenGroup.setLayout(new FormLayout());
		final FormData fd_statusmeldungenGroup = new FormData();
		fd_statusmeldungenGroup.bottom = new FormAttachment(0, 501);
		fd_statusmeldungenGroup.top = new FormAttachment(0, 373);
		fd_statusmeldungenGroup.right = new FormAttachment(0, 843);
		fd_statusmeldungenGroup.left = new FormAttachment(0, 489);
		statusmeldungenGroup.setLayoutData(fd_statusmeldungenGroup);
		statusmeldungenGroup.setText("Statusmeldungen");

		final Text textAufgen = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textAufgen = new FormData();
		fd_textAufgen.bottom = new FormAttachment(0, 32);
		fd_textAufgen.top = new FormAttachment(0, 11);
		fd_textAufgen.right = new FormAttachment(0, 85);
		fd_textAufgen.left = new FormAttachment(0, 44);
		textAufgen.setLayoutData(fd_textAufgen);

		final Label transportnumemmerLabel_5 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5 = new FormData();
		fd_transportnumemmerLabel_5.bottom = new FormAttachment(0, 26);
		fd_transportnumemmerLabel_5.top = new FormAttachment(0, 13);
		fd_transportnumemmerLabel_5.right = new FormAttachment(0, 43);
		fd_transportnumemmerLabel_5.left = new FormAttachment(0, 12);
		transportnumemmerLabel_5.setLayoutData(fd_transportnumemmerLabel_5);
		transportnumemmerLabel_5.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5.setText("Aufg.:");

		final Text textAE = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textAE = new FormData();
		fd_textAE.bottom = new FormAttachment(0, 59);
		fd_textAE.top = new FormAttachment(0, 38);
		fd_textAE.right = new FormAttachment(0, 85);
		fd_textAE.left = new FormAttachment(0, 44);
		textAE.setLayoutData(fd_textAE);

		final Label transportnumemmerLabel_5_1 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_1 = new FormData();
		fd_transportnumemmerLabel_5_1.bottom = new FormAttachment(0, 58);
		fd_transportnumemmerLabel_5_1.top = new FormAttachment(0, 45);
		fd_transportnumemmerLabel_5_1.right = new FormAttachment(0, 43);
		fd_transportnumemmerLabel_5_1.left = new FormAttachment(0, 12);
		transportnumemmerLabel_5_1.setLayoutData(fd_transportnumemmerLabel_5_1);
		transportnumemmerLabel_5_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_1.setText("AE:");

		final Text textS1 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS1 = new FormData();
		fd_textS1.bottom = new FormAttachment(0, 32);
		fd_textS1.top = new FormAttachment(0, 11);
		fd_textS1.right = new FormAttachment(0, 173);
		fd_textS1.left = new FormAttachment(0, 132);
		textS1.setLayoutData(fd_textS1);

		final Label transportnumemmerLabel_5_2 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2 = new FormData();
		fd_transportnumemmerLabel_5_2.bottom = new FormAttachment(0, 26);
		fd_transportnumemmerLabel_5_2.top = new FormAttachment(0, 13);
		fd_transportnumemmerLabel_5_2.right = new FormAttachment(0, 141);
		fd_transportnumemmerLabel_5_2.left = new FormAttachment(0, 110);
		transportnumemmerLabel_5_2.setLayoutData(fd_transportnumemmerLabel_5_2);
		transportnumemmerLabel_5_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2.setText("S1:");

		final Text textS2 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS2 = new FormData();
		fd_textS2.bottom = new FormAttachment(0, 59);
		fd_textS2.top = new FormAttachment(0, 38);
		fd_textS2.right = new FormAttachment(0, 173);
		fd_textS2.left = new FormAttachment(0, 132);
		textS2.setLayoutData(fd_textS2);

		final Text textS3 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS3 = new FormData();
		fd_textS3.bottom = new FormAttachment(0, 86);
		fd_textS3.top = new FormAttachment(0, 65);
		fd_textS3.right = new FormAttachment(0, 173);
		fd_textS3.left = new FormAttachment(0, 132);
		textS3.setLayoutData(fd_textS3);

		final Label transportnumemmerLabel_5_2_1 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_1 = new FormData();
		fd_transportnumemmerLabel_5_2_1.bottom = new FormAttachment(0, 59);
		fd_transportnumemmerLabel_5_2_1.top = new FormAttachment(0, 46);
		fd_transportnumemmerLabel_5_2_1.right = new FormAttachment(0, 141);
		fd_transportnumemmerLabel_5_2_1.left = new FormAttachment(0, 110);
		transportnumemmerLabel_5_2_1.setLayoutData(fd_transportnumemmerLabel_5_2_1);
		transportnumemmerLabel_5_2_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_1.setText("S2:");

		final Label transportnumemmerLabel_5_2_2 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_2 = new FormData();
		fd_transportnumemmerLabel_5_2_2.bottom = new FormAttachment(0, 86);
		fd_transportnumemmerLabel_5_2_2.top = new FormAttachment(0, 73);
		fd_transportnumemmerLabel_5_2_2.right = new FormAttachment(0, 141);
		fd_transportnumemmerLabel_5_2_2.left = new FormAttachment(0, 110);
		transportnumemmerLabel_5_2_2.setLayoutData(fd_transportnumemmerLabel_5_2_2);
		transportnumemmerLabel_5_2_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_2.setText("S3:");

		final Label transportnumemmerLabel_5_2_3 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_3 = new FormData();
		fd_transportnumemmerLabel_5_2_3.bottom = new FormAttachment(0, 27);
		fd_transportnumemmerLabel_5_2_3.top = new FormAttachment(0, 14);
		fd_transportnumemmerLabel_5_2_3.right = new FormAttachment(0, 213);
		fd_transportnumemmerLabel_5_2_3.left = new FormAttachment(0, 195);
		transportnumemmerLabel_5_2_3.setLayoutData(fd_transportnumemmerLabel_5_2_3);
		transportnumemmerLabel_5_2_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3.setText("S4:");

		final Text textS4 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS4 = new FormData();
		fd_textS4.bottom = new FormAttachment(0, 32);
		fd_textS4.top = new FormAttachment(0, 11);
		fd_textS4.right = new FormAttachment(0, 255);
		fd_textS4.left = new FormAttachment(0, 214);
		textS4.setLayoutData(fd_textS4);

		final Text textS5 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS5 = new FormData();
		fd_textS5.bottom = new FormAttachment(0, 59);
		fd_textS5.top = new FormAttachment(0, 38);
		fd_textS5.right = new FormAttachment(0, 255);
		fd_textS5.left = new FormAttachment(0, 214);
		textS5.setLayoutData(fd_textS5);

		final Text textS6 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS6 = new FormData();
		fd_textS6.bottom = new FormAttachment(0, 86);
		fd_textS6.top = new FormAttachment(0, 65);
		fd_textS6.right = new FormAttachment(0, 255);
		fd_textS6.left = new FormAttachment(0, 214);
		textS6.setLayoutData(fd_textS6);

		final Label transportnumemmerLabel_5_2_3_1 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_3_1 = new FormData();
		fd_transportnumemmerLabel_5_2_3_1.bottom = new FormAttachment(0, 54);
		fd_transportnumemmerLabel_5_2_3_1.top = new FormAttachment(0, 41);
		fd_transportnumemmerLabel_5_2_3_1.right = new FormAttachment(0, 213);
		fd_transportnumemmerLabel_5_2_3_1.left = new FormAttachment(0, 195);
		transportnumemmerLabel_5_2_3_1.setLayoutData(fd_transportnumemmerLabel_5_2_3_1);
		transportnumemmerLabel_5_2_3_1.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_1.setText("S5:");

		final Label transportnumemmerLabel_5_2_3_2 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_3_2 = new FormData();
		fd_transportnumemmerLabel_5_2_3_2.bottom = new FormAttachment(0, 81);
		fd_transportnumemmerLabel_5_2_3_2.top = new FormAttachment(0, 68);
		fd_transportnumemmerLabel_5_2_3_2.right = new FormAttachment(0, 213);
		fd_transportnumemmerLabel_5_2_3_2.left = new FormAttachment(0, 195);
		transportnumemmerLabel_5_2_3_2.setLayoutData(fd_transportnumemmerLabel_5_2_3_2);
		transportnumemmerLabel_5_2_3_2.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_2.setText("S6:");

		final Text textS7 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS7 = new FormData();
		fd_textS7.bottom = new FormAttachment(0, 32);
		fd_textS7.top = new FormAttachment(0, 11);
		fd_textS7.right = new FormAttachment(0, 339);
		fd_textS7.left = new FormAttachment(0, 298);
		textS7.setLayoutData(fd_textS7);

		final Text textS8 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS8 = new FormData();
		fd_textS8.bottom = new FormAttachment(0, 59);
		fd_textS8.top = new FormAttachment(0, 38);
		fd_textS8.right = new FormAttachment(0, 339);
		fd_textS8.left = new FormAttachment(0, 298);
		textS8.setLayoutData(fd_textS8);

		final Text textS9 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS9 = new FormData();
		fd_textS9.bottom = new FormAttachment(0, 86);
		fd_textS9.top = new FormAttachment(0, 65);
		fd_textS9.right = new FormAttachment(0, 339);
		fd_textS9.left = new FormAttachment(0, 298);
		textS9.setLayoutData(fd_textS9);

		final Label transportnumemmerLabel_5_2_3_3 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_3_3 = new FormData();
		fd_transportnumemmerLabel_5_2_3_3.bottom = new FormAttachment(0, 27);
		fd_transportnumemmerLabel_5_2_3_3.top = new FormAttachment(0, 14);
		fd_transportnumemmerLabel_5_2_3_3.right = new FormAttachment(0, 295);
		fd_transportnumemmerLabel_5_2_3_3.left = new FormAttachment(0, 277);
		transportnumemmerLabel_5_2_3_3.setLayoutData(fd_transportnumemmerLabel_5_2_3_3);
		transportnumemmerLabel_5_2_3_3.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_3.setText("S7:");

		final Label transportnumemmerLabel_5_2_3_4 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_3_4 = new FormData();
		fd_transportnumemmerLabel_5_2_3_4.bottom = new FormAttachment(0, 54);
		fd_transportnumemmerLabel_5_2_3_4.top = new FormAttachment(0, 41);
		fd_transportnumemmerLabel_5_2_3_4.right = new FormAttachment(0, 295);
		fd_transportnumemmerLabel_5_2_3_4.left = new FormAttachment(0, 277);
		transportnumemmerLabel_5_2_3_4.setLayoutData(fd_transportnumemmerLabel_5_2_3_4);
		transportnumemmerLabel_5_2_3_4.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_4.setText("S8:");

		final Label transportnumemmerLabel_5_2_3_5 = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_transportnumemmerLabel_5_2_3_5 = new FormData();
		fd_transportnumemmerLabel_5_2_3_5.bottom = new FormAttachment(0, 81);
		fd_transportnumemmerLabel_5_2_3_5.top = new FormAttachment(0, 68);
		fd_transportnumemmerLabel_5_2_3_5.right = new FormAttachment(0, 295);
		fd_transportnumemmerLabel_5_2_3_5.left = new FormAttachment(0, 277);
		transportnumemmerLabel_5_2_3_5.setLayoutData(fd_transportnumemmerLabel_5_2_3_5);
		transportnumemmerLabel_5_2_3_5.setForeground(SWTResourceManager.getColor(128, 128, 128));
		transportnumemmerLabel_5_2_3_5.setText("S9:");

		abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.bottom = new FormAttachment(0, 501);
		fd_abbrechenButton.top = new FormAttachment(0, 478);
		fd_abbrechenButton.right = new FormAttachment(0, 1056);
		fd_abbrechenButton.left = new FormAttachment(0, 960);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setImage(SWTResourceManager.getImage(TransportDetailView.class, "/image/LAN Warning.ico"));
		abbrechenButton.setText("Abbrechen");

		okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(0, 501);
		fd_okButton.top = new FormAttachment(0, 478);
		fd_okButton.right = new FormAttachment(0, 954);
		fd_okButton.left = new FormAttachment(0, 858);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");

		group = new Group(shell, SWT.NONE);
		group.setLayout(new FormLayout());
		final FormData fd_group = new FormData();
		fd_group.bottom = new FormAttachment(0, 472);
		fd_group.top = new FormAttachment(0, 373);
		fd_group.right = new FormAttachment(0, 1056);
		fd_group.left = new FormAttachment(0, 846);
		group.setLayoutData(fd_group);
		group.setText("Formularansicht");

		buttonNotfall = new Button(group, SWT.TOGGLE);
		final FormData fd_buttonNotfall = new FormData();
		fd_buttonNotfall.bottom = new FormAttachment(0, 20);
		fd_buttonNotfall.top = new FormAttachment(0, -3);
		fd_buttonNotfall.right = new FormAttachment(0, 201);
		fd_buttonNotfall.left = new FormAttachment(0, 94);
		buttonNotfall.setLayoutData(fd_buttonNotfall);
		buttonNotfall.setToolTipText("Blendet alle für einen Notfall nicht relevanten Felder aus");
		buttonNotfall.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});
		buttonNotfall.setText("Nofall");

		buttonVormerkung = new Button(group, SWT.TOGGLE);
		final FormData fd_buttonVormerkung = new FormData();
		fd_buttonVormerkung.bottom = new FormAttachment(0, 49);
		fd_buttonVormerkung.top = new FormAttachment(0, 26);
		fd_buttonVormerkung.right = new FormAttachment(0, 202);
		fd_buttonVormerkung.left = new FormAttachment(0, 94);
		buttonVormerkung.setLayoutData(fd_buttonVormerkung);
		buttonVormerkung.setToolTipText("Blendet alle für eine Vormerkung nicht relevanten Felder aus");
		buttonVormerkung.setText("Vormerkung");

		buttonAlles = new Button(group, SWT.TOGGLE);
		final FormData fd_buttonAlles = new FormData();
		fd_buttonAlles.bottom = new FormAttachment(0, 78);
		fd_buttonAlles.top = new FormAttachment(0, 55);
		fd_buttonAlles.right = new FormAttachment(0, 202);
		fd_buttonAlles.left = new FormAttachment(0, 94);
		buttonAlles.setLayoutData(fd_buttonAlles);
		buttonAlles.setToolTipText("Blendet alle Felder ein");
		buttonAlles.setText("Alle Felder anzeigen");

		final Label label_7 = new Label(group, SWT.NONE);
		final FormData fd_label_7 = new FormData();
		fd_label_7.bottom = new FormAttachment(0, 76);
		fd_label_7.top = new FormAttachment(0, 26);
		fd_label_7.right = new FormAttachment(0, 88);
		fd_label_7.left = new FormAttachment(0, 5);
		label_7.setLayoutData(fd_label_7);
		shell.setTabList(new Control[] {dateTime, transportdatenGroup, planungGroup, patientenzustandGroup, planungGroup_1, transportdetailsGroup, personalAmFahrzeugGroup, statusmeldungenGroup, okButton, abbrechenButton, group});
		//
	}

}
