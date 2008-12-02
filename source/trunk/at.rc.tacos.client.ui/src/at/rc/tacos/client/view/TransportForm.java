package at.rc.tacos.client.view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import at.rc.tacos.client.controller.DuplicatePriorityATransportAction;
import at.rc.tacos.client.controller.RemoveTransportFromMultiTransportList;
import at.rc.tacos.client.jobs.FilterAddressJob;
import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.AddressHandler;
import at.rc.tacos.client.net.handler.DiseaseHandler;
import at.rc.tacos.client.net.handler.LocationHandler;
import at.rc.tacos.client.net.handler.StaffHandler;
import at.rc.tacos.client.net.handler.TransportHandler;
import at.rc.tacos.client.net.handler.VehicleHandler;
import at.rc.tacos.client.providers.AssignVehicleContentProvider;
import at.rc.tacos.client.providers.DiseaseContentProvider;
import at.rc.tacos.client.providers.DiseaseLabelProvider;
import at.rc.tacos.client.providers.MultiTransportContentProvider;
import at.rc.tacos.client.providers.MultiTransportLabelProvider;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.providers.VehicleAssignLabelProvider;
import at.rc.tacos.client.ui.Activator;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.client.view.sorterAndTooltip.TransportSorter;
import at.rc.tacos.client.view.sorterAndTooltip.VehicleSorter;
import at.rc.tacos.platform.iface.IDirectness;
import at.rc.tacos.platform.iface.IFilterTypes;
import at.rc.tacos.platform.iface.IKindOfTransport;
import at.rc.tacos.platform.iface.IProgramStatus;
import at.rc.tacos.platform.iface.ITransportStatus;
import at.rc.tacos.platform.model.Address;
import at.rc.tacos.platform.model.CallerDetail;
import at.rc.tacos.platform.model.DialysisPatient;
import at.rc.tacos.platform.model.Disease;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Lock;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.Patient;
import at.rc.tacos.platform.model.SickPerson;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.model.Transport;
import at.rc.tacos.platform.model.VehicleDetail;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.RemoveMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;
import at.rc.tacos.platform.util.MyUtils;

/**
 * GUI (form) to manage the transport details one form for all kinds of transports (not necessary
 * groups are blanked out) function is implemented (assign vehicle, multi transport)
 * 
 * @author b.thek
 */
public class TransportForm extends TitleAreaDialog implements DataChangeListener<Object> {

    private Text textRueckmeldung;
    private Text textAnmerkungen;
    private Text textFahrzeug;
    private Text textOrtsstelle;
    private Text textTransportNummer;
    private Text textTermin;
    private Text textBeiPat;
    private Text textAbf;
    private Text createdBy;
    private Text disposedBy;
    private Text textAufgen;
    private Text textAE;
    private Text timestampNA;
    private Text timestampRTH;
    private Text timestampDF;
    private Text timestampBRKDT;
    private Text timestampFW;
    private Text timestampPolizei;
    private Text timestampBergrettung;
    private Text timestampKIT;
    private Text patientLastName, patientFirstName;
    private Text textTelefonAnrufer;
    private Text textAnrufer;

    private Combo comboPrioritaet;
    private Combo comboErkrankungVerletzung;
    private Combo combokindOfTransport;

    private Button buttonVormerkung;
    private Button buttonNotfall;
    private Button buttonDialyse;
    private Button ruecktransportMoeglichButton;
    private Button begleitpersonButton;
    private Button rufhilfepatientButton;
    private Button fernfahrtButton;
    private Button mariazellButton;
    private Button wienButton;
    private Button leobenButton;
    private Button grazButton;
    private Button bruckButton;
    private Button kapfenbergButton;
    private Button bergrettungButton;
    private Button polizeiButton;
    private Button KITButton;
    private Button feuerwehrButton;
    private Button brkdtButton;
    private Button dfButton;
    private Button rthButton;
    private Button notarztButton;
    private Button bd2Button;
    private Button bd1Button;
    private Button buttonMehrfachtransport;
    private Button buttonADDMehrfachtransport;
    private Button buttonAssignCar;

    private Group formGroup;
    private Group statusmeldungenGroup;
    private Group personalAmFahrzeugGroup;
    private Group planungGroup_1;
    private Group patientenzustandGroup;
    private Group planungGroup;
    private Group transportdatenGroup;
    private Group transportdetailsGroup;
    private Group multiTransportGroup;
    private Group assignCarGroup;

    private ComboViewer zustaendigeOrtsstelle;
    private DateTime dateTime;
    private Composite client;

    private MultiTransportContentProvider multiTransportProvider;
    private AssignVehicleContentProvider assignVehicleContentProvider;

    // the stati
    private Text textS1, textS2, textS3, textS4, textS5, textS6;

    private Text textFromStreet, textToStreet, textFromCity, textToCity;
    private AutoCompleteField acFromStreet, acToStreet, acFromCity, acToCity;
    private ComboViewer setTextFahrer, setTextSaniI, setTextSaniII;
    private ComboViewer setErkrVerl;

    private String[] prebookingPriorities = { "2 Transport", "3 Terminfahrt", "4 RT", "5 HT",
            "6 Sonstiges", "7 NEF extern" };
    private String[] emergencyAndTransportPriorities = { "1 NEF", "2 Transport", "3 Terminfahrt",
            "4 RT", "5 HT", "6 Sonstiges", "7 NEF extern" };

    private boolean mehrfachtransport;
    private boolean finalMultiTransportKlick;

    private TableViewer viewer, viewerAssign;

    private RemoveTransportFromMultiTransportList removeAction;

    /**
     * if the old priority is not A but the new is A-> DuplicatePriorityATransportAction necessary
     **/
    private String oldPriority;

    /**
     * save the priority when the type of transport is switched (e.g. from prebooking to emergency)
     */
    private String tmpPriority = "";

    /**
     * possible editingTypes: journal (the "AllesButton" should be visible), prebooking,
     * outstanding, underway
     */
    private String editingType;

    /**
     * transport type used to differ between a normal and an emergency transport possible values:
     * prebooking, emergencyTransport, ?wholeTransportDetails?- possible?
     */
    private String transportType;

    /**
     * The scheduler job to start the filter
     */
    private FilterAddressJob filterJob;

    // the edited transport
    private Transport transport;
    private boolean createNew;

    // the handlers
    private String username = NetWrapper.getSession().getUsername();
    private String authorization = NetWrapper.getSession().getLogin().getAuthorization();
    private ImageRegistry imageRegistry = Activator.getDefault().getImageRegistry();

    private AddressHandler addressHandler = (AddressHandler) NetWrapper.getHandler(Address.class);
    private DiseaseHandler diseaseHandler = (DiseaseHandler) NetWrapper.getHandler(Disease.class);
    private StaffHandler staffHandler = (StaffHandler) NetWrapper.getHandler(StaffMember.class);
    private VehicleHandler vehicleHandler = (VehicleHandler) NetWrapper
            .getHandler(VehicleDetail.class);
    private LocationHandler locationHandler = (LocationHandler) NetWrapper
            .getHandler(Location.class);
    private TransportHandler transportHandler = (TransportHandler) NetWrapper
            .getHandler(Transport.class);

    /**
     * Default class constructor used to create a new Transport.
     * 
     * @param parentShell
     *            the parent shell
     */
    public TransportForm(Shell parentShell) {
        super(parentShell);
        this.createNew = true;
        this.transport = new Transport();
    }

    /**
     * Default class constructor to create a new transport and set the layout.
     * 
     * @param parentShell
     *            the parent shell
     * @param transportType
     *            the type of the transport
     */
    public TransportForm(Shell parentShell, String transportType) {
        super(parentShell);
        this.createNew = true;
        this.transportType = transportType;
        this.transport = new Transport();
    }

    /**
     * Default class constructor to edit an existing transport
     * 
     * @param parentShell
     *            the parent shell
     * @param transport
     *            the transport to edit
     * @param editingType
     *            the layout of the form to show
     */
    public TransportForm(Shell parentShell, Transport transport, String editingType) {
        super(parentShell);
        // update an entry
        createNew = false;
        this.transport = transport;
        this.editingType = editingType;
        this.transportType = "both";
    }

    /**
     * Creates the dialog's contents
     * 
     * @param parent
     *            the parent composite
     * @return Control
     */
    protected Control createContents(Composite parent) {
        multiTransportProvider = new MultiTransportContentProvider();
        Control contents = super.createContents(parent);
        setTitle("Transport");
        setMessage("Hier k�nnen Sie einen neuen Transport anlegen", IMessageProvider.INFORMATION);
        setTitleImage(imageRegistry.get("application.logo"));
        getShell().pack(true);
        setShellStyle(SWT.SYSTEM_MODAL);
        // add some listeners to this view
        NetWrapper.registerListener(this, StaffMember.class);
        NetWrapper.registerListener(this, Disease.class);
        NetWrapper.registerListener(this, Address.class);
        NetWrapper.registerListener(this, VehicleDetail.class);

        return contents;
    }

    @Override
    public boolean close() {
        // remove the lock again, only if the transport is existing
        if (!createNew) {
            Lock lock = new Lock(transport.getTransportId(), Transport.class, "");
            RemoveMessage<Lock> removeMessage = new RemoveMessage<Lock>(lock);
            NetWrapper.sendMessage(removeMessage);
        }
        NetWrapper.removeListener(this, StaffMember.class);
        NetWrapper.removeListener(this, Disease.class);
        NetWrapper.removeListener(this, Address.class);
        NetWrapper.removeListener(this, VehicleDetail.class);
        return super.close();
    }

