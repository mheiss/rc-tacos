package at.rc.tacos.client.ui.dialog;

import java.util.Calendar;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.JobHandler;
import at.rc.tacos.client.net.handler.LocationHandler;
import at.rc.tacos.client.net.handler.ServiceTypeHandler;
import at.rc.tacos.client.net.handler.StaffHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.custom.DatePicker;
import at.rc.tacos.client.ui.providers.JobLabelProvider;
import at.rc.tacos.client.ui.providers.ServiceTypeLabelProvider;
import at.rc.tacos.client.ui.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.ui.providers.StationLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Job;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.RosterEntry;
import at.rc.tacos.platform.model.ServiceType;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.ExecMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

/**
 * GUI (form) to manage a roster entry
 * 
 * @author b.thek
 */
public class RosterEntryForm extends TitleAreaDialog implements DataChangeListener<Object> {

	private FormToolkit toolkit;
	private TextViewer noteEditor;
	private ComboViewer comboDienstverhaeltnis;
	private ComboViewer comboVerwendung;
	private ComboViewer comboOrtsstelle;
	private Button bereitschaftButton;
	private ComboViewer employeenameCombo;

	// the date pickers
	private DatePicker dienstVon;
	private DatePicker dienstBis;
	private DatePicker anmeldung;
	private DatePicker abmeldung;

	// the roster entry
	private RosterEntry rosterEntry;
	private boolean createNew;

	// the model handlers
	private MessageIoSession session = NetWrapper.getSession();
	private ImageRegistry imageRegistry = UiWrapper.getDefault().getImageRegistry();

	private JobHandler jobHandler = (JobHandler) NetWrapper.getHandler(Job.class);
	private LocationHandler locationHandler = (LocationHandler) NetWrapper.getHandler(Location.class);
	private StaffHandler staffHandler = (StaffHandler) NetWrapper.getHandler(StaffMember.class);
	private ServiceTypeHandler serviceHandler = (ServiceTypeHandler) NetWrapper.getHandler(ServiceType.class);

	/**
	 * Default class constructor used to create a new roster entry.
	 * 
	 * @param parentShell
	 *            the parent shell
	 */
	public RosterEntryForm(Shell parentShell) {
		super(parentShell);
		createNew = true;
		this.rosterEntry = new RosterEntry();
	}

	/**
	 * Default class constructor to edit an existing entry.
	 * 
	 * @param parentShell
	 *            the parent shell
	 * @param rosterEntry
	 *            the roster entry to edit
	 */
	public RosterEntryForm(Shell parentShell, RosterEntry rosterEntry) {
		super(parentShell);
		this.createNew = false;
		this.rosterEntry = rosterEntry;
	}

	@Override
	public boolean close() {
		// check if the user wants to close the window
		if (getReturnCode() == CANCEL) {
			boolean exit = MessageDialog.openQuestion(getShell(), "Abbrechen", "Wollen Sie wirklich abbrechen?");
			if (!exit)
				return false;
		}
		// remove the lock from the object
		if (!createNew) {
			rosterEntry.setLocked(false);
			rosterEntry.setLockedBy(null);
			// send the request
			ExecMessage<RosterEntry> execMessage = new ExecMessage<RosterEntry>("doUnlock", rosterEntry);
			execMessage.asnchronRequest(NetWrapper.getSession());
		}
		// cleanup the listeners
		NetWrapper.removeListener(this, StaffMember.class);
		NetWrapper.removeListener(this, Location.class);
		NetWrapper.removeListener(this, Job.class);
		NetWrapper.removeListener(this, ServiceType.class);
		return super.close();
	}

