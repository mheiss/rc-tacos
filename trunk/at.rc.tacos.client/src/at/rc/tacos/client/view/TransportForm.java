package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import at.rc.tacos.client.controller.CreateTransportAction;
import at.rc.tacos.client.controller.DuplicatePriorityATransportAction;
import at.rc.tacos.client.controller.UpdateTransportAction;
import at.rc.tacos.client.controller.VehicleUpdateAction;
import at.rc.tacos.client.modelManager.AddressManager;
import at.rc.tacos.client.modelManager.DiseaseManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.providers.DiseaseContentProvider;
import at.rc.tacos.client.providers.DiseaseLabelProvider;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.client.util.Util;
import at.rc.tacos.common.IDirectness;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.common.IProgramStatus;
import at.rc.tacos.common.ITransportStatus;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.CallerDetail;
import at.rc.tacos.model.Disease;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Patient;
import at.rc.tacos.model.StaffMember;
import at.rc.tacos.model.Transport;
import at.rc.tacos.model.VehicleDetail;

/**
 * GUI (form) to manage the transport details
 * one form for all kinds of transports (not necessary are blanked out)
 * @author b.thek
 *
 */
public class TransportForm extends TitleAreaDialog implements IDirectness, IKindOfTransport, ITransportStatus,IProgramStatus,PropertyChangeListener
{
    //The managed streets
    private AddressManager addressManager = ModelFactory.getInstance().getAddressList();
    private DiseaseManager diseaseManager = ModelFactory.getInstance().getDiseaseList();

    //properties
    private Group transportdetailsGroup;
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
    private Button bruckButton;
    private Button kapfenbergButton;
    private Text textTermin;
    private Text textBeiPat;
    private Text textAbf;
    private ComboViewer zustaendigeOrtsstelle;
    private Text textTelefonAnrufer;
    private Text textAnrufer;
    private Button ruecktransportMoeglichButton;
    private Button begleitpersonButton;
    private Button rufhilfepatientButton;
    private Button eigenerRollstuhlButton;
    private Button krankentrageButton;
    private Button tragsesselButton;
    private Combo comboVorname;
    private Combo comboNachname;
    private Text textFahrzeug;
    private Text textOrtsstelle;
    private Text textTransportNummer;
    private Group group;
    private Button buttonVormerkung;
    private Button buttonAlles;
    private Button buttonNotfall;
    private Group statusmeldungenGroup;
    private Group personalAmFahrzeugGroup;
    private Group planungGroup_1;
    private Group patientenzustandGroup;
    private Group planungGroup;
    private Group transportdatenGroup;
    private DateTime dateTime;
    private Button gehendButton;
    protected Shell shell;

    private Text textAufgen;
    private Text textAE;

    //the stati
    private Text textS1,textS2,textS3,textS4,textS5,textS6,textS7,textS8,textS9;

    private Transport transport;

    private ComboViewer viewerFromStreet,viewerToStreet,viewerFromCity,viewerToCity;
    private ComboViewer setTextFahrer,setTextSaniI,setTextSaniII;
    private ComboViewer setErkrVerl;

    private String[] prebookingPriorities = {"C", "D", "E", "F"};
    private String[] emergencyAndTransportPriorities = {"A", "B", "C", "D", "E", "F", "G"};
    //A: NEF + RTW
    //B: BD1
    //C: normaler Transport
    //D: Rücktransport
    //E: Heimtransport
    //F: Sonstiges
    //G: NEF für extern


    //determine whether to update or to create a new entry
    private boolean createNew;
    /**
     * possible editingTypes: journal (the "AllesButton" should be visible), prebooking, outstanding, underway
     */
    private String editingType;

    /**
     * transport type used to differ between a normal and an emergency transport
     * possible values: prebooking, emergencyTransport, ?wholeTransportDetails?- possible?
     */
    private String transportType;

    /**
     * Default class constructor used to create a new Transport.
     * @param parentShell the parent shell
     */
    public TransportForm(Shell parentShell)
    {
        super(parentShell);
        createNew = true;
        transport = new Transport();
        //bind the staff to this view
        ModelFactory.getInstance().getStaffList().addPropertyChangeListener(this);
        ModelFactory.getInstance().getAddressList().addPropertyChangeListener(this);
        ModelFactory.getInstance().getDiseaseList().addPropertyChangeListener(this);
    }

    /**
     * Default class constructor to create a new transport and set the layout.
     * @param parentShell the parent shell
     * @param transportType the type of the transport
     */
    public TransportForm(Shell parentShell,String transportType)
    {
        super(parentShell);
        createNew = true;
        this.transportType = transportType;
        this.transport = new Transport();
        //bind the staff to this view
        ModelFactory.getInstance().getStaffList().addPropertyChangeListener(this);
        ModelFactory.getInstance().getDiseaseList().addPropertyChangeListener(this);
        ModelFactory.getInstance().getAddressList().addPropertyChangeListener(this);
    }

    /**
     * Default class constructor to edit an existing transport
     * @param parentShell the parent shell
     * @param transport the transport to edit
     * @param editingType the layout of the form to show
     */
    public TransportForm(Shell parentShell,Transport transport, String editingType)
    {
        super(parentShell);
        //update an entry
        createNew = false;
        this.transport = transport;
        this.editingType = editingType;
        transportType = "both";
        //bind the staff to this view
        ModelFactory.getInstance().getStaffList().addPropertyChangeListener(this);
        ModelFactory.getInstance().getDiseaseList().addPropertyChangeListener(this);
        ModelFactory.getInstance().getAddressList().addPropertyChangeListener(this);
    }

    /**
     * Creates the dialog's contents
     * @param parent the parent composite
     * @return Control
     */
    protected Control createContents(Composite parent) 
    {
        Control contents = super.createContents(parent);
        setTitle("Transport");
        setMessage("Hier können Sie einen neuen Transport anlegen", IMessageProvider.INFORMATION);
        setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
        //force redraw
        getShell().pack(true);
        return contents;
    }

