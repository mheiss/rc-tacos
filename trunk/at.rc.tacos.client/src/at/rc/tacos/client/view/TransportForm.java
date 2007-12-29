package at.rc.tacos.client.view;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.graphics.Color;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import at.rc.tacos.client.controller.CreateTransportAction;
import at.rc.tacos.client.controller.UpdateTransportAction;
import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.ITransportPriority;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;
import at.rc.tacos.swtdesigner.SWTResourceManager;

/**
 * GUI (form) to manage the transport details
 * one form for all kinds of transports (not necessary are blanked out)
 * @author b.thek
 *
 */
public class TransportForm implements IDirectness, IKindOfTransport
{

	private Group transportdetailsGroup;
	private Text textSaniII;
	private Text textSaniI;
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
	private Button ruecktransportMoeglichButton;
	private Button begleitpersonButton;
	private Button rufhilfepatientButton;
	private Button eigenerRollstuhlButton;
	private Button krankentrageButton;
	private Button tragsesselButton;
	private Combo comboNachOrt;
	private Combo comboNachStrasse;
	private Combo comboVorname;
	private Combo comboNachname;
	private Combo comboVonOrt;
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
	
	private Listener exitListener;
	private Color inactiveBackgroundColor = SWTResourceManager.getColor(245, 245, 245);
	
	private Transport transport;
	
	//determine whether to update or to create a new entry
    private boolean createNew;
    
    /**
     * transport type used to differ between a normal and an emergency transport
     * possible values: prebooking, emergencyTransport, wholeTransportDetails
     */
    private String transportType;

	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			
			ArrayList<Patient> patientList = new ArrayList<Patient>();
	        Patient p1 = new Patient("Patient1","Patient1");
	        Patient p2 = new Patient("Patient2","Patient2");
	        Patient p3 = new Patient("Patient3","Patient3");
	        patientList.add(p1);
	        patientList.add(p2);
	        patientList.add(p3);
	        
	        
	        ArrayList<CallerDetail> notifierList = new ArrayList<CallerDetail>();
	        CallerDetail n1 = new CallerDetail("Notifer1","0664-123456789","Notes taken");
	        CallerDetail n2 = new CallerDetail("Notifer2","0784-1548154","Notes taken");
	        CallerDetail n3 = new CallerDetail("Notifer3","2147-123456789","Notes taken");
	        notifierList.add(n1);
	        notifierList.add(n2);
	        notifierList.add(n3);
	        
	        ArrayList<StaffMember> staffList = new ArrayList<StaffMember>();
	        StaffMember s1 = new StaffMember("Staff1","Staff1","nick.staff1");
	        s1.setPersonId(0);
	        StaffMember s2 = new StaffMember("Staff2","Staff2","nick.staff2");
	        s2.setPersonId(1);
	        StaffMember s3 = new StaffMember("Staff3","Staff3","nick.staff3");
	        s3.setPersonId(2);
	        staffList.add(s1);
	        staffList.add(s2);
	        staffList.add(s3);
	        
	        
	        ArrayList<MobilePhoneDetail> phoneList = new ArrayList<MobilePhoneDetail>();
	        MobilePhoneDetail ph1 = new MobilePhoneDetail("P1","0699-123456789");
	        MobilePhoneDetail ph2 = new MobilePhoneDetail("P2","0664-123456789");
	        MobilePhoneDetail ph3 = new MobilePhoneDetail("P3","0676-123456789");
	        phoneList.add(ph1);
	        phoneList.add(ph2);
	        phoneList.add(ph3);
	        
	        
	      //create the list
	        ArrayList<VehicleDetail>vehicleList = new ArrayList<VehicleDetail>();
	        //load dummy data
	        VehicleDetail v1 = new VehicleDetail();
	        v1.setVehicleId(0);
	        v1.setVehicleName("Bm01");
	        v1.setVehicleType("RTW");
	        v1.setVehicleNotes("notes vehicle 1");
	        v1.setBasicStation("BM");
	        v1.setCurrentStation("BM");
	        v1.setReadyForAction(true);
	        v1.setOutOfOrder(false);
	        v1.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
	        v1.setDriverName(staffList.get(0));
	        v1.setParamedicIName(staffList.get(1));
	        v1.setParamedicIIName(staffList.get(2));
	        v1.setMobilPhone(phoneList.get(0));
	        
	        
	        //second vehicle
	        VehicleDetail v2 = new VehicleDetail();
	        v2.setVehicleId(1);
	        v2.setVehicleName("Bm02");
	        v2.setVehicleType("KTW");
	        v2.setVehicleNotes("notes vehicle 2");
	        v2.setBasicStation("BM");
	        v2.setCurrentStation("KA");
	        v2.setReadyForAction(true);
	        v2.setOutOfOrder(false);
	        v2.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
	        v2.setDriverName(staffList.get(0));
	        v2.setParamedicIName(staffList.get(1));
	        v2.setParamedicIIName(staffList.get(2));
	        v2.setMobilPhone(phoneList.get(1));
	        //third vehicle
	        VehicleDetail v3 = new VehicleDetail();
	        v3.setVehicleId(2);
	        v3.setVehicleName("Bm03");
	        v3.setVehicleType("RTW");
	        v3.setBasicStation("KA");
	        v3.setCurrentStation("KA");
	        v3.setReadyForAction(false);
	        v3.setOutOfOrder(true);
	        v3.setMostImportantTransportStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
	        v3.setDriverName(staffList.get(0));
	        v3.setParamedicIName(staffList.get(1));
	        v3.setParamedicIIName(staffList.get(2));
	        v3.setMobilPhone(phoneList.get(2));
	        //add to list
	        vehicleList.add(v1);
	        vehicleList.add(v2);
	        vehicleList.add(v3);
	        
	        
	        
			Transport t1 = new Transport();
	        t1.setTransportId(0);
	        t1.setFromStreet("street_from_1");
	        t1.setFromCity("city_from_1");
	        t1.setToStreet("street_to_1");
	        t1.setToCity("city_to_1");
	        t1.setPatient(patientList.get(0));
	        t1.addStatus(1, new Date().getTime());
	        t1.addStatus(2, new Date().getTime());
	        t1.setCallerDetail(notifierList.get(0));
	        t1.setVehicleDetail(vehicleList.get(0));
	        t1.setTransportPriority(ITransportPriority.TRANSPORT_PRIORITY_BLUELIGHT);
	        t1.setDirection(IDirectness.TOWARDS_DISTRICT);
			
			
			TransportForm window = new TransportForm(t1);
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
     * Default class constructor used to create
     * a new transport entry.
     */
    public TransportForm()
    {
        createNew = true;
        this.transport = new Transport();
        //set up the filds
        createContents();
    }
    
