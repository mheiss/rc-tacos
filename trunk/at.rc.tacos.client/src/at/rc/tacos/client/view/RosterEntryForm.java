package at.rc.tacos.client.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
import at.rc.tacos.client.controller.CreateRosterEntryAction;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI (form) to manage a roster entry
 * @author b.thek
 */
public class RosterEntryForm 
{
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
	private Label anmeldungLabel;
	private Label abmeldungLabel;
	private Color inactiveBackgroundColor = SWTResourceManager.getColor(245, 245, 245);
	private String defaultDate;
	private Listener exitListener;
	private String[] timeArray = new String[]{"05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30", "00:00", "00:30", "01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30"};

//	/**
//	 * Launch the application
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			StaffMember sm = new StaffMember();
//			Calendar c = Calendar.getInstance();
//			c.setTime(new Date());
//			long time = c.getTimeInMillis();
//			//new RosterEntry();
//			RosterEntry rosterEntry = new RosterEntry(1,sm,time,time,time, time,"Kapfenberg", "Fahrer", "Hauptamtlich", "the notes", true);
//			RosterEntryForm window = new RosterEntryForm(rosterEntry);
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * used to edit an roster entry
	 * @param rosterEntry the roster entry to edit
	 */
	public RosterEntryForm(RosterEntry rosterEntry)
	{
		this.createContents();
		
		//set field contents
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.setTimeZone(TimeZone.getDefault());
		
		//check in
		gcal.setTimeInMillis(rosterEntry.getRealStartOfWork());
		String anmeldungDate = gcal.get(GregorianCalendar.DATE)+ "." +(gcal.get(GregorianCalendar.MONTH)+1) +"." +gcal.get(GregorianCalendar.YEAR);
		this.dateAnmeldung.setText(anmeldungDate);
		String anmeldungTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
		this.timeAnmeldung.setText(anmeldungTime);
		
		
		//check out
		gcal.setTimeInMillis(rosterEntry.getRealEndOfWork());
		String abmeldungDate = gcal.get(GregorianCalendar.DATE)+ "." +(gcal.get(GregorianCalendar.MONTH)+1) +"." +gcal.get(GregorianCalendar.YEAR);
		this.dateAbmeldung.setText(abmeldungDate);
		String abmeldungTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
		this.timeAbmeldung.setText(abmeldungTime);

		
		//planned start of work
		gcal.setTimeInMillis(rosterEntry.getPlannedStartOfWork());
		System.out.println("date calendar: " +gcal.get(GregorianCalendar.DATE)+ "." +(gcal.get(GregorianCalendar.MONTH)+1) +"." +gcal.get(GregorianCalendar.YEAR));
		//calendar field
		this.dateDienstVon.setDay(gcal.get(GregorianCalendar.DATE));
		this.dateDienstVon.setMonth(gcal.get(GregorianCalendar.MONTH));
		this.dateDienstVon.setYear(gcal.get(GregorianCalendar.YEAR));
		//drop downn field
		this.dateTime.setDay(gcal.get(GregorianCalendar.DATE));
		this.dateTime.setMonth(gcal.get(GregorianCalendar.MONTH));
		this.dateTime.setYear(gcal.get(GregorianCalendar.YEAR));
		
		String realStartTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
		this.timeDienstVon.setText(realStartTime);
		
		
		//planned end of work
		gcal.setTimeInMillis(rosterEntry.getPlannedEndOfWork());
		String plannedEndDate = gcal.get(GregorianCalendar.DATE)+ "." +(gcal.get(GregorianCalendar.MONTH)+1) +"." +gcal.get(GregorianCalendar.YEAR);
		this.dateDienstBis.setText(plannedEndDate);
		String plannedEndTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
		this.timeDienstBis.setText(plannedEndTime);
		
		
		//other fields
		this.textAnmerkungen.setText(rosterEntry.getRosterNotes());
		this.comboDienstverhaeltnis.setText(rosterEntry.getServicetype());
		this.comboVerwendung.setText(rosterEntry.getCompetence());
		this.comboOrtsstelle.setText(rosterEntry.getStation());
		this.bereitschaftButton.setSelection(rosterEntry.getStandby());
		this.setEmployeenameCombo.setText(rosterEntry.getStaffMember().toString());
	}
	
	public RosterEntryForm()
	{
		this.createContents();
	}

