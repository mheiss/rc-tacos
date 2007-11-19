package at.rc.tacos.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.client.controller.CreateItemAction;
import at.rc.tacos.client.controller.CreateRosterEntryAction;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI (form) to manage a roster entry
 * @author b.thek
 */
public class RosterEntryView {

	private DateTime dateTimeAbmeldung;
	private DateTime dateTimeAnmeldung;
	private DateTime dateDienstBis;//Date
	private DateTime dateDienstVon;//Date
	private DateTime timeDienstBis;//Time
	private DateTime timeDienstVon;//Time
	private Text textAnmerkungen;
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
	protected void createContents() 
	{
		//TODO: get a list of StaffMembers form the database
		
		StaffMember sm1 = new StaffMember(0,"Helmut", "Maier", "h.maie");
		StaffMember sm2 = new StaffMember(1,"Daniel", "Haberl", "d.habe");
		
		ArrayList<StaffMember> staffMemberList = new ArrayList<StaffMember>(Arrays.asList(sm1,sm2));

		
		
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
		final ComboViewer comboViewer = new ComboViewer(setEmployeenameCombo);
		for(StaffMember staffMember: staffMemberList)
		{
			comboViewer.add(staffMember);
		}
		
		//setEmployeenameCombo.setItems(new String[] {"Muster Max", "Musterfrau Maximchen", "Schwarzenegger Alexandra"});
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

		timeDienstVon = new DateTime (dienstplanGroup, SWT.TIME | SWT.SHORT);
		//comboDienstVon = new Combo(dienstplanGroup, SWT.NONE);
		//comboDienstVon.select(1);
		//comboDienstVon.setBounds(10, 250,62, 21);
		timeDienstVon.setBounds(10,250,62,21);
		//comboDienstVon.setItems(new String[] {"06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30"});

		final Label vonLabel = new Label(dienstplanGroup, SWT.NONE);
		vonLabel.setBounds(10, 231,62, 13);
		vonLabel.setText("Dienst von:");

		final Label vonLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		vonLabel_1.setBounds(128, 231,62, 13);
		vonLabel_1.setText("Dienst bis: ");

		//tDienstBis = new Combo(dienstplanGroup, SWT.NONE);
//		comboDienstBis.setBounds(128, 250,62, 21);
//		comboDienstBis.setItems(new String[] {"14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30"});
//		comboDienstBis.setData("newKey", null);
		timeDienstBis = new DateTime (dienstplanGroup, SWT.TIME | SWT.SHORT);
		timeDienstBis.setBounds(128,250,62,21);

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

		textAnmerkungen = new Text(dienstplanGroup, SWT.BORDER);
		textAnmerkungen.setBounds(306, 198, 226, 100);

		dateDienstBis = new DateTime(dienstplanGroup, SWT.NONE);
		dateDienstBis.setBounds(129, 277, 92, 21);

		dateDienstVon = new DateTime(dienstplanGroup, SWT.NONE);
		dateDienstVon.setBounds(10, 277, 92, 21);
		dienstplanGroup.setTabList(new Control[] {dateTime, setEmployeenameCombo, bereitschaftButton, comboOrtsstelle, comboVerwendung, comboDienstverhaeltnis, textAnmerkungen, timeDienstVon, timeDienstBis, dateDienstVon, dateDienstBis});

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
		
		// Adding the controller
        okButton.addListener(SWT.Selection, new Listener()
        {
            public void handleEvent(Event event) 
            {
 
//            	private Text textAnmerkungen;
//            	private Combo comboDienstverhaeltnis;
//            	private Combo comboVerwendung;
//            	private Combo comboOrtsstelle;
//            	private Button bereitschaftButton;
//            	private Combo setEmployeenameCombo;

//            	private DateTime dateTime;

            	this.createRosterEntryObject();
            	//RosterEntry newRosterEntry = this.createRosterEntryObject();
               // new CreateItemAction(RosterEntryView.this.textAnmerkungen.getText()).run();
              // new CreateRosterEntryAction(newRosterEntry).run();
            }

			private void createRosterEntryObject() 
			{
				//final int dateTimeAbmeldung = 
				//final RosterEntry rosterEntry = null;
				System.out.println("in der createRosterEntryObject");
				Calendar cal = Calendar.getInstance();
				System.out.println("gleich nach erstem calendar");
				cal.set(dateTimeAnmeldung.getYear(), dateTimeAnmeldung.getMonth(), dateTimeAnmeldung.getDay());
				long timestampCheckIn = cal.getTimeInMillis();
				
				cal.set(dateTimeAbmeldung.getYear(), dateTimeAbmeldung.getMonth(), dateTimeAbmeldung.getDay());
				long timestampCheckOut = cal.getTimeInMillis();
				
				cal.set(dateDienstBis.getYear(), dateDienstBis.getMonth(), dateDienstBis.getDay());
				long timestampPlanedDateEndOfWork = cal.getTimeInMillis();
				
				cal.set(dateDienstVon.getYear(), dateDienstVon.getMonth(), dateDienstVon.getDay());
				long timestampPlanedDateStartOfWork = cal.getTimeInMillis();
				
				cal.set(timeDienstBis.getYear(), timeDienstBis.getMonth(), timeDienstBis.getDay());
				long timestampPlanedTimeEndOfWork = cal.getTimeInMillis();
				
				cal.set(timeDienstVon.getYear(), timeDienstVon.getMonth(), timeDienstVon.getDay());
				long timestampPlanedTimeStartOfWork = cal.getTimeInMillis();
				
				System.out.println("nach letztem long");
				
				//StaffMember staffMember = setEmployeenameCombo.
				int index = comboViewer.getCombo().getSelectionIndex();
				StaffMember staffMember = (StaffMember)comboViewer.getElementAt(index);
			
				System.out.println("selected staffMember: " +staffMember.getLastname());
				//RosterEntry rosterEntry = new RosterEntry(0,sm,timestampPlanedDateStartOfWork, timestampPlanedTimeStartOfWork,timestampPlanedTimeEndOfWork,timestampCheckIn, timestampCheckOut, station, competence, servicetype, rosterNotes, standbyState);
				
				
				
				
				//return rosterEntry;
				
			}
        });
		shell.setTabList(new Control[] {dienstplanGroup, group, okButton, abbrechenButton});
		//
	}

}
