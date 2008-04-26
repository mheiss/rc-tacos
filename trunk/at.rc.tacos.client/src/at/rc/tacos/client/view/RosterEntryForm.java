package at.rc.tacos.client.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Calendar;

import org.eclipse.jface.dialogs.IMessageProvider;
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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import at.rc.tacos.client.controller.PersonalCreateEntryAction;
import at.rc.tacos.client.controller.PersonalUpdateEntryAction;
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
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Job;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.RosterEntry;
import at.rc.tacos.model.ServiceType;
import at.rc.tacos.model.StaffMember;

/**
 * GUI (form) to manage a roster entry
 * @author b.thek
 */
public class RosterEntryForm extends TitleAreaDialog implements PropertyChangeListener
{
	private FormToolkit toolkit;
	private TextViewer noteEditor;
	private ComboViewer comboDienstverhaeltnis;
	private ComboViewer comboVerwendung;
	private ComboViewer comboOrtsstelle;
	private Button bereitschaftButton;
	private ComboViewer employeenameCombo;

	//the date pickers
	private DatePicker dienstVon;
	private DatePicker dienstBis;
	private DatePicker anmeldung;
	private DatePicker abmeldung;

	//the roster entry
	private RosterEntry rosterEntry;

	//determine wheter to update or to create a new entry
	private boolean createNew;

	/**
	 * Default class constructor used to create a new roster entry.
	 * @param shell the parent shell
	 */
	public RosterEntryForm(Shell parentShell)
	{
		super(parentShell);
		createNew = true;
		//bind the staff to this view
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
	}

	/**
	 * Default class constructor to edit an existing entry
	 * @param shell the parent shell
	 * @param rosterEntry the roster entry to edit
	 */
	public RosterEntryForm(Shell parentShell,RosterEntry rosterEntry)
	{
		super(parentShell);
		//update an entry
		createNew = false;
		this.rosterEntry = rosterEntry;
		//bind the staff to this view
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().addPropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().addPropertyChangeListener(this);
	}	

	/**
	 * Cleanup the dialog when it is closed
	 */
	@Override
	public boolean close()
	{
		ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getJobList().removePropertyChangeListener(this);
		ModelFactory.getInstance().getServiceManager().removePropertyChangeListener(this);
		return super.close();
	}