    /**
     * constructor used to create a 
     * a new transport entry.
     */
    public TransportForm(String transportType)
    {
        createNew = true;
        this.transportType = transportType;
        this.transport = new Transport();
        //set up the filds
        createContents();
    }
    
    
    /**
     * used to edit an roster entry
     * @param rosterEntry the roster entry to edit
     */
    public TransportForm(Transport transport)
    {
        //update an entry
        createNew = false;
        this.transport = transport;
       
        //create the fields
        createContents();

        //set field contents
        GregorianCalendar gcal = new GregorianCalendar();
        gcal.setTimeZone(TimeZone.getDefault());
        
        //date of transport
        gcal.setTimeInMillis(transport.getDateOfTransport());
        this.dateTime.setDay(gcal.get(GregorianCalendar.DATE));
        this.dateTime.setMonth(gcal.get(GregorianCalendar.MONTH));
        this.dateTime.setYear(gcal.get(GregorianCalendar.YEAR));
        
        //planned start of transport
        if(transport.getPlannedStartOfTransport() != 0)
        {
        	gcal.setTimeInMillis(transport.getPlannedStartOfTransport());
        	String abfahrtTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textAbf.setText(abfahrtTime);
        }
        
        //time at patient
        if (transport.getPlannedTimeAtPatient() != 0)
        {
        	gcal.setTimeInMillis(transport.getPlannedTimeAtPatient());
        	String beiPatientTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textBeiPat.setText(beiPatientTime);
        }
        
        //time at destination
        if (transport.getAppointmentTimeAtDestination() != 0)
        {
        	gcal.setTimeInMillis(transport.getAppointmentTimeAtDestination());
        	String terminTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textTermin.setText(terminTime);
        }

        //other fields
        this.begleitpersonButton.setSelection(transport.isAccompanyingPerson());
        this.bergrettungButton.setSelection(transport.isMountainRescueServiceAlarming());
        this.brkdtButton.setSelection(transport.isBrkdtAlarming());

       
        if(transport.getKindOfIllness()!= null)
        	this.comboErkrankungVerletzung.setText(transport.getKindOfIllness());
        
        if(transport.getPatient().getLastname() != null)
        	this.comboNachname.setText(transport.getPatient().getLastname());
        
        if(transport.getToCity() != null)
        	this.comboNachOrt.setText(transport.getToCity());
        
        if(transport.getToCity() != null)
        this.comboNachStrasse.setText(transport.getToStreet());
        
        //mandatory fields
        this.comboPrioritaet.setText(transport.getTransportPriority());
        this.comboVonStrasse.setText(transport.getFromStreet());
        
        if(transport.getResponsibleStation() != null)
        this.comboZustaendigeOrtsstelle.setText(transport.getResponsibleStation());//mandatory!! default: Bezirk
        
        if(transport.getFromCity() != null)
        	this.comboVonOrt.setText(transport.getFromCity());
        
        if(transport.getPatient().getFirstname() != null)
        	this.comboVorname.setText(transport.getPatient().getFirstname());
        
        
        this.dfButton.setSelection(transport.isDfAlarming());
        this.fernfahrtButton.setSelection(transport.isLongDistanceTrip());
        this.feuerwehrButton.setSelection(transport.isFirebrigadeAlarming());
        this.notarztButton.setSelection(transport.isEmergencyDoctorAlarming());
        this.polizeiButton.setSelection(transport.isPoliceAlarming());
        this.rthButton.setSelection(transport.isHelicopterAlarming());
        this.ruecktransportMoeglichButton.setSelection(transport.isBackTransport());
        
        if(transport.getDiseaseNotes() != null)
        {
        	this.textAnmerkungen.setText(transport.getDiseaseNotes());
        }
        
        if(transport.getCallerDetail().getCallerName() != null)
        	this.textAnrufer.setText(transport.getCallerDetail().getCallerName());
        
        if(transport.getVehicleDetail().getDriverName().getLastName() != null)
        	this.textFahrer.setText(transport.getVehicleDetail().getDriverName().getLastName() +" " +transport.getVehicleDetail().getDriverName().getFirstName());

        if(transport.getVehicleDetail().getVehicleName() != null)
        	this.textFahrzeug.setText(transport.getVehicleDetail().getVehicleName());
        
        if(transport.getRealStation() != null)
        	this.textOrtsstelle.setText(transport.getRealStation());
        
        if(transport.getFeedback() != null)
        	this.textRueckmeldung.setText(transport.getFeedback());
        
        if(transport.getVehicleDetail().getParamedicIName().getLastName() != null)
        	this.textSaniI.setText(transport.getVehicleDetail().getParamedicIName().getLastName() +" " +transport.getVehicleDetail().getParamedicIName().getFirstName());
        
        if(transport.getVehicleDetail().getParamedicIIName().getLastName() != null)
        	this.textSaniII.setText(transport.getVehicleDetail().getParamedicIIName().getLastName() +" " +transport.getVehicleDetail().getParamedicIIName().getFirstName());
       
        if(transport.getCallerDetail().getCallerTelephoneNumber() != null)
        	this.textTelefonAnrufer.setText(transport.getCallerDetail().getCallerTelephoneNumber());
        
        if(transport.getTransportNumber() != null)
        	this.textTransportNummer.setText(transport.getTransportNumber());
        
        
        
        
        
        //kind of transport
        String kindOfTransport = transport.getKindOfTransport();
        if(TRANSPORT_KIND_GEHEND.equalsIgnoreCase(kindOfTransport))
        {
        	this.gehendButton.setSelection(true);
        }
        else if (TRANSPORT_KIND_TRAGSESSEL.equalsIgnoreCase(kindOfTransport))
        {
        	this.tragsesselButton.setSelection(true);
        }
        else if (TRANSPORT_KIND_KRANKENTRAGE.equalsIgnoreCase(kindOfTransport))
        {
        	this.krankentrageButton.setSelection(true);
        }
        else if (TRANSPORT_KIND_ROLLSTUHL.equalsIgnoreCase(kindOfTransport))
        {
        	this.eigenerRollstuhlButton.setSelection(true);
        }
       
        
        
        
        //directness
        int direction = transport.getDirection();
        if (TOWARDS_DISTRICT == direction)
        {
        	this.bezirkButton.setSelection(true);
        }
        if (TOWARDS_GRAZ == direction)
        {
        	this.grazButton.setSelection(true);
        }
        if (TOWARDS_LEOBEN == direction)
        {
        	this.leobenButton.setSelection(true);
        }
        if (TOWARDS_MARIAZELL== direction)
        {
        	this.mariazellButton.setSelection(true);
        }
        if (TOWARDS_VIENNA == direction)
        {
        	this.wienButton.setSelection(true);
        }
        
        
        //type of editing (Vormerkung, Notfall, Alles - über Construktor und über Buttons)
//
//        //other fields
//        if(rosterEntry.getRosterNotes() != null)
//            this.textAnmerkungen.setText(rosterEntry.getRosterNotes());
//        this.comboDienstverhaeltnis.setText(rosterEntry.getServicetype());
//        this.comboVerwendung.setText(rosterEntry.getJob());
//        this.comboOrtsstelle.setText(rosterEntry.getStation());
//        this.bereitschaftButton.setSelection(rosterEntry.getStandby());
//        this.setEmployeenameCombo.setSelection(new StructuredSelection(rosterEntry.getStaffMember()));
    }


    
    

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setLayout(new FormLayout());
		shell.setRegion(null);
		shell.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/Tacos_LOGO.jpg"));
		shell.setSize(1075, 545);
		shell.setText("Transport");

		
		//TODO get lists from database
		//ArrayList<String> streetList = new ArrayList<String>(Arrays.asList("Leobnerstraße", "Grazerstraße"));
		
		
		
		
		
		
		//listener
		exitListener = new Listener() {
			public void handleEvent(Event e) 
			{
				MessageBox dialog = new MessageBox(shell, SWT.YES | SWT.NO | SWT.ICON_QUESTION);
				dialog.setText("Abbrechen");
				dialog.setMessage("Wollen Sie wirklich abbrechen?");
				if (e.type == SWT.Close) 
					e.doit = false;
				if (dialog.open() != SWT.YES) 
					return;
				shell.dispose();
			}
		};
		
		
		
		
		//calendar
		dateTime = new DateTime(shell, SWT.CALENDAR);
		final FormData fd_dateTime = new FormData();
		fd_dateTime.bottom = new FormAttachment(0, 160);
		fd_dateTime.top = new FormAttachment(0, 10);
		fd_dateTime.right = new FormAttachment(0, 187);
		fd_dateTime.left = new FormAttachment(0, 10);
		dateTime.setLayoutData(fd_dateTime);

