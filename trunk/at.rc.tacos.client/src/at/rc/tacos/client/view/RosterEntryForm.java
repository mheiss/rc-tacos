package at.rc.tacos.client.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
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
public class RosterEntryForm {

	private Combo dateAbmeldung;
	private Combo dateAnmeldung;
	private Text timeAnmeldung;
	private Text timeAbmeldung;
	private Combo dateDienstBis;
	private DateTime dateDienstVon;
	private Combo timeDienstBis;
	private Combo timeDienstVon;
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

	private String timeCheckedIn;
	private String timeCheckedOut;
	private String timePlannedStartOfWork;
	private String timePlannedEndOfWork;

	private Label anmeldungLabel;
	private Label abmeldungLabel;

	private Color inactiveBackgroundColor = SWTResourceManager.getColor(245, 245, 245);

	private String defaultDate;
	private Vector contentofdateentry;

	private Listener exitListener;

	String[] timeArray = new String[]{"05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30"};

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			RosterEntryForm window = new RosterEntryForm();
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
		//get data
		StaffMember sm1 = new StaffMember(0,"Helmut", "Maier", "h.maie");
		StaffMember sm2 = new StaffMember(1,"Daniel", "Haberl", "d.habe");

		ArrayList<StaffMember> staffMemberList = new ArrayList<StaffMember>(Arrays.asList(sm1,sm2));