	/**
	 * Creates the dialog's contents
	 * 
	 * @param parent
	 *            the parent composite
	 * @return Control
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		setTitle("Dienstplaneintrag");
		setMessage("Hier können Sie einen neuen Dienstplaneintrag anlegen", IMessageProvider.INFORMATION);
		setTitleImage(imageRegistry.get("application.logo"));
		return contents;
	}

	/**
	 * Create contents of the window
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// setup the composite
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 30;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		composite.setBackground(CustomColors.SECTION_BACKGROUND);
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));

		// create the content of the dialog
		createGeneralSection(composite);
		createPlaningSection(composite);
		createSignSection(composite);

		// init data
		if (!createNew) {
			Calendar cal = Calendar.getInstance();
			// check in
			if (rosterEntry.getRealStartOfWork() != 0) {
				cal.setTimeInMillis(rosterEntry.getRealStartOfWork());
				anmeldung.setDate(cal);
			}

			// check out
			if (rosterEntry.getRealEndOfWork() != 0) {
				cal = Calendar.getInstance();
				cal.setTimeInMillis(rosterEntry.getRealEndOfWork());
				abmeldung.setDate(cal);
			}

			// planned start of work
			cal = Calendar.getInstance();
			cal.setTimeInMillis(rosterEntry.getPlannedStartOfWork());
			dienstVon.setDate(cal);

			// planned end of work
			cal = Calendar.getInstance();
			cal.setTimeInMillis(rosterEntry.getPlannedEndOfWork());
			dienstBis.setDate(cal);

			// other fields
			if (rosterEntry.getRosterNotes() != null)
				noteEditor.getDocument().set(rosterEntry.getRosterNotes());
			this.comboDienstverhaeltnis.setSelection(new StructuredSelection(rosterEntry.getServicetype()));
			this.comboVerwendung.setSelection(new StructuredSelection(rosterEntry.getJob()));
			this.comboOrtsstelle.setSelection(new StructuredSelection(rosterEntry.getStation()));
			this.bereitschaftButton.setSelection(rosterEntry.getStandby());
			this.employeenameCombo.setSelection(new StructuredSelection(rosterEntry.getStaffMember()));
		}
		// add the listeners
		NetWrapper.registerListener(this, StaffMember.class);
		NetWrapper.registerListener(this, Location.class);
		NetWrapper.registerListener(this, Job.class);
		NetWrapper.registerListener(this, ServiceType.class);

		return composite;
	}

	/**
	 * The user pressed the ok button
	 */
	@Override
	protected void okPressed() {
		// check the required fileds
		if (checkRequiredFields()) {
			// get all values and create the roster entry
			rosterEntry.setPlannedStartOfWork(dienstVon.getDate().getTimeInMillis());
			rosterEntry.setPlannedEndOfWork(dienstBis.getDate().getTimeInMillis());
			if (anmeldung.getDate() != null && abmeldung.getDate() != null) {
				rosterEntry.setRealStartOfWork(anmeldung.getDate().getTimeInMillis());
				rosterEntry.setRealEndOfWork(abmeldung.getDate().getTimeInMillis());
			}
			// set the needed values
			int index = employeenameCombo.getCombo().getSelectionIndex();
			rosterEntry.setStaffMember((StaffMember) employeenameCombo.getElementAt(index));

			int index3 = comboDienstverhaeltnis.getCombo().getSelectionIndex();
			rosterEntry.setServicetype((ServiceType) comboDienstverhaeltnis.getElementAt(index3));

			int index1 = comboVerwendung.getCombo().getSelectionIndex();
			rosterEntry.setJob((Job) comboVerwendung.getElementAt(index1));

			int index2 = comboOrtsstelle.getCombo().getSelectionIndex();
			rosterEntry.setStation((Location) comboOrtsstelle.getElementAt(index2));

			if (noteEditor.getTextWidget().getText().length() > 400) {
				getShell().getDisplay().beep();
				setErrorMessage("Die Anmerkungen dürfen nicht länger als 400 Zeichen sein.");
				return;
			}

			rosterEntry.setRosterNotes(noteEditor.getTextWidget().getText());
			rosterEntry.setStandby(bereitschaftButton.getSelection());

			// remove the lock
			rosterEntry.setLocked(false);
			rosterEntry.setLockedBy(null);

			// create or update the roster entry
			if (createNew) {
				rosterEntry.setCreatedByUsername(session.getUsername());
				AddMessage<RosterEntry> addMessage = new AddMessage<RosterEntry>(rosterEntry);
				addMessage.asnchronRequest(NetWrapper.getSession());
			}
			else {
				UpdateMessage<RosterEntry> updateMessage = new UpdateMessage<RosterEntry>(rosterEntry);
				updateMessage.asnchronRequest(NetWrapper.getSession());
			}

			// closes the sehll
			super.okPressed();
			return;
		}
		getShell().getDisplay().beep();
	}

