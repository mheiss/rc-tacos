package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI (form) to manage a roster entry
 * @author b.thek
 */
public class RosterEntryView {

	private DateTime dateTimeAbmeldung;
	private DateTime dateTimeAnmeldung;
	private DateTime dateTimeDienstBis;
	private DateTime dateTimeDienstVon;
	private Combo comboDienstBis;
	private Combo comboDienstVon;
	private Text textAnmerkugen;
	private Combo comboDienstverhaeltnis;
	private Combo comboVerwendung;
	private Combo comboOrtsstelle;
	private Button bereitschaftButton;
	private Combo setEmployeenameCombo;
	private Button abbrechenButton;
	private Button okButton;
	private Group group;
	private Group dienstplanGroup;
	private DateTime dateTime;
	protected Shell shell;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RosterEntryView window = new RosterEntryView();
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
		shell.setImage(SWTResourceManager.getImage(RosterEntryView.class, "/image/Tacos_LOGO.jpg"));
		shell.setSize(591, 512);
		shell.setText("Dienstplaneintrag");

		dienstplanGroup = new Group(shell, SWT.NONE);
		dienstplanGroup.setText("Dienstplan");
		dienstplanGroup.setBounds(10, 10, 559, 312);

		final Label datumBeschriftungLabel = new Label(dienstplanGroup, SWT.NONE);
		datumBeschriftungLabel.setBounds(10, 24,165, 13);
		datumBeschriftungLabel.setText("Datum Dienstbeginn auswählen:");

		dateTime = new DateTime(dienstplanGroup, SWT.CALENDAR);
		dateTime.setToolTipText("Zeigt das Datum des Dienstbeginns an");
		dateTime.setBounds(10, 43,180, 171);
		dateTime.setData("newKey", null);

		final Label mitarbeiterLabel = new Label(dienstplanGroup, SWT.NONE);
		mitarbeiterLabel.setBounds(213, 48,55, 13);
		mitarbeiterLabel.setText("Mitarbeiter:");

		setEmployeenameCombo = new Combo(dienstplanGroup, SWT.READ_ONLY);
		setEmployeenameCombo.setItems(new String[] {"Muster Max", "Musterfrau Maximchen", "Schwarzenegger Alexandra"});
		setEmployeenameCombo.setBounds(306, 43,226, 24);
		setEmployeenameCombo.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setEmployeenameCombo.setText("set employeename ");

		final Label ortsstelleLabel = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel.setBounds(213, 102,50, 13);
		ortsstelleLabel.setText("Ortsstelle:");

		comboOrtsstelle = new Combo(dienstplanGroup, SWT.NONE);
		comboOrtsstelle.setBounds(306, 99,226, 21);
		comboOrtsstelle.setItems(new String[] {"Bruck-Kapfenberg", "Bruck an der Mur", "Kapfenberg", "St. Marein", "Thörl", "Turnau", "Breitenau"});

		bereitschaftButton = new Button(dienstplanGroup, SWT.CHECK);
		bereitschaftButton.setBounds(306, 77,85, 16);
		bereitschaftButton.setText("Bereitschaft");

		comboDienstVon = new Combo(dienstplanGroup, SWT.NONE);
		comboDienstVon.select(1);
		comboDienstVon.setBounds(10, 250,62, 21);
		comboDienstVon.setItems(new String[] {"06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30"});

		final Label vonLabel = new Label(dienstplanGroup, SWT.NONE);
		vonLabel.setBounds(10, 231,62, 13);
		vonLabel.setText("Dienst von:");

		final Label vonLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		vonLabel_1.setBounds(128, 231,62, 13);
		vonLabel_1.setText("Dienst bis: ");

		comboDienstBis = new Combo(dienstplanGroup, SWT.NONE);
		comboDienstBis.setBounds(128, 250,62, 21);
		comboDienstBis.setItems(new String[] {"14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30"});
		comboDienstBis.setData("newKey", null);

		final Label ortsstelleLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel_1.setBounds(213, 129, 77, 13);
		ortsstelleLabel_1.setText("Verwendung:");

		comboVerwendung = new Combo(dienstplanGroup, SWT.NONE);
		comboVerwendung.setItems(new String[] {"Fahrer", "Sanitäter", "Zweithelfer", "Leitstellendisponent", "Notfallsanitäter", "Notarzt", "Sonstiges"});
		comboVerwendung.setBounds(306, 126, 226, 21);

		final Label ortsstelleLabel_1_1 = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel_1_1.setBounds(213, 156, 87, 13);
		ortsstelleLabel_1_1.setText("Dienstverhältnis:");

		comboDienstverhaeltnis = new Combo(dienstplanGroup, SWT.NONE);
		comboDienstverhaeltnis.setItems(new String[] {"Hauptamtlich", "Freiwillig", "Zivildienstleistender", "Sonstiges"});
		comboDienstverhaeltnis.setBounds(306, 153, 226, 21);

		final Label anmerkungenLabel = new Label(dienstplanGroup, SWT.NONE);
		anmerkungenLabel.setText("Anmerkungen:");
		anmerkungenLabel.setBounds(213, 201, 87, 13);

		textAnmerkugen = new Text(dienstplanGroup, SWT.BORDER);
		textAnmerkugen.setBounds(306, 198, 226, 100);

		dateTimeDienstBis = new DateTime(dienstplanGroup, SWT.NONE);
		dateTimeDienstBis.setBounds(129, 277, 92, 21);

		dateTimeDienstVon = new DateTime(dienstplanGroup, SWT.NONE);
		dateTimeDienstVon.setBounds(10, 277, 92, 21);
		dienstplanGroup.setTabList(new Control[] {dateTime, setEmployeenameCombo, bereitschaftButton, comboOrtsstelle, comboVerwendung, comboDienstverhaeltnis, textAnmerkugen, comboDienstVon, comboDienstBis, dateTimeDienstVon, dateTimeDienstBis});

		group = new Group(shell, SWT.NONE);
		group.setText("Tatsächliche Dienstzeiten");
		group.setBounds(10, 328, 559, 90);

		dateTimeAnmeldung = new DateTime(group, SWT.TIME);
		dateTimeAnmeldung.setBounds(10, 47,92, 21);

		dateTimeAbmeldung = new DateTime(group, SWT.TIME);
		dateTimeAbmeldung.setBounds(133, 47,92, 21);

		final Label anmeldungLabel = new Label(group, SWT.NONE);
		anmeldungLabel.setText("Anmeldung:");
		anmeldungLabel.setBounds(10, 25, 72, 13);

		final Label anmeldungLabel_1 = new Label(group, SWT.NONE);
		anmeldungLabel_1.setBounds(134, 25, 72, 13);
		anmeldungLabel_1.setText("Abmeldung:");
		group.setTabList(new Control[] {dateTimeAnmeldung, dateTimeAbmeldung});

		abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(SWTResourceManager.getImage(RosterEntryView.class, "/image/LAN Warning.ico"));
		abbrechenButton.setBounds(473, 445, 96, 23);
		abbrechenButton.setText("Abbrechen");

		okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(371, 445, 96, 23);
		okButton.setText("OK");
		shell.setTabList(new Control[] {dienstplanGroup, group, okButton, abbrechenButton});
		//
	}

}