		//content of date combo boxes
		List<String> listOfDates = this.fillDateComboBox();
		String[] arrayOfDates = (String[]) listOfDates.toArray(new String[]{});
		defaultDate = this.getDefaultDate();

		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) {
				MessageBox dialog = new MessageBox(shell, SWT.OK | SWT.CANCEL | SWT.ICON_QUESTION);
				dialog.setText("Abbrechen");
				dialog.setMessage("Wollen Sie wirklich abbrechen?");
				if (e.type == SWT.Close) e.doit = false;
				if (dialog.open() != SWT.OK) return;
				shell.dispose();
			}
		};

		//GUI
		shell = new Shell();
		shell.setImage(SWTResourceManager.getImage(RosterEntryForm.class, "/image/Tacos_LOGO.jpg"));
		shell.setSize(591, 512);
		shell.setText("Dienstplaneintrag");

		dienstplanGroup = new Group(shell, SWT.NONE);
		dienstplanGroup.setText("Dienstplan");
		dienstplanGroup.setBounds(10, 10, 559, 312);

		final Label datumBeschriftungLabel = new Label(dienstplanGroup, SWT.NONE);
		datumBeschriftungLabel.setBounds(10, 24,165, 13);
		datumBeschriftungLabel.setText("Datum Dienstbeginn auswählen:");

		//Calendar field
		dateTime = new DateTime(dienstplanGroup, SWT.CALENDAR);
		dateTime.setToolTipText("Zeigt das Datum des Dienstbeginns an");
		dateTime.setBounds(10, 43,180, 171);
		dateTime.setData("newKey", null);
		dateTime.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println ("calendar date changed - at the calendar");
			}
		});

		final Label mitarbeiterLabel = new Label(dienstplanGroup, SWT.NONE);
		mitarbeiterLabel.setBounds(213, 48,55, 13);
		mitarbeiterLabel.setText("Mitarbeiter:");

		setEmployeenameCombo = new Combo(dienstplanGroup, SWT.READ_ONLY);
		final ComboViewer comboViewer = new ComboViewer(setEmployeenameCombo);

		//fill combo employee name with data
		for(StaffMember staffMember: staffMemberList)
		{
			comboViewer.add(staffMember);
		}

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

		timeDienstVon = new Combo (dienstplanGroup, SWT.TIME | SWT.NONE);
		timeDienstVon.setBounds(10,250,62,21);
		timeDienstVon.setItems(timeArray);






		final Label vonLabel = new Label(dienstplanGroup, SWT.NONE);
		vonLabel.setBounds(10, 231,62, 13);
		vonLabel.setText("Dienst von:");

		final Label vonLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		vonLabel_1.setBounds(128, 231,62, 13);
		vonLabel_1.setText("Dienst bis: ");

		timeDienstBis = new Combo(dienstplanGroup, SWT.NONE);
		timeDienstBis.setBounds(128, 250,62, 21);
		timeDienstBis.setItems(timeArray);
		timeDienstBis.setData("newKey", null);


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

		dateDienstBis = new Combo(dienstplanGroup, SWT.NONE);
		dateDienstBis.setBounds(128, 277, 92, 21);
		dateDienstBis.setItems(arrayOfDates);
		dateDienstBis.setText(defaultDate);

		dateDienstVon = new DateTime(dienstplanGroup, SWT.NONE);
		dateDienstVon.setBounds(10, 277, 92, 21);
		dateDienstVon.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println ("calendar date changed - at the normal field");
			}
		});
		dienstplanGroup.setTabList(new Control[] {dateTime, setEmployeenameCombo, bereitschaftButton, comboOrtsstelle, comboVerwendung, comboDienstverhaeltnis, textAnmerkungen, timeDienstVon, timeDienstBis, dateDienstVon, dateDienstBis});

		group = new Group(shell, SWT.NONE);
		group.setText("Tatsächliche Dienstzeiten");
		group.setBounds(10, 328, 559, 110);



		//TODO
		timeAnmeldung = new Text(group, SWT.NONE);
		timeAnmeldung.setBounds(10, 47,62, 21);

		timeAbmeldung = new Text(group, SWT.NONE);
		timeAbmeldung.setBounds(133, 47,62, 21);

		dateAnmeldung = new Combo(group, SWT.NONE);
		dateAnmeldung.setBounds(10, 74,92, 21);		
		dateAnmeldung.setItems(arrayOfDates);

		dateAbmeldung = new Combo(group, SWT.NONE);
		dateAbmeldung.setBounds(133, 74,92, 21);		
		dateAbmeldung.setItems(arrayOfDates);



		anmeldungLabel = new Label(group, SWT.NONE);
		anmeldungLabel.setText("Anmeldung:");
		anmeldungLabel.setBounds(10, 25, 72, 13);

		abmeldungLabel = new Label(group, SWT.NONE);
		abmeldungLabel.setBounds(134, 25, 72, 13);
		abmeldungLabel.setText("Abmeldung:");
		group.setTabList(new Control[] {timeAnmeldung, timeAbmeldung});

		abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(SWTResourceManager.getImage(RosterEntryForm.class, "/image/LAN Warning.ico"));
		abbrechenButton.setBounds(473, 445, 96, 23);
		abbrechenButton.setText("Abbrechen");
		abbrechenButton.addListener(SWT.Selection, exitListener);

		okButton = new Button(shell, SWT.NONE);
		okButton.setBounds(371, 445, 96, 23);
		okButton.setText("OK");

		//this.setRealWorktimesInactive();


		// Adding the controller
		okButton.addListener(SWT.Selection, new Listener()
		{
			public void handleEvent(Event event) 
			{
				RosterEntry newRosterEntry = this.createRosterEntryObject();
				new CreateRosterEntryAction(newRosterEntry).run();
			}

			private RosterEntry createRosterEntryObject() 
			{
				Calendar cal = Calendar.getInstance();


				//planned start of work
				String timePlannedStartOfWork = timeDienstVon.getText();
				String[] plannedStartTime = timePlannedStartOfWork.split(":");
				System.out.println("planned start time: " +plannedStartTime[0]);
				int hoursPlannedStart = Integer.valueOf(plannedStartTime[0]).intValue();
				int minutesPlannedStart = Integer.valueOf(plannedStartTime[1]).intValue();

				int yearPlannedStart = dateDienstVon.getYear();
				int monthPlannedStart = dateDienstVon.getMonth();
				int dayPlannedStart = dateDienstVon.getDay();

				cal.set(yearPlannedStart, monthPlannedStart, dayPlannedStart, hoursPlannedStart, minutesPlannedStart, 0);
				long plannedStartOfWork = cal.getTimeInMillis();


				//planned end of work
				String timePlannedEndOfWork = timeDienstBis.getText();
				String[] plannedEndTime = timePlannedEndOfWork.split(":");
				int hoursPlannedEnd = Integer.valueOf(plannedEndTime[0]).intValue();
				int minutesPlannedEnd = Integer.valueOf(plannedEndTime[1]).intValue();

				String datePlannedEndOfWork = dateDienstBis.getText();
				String[] plannedEndDate = datePlannedEndOfWork.split("\\.");
				int yearPlannedEnd = Integer.valueOf(plannedEndDate[2]).intValue();
				int monthPlannedEnd = Integer.valueOf(plannedEndDate[1]).intValue();
				int dayPlannedEnd = Integer.valueOf(plannedEndDate[0]).intValue();

				cal.set(yearPlannedEnd, monthPlannedEnd, dayPlannedEnd, hoursPlannedEnd, minutesPlannedEnd, 0);
				long plannedEndOfWork = cal.getTimeInMillis();


				//real start of work
				String timeRealStartOfWork = timeAnmeldung.getText();
				String[] realStartTime = timeRealStartOfWork.split(":");
				int hoursRealStart = Integer.valueOf(realStartTime[0]).intValue();
				int minutesRealStart = Integer.valueOf(realStartTime[1]).intValue();

				String dateRealStartOfWork = dateAnmeldung.getText();
				String[] realStartDate = dateRealStartOfWork.split("\\.");
				int yearRealStart = Integer.valueOf(realStartDate[2]).intValue();
				int monthRealStart = Integer.valueOf(realStartDate[1]).intValue();
				int dayRealStart = Integer.valueOf(realStartDate[0]).intValue();

				cal.set(yearRealStart, monthRealStart, dayRealStart, hoursRealStart, minutesRealStart, 0);
				long realStartOfWork = cal.getTimeInMillis();

				//real end of work
				String timeRealEndOfWork = timeAbmeldung.getText();
				String[] realEndTime = timeRealEndOfWork.split(":");
				int hoursRealEnd = Integer.valueOf(realEndTime[0]).intValue();
				int minutesRealEnd = Integer.valueOf(realEndTime[1]).intValue();

				String dateRealEndOfWork = dateAbmeldung.getText();
				String[] realEndDate = dateRealEndOfWork.split("\\.");
				int yearRealEnd = Integer.valueOf(realEndDate[2]).intValue();
				int monthRealEnd = Integer.valueOf(realEndDate[1]).intValue();
				int dayRealEnd = Integer.valueOf(realEndDate[0]).intValue();

				cal.set(yearRealEnd, monthRealEnd, dayRealEnd, hoursRealEnd, minutesRealEnd, 0);
				long realEndOfWork = cal.getTimeInMillis();

				//staff member
				int index = (comboViewer.getCombo().getSelectionIndex());
				StaffMember staffMember = (StaffMember)comboViewer.getElementAt(index);

				//several parameters
				String station = comboOrtsstelle.getText();
				String servicetype = comboDienstverhaeltnis.getText();
				String competence = comboVerwendung.getText();
				String rosterNotes = textAnmerkungen.getText();
				boolean standbyState = bereitschaftButton.getSelection();

				SimpleDateFormat sdf = new SimpleDateFormat("dd.mm.yyyy HH:mm");
				System.out.println("real start of work: " +sdf.format(realStartOfWork));

				RosterEntry re = new RosterEntry(-1,staffMember,plannedStartOfWork, plannedEndOfWork, realStartOfWork, realEndOfWork, station, competence, servicetype, rosterNotes, standbyState);

				System.out.println("the roster entry: " +re.getCompetence().toString() +' ' +re.getRealEndOfWork() +' ' +re.getRealStartOfWork() +' ' +re.getRosterId() +' ' +re.getRosterNotes() +' ' +re.getServicetype() +' ' +re.getStation()
						+re.getStaffMember().getUserName());




				return re;

			}
		});
		shell.setTabList(new Control[] {dienstplanGroup, group, okButton, abbrechenButton});
		//
	}

	public void setRealWorktimesInactive()
	{
		group.setEnabled(false);
		timeAnmeldung.setBackground(inactiveBackgroundColor);
		timeAbmeldung.setBackground(inactiveBackgroundColor);
		group.setBackground(inactiveBackgroundColor);
		anmeldungLabel.setBackground(inactiveBackgroundColor);
		abmeldungLabel.setBackground(inactiveBackgroundColor);

	}

	public List<String> fillDateComboBox()
	{
		GregorianCalendar gcal = new GregorianCalendar();

		List<String> content = new ArrayList<String>();
		//the previous 5 days
		gcal.set(GregorianCalendar.DATE,(gcal.get(GregorianCalendar.DATE)-5));
		//up from tomorrow
		for (int i=0;i<=100;i++) // the next 100 days
		{
			gcal.set(GregorianCalendar.DATE,(gcal.get(GregorianCalendar.DATE))+1);
			content.add(gcal.get(GregorianCalendar.DATE)+ "." +(gcal.get(GregorianCalendar.MONTH)+1) +"." +gcal.get(GregorianCalendar.YEAR));
		}
		return content;
	}

	public String getDefaultDate()
	{
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.set(GregorianCalendar.DATE,(gcal.get(GregorianCalendar.DATE)));
		defaultDate = gcal.get(GregorianCalendar.DATE)+ "." +(gcal.get(GregorianCalendar.MONTH)+1) +"." +gcal.get(GregorianCalendar.YEAR);
		return defaultDate;
	}
}