	/**
	 * Creates the planing section
	 */
	private void createGeneralSection(Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText("Allgemeine Daten");
		group.setLayout(new GridLayout());
		group.setBackground(CustomColors.SECTION_BACKGROUND);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite client = new Composite(group, SWT.NONE);
		client.setBackground(CustomColors.SECTION_BACKGROUND);
		// layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		client.setLayout(gridLayout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		client.setLayoutData(clientDataLayout);

		final Label labelStaff = new Label(client, SWT.NONE);
		labelStaff.setText("Mitarbeiter:");
		labelStaff.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo combo = new Combo(client, SWT.READ_ONLY);
		employeenameCombo = new ComboViewer(combo);
		employeenameCombo.setContentProvider(new ArrayContentProvider());
		employeenameCombo.setLabelProvider(new StaffMemberLabelProvider());
		employeenameCombo.setInput(staffHandler.toArray());

		final Label labelStation = new Label(client, SWT.NONE);
		labelStation.setText("Ortsstelle:");
		labelStation.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo comboOrts = new Combo(client, SWT.READ_ONLY);
		comboOrtsstelle = new ComboViewer(comboOrts);
		comboOrtsstelle.setContentProvider(new ArrayContentProvider());
		comboOrtsstelle.setLabelProvider(new StationLabelProvider());
		comboOrtsstelle.setInput(locationHandler.toArray());

		bereitschaftButton = new Button(client, SWT.CHECK);
		bereitschaftButton.setText("Bereitschaft");
		bereitschaftButton.setBackground(CustomColors.SECTION_BACKGROUND);
		// should span over two
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		bereitschaftButton.setLayoutData(data);

		final Label labelJob = new Label(client, SWT.NONE);
		labelJob.setText("Verwendung:");
		labelJob.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo comboVerw = new Combo(client, SWT.READ_ONLY);
		comboVerwendung = new ComboViewer(comboVerw);
		comboVerwendung.setContentProvider(new ArrayContentProvider());
		comboVerwendung.setLabelProvider(new JobLabelProvider());
		comboVerwendung.setInput(jobHandler.toArray());

		final Label labelService = new Label(client, SWT.NONE);
		labelService.setText("Dienstverhältnis:");
		labelService.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo comboDienstv = new Combo(client, SWT.READ_ONLY);
		comboDienstverhaeltnis = new ComboViewer(comboDienstv);
		comboDienstverhaeltnis.setContentProvider(new ArrayContentProvider());
		comboDienstverhaeltnis.setLabelProvider(new ServiceTypeLabelProvider());
		comboDienstverhaeltnis.setInput(serviceHandler.toArray());

		// create the section
		Group dayInfoGroup = new Group(client, SWT.NONE);
		dayInfoGroup.setText("Anmerkungen");
		dayInfoGroup.setLayout(new GridLayout());
		dayInfoGroup.setBackground(CustomColors.SECTION_BACKGROUND);
		// info should span over two
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		dayInfoGroup.setLayoutData(data);

		// create the container for the notes
		Composite notesField = toolkit.createComposite(dayInfoGroup);
		notesField.setBackground(CustomColors.SECTION_BACKGROUND);
		notesField.setLayout(new GridLayout());
		GridData notesData = new GridData(GridData.FILL_BOTH);
		notesData.heightHint = 80;
		notesData.grabExcessVerticalSpace = true;
		notesData.grabExcessHorizontalSpace = true;
		notesField.setLayoutData(notesData);

		noteEditor = new TextViewer(notesField, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		noteEditor.setDocument(new Document());
		noteEditor.getControl().setLayoutData(notesData);
		noteEditor.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		noteEditor.setEditable(true);

		// layout for the labels
		data = new GridData();
		data.widthHint = 100;
		labelStaff.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		labelStation.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		labelJob.setLayoutData(data);
		data = new GridData();
		data.widthHint = 100;
		labelService.setLayoutData(data);
		// layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		combo.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		comboOrts.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		comboVerw.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		comboDienstv.setLayoutData(data2);
	}

	/**
	 * Creates the planing section
	 */
	private void createPlaningSection(Composite parent) {
		// create the section
		Group group = new Group(parent, SWT.NONE);
		group.setText("Dienstzeiten");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setBackground(CustomColors.SECTION_BACKGROUND);
		// composite to add the client area
		Composite client = new Composite(group, SWT.NONE);
		client.setBackground(CustomColors.SECTION_BACKGROUND);

		// layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		client.setLayoutData(clientDataLayout);

		// start time
		Composite valueComp = makeComposite(client, 2);
		final Label vonLabel = new Label(valueComp, SWT.NONE);
		vonLabel.setText("Dienst von:");
		vonLabel.setBackground(CustomColors.SECTION_BACKGROUND);
		dienstVon = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);
		dienstVon.setBackground(CustomColors.GREY_COLOR2);

		// end time
		valueComp = makeComposite(client, 2);
		final Label bisLabel = new Label(valueComp, SWT.NONE);
		bisLabel.setText(" bis: ");
		bisLabel.setBackground(CustomColors.SECTION_BACKGROUND);
		dienstBis = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);
		dienstBis.setBackground(CustomColors.GREY_COLOR2);