    /**
     * Create contents of the window
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        // setup the composite
        Composite composite = (Composite) super.createDialogArea(parent);
        setShellStyle(SWT.SYSTEM_MODAL);
        // Create the content of the dialog
        createTransportSection(composite);
        // disable some buttons
        if (transportType.equalsIgnoreCase("emergencyTransport")) {
            buttonMehrfachtransport.setEnabled(false);
            buttonAssignCar.setEnabled(true);
        }
        if (transportType.equalsIgnoreCase("prebooking"))
            buttonAssignCar.setEnabled(false);

        // do not allow editing longer than 4 days after the transport
        if (transportType.equalsIgnoreCase("both")) {
            Calendar fourDaysAgo = Calendar.getInstance();
            fourDaysAgo.set(Calendar.DAY_OF_YEAR, fourDaysAgo.get(Calendar.DAY_OF_YEAR) - 4);

            Calendar transportDate = Calendar.getInstance();
            transportDate.setTimeInMillis(transport.getDateOfTransport());
            if (transportDate.getTimeInMillis() < fourDaysAgo.getTimeInMillis()) {
                composite.setEnabled(false);
            }
        }

        // Simple date format for the alarming timestamps
        SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");

        // init data
        if (!createNew) {
            // changing transport type only possible for a new transport
            buttonVormerkung.setEnabled(false);
            buttonNotfall.setEnabled(false);
            buttonDialyse.setEnabled(false);
            buttonMehrfachtransport.setVisible(false);
            buttonADDMehrfachtransport.setVisible(false);
            buttonAssignCar.setVisible(false);

            // set field contents
            GregorianCalendar gcal = new GregorianCalendar();
            gcal.setTimeZone(TimeZone.getDefault());
            // formatter for the date and time
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();

            // date of transport
            gcal.setTimeInMillis(transport.getDateOfTransport());
            this.dateTime.setDay(gcal.get(GregorianCalendar.DATE));
            this.dateTime.setMonth(gcal.get(GregorianCalendar.MONTH));
            this.dateTime.setYear(gcal.get(GregorianCalendar.YEAR));

            // planned start of transport
            if (transport.getPlannedStartOfTransport() != 0) {
                gcal.setTimeInMillis(transport.getPlannedStartOfTransport());
                String abfahrtTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <= 9 ? "0" : "")
                        + gcal.get(GregorianCalendar.HOUR_OF_DAY)
                        + ":"
                        + ((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") + gcal
                                .get(GregorianCalendar.MINUTE));
                this.textAbf.setText(abfahrtTime);
            }

            // time at patient
            if (transport.getPlannedTimeAtPatient() != 0) {
                gcal.setTimeInMillis(transport.getPlannedTimeAtPatient());
                String beiPatientTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <= 9 ? "0" : "")
                        + gcal.get(GregorianCalendar.HOUR_OF_DAY)
                        + ":"
                        + ((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") + gcal
                                .get(GregorianCalendar.MINUTE));
                this.textBeiPat.setText(beiPatientTime);
            }

            // time at destination
            if (transport.getAppointmentTimeAtDestination() != 0) {
                gcal.setTimeInMillis(transport.getAppointmentTimeAtDestination());
                String terminTime = (gcal.get(GregorianCalendar.HOUR_OF_DAY) <= 9 ? "0" : "")
                        + gcal.get(GregorianCalendar.HOUR_OF_DAY)
                        + ":"
                        + ((gcal.get(GregorianCalendar.MINUTE) <= 9 ? "0" : "") + gcal
                                .get(GregorianCalendar.MINUTE));
                this.textTermin.setText(terminTime);
            }

            // transport stati
            if (transport.getStatusMessages() != null) {
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED));
                    this.textAE.setText(sdf.format(cal.getTime()));
                }
                // Status 0
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY));
                    textS1.setText(sdf.format(cal.getTime()));
                }
                // Status 2
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_AT_PATIENT)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_AT_PATIENT));
                    textS2.setText(sdf.format(cal.getTime()));
                }
                // Status 3
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT));
                    textS3.setText(sdf.format(cal.getTime()));
                }
                // Status 4
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION));
                    textS4.setText(sdf.format(cal.getTime()));
                }
                // Status 5
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE));
                    textS5.setText(sdf.format(cal.getTime()));
                }
                // Status 6
                if (transport.getStatusMessages().containsKey(
                        ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION)) {
                    cal.setTimeInMillis(transport.getStatusMessages().get(
                            ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION));
                    textS6.setText(sdf.format(cal.getTime()));
                }
            }

            if (transport.getCreationTime() != 0) {
                cal.setTimeInMillis(transport.getCreationTime());
                textAufgen.setText(sdf.format(cal.getTime()));
            }

            // alarming timestamps
            if (transport.getTimestampNA() != 0) {
                cal.setTimeInMillis(transport.getTimestampNA());
                timestampNA.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampRTH() != 0) {
                cal.setTimeInMillis(transport.getTimestampRTH());
                timestampRTH.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampDF() != 0) {
                cal.setTimeInMillis(transport.getTimestampDF());
                timestampDF.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampBRKDT() != 0) {
                cal.setTimeInMillis(transport.getTimestampBRKDT());
                timestampBRKDT.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampFW() != 0) {
                cal.setTimeInMillis(transport.getTimestampFW());
                timestampFW.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampPolizei() != 0) {
                cal.setTimeInMillis(transport.getTimestampPolizei());
                timestampPolizei.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampBergrettung() != 0) {
                cal.setTimeInMillis(transport.getTimestampBergrettung());
                timestampBergrettung.setText(sdf_dateTime.format(cal.getTime()));
            }
            if (transport.getTimestampKIT() != 0) {
                cal.setTimeInMillis(transport.getTimestampKIT());
                timestampKIT.setText(sdf_dateTime.format(cal.getTime()));
            }

            // other fields
            this.begleitpersonButton.setSelection(transport.isAssistantPerson());
            this.bergrettungButton.setSelection(transport.isMountainRescueServiceAlarming());
            this.brkdtButton.setSelection(transport.isBrkdtAlarming());

            // the disease
            if (transport.getKindOfIllness() != null) {
                // the returned disease has no id so we query the managed
                // diseases and try to get the complete object :)
                String name = transport.getKindOfIllness().getDiseaseName();
                Disease disease = diseaseHandler.getDiseaseByName(name);
                if (disease != null) {
                    transport.setKindOfIllness(disease);
                    setErkrVerl.setSelection(new StructuredSelection(disease));
                }
            }

            if (transport.getPatient() != null) {
                this.patientLastName.setText(transport.getPatient().getLastname());
                this.patientFirstName.setText(transport.getPatient().getFirstname());
            }

            if (transport.getToCity() != null)
                textToCity.setText(transport.getToCity());

            if (transport.getToStreet() != null)
                textToStreet.setText(transport.getToStreet());

            // mandatory fields
            if (transport.getTransportPriority() != null) {
                oldPriority = transport.getTransportPriority();
                comboPrioritaet.setText(this.priorityToString(transport.getTransportPriority()));
            }

            textFromStreet.setText(transport.getFromStreet());

            if (transport.getPlanedLocation() != null)
                this.zustaendigeOrtsstelle.setSelection(new StructuredSelection(transport
                        .getPlanedLocation()));// mandatory!!
            // default: Bezirk
            if (transport.getFromCity() != null)
                textFromCity.setText(transport.getFromCity());

            this.dfButton.setSelection(transport.isDfAlarming());
            this.fernfahrtButton.setSelection(transport.isLongDistanceTrip());
            this.feuerwehrButton.setSelection(transport.isFirebrigadeAlarming());
            this.notarztButton.setSelection(transport.isEmergencyDoctorAlarming());
            this.polizeiButton.setSelection(transport.isPoliceAlarming());
            this.KITButton.setSelection(transport.isKITAlarming());
            this.rthButton.setSelection(transport.isHelicopterAlarming());
            this.ruecktransportMoeglichButton.setSelection(transport.isBackTransport());
            this.rufhilfepatientButton.setSelection(transport.isEmergencyPhone());
            this.bd2Button.setSelection(transport.isBlueLightToGoal());
            this.bd1Button.setSelection(transport.isBlueLight1());
            this.createdBy.setText(transport.getCreatedByUsername());

            if (transport.getDisposedByUsername() != null)
                disposedBy.setText(transport.getDisposedByUsername());

            if (transport.getNotes() != null) {
                this.textAnmerkungen.setText(transport.getNotes());
            }

            if (transport.getCallerDetail() != null) {
                if (transport.getCallerDetail().getCallerName() != null)
                    this.textAnrufer.setText(transport.getCallerDetail().getCallerName());
                if (transport.getCallerDetail().getCallerTelephoneNumber() != null)
                    this.textTelefonAnrufer.setText(transport.getCallerDetail()
                            .getCallerTelephoneNumber());
            }

            // the real station which did the transport
            if (transport.getVehicleDetail() != null) {

                this.textOrtsstelle.setText(transport.getVehicleDetail().getCurrentStation()
                        .getLocationName());
            }

            if (transport.getFeedback() != null)
                this.textRueckmeldung.setText(transport.getFeedback());

            if (transport.getTransportNumber() != 0)
                if (transport.getTransportNumber() == -1)
                    this.textTransportNummer.setText("STORNO");
                else if (transport.getTransportNumber() == -2)
                    this.textTransportNummer.setText("WTGL");
                else if (transport.getTransportNumber() == -4)
                    this.textTransportNummer.setText("NEF");
                else if (transport.getTransportNumber() != 0)
                    this.textTransportNummer
                            .setText(String.valueOf(transport.getTransportNumber()));

            // kind of transport
            if (transport.getKindOfTransport() != null)
                combokindOfTransport.setText(transport.getKindOfTransport());

            // directness
            int direction = transport.getDirection();
            if (IDirectness.TOWARDS_BRUCK == direction) {
                this.bruckButton.setSelection(true);
            }
            if (IDirectness.TOWARDS_GRAZ == direction) {
                this.grazButton.setSelection(true);
            }
            if (IDirectness.TOWARDS_LEOBEN == direction) {
                this.leobenButton.setSelection(true);
            }
            if (IDirectness.TOWARDS_MARIAZELL == direction) {
                this.mariazellButton.setSelection(true);
            }
            if (IDirectness.TOWARDS_VIENNA == direction) {
                this.wienButton.setSelection(true);
            }
            if (IDirectness.TOWARDS_KAPFENBERG == direction) {
                this.kapfenbergButton.setSelection(true);
            }

            if (transport.getVehicleDetail() != null) {
                if (transport.getVehicleDetail().getDriver() != null) {
                    this.setTextFahrer.setSelection(new StructuredSelection(transport
                            .getVehicleDetail().getDriver()));
                }
                if (transport.getVehicleDetail().getFirstParamedic() != null) {
                    this.setTextSaniI.setSelection(new StructuredSelection(transport
                            .getVehicleDetail().getFirstParamedic()));
                }
                if (transport.getVehicleDetail().getSecondParamedic() != null) {
                    this.setTextSaniII.setSelection(new StructuredSelection(transport
                            .getVehicleDetail().getSecondParamedic()));
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
    protected void okPressed() {
        handleOK();
    }

    /**
     * The user pressed the cancel button
     */
    @Override
    protected void cancelPressed() {
        boolean result = MessageDialog.openConfirm(getShell(), "Abbrechen",
                "Wollen Sie wirklich abbrechen?");
        // check the result
        if (result) {
            getShell().close();
        }
    }