    /**
     * Create contents of the window
     */
    @Override
    protected Control createDialogArea(Composite parent)
    {
        //setup the composite
        Composite composite = (Composite) super.createDialogArea(parent);

        //Create the content of the dialog
        createTransportSection(composite);

        //init data
        if(!createNew)
        {
            //set field contents
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTimeZone(TimeZone.getDefault());
            //formatter for the date and time
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();

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

            //transport stati
            if(transport.getStatusMessages() != null)
            {
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
                    this.textAE.setText(sdf.format(cal.getTime()));
                }
                //Status 0 
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
                    textS1.setText(sdf.format(cal.getTime()));
                }
                //Status 2
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
                    textS2.setText(sdf.format(cal.getTime()));
                }       
                //Status 3
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
                    textS3.setText(sdf.format(cal.getTime()));
                }
                //Status 4 
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
                    textS4.setText(sdf.format(cal.getTime()));
                }
                //Status 5
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE));
                    textS5.setText(sdf.format(cal.getTime()));
                }
                //Status 6
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION));
                    textS6.setText(sdf.format(cal.getTime()));
                }
                //Status 7
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_OUT_OF_OPERATION_AREA));
                    textS7.setText(sdf.format(cal.getTime()));
                }
                //Status 8
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_BACK_IN_OPERATION_AREA));
                    textS8.setText(sdf.format(cal.getTime()));
                }
                //Status 9
                if(transport.getStatusMessages().containsKey(ITransportStatus.TRANSPORT_STATUS_OTHER))
                {
                    cal.setTimeInMillis(transport.getStatusMessages().get(ITransportStatus.TRANSPORT_STATUS_OTHER));
                    textS9.setText(sdf.format(cal.getTime()));
                } 
            }

            if(transport.getCreationTime() != 0)
            {
                cal.setTimeInMillis(transport.getCreationTime());
                textAufgen.setText(sdf.format(cal.getTime()));
            }

            //other fields
            this.begleitpersonButton.setSelection(transport.isAssistantPerson());
            this.bergrettungButton.setSelection(transport.isMountainRescueServiceAlarming());
            this.brkdtButton.setSelection(transport.isBrkdtAlarming());

            //the disease
            if(transport.getKindOfIllness() != null)
            {
                //the returned disease has no id so we query the managed diseases and try to get the complete object :)
                String name = transport.getKindOfIllness().getDiseaseName();
                Disease disease = diseaseManager.getDiseaseByName(name);
                if(disease != null)
                {
                    transport.setKindOfIllness(disease);
                    setErkrVerl.setSelection(new StructuredSelection(disease));
                }
            }

            if(transport.getPatient() != null)
            {
                this.comboNachname.setText(transport.getPatient().getLastname());
                this.comboVorname.setText(transport.getPatient().getFirstname());
            }

            if(transport.getToCity() != null)
                viewerToCity.getCombo().setText(transport.getToCity());

            if(transport.getToStreet() != null)
                viewerToStreet.getCombo().setText(transport.getToStreet());

            //mandatory fields
            if(transport.getTransportPriority() != null)
                comboPrioritaet.setText(transport.getTransportPriority());

            viewerFromStreet.getCombo().setText(transport.getFromStreet());

            if(transport.getPlanedLocation() != null)
                this.zustaendigeOrtsstelle.setSelection(new StructuredSelection(transport.getPlanedLocation()));//mandatory!! default: Bezirk

            if(transport.getFromCity() != null)
                viewerFromCity.getCombo().setText(transport.getFromCity());

            this.dfButton.setSelection(transport.isDfAlarming());
            this.fernfahrtButton.setSelection(transport.isLongDistanceTrip());
            this.feuerwehrButton.setSelection(transport.isFirebrigadeAlarming());
            this.notarztButton.setSelection(transport.isEmergencyDoctorAlarming());
            this.polizeiButton.setSelection(transport.isPoliceAlarming());
            this.rthButton.setSelection(transport.isHelicopterAlarming());
            this.ruecktransportMoeglichButton.setSelection(transport.isBackTransport());
            this.rufhilfepatientButton.setSelection(transport.isEmergencyPhone());
            this.bd2Button.setSelection(transport.isBlueLightToGoal());

            if(transport.getNotes() != null)
            {
                this.textAnmerkungen.setText(transport.getNotes());
            }

            if(transport.getCallerDetail() != null)
            {
                if(transport.getCallerDetail().getCallerName() != null)
                    this.textAnrufer.setText(transport.getCallerDetail().getCallerName());
                if(transport.getCallerDetail().getCallerTelephoneNumber() != null)
                    this.textTelefonAnrufer.setText(transport.getCallerDetail().getCallerTelephoneNumber());
            }

            //the real station which did the transport
            if(transport.getVehicleDetail() != null)
            {
            	
                this.textOrtsstelle.setText(transport.getVehicleDetail().getCurrentStation().getLocationName());
            }

            if(transport.getFeedback() != null)
                this.textRueckmeldung.setText(transport.getFeedback());

            if(transport.getTransportNumber() != 0)
            	if(transport.getTransportNumber() == -1)
            		this.textTransportNummer.setText("STORNO");
            	else if(transport.getTransportNumber() == -2)
            		this.textTransportNummer.setText("WTGL");
            	else if(transport.getTransportNumber() == -4)
            		this.textTransportNummer.setText("NEF");
            	else if(transport.getTransportNumber() != 0)
            		this.textTransportNummer.setText(String.valueOf(transport.getTransportNumber()));

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
            if (TOWARDS_BRUCK == direction)
            {
                this.bruckButton.setSelection(true);
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
            if (TOWARDS_KAPFENBERG == direction)
            {
                this.kapfenbergButton.setSelection(true);
            }

            if(transport.getVehicleDetail() != null)
            {
                if(transport.getVehicleDetail().getDriver() != null)
                {
                    this.setTextFahrer.setSelection(new StructuredSelection(transport.getVehicleDetail().getDriver()));
                }
                if(transport.getVehicleDetail().getFirstParamedic() != null)
                {
                    this.setTextSaniI.setSelection(new StructuredSelection(transport.getVehicleDetail().getFirstParamedic()));
                }
                if(transport.getVehicleDetail().getSecondParamedic() != null)
                {
                    this.setTextSaniII.setSelection(new StructuredSelection(transport.getVehicleDetail().getSecondParamedic()));
                }

                textFahrzeug.setText(transport.getVehicleDetail().getVehicleName());
            }
        }
        return composite;
    }

    /**
     * The user pressed the ok button
     */
    @Override
    protected void okPressed()
    {
        //reset the error messages
        setMessage("Hier können Sie einen neuen Transport anlegen");

        //the street
        if (viewerFromStreet.getCombo().getText().trim().isEmpty())
        {
            getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie die Straße ein, von der der Transport gestartet wird");
            return;
        }
        transport.setFromStreet(viewerFromStreet.getCombo().getText());

        //the city--> can be empty if the street is LKH or PH
        if (viewerFromCity.getCombo().getText().trim().isEmpty() &!
                (transport.getFromStreet().contains("LKH") || transport.getFromStreet().startsWith("LKH")
                        || transport.getFromStreet().contains("PH") || transport.getFromStreet().startsWith("PH") ))
        {
            getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie die Stadt ein, von der der Transport gestartet wird");
            return;
        }

        transport.setFromCity(viewerFromCity.getCombo().getText());

        transport.setToStreet(viewerToStreet.getCombo().getText());

        transport.setToCity(viewerFromCity.getCombo().getText());

        //the planned location
        int index = zustaendigeOrtsstelle.getCombo().getSelectionIndex();
        if (index == -1)
        {
            getShell().getDisplay().beep();
            setErrorMessage("Bitte geben sie die zuständige Ortsstelle ein");
            return;
        }
        transport.setPlanedLocation((Location)zustaendigeOrtsstelle.getElementAt(index));

        //the transport priority
        index = comboPrioritaet.getSelectionIndex();
        if (index == -1)
        {
            getShell().getDisplay().beep();
            setErrorMessage("Bitte geben sie die Priorität des Transports ein");
            return;
        }
        transport.setTransportPriority(comboPrioritaet.getItem(index));

        //convert the start time --> no validation when an emergency transport
        Calendar startTime = convertStringToDate(textAbf.getText());
        if(startTime == null &! transportType.equalsIgnoreCase("emergencyTransport") && createNew)
        {
            getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie eine gültige Abfahrtszeit in der Form HH:mm oder HHmm ein");
            return;
        }
        if(startTime != null)
        {
            startTime.set(Calendar.YEAR, dateTime.getYear());
            startTime.set(Calendar.MONTH, dateTime.getMonth());
            startTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());

            transport.setPlannedStartOfTransport(startTime.getTimeInMillis());
        }
        else
            transport.setPlannedStartOfTransport(0);

        //time at patient  --> no validation 
        Calendar patientTime = convertStringToDate(textBeiPat.getText());
        if(patientTime != null)
        {
            patientTime.set(Calendar.YEAR, dateTime.getYear());
            patientTime.set(Calendar.MONTH, dateTime.getMonth());
            patientTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
            transport.setPlannedTimeAtPatient(patientTime.getTimeInMillis());
        }
        else
            transport.setPlannedTimeAtPatient(0);

        //check the time  --> no validation 
        Calendar appointmentTime = convertStringToDate(textTermin.getText());
        if(appointmentTime != null)
        {
            appointmentTime.set(Calendar.YEAR, dateTime.getYear());
            appointmentTime.set(Calendar.MONTH, dateTime.getMonth());
            appointmentTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
            transport.setAppointmentTimeAtDestination(appointmentTime.getTimeInMillis());
        }
        else
            transport.setAppointmentTimeAtDestination(0);

        //validate: start before atPatient
        if(transport.getPlannedTimeAtPatient() < transport.getPlannedStartOfTransport() &!(transport.getPlannedTimeAtPatient()==0))
        {
            getShell().getDisplay().beep();
            setErrorMessage("Ankunft bei Patient kann nicht vor Abfahrtszeit des Fahrzeuges liegen.");
            return;
        }				

        //validate: atPatient before term
        if(transport.getAppointmentTimeAtDestination() < transport.getPlannedTimeAtPatient() &!(transport.getAppointmentTimeAtDestination()==0)&!(transport.getPlannedTimeAtPatient()==0))
        {
            getShell().getDisplay().beep();
            setErrorMessage("Termin kann nicht vor Ankunft bei Patient sein");
            return;
        }

        //validate: start before term
        if(transport.getAppointmentTimeAtDestination() < transport.getPlannedStartOfTransport() &!(transport.getAppointmentTimeAtDestination() ==0))
        {
            getShell().getDisplay().beep();
            setErrorMessage("Termin kann nicht vor Abfahrtszeit des Fahrzeuges liegen");
            return;
        }

        //kind of illness
        index = setErkrVerl.getCombo().getSelectionIndex();
        if(index != -1)
        {
            transport.setKindOfIllness((Disease)setErkrVerl.getElementAt(index));
        }

        //set the fields that do not have to be validated
        transport.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());
        transport.setBackTransport(ruecktransportMoeglichButton.getSelection());

        //the kind of transport
        if(eigenerRollstuhlButton.getSelection())
            transport.setKindOfTransport(TRANSPORT_KIND_ROLLSTUHL);
        else if(krankentrageButton.getSelection())
            transport.setKindOfTransport(TRANSPORT_KIND_KRANKENTRAGE);
        else if (tragsesselButton.getSelection())
            transport.setKindOfTransport(TRANSPORT_KIND_TRAGSESSEL);
        else if (gehendButton.getSelection())
            transport.setKindOfTransport(TRANSPORT_KIND_GEHEND);
        else
            transport.setKindOfTransport("<keine Angabe>");

        //if we have a patient just update it
        if(transport.getPatient() == null)
        {
            Patient patient = new Patient(comboVorname.getText(),comboNachname.getText());
            transport.setPatient(patient);
        }
        else
        {
            transport.getPatient().setFirstname(comboVorname.getText());
            transport.getPatient().setLastname(comboNachname.getText());
        }

        //if we have a caller, just update it
        if(transport.getCallerDetail() == null)
        {
            CallerDetail caller = new CallerDetail(textAnrufer.getText(),textTelefonAnrufer.getText());
            transport.setCallerDetail(caller);
        }
        else
        {
            transport.getCallerDetail().setCallerName(textAnrufer.getText());
            transport.getCallerDetail().setCallerTelephoneNumber(textTelefonAnrufer.getText());
        }

        //notes and feedback
        transport.setNotes(textAnmerkungen.getText());
        transport.setFeedback(textRueckmeldung.getText());

        //the destination
        transport.setToStreet(viewerToStreet.getCombo().getText());
        transport.setToCity(viewerToCity.getCombo().getText());

        //the boolean values
        transport.setAssistantPerson(begleitpersonButton.getSelection());
        transport.setBlueLightToGoal(bd2Button.getSelection());
        transport.setBrkdtAlarming(brkdtButton.getSelection());
        transport.setDfAlarming(dfButton.getSelection());
        transport.setEmergencyDoctorAlarming(notarztButton.getSelection());
        transport.setEmergencyPhone(rufhilfepatientButton.getSelection());
        transport.setFirebrigadeAlarming(feuerwehrButton.getSelection());
        transport.setHelicopterAlarming(rthButton.getSelection());
        transport.setLongDistanceTrip(fernfahrtButton.getSelection());
        transport.setMountainRescueServiceAlarming(bergrettungButton.getSelection());
        transport.setPoliceAlarming(polizeiButton.getSelection());

        //set the type of the transport
        if(transportType.equalsIgnoreCase("prebooking"))
            transport.setProgramStatus(PROGRAM_STATUS_PREBOOKING);
        if(transportType.equalsIgnoreCase("emergencyTransport"))
            transport.setProgramStatus(PROGRAM_STATUS_OUTSTANDING);

        if (mariazellButton.getSelection())
            transport.setDirection(TOWARDS_MARIAZELL);
        else if (wienButton.getSelection())
            transport.setDirection(TOWARDS_VIENNA);
        else if (leobenButton.getSelection())
            transport.setDirection(TOWARDS_LEOBEN);
        else if (grazButton.getSelection())
            transport.setDirection(TOWARDS_GRAZ);
        else if (kapfenbergButton.getSelection())
            transport.setDirection(TOWARDS_KAPFENBERG);
        else
            transport.setDirection(TOWARDS_BRUCK);
        
        //transport date 
        Calendar transportDate = Calendar.getInstance();
        transportDate.set(Calendar.YEAR, dateTime.getYear());
        transportDate.set(Calendar.MONTH, dateTime.getMonth());
        transportDate.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
        transport.setDateOfTransport(transportDate.getTimeInMillis());
        
        
