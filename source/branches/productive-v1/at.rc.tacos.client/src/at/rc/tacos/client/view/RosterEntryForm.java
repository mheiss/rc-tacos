/*******************************************************************************
 * Copyright (c) 2008, 2009 Internettechnik, FH JOANNEUM
 * http://www.fh-joanneum.at/itm
 * 
 * 	Licenced under the GNU GENERAL PUBLIC LICENSE Version 2;
 * 	You may obtain a copy of the License at
 * 	http://www.gnu.org/licenses/gpl-2.0.txt
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *******************************************************************************/
package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
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

import at.rc.tacos.client.modelManager.LockManager;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.modelManager.SessionManager;
import at.rc.tacos.client.providers.JobContentProvider;
import at.rc.tacos.client.providers.JobLabelProvider;
import at.rc.tacos.client.providers.ServiceTypeContentProvider;
import at.rc.tacos.client.providers.ServiceTypeLabelProvider;
import at.rc.tacos.client.providers.StaffMemberContentProvider;
import at.rc.tacos.client.providers.StaffMemberLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

/**
 * GUI (form) to manage a roster entry
 * 
 * @author b.thek
 */
public class RosterEntryForm extends TitleAreaDialog implements PropertyChangeListener {

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

	// determine wheter to update or to create a new entry
	private boolean createNew;

	/**
	 * Default class constructor used to create a new roster entry.
	 * 
	 * @param shell
	 *            the parent shell
	 */
	public RosterEntryForm(Shell parentShell) {
		super(parentShell);
		createNew = true;
		// bind the staff to this view
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
	}

	/**
	 * Default class constructor to edit an existing entry
	 * 
	 * @param shell
	 *            the parent shell
	 * @param rosterEntry
	 *            the roster entry to edit
	 */
	public RosterEntryForm(Shell parentShell, RosterEntry rosterEntry) {
		super(parentShell);
		// update an entry
		createNew = false;
		this.rosterEntry = rosterEntry;
		// bind the staff to this view
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
	}

	@Override
	public boolean close() {
		// check if the user wants to close the window
		if (getReturnCode() == CANCEL) {
			// confirm exit
			boolean exit = MessageDialog.openQuestion(getShell(), "Abbrechen", "Wollen Sie wirklich abbrechen?");
			// check the result
			if (!exit)
				return false;
		}

		// remove the lock from the object
		if (!createNew)
			LockManager.removeLock(RosterEntry.ID, rosterEntry.getRosterId());

		// cleanup the listeners
		ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().removePropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().removePropertyChangeListener(this);

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
		setMessage("Hier k�nnen Sie einen neuen Dienstplaneintrag anlegen", IMessageProvider.INFORMATION);
		setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
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
		if (rosterEntry != null) {
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
		return composite;
	}

	/**
	 * The user pressed the ok button
	 */
	@Override
	protected void okPressed() {
		// check the required fileds
		if (checkRequiredFields()) {
			if (rosterEntry == null)
				rosterEntry = new RosterEntry();
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
				setErrorMessage("Die Anmerkungen d�rfen nicht l�nger als 400 Zeichen sein.");
				return;
			}

			rosterEntry.setRosterNotes(noteEditor.getTextWidget().getText());
			rosterEntry.setStandby(bereitschaftButton.getSelection());
			rosterEntry.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());

			// create or update the roster entry
			if (createNew)
				NetWrapper.getDefault().sendAddMessage(RosterEntry.ID, rosterEntry);
			else
				NetWrapper.getDefault().sendUpdateMessage(RosterEntry.ID, rosterEntry);

			// closes the sehll
			super.okPressed();
			return;
		}
		// beep
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
		employeenameCombo.setContentProvider(new StaffMemberContentProvider());
		employeenameCombo.setLabelProvider(new StaffMemberLabelProvider());
		employeenameCombo.setInput(ModelFactory.getInstance().getStaffManager());

		final Label labelStation = new Label(client, SWT.NONE);
		labelStation.setText("Ortsstelle:");
		labelStation.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo comboOrts = new Combo(client, SWT.READ_ONLY);
		comboOrtsstelle = new ComboViewer(comboOrts);
		comboOrtsstelle.setContentProvider(new StationContentProvider());
		comboOrtsstelle.setLabelProvider(new StationLabelProvider());
		comboOrtsstelle.setInput(ModelFactory.getInstance().getJobList());

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
		comboVerwendung.setContentProvider(new JobContentProvider());
		comboVerwendung.setLabelProvider(new JobLabelProvider());
		comboVerwendung.setInput(ModelFactory.getInstance().getJobList());

		final Label labelService = new Label(client, SWT.NONE);
		labelService.setText("Dienstverh�ltnis:");
		labelService.setBackground(CustomColors.SECTION_BACKGROUND);

		Combo comboDienstv = new Combo(client, SWT.READ_ONLY);
		comboDienstverhaeltnis = new ComboViewer(comboDienstv);
		comboDienstverhaeltnis.setContentProvider(new ServiceTypeContentProvider());
		comboDienstverhaeltnis.setLabelProvider(new ServiceTypeLabelProvider());
		comboDienstverhaeltnis.setInput(ModelFactory.getInstance().getServiceManager());

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
			setErrorMessage("Bitte w�hlen Sie einen Mitarbeiter aus");
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
			setErrorMessage("Bitte geben Sie ein Dienstverh�ltnis an");
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
	public void propertyChange(PropertyChangeEvent evt) {
		// the viewer represents simple model. refresh should be enough.
		if ("STAFF_ADD".equals(evt.getPropertyName()) || "STAFF_REMOVE".equals(evt.getPropertyName()) || "STAFF_UPDATE".equals(evt.getPropertyName())
				|| "STAFF_CLEARED".equals(evt.getPropertyName())) {
			employeenameCombo.refresh();
		}
		// keep on track on job changes
		if ("JOB_ADD".equals(evt.getPropertyName()) || "JOB_REMOVE".equals(evt.getPropertyName()) || "JOB_UPDATE".equals(evt.getPropertyName())
				|| "KPB_CLEARED".equals(evt.getPropertyName())) {
			comboVerwendung.refresh();
		}
		// keep on track on location changes
		if ("LOCATION_ADD".equals(evt.getPropertyName()) || "LOCATION_REMOVE".equals(evt.getPropertyName())
				|| "LOCATION_UPDATE".equals(evt.getPropertyName()) || "LOCATION_CLEARED".equals(evt.getPropertyName())) {
			comboOrtsstelle.refresh();
		}
		// keep on track on service type changes
		if ("SERVICETYPE_ADD".equals(evt.getPropertyName()) || "SERVICETYPE_REMOVE".equals(evt.getPropertyName())
				|| "SERVICETYPE_UPDATE".equals(evt.getPropertyName()) || "SERVICETYPE_CLEARED".equals(evt.getPropertyName())) {
			comboDienstverhaeltnis.refresh();
		}
	}
}