		// some layout options
		GridData data = new GridData();
		data.widthHint = 70;
		vonLabel.setLayoutData(data);
		data = new GridData();
		data.widthHint = 70;
		bisLabel.setLayoutData(data);
	}

	private Composite makeComposite(Composite parent, int col) {
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(col, false);
		layout.marginHeight = 3;
		nameValueComp.setLayout(layout);
		return nameValueComp;
	}

	/**
	 * Creates the real time section
	 */
	private void createSignSection(Composite parent) {
		// create the section
		Group group = new Group(parent, SWT.NONE);
		group.setBackground(CustomColors.SECTION_BACKGROUND);
		group.setText("Anmeldung zum Dienst / Abmeldung vom Dienst");
		group.setLayout(new GridLayout());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// composite to add the client area
		Composite client = new Composite(group, SWT.NONE);
		client.setBackground(CustomColors.SECTION_BACKGROUND);

		// layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		// real start of work and real end of work
		Composite valueComp = makeComposite(client, 2);
		Label anmeldungLabel = new Label(valueComp, SWT.NONE);
		anmeldungLabel.setText("Anmeldung:");
		anmeldungLabel.setBackground(CustomColors.SECTION_BACKGROUND);
		anmeldung = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);
		anmeldung.setBackground(CustomColors.GREY_COLOR2);

		// label
		valueComp = makeComposite(client, 2);
		Label abmeldungLabel = new Label(valueComp, SWT.NONE);
		abmeldungLabel.setText("Abmeldung:");
		abmeldungLabel.setBackground(CustomColors.SECTION_BACKGROUND);
		abmeldung = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);
		abmeldung.setBackground(CustomColors.GREY_COLOR2);

		// some layout options
		GridData data = new GridData();
		data.widthHint = 70;
		anmeldungLabel.setLayoutData(data);
		data = new GridData();
		data.widthHint = 70;
		abmeldungLabel.setLayoutData(data);
	}

	/**
	 * Helper method to determine wheter all fields are valid
	 * 
	 * @return true if all fields are valid, otherwise false
	 */
	private boolean checkRequiredFields() {
		// check the required fields
		if (employeenameCombo.getCombo().getSelectionIndex() == -1) {
			setErrorMessage("Bitte wählen Sie einen Mitarbeiter aus");
			return false;
		}
		if (comboOrtsstelle.getCombo().getSelectionIndex() == -1) {
			setErrorMessage("Bitte geben Sie eine Ortsstelle an");
			return false;
		}
		if (comboVerwendung.getCombo().getSelectionIndex() == -1) {
			setErrorMessage("Bitte geben Sie eine Verwendung an");
			return false;
		}
		if (comboDienstverhaeltnis.getCombo().getSelectionIndex() == -1) {
			setErrorMessage("Bitte geben Sie ein Dienstverhältnis an");
			return false;
		}
		if (dienstVon.getDate() == null) {
			setErrorMessage("Bitte geben Sie einen Dienstbeginn an");
			return false;
		}
		if (dienstBis.getDate() == null) {
			setErrorMessage("Bitte geben Sie ein Dienstende an");
			return false;
		}
		// validate start before end
		if (dienstVon.getDate().getTimeInMillis() > dienstBis.getDate().getTimeInMillis()) {
			setErrorMessage("Dienstende liegt vor dem Dienstbeginn");
			return false;
		}
		// validate start before end
		if (anmeldung.getDate() != null && abmeldung.getDate() != null) {
			if (anmeldung.getDate().getTimeInMillis() > abmeldung.getDate().getTimeInMillis()) {
				setErrorMessage("Die Anmeldung liegt vor der Abmeldung");
				return false;
			}
		}
		return true;
	}

	@Override
	public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
		employeenameCombo.refresh();
		comboVerwendung.refresh();
		comboOrtsstelle.refresh();
		comboDienstverhaeltnis.refresh();
	}
}