    /**
     * Creates the planing section
     */
    private void createTransportSection(Composite parent) {
        client = new Composite(parent, SWT.NONE);
        client.setLayout(new FormLayout());
        // calendar
        dateTime = new DateTime(client, SWT.CALENDAR);
        final FormData fd_dateTime = new FormData();
        fd_dateTime.bottom = new FormAttachment(0, 160);
        fd_dateTime.top = new FormAttachment(0, 10);
        fd_dateTime.right = new FormAttachment(0, 187);
        fd_dateTime.left = new FormAttachment(0, 10);
        dateTime.setLayoutData(fd_dateTime);

        // group 'Transportdaten'
        transportdatenGroup = new Group(client, SWT.NONE);
        transportdatenGroup.setLayout(new FormLayout());
        final FormData fd_transportdatenGroup = new FormData();
        fd_transportdatenGroup.bottom = new FormAttachment(0, 160);
        fd_transportdatenGroup.top = new FormAttachment(0, 10);
        fd_transportdatenGroup.right = new FormAttachment(0, 1056);
        fd_transportdatenGroup.left = new FormAttachment(0, 194);
        transportdatenGroup.setLayoutData(fd_transportdatenGroup);
        transportdatenGroup.setForeground(CustomColors.COLOR_GREY);
        transportdatenGroup.setText("Transportdaten");

        final Label vonLabel = new Label(transportdatenGroup, SWT.NONE);
        vonLabel.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_vonLabel = new FormData();
        fd_vonLabel.bottom = new FormAttachment(0, 42);
        fd_vonLabel.top = new FormAttachment(0, 29);
        fd_vonLabel.right = new FormAttachment(0, 32);
        fd_vonLabel.left = new FormAttachment(0, 7);

        vonLabel.setLayoutData(fd_vonLabel);
        vonLabel.setForeground(new Color(Display.getCurrent(), 0, 0, 255));
        vonLabel.setText("von:");

        textToStreet = new Text(transportdatenGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
        final FormData fd_comboNachStrasse = new FormData();
        fd_comboNachStrasse.right = new FormAttachment(0, 260);
        fd_comboNachStrasse.bottom = new FormAttachment(0, 74);
        fd_comboNachStrasse.top = new FormAttachment(0, 53);
        fd_comboNachStrasse.left = new FormAttachment(0, 38);
        textToStreet.setLayoutData(fd_comboNachStrasse);
        textToStreet.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // no action when the enter key is pressed
                if (e.keyCode == 13) {
                    e.doit = false;
                    return;
                }
                // go to the next field when the tab is pressed
                if (e.keyCode == 9) {
                    textToCity.setFocus();
                    e.doit = false;
                    return;
                }
                inputChanged(textToStreet.getText(), IFilterTypes.SEARCH_STRING_STREET);
            }
        });
        acToStreet = new AutoCompleteField(textToStreet, new TextContentAdapter(), new String[] {});

        final Button buttonAddressFrom = new Button(transportdatenGroup, SWT.NONE);
        final FormData fd_buttonAddressFrom = new FormData();
        fd_buttonAddressFrom.bottom = new FormAttachment(0, 47);
        fd_buttonAddressFrom.top = new FormAttachment(0, 26);
        fd_buttonAddressFrom.right = new FormAttachment(0, 452);
        fd_buttonAddressFrom.left = new FormAttachment(0, 422);
        buttonAddressFrom.setLayoutData(fd_buttonAddressFrom);
        buttonAddressFrom.setText("...");
        buttonAddressFrom.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                // setup and prepare the initial value
                String initStreetValue = textFromStreet.getText();
                String initCityValue = textFromCity.getText();

                // open the selection dialog to choose a address
                AddressSelectionDialog selectionDialog = new AddressSelectionDialog(
                        initStreetValue, initCityValue, getShell());
                selectionDialog.open();
                Address selectedAddress = (Address) selectionDialog.getResult()[0];

                // assert valid
                if (selectedAddress == null)
                    return;

                // fill in the form fields
                if (selectedAddress.getStreet() != null)
                    textFromStreet.setText(selectedAddress.getStreet());
                if (selectedAddress.getCity() != null)
                    textFromCity.setText(selectedAddress.getCity());
            }
        });

        final Button buttonAddressTo = new Button(transportdatenGroup, SWT.NONE);
        final FormData fd_buttonAddressTo = new FormData();
        fd_buttonAddressTo.bottom = new FormAttachment(0, 74);
        fd_buttonAddressTo.top = new FormAttachment(0, 53);
        fd_buttonAddressTo.right = new FormAttachment(0, 452);
        fd_buttonAddressTo.left = new FormAttachment(0, 422);
        buttonAddressTo.setLayoutData(fd_buttonAddressTo);
        buttonAddressTo.setText("...");
        buttonAddressTo.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                // setup and prepare the initial value
                String initStreetValue = textToStreet.getText();
                String initCityValue = textToCity.getText();

                // open the selection dialog to choose a address
                AddressSelectionDialog selectionDialog = new AddressSelectionDialog(
                        initStreetValue, initCityValue, getShell());
                selectionDialog.open();
                Address selectedAddress = (Address) selectionDialog.getResult()[0];

                // assert valid
                if (selectedAddress == null)
                    return;

                // fill in the form fields
                if (selectedAddress.getStreet() != null)
                    textToStreet.setText(selectedAddress.getStreet());
                if (selectedAddress.getCity() != null)
                    textToCity.setText(selectedAddress.getCity());
            }
        });

        textFromStreet = new Text(transportdatenGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
        final FormData fd_comboVonStrasse = new FormData();
        fd_comboVonStrasse.right = new FormAttachment(0, 260);
        fd_comboVonStrasse.bottom = new FormAttachment(0, 47);
        fd_comboVonStrasse.top = new FormAttachment(0, 26);
        fd_comboVonStrasse.left = new FormAttachment(0, 38);
        textFromStreet.setLayoutData(fd_comboVonStrasse);
        textFromStreet.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // no action when the enter key is pressed
                if (e.keyCode == 13) {
                    e.doit = false;
                    return;
                }
                // go to the next field when the tab is pressed
                if (e.keyCode == 9) {
                    textFromCity.setFocus();
                    e.doit = false;
                    return;
                }
                inputChanged(textFromStreet.getText(), IFilterTypes.SEARCH_STRING_STREET);
            }
        });
        acFromStreet = new AutoCompleteField(textFromStreet, new TextContentAdapter(),
                new String[] {});

        final Label nachLabel = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_nachLabel = new FormData();
        fd_nachLabel.bottom = new FormAttachment(0, 69);
        fd_nachLabel.top = new FormAttachment(0, 56);
        fd_nachLabel.right = new FormAttachment(0, 32);
        fd_nachLabel.left = new FormAttachment(0, 7);
        nachLabel.setLayoutData(fd_nachLabel);
        nachLabel.setForeground(CustomColors.COLOR_GREY);
        nachLabel.setText("nach:");

        final Label label = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_label = new FormData();
        fd_label.bottom = new FormAttachment(0, 20);
        fd_label.top = new FormAttachment(0, 7);
        fd_label.right = new FormAttachment(0, 94);
        fd_label.left = new FormAttachment(0, 38);
        label.setLayoutData(fd_label);
        label.setForeground(CustomColors.COLOR_GREY);
        label.setText("Stra�e");

        textFromCity = new Text(transportdatenGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
        final FormData fd_comboVonOrt = new FormData();
        fd_comboVonOrt.left = new FormAttachment(0, 264);
        fd_comboVonOrt.bottom = new FormAttachment(0, 47);
        fd_comboVonOrt.top = new FormAttachment(0, 26);
        fd_comboVonOrt.right = new FormAttachment(0, 420);
        textFromCity.setLayoutData(fd_comboVonOrt);
        textFromCity.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // no action when the enter key is pressed
                if (e.keyCode == 13) {
                    e.doit = false;
                    return;
                }
                // go to the next field when the tab is pressed
                if (e.keyCode == 9) {
                    patientLastName.setFocus();
                    e.doit = false;
                    return;
                }
                inputChanged(textFromCity.getText(), IFilterTypes.SEARCH_STRING_CITY);
            }
        });
        acFromCity = new AutoCompleteField(textFromCity, new TextContentAdapter(), new String[] {});

        textToCity = new Text(transportdatenGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
        final FormData fd_comboNachOrt = new FormData();
        fd_comboNachOrt.left = new FormAttachment(0, 264);
        fd_comboNachOrt.bottom = new FormAttachment(0, 74);
        fd_comboNachOrt.top = new FormAttachment(0, 53);
        fd_comboNachOrt.right = new FormAttachment(0, 420);
        textToCity.setLayoutData(fd_comboNachOrt);
        textToCity.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // no action when the enter key is pressed
                if (e.keyCode == 13) {
                    e.doit = false;
                    return;
                }
                // go to the next field when the tab is pressed
                if (e.keyCode == 9) {
                    ruecktransportMoeglichButton.setFocus();
                    e.doit = false;
                    return;
                }
                inputChanged(textToCity.getText(), IFilterTypes.SEARCH_STRING_CITY);
            }
        });
        acToCity = new AutoCompleteField(textToCity, new TextContentAdapter(), new String[] {});

        final Label ortLabel = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_ortLabel = new FormData();
        fd_ortLabel.bottom = new FormAttachment(0, 20);
        fd_ortLabel.top = new FormAttachment(0, 7);
        fd_ortLabel.right = new FormAttachment(0, 344);
        fd_ortLabel.left = new FormAttachment(0, 319);
        ortLabel.setLayoutData(fd_ortLabel);
        ortLabel.setForeground(CustomColors.COLOR_GREY);
        ortLabel.setText("Ort");

        patientLastName = new Text(transportdatenGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
        final FormData fd_comboNachname = new FormData();
        fd_comboNachname.bottom = new FormAttachment(0, 47);
        fd_comboNachname.top = new FormAttachment(0, 26);
        fd_comboNachname.right = new FormAttachment(0, 635);
        fd_comboNachname.left = new FormAttachment(0, 464);
        patientLastName.setLayoutData(fd_comboNachname);
        patientLastName.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // no action when the enter key is pressed
                if (e.keyCode == 13) {
                    e.doit = false;
                    return;
                }
                // go to the next field when the tab is pressed
                if (e.keyCode == 9) {
                    patientFirstName.setFocus();
                    e.doit = false;
                    return;
                }
            }
        });

        final Label nachnameLabel = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_nachnameLabel = new FormData();
        fd_nachnameLabel.bottom = new FormAttachment(0, 20);
        fd_nachnameLabel.top = new FormAttachment(0, 7);
        fd_nachnameLabel.right = new FormAttachment(0, 520);
        fd_nachnameLabel.left = new FormAttachment(0, 464);
        nachnameLabel.setLayoutData(fd_nachnameLabel);
        nachnameLabel.setForeground(CustomColors.COLOR_GREY);
        nachnameLabel.setText("Nachname");

        patientFirstName = new Text(transportdatenGroup, SWT.WRAP | SWT.MULTI | SWT.BORDER);
        final FormData fd_vornameVorname = new FormData();
        fd_vornameVorname.bottom = new FormAttachment(0, 47);
        fd_vornameVorname.top = new FormAttachment(0, 26);
        fd_vornameVorname.right = new FormAttachment(0, 812);
        fd_vornameVorname.left = new FormAttachment(0, 641);
        patientFirstName.setLayoutData(fd_vornameVorname);
        patientFirstName.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                // no action when the enter key is pressed
                if (e.keyCode == 13) {
                    e.doit = false;
                    return;
                }
                // go to the next field when the tab is pressed
                if (e.keyCode == 9) {
                    combokindOfTransport.setFocus();
                    e.doit = false;
                    return;
                }
            }
        });

        final Button buttonPatientendatenPruefen = new Button(transportdatenGroup, SWT.NONE);
        final FormData fd_buttonPatientendatenPruefen = new FormData();
        fd_buttonPatientendatenPruefen.bottom = new FormAttachment(0, 47);
        fd_buttonPatientendatenPruefen.top = new FormAttachment(0, 24);
        fd_buttonPatientendatenPruefen.right = new FormAttachment(0, 850);
        fd_buttonPatientendatenPruefen.left = new FormAttachment(0, 818);
        buttonPatientendatenPruefen.setLayoutData(fd_buttonPatientendatenPruefen);
        buttonPatientendatenPruefen.setText("...");
        buttonPatientendatenPruefen.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                if (Login.AUTH_ADMIN.equalsIgnoreCase(authorization)
                        && "journal".equalsIgnoreCase(editingType)) {
                    if (patientLastName.getText() == null
                            || patientLastName.getText().trim().isEmpty()) {
                        // getShell().getDisplay().beep();
                        setErrorMessage("Der Nachname kann beim Anlegen eines Patienten in der Patientendatenbank nicht leer gelassen werden.");
                        return;
                    }

                    // confirm the cancel
                    boolean cancelConfirmed = MessageDialog.openQuestion(PlatformUI.getWorkbench()
                            .getActiveWorkbenchWindow().getShell(), "Patient hinzuf�gen",
                            "M�chten Sie diesen Patienten zur Patientendatenbank hinzuf�gen?");
                    if (!cancelConfirmed)
                        return;

                    SickPerson person = new SickPerson();
                    person.setLastName(patientLastName.getText());
                    person.setFirstName(patientFirstName.getText());
                    person.setCityname(textFromCity.getText());
                    person.setStreetname(textFromStreet.getText());
                    person.setKindOfTransport(combokindOfTransport.getText());

                    AddMessage<SickPerson> addMessage = new AddMessage<SickPerson>(person);
                    NetWrapper.sendMessage(addMessage);
                } else {
                    // open the selection dialog to choose a patient
                    PatientSelectionDialog selectionDialog = new PatientSelectionDialog(
                            patientLastName.getText(), getShell());
                    selectionDialog.open();
                    SickPerson selectedPerson = (SickPerson) selectionDialog.getResult()[0];

                    // assert valid
                    if (selectedPerson == null)
                        return;

                    if (selectedPerson.getFirstName() != null)
                        patientFirstName.setText(selectedPerson.getFirstName());
                    if (selectedPerson.getLastName() != null)
                        patientLastName.setText(selectedPerson.getLastName());
                    if (selectedPerson.getStreetname() != null)
                        textFromStreet.setText(selectedPerson.getStreetname());
                    if (selectedPerson.getCityname() != null)
                        textFromCity.setText(selectedPerson.getCityname());
                    if (selectedPerson.getKindOfTransport() != null)
                        combokindOfTransport.setText(selectedPerson.getKindOfTransport());
                    if (selectedPerson.getNotes() != null)
                        textAnmerkungen.setText(selectedPerson.getNotes());
                }
            }
        });

        // no sick person editing from the journal transport form if the user is
        // a normal user
        if ("journal".equalsIgnoreCase(editingType)) {
            buttonPatientendatenPruefen.setEnabled(false);
        }

        if (Login.AUTH_ADMIN.equalsIgnoreCase(authorization)
                && "journal".equalsIgnoreCase(editingType)) {
            buttonPatientendatenPruefen.setImage(imageRegistry.get("admin.patientAdd"));
            buttonPatientendatenPruefen.setEnabled(true);
        }

        final Label nachnameLabel_1 = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_nachnameLabel_1 = new FormData();
        fd_nachnameLabel_1.bottom = new FormAttachment(0, 20);
        fd_nachnameLabel_1.top = new FormAttachment(0, 7);
        fd_nachnameLabel_1.right = new FormAttachment(0, 697);
        fd_nachnameLabel_1.left = new FormAttachment(0, 641);
        nachnameLabel_1.setLayoutData(fd_nachnameLabel_1);
        nachnameLabel_1.setForeground(CustomColors.COLOR_GREY);
        nachnameLabel_1.setText("Vorname");

        final Label label_kind = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_label_kind = new FormData();
        fd_label_kind.left = new FormAttachment(0, 680);
        fd_label_kind.bottom = new FormAttachment(0, 69);
        fd_label_kind.top = new FormAttachment(0, 56);
        fd_label_kind.right = new FormAttachment(0, 752);
        label_kind.setLayoutData(fd_label_kind);
        label_kind.setText("Transportart:");

        combokindOfTransport = new Combo(transportdatenGroup, SWT.READ_ONLY);
        combokindOfTransport.setItems(IKindOfTransport.KINDS);

        final FormData fd_comboTransportKind = new FormData();
        fd_comboTransportKind.bottom = new FormAttachment(0, 74);
        fd_comboTransportKind.top = new FormAttachment(0, 53);
        fd_comboTransportKind.right = new FormAttachment(0, 850);
        fd_comboTransportKind.left = new FormAttachment(0, 753);
        combokindOfTransport.setLayoutData(fd_comboTransportKind);

        begleitpersonButton = new Button(transportdatenGroup, SWT.CHECK);
        final FormData fd_begleitpersonButton = new FormData();
        fd_begleitpersonButton.bottom = new FormAttachment(0, 116);
        fd_begleitpersonButton.top = new FormAttachment(0, 100);
        fd_begleitpersonButton.right = new FormAttachment(0, 583);
        fd_begleitpersonButton.left = new FormAttachment(0, 462);
        begleitpersonButton.setLayoutData(fd_begleitpersonButton);
        begleitpersonButton.setToolTipText("Begleitperson");
        begleitpersonButton.setText("Begleitperson");

        ruecktransportMoeglichButton = new Button(transportdatenGroup, SWT.CHECK);
        final FormData fd_button_1 = new FormData();
        fd_button_1.bottom = new FormAttachment(0, 96);
        fd_button_1.top = new FormAttachment(0, 80);
        fd_button_1.right = new FormAttachment(0, 159);
        fd_button_1.left = new FormAttachment(0, 38);
        ruecktransportMoeglichButton.setLayoutData(fd_button_1);
        ruecktransportMoeglichButton.setText("R�cktransport m�glich");

        final Label anruferLabel = new Label(transportdatenGroup, SWT.NONE);
        final FormData fd_anruferLabel = new FormData();
        fd_anruferLabel.bottom = new FormAttachment(0, 95);
        fd_anruferLabel.top = new FormAttachment(0, 82);
        fd_anruferLabel.right = new FormAttachment(0, 657);
        fd_anruferLabel.left = new FormAttachment(0, 610);
        anruferLabel.setLayoutData(fd_anruferLabel);
        anruferLabel.setForeground(CustomColors.COLOR_GREY);
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
        telefonLabel.setForeground(CustomColors.COLOR_GREY);
        telefonLabel.setText("Telefon:");

        textTelefonAnrufer = new Text(transportdatenGroup, SWT.BORDER);
        final FormData fd_textTelefonAnrufer = new FormData();
        fd_textTelefonAnrufer.bottom = new FormAttachment(0, 120);
        fd_textTelefonAnrufer.top = new FormAttachment(0, 99);
        fd_textTelefonAnrufer.right = new FormAttachment(0, 850);
        fd_textTelefonAnrufer.left = new FormAttachment(0, 663);
        textTelefonAnrufer.setLayoutData(fd_textTelefonAnrufer);

        rufhilfepatientButton = new Button(transportdatenGroup, SWT.CHECK);
        final FormData fd_rufhilfepatientButton = new FormData();
        fd_rufhilfepatientButton.bottom = new FormAttachment(0, 96);
        fd_rufhilfepatientButton.top = new FormAttachment(0, 80);
        fd_rufhilfepatientButton.right = new FormAttachment(0, 547);
        fd_rufhilfepatientButton.left = new FormAttachment(0, 462);
        rufhilfepatientButton.setLayoutData(fd_rufhilfepatientButton);
        rufhilfepatientButton.setText("Rufhilfepatient");

        final Label label_6 = new Label(transportdatenGroup, SWT.NONE);
        label_6.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_label_6 = new FormData();
        fd_label_6.bottom = new FormAttachment(0, 122);
        fd_label_6.top = new FormAttachment(0, 105);
        fd_label_6.right = new FormAttachment(0, 315);
        fd_label_6.left = new FormAttachment(0, 200);
        label_6.setLayoutData(fd_label_6);
        label_6.setForeground(CustomColors.COLOR_BLUE);
        label_6.setText("Zust�ndige Ortsstelle:");

        Combo comboZustaendigeOrtsstelle = new Combo(transportdatenGroup, SWT.READ_ONLY);
        zustaendigeOrtsstelle = new ComboViewer(comboZustaendigeOrtsstelle);
        zustaendigeOrtsstelle.setContentProvider(new StationContentProvider());
        zustaendigeOrtsstelle.setLabelProvider(new StationLabelProvider());
        zustaendigeOrtsstelle.setInput(locationHandler.toArray());

        final FormData fd_comboZustaendigeOrtsstelle = new FormData();
        fd_comboZustaendigeOrtsstelle.bottom = new FormAttachment(0, 121);
        fd_comboZustaendigeOrtsstelle.top = new FormAttachment(0, 100);
        fd_comboZustaendigeOrtsstelle.right = new FormAttachment(0, 431);
        fd_comboZustaendigeOrtsstelle.left = new FormAttachment(0, 319);
        comboZustaendigeOrtsstelle.setLayoutData(fd_comboZustaendigeOrtsstelle);

        transportdatenGroup.setTabList(new Control[] { textFromStreet, textFromCity,
                patientLastName, patientFirstName, combokindOfTransport, textToStreet, textToCity,
                ruecktransportMoeglichButton, rufhilfepatientButton, begleitpersonButton,
                textAnrufer, textTelefonAnrufer, comboZustaendigeOrtsstelle });

        planungGroup = new Group(client, SWT.NONE);
        planungGroup.setLayout(new FormLayout());
        final FormData fd_planungGroup = new FormData();
        fd_planungGroup.bottom = new FormAttachment(0, 348);
        fd_planungGroup.top = new FormAttachment(0, 166);
        fd_planungGroup.right = new FormAttachment(0, 187);
        fd_planungGroup.left = new FormAttachment(0, 10);
        planungGroup.setLayoutData(fd_planungGroup);
        planungGroup.setText("Zeiten/Richtung");

        // group 'Zeiten/Richtung'
        final Label abfLabel = new Label(planungGroup, SWT.NONE);
        abfLabel.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_abfLabel = new FormData();
        fd_abfLabel.bottom = new FormAttachment(0, 37);
        fd_abfLabel.top = new FormAttachment(0, 24);
        fd_abfLabel.right = new FormAttachment(0, 32);
        fd_abfLabel.left = new FormAttachment(0, 7);
        abfLabel.setLayoutData(fd_abfLabel);
        abfLabel.setForeground(CustomColors.COLOR_BLUE);
        abfLabel.setText("Abf:");

        final Label beiPatLabel = new Label(planungGroup, SWT.NONE);
        final FormData fd_beiPatLabel = new FormData();
        fd_beiPatLabel.bottom = new FormAttachment(0, 64);
        fd_beiPatLabel.top = new FormAttachment(0, 51);
        fd_beiPatLabel.right = new FormAttachment(0, 32);
        fd_beiPatLabel.left = new FormAttachment(0, 7);
        beiPatLabel.setLayoutData(fd_beiPatLabel);
        beiPatLabel.setForeground(CustomColors.COLOR_GREY);
        beiPatLabel.setText("Pat.:");

        final Label terminLabel = new Label(planungGroup, SWT.NONE);
        final FormData fd_terminLabel = new FormData();
        fd_terminLabel.bottom = new FormAttachment(0, 91);
        fd_terminLabel.top = new FormAttachment(0, 78);
        fd_terminLabel.right = new FormAttachment(0, 35);
        fd_terminLabel.left = new FormAttachment(0, 7);
        terminLabel.setLayoutData(fd_terminLabel);
        terminLabel.setForeground(CustomColors.COLOR_GREY);
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

        // 'Richtung'
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
        fernfahrtButton.setText("Fernfahrt");
        fernfahrtButton.setToolTipText("Fernfahrten sind lt. RKT deklariert");
        planungGroup.setTabList(new Control[] { textAbf, textBeiPat, textTermin, fernfahrtButton,
                bruckButton, kapfenbergButton, grazButton, leobenButton, wienButton,
                mariazellButton });

        // group 'Patientenzustand'
        patientenzustandGroup = new Group(client, SWT.NONE);
        patientenzustandGroup.setLayout(new FormLayout());
        final FormData fd_patientenzustandGroup = new FormData();
        fd_patientenzustandGroup.bottom = new FormAttachment(0, 348);
        fd_patientenzustandGroup.top = new FormAttachment(0, 166);
        fd_patientenzustandGroup.right = new FormAttachment(0, 870);
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
        setErkrVerl.setInput(diseaseHandler.toArray());

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
        erkrankungverletzungLabel.setForeground(CustomColors.COLOR_GREY);
        erkrankungverletzungLabel.setText("Erkrankung/Verletzung");

        final Label anmerkungenLabel = new Label(patientenzustandGroup, SWT.NONE);
        final FormData fd_anmerkungenLabel = new FormData();
        fd_anmerkungenLabel.bottom = new FormAttachment(0, 69);
        fd_anmerkungenLabel.top = new FormAttachment(0, 56);
        fd_anmerkungenLabel.right = new FormAttachment(0, 134);
        fd_anmerkungenLabel.left = new FormAttachment(0, 7);
        anmerkungenLabel.setLayoutData(fd_anmerkungenLabel);
        anmerkungenLabel.setForeground(CustomColors.COLOR_GREY);
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
        label_3.setForeground(CustomColors.COLOR_GREY);
        label_3.setText("R�ckmeldung");

        bd2Button = new Button(patientenzustandGroup, SWT.CHECK);
        final FormData fd_bd2Button = new FormData();
        fd_bd2Button.bottom = new FormAttachment(0, 25);
        fd_bd2Button.top = new FormAttachment(0, 9);
        fd_bd2Button.right = new FormAttachment(0, 666);
        fd_bd2Button.left = new FormAttachment(0, 623);
        bd2Button.setLayoutData(fd_bd2Button);
        bd2Button.setToolTipText("Sondersignal auf dem Weg zum Transportziel");
        bd2Button.setText("BD 2");

        bd1Button = new Button(patientenzustandGroup, SWT.CHECK);
        final FormData fd_bd1Button = new FormData();
        fd_bd1Button.bottom = new FormAttachment(0, 25);
        fd_bd1Button.top = new FormAttachment(0, 9);
        fd_bd1Button.right = new FormAttachment(0, 500);
        fd_bd1Button.left = new FormAttachment(0, 253);
        bd1Button.setLayoutData(fd_bd1Button);
        bd1Button.setToolTipText("Sondersignal auf dem Weg zum Einsatzort");
        bd1Button.setText("BD 1");

        comboPrioritaet = new Combo(patientenzustandGroup, SWT.READ_ONLY);
        comboPrioritaet
                .setToolTipText("1 (NEF), 2 (Transport), 3 (Terminfahrt), 4 (R�cktransport), 5 (Heimtransport), 6 (Sonstiges), 7 (NEF extern)");

        // set possible priorities
        if (transportType.equalsIgnoreCase("prebooking")) {
            comboPrioritaet.setItems(prebookingPriorities);
        }
        if (transportType.equalsIgnoreCase("emergencyTransport")
                || transportType.equalsIgnoreCase("both")) {
            comboPrioritaet.setItems(emergencyAndTransportPriorities);
        }
        final FormData fd_comboPrioritaet = new FormData();
        fd_comboPrioritaet.bottom = new FormAttachment(0, 73);
        fd_comboPrioritaet.top = new FormAttachment(0, 52);
        fd_comboPrioritaet.right = new FormAttachment(0, 287);
        fd_comboPrioritaet.left = new FormAttachment(0, 193);
        comboPrioritaet.setLayoutData(fd_comboPrioritaet);
        comboPrioritaet.addSelectionListener(new SelectionAdapter() {

            int index;

            public void widgetSelected(final SelectionEvent e) {
                // set possible priorities
                index = comboPrioritaet.getSelectionIndex();
                if (index != -1)
                    tmpPriority = comboPrioritaet.getItem(index);
                // automatically set bd1 and bd2 if the priority 1 NEF (A) is
                // choosen

                if (tmpPriority.equalsIgnoreCase("1 NEF")) {
                    bd1Button.setSelection(true);
                    bd2Button.setSelection(true);
                } else {
                    bd1Button.setSelection(false);
                    bd2Button.setSelection(false);
                }
            }
        });

        final Label label_4 = new Label(patientenzustandGroup, SWT.NONE);
        label_4.setFont(CustomColors.SUBHEADER_FONT);
        final FormData fd_label_4 = new FormData();
        fd_label_4.left = new FormAttachment(0, 135);
        fd_label_4.bottom = new FormAttachment(0, 69);
        fd_label_4.top = new FormAttachment(0, 56);
        fd_label_4.right = new FormAttachment(0, 255);
        label_4.setLayoutData(fd_label_4);
        label_4.setForeground(CustomColors.COLOR_BLUE);
        label_4.setText("Priorit�t:");
        patientenzustandGroup.setTabList(new Control[] { setErkrVerl.getControl(), bd1Button,
                comboPrioritaet, textAnmerkungen, textRueckmeldung, bd2Button });

        // group multi transport (only visible if the multi transport button was
        // pressed
        multiTransportGroup = new Group(client, SWT.NONE);
        multiTransportGroup.setLayout(new FormLayout());
        final FormData fd_multitransportGroup = new FormData();
        fd_multitransportGroup.right = new FormAttachment(0, 842);
        fd_multitransportGroup.bottom = new FormAttachment(0, 500);
        fd_multitransportGroup.top = new FormAttachment(0, 360);
        fd_multitransportGroup.left = new FormAttachment(0, 10);
        multiTransportGroup.setLayoutData(fd_multitransportGroup);
        multiTransportGroup.setText("Mehrfachtransport");
        multiTransportGroup.setVisible(false);
        createMultiTransportTable();

        // assign car group
        assignCarGroup = new Group(client, SWT.NONE);
        assignCarGroup.setLayout(new FormLayout());
        final FormData fd_assignCarGroup = new FormData();
        fd_assignCarGroup.right = new FormAttachment(0, 842);
        fd_assignCarGroup.bottom = new FormAttachment(0, 500);
        fd_assignCarGroup.top = new FormAttachment(0, 360);
        fd_assignCarGroup.left = new FormAttachment(0, 10);
        assignCarGroup.setLayoutData(fd_assignCarGroup);
        assignCarGroup.setText("Fahrzeug zuweisen");
        assignCarGroup.setVisible(false);
        createAssignCarTable();

        // group 'Alarmierung'
        planungGroup_1 = new Group(client, SWT.NONE);
        planungGroup_1.setLayout(new FormLayout());
        final FormData fd_planungGroup_1 = new FormData();
        fd_planungGroup_1.bottom = new FormAttachment(0, 348);
        fd_planungGroup_1.top = new FormAttachment(0, 166);
        fd_planungGroup_1.right = new FormAttachment(0, 1056);
        fd_planungGroup_1.left = new FormAttachment(0, 875);
        planungGroup_1.setLayoutData(fd_planungGroup_1);
        planungGroup_1.setText("Alarmierung");

        notarztButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_notarztButton = new FormData();
        fd_notarztButton.bottom = new FormAttachment(0, 19);
        fd_notarztButton.top = new FormAttachment(0, 3);
        fd_notarztButton.right = new FormAttachment(0, 88);
        fd_notarztButton.left = new FormAttachment(0, 5);
        notarztButton.setLayoutData(fd_notarztButton);
        notarztButton.setText("NA extern");
        notarztButton.setToolTipText("Externer! Notarzt f�r diesen Transport alarmiert");
        notarztButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (notarztButton.getSelection())
                    timestampNA.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampNA.setText("");
            }
        });

        rthButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_rthButton = new FormData();
        fd_rthButton.bottom = new FormAttachment(0, 40);
        fd_rthButton.top = new FormAttachment(0, 24);
        fd_rthButton.right = new FormAttachment(0, 88);
        fd_rthButton.left = new FormAttachment(0, 5);
        rthButton.setLayoutData(fd_rthButton);
        rthButton.setToolTipText("Hubschrauber");
        rthButton.setText("RTH");
        rthButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (rthButton.getSelection())
                    timestampRTH.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampRTH.setText("");
            }
        });

        dfButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_dfButton = new FormData();
        fd_dfButton.bottom = new FormAttachment(0, 61);
        fd_dfButton.top = new FormAttachment(0, 45);
        fd_dfButton.right = new FormAttachment(0, 88);
        fd_dfButton.left = new FormAttachment(0, 5);
        dfButton.setLayoutData(fd_dfButton);
        dfButton.setText("DF/Inspektion");
        dfButton.setToolTipText("DF/Inspektionsdienst");
        dfButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (dfButton.getSelection())
                    timestampDF.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampDF.setText("");
            }
        });

        brkdtButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_brkdtButton = new FormData();
        fd_brkdtButton.bottom = new FormAttachment(0, 82);
        fd_brkdtButton.top = new FormAttachment(0, 66);
        fd_brkdtButton.right = new FormAttachment(0, 88);
        fd_brkdtButton.left = new FormAttachment(0, 5);
        brkdtButton.setLayoutData(fd_brkdtButton);
        brkdtButton.setToolTipText("Bezirksrettungskommandant");
        brkdtButton.setText("BRKDT");
        brkdtButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (brkdtButton.getSelection())
                    timestampBRKDT.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampBRKDT.setText("");
            }
        });

        feuerwehrButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_feuerwehrButton = new FormData();
        fd_feuerwehrButton.bottom = new FormAttachment(0, 103);
        fd_feuerwehrButton.top = new FormAttachment(0, 87);
        fd_feuerwehrButton.right = new FormAttachment(0, 88);
        fd_feuerwehrButton.left = new FormAttachment(0, 5);
        feuerwehrButton.setLayoutData(fd_feuerwehrButton);
        feuerwehrButton.setToolTipText("Feuerwehr");
        feuerwehrButton.setText("FW");
        feuerwehrButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (feuerwehrButton.getSelection())
                    timestampFW.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampFW.setText("");
            }
        });

        polizeiButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_polizeiButton = new FormData();
        fd_polizeiButton.bottom = new FormAttachment(0, 124);
        fd_polizeiButton.top = new FormAttachment(0, 108);
        fd_polizeiButton.right = new FormAttachment(0, 88);
        fd_polizeiButton.left = new FormAttachment(0, 5);
        polizeiButton.setLayoutData(fd_polizeiButton);
        polizeiButton.setToolTipText("Polizei");
        polizeiButton.setText("Polizei");
        polizeiButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (polizeiButton.getSelection())
                    timestampPolizei.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampPolizei.setText("");
            }
        });

        KITButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_KITButton = new FormData();
        fd_KITButton.bottom = new FormAttachment(0, 166);
        fd_KITButton.top = new FormAttachment(0, 150);
        fd_KITButton.right = new FormAttachment(0, 88);
        fd_KITButton.left = new FormAttachment(0, 5);
        KITButton.setLayoutData(fd_KITButton);
        KITButton.setToolTipText("Kriseninterventionsteam");
        KITButton.setText("KIT");
        KITButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (KITButton.getSelection())
                    timestampKIT.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampKIT.setText("");
            }
        });

        bergrettungButton = new Button(planungGroup_1, SWT.CHECK);
        final FormData fd_bergrettungButton = new FormData();
        fd_bergrettungButton.bottom = new FormAttachment(0, 145);
        fd_bergrettungButton.top = new FormAttachment(0, 129);
        fd_bergrettungButton.right = new FormAttachment(0, 88);
        fd_bergrettungButton.left = new FormAttachment(0, 5);
        bergrettungButton.setLayoutData(fd_bergrettungButton);
        bergrettungButton.setText("Bergrettung");
        bergrettungButton.setToolTipText("Bergrettung");
        bergrettungButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                SimpleDateFormat sdf_dateTime = new SimpleDateFormat("dd.MM.yy HH:mm");
                Calendar cal = Calendar.getInstance();
                if (bergrettungButton.getSelection())
                    timestampBergrettung.setText(sdf_dateTime.format(cal.getTime()));
                else
                    timestampBergrettung.setText("");
            }
        });

        timestampNA = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampNA = new FormData();
        fd_timestampNA.bottom = new FormAttachment(0, 19);
        fd_timestampNA.top = new FormAttachment(0, 3);
        fd_timestampNA.right = new FormAttachment(0, 175);
        fd_timestampNA.left = new FormAttachment(0, 89);
        timestampNA.setLayoutData(fd_timestampNA);
        timestampNA.setToolTipText("Zeitpunkt des letzten Anhakens des Notarzt-H�kchens");

        timestampRTH = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampRTH = new FormData();
        fd_timestampRTH.bottom = new FormAttachment(0, 40);
        fd_timestampRTH.top = new FormAttachment(0, 24);
        fd_timestampRTH.right = new FormAttachment(0, 175);
        fd_timestampRTH.left = new FormAttachment(0, 89);
        timestampRTH.setLayoutData(fd_timestampRTH);
        timestampRTH.setToolTipText("Zeitpunkt des letzten Anhakens des Hubschrauber-H�kchens");

        timestampDF = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampDF = new FormData();
        fd_timestampDF.bottom = new FormAttachment(0, 61);
        fd_timestampDF.top = new FormAttachment(0, 45);
        fd_timestampDF.right = new FormAttachment(0, 175);
        fd_timestampDF.left = new FormAttachment(0, 89);
        timestampDF.setLayoutData(fd_timestampDF);
        timestampDF
                .setToolTipText("Zeitpunkt des letzten Anhakens des Dienstf�hrenden/Insp.-H�kchens");

        timestampBRKDT = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampBRKDT = new FormData();
        fd_timestampBRKDT.bottom = new FormAttachment(0, 82);
        fd_timestampBRKDT.top = new FormAttachment(0, 66);
        fd_timestampBRKDT.right = new FormAttachment(0, 175);
        fd_timestampBRKDT.left = new FormAttachment(0, 89);
        timestampBRKDT.setLayoutData(fd_timestampBRKDT);
        timestampBRKDT
                .setToolTipText("Zeitpunkt des letzten Anhakens des Bezirksrettungskommandanten-H�kchens");

        timestampFW = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampFW = new FormData();
        fd_timestampFW.bottom = new FormAttachment(0, 103);
        fd_timestampFW.top = new FormAttachment(0, 87);
        fd_timestampFW.right = new FormAttachment(0, 175);
        fd_timestampFW.left = new FormAttachment(0, 89);
        timestampFW.setLayoutData(fd_timestampFW);
        timestampFW.setToolTipText("Zeitpunkt des letzten Anhakens des Feuerwehr-H�kchens");

        timestampPolizei = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampPolizei = new FormData();
        fd_timestampPolizei.bottom = new FormAttachment(0, 124);
        fd_timestampPolizei.top = new FormAttachment(0, 108);
        fd_timestampPolizei.right = new FormAttachment(0, 175);
        fd_timestampPolizei.left = new FormAttachment(0, 89);
        timestampPolizei.setLayoutData(fd_timestampPolizei);
        timestampPolizei.setToolTipText("Zeitpunkt des letzten Anhakens des Polizei-H�kchens");

        timestampBergrettung = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampBergrettung = new FormData();
        fd_timestampBergrettung.bottom = new FormAttachment(0, 145);
        fd_timestampBergrettung.top = new FormAttachment(0, 129);
        fd_timestampBergrettung.right = new FormAttachment(0, 175);
        fd_timestampBergrettung.left = new FormAttachment(0, 89);
        timestampBergrettung.setLayoutData(fd_timestampBergrettung);
        timestampBergrettung
                .setToolTipText("Zeitpunkt des letzten Anhakens des Bergrettung-H�kchens");

        timestampKIT = new Text(planungGroup_1, SWT.BORDER);
        final FormData fd_timestampKIT = new FormData();
        fd_timestampKIT.bottom = new FormAttachment(0, 166);
        fd_timestampKIT.top = new FormAttachment(0, 150);
        fd_timestampKIT.right = new FormAttachment(0, 175);
        fd_timestampKIT.left = new FormAttachment(0, 89);
        timestampKIT.setLayoutData(fd_timestampKIT);
        timestampKIT.setToolTipText("Zeitpunkt des letzten Anhakens des KIT-H�kchens");

        timestampNA.setEditable(false);
        timestampRTH.setEditable(false);
        timestampDF.setEditable(false);
        timestampBRKDT.setEditable(false);
        timestampFW.setEditable(false);
        timestampPolizei.setEditable(false);
        timestampBergrettung.setEditable(false);
        timestampKIT.setEditable(false);

        planungGroup_1.setTabList(new Control[] { notarztButton, rthButton, dfButton, brkdtButton,
                feuerwehrButton, polizeiButton, bergrettungButton, KITButton });

        final Label label_5 = new Label(client, SWT.SEPARATOR | SWT.HORIZONTAL);
        final FormData fd_label_5 = new FormData();
        fd_label_5.bottom = new FormAttachment(0, 367);
        fd_label_5.top = new FormAttachment(0, 354);
        fd_label_5.right = new FormAttachment(0, 1056);
        fd_label_5.left = new FormAttachment(0, 10);
        label_5.setLayoutData(fd_label_5);

        // group 'Transportdetails'
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
        transportnumemmerLabel.setForeground(CustomColors.COLOR_GREY);
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
        ortsstelleLabel.setForeground(CustomColors.COLOR_DARK_BLUE);
        ortsstelleLabel.setText("Ortsstelle:");

        textOrtsstelle = new Text(transportdetailsGroup, SWT.BORDER);
        final FormData fd_textOrtsstelle = new FormData();
        fd_textOrtsstelle.bottom = new FormAttachment(0, 59);
        fd_textOrtsstelle.top = new FormAttachment(0, 38);
        fd_textOrtsstelle.right = new FormAttachment(0, 158);
        fd_textOrtsstelle.left = new FormAttachment(0, 60);
        textOrtsstelle.setLayoutData(fd_textOrtsstelle);
        textOrtsstelle.setEditable(false);

        textFahrzeug = new Text(transportdetailsGroup, SWT.BORDER);
        final FormData fd_textFahrzeug = new FormData();
        fd_textFahrzeug.bottom = new FormAttachment(0, 86);
        fd_textFahrzeug.top = new FormAttachment(0, 65);
        fd_textFahrzeug.right = new FormAttachment(0, 158);
        fd_textFahrzeug.left = new FormAttachment(0, 60);
        textFahrzeug.setLayoutData(fd_textFahrzeug);
        textFahrzeug.setEditable(false);

        final Label farzeugLabel = new Label(transportdetailsGroup, SWT.NONE);
        final FormData fd_farzeugLabel = new FormData();
        fd_farzeugLabel.bottom = new FormAttachment(0, 81);
        fd_farzeugLabel.top = new FormAttachment(0, 68);
        fd_farzeugLabel.right = new FormAttachment(0, 57);
        fd_farzeugLabel.left = new FormAttachment(0, 7);
        farzeugLabel.setLayoutData(fd_farzeugLabel);
        farzeugLabel.setForeground(CustomColors.COLOR_GREY);
        farzeugLabel.setText("Fahrzeug:");
        transportdetailsGroup.setTabList(new Control[] { textTransportNummer, textOrtsstelle,
                textFahrzeug });

        // group 'Personal am Fahrzeug'
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
        setTextFahrer.setInput(staffHandler.toArray());

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
        setTextSaniI.setInput(staffHandler.toArray());

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
        setTextSaniII.setInput(staffHandler.toArray());

        final Label driverLabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
        final FormData fd_driverLabel = new FormData();
        fd_driverLabel.bottom = new FormAttachment(0, 27);
        fd_driverLabel.top = new FormAttachment(0, 14);
        fd_driverLabel.right = new FormAttachment(0, 54);
        fd_driverLabel.left = new FormAttachment(0, 7);
        driverLabel.setLayoutData(fd_driverLabel);
        driverLabel.setForeground(CustomColors.COLOR_GREY);
        driverLabel.setText("Fahrer:");

        final Label paramedicILabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
        final FormData fd_paramedicILabel = new FormData();
        fd_paramedicILabel.bottom = new FormAttachment(0, 54);
        fd_paramedicILabel.top = new FormAttachment(0, 41);
        fd_paramedicILabel.right = new FormAttachment(0, 68);
        fd_paramedicILabel.left = new FormAttachment(0, 7);
        paramedicILabel.setLayoutData(fd_paramedicILabel);
        paramedicILabel.setForeground(CustomColors.COLOR_GREY);
        paramedicILabel.setText("Sanit�ter I:");

        final Label paramedicIILabel = new Label(personalAmFahrzeugGroup, SWT.NONE);
        final FormData fd_paramedicIILabel = new FormData();
        fd_paramedicIILabel.bottom = new FormAttachment(0, 81);
        fd_paramedicIILabel.top = new FormAttachment(0, 68);
        fd_paramedicIILabel.right = new FormAttachment(0, 68);
        fd_paramedicIILabel.left = new FormAttachment(0, 7);
        paramedicIILabel.setLayoutData(fd_paramedicIILabel);
        paramedicIILabel.setForeground(CustomColors.COLOR_GREY);
        paramedicIILabel.setText("Sanit�ter II:");
        personalAmFahrzeugGroup.setTabList(new Control[] { textFahrer, textSaniI, textSaniII });

        // group 'Statusmeldungen'
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
        aufgLabel.setForeground(CustomColors.COLOR_GREY);
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
        aeLabel.setForeground(CustomColors.COLOR_GREY);
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
        ts1Label.setForeground(CustomColors.COLOR_GREY);
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
        ts2Label.setForeground(CustomColors.COLOR_GREY);
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
        ts3Label.setForeground(CustomColors.COLOR_GREY);
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
        ts4Label.setForeground(CustomColors.COLOR_GREY);
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
        ts5Label.setForeground(CustomColors.COLOR_GREY);
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
        ts6Label.setForeground(CustomColors.COLOR_GREY);
        ts6Label.setText("S6:");

        // set uninteresting groups invisible
        if ("prebooking".equalsIgnoreCase(transportType)) {
            transportdetailsGroup.setVisible(false);
            statusmeldungenGroup.setVisible(false);
            personalAmFahrzeugGroup.setVisible(false);
            planungGroup_1.setVisible(false);
        }

        if ("emergencyTransport".equalsIgnoreCase(transportType)) {
            transportdetailsGroup.setVisible(false);
            statusmeldungenGroup.setVisible(false);
            personalAmFahrzeugGroup.setVisible(false);
            planungGroup.setVisible(false);
            // set dateTime to default and disable editing
            GregorianCalendar gcal = new GregorianCalendar();
            dateTime.setDay(gcal.get(GregorianCalendar.DATE));
            dateTime.setMonth(gcal.get(GregorianCalendar.MONTH));
            dateTime.setYear(gcal.get(GregorianCalendar.YEAR));
            dateTime.setEnabled(false);
        }

        if ("journal".equalsIgnoreCase(editingType)) {
            transportdetailsGroup.setVisible(true);
            statusmeldungenGroup.setVisible(true);
            personalAmFahrzeugGroup.setVisible(true);
        } else if ("both".equalsIgnoreCase(transportType)) {
            transportdetailsGroup.setVisible(false);
            statusmeldungenGroup.setVisible(false);
            personalAmFahrzeugGroup.setVisible(false);

            planungGroup.setVisible(true);
            planungGroup_1.setVisible(true);
        } else {
            transportdetailsGroup.setVisible(false);
            statusmeldungenGroup.setVisible(false);
            personalAmFahrzeugGroup.setVisible(false);
        }

        // transport type selection buttons
        formGroup = new Group(client, SWT.NONE);
        formGroup.setLayout(new FormLayout());
        final FormData fd_group = new FormData();
        fd_group.bottom = new FormAttachment(0, 501);
        fd_group.top = new FormAttachment(0, 373);
        fd_group.right = new FormAttachment(0, 1056);
        fd_group.left = new FormAttachment(0, 846);
        formGroup.setLayoutData(fd_group);
        formGroup.setText("Formularansicht");

        buttonNotfall = new Button(formGroup, SWT.NONE);
        final FormData fd_buttonNotfall = new FormData();
        fd_buttonNotfall.bottom = new FormAttachment(0, 25);
        fd_buttonNotfall.top = new FormAttachment(0, 2);
        fd_buttonNotfall.right = new FormAttachment(0, 204);
        fd_buttonNotfall.left = new FormAttachment(0, 104);
        buttonNotfall.setLayoutData(fd_buttonNotfall);
        buttonNotfall.setToolTipText("Blendet alle f�r einen Notfall nicht relevanten Felder aus");
        buttonNotfall.addSelectionListener(new SelectionAdapter() {

            int index;

            public void widgetSelected(final SelectionEvent e) {
                planungGroup.setVisible(false);
                planungGroup_1.setVisible(true);
                transportdetailsGroup.setVisible(false);
                statusmeldungenGroup.setVisible(false);
                personalAmFahrzeugGroup.setVisible(false);
                multiTransportGroup.setVisible(false);
                mehrfachtransport = false;
                buttonMehrfachtransport.setEnabled(false);
                buttonADDMehrfachtransport.setEnabled(false);
                buttonAssignCar.setEnabled(true);
                // set dateTime to default and disable editing
                GregorianCalendar gcal = new GregorianCalendar();
                dateTime.setDay(gcal.get(GregorianCalendar.DATE));
                dateTime.setMonth(gcal.get(GregorianCalendar.MONTH));
                dateTime.setYear(gcal.get(GregorianCalendar.YEAR));
                dateTime.setEnabled(false);

                transportType = "emergencyTransport";

                // set possible priorities
                index = comboPrioritaet.getSelectionIndex();
                if (index != -1)
                    tmpPriority = comboPrioritaet.getItem(index);

                if (transportType.equalsIgnoreCase("prebooking")) {
                    comboPrioritaet.setItems(prebookingPriorities);
                    comboPrioritaet.setText(tmpPriority);
                }
                if (transportType.equalsIgnoreCase("emergencyTransport")) {
                    comboPrioritaet.setItems(emergencyAndTransportPriorities);
                    comboPrioritaet.setText(tmpPriority);
                }

                // remove all transports from the multi transport list
                multiTransportProvider.removeAllTransports();
                viewer.refresh();
            }
        });
        buttonNotfall.setText("Notfall");

        buttonVormerkung = new Button(formGroup, SWT.NONE);
        final FormData fd_buttonVormerkung = new FormData();
        fd_buttonVormerkung.bottom = new FormAttachment(0, 59);
        fd_buttonVormerkung.top = new FormAttachment(0, 36);
        fd_buttonVormerkung.right = new FormAttachment(0, 204);
        fd_buttonVormerkung.left = new FormAttachment(0, 104);
        buttonVormerkung.setLayoutData(fd_buttonVormerkung);
        buttonVormerkung
                .setToolTipText("Blendet alle f�r eine Vormerkung nicht relevanten Felder aus");
        buttonVormerkung.setText("Transport");
        buttonVormerkung.addSelectionListener(new SelectionAdapter() {

            int index;

            public void widgetSelected(final SelectionEvent e) {
                planungGroup_1.setVisible(false);
                planungGroup.setVisible(true);
                transportdetailsGroup.setVisible(false);
                statusmeldungenGroup.setVisible(false);
                personalAmFahrzeugGroup.setVisible(false);
                dateTime.setEnabled(true);
                viewerAssign.getTable().setSelection(-1);// deselect
                assignCarGroup.setVisible(false);
                buttonMehrfachtransport.setEnabled(true);
                buttonADDMehrfachtransport.setEnabled(true);

                transportType = "prebooking";

                // set possible priorities
                index = comboPrioritaet.getSelectionIndex();
                if (index != -1)
                    tmpPriority = comboPrioritaet.getItem(index);

                if (transportType.equalsIgnoreCase("prebooking")) {
                    comboPrioritaet.setItems(prebookingPriorities);
                    comboPrioritaet.setText(tmpPriority);
                }
                if (transportType.equalsIgnoreCase("emergencyTransport")) {
                    comboPrioritaet.setItems(emergencyAndTransportPriorities);
                    comboPrioritaet.setText(tmpPriority);
                }
            }
        });

        createdBy = new Text(formGroup, SWT.BORDER);
        final FormData fd_crreatedBy = new FormData();
        fd_crreatedBy.bottom = new FormAttachment(0, 108);
        fd_crreatedBy.top = new FormAttachment(0, 88);
        fd_crreatedBy.right = new FormAttachment(0, 102);
        fd_crreatedBy.left = new FormAttachment(0, 2);
        createdBy.setLayoutData(fd_crreatedBy);
        createdBy.setToolTipText("Aufgenommen von");
        createdBy.setEditable(false);

        disposedBy = new Text(formGroup, SWT.BORDER);
        final FormData fd_disposedBy = new FormData();
        fd_disposedBy.bottom = new FormAttachment(0, 108);
        fd_disposedBy.top = new FormAttachment(0, 88);
        fd_disposedBy.right = new FormAttachment(0, 204);
        fd_disposedBy.left = new FormAttachment(0, 104);
        disposedBy.setLayoutData(fd_disposedBy);
        disposedBy.setToolTipText("Disponiert von");
        disposedBy.setEditable(false);

        buttonDialyse = new Button(formGroup, SWT.NONE);
        final FormData fd_buttonDialye = new FormData();
        fd_buttonDialye.bottom = new FormAttachment(0, 25);
        fd_buttonDialye.top = new FormAttachment(0, 2);
        fd_buttonDialye.right = new FormAttachment(0, 102);
        fd_buttonDialye.left = new FormAttachment(0, 2);
        buttonDialyse.setLayoutData(fd_buttonDialye);
        buttonDialyse.setToolTipText("Erstellt einen Dialysetransport");
        buttonDialyse.setText("Dialyse");
        buttonDialyse.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                DialysisPatient dia = new DialysisPatient();
                if (!textFromStreet.getText().equalsIgnoreCase(""))
                    dia.setFromStreet(textFromStreet.getText());
                else
                    dia.setFromStreet("<bitte ausf�llen>");

                if (!textFromCity.getText().equalsIgnoreCase(""))
                    dia.setFromCity(textFromCity.getText());
                else
                    dia.setFromCity("<bitte ausf�llen>");

                dia.setToStreet(textToStreet.getText());

                dia.setToCity(textToCity.getText());
                int index = zustaendigeOrtsstelle.getCombo().getSelectionIndex();
                if (index != -1)
                    dia.setLocation((Location) zustaendigeOrtsstelle.getElementAt(index));

                Calendar startTime = convertStringToDate(textAbf.getText());
                if (startTime != null) {
                    startTime.set(Calendar.YEAR, dateTime.getYear());
                    startTime.set(Calendar.MONTH, dateTime.getMonth());
                    startTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());

                    dia.setPlannedStartOfTransport(startTime.getTimeInMillis());
                } else
                    dia.setPlannedStartOfTransport(0);

                Calendar patientTime = convertStringToDate(textBeiPat.getText());
                if (patientTime != null) {
                    patientTime.set(Calendar.YEAR, dateTime.getYear());
                    patientTime.set(Calendar.MONTH, dateTime.getMonth());
                    patientTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
                    dia.setPlannedTimeAtPatient(patientTime.getTimeInMillis());
                } else
                    dia.setPlannedTimeAtPatient(0);

                Calendar appointmentTime = convertStringToDate(textTermin.getText());
                if (appointmentTime != null) {
                    appointmentTime.set(Calendar.YEAR, dateTime.getYear());
                    appointmentTime.set(Calendar.MONTH, dateTime.getMonth());
                    appointmentTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
                    dia.setAppointmentTimeAtDialysis(appointmentTime.getTimeInMillis());
                } else
                    dia.setAppointmentTimeAtDialysis(0);

                // the kind of transport
                index = combokindOfTransport.getSelectionIndex();
                if (index != -1)
                    dia.setKindOfTransport(combokindOfTransport.getItem(index));

                Patient patient = new Patient(patientFirstName.getText(), patientLastName.getText());
                dia.setPatient(patient);

                DialysisForm form = new DialysisForm(dia, true);
                form.open();

                getShell().close();
            }
        });

        buttonMehrfachtransport = new Button(formGroup, SWT.NONE);
        final FormData fd_buttonMehrfachtransport = new FormData();
        fd_buttonMehrfachtransport.bottom = new FormAttachment(0, 59);
        fd_buttonMehrfachtransport.top = new FormAttachment(0, 36);
        fd_buttonMehrfachtransport.right = new FormAttachment(0, 102);
        fd_buttonMehrfachtransport.left = new FormAttachment(0, 2);
        buttonMehrfachtransport.setLayoutData(fd_buttonMehrfachtransport);
        buttonMehrfachtransport.setToolTipText("Mehrfachtransportfenster �ffnen");
        buttonMehrfachtransport.setText("Mehrfachtransport");
        buttonMehrfachtransport.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                hookContextMenu();
                mehrfachtransport = true;
                buttonMehrfachtransport.setEnabled(false);
                buttonADDMehrfachtransport.setEnabled(true);
                multiTransportGroup.setVisible(true);
                assignCarGroup.setVisible(false);
                buttonAssignCar.setEnabled(false);

                // remove all transports from the multi transport list
                multiTransportProvider.removeAllTransports();
                viewer.refresh();
            }
        });

        buttonADDMehrfachtransport = new Button(formGroup, SWT.NONE);
        final FormData fd_buttonADDMehrfachtransport = new FormData();
        fd_buttonADDMehrfachtransport.bottom = new FormAttachment(0, 83);
        fd_buttonADDMehrfachtransport.top = new FormAttachment(0, 60);
        fd_buttonADDMehrfachtransport.right = new FormAttachment(0, 102);
        fd_buttonADDMehrfachtransport.left = new FormAttachment(0, 2);
        buttonADDMehrfachtransport.setLayoutData(fd_buttonADDMehrfachtransport);
        buttonADDMehrfachtransport.setToolTipText("Mehrfachtransport hinzuf�gen");
        buttonADDMehrfachtransport.setText("hinzuf�gen");
        buttonADDMehrfachtransport.setEnabled(false);
        buttonADDMehrfachtransport.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                // make a difference between immediate and final transports
                finalMultiTransportKlick = false;
                // check the fields
                if (!handleOK())
                    return;

                // copy the transport (don't use the same objects (transport,
                // patient, caller)!!
                Patient newPatient = new Patient();
                CallerDetail newCaller = new CallerDetail();
                if (transport.getPatient() != null) {
                    newPatient.setLastname(transport.getPatient().getLastname());
                    newPatient.setFirstname(transport.getPatient().getFirstname());
                }
                if (transport.getCallerDetail() != null) {
                    newCaller.setCallerName(transport.getCallerDetail().getCallerName());
                    newCaller.setCallerTelephoneNumber(transport.getCallerDetail()
                            .getCallerTelephoneNumber());
                }
                Transport newTransport = Transport.copyTransport(transport);
                newTransport.setCreatedByUsername(username);
                newTransport.setPatient(newPatient);
                newTransport.setCallerDetail(newCaller);
                // add the created transport to the table object list
                multiTransportProvider.addTransport(newTransport);

                // refresh the viewer
                viewer.refresh();
            }
        });

        buttonAssignCar = new Button(formGroup, SWT.NONE);
        final FormData fd_buttonAssignCar = new FormData();
        fd_buttonAssignCar.bottom = new FormAttachment(0, 83);
        fd_buttonAssignCar.top = new FormAttachment(0, 60);
        fd_buttonAssignCar.right = new FormAttachment(0, 204);
        fd_buttonAssignCar.left = new FormAttachment(0, 104);
        buttonAssignCar.setLayoutData(fd_buttonAssignCar);
        buttonAssignCar.setToolTipText("Fahrzeug zuweisen");
        buttonAssignCar.setText("Fahrzeug zuweisen");
        buttonAssignCar.setEnabled(false);
        buttonAssignCar.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(final SelectionEvent e) {
                mehrfachtransport = false;
                buttonMehrfachtransport.setEnabled(false);
                buttonADDMehrfachtransport.setEnabled(false);
                multiTransportGroup.setVisible(false);

                // remove all transports from the multi transport list
                multiTransportProvider.removeAllTransports();
                viewer.refresh();

                viewerAssign.refresh();
                assignCarGroup.setVisible(true);
            }
        });
    }

    /**
     * Converts and returns the given time string to a calendar.<br>
     * The provided time string must have either the format HHmm or HH:mm.
     * 
     * @return the date entered or null if the time is not valid
     */
    private Calendar convertStringToDate(final String dateString) {
        // the date formatter
        DateFormat formatter;

        // the types of the accepted strings
        if (dateString.contains(":"))
            formatter = new SimpleDateFormat("HH:mm");
        else
            formatter = new SimpleDateFormat("HHmm");

        // try and parse the string
        try {
            formatter.setLenient(false);
            long timestamp = formatter.parse(dateString).getTime();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(timestamp);

            // set the date for the timestamps to get a correct sort order
            Calendar calDateOfTransport = Calendar.getInstance();
            calDateOfTransport.setTimeInMillis(transport.getDateOfTransport());
            cal.set(Calendar.YEAR, calDateOfTransport.get(Calendar.YEAR));
            cal.set(Calendar.MONTH, calDateOfTransport.get(Calendar.MONTH));
            cal.set(Calendar.DAY_OF_MONTH, calDateOfTransport.get(Calendar.DAY_OF_MONTH));

            return cal;
        } catch (ParseException pe) {
            return null;
        }
    }

    /**
     * returns the priority to set
     * 
     * @param the
     *            priority string
     */
    private String stringToPriority(String priorityString) {
        if (priorityString.equalsIgnoreCase("1 NEF"))
            return "A";
        else if (priorityString.equalsIgnoreCase("2 Transport"))
            return "B";
        else if (priorityString.equalsIgnoreCase("3 Terminfahrt"))
            return "C";
        else if (priorityString.equalsIgnoreCase("4 RT"))
            return "D";
        else if (priorityString.equalsIgnoreCase("5 HT"))
            return "E";
        else if (priorityString.equalsIgnoreCase("6 Sonstiges"))
            return "F";
        else if (priorityString.equalsIgnoreCase("7 NEF extern"))
            return "G";
        else
            return "F";// default in case of a problem
    }

    /**
     * returns the priority string to display
     * 
     * @param the
     *            priority
     */
    private String priorityToString(String priority) {
        if (priority.equalsIgnoreCase("A"))
            return "1 NEF";
        else if (priority.equalsIgnoreCase("B"))
            return "2 Transport";
        else if (priority.equalsIgnoreCase("C"))
            return "3 Terminfahrt";
        else if (priority.equalsIgnoreCase("D"))
            return "4 RT";
        else if (priority.equalsIgnoreCase("E"))
            return "5 HT";
        else if (priority.equalsIgnoreCase("F"))
            return "6 Sonstiges";
        else if (priority.equalsIgnoreCase("G"))
            return "7 NEF extern";
        else
            return null;
    }

    private boolean handleOK() {
        // reset the error messages
        setMessage("Hier k�nnen Sie einen neuen Transport anlegen");

        // the street
        if (textFromStreet.getText().length() > 100) {
            // getShell().getDisplay().beep();
            setErrorMessage("Der Stra�enname (von)darf h�chstens 100 Zeichen lang sein");
            return false;
        }

        if (textFromStreet.getText().trim().isEmpty()) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie die Stra�e ein, von der der Transport gestartet wird");
            return false;
        }

        transport.setFromStreet(textFromStreet.getText());

        // the from city
        if (textFromCity.getText().length() > 50) {
            // getShell().getDisplay().beep();
            setErrorMessage("Der Stadtname (von)darf h�chstens 50 Zeichen lang sein");
            return false;
        }

        // the city--> can be empty if the street is LKH or PH
        if (textFromCity.getText().trim().isEmpty()
                & !(transport.getFromStreet().contains("LKH")
                        || transport.getFromStreet().startsWith("LKH")
                        || transport.getFromStreet().contains("PH") || transport.getFromStreet()
                        .startsWith("PH"))) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie die Stadt ein, von der der Transport gestartet wird");
            return false;
        }

        transport.setFromCity(textFromCity.getText());

        if (textToStreet.getText().length() > 100) {
            // getShell().getDisplay().beep();
            setErrorMessage("Der Stra�enname (nach) darf h�chstens 100 Zeichen lang sein");
            return false;
        }

        if (textToCity.getText().length() > 50) {
            // getShell().getDisplay().beep();
            setErrorMessage("Der Stadtname (nach) darf h�chstens 50 Zeichen lang sein");
            return false;
        }

        // the planned location
        int index = zustaendigeOrtsstelle.getCombo().getSelectionIndex();
        if (index == -1) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben sie die zust�ndige Ortsstelle ein");
            return false;
        }
        transport.setPlanedLocation((Location) zustaendigeOrtsstelle.getElementAt(index));

        // the transport priority
        index = comboPrioritaet.getSelectionIndex();
        if (index == -1) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben sie die Priorit�t des Transports ein");
            return false;
        }
        transport.setTransportPriority(this.stringToPriority(comboPrioritaet.getItem(index)));

        // convert the start time --> no validation when an emergency transport
        Calendar startTime = convertStringToDate(textAbf.getText());
        if (startTime == null & !transportType.equalsIgnoreCase("emergencyTransport") && createNew) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie eine g�ltige Abfahrtszeit in der Form HH:mm oder HHmm ein");
            return false;
        }
        if (startTime != null) {
            startTime.set(Calendar.YEAR, dateTime.getYear());
            startTime.set(Calendar.MONTH, dateTime.getMonth());
            startTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());

            transport.setPlannedStartOfTransport(startTime.getTimeInMillis());
        } else
            transport.setPlannedStartOfTransport(0);

        // time at patient --> validation if the field is not empty
        Calendar patientTime = convertStringToDate(textBeiPat.getText());
        if (!textBeiPat.getText().trim().isEmpty() && patientTime == null) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie eine g�ltige Zeit (bei Patient) in der Form HH:mm oder HHmm ein");
            return false;
        }
        if (patientTime != null) {
            patientTime.set(Calendar.YEAR, dateTime.getYear());
            patientTime.set(Calendar.MONTH, dateTime.getMonth());
            patientTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
            transport.setPlannedTimeAtPatient(patientTime.getTimeInMillis());
        } else
            transport.setPlannedTimeAtPatient(0);

        // check the time --> validation if the field is not empty
        Calendar appointmentTime = convertStringToDate(textTermin.getText());
        if (!textTermin.getText().trim().isEmpty() && appointmentTime == null) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Termin in der Form HH:mm oder HHmm ein");
            return false;
        }
        if (appointmentTime != null) {
            appointmentTime.set(Calendar.YEAR, dateTime.getYear());
            appointmentTime.set(Calendar.MONTH, dateTime.getMonth());
            appointmentTime.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
            transport.setAppointmentTimeAtDestination(appointmentTime.getTimeInMillis());
        } else
            transport.setAppointmentTimeAtDestination(0);

        // calendar to allow transport times around midnight
        Calendar calMid = Calendar.getInstance();
        calMid.setTimeInMillis(transport.getPlannedStartOfTransport());
        int hour = calMid.get(Calendar.HOUR_OF_DAY);

        // validate: start before atPatient
        if (transport.getPlannedTimeAtPatient() < transport.getPlannedStartOfTransport()
                & !(transport.getPlannedTimeAtPatient() == 0)) {
            if (hour < 22) {
                // getShell().getDisplay().beep();
                setErrorMessage("Ankunft bei Patient kann nicht vor Abfahrtszeit des Fahrzeuges liegen.");
                return false;
            }
        }

        // validate: atPatient before term
        if (transport.getAppointmentTimeAtDestination() < transport.getPlannedTimeAtPatient()
                & !(transport.getAppointmentTimeAtDestination() == 0)
                & !(transport.getPlannedTimeAtPatient() == 0)) {
            if (hour < 22) {
                // getShell().getDisplay().beep();
                setErrorMessage("Termin kann nicht vor Ankunft bei Patient sein");
                return false;
            }
        }

        // validate: start before term
        if (transport.getAppointmentTimeAtDestination() < transport.getPlannedStartOfTransport()
                & !(transport.getAppointmentTimeAtDestination() == 0)) {
            if (hour < 22) {
                // getShell().getDisplay().beep();
                setErrorMessage("Termin kann nicht vor Abfahrtszeit des Fahrzeuges liegen");
                return false;
            }
        }

        // kind of illness
        index = setErkrVerl.getCombo().getSelectionIndex();
        if (index != -1) {
            transport.setKindOfIllness((Disease) setErkrVerl.getElementAt(index));
        }

        // set the fields that do not have to be validated
        transport.setBackTransport(ruecktransportMoeglichButton.getSelection());

        // the disposed by user
        if (!disposedBy.getText().trim().isEmpty())
            transport.setDisposedByUsername(disposedBy.getText());
        // the kind of transport
        index = combokindOfTransport.getSelectionIndex();
        if (index != -1)
            transport.setKindOfTransport(combokindOfTransport.getItem(index));

        if (patientFirstName.getText().length() > 30) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie einen Vornamen, der k�rzer als 30 Zeichen ist ein");
            return false;
        }

        if (patientLastName.getText().length() > 30) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie einen Nachnamen, der k�rzer als 30 Zeichen ist ein");
            return false;
        }

        // if we have a patient just update it
        if (transport.getPatient() == null) {
            Patient patient = new Patient(patientFirstName.getText(), patientLastName.getText());
            transport.setPatient(patient);
        } else {
            transport.getPatient().setFirstname(patientFirstName.getText());
            transport.getPatient().setLastname(patientLastName.getText());
        }

        // validate caller
        if (textAnrufer.getText().length() > 30) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie einen Anrufer k�rzer 30 Zeichen ein.");
            return false;
        }
        if (textTelefonAnrufer.getText().length() > 30) {
            // getShell().getDisplay().beep();
            setErrorMessage("Bitte geben Sie eine Telefonnummer k�rzer 30 ein!");
            return false;
        }

        // if we have a caller, just update it
        if (transport.getCallerDetail() == null) {
            CallerDetail caller = new CallerDetail(textAnrufer.getText(), textTelefonAnrufer
                    .getText());
            transport.setCallerDetail(caller);
        } else {
            transport.getCallerDetail().setCallerName(textAnrufer.getText());
            transport.getCallerDetail().setCallerTelephoneNumber(textTelefonAnrufer.getText());
        }

        // notes and feedback
        if (textAnmerkungen.getText().length() > 2000) {
            // getShell().getDisplay().beep();
            setErrorMessage("Die Anmerkung muss k�rzer als 2000 Zeichen sein");
            return false;
        }

        transport.setNotes(textAnmerkungen.getText());

        if (textRueckmeldung.getText().length() > 2000) {
            // getShell().getDisplay().beep();
            setErrorMessage("Die R�ckmeldung muss k�rzer als 2000 Zeichen sein");
            return false;
        }

        transport.setFeedback(textRueckmeldung.getText());

        // the destination
        transport.setToStreet(textToStreet.getText());
        transport.setToCity(textToCity.getText());

        // the boolean values
        transport.setAssistantPerson(begleitpersonButton.getSelection());
        transport.setBlueLightToGoal(bd2Button.getSelection());
        transport.setBlueLight1(bd1Button.getSelection());
        transport.setBrkdtAlarming(brkdtButton.getSelection());
        transport.setDfAlarming(dfButton.getSelection());
        transport.setEmergencyDoctorAlarming(notarztButton.getSelection());
        transport.setEmergencyPhone(rufhilfepatientButton.getSelection());
        transport.setFirebrigadeAlarming(feuerwehrButton.getSelection());
        transport.setHelicopterAlarming(rthButton.getSelection());
        transport.setLongDistanceTrip(fernfahrtButton.getSelection());
        transport.setMountainRescueServiceAlarming(bergrettungButton.getSelection());
        transport.setPoliceAlarming(polizeiButton.getSelection());
        transport.setKITAlarming(KITButton.getSelection());

        // the timestamps for the alarming fields
        if (!timestampNA.getText().equalsIgnoreCase(""))
            transport.settimestampNA(MyUtils.stringToTimestamp(timestampNA.getText(),
                    MyUtils.timeAndDateFormatShort));
        if (!timestampRTH.getText().equalsIgnoreCase(""))
            transport.settimestampRTH(MyUtils.stringToTimestamp(timestampRTH.getText(),
                    MyUtils.timeAndDateFormatShort));
        if (!timestampDF.getText().equalsIgnoreCase(""))
            transport.settimestampDF(MyUtils.stringToTimestamp(timestampDF.getText(),
                    MyUtils.timeAndDateFormatShort));
        if (!timestampBRKDT.getText().equalsIgnoreCase(""))
            transport.settimestampBRKDT(MyUtils.stringToTimestamp(timestampBRKDT.getText(),
                    MyUtils.timeAndDateFormatShort));
        if (!timestampFW.getText().equalsIgnoreCase(""))
            transport.settimestampFW(MyUtils.stringToTimestamp(timestampFW.getText(),
                    MyUtils.timeAndDateFormatShort));
        if (!timestampPolizei.getText().equalsIgnoreCase(""))
            transport.settimestampPolizei(MyUtils.stringToTimestamp(timestampPolizei.getText(),
                    MyUtils.timeAndDateFormatShort));
        if (!timestampBergrettung.getText().equalsIgnoreCase(""))
            transport.settimestampBergrettung(MyUtils.stringToTimestamp(timestampBergrettung
                    .getText(), MyUtils.timeAndDateFormatShort));
        if (!timestampKIT.getText().equalsIgnoreCase(""))
            transport.settimestampKIT(MyUtils.stringToTimestamp(timestampKIT.getText(),
                    MyUtils.timeAndDateFormatShort));

        // set the type of the transport
        if (transportType.equalsIgnoreCase("prebooking"))
            transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_PREBOOKING);
        if (transportType.equalsIgnoreCase("emergencyTransport"))
            transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_OUTSTANDING);

        if (mariazellButton.getSelection())
            transport.setDirection(IDirectness.TOWARDS_MARIAZELL);
        else if (wienButton.getSelection())
            transport.setDirection(IDirectness.TOWARDS_VIENNA);
        else if (leobenButton.getSelection())
            transport.setDirection(IDirectness.TOWARDS_LEOBEN);
        else if (grazButton.getSelection())
            transport.setDirection(IDirectness.TOWARDS_GRAZ);
        else if (kapfenbergButton.getSelection())
            transport.setDirection(IDirectness.TOWARDS_KAPFENBERG);
        else
            transport.setDirection(IDirectness.TOWARDS_BRUCK);

        // transport date
        Calendar transportDate = Calendar.getInstance();
        transportDate.set(Calendar.YEAR, dateTime.getYear());
        transportDate.set(Calendar.MONTH, dateTime.getMonth());
        transportDate.set(Calendar.DAY_OF_MONTH, dateTime.getDay());
        transport.setDateOfTransport(transportDate.getTimeInMillis());

        if (createNew) {
            // created time
            transport.setCreationTime(Calendar.getInstance().getTimeInMillis());
            // created by user
            transport.setCreatedByUsername(username);
            if (!mehrfachtransport) {
                // assign the vehicle if one is selected
                if (viewerAssign.getTable().getSelectionIndex() != -1) {
                    // the selection
                    ISelection selection = viewerAssign.getSelection();
                    // get the selected transport
                    VehicleDetail vehicle = (VehicleDetail) ((IStructuredSelection) selection)
                            .getFirstElement();
                    // set the details
                    transport.setVehicleDetail(vehicle);
                    transport.setDisposedByUsername(username);
                    GregorianCalendar cal = new GregorianCalendar();
                    long now = cal.getTimeInMillis();
                    transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, now);
                    transport.setProgramStatus(IProgramStatus.PROGRAM_STATUS_UNDERWAY);
                }

                // create and run the add action
                AddMessage<Transport> addMessage = new AddMessage<Transport>(transport);
                NetWrapper.sendMessage(addMessage);
                if (transport.getTransportPriority().equalsIgnoreCase("A")) {
                    DuplicatePriorityATransportAction duplicateAction = new DuplicatePriorityATransportAction(
                            transport);
                    duplicateAction.run();
                }
            } else {
                if (finalMultiTransportKlick) {
                    List<Transport> transportList = multiTransportProvider.getObjectList();
                    AddMessage<Transport> addMessage = new AddMessage<Transport>(transportList);
                    NetWrapper.sendMessage(addMessage);
                }
            }
        } else {
            // staff
            index = setTextFahrer.getCombo().getSelectionIndex();
            if (index != -1)
                transport.getVehicleDetail().setDriver(
                        (StaffMember) setTextFahrer.getElementAt(index));

            index = setTextSaniI.getCombo().getSelectionIndex();
            if (index != -1)
                transport.getVehicleDetail().setFirstParamedic(
                        (StaffMember) setTextSaniI.getElementAt(index));

            index = setTextSaniII.getCombo().getSelectionIndex();
            if (index != -1)
                transport.getVehicleDetail().setSecondParamedic(
                        (StaffMember) setTextSaniII.getElementAt(index));

            // transport stati
            // S0
            Calendar s0 = convertStringToDate(textAE.getText());
            if (!textAE.getText().trim().isEmpty() && s0 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status AE (Auftrag erteilt) in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s0 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED, s0
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_ORDER_PLACED);

            // S1
            Calendar s1 = convertStringToDate(textS1.getText());
            if (!textS1.getText().trim().isEmpty() && s1 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status S1 in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s1 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY, s1
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_ON_THE_WAY);

            // S2
            Calendar s2 = convertStringToDate(textS2.getText());
            if (!textS2.getText().trim().isEmpty() && s2 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status S2 in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s2 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT, s2
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_AT_PATIENT);

            // S3
            Calendar s3 = convertStringToDate(textS3.getText());
            if (!textS3.getText().trim().isEmpty() && s3 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status S3 in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s3 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT, s3
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_START_WITH_PATIENT);
            // S4
            Calendar s4 = convertStringToDate(textS4.getText());
            if (!textS4.getText().trim().isEmpty() && s4 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status S4 in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s4 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION, s4
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_AT_DESTINATION);
            // S5
            Calendar s5 = convertStringToDate(textS5.getText());
            if (!textS5.getText().trim().isEmpty() && s5 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status S5 in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s5 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE, s5
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_DESTINATION_FREE);
            // S6
            Calendar s6 = convertStringToDate(textS6.getText());
            if (!textS6.getText().trim().isEmpty() && s6 == null) {
                // getShell().getDisplay().beep();
                setErrorMessage("Bitte geben Sie eine g�ltige Zeit f�r den Status S6 in der Form HHmm oder HH:mm ein");
                return false;
            }
            if (s6 != null)
                transport.addStatus(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION, s6
                        .getTimeInMillis());
            else
                transport.removeStatus(ITransportStatus.TRANSPORT_STATUS_CAR_IN_STATION);

            /** duplicate the transport if the priority is changed to A */
            if (!oldPriority.equalsIgnoreCase("A")
                    && transport.getTransportPriority().equalsIgnoreCase("A")
                    & !editingType.equalsIgnoreCase("journal")) {
                DuplicatePriorityATransportAction duplicateAction = new DuplicatePriorityATransportAction(
                        transport);
                duplicateAction.run();
            }

            UpdateMessage<Transport> updateMessage = new UpdateMessage<Transport>(transport);
            NetWrapper.sendMessage(updateMessage);
        }
        if (!mehrfachtransport)
            getShell().close();
        if (finalMultiTransportKlick)
            getShell().close();
        finalMultiTransportKlick = true;

        // remove the lock
        Lock lock = new Lock(transport.getTransportId(), Transport.class, "");
        RemoveMessage<Lock> removeMessage = new RemoveMessage<Lock>(lock);
        NetWrapper.sendMessage(removeMessage);
        return true;
    }

    private void createAssignCarTable() {
        viewerAssign = new TableViewer(assignCarGroup, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER
                | SWT.FULL_SELECTION);
        assignVehicleContentProvider = new AssignVehicleContentProvider();
        viewerAssign.setContentProvider(assignVehicleContentProvider);
        viewerAssign.setLabelProvider(new VehicleAssignLabelProvider());
        viewerAssign.setInput(vehicleHandler.getReadyVehicleList().toArray());
        final Table table_1 = viewerAssign.getTable();
        final FormData fd_table_1 = new FormData();
        fd_table_1.right = new FormAttachment(0, 824);
        fd_table_1.left = new FormAttachment(0, 3);
        fd_table_1.top = new FormAttachment(0, 1);
        fd_table_1.bottom = new FormAttachment(0, 122);
        table_1.setLayoutData(fd_table_1);

        viewerAssign.getTable().addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                if (viewerAssign.getTable().getItem(new Point(e.x, e.y)) == null) {
                    viewerAssign.setSelection(new StructuredSelection());
                }
            }
        });

        final Table table = viewerAssign.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        final TableColumn stationColumn = new TableColumn(table, SWT.NONE);
        stationColumn.setToolTipText("Aktuelle Dienststelle des Fahrzeuges");
        stationColumn.setWidth(30);
        stationColumn.setText("Dienststelle");

        final TableColumn nameColumn = new TableColumn(table, SWT.NONE);
        nameColumn.setToolTipText("Fahrzeug");
        nameColumn.setWidth(60);
        nameColumn.setText("Fahrzeug");

        final TableColumn typeColumn = new TableColumn(table, SWT.NONE);
        typeColumn.setWidth(60);
        typeColumn.setText("Fahrzeugtyp");

        final TableColumn driverColumn = new TableColumn(table, SWT.NONE);
        driverColumn.setToolTipText("Fahrer");
        driverColumn.setWidth(100);
        driverColumn.setText("Fahrer");

        final TableColumn medicIColumn = new TableColumn(table, SWT.NONE);
        medicIColumn.setToolTipText("Sanit�ter I");
        medicIColumn.setWidth(100);
        medicIColumn.setText("Sanit�ter I");

        final TableColumn medicIIColumn = new TableColumn(table, SWT.NONE);
        medicIIColumn.setToolTipText("Sanit�ter II");
        medicIIColumn.setWidth(100);
        medicIIColumn.setText("Sanit�ter II");

        Listener sortListener = new Listener() {

            public void handleEvent(Event e) {
                // determine new sort column and direction
                TableColumn sortColumn = viewerAssign.getTable().getSortColumn();
                TableColumn currentColumn = (TableColumn) e.widget;
                int dir = viewerAssign.getTable().getSortDirection();
                // revert the sort order if the column is the same
                if (sortColumn == currentColumn) {
                    if (dir == SWT.UP)
                        dir = SWT.DOWN;
                    else
                        dir = SWT.UP;
                } else {
                    viewerAssign.getTable().setSortColumn(currentColumn);
                    dir = SWT.UP;
                }
                // sort the data based on column and direction
                String sortIdentifier = null;
                if (currentColumn == stationColumn)
                    sortIdentifier = VehicleSorter.CURRENT_STATION_SORTER;
                if (currentColumn == nameColumn)
                    sortIdentifier = VehicleSorter.VEHICLE_SORTER;
                if (currentColumn == typeColumn)
                    sortIdentifier = VehicleSorter.VEHICLE_TYPE_SORTER;
                if (currentColumn == driverColumn)
                    sortIdentifier = VehicleSorter.DRIVER_SORTER;
                if (currentColumn == medicIColumn)
                    sortIdentifier = VehicleSorter.PARAMEDIC_I_SORTER;
                if (currentColumn == medicIIColumn)
                    sortIdentifier = VehicleSorter.PARAMEDIC_II_SORTER;
                // apply the filter
                viewerAssign.getTable().setSortDirection(dir);
                viewerAssign.setSorter(new VehicleSorter(sortIdentifier, dir));
                viewerAssign.refresh();
            }
        };

        // attach the listener
        stationColumn.addListener(SWT.Selection, sortListener);
        nameColumn.addListener(SWT.Selection, sortListener);
        typeColumn.addListener(SWT.Selection, sortListener);
        driverColumn.addListener(SWT.Selection, sortListener);
        medicIColumn.addListener(SWT.Selection, sortListener);
        medicIIColumn.addListener(SWT.Selection, sortListener);

        viewerAssign.refresh();
    }

    private void createMultiTransportTable() {
        viewer = new TableViewer(multiTransportGroup, SWT.VIRTUAL | SWT.MULTI | SWT.BORDER
                | SWT.FULL_SELECTION);
        final Table table_1 = viewer.getTable();
        final FormData fd_table_1 = new FormData();
        fd_table_1.right = new FormAttachment(0, 824);
        fd_table_1.left = new FormAttachment(0, 3);
        fd_table_1.top = new FormAttachment(0, 1);
        fd_table_1.bottom = new FormAttachment(0, 122);
        table_1.setLayoutData(fd_table_1);
        viewer.setContentProvider(multiTransportProvider);
        viewer.setLabelProvider(new MultiTransportLabelProvider());
        viewer.setInput(transportHandler.toArray());
        viewer.getTable().addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                if (viewer.getTable().getItem(new Point(e.x, e.y)) == null) {
                    viewer.setSelection(new StructuredSelection());
                }
            }
        });

        final Table table = viewer.getTable();
        table.setLinesVisible(true);
        table.setHeaderVisible(true);

        final TableColumn dateColumn = new TableColumn(table, SWT.NONE);
        dateColumn.setToolTipText("Transportdatum");
        dateColumn.setWidth(65);
        dateColumn.setText("Datum");

        final TableColumn bTableColumnOrtsstelle = new TableColumn(table, SWT.NONE);
        bTableColumnOrtsstelle.setWidth(27);
        bTableColumnOrtsstelle.setText("OS");

        final TableColumn bTableColumnAbfahrt = new TableColumn(table, SWT.NONE);
        bTableColumnAbfahrt.setToolTipText("Geplante Abfahrt an der Ortsstelle");
        bTableColumnAbfahrt.setWidth(40);
        bTableColumnAbfahrt.setText("Abf");

        final TableColumn bTableColumnAnkunft = new TableColumn(table, SWT.NONE);
        bTableColumnAnkunft.setToolTipText("Geplante Ankunft beim Patienten");
        bTableColumnAnkunft.setWidth(40);
        bTableColumnAnkunft.setText("Ank");

        final TableColumn bTableColumnTermin = new TableColumn(table, SWT.NONE);
        bTableColumnTermin.setToolTipText("Termin am Zielort");
        bTableColumnTermin.setWidth(40);
        bTableColumnTermin.setText("Termin");

        final TableColumn bTableColumnTransportVon = new TableColumn(table, SWT.NONE);
        bTableColumnTransportVon.setWidth(200);
        bTableColumnTransportVon.setText("Transport von");

        final TableColumn bTtableColumnPatient = new TableColumn(table, SWT.NONE);
        bTtableColumnPatient.setWidth(170);
        bTtableColumnPatient.setText("Patient");

        final TableColumn bTableColumnTransportNach = new TableColumn(table, SWT.NONE);
        bTableColumnTransportNach.setWidth(200);
        bTableColumnTransportNach.setText("Transport nach");

        final TableColumn bTableColumnTA = new TableColumn(table, SWT.NONE);
        bTableColumnTA.setToolTipText("Transportart");
        bTableColumnTA.setWidth(20);
        bTableColumnTA.setText("T");

        Listener sortListener = new Listener() {

            public void handleEvent(Event e) {
                // determine new sort column and direction
                TableColumn sortColumn = viewer.getTable().getSortColumn();
                TableColumn currentColumn = (TableColumn) e.widget;
                int dir = viewer.getTable().getSortDirection();
                // revert the sort order if the column is the same
                if (sortColumn == currentColumn) {
                    if (dir == SWT.UP)
                        dir = SWT.DOWN;
                    else
                        dir = SWT.UP;
                } else {
                    viewer.getTable().setSortColumn(currentColumn);
                    dir = SWT.UP;
                }
                // sort the data based on column and direction
                String sortIdentifier = null;
                if (currentColumn == bTableColumnOrtsstelle)
                    sortIdentifier = TransportSorter.RESP_STATION_SORTER;
                if (currentColumn == bTableColumnAbfahrt)
                    sortIdentifier = TransportSorter.ABF_SORTER;
                if (currentColumn == bTableColumnAnkunft)
                    sortIdentifier = TransportSorter.AT_PATIENT_SORTER;
                if (currentColumn == bTableColumnTermin)
                    sortIdentifier = TransportSorter.TERM_SORTER;
                if (currentColumn == bTableColumnTransportVon)
                    sortIdentifier = TransportSorter.TRANSPORT_FROM_SORTER;
                if (currentColumn == bTtableColumnPatient)
                    sortIdentifier = TransportSorter.PATIENT_SORTER;
                if (currentColumn == bTableColumnTransportNach)
                    sortIdentifier = TransportSorter.TRANSPORT_TO_SORTER;
                if (currentColumn == bTableColumnTA)
                    sortIdentifier = TransportSorter.TA_SORTER;
                // apply the filter
                viewer.getTable().setSortDirection(dir);
                viewer.setSorter(new TransportSorter(sortIdentifier, dir));
                viewer.refresh();
            }
        };

        // attach the listener
        bTableColumnOrtsstelle.addListener(SWT.Selection, sortListener);
        bTableColumnAbfahrt.addListener(SWT.Selection, sortListener);
        bTableColumnAnkunft.addListener(SWT.Selection, sortListener);
        bTableColumnTermin.addListener(SWT.Selection, sortListener);
        bTableColumnTransportVon.addListener(SWT.Selection, sortListener);
        bTtableColumnPatient.addListener(SWT.Selection, sortListener);
        bTableColumnTransportNach.addListener(SWT.Selection, sortListener);
        bTableColumnTA.addListener(SWT.Selection, sortListener);
        hookContextMenu();
        viewer.refresh();
    }

    /**
     * Creates the context menu
     */
    private void hookContextMenu() {
        MenuManager menuManager = new MenuManager("#JournalPopupMenu");
        menuManager.setRemoveAllWhenShown(true);
        menuManager.addMenuListener(new IMenuListener() {

            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        Menu menu = menuManager.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
    }

    /**
     * Fills the context menu with the actions
     */
    private void fillContextMenu(IMenuManager manager) {
        // get the selected object
        IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
        Transport transport = (Transport) selection.getFirstElement();
        if (transport == null)
            return;

        // get the index
        int index = viewer.getTable().getSelectionIndex();
        // create the menu
        removeAction = new RemoveTransportFromMultiTransportList(multiTransportProvider, viewer,
                index);
        manager.add(removeAction);
    }

    // PRIVATE METHODS
    /**
     * Called when the input text of a filter is changes
     */
    private void inputChanged(String changedText, String filterType) {
        if (changedText == null)
            return;
        // get the entered text
        if (changedText.trim().length() < 1) {
            setErrorMessage("Bitte geben sie mindestens drei Zeichen ein, um die Autovervollst�ndigung zu nutzen");
            // Display.getCurrent().beep();
            return;
        }
        setErrorMessage(null);

        if (filterJob == null)
            filterJob = new FilterAddressJob(null);

        // check the state
        if (filterJob.getState() == Job.RUNNING) {
            return;
        }

        // check if the filter should return streets
        if (filterType.equalsIgnoreCase(IFilterTypes.SEARCH_STRING_STREET))
            filterJob.setStrStreet(changedText);
        else
            filterJob.setStrStreet("");
        // check if the filter should return cities
        if (filterType.equalsIgnoreCase(IFilterTypes.SEARCH_STRING_CITY))
            filterJob.setStrCity(changedText);
        else
            filterJob.setStrCity("");
        // check if the filter should return zip codes
        if (filterType.equalsIgnoreCase(IFilterTypes.SEARCH_STRING_ZIP))
            filterJob.setStrZip(changedText);
        else
            filterJob.setStrZip("");

        // schedule the thread to run now
        filterJob.schedule(0);
    }

    @Override
    public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
        Object object = message.getFirstElement();
        // update the address proposals
        if (object instanceof Address) {
            acFromStreet.setProposals(addressHandler.toStreetArray());
            acFromCity.setProposals(addressHandler.toCityArray());
            acToStreet.setProposals(addressHandler.toStreetArray());
            acToCity.setProposals(addressHandler.toCityArray());
        }
        // update the staff member combos
        if (object instanceof StaffHandler) {
            setTextFahrer.refresh();
            setTextSaniI.refresh();
            setTextSaniII.refresh();
        }
        // update the disease
        if (object instanceof Disease) {
            setErkrVerl.refresh();
        }
        // update the vehicles
        if (object instanceof VehicleDetail) {
            viewerAssign.refresh();
        }
    }
}
