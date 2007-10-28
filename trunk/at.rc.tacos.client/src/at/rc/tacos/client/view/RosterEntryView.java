package at.rc.tacos.client.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;
import at.rc.tacos.swtdesigner.*;

/**
 * GUI to manage the roster entry details
 * @author b.thek
 */

public class RosterEntryView {

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
		shell.setImage(SWTResourceManager.getImage(RosterEntryView.class, "image/Tacos_LoginSize.bmp"));
		shell.setSize(591, 512);
		shell.setText("Dienstplaneintrag");

		final Group dienstplanGroup = new Group(shell, SWT.NONE);
		dienstplanGroup.setText("Dienstplan");
		dienstplanGroup.setBounds(10, 10, 559, 312);

		final Label datumLabel = new Label(dienstplanGroup, SWT.NONE);
		datumLabel.setBounds(10, 24,165, 13);
		datumLabel.setText("Datum Dienstbeginn auswählen:");

		dateTime = new DateTime(dienstplanGroup, SWT.CALENDAR);
		dateTime.setBounds(10, 43,180, 171);
		dateTime.setData("newKey", null);

		final Label mitarbeiterLabel = new Label(dienstplanGroup, SWT.NONE);
		mitarbeiterLabel.setBounds(213, 48,55, 13);
		mitarbeiterLabel.setText("Mitarbeiter:");

		final Combo setEmployeenameCombo = new Combo(dienstplanGroup, SWT.READ_ONLY);
		setEmployeenameCombo.setItems(new String[] {"Muster Max", "Musterfrau Maximchen", "Schwarzenegger Alexandra"});
		setEmployeenameCombo.setBounds(306, 43,226, 24);
		setEmployeenameCombo.setFont(SWTResourceManager.getFont("", 10, SWT.BOLD));
		setEmployeenameCombo.setText("set employeename ");

		final Label ortsstelleLabel = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel.setBounds(213, 102,50, 13);
		ortsstelleLabel.setText("Ortsstelle:");

		final Combo combo = new Combo(dienstplanGroup, SWT.NONE);
		combo.setBounds(306, 99,226, 21);
		combo.setItems(new String[] {"Bruck-Kapfenberg", "Bruck an der Mur", "Kapfenberg", "St. Marein", "Thörl", "Turnau", "Breitenau"});

		final Button bereitschaftButton = new Button(dienstplanGroup, SWT.CHECK);
		bereitschaftButton.setBounds(306, 77,85, 16);
		bereitschaftButton.setText("Bereitschaft");

		final Combo combo_1 = new Combo(dienstplanGroup, SWT.NONE);
		combo_1.select(1);
		combo_1.setBounds(10, 250,62, 21);
		combo_1.setItems(new String[] {"06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30"});

		final Label vonLabel = new Label(dienstplanGroup, SWT.NONE);
		vonLabel.setBounds(10, 231,62, 13);
		vonLabel.setText("Dienst von:");

		final Label vonLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		vonLabel_1.setBounds(128, 231,62, 13);
		vonLabel_1.setText("Dienst bis: ");

		final Combo combo_1_1 = new Combo(dienstplanGroup, SWT.NONE);
		combo_1_1.setBounds(128, 250,62, 21);
		combo_1_1.setItems(new String[] {"14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30"});
		combo_1_1.setData("newKey", null);

		final Label ortsstelleLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel_1.setBounds(213, 129, 77, 13);
		ortsstelleLabel_1.setText("Verwendung:");

		final Combo combo_2 = new Combo(dienstplanGroup, SWT.NONE);
		combo_2.setItems(new String[] {"Fahrer", "Sanitäter", "Zweithelfer", "Leitstellendisponent", "Notfallsanitäter", "Notarzt", "Sonstiges"});
		combo_2.setBounds(306, 126, 226, 21);

		final Label ortsstelleLabel_1_1 = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel_1_1.setBounds(213, 156, 87, 13);
		ortsstelleLabel_1_1.setText("Dienstverhältnis:");

		final Combo combo_2_1 = new Combo(dienstplanGroup, SWT.NONE);
		combo_2_1.setItems(new String[] {"Hauptamtlich", "Freiwillig", "Zivildienstleistender", "Sonstiges"});
		combo_2_1.setBounds(306, 153, 226, 21);

		final Label anmerkungenLabel = new Label(dienstplanGroup, SWT.NONE);
		anmerkungenLabel.setText("Anmerkungen:");
		anmerkungenLabel.setBounds(213, 201, 87, 13);

		final Text text = new Text(dienstplanGroup, SWT.BORDER);
		text.setBounds(306, 198, 226, 100);

		final DateTime dateTime_3 = new DateTime(dienstplanGroup, SWT.NONE);
		dateTime_3.setBounds(129, 277, 92, 21);

		final DateTime dateTime_3_1 = new DateTime(dienstplanGroup, SWT.NONE);
		dateTime_3_1.setBounds(10, 277, 92, 21);

		final Group group = new Group(shell, SWT.NONE);
		group.setText("Tatsächliche Dienstzeiten");
		group.setBounds(10, 328, 559, 90);

		final DateTime dateTime_1 = new DateTime(group, SWT.TIME);
		dateTime_1.setBounds(10, 47,92, 21);

		final DateTime dateTime_2 = new DateTime(group, SWT.TIME);
		dateTime_2.setBounds(133, 47,92, 21);

		final Label anmeldungLabel = new Label(group, SWT.NONE);
		anmeldungLabel.setText("Anmeldung:");
		anmeldungLabel.setBounds(10, 25, 72, 13);

		final Label anmeldungLabel_1 = new Label(group, SWT.NONE);
		anmeldungLabel_1.setBounds(134, 25, 72, 13);
		anmeldungLabel_1.setText("Abmeldung:");

		final Button abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(SWTResourceManager.getImage(RosterEntryView.class, "image/LAN Warning.ico"));
		abbrechenButton.setBounds(473, 445, 96, 23);
		abbrechenButton.setText("Abbrechen");

		final Button okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(371, 445, 96, 23);
		okButton.setText("OK");
		//
	}

}
