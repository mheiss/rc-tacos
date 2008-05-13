package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.modelManager.AddressManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.DialysisPatient;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;

/**
 * GUI (form) to manage the details of a dialysis patient
 * @author b.thek
*/
public class DialysisForm implements IKindOfTransport, PropertyChangeListener
{
	//The managed streets
    private AddressManager addressManager = ModelFactory.getInstance().getAddressManager();
    
    private ComboViewer viewerFromStreet,viewerToStreet,viewerFromCity,viewerToCity;
    
	private Text textFertig;
	private Label abfLabel_1;
	private Button abbrechenButton;
	private Button okButton;
	private Group transportdatenGroup;
	private Group patientenzustandGroup;
	private Group planungGroup;
	private Button begleitpersonButton;
	private Button button_stationary;
	private Button button;
	private Combo comboNachOrt;
	private Combo comboNachStrasse;
	private Combo comboVorname;
	private Combo comboNachname;
	private Combo comboVonOrt;
	private Combo comboVonStrasse;
	private Button sonntagButton;
	private Button samstagButton;
	private Button freitagButton;
	private Button donnerstagButton;
	private Button mittwochButton;
	private Button dienstagButton;
	private Button montagButton;
	private Text textAbfRT;
	private Text textTermin;
	private Text textBeiPat;
	private Text textAbf;
	protected Shell shell;
	private Combo combokindOfTransport;
	
	private Listener exitListener;
	private ComboViewer zustaendigeOrtsstelle;
	
	private boolean createNew;
	
	private DialysisPatient dia;

	 /**
     * constructor used to create a a new dialysis transport entry.
     */
	public DialysisForm()
	{
		createNew = true;
		this.dia = new DialysisPatient();
		createContents();
	}
	
	 /**
     * Open the window
     */
	public void open()
	{
		//get the active shell
		Shell parent = PlatformUI.getWorkbench().getDisplay().getActiveShell();

		//get the shell and resize
		shell.setSize(1083, 223);

		//calculate and draw centered
		Rectangle workbenchSize = parent.getBounds();
		Rectangle mySize = shell.getBounds();
		int locationX, locationY;
		locationX = (workbenchSize.width - mySize.width)/2+workbenchSize.x;
		locationY = (workbenchSize.height - mySize.height)/2+workbenchSize.y;
		shell.setLocation(locationX,locationY);
		shell.open();
	}
	