		//group 'Transportdaten'
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
//		new FormAttachment()
		vonLabel.setLayoutData(fd_vonLabel);
		vonLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		vonLabel.setText("von:");

		comboNachStrasse = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboNachStrasse = new FormData();
		fd_comboNachStrasse.right = new FormAttachment(0, 260);
		fd_comboNachStrasse.bottom = new FormAttachment(0, 74);
		fd_comboNachStrasse.top = new FormAttachment(0, 53);
		fd_comboNachStrasse.left = new FormAttachment(0, 38);
		comboNachStrasse.setLayoutData(fd_comboNachStrasse);
		comboNachStrasse.setItems(new String[] {"Leobnerstraße", "Mariazellerstraße", "Am Hang", "Wienerstraße"});//TODO get from db

		comboVonStrasse = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboVonStrasse = new FormData();
		fd_comboVonStrasse.right = new FormAttachment(0, 260);
		fd_comboVonStrasse.bottom = new FormAttachment(0, 47);
		fd_comboVonStrasse.top = new FormAttachment(0, 26);
		fd_comboVonStrasse.left = new FormAttachment(0, 38);
		comboVonStrasse.setLayoutData(fd_comboVonStrasse);
		comboVonStrasse.setItems(new String[] {"Leobnerstraße", "Mariazellerstraße", "Am Hang", "Wienerstraße"});//TODO get from db

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

		comboVonOrt = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboVonOrt = new FormData();
		fd_comboVonOrt.left = new FormAttachment(0, 274);
		fd_comboVonOrt.bottom = new FormAttachment(0, 47);
		fd_comboVonOrt.top = new FormAttachment(0, 26);
		fd_comboVonOrt.right = new FormAttachment(0, 430);
		comboVonOrt.setLayoutData(fd_comboVonOrt);
		comboVonOrt.setItems(new String[] {"Bruck an der Mur", "Oberaich", "Pernegg", "Turnau", "Kapfenberg"});//TODO get form db

		comboNachOrt = new Combo(transportdatenGroup, SWT.NONE);
		final FormData fd_comboNachOrt = new FormData();
		fd_comboNachOrt.left = new FormAttachment(0, 274);
		fd_comboNachOrt.bottom = new FormAttachment(0, 74);
		fd_comboNachOrt.top = new FormAttachment(0, 53);
		fd_comboNachOrt.right = new FormAttachment(0, 430);
		comboNachOrt.setLayoutData(fd_comboNachOrt);
		comboNachOrt.setItems(new String[] {"Bruck an der Mur", "Oberaich", "Pernegg", "Turnau", "Kapfenberg"});//TODO get form db

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
		begleitpersonButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_BeglPerson.gif"));

		ruecktransportMoeglichButton = new Button(transportdatenGroup, SWT.CHECK);
		final FormData fd_button_1 = new FormData();
		fd_button_1.bottom = new FormAttachment(0, 96);
		fd_button_1.top = new FormAttachment(0, 80);
		fd_button_1.right = new FormAttachment(0, 159);
		fd_button_1.left = new FormAttachment(0, 38);
		ruecktransportMoeglichButton.setLayoutData(fd_button_1);
		ruecktransportMoeglichButton.setText("Rücktransport möglich");

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
		transportdatenGroup.setTabList(new Control[] {comboVonStrasse, comboVonOrt, comboNachname, comboVorname, comboNachStrasse, comboNachOrt, gehendButton, tragsesselButton, krankentrageButton, eigenerRollstuhlButton, ruecktransportMoeglichButton, comboZustaendigeOrtsstelle, begleitpersonButton, textAnrufer, textTelefonAnrufer});

		planungGroup = new Group(shell, SWT.NONE);
		planungGroup.setLayout(new FormLayout());
		final FormData fd_planungGroup = new FormData();
		fd_planungGroup.bottom = new FormAttachment(0, 348);
		fd_planungGroup.top = new FormAttachment(0, 166);
		fd_planungGroup.right = new FormAttachment(0, 187);
		fd_planungGroup.left = new FormAttachment(0, 10);
		planungGroup.setLayoutData(fd_planungGroup);
		planungGroup.setText("Zeiten/Richtung");

		//group 'Zeiten/Richtung' 
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

		//'Richtung'
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
		fernfahrtButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_Fernfahrt.gif"));
		fernfahrtButton.setToolTipText("Fernfahrten sind lt. RKT deklariert");
		planungGroup.setTabList(new Control[] {textAbf, textBeiPat, textTermin, fernfahrtButton, bezirkButton, grazButton, leobenButton, wienButton, mariazellButton});