//        int index1;
//        if(transport.getVehicleDetail() != null)
//        {
//        	index1 = setTextFahrer.getCombo().getSelectionIndex();
//			transport.getVehicleDetail().setDriver((StaffMember)setTextFahrer.getElementAt(index1));
//        }
		
//     TODO set content for paramedic 1 and paramedic 2

        if(createNew)
        {
            //created time
            transport.setCreationTime(Calendar.getInstance().getTimeInMillis());

            
            //create and run the add action
            CreateTransportAction newAction = new CreateTransportAction(transport);
            newAction.run();
            if(transport.getTransportPriority().equalsIgnoreCase("A"))
            {
                DuplicatePriorityATransportAction duplicateAction = new DuplicatePriorityATransportAction(transport);
                duplicateAction.run();
            }
        }
        else
        {
            //create and run the update action
//        	NetWrapper.getDefault().sendUpdateMessage(VehicleDetail.ID, transport.getVehicleDetail());
        	//VehicleUpdateAction updateVehAction = new VehicleUpdateAction(transport.getVehicleDetail());
           // updateVehAction.run();
            UpdateTransportAction updateAction = new UpdateTransportAction(transport);
            updateAction.run();
            
        }
        getShell().close();
    }

    /**
     * Cleanup and deregister the listener
     */
    @Override
    public boolean close()
    {

        ModelFactory.getInstance().getStaffList().removePropertyChangeListener(this);
        ModelFactory.getInstance().getAddressList().removePropertyChangeListener(this);
        ModelFactory.getInstance().getDiseaseList().removePropertyChangeListener(this);  
        super.close();
        return true;
    }

    /**
     * The user pressed the cancel button
     */
    @Override
    protected void cancelPressed()
    {
    	boolean result = MessageDialog.openConfirm(getShell(), "Abbrechen", "Wollen Sie wirklich abbrechen?"); 
        //check the result
    	if(result)
    	{
    		getShell().close();
    	}
    }

    /**
     * Creates the planing section
     */
    private void createTransportSection(Composite parent)
    {
        Composite client = new Composite(parent,SWT.NONE);
        client.setLayout(new FormLayout());
        //calendar
        dateTime = new DateTime(client, SWT.CALENDAR);
        final FormData fd_dateTime = new FormData();
        fd_dateTime.bottom = new FormAttachment(0, 160);
        fd_dateTime.top = new FormAttachment(0, 10);
        fd_dateTime.right = new FormAttachment(0, 187);
        fd_dateTime.left = new FormAttachment(0, 10);
        dateTime.setLayoutData(fd_dateTime);

        //group 'Transportdaten'
        transportdatenGroup = new Group(client, SWT.NONE);
        transportdatenGroup.setLayout(new FormLayout());
        final FormData fd_transportdatenGroup = new FormData();
        fd_transportdatenGroup.bottom = new FormAttachment(0, 160);
        fd_transportdatenGroup.top = new FormAttachment(0, 10);
        fd_transportdatenGroup.right = new FormAttachment(0, 1056);
        fd_transportdatenGroup.left = new FormAttachment(0, 194);
        transportdatenGroup.setLayoutData(fd_transportdatenGroup);
        transportdatenGroup.setForeground(Util.getColor(128, 128, 128));
        transportdatenGroup.setText("Transportdaten");

        final Label vonLabel = new Label(transportdatenGroup, SWT.NONE);
        vonLabel.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_vonLabel = new FormData();
        fd_vonLabel.bottom = new FormAttachment(0, 42);
        fd_vonLabel.top = new FormAttachment(0, 29);
        fd_vonLabel.right = new FormAttachment(0, 32);
        fd_vonLabel.left = new FormAttachment(0, 7);

        vonLabel.setLayoutData(fd_vonLabel);
        vonLabel.setForeground(Util.getColor(25, 25, 112));
        vonLabel.setText("von:");

        Combo comboNachStrasse = new Combo(transportdatenGroup, SWT.NONE);
        final FormData fd_comboNachStrasse = new FormData();
        fd_comboNachStrasse.right = new FormAttachment(0, 260);
        fd_comboNachStrasse.bottom = new FormAttachment(0, 74);
        fd_comboNachStrasse.top = new FormAttachment(0, 53);
        fd_comboNachStrasse.left = new FormAttachment(0, 38);
        comboNachStrasse.setLayoutData(fd_comboNachStrasse);
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

        Combo comboVonStrasse = new Combo(transportdatenGroup, SWT.NONE);
        final FormData fd_comboVonStrasse = new FormData();
        fd_comboVonStrasse.right = new FormAttachment(0, 260);
        fd_comboVonStrasse.bottom = new FormAttachment(0, 47);
        fd_comboVonStrasse.top = new FormAttachment(0, 26);
        fd_comboVonStrasse.left = new FormAttachment(0, 38);
        comboVonStrasse.setLayoutData(fd_comboVonStrasse);
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
        final FormData fd_nachLabel = new FormData();
        fd_nachLabel.bottom = new FormAttachment(0, 69);
        fd_nachLabel.top = new FormAttachment(0, 56);
        fd_nachLabel.right = new FormAttachment(0, 32);
        fd_nachLabel.left = new FormAttachment(0, 7);
        nachLabel.setLayoutData(fd_nachLabel);
        nachLabel.setForeground(Util.getColor(128, 128, 128));
        nachLabel.setText("nach:");

        final Label label = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_label = new FormData();
        fd_label.bottom = new FormAttachment(0, 20);
        fd_label.top = new FormAttachment(0, 7);
        fd_label.right = new FormAttachment(0, 94);
        fd_label.left = new FormAttachment(0, 38);
        label.setLayoutData(fd_label);
        label.setForeground(Util.getColor(128, 128, 128));
        label.setText("Straße");

        Combo comboVonOrt = new Combo(transportdatenGroup, SWT.NONE);
        final FormData fd_comboVonOrt = new FormData();
        fd_comboVonOrt.left = new FormAttachment(0, 274);
        fd_comboVonOrt.bottom = new FormAttachment(0, 47);
        fd_comboVonOrt.top = new FormAttachment(0, 26);
        fd_comboVonOrt.right = new FormAttachment(0, 430);
        comboVonOrt.setLayoutData(fd_comboVonOrt);
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

        Combo comboNachOrt = new Combo(transportdatenGroup, SWT.NONE);
        final FormData fd_comboNachOrt = new FormData();
        fd_comboNachOrt.left = new FormAttachment(0, 274);
        fd_comboNachOrt.bottom = new FormAttachment(0, 74);
        fd_comboNachOrt.top = new FormAttachment(0, 53);
        fd_comboNachOrt.right = new FormAttachment(0, 430);
        comboNachOrt.setLayoutData(fd_comboNachOrt);
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
        final FormData fd_ortLabel = new FormData();
        fd_ortLabel.bottom = new FormAttachment(0, 20);
        fd_ortLabel.top = new FormAttachment(0, 7);
        fd_ortLabel.right = new FormAttachment(0, 344);
        fd_ortLabel.left = new FormAttachment(0, 319);
        ortLabel.setLayoutData(fd_ortLabel);
        ortLabel.setForeground(Util.getColor(128, 128, 128));
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
        nachnameLabel.setForeground(Util.getColor(128, 128, 128));
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
        nachnameLabel_1.setForeground(Util.getColor(128, 128, 128));
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
        begleitpersonButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.assistantPerson"));

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
        anruferLabel.setForeground(Util.getColor(128, 128, 128));
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
        telefonLabel.setForeground(Util.getColor(128, 128, 128));
        telefonLabel.setText("Telefon:");

        textTelefonAnrufer = new Text(transportdatenGroup, SWT.BORDER);
        final FormData fd_textTelefonAnrufer = new FormData();
        fd_textTelefonAnrufer.bottom = new FormAttachment(0, 120);
        fd_textTelefonAnrufer.top = new FormAttachment(0, 99);
        fd_textTelefonAnrufer.right = new FormAttachment(0, 850);
        fd_textTelefonAnrufer.left = new FormAttachment(0, 663);
        textTelefonAnrufer.setLayoutData(fd_textTelefonAnrufer);

        final Label label_6 = new Label(transportdatenGroup, SWT.NONE);
        label_6.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_label_6 = new FormData();
        fd_label_6.bottom = new FormAttachment(0, 118);
        fd_label_6.top = new FormAttachment(0, 105);
        fd_label_6.right = new FormAttachment(0, 313);
        fd_label_6.left = new FormAttachment(0, 150);
        label_6.setLayoutData(fd_label_6);
        label_6.setForeground(Util.getColor(25, 25, 112));
        label_6.setText("Zuständige Ortsstelle:");

        Combo comboZustaendigeOrtsstelle = new Combo(transportdatenGroup, SWT.READ_ONLY);
        zustaendigeOrtsstelle = new ComboViewer(comboZustaendigeOrtsstelle);
        zustaendigeOrtsstelle.setContentProvider(new StationContentProvider());
        zustaendigeOrtsstelle.setLabelProvider(new StationLabelProvider());
        zustaendigeOrtsstelle.setInput(ModelFactory.getInstance().getLocationList());

        final FormData fd_comboZustaendigeOrtsstelle = new FormData();
        fd_comboZustaendigeOrtsstelle.bottom = new FormAttachment(0, 121);
        fd_comboZustaendigeOrtsstelle.top = new FormAttachment(0, 100);
        fd_comboZustaendigeOrtsstelle.right = new FormAttachment(0, 431);
        fd_comboZustaendigeOrtsstelle.left = new FormAttachment(0, 319);
        comboZustaendigeOrtsstelle.setLayoutData(fd_comboZustaendigeOrtsstelle);

        transportdatenGroup.setTabList(new Control[] {comboVonStrasse, comboVonOrt, comboNachname, comboVorname, comboNachStrasse, comboNachOrt, gehendButton, tragsesselButton, krankentrageButton, eigenerRollstuhlButton, ruecktransportMoeglichButton, comboZustaendigeOrtsstelle, begleitpersonButton, textAnrufer, textTelefonAnrufer});

        planungGroup = new Group(client, SWT.NONE);
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
        abfLabel.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_abfLabel = new FormData();
        fd_abfLabel.bottom = new FormAttachment(0, 37);
        fd_abfLabel.top = new FormAttachment(0, 24);
        fd_abfLabel.right = new FormAttachment(0, 32);
        fd_abfLabel.left = new FormAttachment(0, 7);
        abfLabel.setLayoutData(fd_abfLabel);
        abfLabel.setForeground(Util.getColor(25, 25, 112));
        abfLabel.setText("Abf:");

        final Label beiPatLabel = new Label(planungGroup, SWT.NONE);
        final FormData fd_beiPatLabel = new FormData();
        fd_beiPatLabel.bottom = new FormAttachment(0, 64);
        fd_beiPatLabel.top = new FormAttachment(0, 51);
        fd_beiPatLabel.right = new FormAttachment(0, 32);
        fd_beiPatLabel.left = new FormAttachment(0, 7);
        beiPatLabel.setLayoutData(fd_beiPatLabel);
        beiPatLabel.setForeground(Util.getColor(128, 128, 128));
        beiPatLabel.setText("Pat.:");

        final Label terminLabel = new Label(planungGroup, SWT.NONE);
        final FormData fd_terminLabel = new FormData();
        fd_terminLabel.bottom = new FormAttachment(0, 91);
        fd_terminLabel.top = new FormAttachment(0, 78);
        fd_terminLabel.right = new FormAttachment(0, 35);
        fd_terminLabel.left = new FormAttachment(0, 7);
        terminLabel.setLayoutData(fd_terminLabel);
        terminLabel.setForeground(Util.getColor(128, 128, 128));
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
        mariazellButton.setLayoutData(fd_mariazellButton);
        mariazellButton.setText("Mariazell");

        wienButton = new Button(planungGroup, SWT.RADIO);
        fd_mariazellButton.bottom = new FormAttachment(wienButton, 16, SWT.BOTTOM);
        fd_mariazellButton.top = new FormAttachment(wienButton, 0, SWT.BOTTOM);
        fd_mariazellButton.right = new FormAttachment(wienButton, 71, SWT.LEFT);
        fd_mariazellButton.left = new FormAttachment(wienButton, 0, SWT.LEFT);
        final FormData fd_wienButton = new FormData();
        wienButton.setLayoutData(fd_wienButton);
        wienButton.setText("Wien");

        leobenButton = new Button(planungGroup, SWT.RADIO);
        fd_wienButton.bottom = new FormAttachment(leobenButton, 16, SWT.BOTTOM);
        fd_wienButton.top = new FormAttachment(leobenButton, 0, SWT.BOTTOM);
        fd_wienButton.right = new FormAttachment(leobenButton, 71, SWT.LEFT);
        fd_wienButton.left = new FormAttachment(leobenButton, 0, SWT.LEFT);
        final FormData fd_leobenButton = new FormData();
        leobenButton.setLayoutData(fd_leobenButton);
        leobenButton.setText("Leoben");

        grazButton = new Button(planungGroup, SWT.RADIO);
        fd_leobenButton.bottom = new FormAttachment(grazButton, 16, SWT.BOTTOM);
        fd_leobenButton.top = new FormAttachment(grazButton, 0, SWT.BOTTOM);
        fd_leobenButton.right = new FormAttachment(grazButton, 71, SWT.LEFT);
        fd_leobenButton.left = new FormAttachment(grazButton, 0, SWT.LEFT);
        final FormData fd_grazButton = new FormData();
        grazButton.setLayoutData(fd_grazButton);
        grazButton.setText("Graz");

        bruckButton = new Button(planungGroup, SWT.RADIO);
        final FormData fd_bezirkButton = new FormData();
        fd_bezirkButton.bottom = new FormAttachment(bruckButton, 16, SWT.BOTTOM);
        fd_bezirkButton.top = new FormAttachment(bruckButton, 0, SWT.BOTTOM);
        fd_bezirkButton.right = new FormAttachment(bruckButton, 77, SWT.LEFT);
        fd_bezirkButton.left = new FormAttachment(bruckButton, 0, SWT.LEFT);
        final FormData fd_bruckButton = new FormData();
        bruckButton.setLayoutData(fd_bruckButton);
        bruckButton.setText("Bruck");

        kapfenbergButton = new Button(planungGroup, SWT.RADIO);
        fd_grazButton.bottom = new FormAttachment(kapfenbergButton, 16, SWT.BOTTOM);
        fd_grazButton.top = new FormAttachment(kapfenbergButton, 0, SWT.BOTTOM);
        fd_grazButton.right = new FormAttachment(kapfenbergButton, 71, SWT.LEFT);
        fd_grazButton.left = new FormAttachment(kapfenbergButton, 0, SWT.LEFT);
        final FormData fd_kapfenbergButton = new FormData();
        fd_kapfenbergButton.bottom = new FormAttachment(0, 25);
        fd_kapfenbergButton.top = new FormAttachment(0, 39);
        fd_kapfenbergButton.right = new FormAttachment(0, 166);
        fd_kapfenbergButton.left = new FormAttachment(0, 95);
        kapfenbergButton.setLayoutData(fd_bezirkButton);
        kapfenbergButton.setText("Kapfenberg");

        Label label_2;
        label_2 = new Label(planungGroup, SWT.SEPARATOR);
        fd_bruckButton.bottom = new FormAttachment(label_2, 16, SWT.TOP);
        fd_bruckButton.top = new FormAttachment(label_2, 0, SWT.TOP);
        fd_bruckButton.right = new FormAttachment(label_2, 49, SWT.RIGHT);
        fd_bruckButton.left = new FormAttachment(label_2, 0, SWT.RIGHT);
        final FormData fd_label_2 = new FormData();
        fd_label_2.bottom = new FormAttachment(0, 159);
        fd_label_2.top = new FormAttachment(0, 20);
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
        fernfahrtButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.fernfahrt"));
        fernfahrtButton.setToolTipText("Fernfahrten sind lt. RKT deklariert");
        planungGroup.setTabList(new Control[] {textAbf, textBeiPat, textTermin, fernfahrtButton, bruckButton, kapfenbergButton, grazButton, leobenButton, wienButton, mariazellButton});

        //group 'Patientenzustand'
        patientenzustandGroup = new Group(client, SWT.NONE);
        patientenzustandGroup.setLayout(new FormLayout());
        final FormData fd_patientenzustandGroup = new FormData();
        fd_patientenzustandGroup.bottom = new FormAttachment(0, 348);
        fd_patientenzustandGroup.top = new FormAttachment(0, 166);
        fd_patientenzustandGroup.right = new FormAttachment(0, 920);
        fd_patientenzustandGroup.left = new FormAttachment(0, 194);
        patientenzustandGroup.setLayoutData(fd_patientenzustandGroup);
        patientenzustandGroup.setText("Patientenzustand");

        comboErkrankungVerletzung = new Combo(patientenzustandGroup, SWT.READ_ONLY);
        final FormData fd_comboErkrankungVerletzung = new FormData();
        fd_comboErkrankungVerletzung.bottom = new FormAttachment(0, 50);
        fd_comboErkrankungVerletzung.top = new FormAttachment(0, 29);
        fd_comboErkrankungVerletzung.right = new FormAttachment(0, 289);
        fd_comboErkrankungVerletzung.left = new FormAttachment(0, 7);
        comboErkrankungVerletzung.setLayoutData(fd_comboErkrankungVerletzung);
        setErkrVerl = new ComboViewer(comboErkrankungVerletzung);
        setErkrVerl.setContentProvider(new DiseaseContentProvider());
        setErkrVerl.setLabelProvider(new DiseaseLabelProvider());
        setErkrVerl.setInput(ModelFactory.getInstance().getDialyseList());

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
        erkrankungverletzungLabel.setForeground(Util.getColor(128, 128, 128));
        erkrankungverletzungLabel.setText("Erkrankung/Verletzung");

        final Label anmerkungenLabel = new Label(patientenzustandGroup, SWT.NONE);
        final FormData fd_anmerkungenLabel = new FormData();
        fd_anmerkungenLabel.bottom = new FormAttachment(0, 69);
        fd_anmerkungenLabel.top = new FormAttachment(0, 56);
        fd_anmerkungenLabel.right = new FormAttachment(0, 134);
        fd_anmerkungenLabel.left = new FormAttachment(0, 7);
        anmerkungenLabel.setLayoutData(fd_anmerkungenLabel);
        anmerkungenLabel.setForeground(Util.getColor(128, 128, 128));
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
        label_3.setForeground(Util.getColor(128, 128, 128));
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

        comboPrioritaet = new Combo(patientenzustandGroup, SWT.READ_ONLY);
        comboPrioritaet.setToolTipText("A (NEF), B (BD1), C (Transport), D (Rücktransport), E (Heimtransport), F (Sonstiges), G (NEF extern)");

        //set possible priorities
        if(transportType.equalsIgnoreCase("prebooking"))
            comboPrioritaet.setItems(prebookingPriorities);
        if(transportType.equalsIgnoreCase("emergencyTransport") || transportType.equalsIgnoreCase("both"))
            comboPrioritaet.setItems(emergencyAndTransportPriorities);
        comboPrioritaet.setData("newKey", null);
        final FormData fd_comboPrioritaet = new FormData();
        fd_comboPrioritaet.bottom = new FormAttachment(0, 73);
        fd_comboPrioritaet.top = new FormAttachment(0, 52);
        fd_comboPrioritaet.right = new FormAttachment(0, 287);
        fd_comboPrioritaet.left = new FormAttachment(0, 225);
        comboPrioritaet.setLayoutData(fd_comboPrioritaet);

        final Label label_4 = new Label(patientenzustandGroup, SWT.NONE);
        label_4.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_label_4 = new FormData();
        fd_label_4.left = new FormAttachment(0, 160);
        fd_label_4.bottom = new FormAttachment(0, 69);
        fd_label_4.top = new FormAttachment(0, 56);
        fd_label_4.right = new FormAttachment(0, 220);
        label_4.setLayoutData(fd_label_4);
        label_4.setForeground(Util.getColor(25, 25, 112));
        label_4.setText("Priorität:");
        patientenzustandGroup.setTabList(new Control[] {setErkrVerl.getControl(), comboPrioritaet, textAnmerkungen, textRueckmeldung, bd2Button});

        //group 'Alarmierung'
        planungGroup_1 = new Group(client, SWT.NONE);
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
        notarztButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.notarzt"));
        notarztButton.setToolTipText("Externer! Notarzt für diesen Transport alarmiert");

        rthButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_rthButton = new FormData();
        fd_rthButton.bottom = new FormAttachment(0, 49);
        fd_rthButton.top = new FormAttachment(0, 33);
        fd_rthButton.right = new FormAttachment(0, 92);
        fd_rthButton.left = new FormAttachment(0, 7);
        rthButton.setLayoutData(fd_rthButton);
        rthButton.setToolTipText("Hubschrauber");
        rthButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.hubschrauber"));

        dfButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_dfButton = new FormData();
        fd_dfButton.bottom = new FormAttachment(0, 71);
        fd_dfButton.top = new FormAttachment(0, 55);
        fd_dfButton.right = new FormAttachment(0, 80);
        fd_dfButton.left = new FormAttachment(0, 7);
        dfButton.setLayoutData(fd_dfButton);
        dfButton.setToolTipText("DF/Inspektionsdienst");
        dfButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.rotlicht"));

        brkdtButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_brkdtButton = new FormData();
        fd_brkdtButton.bottom = new FormAttachment(0, 93);
        fd_brkdtButton.top = new FormAttachment(0, 77);
        fd_brkdtButton.right = new FormAttachment(0, 92);
        fd_brkdtButton.left = new FormAttachment(0, 7);
        brkdtButton.setLayoutData(fd_brkdtButton);
        brkdtButton.setToolTipText("Bezirksrettungskommandant");
        brkdtButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.rotlicht"));

        feuerwehrButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_feuerwehrButton = new FormData();
        fd_feuerwehrButton.bottom = new FormAttachment(0, 115);
        fd_feuerwehrButton.top = new FormAttachment(0, 99);
        fd_feuerwehrButton.right = new FormAttachment(0, 80);
        fd_feuerwehrButton.left = new FormAttachment(0, 7);
        feuerwehrButton.setLayoutData(fd_feuerwehrButton);
        feuerwehrButton.setToolTipText("Feuerwehr");
        feuerwehrButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.feuerwehr"));

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
        polizeiButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.polizei"));

        bergrettungButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_bergrettungButton = new FormData();
        fd_bergrettungButton.bottom = new FormAttachment(0, 159);
        fd_bergrettungButton.top = new FormAttachment(0, 143);
        fd_bergrettungButton.right = new FormAttachment(0, 92);
        fd_bergrettungButton.left = new FormAttachment(0, 7);
        bergrettungButton.setLayoutData(fd_bergrettungButton);
        bergrettungButton.setImage(ImageFactory.getInstance().getRegisteredImage("transport.alarming.bergrettung"));
        bergrettungButton.setToolTipText("Bergrettung");

        planungGroup_1.setTabList(new Control[] {notarztButton, rthButton, dfButton, brkdtButton, feuerwehrButton, polizeiButton, bergrettungButton});

        final Label label_5 = new Label(client, SWT.SEPARATOR | SWT.HORIZONTAL);
        final FormData fd_label_5 = new FormData();
        fd_label_5.bottom = new FormAttachment(0, 367);
        fd_label_5.top = new FormAttachment(0, 354);
        fd_label_5.right = new FormAttachment(0, 1056);
        fd_label_5.left = new FormAttachment(0, 10);
        label_5.setLayoutData(fd_label_5);

        //group 'Transportdetails'
        transportdetailsGroup = new Group(client, SWT.NONE);
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
        transportnumemmerLabel.setForeground(Util.getColor(128, 128, 128));
        transportnumemmerLabel.setText("Trsp.Nr.:");

        textTransportNummer = new Text(transportdetailsGroup, SWT.BORDER);
        final FormData fd_textTransportNummer = new FormData();
        fd_textTransportNummer.bottom = new FormAttachment(0, 32);
        fd_textTransportNummer.top = new FormAttachment(0, 11);
        fd_textTransportNummer.right = new FormAttachment(0, 158);
        fd_textTransportNummer.left = new FormAttachment(0, 60);
        textTransportNummer.setLayoutData(fd_textTransportNummer);
        textTransportNummer.setEditable(false);

        final Label ortsstelleLabel = new Label(transportdetailsGroup, SWT.NONE);
        final FormData fd_ortsstelleLabel = new FormData();
        fd_ortsstelleLabel.bottom = new FormAttachment(0, 54);
        fd_ortsstelleLabel.top = new FormAttachment(0, 41);
        fd_ortsstelleLabel.right = new FormAttachment(0, 57);
        fd_ortsstelleLabel.left = new FormAttachment(0, 7);
        ortsstelleLabel.setLayoutData(fd_ortsstelleLabel);
        ortsstelleLabel.setForeground(Util.getColor(25, 25, 112));
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
        farzeugLabel.setForeground(Util.getColor(128, 128, 128));
        farzeugLabel.setText("Fahrzeug:");
        transportdetailsGroup.setTabList(new Control[] {textTransportNummer, textOrtsstelle, textFahrzeug});


        //group 'Personal am Fahrzeug'
        personalAmFahrzeugGroup = new Group(client, SWT.NONE);
        personalAmFahrzeugGroup.setLayout(new FormLayout());
        final FormData fd_personalAmFahrzeugGroup = new FormData();
        fd_personalAmFahrzeugGroup.bottom = new FormAttachment(0, 501);
        fd_personalAmFahrzeugGroup.top = new FormAttachment(0, 373);
        fd_personalAmFahrzeugGroup.right = new FormAttachment(0, 483);
        fd_personalAmFahrzeugGroup.left = new FormAttachment(0, 194);
        personalAmFahrzeugGroup.setLayoutData(fd_personalAmFahrzeugGroup);
        personalAmFahrzeugGroup.setText("Personal am Fahrzeug");

        Combo textFahrer = new Combo(personalAmFahrzeugGroup, SWT.BORDER);
        final FormData fd_textFahrer = new FormData();
        fd_textFahrer.bottom = new FormAttachment(0, 32);
        fd_textFahrer.top = new FormAttachment(0, 11);
        fd_textFahrer.right = new FormAttachment(0, 276);
        fd_textFahrer.left = new FormAttachment(0, 73);
        textFahrer.setLayoutData(fd_textFahrer);
        setTextFahrer = new ComboViewer(textFahrer);
        setTextFahrer.setContentProvider(new StaffMemberContentProvider());
        setTextFahrer.setLabelProvider(new StaffMemberLabelProvider());
        setTextFahrer.setInput(ModelFactory.getInstance().getStaffList());

        Combo textSaniI = new Combo(personalAmFahrzeugGroup, SWT.BORDER);
        final FormData fd_textSnaniI = new FormData();
        fd_textSnaniI.bottom = new FormAttachment(0, 59);
        fd_textSnaniI.top = new FormAttachment(0, 38);
        fd_textSnaniI.right = new FormAttachment(0, 276);
        fd_textSnaniI.left = new FormAttachment(0, 73);
        textSaniI.setLayoutData(fd_textSnaniI);
        setTextSaniI = new ComboViewer(textSaniI);
        setTextSaniI.setContentProvider(new StaffMemberContentProvider());
        setTextSaniI.setLabelProvider(new StaffMemberLabelProvider());
        setTextSaniI.setInput(ModelFactory.getInstance().getStaffList());

        Combo textSaniII = new Combo(personalAmFahrzeugGroup, SWT.BORDER);
        final FormData fd_textSaniII = new FormData();
        fd_textSaniII.bottom = new FormAttachment(0, 86);
        fd_textSaniII.top = new FormAttachment(0, 65);
        fd_textSaniII.right = new FormAttachment(0, 276);
        fd_textSaniII.left = new FormAttachment(0, 73);
        textSaniII.setLayoutData(fd_textSaniII);
        setTextSaniII = new ComboViewer(textSaniII);
        setTextSaniII.setContentProvider(new StaffMemberContentProvider());
        setTextSaniII.setLabelProvider(new StaffMemberLabelProvider());
        setTextSaniII.setInput(ModelFactory.getInstance().getServiceList());

        final Label driverLabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
        final FormData fd_driverLabel = new FormData();
        fd_driverLabel.bottom = new FormAttachment(0, 27);
        fd_driverLabel.top = new FormAttachment(0, 14);
        fd_driverLabel.right = new FormAttachment(0, 54);
        fd_driverLabel.left = new FormAttachment(0, 7);
        driverLabel.setLayoutData(fd_driverLabel);
        driverLabel.setForeground(Util.getColor(128, 128, 128));
        driverLabel.setText("Fahrer:");

        final Label paramedicILabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
        final FormData fd_paramedicILabel = new FormData();
        fd_paramedicILabel.bottom = new FormAttachment(0, 54);
        fd_paramedicILabel.top = new FormAttachment(0, 41);
        fd_paramedicILabel.right = new FormAttachment(0, 68);
        fd_paramedicILabel.left = new FormAttachment(0, 7);
        paramedicILabel.setLayoutData(fd_paramedicILabel);
        paramedicILabel.setForeground(Util.getColor(128, 128, 128));
        paramedicILabel.setText("Sanitäter I:");

        final Label paramedicIILabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
        final FormData fd_paramedicIILabel = new FormData();
        fd_paramedicIILabel.bottom = new FormAttachment(0, 81);
        fd_paramedicIILabel.top = new FormAttachment(0, 68);
        fd_paramedicIILabel.right = new FormAttachment(0, 68);
        fd_paramedicIILabel.left = new FormAttachment(0, 7);
        paramedicIILabel.setLayoutData(fd_paramedicIILabel);
        paramedicIILabel.setForeground(Util.getColor(128, 128, 128));
        paramedicIILabel.setText("Sanitäter II:");
        personalAmFahrzeugGroup.setTabList(new Control[] {textFahrer, textSaniI, textSaniII});


        //group 'Statusmeldungen'
        statusmeldungenGroup = new Group(client, SWT.NONE);
        statusmeldungenGroup.setLayout(new FormLayout());
        final FormData fd_statusmeldungenGroup = new FormData();
        fd_statusmeldungenGroup.bottom = new FormAttachment(0, 501);
        fd_statusmeldungenGroup.top = new FormAttachment(0, 373);
        fd_statusmeldungenGroup.right = new FormAttachment(0, 843);
        fd_statusmeldungenGroup.left = new FormAttachment(0, 489);
        statusmeldungenGroup.setLayoutData(fd_statusmeldungenGroup);
        statusmeldungenGroup.setText("Statusmeldungen");

        textAufgen = new Text(statusmeldungenGroup, SWT.BORDER);
        final FormData fd_textAufgen = new FormData();
        fd_textAufgen.bottom = new FormAttachment(0, 32);
        fd_textAufgen.top = new FormAttachment(0, 11);
        fd_textAufgen.right = new FormAttachment(0, 85);
        fd_textAufgen.left = new FormAttachment(0, 44);
        textAufgen.setLayoutData(fd_textAufgen);

        textAufgen.setEditable(false);

        final Label aufgLabel = new Label(statusmeldungenGroup, SWT.NONE);
        final FormData fd_aufgLabel = new FormData();
        fd_aufgLabel.bottom = new FormAttachment(0, 26);
        fd_aufgLabel.top = new FormAttachment(0, 13);
        fd_aufgLabel.right = new FormAttachment(0, 43);
        fd_aufgLabel.left = new FormAttachment(0, 12);
        aufgLabel.setLayoutData(fd_aufgLabel);
        aufgLabel.setForeground(Util.getColor(128, 128, 128));
        aufgLabel.setText("Aufg.:");


        textAE = new Text(statusmeldungenGroup, SWT.BORDER);
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
        aeLabel.setForeground(Util.getColor(128, 128, 128));
        aeLabel.setText("AE:");


        textS1 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts1Label.setForeground(Util.getColor(128, 128, 128));
        ts1Label.setText("S1:");


        textS2 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts2Label.setForeground(Util.getColor(128, 128, 128));
        ts2Label.setText("S2:");


        textS3 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts3Label.setForeground(Util.getColor(128, 128, 128));
        ts3Label.setText("S3:");


        textS4 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts4Label.setForeground(Util.getColor(128, 128, 128));
        ts4Label.setText("S4:");


        textS5 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts5Label.setForeground(Util.getColor(128, 128, 128));
        ts5Label.setText("S5:");


        textS6 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts6Label.setForeground(Util.getColor(128, 128, 128));
        ts6Label.setText("S6:");


        textS7 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts7Label.setForeground(Util.getColor(128, 128, 128));
        ts7Label.setText("S7:");


        textS8 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts8Label.setForeground(Util.getColor(128, 128, 128));
        ts8Label.setText("S8:");


        textS9 = new Text(statusmeldungenGroup, SWT.BORDER);
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
        ts9Label.setForeground(Util.getColor(128, 128, 128));
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

        if("journal".equalsIgnoreCase(editingType))
        {
            transportdetailsGroup.setVisible(true);
            statusmeldungenGroup.setVisible(true);
            personalAmFahrzeugGroup.setVisible(true);
        }
        else if ("both".equalsIgnoreCase(transportType))//in the case of editing a transport from the following views: UnderwayTransportsView, OutstandingTransportsView, PrebookingView
        {
            transportdetailsGroup.setVisible(false);
            statusmeldungenGroup.setVisible(false);
            personalAmFahrzeugGroup.setVisible(false);

            planungGroup.setVisible(true);
            planungGroup_1.setVisible(true);
        }
        else
        {
            transportdetailsGroup.setVisible(false);
            statusmeldungenGroup.setVisible(false);
            personalAmFahrzeugGroup.setVisible(false);
        }

        //transport type selection buttons
        group = new Group(client, SWT.NONE);
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

                transportType = "emergencyTransport";

                //set possible priorities
                if(transportType.equalsIgnoreCase("prebooking"))
                    comboPrioritaet.setItems(prebookingPriorities);
                if(transportType.equalsIgnoreCase("emergencyTransport"))
                    comboPrioritaet.setItems(emergencyAndTransportPriorities);
            }
        });
        buttonNotfall.setText("Transport/Einsatz");

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

                transportType = "prebooking";

                //set possible priorities
                if(transportType.equalsIgnoreCase("prebooking"))
                {
                    comboPrioritaet.setItems(prebookingPriorities);
                }
                if(transportType.equalsIgnoreCase("emergencyTransport"))
                {
                    comboPrioritaet.setItems(emergencyAndTransportPriorities);
                }

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
        if("journal".equalsIgnoreCase(editingType))
            buttonAlles.setVisible(true);
        else
            buttonAlles.setVisible(false);

        final Label label_7 = new Label(group, SWT.NONE);
        final FormData fd_label_7 = new FormData();
        fd_label_7.bottom = new FormAttachment(0, 76);
        fd_label_7.top = new FormAttachment(0, 26);
        fd_label_7.right = new FormAttachment(0, 88);
        fd_label_7.left = new FormAttachment(0, 5);
        label_7.setLayoutData(fd_label_7);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
        // the viewer represents simple model. refresh should be enough.
        if ("STAFF_ADD".equals(evt.getPropertyName())
                || "STAFF_REMOVE".equals(evt.getPropertyName())
                || "STAFF_UPDATE".equals(evt.getPropertyName())
                || "STAFF_CLEARED".equals(evt.getPropertyName()))
        { 
            setTextFahrer.refresh();
            setTextSaniI.refresh();
            setTextSaniII.refresh();
        }
        // update the viewer when a disease has changed
        if ("DISEASE_ADD".equals(evt.getPropertyName())
                || "DISEASE_REMOVE".equalsIgnoreCase(evt.getPropertyName())
                || "DISEASE_UPDATE".equalsIgnoreCase(evt.getPropertyName())
                || "DISEASE_CLEARED".equalsIgnoreCase(evt.getPropertyName()))
        { 
            setErkrVerl.refresh();
        }
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

    /**
     * Converts and returns the given time string to a calendar.<br>
     * The provided time string must have either the format HHmm or HH:mm.
     * @return the date entered or null if the time is not valid
     */
    private Calendar convertStringToDate(final String dateString)
    {
        //the date formatter
        DateFormat formatter;

        //the types of the accepted strings
        if(dateString.contains(":"))
            formatter = new SimpleDateFormat("HH:mm");
        else
            formatter = new SimpleDateFormat("HHmm");

        //try and parse the string
        try
        {
            long timestamp = formatter.parse(dateString).getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            return cal;
        }
        catch(ParseException pe)
        {
            return null;
        }
    }
}