	/**
	 * Open the window
	 */
	public void open() {
		final Display display = Display.getDefault();
//		createContents();
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
		//TODO: get a list of StaffMembers from the database
		//get data
		StaffMember sm1 = new StaffMember("Helmut", "Maier", "h.maie");
		StaffMember sm2 = new StaffMember("Daniel", "Haberl", "d.habe");

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
		shell.setImage(ImageFactory.getInstance().getRegisteredImage("application.logo.small"));
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

		final Label ortsstelleLabel = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel.setBounds(213, 102,50, 13);
		ortsstelleLabel.setText("Ortsstelle:");

		comboOrtsstelle = new Combo(dienstplanGroup, SWT.READ_ONLY);
		comboOrtsstelle.setBounds(306, 99,226, 21);
		comboOrtsstelle.setItems(new String[] {"Bruck-Kapfenberg", "Bruck an der Mur", "Kapfenberg", "St. Marein", "Thörl", "Turnau", "Breitenau"});

		bereitschaftButton = new Button(dienstplanGroup, SWT.CHECK);
		bereitschaftButton.setBounds(306, 77,85, 16);
		bereitschaftButton.setText("Bereitschaft");

		timeDienstVon = new Combo (dienstplanGroup, SWT.TIME | SWT.READ_ONLY);
		timeDienstVon.setBounds(10,250,62,21);
		timeDienstVon.setItems(timeArray);


		final Label vonLabel = new Label(dienstplanGroup, SWT.NONE);
		vonLabel.setBounds(10, 231,62, 13);
		vonLabel.setText("Dienst von:");

		final Label vonLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		vonLabel_1.setBounds(128, 231,62, 13);
		vonLabel_1.setText("Dienst bis: ");

		timeDienstBis = new Combo(dienstplanGroup, SWT.READ_ONLY);
		timeDienstBis.setBounds(128, 250,62, 21);
		timeDienstBis.setItems(timeArray);
		timeDienstBis.setData("newKey", null);


		final Label ortsstelleLabel_1 = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel_1.setBounds(213, 129, 77, 13);
		ortsstelleLabel_1.setText("Verwendung:");

		comboVerwendung = new Combo(dienstplanGroup, SWT.READ_ONLY);
		comboVerwendung.setItems(new String[] {"Fahrer", "Sanitäter", "Zweithelfer", "Leitstellendisponent", "Notfallsanitäter", "Notarzt", "Sonstiges"});
		comboVerwendung.setBounds(306, 126, 226, 21);

		final Label ortsstelleLabel_1_1 = new Label(dienstplanGroup, SWT.NONE);
		ortsstelleLabel_1_1.setBounds(213, 156, 87, 13);
		ortsstelleLabel_1_1.setText("Dienstverhältnis:");

		comboDienstverhaeltnis = new Combo(dienstplanGroup,SWT.READ_ONLY);
		comboDienstverhaeltnis.setItems(new String[] {"Hauptamtlich", "Freiwillig", "Zivildienstleistender", "Sonstiges"});
		comboDienstverhaeltnis.setBounds(306, 153, 226, 21);

		final Label anmerkungenLabel = new Label(dienstplanGroup, SWT.NONE);
		anmerkungenLabel.setText("Anmerkungen:");
		anmerkungenLabel.setBounds(213, 201, 87, 13);

		textAnmerkungen = new Text(dienstplanGroup, SWT.BORDER);
		textAnmerkungen.setBounds(306, 198, 226, 100);

		dateDienstBis = new Combo(dienstplanGroup, SWT.READ_ONLY);
		dateDienstBis.setBounds(128, 277, 92, 21);
		dateDienstBis.setItems(arrayOfDates);
		dateDienstBis.setText(defaultDate);

		dateDienstVon = new DateTime(dienstplanGroup, SWT.READ_ONLY);
		dateDienstVon.setBounds(10, 277, 92, 21);
		dateDienstVon.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println ("calendar date changed - at the normal field");
			}
		});
		dienstplanGroup.setTabList(new Control[] {dateTime, setEmployeenameCombo, bereitschaftButton, comboOrtsstelle, comboVerwendung, comboDienstverhaeltnis, textAnmerkungen, timeDienstVon, dateDienstVon, timeDienstBis, dateDienstBis});

		group = new Group(shell, SWT.NONE);
		group.setText("Tatsächliche Dienstzeiten");
		group.setBounds(10, 328, 559, 110);
		

		timeAnmeldung = new Text(group, SWT.NONE);
		timeAnmeldung.setBounds(10, 47,62, 21);

		timeAbmeldung = new Text(group, SWT.NONE);
		timeAbmeldung.setBounds(133, 47,62, 21);

		dateAnmeldung = new Combo(group, SWT.READ_ONLY);
		dateAnmeldung.setBounds(10, 74,92, 21);		
		dateAnmeldung.setItems(arrayOfDates);

		dateAbmeldung = new Combo(group, SWT.READ_ONLY);
		dateAbmeldung.setBounds(133, 74,92, 21);		
		dateAbmeldung.setItems(arrayOfDates);

		anmeldungLabel = new Label(group, SWT.NONE);
		anmeldungLabel.setText("Anmeldung:");
		anmeldungLabel.setBounds(10, 25, 72, 13);

		abmeldungLabel = new Label(group, SWT.NONE);
		abmeldungLabel.setBounds(134, 25, 72, 13);
		abmeldungLabel.setText("Abmeldung:");
		group.setTabList(new Control[] {timeAnmeldung, dateAnmeldung, timeAbmeldung, dateAbmeldung});

		abbrechenButton = new Button(shell, SWT.NONE);
		abbrechenButton.setImage(ImageFactory.getInstance().getRegisteredImage("icon.stop"));
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
			int index;
			boolean standbyState;
			String station;
			String competence;
			String servicetype;
			String rosterNotes;
			String timePlannedStartOfWork;
			String timePlannedEndOfWork;
			String datePlannedEndOfWork;
			String timeRealStartOfWork;
			String dateRealStartOfWork;
			String timeRealEndOfWork;
			String dateRealEndOfWork;
			StaffMember staffMember;
			String requiredFields;//the Name of the required fields that have no content
			
			int hourCheckIn;
			int hourCheckOut;
			int minutesCheckIn;
			int minutesCheckOut;
			String formatOfRealTime;
			String requiredRealDateFields;
			String realWorkTimeNoDateIfNoTime;
			Calendar cal = Calendar.getInstance();
			long plannedStartOfWork;
			long plannedEndOfWork;
			long realStartOfWork;
			long realEndOfWork;
			
			public void handleEvent(Event event) 
			{
				requiredFields = "";
				hourCheckIn = -1;
				hourCheckOut = -1;
				minutesCheckIn = -1;
				minutesCheckOut = -1;
				formatOfRealTime = "";
				requiredRealDateFields = "";
				realWorkTimeNoDateIfNoTime = "";
				
				//get content of all fields
				this.getContentOfAllFields();
				
				//check required Fields
				if (!this.checkRequiredFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, requiredFields, "Bitte noch folgende Mussfelder ausfüllen");
				}
				
				else
				{
					//validating
					if(!this.checkFormatOfRealWorkTimeFields().equalsIgnoreCase(""))
					{
						this.displayMessageBox(event,formatOfRealTime, "Format von tatsächlicher Dienstzeit falsch: ");
						//formatOfRealTime = "";	
					}
					else
					{
						if(!this.checkDateIfTime().equalsIgnoreCase(""))
						{
							this.displayMessageBox(event, requiredRealDateFields, "Bitte tragen Sie für unten angeführte Felder auch noch ein Datum ein.");
						}
						else
						{
							this.transformToLong();//set planned work time
							//validate start before end
							if(plannedEndOfWork<plannedStartOfWork)
							{
								this.displayMessageBox(event, "Dienstende vor Dienstbeginn!", "Fehler");
							}
							else
							{
								this.noRealWDIfNoRealWT();
								if(!realWorkTimeNoDateIfNoTime.equalsIgnoreCase(""))
								{
									this.displayMessageBox(event, realWorkTimeNoDateIfNoTime, "Datum ohne Zeiteintrag");
								}
								else
								{
									this.setRealWorkTime();
									if(realEndOfWork<realStartOfWork)
									{
										this.displayMessageBox(event, "Abmeldung vor Anmeldung","Fehler");
									}
									else
									{
										RosterEntry rosterEntry = new RosterEntry(staffMember,plannedStartOfWork, plannedEndOfWork, realStartOfWork, realEndOfWork, station, competence, servicetype, rosterNotes, standbyState);
					
										//send the roster entry
										new CreateRosterEntryAction(rosterEntry).run();
									}
									
								}
							}
						}
					}					
				}
			}

			
			private void getContentOfAllFields()
			{
				index = (comboViewer.getCombo().getSelectionIndex());
				staffMember = (StaffMember)comboViewer.getElementAt(index);
				standbyState = bereitschaftButton.getSelection();
				station = comboOrtsstelle.getText();
				competence = comboVerwendung.getText();
				servicetype = comboDienstverhaeltnis.getText();
				rosterNotes = textAnmerkungen.getText();
				timePlannedStartOfWork = timeDienstVon.getText();	
				timePlannedEndOfWork = timeDienstBis.getText();
				datePlannedEndOfWork = dateDienstBis.getText();
				timeRealStartOfWork = timeAnmeldung.getText();
				dateRealStartOfWork = dateAnmeldung.getText();
				timeRealEndOfWork = timeAbmeldung.getText();
				dateRealEndOfWork = dateAbmeldung.getText();
			}
			
			private String checkRequiredFields()
			{
				if (index == -1)
					requiredFields = requiredFields +" - Mitarbeiter";
				if (station.equalsIgnoreCase(""))
					requiredFields = requiredFields +" - Ortsstelle";
				if (competence.equalsIgnoreCase(""))
					requiredFields = requiredFields +" - Verwendung";
				if (servicetype.equalsIgnoreCase(""))
					requiredFields = requiredFields +" - Dienstverhältnis";
				if (timePlannedStartOfWork.equalsIgnoreCase(""))
					requiredFields = requiredFields +" - Dienst von";
				if (timePlannedEndOfWork.equalsIgnoreCase(""))
					requiredFields = requiredFields +" - Dienst bis";
				
				return requiredFields;
			}
			
			private String checkFormatOfRealWorkTimeFields()
			{
				Pattern p4 = Pattern.compile("(\\d{2})(\\d{2})");//if content is e.g. 1234
				Pattern p5 = Pattern.compile("(\\d{2}):(\\d{2})");//if content is e.g. 12:34
				
				//check in
				if(!timeRealStartOfWork.equalsIgnoreCase(""))
				{
					Matcher m41= p4.matcher(timeRealStartOfWork);
					Matcher m51= p5.matcher(timeRealStartOfWork);
						if(m41.matches())
						{
							hourCheckIn = Integer.parseInt(m41.group(1));
							minutesCheckIn = Integer.parseInt(m41.group(2));
							
							if(hourCheckIn >= 0 && hourCheckIn <=23 && minutesCheckIn >= 0 && minutesCheckIn <=59)
							{
								timeRealStartOfWork = hourCheckIn + ":" +minutesCheckIn;//for the splitter
							}
							else
							{
								formatOfRealTime = " - Anmeldung (Zeit)";
							}
						}
						else if(m51.matches())
						{
								hourCheckIn = Integer.parseInt(m51.group(1));
								minutesCheckIn = Integer.parseInt(m51.group(2));
							
							if(!(hourCheckIn >= 0 && hourCheckIn <=23 && minutesCheckIn >= 0 && minutesCheckIn <=59))
							{
								formatOfRealTime = " - Anmeldung (Zeit)";
							}
						}
						else
						{
							formatOfRealTime = " - Anmeldung (Zeit)";
						}
				}
				
				//check out
				if (!timeRealEndOfWork.equalsIgnoreCase(""))
				{
					Matcher m42= p4.matcher(timeRealEndOfWork);
					Matcher m52= p5.matcher(timeRealEndOfWork);
					if(m42.matches())
					{
						hourCheckOut = Integer.parseInt(m42.group(1));
						minutesCheckOut = Integer.parseInt(m42.group(2));
						
						if(hourCheckOut >= 0 && hourCheckOut <=23 && minutesCheckOut >= 0 && minutesCheckOut <=59)
						{
							timeRealEndOfWork = hourCheckOut +":" +minutesCheckOut;
						}
						else
						{
							formatOfRealTime = formatOfRealTime +"Abmeldung (Zeit)";
						}
					}
					else if(m52.matches())
					{
						hourCheckOut = Integer.parseInt(m52.group(1));
						minutesCheckOut = Integer.parseInt(m52.group(2));
						
						if(!(hourCheckOut >= 0 && hourCheckOut <=23 && minutesCheckOut >= 0 && minutesCheckOut <=59))
						{
							formatOfRealTime = formatOfRealTime +"Abmeldung (Zeit)";
						}
					}
					else
					{
						formatOfRealTime = formatOfRealTime +"Abmeldung (Zeit)";
					}
				}
				return formatOfRealTime;
			}
			
			private String checkDateIfTime()
			{
				if (hourCheckIn != -1)
				{
					//a check in date must be available
					if(dateRealStartOfWork.equalsIgnoreCase(""))
					{
						requiredRealDateFields = "-Anmeldedatum";
					}
				}
				
				if (hourCheckOut != -1)
				{
					//a check out date must be available
					if(dateRealEndOfWork.equalsIgnoreCase(""))
					{
						requiredRealDateFields = requiredRealDateFields + " -Abmeldedatum";
					}
				}
				return requiredRealDateFields;
			}
			
			private void transformToLong()
			{
				//planned start of work
				//time
				String[] plannedStartTime = timePlannedStartOfWork.split(":");
				int hoursPlannedStart = Integer.valueOf(plannedStartTime[0]).intValue();
				int minutesPlannedStart = Integer.valueOf(plannedStartTime[1]).intValue();
				
				//date
				int yearPlannedStart = dateDienstVon.getYear();
				int monthPlannedStart = dateDienstVon.getMonth();
				int dayPlannedStart = dateDienstVon.getDay();

				cal.set(yearPlannedStart, monthPlannedStart, dayPlannedStart, hoursPlannedStart, minutesPlannedStart, 0);
				plannedStartOfWork = cal.getTimeInMillis();
				
				
				//planned end of work
				//time
				String[] plannedEndTime = timePlannedEndOfWork.split(":");
				int hoursPlannedEnd = Integer.valueOf(plannedEndTime[0]).intValue();
				int minutesPlannedEnd = Integer.valueOf(plannedEndTime[1]).intValue();

				//date
				String[] plannedEndDate = datePlannedEndOfWork.split("\\.");
				int yearPlannedEnd = Integer.valueOf(plannedEndDate[2]).intValue();
				int monthPlannedEnd = (Integer.valueOf(plannedEndDate[1]).intValue())-1;
				int dayPlannedEnd = Integer.valueOf(plannedEndDate[0]).intValue();

				cal.set(yearPlannedEnd, monthPlannedEnd, dayPlannedEnd, hoursPlannedEnd, minutesPlannedEnd, 0);
				plannedEndOfWork = cal.getTimeInMillis();
			}
			
			private String noRealWDIfNoRealWT()
			{
				if (timeRealStartOfWork.equalsIgnoreCase("") & !dateRealStartOfWork.equalsIgnoreCase(""))
				{
					realWorkTimeNoDateIfNoTime = " - Anmeldung";
				}
				if(timeRealEndOfWork.equalsIgnoreCase("") & !dateRealEndOfWork.equalsIgnoreCase(""))
				{
					realWorkTimeNoDateIfNoTime = realWorkTimeNoDateIfNoTime + " - Abmeldung";
				}
				return realWorkTimeNoDateIfNoTime;
			}
			private void setRealWorkTime()
			{
				//real start of work
				int hoursRealStart = 0;
				int minutesRealStart = 0;
				if(!timeRealStartOfWork.equalsIgnoreCase(""))
				{
					String[] realStartTime = timeRealStartOfWork.split(":");
					hoursRealStart = Integer.valueOf(realStartTime[0]).intValue();
					minutesRealStart = Integer.valueOf(realStartTime[1]).intValue();
				}
				
				int yearRealStart = 0;
				int monthRealStart = 0;
				int dayRealStart = 0;
				if (!dateRealStartOfWork.equalsIgnoreCase(""))
				{
					String[] realStartDate = dateRealStartOfWork.split("\\.");
					yearRealStart = Integer.valueOf(realStartDate[2]).intValue();
					monthRealStart = Integer.valueOf(realStartDate[1]).intValue()-1;
					dayRealStart = Integer.valueOf(realStartDate[0]).intValue();
				}
				cal.set(yearRealStart, monthRealStart, dayRealStart, hoursRealStart, minutesRealStart, 0);
				realStartOfWork = cal.getTimeInMillis();
				
				//real end of work
				int hoursRealEnd = 0;
				int minutesRealEnd = 0;
				if(!timeRealEndOfWork.equalsIgnoreCase(""))
				{
					String[] realEndTime = timeRealEndOfWork.split(":");
					hoursRealEnd = Integer.valueOf(realEndTime[0]).intValue();
					minutesRealEnd = Integer.valueOf(realEndTime[1]).intValue();
				}
				
				int yearRealEnd = 0;
				int monthRealEnd = 0;
				int dayRealEnd = 0;
				if(!dateRealEndOfWork.equalsIgnoreCase(""))
				{
					String[] realEndDate = dateRealEndOfWork.split("\\.");
					yearRealEnd = Integer.valueOf(realEndDate[2]).intValue();
					monthRealEnd = Integer.valueOf(realEndDate[1]).intValue()-1;
					dayRealEnd = Integer.valueOf(realEndDate[0]).intValue();
					
					cal.set(yearRealEnd, monthRealEnd, dayRealEnd, hoursRealEnd, minutesRealEnd, 0);
					realEndOfWork = cal.getTimeInMillis();
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
		shell.setTabList(new Control[] {dienstplanGroup, group, okButton, abbrechenButton});
	}

	
	//not used at the time
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