		//group 'Patientenzustand'
		patientenzustandGroup = new Group(shell, SWT.NONE);
		patientenzustandGroup.setLayout(new FormLayout());
		final FormData fd_patientenzustandGroup = new FormData();
		fd_patientenzustandGroup.bottom = new FormAttachment(0, 348);
		fd_patientenzustandGroup.top = new FormAttachment(0, 166);
		fd_patientenzustandGroup.right = new FormAttachment(0, 920);//TODO
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
		comboErkrankungVerletzung.setItems(new String[] {"Schlaganfall", "Herzinfarkt", "Atemnot", "Pseudokrupp"});//TODO get form db


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
		comboPrioritaet.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), G (NEF extern)");
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
		patientenzustandGroup.setTabList(new Control[] {comboErkrankungVerletzung, comboPrioritaet, textAnmerkungen, textRueckmeldung, bd2Button});

		//group 'Alarmierung'
		planungGroup_1 = new Group(shell, SWT.NONE);
		planungGroup_1.setLayout(new FormLayout());
		final FormData fd_planungGroup_1 = new FormData();
		fd_planungGroup_1.bottom = new FormAttachment(0, 348);
		fd_planungGroup_1.top = new FormAttachment(0, 166);
		fd_planungGroup_1.right = new FormAttachment(0, 1056);
		fd_planungGroup_1.left = new FormAttachment(0, 927);
		planungGroup_1.setLayoutData(fd_planungGroup_1);
		planungGroup_1.setText("Alarmierung");

		notarztButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_notarztButton = new FormData();
		fd_notarztButton.bottom = new FormAttachment(0, 27);
		fd_notarztButton.top = new FormAttachment(0, 11);
		fd_notarztButton.right = new FormAttachment(0, 92);
		fd_notarztButton.left = new FormAttachment(0, 7);
		notarztButton.setLayoutData(fd_notarztButton);
		notarztButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_NEF.gif"));
		notarztButton.setToolTipText("Externer Notarzt für diesen Transport alarmiert");

		rthButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_rthButton = new FormData();
		fd_rthButton.bottom = new FormAttachment(0, 49);
		fd_rthButton.top = new FormAttachment(0, 33);
		fd_rthButton.right = new FormAttachment(0, 92);
		fd_rthButton.left = new FormAttachment(0, 7);
		rthButton.setLayoutData(fd_rthButton);
		rthButton.setToolTipText("Hubschrauber");
		rthButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_Hubi.gif"));

		dfButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_dfButton = new FormData();
		fd_dfButton.bottom = new FormAttachment(0, 71);
		fd_dfButton.top = new FormAttachment(0, 55);
		fd_dfButton.right = new FormAttachment(0, 80);
		fd_dfButton.left = new FormAttachment(0, 7);
		dfButton.setLayoutData(fd_dfButton);
		dfButton.setToolTipText("DF/Inspektionsdienst");
		dfButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_Rotlicht.gif"));

		brkdtButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_brkdtButton = new FormData();
		fd_brkdtButton.bottom = new FormAttachment(0, 93);
		fd_brkdtButton.top = new FormAttachment(0, 77);
		fd_brkdtButton.right = new FormAttachment(0, 92);
		fd_brkdtButton.left = new FormAttachment(0, 7);
		brkdtButton.setLayoutData(fd_brkdtButton);
		brkdtButton.setToolTipText("Bezirksrettungskommandant");
		brkdtButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_Rotlicht.gif"));

		feuerwehrButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_feuerwehrButton = new FormData();
		fd_feuerwehrButton.bottom = new FormAttachment(0, 115);
		fd_feuerwehrButton.top = new FormAttachment(0, 99);
		fd_feuerwehrButton.right = new FormAttachment(0, 80);
		fd_feuerwehrButton.left = new FormAttachment(0, 7);
		feuerwehrButton.setLayoutData(fd_feuerwehrButton);
		feuerwehrButton.setToolTipText("Feuerwehr");
		feuerwehrButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_FF.gif"));
		feuerwehrButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});

		
		rufhilfepatientButton = new Button(transportdatenGroup, SWT.CHECK);
		final FormData fd_rufhilfepatientButton = new FormData();
		fd_rufhilfepatientButton.bottom = new FormAttachment(0, 96);
		fd_rufhilfepatientButton.top = new FormAttachment(0, 80);
		fd_rufhilfepatientButton.right = new FormAttachment(0, 547);
		fd_rufhilfepatientButton.left = new FormAttachment(0, 462);
		rufhilfepatientButton.setLayoutData(fd_rufhilfepatientButton);
		rufhilfepatientButton.setText("Rufhilfepatient");
		  
		  
		polizeiButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_polizeiButton = new FormData();
		fd_polizeiButton.bottom = new FormAttachment(0, 137);
		fd_polizeiButton.top = new FormAttachment(0, 121);
		fd_polizeiButton.right = new FormAttachment(0, 92);
		fd_polizeiButton.left = new FormAttachment(0, 7);
		polizeiButton.setLayoutData(fd_polizeiButton);
		polizeiButton.setToolTipText("Polizei");
		polizeiButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_Polizei.gif"));

		bergrettungButton = new Button(planungGroup_1, SWT.CHECK);
		final FormData fd_bergrettungButton = new FormData();
		fd_bergrettungButton.bottom = new FormAttachment(0, 159);
		fd_bergrettungButton.top = new FormAttachment(0, 143);
		fd_bergrettungButton.right = new FormAttachment(0, 92);
		fd_bergrettungButton.left = new FormAttachment(0, 7);
		bergrettungButton.setLayoutData(fd_bergrettungButton);
		bergrettungButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/O_Bergrettung.gif"));
		bergrettungButton.setToolTipText("Bergrettung");
		bergrettungButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
			}
		});

		planungGroup_1.setTabList(new Control[] {notarztButton, rthButton, dfButton, brkdtButton, feuerwehrButton, polizeiButton, bergrettungButton});

		final Label label_5 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		final FormData fd_label_5 = new FormData();
		fd_label_5.bottom = new FormAttachment(0, 367);
		fd_label_5.top = new FormAttachment(0, 354);
		fd_label_5.right = new FormAttachment(0, 1056);
		fd_label_5.left = new FormAttachment(0, 10);
		label_5.setLayoutData(fd_label_5);

		//group 'Transportdetails'
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

		final Label ortsstelleLabel = new Label(transportdetailsGroup, SWT.NONE);
		final FormData fd_ortsstelleLabel = new FormData();
		fd_ortsstelleLabel.bottom = new FormAttachment(0, 54);
		fd_ortsstelleLabel.top = new FormAttachment(0, 41);
		fd_ortsstelleLabel.right = new FormAttachment(0, 57);
		fd_ortsstelleLabel.left = new FormAttachment(0, 7);
		ortsstelleLabel.setLayoutData(fd_ortsstelleLabel);
		ortsstelleLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ortsstelleLabel.setText("Ortsstelle:");

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

		final Label farzeugLabel = new Label(transportdetailsGroup, SWT.NONE);
		final FormData fd_farzeugLabel = new FormData();
		fd_farzeugLabel.bottom = new FormAttachment(0, 81);
		fd_farzeugLabel.top = new FormAttachment(0, 68);
		fd_farzeugLabel.right = new FormAttachment(0, 57);
		fd_farzeugLabel.left = new FormAttachment(0, 7);
		farzeugLabel.setLayoutData(fd_farzeugLabel);
		farzeugLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		farzeugLabel.setText("Fahrzeug:");
		transportdetailsGroup.setTabList(new Control[] {textTransportNummer, textOrtsstelle, textFahrzeug});

		
		//group 'Personal am Fahrzeug'
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

		textSaniI = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		final FormData fd_textSnaniI = new FormData();
		fd_textSnaniI.bottom = new FormAttachment(0, 59);
		fd_textSnaniI.top = new FormAttachment(0, 38);
		fd_textSnaniI.right = new FormAttachment(0, 276);
		fd_textSnaniI.left = new FormAttachment(0, 73);
		textSaniI.setLayoutData(fd_textSnaniI);

		textSaniII = new Text(personalAmFahrzeugGroup, SWT.BORDER);
		final FormData fd_textSaniII = new FormData();
		fd_textSaniII.bottom = new FormAttachment(0, 86);
		fd_textSaniII.top = new FormAttachment(0, 65);
		fd_textSaniII.right = new FormAttachment(0, 276);
		fd_textSaniII.left = new FormAttachment(0, 73);
		textSaniII.setLayoutData(fd_textSaniII);

		final Label driverLabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
		final FormData fd_driverLabel = new FormData();
		fd_driverLabel.bottom = new FormAttachment(0, 27);
		fd_driverLabel.top = new FormAttachment(0, 14);
		fd_driverLabel.right = new FormAttachment(0, 54);
		fd_driverLabel.left = new FormAttachment(0, 7);
		driverLabel.setLayoutData(fd_driverLabel);
		driverLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		driverLabel.setText("Fahrer:");

		final Label paramedicILabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
		final FormData fd_paramedicILabel = new FormData();
		fd_paramedicILabel.bottom = new FormAttachment(0, 54);
		fd_paramedicILabel.top = new FormAttachment(0, 41);
		fd_paramedicILabel.right = new FormAttachment(0, 68);
		fd_paramedicILabel.left = new FormAttachment(0, 7);
		paramedicILabel.setLayoutData(fd_paramedicILabel);
		paramedicILabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		paramedicILabel.setText("Sanitäter I:");

		final Label paramedicIILabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
		final FormData fd_paramedicIILabel = new FormData();
		fd_paramedicIILabel.bottom = new FormAttachment(0, 81);
		fd_paramedicIILabel.top = new FormAttachment(0, 68);
		fd_paramedicIILabel.right = new FormAttachment(0, 68);
		fd_paramedicIILabel.left = new FormAttachment(0, 7);
		paramedicIILabel.setLayoutData(fd_paramedicIILabel);
		paramedicIILabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		paramedicIILabel.setText("Sanitäter II:");
		personalAmFahrzeugGroup.setTabList(new Control[] {textFahrer, textSaniI, textSaniII});

		
		//group 'Statusmeldungen'
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

		final Label aufgLabel = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_aufgLabel = new FormData();
		fd_aufgLabel.bottom = new FormAttachment(0, 26);
		fd_aufgLabel.top = new FormAttachment(0, 13);
		fd_aufgLabel.right = new FormAttachment(0, 43);
		fd_aufgLabel.left = new FormAttachment(0, 12);
		aufgLabel.setLayoutData(fd_aufgLabel);
		aufgLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		aufgLabel.setText("Aufg.:");

		
		final Text textAE = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textAE = new FormData();
		fd_textAE.bottom = new FormAttachment(0, 59);
		fd_textAE.top = new FormAttachment(0, 38);
		fd_textAE.right = new FormAttachment(0, 85);
		fd_textAE.left = new FormAttachment(0, 44);
		textAE.setLayoutData(fd_textAE);

		final Label aeLabel = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_aeLabel = new FormData();
		fd_aeLabel.bottom = new FormAttachment(0, 58);
		fd_aeLabel.top = new FormAttachment(0, 45);
		fd_aeLabel.right = new FormAttachment(0, 43);
		fd_aeLabel.left = new FormAttachment(0, 12);
		aeLabel.setLayoutData(fd_aeLabel);
		aeLabel.setForeground(SWTResourceManager.getColor(128, 128, 128));
		aeLabel.setText("AE:");

		
		final Text textS1 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS1 = new FormData();
		fd_textS1.bottom = new FormAttachment(0, 32);
		fd_textS1.top = new FormAttachment(0, 11);
		fd_textS1.right = new FormAttachment(0, 173);
		fd_textS1.left = new FormAttachment(0, 132);
		textS1.setLayoutData(fd_textS1);

		final Label ts1Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts1Label = new FormData();
		fd_ts1Label.bottom = new FormAttachment(0, 26);
		fd_ts1Label.top = new FormAttachment(0, 13);
		fd_ts1Label.right = new FormAttachment(0, 141);
		fd_ts1Label.left = new FormAttachment(0, 110);
		ts1Label.setLayoutData(fd_ts1Label);
		ts1Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts1Label.setText("S1:");

		
		final Text textS2 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS2 = new FormData();
		fd_textS2.bottom = new FormAttachment(0, 59);
		fd_textS2.top = new FormAttachment(0, 38);
		fd_textS2.right = new FormAttachment(0, 173);
		fd_textS2.left = new FormAttachment(0, 132);
		textS2.setLayoutData(fd_textS2);
		
		final Label ts2Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts2Label = new FormData();
		fd_ts2Label.bottom = new FormAttachment(0, 59);
		fd_ts2Label.top = new FormAttachment(0, 46);
		fd_ts2Label.right = new FormAttachment(0, 141);
		fd_ts2Label.left = new FormAttachment(0, 110);
		ts2Label.setLayoutData(fd_ts2Label);
		ts2Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts2Label.setText("S2:");

		
		final Text textS3 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS3 = new FormData();
		fd_textS3.bottom = new FormAttachment(0, 86);
		fd_textS3.top = new FormAttachment(0, 65);
		fd_textS3.right = new FormAttachment(0, 173);
		fd_textS3.left = new FormAttachment(0, 132);
		textS3.setLayoutData(fd_textS3);

		final Label ts3Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts3Label = new FormData();
		fd_ts3Label.bottom = new FormAttachment(0, 86);
		fd_ts3Label.top = new FormAttachment(0, 73);
		fd_ts3Label.right = new FormAttachment(0, 141);
		fd_ts3Label.left = new FormAttachment(0, 110);
		ts3Label.setLayoutData(fd_ts3Label);
		ts3Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts3Label.setText("S3:");

		
		final Text textS4 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS4 = new FormData();
		fd_textS4.bottom = new FormAttachment(0, 32);
		fd_textS4.top = new FormAttachment(0, 11);
		fd_textS4.right = new FormAttachment(0, 255);
		fd_textS4.left = new FormAttachment(0, 214);
		textS4.setLayoutData(fd_textS4);
		
		final Label ts4Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts4Label = new FormData();
		fd_ts4Label.bottom = new FormAttachment(0, 27);
		fd_ts4Label.top = new FormAttachment(0, 14);
		fd_ts4Label.right = new FormAttachment(0, 213);
		fd_ts4Label.left = new FormAttachment(0, 195);
		ts4Label.setLayoutData(fd_ts4Label);
		ts4Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts4Label.setText("S4:");

		
		final Text textS5 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS5 = new FormData();
		fd_textS5.bottom = new FormAttachment(0, 59);
		fd_textS5.top = new FormAttachment(0, 38);
		fd_textS5.right = new FormAttachment(0, 255);
		fd_textS5.left = new FormAttachment(0, 214);
		textS5.setLayoutData(fd_textS5);
		
		final Label ts5Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts5Label = new FormData();
		fd_ts5Label.bottom = new FormAttachment(0, 54);
		fd_ts5Label.top = new FormAttachment(0, 41);
		fd_ts5Label.right = new FormAttachment(0, 213);
		fd_ts5Label.left = new FormAttachment(0, 195);
		ts5Label.setLayoutData(fd_ts5Label);
		ts5Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts5Label.setText("S5:");

		
		final Text textS6 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS6 = new FormData();
		fd_textS6.bottom = new FormAttachment(0, 86);
		fd_textS6.top = new FormAttachment(0, 65);
		fd_textS6.right = new FormAttachment(0, 255);
		fd_textS6.left = new FormAttachment(0, 214);
		textS6.setLayoutData(fd_textS6);

		final Label ts6Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts6Label = new FormData();
		fd_ts6Label.bottom = new FormAttachment(0, 81);
		fd_ts6Label.top = new FormAttachment(0, 68);
		fd_ts6Label.right = new FormAttachment(0, 213);
		fd_ts6Label.left = new FormAttachment(0, 195);
		ts6Label.setLayoutData(fd_ts6Label);
		ts6Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts6Label.setText("S6:");

		
		final Text textS7 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS7 = new FormData();
		fd_textS7.bottom = new FormAttachment(0, 32);
		fd_textS7.top = new FormAttachment(0, 11);
		fd_textS7.right = new FormAttachment(0, 339);
		fd_textS7.left = new FormAttachment(0, 298);
		textS7.setLayoutData(fd_textS7);
		
		final Label ts7Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts7Label = new FormData();
		fd_ts7Label.bottom = new FormAttachment(0, 27);
		fd_ts7Label.top = new FormAttachment(0, 14);
		fd_ts7Label.right = new FormAttachment(0, 295);
		fd_ts7Label.left = new FormAttachment(0, 277);
		ts7Label.setLayoutData(fd_ts7Label);
		ts7Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts7Label.setText("S7:");
		
		
		final Text textS8 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS8 = new FormData();
		fd_textS8.bottom = new FormAttachment(0, 59);
		fd_textS8.top = new FormAttachment(0, 38);
		fd_textS8.right = new FormAttachment(0, 339);
		fd_textS8.left = new FormAttachment(0, 298);
		textS8.setLayoutData(fd_textS8);
		
		final Label ts8Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts8Label = new FormData();
		fd_ts8Label.bottom = new FormAttachment(0, 54);
		fd_ts8Label.top = new FormAttachment(0, 41);
		fd_ts8Label.right = new FormAttachment(0, 295);
		fd_ts8Label.left = new FormAttachment(0, 277);
		ts8Label.setLayoutData(fd_ts8Label);
		ts8Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts8Label.setText("S8:");

		
		final Text textS9 = new Text(statusmeldungenGroup, SWT.BORDER);
		final FormData fd_textS9 = new FormData();
		fd_textS9.bottom = new FormAttachment(0, 86);
		fd_textS9.top = new FormAttachment(0, 65);
		fd_textS9.right = new FormAttachment(0, 339);
		fd_textS9.left = new FormAttachment(0, 298);
		textS9.setLayoutData(fd_textS9);

		final Label ts9Label = new Label(statusmeldungenGroup, SWT.NONE);
		final FormData fd_ts9Label = new FormData();
		fd_ts9Label.bottom = new FormAttachment(0, 81);
		fd_ts9Label.top = new FormAttachment(0, 68);
		fd_ts9Label.right = new FormAttachment(0, 295);
		fd_ts9Label.left = new FormAttachment(0, 277);
		ts9Label.setLayoutData(fd_ts9Label);
		ts9Label.setForeground(SWTResourceManager.getColor(128, 128, 128));
		ts9Label.setText("S9:");

		
		//set uninteresting groups invisible
		if ("prebooking".equalsIgnoreCase(transportType))
		{
			transportdetailsGroup.setVisible(false);
			statusmeldungenGroup.setVisible(false);
			personalAmFahrzeugGroup.setVisible(false);
			planungGroup_1.setVisible(false);
		}
		
		if ("emergencyTransport".equalsIgnoreCase(transportType))
		{
			transportdetailsGroup.setVisible(false);
			statusmeldungenGroup.setVisible(false);
			personalAmFahrzeugGroup.setVisible(false);
			planungGroup.setVisible(false);
		}
		
		
		
		
		
		
		
		
		//buttons
		abbrechenButton = new Button(shell, SWT.NONE);
		final FormData fd_abbrechenButton = new FormData();
		fd_abbrechenButton.bottom = new FormAttachment(0, 501);
		fd_abbrechenButton.top = new FormAttachment(0, 478);
		fd_abbrechenButton.right = new FormAttachment(0, 1056);
		fd_abbrechenButton.left = new FormAttachment(0, 960);
		abbrechenButton.setLayoutData(fd_abbrechenButton);
		abbrechenButton.setImage(SWTResourceManager.getImage(TransportForm.class, "/image/LAN Warning.ico"));
		abbrechenButton.setText("Abbrechen");
		abbrechenButton.addListener(SWT.Selection, exitListener);
		
		
		okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.bottom = new FormAttachment(0, 501);
		fd_okButton.top = new FormAttachment(0, 478);
		fd_okButton.right = new FormAttachment(0, 954);
		fd_okButton.left = new FormAttachment(0, 858);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");
		//TODO start
		okButton.addListener(SWT.Selection, new Listener()
		{
			
			String requiredFields;//contains the names of the required fields that have no content
//			
			int hourStart;
			int hourAtPatient;
			int hourTerm;
			int minutesStart;
			int minutesAtPatient;
			int minutesTerm;
			
			//fields
			String paramedicII;
			String paramedicI;
			String driver;
			
			boolean mountainRescue;
			boolean police;
			boolean fireBrigade;
			boolean brkdt;
			boolean df;
			boolean rth;
			boolean emergencyDoctor;
			boolean blueLight;
			
			String feedback;
			String notes;
			String priority;
			String kindOfIllness;
			
			boolean longDistanceTrip;
			
			boolean toMariazell;
			boolean toVienna;
			boolean toLeoben;
			boolean toGraz;
			boolean toDistrict;
			
			String term;
			String atPatient;
			String start;
			
			String station;
			
			String numberNotifier;
			String notifierName;
			
			boolean backTransportPossible;
			boolean accompanyingPerson;
			boolean rufhilfepatient;
			
			
			boolean wheelChairButton;
			boolean gurney;
			boolean chair;
			boolean moving;
			
			String toCommunity;
			String toStreet;
			String firstName;
			String lastName;
			String fromCommunity;
			String fromStreet;
			
			String vehicle;
			String theStation;
			String transportNumber;
			long transportDate;
			
			long termLong;
			long atPatientLong;
			long startLong;
			
			int directness;
			
			String formatOfTime;
			
			Calendar cal = Calendar.getInstance();
			
			
			public void handleEvent(Event event) 
			{
				String kindOfTransport;
				requiredFields = "";
				hourStart = -1;
				hourAtPatient = -1;
				minutesStart = -1;
				minutesAtPatient = -1;
				hourTerm = -1;
				minutesTerm = -1;
				
				
				formatOfTime = "";
				
				//get content of all fields
				this.getContentOfAllFields();
				this.setDirectness();
				
				//check required Fields
				if (!this.checkRequiredFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, requiredFields, "Bitte noch folgende Mussfelder ausfüllen:");
					return;
				}
				
				//validating
				if(!this.checkFormatOfTimeFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event,formatOfTime, "Format von Transportzeiten falsch: ");	
					return;
				}
				
				this.transformToLong();//set planned work time
				//validate: start before atPatient
				if(atPatientLong<startLong && !start.equalsIgnoreCase("") && !atPatient.equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, "Ankunft bei Patient kann nicht vor Abfahrtszeit des Fahrzeuges liegen", "Fehler (Zeit)");
					return;
				}	
				
				
				//validate: atPatient before term
				if((termLong<atPatientLong && !term.equalsIgnoreCase("") && !atPatient.equalsIgnoreCase("")))
				{
					this.displayMessageBox(event, "Termin kann nicht vor Ankunft bei Patient sein", "Fehler (Zeit)");
					return;
				}
				
				//validate: start before term
				if(termLong<startLong && !term.equalsIgnoreCase("") && !start.equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, "Termin kann nicht vor Abfahrtszeit des Fahrzeuges liegen", "Fehler (Zeit)");
					return;
				}
				
				
				//set the kind of transport
				
				if(wheelChairButton)
					kindOfTransport = TRANSPORT_KIND_ROLLSTUHL;
				else if(gurney)
					kindOfTransport = TRANSPORT_KIND_KRANKENTRAGE;
				else if(chair)
					kindOfTransport = TRANSPORT_KIND_TRAGSESSEL;
				else if(moving)
					kindOfTransport = TRANSPORT_KIND_GEHEND;
				else
					kindOfTransport = "";
				
				//set the receiving time
				Calendar cal = Calendar.getInstance();
				long receivingTime = cal.getTimeInMillis();
				
				//create a new entry
				
                if(createNew)
                {
                	transport = new Transport(fromStreet,fromCommunity,theStation,transportDate,startLong,priority,directness);
                	transport.setBackTransport(backTransportPossible);
                	//TODO set the other values
                	Patient patient = new Patient(lastName,firstName);
                	transport.setPatient(patient);
                	transport.setAccompanyingPerson(accompanyingPerson);
                	transport.setAppointmentTimeAtDestination(termLong);
                	transport.setBlueLightToGoal(blueLight);
                	transport.setBrkdtAlarming(brkdt);
                	CallerDetail callerDetail = new CallerDetail(notifierName,numberNotifier);
                	transport.setCallerDetail(callerDetail);
                	transport.setDateOfTransport(transportDate);
                	transport.setDfAlarming(df);
                	transport.setDiseaseNotes(notes);
                	transport.setEmergencyDoctorAlarming(emergencyDoctor);
                	transport.setEmergencyPhone(rufhilfepatient);
                	transport.setFeedback(feedback);
                	transport.setFirebrigadeAlarming(fireBrigade);
                	transport.setHelicopterAlarming(rth);
                	transport.setKindOfIllness(kindOfIllness);
                	transport.setKindOfTransport(kindOfTransport);
                	transport.setLongDistanceTrip(longDistanceTrip);
                	transport.setMountainRescueServiceAlarming(mountainRescue);
                	transport.setPlannedTimeAtPatient(atPatientLong);
                	transport.setPoliceAlarming(police);
                	transport.setReceiveTime(receivingTime);
                	transport.setToStreet(toStreet);
                	transport.setToCity(toCommunity);
                	
                	//TODO setRealStation, Transportnumber, VehicleDetail wann?
                	
              
                	
                    //create and run the add action
                    CreateTransportAction newAction = new CreateTransportAction(transport);
                    newAction.run();//TODO
                }
                else
                {
                    // set the needed values
                	transport.setTransportNumber(transportNumber);
                	//TOD set the values
                    
                    //create and run the update action
                    UpdateTransportAction updateAction = new UpdateTransportAction(transport);
                    updateAction.run();
                }
           
                shell.close();
				System.out.println("Transport angelegt!");
				
			}
			
			private void getContentOfAllFields()
			{		
				paramedicII = textSaniII.getText();
				paramedicI = textSaniI.getText();
				driver = textFahrer.getText();
				mountainRescue = bergrettungButton.getSelection();
				police = polizeiButton.getSelection();
				fireBrigade = feuerwehrButton.getSelection();
				brkdt = brkdtButton.getSelection();
				df = dfButton.getSelection();
				rth = rthButton.getSelection();
				emergencyDoctor = notarztButton.getSelection();
				blueLight = bd2Button.getSelection();
				feedback = textRueckmeldung.getText();
				notes = textAnmerkungen.getText();
				priority = comboPrioritaet.getText();
				kindOfIllness = comboErkrankungVerletzung.getText();
				longDistanceTrip = fernfahrtButton.getSelection();
				toMariazell = mariazellButton.getSelection();
				toVienna = wienButton.getSelection();
				toLeoben = leobenButton.getSelection();
				toGraz = grazButton.getSelection();
				toDistrict = bezirkButton.getSelection();
				
				term = textTermin.getText();
				atPatient = textBeiPat.getText();
				start = textAbf.getText();
				
				theStation = comboZustaendigeOrtsstelle.getText();
				numberNotifier = textTelefonAnrufer.getText();
				notifierName = textAnrufer.getText();
				backTransportPossible = ruecktransportMoeglichButton.getSelection();
				accompanyingPerson = begleitpersonButton.getSelection();
				rufhilfepatient = rufhilfepatientButton.getSelection();
				wheelChairButton = eigenerRollstuhlButton.getSelection();
				gurney = krankentrageButton.getSelection();
				chair = tragsesselButton.getSelection();
				moving = gehendButton.getSelection();
				
				toCommunity = comboNachOrt.getText();
				toStreet = comboNachStrasse.getText();
				firstName = comboVorname.getText();
				lastName = comboNachname.getText();
				fromCommunity = comboVonOrt.getText();
				fromStreet = comboVonStrasse.getText();
				
				vehicle = textFahrzeug.getText();
				station = textOrtsstelle.getText();
				transportNumber = textTransportNummer.getText();
				
			}
			
			private void setDirectness()
			{
				if (toMariazell)
					directness = TOWARDS_MARIAZELL;
				else if (toVienna)
					directness = TOWARDS_VIENNA;
				else if (toLeoben)
					directness = TOWARDS_LEOBEN;
				else if (toGraz)
					directness = TOWARDS_GRAZ;
				else
					directness = TOWARDS_DISTRICT;
			}
			private String checkRequiredFields()
			{
				//TODO
				/*TRANSPORT - the required fields
				- fromStreet ist muss. Wenn da nicht die Substrings "LKH" oder "PH" vorkommen, dann müssen zusätzlich auch die Felder
				- from City ausgefüllt sein.
				*/

				if (fromStreet.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"von Straße";
				if (fromCommunity.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"von Ort";
				if (theStation.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"zuständige Ortsstelle";
				if (priority.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Priorität";
				if (start.equalsIgnoreCase(""))
					this.setStartTimeDefault();
				return requiredFields;
			}
			
			
			private void setStartTimeDefault()
			{
				Date time = new Date();
				startLong = time.getTime();
			}
			private String checkFormatOfTimeFields()
			{
				Pattern p4 = Pattern.compile("(\\d{2})(\\d{2})");//if content is e.g. 1234
				Pattern p5 = Pattern.compile("(\\d{2}):(\\d{2})");//if content is e.g. 12:34
				
				//check in
				if(!start.equalsIgnoreCase(""))
				{
					Matcher m41= p4.matcher(start);
					Matcher m51= p5.matcher(start);
						if(m41.matches())
						{
							hourStart = Integer.parseInt(m41.group(1));
							minutesStart = Integer.parseInt(m41.group(2));
							
							if(hourStart >= 0 && hourStart <=23 && minutesStart >= 0 && minutesStart <=59)
							{
								start = hourStart + ":" +minutesStart;//for the splitter
							}
							else
							{
								formatOfTime = " - Abfahrtszeit";
							}
						}
						else if(m51.matches())
						{
								hourStart = Integer.parseInt(m51.group(1));
								minutesStart = Integer.parseInt(m51.group(2));
							
							if(!(hourStart >= 0 && hourStart <=23 && minutesStart >= 0 && minutesStart <=59))
							{
								formatOfTime = " - Abfahrtszeit";
							}
						}
						else
						{
							formatOfTime = " - Abfahrtszeit";
						}
				}
				
				//at patient
				if (!atPatient.equalsIgnoreCase(""))
				{
					Matcher m42= p4.matcher(atPatient);
					Matcher m52= p5.matcher(atPatient);
					if(m42.matches())
					{
						hourAtPatient = Integer.parseInt(m42.group(1));
						minutesAtPatient = Integer.parseInt(m42.group(2));
						
						if(hourAtPatient >= 0 && hourAtPatient <=23 && minutesAtPatient >= 0 && minutesAtPatient <=59)
						{
							atPatient = hourAtPatient +":" +minutesAtPatient;
						}
						else
						{
							formatOfTime = formatOfTime +"Ankunft bei Patient (Zeit)";
						}
					}
					else if(m52.matches())
					{
						hourAtPatient = Integer.parseInt(m52.group(1));
						minutesAtPatient = Integer.parseInt(m52.group(2));
						
						if(!(hourAtPatient >= 0 && hourAtPatient <=23 && minutesAtPatient >= 0 && minutesAtPatient <=59))
						{
							formatOfTime = formatOfTime +"Ankunft bei Patient (Zeit)";
						}
					}
					else
					{
						formatOfTime = formatOfTime +"Ankunft bei Patient (Zeit)";
					}
				}
				
				//term
				if (!term.equalsIgnoreCase(""))
				{
					Matcher m42= p4.matcher(term);
					Matcher m52= p5.matcher(term);
					if(m42.matches())
					{
						hourTerm = Integer.parseInt(m42.group(1));
						minutesTerm = Integer.parseInt(m42.group(2));
						
						if(hourTerm >= 0 && hourTerm <=23 && minutesTerm >= 0 && minutesTerm <=59)
						{
							term = hourTerm +":" +minutesTerm;
						}
						else
						{
							formatOfTime = formatOfTime +"Terminzeit";
						}
					}
					else if(m52.matches())
					{
						hourTerm = Integer.parseInt(m52.group(1));
						minutesTerm = Integer.parseInt(m52.group(2));
						
						if(!(hourTerm >= 0 && hourTerm <=23 && minutesTerm >= 0 && minutesTerm <=59))
						{
							formatOfTime = formatOfTime +"Terminzeit";
						}
					}
					else
					{
						formatOfTime = formatOfTime +"Terminzeit";
					}
				}
				return formatOfTime;
			}

			
			private void transformToLong()
			{
				//get a new instance of the calendar
				cal = Calendar.getInstance();
				
				//date of the transport
				int yearTransportDate = dateTime.getYear();
				int monthTransportDate = dateTime.getMonth();
				int dayTransportDate = dateTime.getDay();
				cal.set(yearTransportDate, monthTransportDate, dayTransportDate);
				transportDate = cal.getTimeInMillis();
				
				
				if (!term.equalsIgnoreCase(""))
				{
					String[] theTerm = term.split(":");
					
					int hoursTerm = Integer.valueOf(theTerm[0]).intValue();
					int minutesTerm = Integer.valueOf(theTerm[1]).intValue();
					cal.set(yearTransportDate, monthTransportDate, dayTransportDate,hoursTerm,minutesTerm);
					termLong = cal.getTimeInMillis();
				}
				
				if (!atPatient.equalsIgnoreCase(""))
				{
					String[] theTimeAtPatient = atPatient.split(":");
					int hourstheTimeAtPatient = Integer.valueOf(theTimeAtPatient[0]).intValue();
					int minutestheTimeAtPatient = Integer.valueOf(theTimeAtPatient[1]).intValue();
					cal.set(yearTransportDate, monthTransportDate, dayTransportDate,hourstheTimeAtPatient,minutestheTimeAtPatient);
					atPatientLong = cal.getTimeInMillis();
				}
				
				if (!start.equalsIgnoreCase(""))
				{
					String[] theStartTime = start.split(":");
					int hourstheStartTime = Integer.valueOf(theStartTime[0]).intValue();
					int minutestheStartTime = Integer.valueOf(theStartTime[1]).intValue();
					cal.set(yearTransportDate, monthTransportDate, dayTransportDate,hourstheStartTime,minutestheStartTime);
					startLong = cal.getTimeInMillis();
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
		
		
		
		//TODO - end
		
		//transport type selction buttons //TODO
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
		buttonNotfall.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(final SelectionEvent e) 
			{
				planungGroup.setVisible(false);
				planungGroup_1.setVisible(true);
				transportdetailsGroup.setVisible(false);
				statusmeldungenGroup.setVisible(false);
				personalAmFahrzeugGroup.setVisible(false);
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
		buttonVormerkung.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(final SelectionEvent e) 
			{
				planungGroup_1.setVisible(false);
				planungGroup.setVisible(true);
				transportdetailsGroup.setVisible(false);
				statusmeldungenGroup.setVisible(false);
				personalAmFahrzeugGroup.setVisible(false);
			}
			
		});
		
		
		

		buttonAlles = new Button(group, SWT.TOGGLE);
		final FormData fd_buttonAlles = new FormData();
		fd_buttonAlles.bottom = new FormAttachment(0, 78);
		fd_buttonAlles.top = new FormAttachment(0, 55);
		fd_buttonAlles.right = new FormAttachment(0, 202);
		fd_buttonAlles.left = new FormAttachment(0, 94);
		buttonAlles.setLayoutData(fd_buttonAlles);
		buttonAlles.setToolTipText("Blendet alle Felder ein");
		buttonAlles.setText("Alle Felder anzeigen");
		buttonAlles.addSelectionListener(new SelectionAdapter() 
		{
			public void widgetSelected(final SelectionEvent e) 
			{
				planungGroup_1.setVisible(true);
				planungGroup.setVisible(true);
				transportdetailsGroup.setVisible(true);
				statusmeldungenGroup.setVisible(true);
				personalAmFahrzeugGroup.setVisible(true);
			}
		});

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
	

    
	
	//METHODS
	

}