	/**
     * used to edit an dialysis entry
     * @param dialysisPatient the dialysisPatient to edit
     */
	public DialysisForm(DialysisPatient patient, boolean createNew)
	{
		this.createNew = createNew;
		this.dia = patient;
		
		createContents();
		
		 GregorianCalendar gcal = new GregorianCalendar();
		
		//planned start of transport
        if(dia.getPlannedStartOfTransport() != 0)
        {
        	gcal.setTimeInMillis(dia.getPlannedStartOfTransport());
        	String abfahrtTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textAbf.setText(abfahrtTime);
        }
        
        //time at patient
        if (dia.getPlannedTimeAtPatient() != 0)
        {
        	gcal.setTimeInMillis(dia.getPlannedTimeAtPatient());
        	String beiPatientTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textBeiPat.setText(beiPatientTime);
        }
        
        //time at destination
        if (dia.getAppointmentTimeAtDialysis() != 0)
        {
        	gcal.setTimeInMillis(dia.getAppointmentTimeAtDialysis());
        	String terminTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textTermin.setText(terminTime);
        }
        
        //time abfRT
        if (dia.getPlannedStartForBackTransport() != 0)
        {
        	gcal.setTimeInMillis(dia.getPlannedStartForBackTransport());
        	String abfRTTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textAbfRT.setText(abfRTTime);
        }
        
        //time ready
        if (dia.getReadyTime() != 0)
        {
        	gcal.setTimeInMillis(dia.getReadyTime());
        	String readyTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <=9 ? "0" : "") +gcal.get(GregorianCalendar.HOUR_OF_DAY)+":" +((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") +gcal.get(GregorianCalendar.MINUTE));
        	this.textFertig.setText(readyTime);
        }
		
        viewerFromStreet.getCombo().setText(dia.getFromStreet());
        if(dia.getFromCity() != null)
            viewerFromCity.getCombo().setText(dia.getFromCity());
        if(dia.getToCity() != null)
            viewerToCity.getCombo().setText(dia.getToCity());

        if(dia.getToStreet() != null)
            viewerToStreet.getCombo().setText(dia.getToStreet());
        
        if(dia.getPatient().getLastname() != null)
        	this.comboNachname.setText(dia.getPatient().getLastname());
        
        if(dia.getPatient().getFirstname() != null)
        	this.comboVorname.setText(dia.getPatient().getFirstname());
        
        this.begleitpersonButton.setSelection(dia.isAssistantPerson());
        
        if(dia.getLocation() != null)
            this.zustaendigeOrtsstelle.setSelection(new StructuredSelection(dia.getLocation()));//mandatory!! default: Bezirk

        
        this.montagButton.setSelection(dia.isMonday());
        this.dienstagButton.setSelection(dia.isTuesday());
        this.mittwochButton.setSelection(dia.isWednesday());
        this.donnerstagButton.setSelection(dia.isThursday());
        this.freitagButton.setSelection(dia.isFriday());
        this.samstagButton.setSelection(dia.isSaturday());
        this.sonntagButton.setSelection(dia.isSunday());
        
        this.button_stationary.setSelection(dia.isStationary());
        

        //kind of transport
        if(dia.getKindOfTransport() != null)
        	combokindOfTransport.setText(dia.getKindOfTransport());
		
	}

	/**
	 * Create contents of the window
	 */
	protected void createContents() {
		shell = new Shell(Display.getCurrent(),SWT.APPLICATION_MODAL | SWT.TITLE | SWT.BORDER | SWT.CLOSE);
		shell.setLayout(new FormLayout());
		shell.setImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
		shell.setText("Dialysetransport");

		transportdatenGroup = new Group(shell, SWT.NONE);
		final FormData fd_transportdatenGroup = new FormData();
		fd_transportdatenGroup.right = new FormAttachment(0, 1067);
		fd_transportdatenGroup.left = new FormAttachment(0, 205);
		transportdatenGroup.setLayoutData(fd_transportdatenGroup);
		transportdatenGroup.setForeground(Util.getColor(128, 128, 128));
		transportdatenGroup.setText("Transportdaten");

		final Label vonLabel = new Label(transportdatenGroup, SWT.NONE);
		vonLabel.setForeground(Util.getColor(128, 128, 128));
		vonLabel.setText("von:");
		vonLabel.setBounds(10, 42, 25, 13);

		comboNachStrasse = new Combo(transportdatenGroup, SWT.NONE);
		comboNachStrasse.setBounds(41, 66, 230, 21);
		viewerToStreet = new ComboViewer(comboNachStrasse);
        viewerToStreet.setContentProvider(new IStructuredContentProvider()
        {
            @Override
            public Object[] getElements(Object arg0)
            {
                return addressManager.toStreetArray();
            }

            @Override
            public void dispose() { }

            @Override
            public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
        });
        viewerToStreet.setInput(addressManager.toStreetArray());

		comboVonStrasse = new Combo(transportdatenGroup, SWT.NONE);
		comboVonStrasse.setBounds(41, 39, 230, 21);
		viewerFromStreet = new ComboViewer(comboVonStrasse);
        viewerFromStreet.setContentProvider(new IStructuredContentProvider()
        {
            @Override
            public Object[] getElements(Object arg0)
            {
                return addressManager.toStreetArray();
            }

            @Override
            public void dispose() { }

            @Override
            public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
        });
        viewerFromStreet.setInput(addressManager.toStreetArray());

		final Label nachLabel = new Label(transportdatenGroup, SWT.NONE);
		nachLabel.setForeground(Util.getColor(128, 128, 128));
		nachLabel.setText("nach:");
		nachLabel.setBounds(10, 69, 25, 13);

		final Label label = new Label(transportdatenGroup, SWT.NONE);
		label.setForeground(Util.getColor(128, 128, 128));
		label.setText("Stra�e");
		label.setBounds(41, 20, 56, 13);

		comboVonOrt = new Combo(transportdatenGroup, SWT.NONE);
		comboVonOrt.setBounds(277, 39, 156, 21);
		viewerFromCity = new ComboViewer(comboVonOrt);
        viewerFromCity.setContentProvider(new IStructuredContentProvider()
        {
            @Override
            public Object[] getElements(Object arg0)
            {
                return addressManager.toCityArray();
            }

            @Override
            public void dispose() { }

            @Override
            public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
        });
        viewerFromCity.setInput(addressManager.toCityArray());

		comboNachOrt = new Combo(transportdatenGroup, SWT.NONE);
		comboNachOrt.setBounds(277, 66, 156, 21);
		viewerToCity = new ComboViewer(comboNachOrt);
        viewerToCity.setContentProvider(new IStructuredContentProvider()
        {
            @Override
            public Object[] getElements(Object arg0)
            {
                return addressManager.toCityArray();
            }

            @Override
            public void dispose() { }

            @Override
            public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
        });
        viewerToCity.setInput(addressManager.toCityArray());

		final Label ortLabel = new Label(transportdatenGroup, SWT.NONE);
		ortLabel.setForeground(Util.getColor(128, 128, 128));
		ortLabel.setText("Ort");
		ortLabel.setBounds(322, 20, 25, 13);

		comboNachname = new Combo(transportdatenGroup, SWT.NONE);
		comboNachname.setBounds(467, 39, 171, 21);

		final Label nachnameLabel = new Label(transportdatenGroup, SWT.NONE);
		nachnameLabel.setForeground(Util.getColor(128, 128, 128));
		nachnameLabel.setText("Nachname");
		nachnameLabel.setBounds(467, 20, 56, 13);

		comboVorname = new Combo(transportdatenGroup, SWT.NONE);
		comboVorname.setBounds(644, 39, 171, 21);

		button = new Button(transportdatenGroup, SWT.NONE);
		button.setBounds(821, 37,32, 23);
		button.setText("...");

		final Label nachnameLabel_1 = new Label(transportdatenGroup, SWT.NONE);
		nachnameLabel_1.setBounds(644, 20, 56, 13);
		nachnameLabel_1.setForeground(Util.getColor(128, 128, 128));
		nachnameLabel_1.setText("Vorname");

		final Label label_kind = new Label(transportdatenGroup, SWT.NONE);
		label_kind.setBounds(680, 72, 70, 13);
		label_kind.setForeground(Util.getColor(128, 128, 128));
        label_kind.setText("Transportart:");
        combokindOfTransport = new Combo(transportdatenGroup, SWT.READ_ONLY);
        //set possible priorities
        String[] kindsOfTransport = {TRANSPORT_KIND_GEHEND, TRANSPORT_KIND_TRAGSESSEL, TRANSPORT_KIND_KRANKENTRAGE, TRANSPORT_KIND_ROLLSTUHL};
        combokindOfTransport.setItems(kindsOfTransport);
        combokindOfTransport.setBounds(753, 69, 100, 23);
        combokindOfTransport.setForeground(Util.getColor(128, 128, 128));

		begleitpersonButton = new Button(transportdatenGroup, SWT.CHECK);
		begleitpersonButton.setText("Begleitperson");
		begleitpersonButton.setBounds(465, 113, 85, 16);

		final Label label_6 = new Label(transportdatenGroup, SWT.NONE);
		label_6.setForeground(Util.getColor(128, 128, 128));
		label_6.setText("Zust�ndige Ortsstelle:");
		label_6.setBounds(205, 118, 111, 13);

		 Combo comboZustaendigeOrtsstelle = new Combo(transportdatenGroup, SWT.READ_ONLY);
	        zustaendigeOrtsstelle = new ComboViewer(comboZustaendigeOrtsstelle);
	        zustaendigeOrtsstelle.setContentProvider(new StationContentProvider());
	        zustaendigeOrtsstelle.setLabelProvider(new StationLabelProvider());
	        zustaendigeOrtsstelle.setInput(ModelFactory.getInstance().getLocationManager());
	        comboZustaendigeOrtsstelle.setBounds(322, 113, 112, 21);

		button_stationary = new Button(transportdatenGroup, SWT.CHECK);
		button_stationary.setText("station�r");
		button_stationary.setBounds(41, 118, 85, 16);

		planungGroup = new Group(shell, SWT.NONE);
		final FormData fd_planungGroup = new FormData();
		fd_planungGroup.bottom = new FormAttachment(transportdatenGroup, 182, SWT.TOP);
		fd_planungGroup.top = new FormAttachment(transportdatenGroup, 0, SWT.TOP);
		fd_planungGroup.right = new FormAttachment(0, 100);
		fd_planungGroup.left = new FormAttachment(0, 10);
		planungGroup.setLayoutData(fd_planungGroup);
		planungGroup.setText("Zeiten");

		final Label abfLabel = new Label(planungGroup, SWT.NONE);
		abfLabel.setForeground(Util.getColor(128, 128, 128));
		abfLabel.setText("Abf:");
		abfLabel.setBounds(10, 37, 25, 13);

		final Label beiPatLabel = new Label(planungGroup, SWT.NONE);
		beiPatLabel.setForeground(Util.getColor(128, 128, 128));
		beiPatLabel.setText("Pat.:");
		beiPatLabel.setBounds(10, 64, 25, 13);

		final Label terminLabel = new Label(planungGroup, SWT.NONE);
		terminLabel.setForeground(Util.getColor(128, 128, 128));
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
		terminLabel_1.setForeground(Util.getColor(128, 128, 128));
		terminLabel_1.setText("Abf.:");

		textAbfRT = new Text(planungGroup, SWT.BORDER);
		textAbfRT.setBounds(41, 115, 41, 21);

		abfLabel_1 = new Label(planungGroup, SWT.NONE);
		abfLabel_1.setForeground(Util.getColor(128, 128, 128));
		abfLabel_1.setText("fertig");
		abfLabel_1.setBounds(10, 145, 25, 13);

		textFertig = new Text(planungGroup, SWT.BORDER);
		textFertig.setBounds(41, 142, 41, 21);
		planungGroup.setTabList(new Control[] {textAbf, textBeiPat, textTermin, textAbfRT, textFertig});

		patientenzustandGroup = new Group(shell, SWT.NONE);
		fd_transportdatenGroup.bottom = new FormAttachment(patientenzustandGroup, 150, SWT.TOP);
		fd_transportdatenGroup.top = new FormAttachment(patientenzustandGroup, 0, SWT.TOP);
		transportdatenGroup.setTabList(new Control[] {
				comboVonStrasse, comboVonOrt, comboNachname, comboVorname, combokindOfTransport, 
        		comboNachStrasse, comboNachOrt, begleitpersonButton, 
        		button_stationary});
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
//		abbrechenButton.setImage(ImageFactory.getInstance().getRegisteredImage("icon.stop"));
		abbrechenButton.setText("Abbrechen");
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
		
		abbrechenButton.addListener(SWT.Selection, exitListener);
		
		

		okButton = new Button(shell, SWT.NONE);
		final FormData fd_okButton = new FormData();
		fd_okButton.top = new FormAttachment(abbrechenButton, -23, SWT.BOTTOM);
		fd_okButton.bottom = new FormAttachment(abbrechenButton, 0, SWT.BOTTOM);
		fd_okButton.left = new FormAttachment(abbrechenButton, -101, SWT.LEFT);
		fd_okButton.right = new FormAttachment(abbrechenButton, -5, SWT.LEFT);
		okButton.setLayoutData(fd_okButton);
		okButton.setText("OK");
		okButton.addListener(SWT.Selection, new Listener()
		{	
			String requiredFields;
		
			int hourStart;
			int hourAtPatient;
			int hourTerm;
			int hourAbfRT;
			int hourReady;
			
			int minutesStart;
			int minutesAtPatient;
			int minutesTerm;
			int minutesAbfRT;
			int minutesReady;
			
			String term;
			String atPatient;
			String start;
			String abfRT;
			String ready;
			
			long termLong;
			long atPatientLong;
			long startLong;
			long abfRTLong;
			long readyLong;

			boolean montag;
			boolean dienstag;
			boolean mittwoch;
			boolean donnerstag;
			boolean freitag;
			boolean samstag;
			boolean sonntag;
			
			boolean assistant;
			
			boolean stationary;
			
			String toCommunity;
			String toStreet;
			
			String firstName;
			String lastName;
			
			String fromCommunity;
			String fromStreet;
				
			String formatOfTime;

			public void handleEvent(Event event) 
			{
				String kindOfTransport = "";
				
				requiredFields = "";
				
				hourStart = -1;
				hourAtPatient = -1;
				hourTerm = -1;
				hourAbfRT = -1;
				hourReady = -1;
				
				minutesStart = -1;
				minutesAtPatient = -1;
				minutesTerm = -1;
				minutesAbfRT = -1;
				minutesReady = -1;
				
				formatOfTime = "";
				
				this.getContentOfAllFields();
				
				   if (comboVorname.getText().length() >30)
			        {
			        	this.displayMessageBox(event, "Bitte geben Sie einen Vornamen, der k�rzer 30 Zeichen ist, ein", firstName);
			        	return;
			        }
				   
				   if (comboNachname.getText().length() >30)
			        {
			        	this.displayMessageBox(event, "Bitte geben Sie einen Nachname, der k�rzer 30 Zeichen ist, ein", lastName);
			        	return;
			        }
				   
				   if (viewerToCity.getCombo().getText().length() >30)
			        {
			        	this.displayMessageBox(event, "Bitte geben Sie einen Stadt (nach) ein, der k�rzer 30 Zeichen ist, ein", toCommunity);
			        	return;
			        }
				   if (viewerToStreet.getCombo().getText().length() >50)
			        {
			        	this.displayMessageBox(event, "Bitte geben Sie eine Stra�e (zu), der k�rzer 50 Zeichen ist, ein", toStreet);
			        	return;
			        }
				   
				   if (viewerFromCity.getCombo().getText().length() >30)
			        {
			        	this.displayMessageBox(event, "Bitte geben Sie einen Stadt (von) ein, der k�rzer 30 Zeichen ist, ein", fromCommunity);
			        	return;
			        }
				   if (viewerFromStreet.getCombo().getText().length() >50)
			        {
			        	this.displayMessageBox(event, "Bitte geben Sie eine Stra�e (von), der k�rzer 50 Zeichen ist, ein", fromStreet);
			        	return;
			        }
				  
				//check required fields
				if (!this.checkRequiredFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, requiredFields, "Bitte noch folgende Mussfelder ausf�llen:");
					return;
				}
				
				//validating
				if(!this.checkFormatOfTimeFields().equalsIgnoreCase(""))
				{
					this.displayMessageBox(event,formatOfTime, "Format von Transportzeiten falsch: ");	
					return;
				}
				
				this.transformToLong();
				
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
				
				//validate: abfRT before ready
				if(readyLong<abfRTLong && !abfRT.equalsIgnoreCase("") && !ready.equalsIgnoreCase(""))
				{
					this.displayMessageBox(event, "Abholzeit (fertig) kann nicht vor Abfahrtszeit liegen", "Fehler (Zeit)");
					return;
				}	
				
				
				
				//set the kind of transport
				 //the kind of transport
		        int index = combokindOfTransport.getSelectionIndex();
		        if (index != -1)
		        	kindOfTransport = combokindOfTransport.getItem(index);
		        
		     
		     
		       
				if(createNew)
				{
					dia = new DialysisPatient();
					dia.setAppointmentTimeAtDialysis(termLong);
					
					Patient patient = new Patient();
					patient.setFirstname(firstName);
					patient.setLastname(lastName);
					dia.setPatient(patient);
					
					dia.setFromCity(fromCommunity);
					dia.setFromStreet(fromStreet);
					dia.setInsurance("Versicherung unbekannt");
					if(kindOfTransport != null)
						dia.setKindOfTransport(kindOfTransport);
					
					index = zustaendigeOrtsstelle.getCombo().getSelectionIndex();
			        dia.setLocation((Location)zustaendigeOrtsstelle.getElementAt(index));

					
					dia.setPlannedStartForBackTransport(abfRTLong);
					dia.setPlannedStartOfTransport(startLong);
					dia.setPlannedTimeAtPatient(atPatientLong);
					dia.setReadyTime(readyLong);
					dia.setStationary(stationary);
					dia.setToCity(toCommunity);
					dia.setToStreet(toStreet);
					
					dia.setMonday(montag);
					dia.setTuesday(dienstag);
					dia.setWednesday(mittwoch);
					dia.setThursday(donnerstag);
					dia.setFriday(freitag);
					dia.setSaturday(samstag);
					dia.setSunday(sonntag);
					
					dia.setAssistantPerson(assistant);
					System.out.println("......... start: " +dia.getPlannedStartOfTransport());
					System.out.println("......... bei pat: " +dia.getPlannedTimeAtPatient());
					System.out.println("......... termin: " +dia.getAppointmentTimeAtDialysis());
					System.out.println("......... start rt: " +dia.getPlannedStartForBackTransport());
					System.out.println("......... ready: " +dia.getReadyTime());
					
					NetWrapper.getDefault().sendAddMessage(DialysisPatient.ID, dia);
				}
				else
				{
					dia.setAppointmentTimeAtDialysis(termLong);
					
					Patient patient = new Patient();
					patient.setFirstname(firstName);
					patient.setLastname(lastName);
					dia.setPatient(patient);
					dia.setFromCity(fromCommunity);
					dia.setFromStreet(fromStreet);
					if(kindOfTransport != null)
						dia.setKindOfTransport(kindOfTransport);
					dia.setPlannedStartForBackTransport(abfRTLong);
					dia.setPlannedStartOfTransport(startLong);
					dia.setPlannedTimeAtPatient(atPatientLong);
					dia.setReadyTime(readyLong);
					dia.setStationary(stationary);
					dia.setToCity(toCommunity);
					dia.setToStreet(toStreet);
					
					dia.setMonday(montag);
					dia.setTuesday(dienstag);
					dia.setWednesday(mittwoch);
					dia.setThursday(donnerstag);
					dia.setFriday(freitag);
					dia.setSaturday(samstag);
					dia.setSunday(sonntag);
					
					dia.setAssistantPerson(assistant);
					
					NetWrapper.getDefault().sendUpdateMessage(DialysisPatient.ID, dia);
				}
				shell.close();
			}
			
			private void getContentOfAllFields()
			{
				montag = montagButton.getSelection();
				dienstag = dienstagButton.getSelection();
				mittwoch = mittwochButton.getSelection();
				donnerstag = donnerstagButton.getSelection();
				freitag = freitagButton.getSelection();
				samstag = samstagButton.getSelection();
				sonntag = sonntagButton.getSelection();
				
				assistant = begleitpersonButton.getSelection();
				
				stationary = button_stationary.getSelection();
				
				term = textTermin.getText();
				atPatient = textBeiPat.getText();
				start = textAbf.getText();
				abfRT = textAbfRT.getText();
				ready = textFertig.getText();
				
				 
				
				toCommunity = viewerToCity.getCombo().getText();
				toStreet = viewerToStreet.getCombo().getText();
				firstName = comboVorname.getText();
				lastName = comboNachname.getText();
				fromCommunity = viewerFromCity.getCombo().getText();
				fromStreet = viewerFromStreet.getCombo().getText();
			}
			
			private String checkRequiredFields()
			{
				if (fromStreet.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"von Stra�e";
				if (fromCommunity.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"von Ort";
				if (toStreet.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Zielort";
				//the planned location
		        int index = zustaendigeOrtsstelle.getCombo().getSelectionIndex();
		        if(index == -1)
		        	requiredFields = requiredFields +"zust�ndige Ortsstelle";
		        
				
				if (start.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Abfahrtszeit";
				if (atPatient.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Zeit bei Patient";
				if (term.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Termin Dialyse";
				if (abfRT.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Abfahrt f�r R�cktransport";
				if (ready.equalsIgnoreCase(""))
					requiredFields = requiredFields +" " +"Abholzeit (fertig)";
				return requiredFields;
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
				
				
				//abf RT
				if (!abfRT.equalsIgnoreCase(""))
				{
					Matcher m42= p4.matcher(abfRT);
					Matcher m52= p5.matcher(abfRT);
					if(m42.matches())
					{
						hourAbfRT = Integer.parseInt(m42.group(1));
						minutesAbfRT = Integer.parseInt(m42.group(2));
						
						if(hourAbfRT >= 0 && hourAbfRT <=23 && minutesAbfRT >= 0 && minutesAbfRT <=59)
						{
							abfRT = hourAbfRT +":" +minutesAbfRT;
						}
						else
						{
							formatOfTime = formatOfTime +"Abfahrt R�cktransport";
						}
					}
					else if(m52.matches())
					{
						hourAbfRT = Integer.parseInt(m52.group(1));
						minutesAbfRT = Integer.parseInt(m52.group(2));
						
						if(!(hourAbfRT >= 0 && hourAbfRT <=23 && minutesAbfRT >= 0 && minutesAbfRT <=59))
						{
							formatOfTime = formatOfTime +"Abfahrt R�cktransport";
						}
					}
					else
					{
						formatOfTime = formatOfTime +"Abfahrt R�cktransport";
					}
				}
				
				//fertig
				if (!ready.equalsIgnoreCase(""))
				{
					Matcher m42= p4.matcher(ready);
					Matcher m52= p5.matcher(ready);
					if(m42.matches())
					{
						hourReady = Integer.parseInt(m42.group(1));
						minutesReady = Integer.parseInt(m42.group(2));
						
						if(hourReady >= 0 && hourReady <=23 && minutesReady >= 0 && minutesReady <=59)
						{
							ready = hourReady +":" +minutesReady;
						}
						else
						{
							formatOfTime = formatOfTime +"Ankunft bei Patient (Zeit)";
						}
					}
					else if(m52.matches())
					{
						hourReady = Integer.parseInt(m52.group(1));
						minutesReady = Integer.parseInt(m52.group(2));
						
						if(!(hourReady >= 0 && hourReady <=23 && minutesReady >= 0 && minutesReady <=59))
						{
							formatOfTime = formatOfTime +"Zeit fertig";
						}
					}
					else
					{
						formatOfTime = formatOfTime +"Zeit fertig";
					}
				}
				return formatOfTime;
			}
			
			
			private void transformToLong()
			{
				//get a new instance of the calendar
				GregorianCalendar cal = new GregorianCalendar();
				
				
				if (!term.equalsIgnoreCase(""))
				{
					String[] theTerm = term.split(":");
					
					int hoursTerm = Integer.valueOf(theTerm[0]).intValue();
					int minutesTerm = Integer.valueOf(theTerm[1]).intValue();
	
					cal.set(GregorianCalendar.HOUR_OF_DAY, hoursTerm);
					cal.set(GregorianCalendar.MINUTE,minutesTerm);
					
					termLong = cal.getTimeInMillis();
				}
				
				if (!atPatient.equalsIgnoreCase(""))
				{
					String[] theTimeAtPatient = atPatient.split(":");
					int hourstheTimeAtPatient = Integer.valueOf(theTimeAtPatient[0]).intValue();
					int minutestheTimeAtPatient = Integer.valueOf(theTimeAtPatient[1]).intValue();
					
					cal.set(GregorianCalendar.HOUR_OF_DAY, hourstheTimeAtPatient);
					cal.set(GregorianCalendar.MINUTE,minutestheTimeAtPatient);
					
					atPatientLong = cal.getTimeInMillis();
				}
				
				if (!start.equalsIgnoreCase(""))
				{
					String[] theStartTime = start.split(":");
					int hourstheStartTime = Integer.valueOf(theStartTime[0]).intValue();
					int minutestheStartTime = Integer.valueOf(theStartTime[1]).intValue();
					
					cal.set(GregorianCalendar.HOUR_OF_DAY, hourstheStartTime);
					cal.set(GregorianCalendar.MINUTE,minutestheStartTime);
					
					startLong = cal.getTimeInMillis();
				}
				
				if (!abfRT.equalsIgnoreCase(""))
				{
					String[] theAbfRTTime = abfRT.split(":");
					int hourstheAbfRTTime = Integer.valueOf(theAbfRTTime[0]).intValue();
					int minutestheAbfRTTime = Integer.valueOf(theAbfRTTime[1]).intValue();
					
					cal.set(GregorianCalendar.HOUR_OF_DAY, hourstheAbfRTTime);
					cal.set(GregorianCalendar.MINUTE,minutestheAbfRTTime);
					
					abfRTLong = cal.getTimeInMillis();
				}
				
				if (!ready.equalsIgnoreCase(""))
				{
					String[] theReadyTime = ready.split(":");
					int hourstheReadyTime = Integer.valueOf(theReadyTime[0]).intValue();
					int minutestheReadyTime = Integer.valueOf(theReadyTime[1]).intValue();
					
					cal.set(GregorianCalendar.HOUR_OF_DAY, hourstheReadyTime);
					cal.set(GregorianCalendar.MINUTE,minutestheReadyTime);
					
					readyLong = cal.getTimeInMillis();
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
		shell.setTabList(new Control[] {planungGroup, patientenzustandGroup, transportdatenGroup, okButton, abbrechenButton});

	}
	
	@Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        //update the view when a address has changed
        if ("ADDRESS_ADD".equals(evt.getPropertyName())
                || "ADDRESS_REMOVE".equalsIgnoreCase(evt.getPropertyName())
                || "ADDRESS_UPDATE".equalsIgnoreCase(evt.getPropertyName())
                || "ADDRESS_CLEARED".equalsIgnoreCase(evt.getPropertyName())
                || "ADDRESS_ADD_ALL".equalsIgnoreCase(evt.getPropertyName()))
        { 
            //update the address data
            viewerFromCity.refresh();
            viewerToCity.refresh();
            viewerToStreet.refresh();
            viewerFromStreet.refresh();
        }
    }
}