	/**
	 * Creates the dialog's contents
	 * @param parent the parent composite
	 * @return Control
	 */
	@Override
	protected Control createContents(Composite parent) 
	{
		Control contents = super.createContents(parent);
		setTitle("Dienstplaneintrag");
		setMessage("Hier können Sie einen neuen Dienstplaneintrag anlegen", IMessageProvider.INFORMATION);
		setTitleImage(ImageFactory.getInstance().getRegisteredImage("application.logo"));
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
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));

		//create the content of the dialog
		createGeneralSection(composite);
		createPlaningSection(composite);
		createSignSection(composite);

		//init data
		if(rosterEntry != null)
		{
			Calendar cal = Calendar.getInstance();

			//check in
			if(rosterEntry.getRealStartOfWork() != 0)
			{
				cal.setTimeInMillis(rosterEntry.getRealStartOfWork());
				anmeldung.setDate(cal);
			}

			//check out
			if(rosterEntry.getRealEndOfWork() != 0)
			{
				cal = Calendar.getInstance();
				cal.setTimeInMillis(rosterEntry.getRealEndOfWork());
				abmeldung.setDate(cal);
			}          
			//planned start of work
			cal = Calendar.getInstance();
			cal.setTimeInMillis(rosterEntry.getPlannedStartOfWork());
			dienstVon.setDate(cal);

			//planned end of work
			cal = Calendar.getInstance();
			cal.setTimeInMillis(rosterEntry.getPlannedEndOfWork());
			dienstBis.setDate(cal);

			//other fields
			if(rosterEntry.getRosterNotes() != null)
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
	 * The user pressed the cancel button
	 */
	@Override
	protected void cancelPressed()
	{
		MessageBox dialog = new MessageBox(getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
		dialog.setText("Abbrechen");
		dialog.setMessage("Wollen Sie wirklich abbrechen?");
		//check the result
		if (dialog.open() != SWT.NO)
			getShell().close();
	}

	/**
	 * The user pressed the ok button
	 */
	@Override
	protected void okPressed()
	{
		//check the required fileds
		if(checkRequiredFields())
		{
			if(rosterEntry == null)
				rosterEntry = new RosterEntry();
			//get all values and create the roster entry
			rosterEntry.setPlannedStartOfWork(dienstVon.getDate().getTimeInMillis());
			rosterEntry.setPlannedEndOfWork(dienstBis.getDate().getTimeInMillis());
			if(anmeldung.getDate() != null && abmeldung.getDate() != null)
			{
				rosterEntry.setRealStartOfWork(anmeldung.getDate().getTimeInMillis());
				rosterEntry.setRealEndOfWork(abmeldung.getDate().getTimeInMillis());
			}
			// set the needed values
			int index = employeenameCombo.getCombo().getSelectionIndex();
			rosterEntry.setStaffMember((StaffMember)employeenameCombo.getElementAt(index));

			int index3 = comboDienstverhaeltnis.getCombo().getSelectionIndex();
			rosterEntry.setServicetype((ServiceType)comboDienstverhaeltnis.getElementAt(index3));

			int index1 = comboVerwendung.getCombo().getSelectionIndex();
			rosterEntry.setJob((Job)comboVerwendung.getElementAt(index1));

			int index2 = comboOrtsstelle.getCombo().getSelectionIndex();
			rosterEntry.setStation((Location)comboOrtsstelle.getElementAt(index2));
			System.out.println(rosterEntry.getStation());

			rosterEntry.setRosterNotes(noteEditor.getTextWidget().getText());
			rosterEntry.setStandby(bereitschaftButton.getSelection());
			rosterEntry.setCreatedByUsername(SessionManager.getInstance().getLoginInformation().getUsername());

			//create a new entry
			if(createNew)
			{
				//create and run the add action
				PersonalCreateEntryAction newAction = new PersonalCreateEntryAction(rosterEntry);
				newAction.run();
			}
			else
			{
				//create and run the update action
				PersonalUpdateEntryAction updateAction = new PersonalUpdateEntryAction(rosterEntry);
				updateAction.run();
			}
			getShell().close();
			return;
		}
		//beep
		getShell().getDisplay().beep();
	}

	/**
	 * Creates the planing section
	 */
	private void createGeneralSection(Composite parent)
	{
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText("Allgemeine Daten");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setExpanded(true);
		section.addExpansionListener(new IExpansionListener() 
		{
			public void expansionStateChanging(ExpansionEvent e) 
			{
				getShell().pack(true);
			}

			public void expansionStateChanged(ExpansionEvent e) 
			{
				getShell().pack(true);
			}
		});

		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);
		//layout
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		client.setLayout(gridLayout);

		final Label labelStaff = new Label(client, SWT.NONE);
		labelStaff.setText("Mitarbeiter:");

		Combo combo = new Combo(client, SWT.READ_ONLY);
		employeenameCombo = new ComboViewer(combo);
		employeenameCombo.setContentProvider(new StaffMemberContentProvider());
		employeenameCombo.setLabelProvider(new StaffMemberLabelProvider());
		employeenameCombo.setInput(ModelFactory.getInstance().getStaffManager());

		final Label labelStation = new Label(client, SWT.NONE);
		labelStation.setText("Ortsstelle:");

		Combo comboOrts = new Combo(client, SWT.READ_ONLY);
		comboOrtsstelle = new ComboViewer(comboOrts);
		comboOrtsstelle.setContentProvider(new StationContentProvider());
		comboOrtsstelle.setLabelProvider(new StationLabelProvider());
		comboOrtsstelle.setInput(ModelFactory.getInstance().getJobList());

		bereitschaftButton = new Button(client, SWT.CHECK);
		bereitschaftButton.setText("Bereitschaft");
		//should span over two
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		bereitschaftButton.setLayoutData(data);

		final Label labelJob = new Label(client, SWT.NONE);
		labelJob.setText("Verwendung:");

		Combo comboVerw = new Combo(client, SWT.READ_ONLY);
		comboVerwendung = new ComboViewer(comboVerw);
		comboVerwendung.setContentProvider(new JobContentProvider());
		comboVerwendung.setLabelProvider(new JobLabelProvider());
		comboVerwendung.setInput(ModelFactory.getInstance().getJobList());

		final Label labelService = new Label(client, SWT.NONE);
		labelService.setText("Dienstverhältnis:");

		Combo comboDienstv = new Combo(client,SWT.READ_ONLY);
		comboDienstverhaeltnis = new ComboViewer(comboDienstv);
		comboDienstverhaeltnis.setContentProvider(new ServiceTypeContentProvider());
		comboDienstverhaeltnis.setLabelProvider(new ServiceTypeLabelProvider());
		comboDienstverhaeltnis.setInput(ModelFactory.getInstance().getServiceManager());

		//create the section
		Section dayInfoSection = toolkit.createSection(client, ExpandableComposite.TITLE_BAR);
		toolkit.createCompositeSeparator(dayInfoSection);
		dayInfoSection.setText("Anmerkungen");
		dayInfoSection.setExpanded(true);
		dayInfoSection.setLayout(new GridLayout());
		//info should span over two
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		dayInfoSection.setLayoutData(data);

		//create the container for the notes
		Composite notesField = toolkit.createComposite(dayInfoSection);
		dayInfoSection.setClient(notesField);
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

		//layout for the labels
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
		//layout for the text fields
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
	private void createPlaningSection(Composite parent)
	{
		//create the section
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText("Dienstzeiten");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		section.setExpanded(true);
		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		client.setLayoutData(clientDataLayout);

		//start time
		Composite valueComp = makeComposite(client, 2);
		final Label vonLabel = new Label(valueComp, SWT.NONE);
		vonLabel.setText("Dienst von:");
		dienstVon = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);

		//end time
		valueComp = makeComposite(client, 2);
		final Label bisLabel = new Label(valueComp, SWT.NONE);
		bisLabel.setText(" bis: ");
		dienstBis = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);
	}

	private Composite makeComposite(Composite parent, int col) 
	{
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(col, false);
		layout.marginHeight = 3;
		nameValueComp.setLayout(layout);
		return nameValueComp;
	}

	/**
	 * Creates the real time section
	 */
	private void createSignSection(Composite parent)
	{
		//create the section
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText("Anmeldung zum Dienst / Abmeldung vom Dienst");
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (createNew)
			section.setExpanded(false);
		else
			section.setExpanded(true);

		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 15;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		client.setLayoutData(clientDataLayout);

		//real start of work and real end of work
		Composite valueComp = makeComposite(client, 2);
		Label anmeldungLabel = new Label(valueComp, SWT.NONE);
		anmeldungLabel.setText("Anmeldung:");
		anmeldung = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);

		//label 
		valueComp = makeComposite(client, 2);
		Label abmeldungLabel = new Label(valueComp, SWT.NONE);
		abmeldungLabel.setText("Abmeldung:");
		abmeldung = new DatePicker(valueComp, SWT.FLAT, DatePicker.LABEL_CHOOSE);
	}

	/**
	 * Helper method to determine wheter all fields are valid
	 * @return true if all fields are valid, otherwise false
	 */
	private boolean checkRequiredFields()
	{
		//check the required fields
		if (employeenameCombo.getCombo().getSelectionIndex() == -1)
		{
			setErrorMessage("Bitte wählen Sie einen Mitarbeiter aus");
			return false;
		}
		if (comboOrtsstelle.getCombo().getSelectionIndex() == -1)
		{
			setErrorMessage("Bitte geben Sie eine Ortsstelle an");
			return false;
		}
		if (comboVerwendung.getCombo().getSelectionIndex() == -1)
		{
			setErrorMessage("Bitte geben Sie eine Verwendung an");
			return false;
		}
		if (comboDienstverhaeltnis.getCombo().getSelectionIndex() == -1)
		{
			setErrorMessage("Bitte geben Sie ein Dienstverhältnis an");
			return false;
		}
		if (dienstVon.getDate() == null)
		{
			setErrorMessage("Bitte geben Sie einen Dienstbegin an");
			return false;
		}
		if (dienstBis.getDate() == null)
		{
			setErrorMessage("Bitte geben Sie ein Dienstende an");
			return false;
		}
		//validate start before end
		if(dienstVon.getDate().getTimeInMillis() > dienstBis.getDate().getTimeInMillis())
		{
			setErrorMessage("Dienstende liegt vor dem Dienstbeginn");
			return false;
		}
		//validate start before end
		if(anmeldung.getDate() != null && abmeldung.getDate() != null)
		{
			if(anmeldung.getDate().getTimeInMillis() > abmeldung.getDate().getTimeInMillis())
			{
				setErrorMessage("Die Anmeldung liegt vor der Abmeldung");
				return false;
			}
		}
		return true;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		// the viewer represents simple model. refresh should be enough.
		if ("STAFF_ADD".equals(evt.getPropertyName())
				|| "STAFF_REMOVE".equals(evt.getPropertyName()) 
				|| "STAFF_UPDATE".equals(evt.getPropertyName())
				||"STAFF_CLEARED".equals(evt.getPropertyName()))
		{ 
			employeenameCombo.refresh();
		}
		//keep on track on job changes
		if ("JOB_ADD".equals(evt.getPropertyName())
				|| "JOB_REMOVE".equals(evt.getPropertyName()) 
				|| "JOB_UPDATE".equals(evt.getPropertyName())
				||"KPB_CLEARED".equals(evt.getPropertyName()))
		{ 
			comboVerwendung.refresh();
		}
		//keep on track on location changes
		if ("LOCATION_ADD".equals(evt.getPropertyName())
				|| "LOCATION_REMOVE".equals(evt.getPropertyName()) 
				|| "LOCATION_UPDATE".equals(evt.getPropertyName())
				||"LOCATION_CLEARED".equals(evt.getPropertyName()))
		{ 
			comboOrtsstelle.refresh();
		}
		//keep on track on service type changes
		if("SERVICETYPE_ADD".equals(evt.getPropertyName())
				|| "SERVICETYPE_REMOVE".equals(evt.getPropertyName()) 
				|| "SERVICETYPE_UPDATE".equals(evt.getPropertyName())
				||"SERVICETYPE_CLEARED".equals(evt.getPropertyName()))
		{ 
			comboDienstverhaeltnis.refresh();
		}
	}
}
