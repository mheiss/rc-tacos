package at.rc.tacos.client.ui.admin.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPartConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.rc.tacos.client.net.NetWrapper;
import at.rc.tacos.client.net.handler.CompetenceHandler;
import at.rc.tacos.client.net.handler.LocationHandler;
import at.rc.tacos.client.ui.UiWrapper;
import at.rc.tacos.client.ui.controller.EditorSaveAction;
import at.rc.tacos.client.ui.providers.CompetenceLabelProvider;
import at.rc.tacos.client.ui.providers.StationLabelProvider;
import at.rc.tacos.client.ui.utils.CustomColors;
import at.rc.tacos.platform.model.Competence;
import at.rc.tacos.platform.model.Location;
import at.rc.tacos.platform.model.Login;
import at.rc.tacos.platform.model.StaffMember;
import at.rc.tacos.platform.net.Message;
import at.rc.tacos.platform.net.listeners.DataChangeListener;
import at.rc.tacos.platform.net.message.AddMessage;
import at.rc.tacos.platform.net.message.UpdateMessage;
import at.rc.tacos.platform.net.mina.MessageIoSession;

public class StaffMemberEditor extends EditorPart implements DataChangeListener<Object> {

	public static final String ID = "at.rc.tacos.client.editors.staffMemberEditor";

	// the toolkit
	private FormToolkit toolkit;
	// the form holding the widgets
	private ScrolledForm form;

	// the values that can be changed
	private CLabel infoLabel;
	private Text staffId, fName, lName, dateOfBirth;
	private Text uName, pwd, pwdRetype;
	private TableViewer competenceViewer;
	private ComboViewer primaryLocationComboViewer, competenceComboViewer, authorisationComboViewer, sexComboViewer;
	private Button locked;
	private Hyperlink removeCompetence, addCompetence;
	private ImageHyperlink saveHyperlink;
	private Text phone1, phone2;

	// temp competence
	private List<Competence> compList;

	// indicates non-saved changes
	protected boolean isDirty;

	// the staff member to manage
	private boolean isNew;
	private StaffMember staffMember;
	private Login loginInfo;

	// the needed data handlers
	private LocationHandler locationHandler = (LocationHandler) NetWrapper.getHandler(Location.class);
	private CompetenceHandler competenceHandler = (CompetenceHandler) NetWrapper.getHandler(Competence.class);

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	@Override
	public void createPartControl(final Composite parent) {
		compList = new ArrayList<Competence>();

		// save the staff member
		staffMember = ((StaffMemberEditorInput) getEditorInput()).getStaffMember();
		loginInfo = ((StaffMemberEditorInput) getEditorInput()).getLoginInformation();
		isNew = ((StaffMemberEditorInput) getEditorInput()).isNew();
		isDirty = false;

		// Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());

		Composite composite = form.getBody();
		composite.setLayout(new GridLayout(2, false));

		Composite manage = createManageSection(composite);
		createGeneralSection(composite);
		createLoginSection(composite);
		createServiceSection(composite);

		// set the layout
		GridData data = new GridData(GridData.BEGINNING);
		data.horizontalSpan = 2;
		manage.getParent().setLayoutData(data);

		// load the data
		loadData();

		// force redraw
		form.pack(true);

		// access authority only for admins and the own (logged in) staffMember
		Login currentUser = NetWrapper.getSession().getLogin();
		StaffMember loggedInMember = currentUser.getUserInformation();
		if (loggedInMember.getStaffMemberId() == staffMember.getStaffMemberId())
			form.setEnabled(true);
		else
			form.setEnabled(false);
		if (currentUser.getAuthorization().equalsIgnoreCase(Login.AUTH_ADMIN))
			form.setEnabled(true);

		// prevent authorization changing of not admins for itself
		if (!currentUser.getAuthorization().equalsIgnoreCase(Login.AUTH_ADMIN))
			authorisationComboViewer.getCombo().setEnabled(false);

		// register the listeners
		NetWrapper.registerListener(this, StaffMember.class);
		NetWrapper.registerListener(this, Login.class);
		NetWrapper.registerListener(this, Competence.class);
		NetWrapper.registerListener(this, Location.class);
	}

	@Override
	public void dispose() {
		NetWrapper.removeListener(this, StaffMember.class);
		NetWrapper.removeListener(this, Login.class);
		NetWrapper.removeListener(this, Competence.class);
		NetWrapper.removeListener(this, Location.class);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData() {
		// init the editor
		if (isNew) {
			form.setText("Neuen Mitarbeiter anlegen");
			return;
		}

		// set the data of the staff member
		form.setText("Details des Mitarbeiters " + staffMember.getFirstName() + " " + staffMember.getLastName());
		staffId.setText(String.valueOf(staffMember.getStaffMemberId()));
		fName.setText(staffMember.getFirstName());
		lName.setText(staffMember.getLastName());
		if (staffMember.getPhone1() != null)
			phone1.setText(staffMember.getPhone1());
		if (staffMember.getPhone2() != null)
			phone2.setText(staffMember.getPhone2());
		if (staffMember.getBirthday() != null)
			dateOfBirth.setText(staffMember.getBirthday());
		if (staffMember.isMale())
			sexComboViewer.setSelection(new StructuredSelection(StaffMember.STAFF_MALE));
		else
			sexComboViewer.setSelection(new StructuredSelection(StaffMember.STAFF_FEMALE));
		primaryLocationComboViewer.setSelection(new StructuredSelection(staffMember.getPrimaryLocation()));
		// update the login
		uName.setText(loginInfo.getUsername());
		locked.setSelection(loginInfo.isIslocked());
		authorisationComboViewer.setSelection(new StructuredSelection(loginInfo.getAuthorization()));

		// update the competence view
		compList.clear();
		compList.addAll(staffMember.getCompetenceList());
		competenceViewer.refresh(true);

		// personal numer is not changeable
		staffId.setEditable(false);
		staffId.setBackground(CustomColors.GREY_COLOR);
		// username is not editable
		uName.setEditable(false);
		uName.setBackground(CustomColors.GREY_COLOR);
		uName.setToolTipText("Der Benutzername kann nicht verändert werden");

		// validate
		inputChanged();
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() {
		form.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// reset error message
		form.setMessage(null, IMessageProvider.NONE);

		// validate the staff id
		String pattern = "5\\d{7}";
		if (!staffId.getText().matches(pattern)) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine gültige Personalnummer in der Form 5xxxxxxx ein.", IMessageProvider.ERROR);
			return;
		}
		staffMember.setStaffMemberId(Integer.parseInt(staffId.getText()));

		// save the input in the staff member
		if (fName.getText().length() > 30 || fName.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen gültigen Vornamen ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		staffMember.setFirstName(fName.getText());

		// Set the lastname
		if (lName.getText().length() > 30 || lName.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen gültigen Nachnamen ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		staffMember.setLastName(lName.getText());

		// set the phone 1 and phone 2
		if (phone1.getText().length() > 50) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine gültige Telefonnummer(1) (max. 50 Zeichen) ein", IMessageProvider.ERROR);
			return;
		}
		if (!phone1.getText().trim().isEmpty())
			staffMember.setPhone1(phone1.getText());

		if (phone2.getText().length() > 50) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine gültige Telefonnummer(2) (max. 50 Zeichen) ein", IMessageProvider.ERROR);
			return;
		}
		if (!phone2.getText().trim().isEmpty())
			staffMember.setPhone2(phone2.getText());

		// date of birth
		String patternDate = "\\d{2}\\-\\d{2}-\\d{4}";
		if (!dateOfBirth.getText().trim().isEmpty() & !dateOfBirth.getText().matches(patternDate)) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben sie ein Geburtsdatum in der Form dd-mm-yyyy ein", IMessageProvider.ERROR);
			return;
		}
		if (!dateOfBirth.getText().trim().isEmpty())
			staffMember.setBirthday(dateOfBirth.getText());
		// sex
		int index = sexComboViewer.getCombo().getSelectionIndex();
		if (index != -1) {
			String selectedSex = (String) sexComboViewer.getElementAt(index);
			if (selectedSex.equalsIgnoreCase(StaffMember.STAFF_MALE))
				staffMember.setMale(true);
			else
				staffMember.setMale(false);
		}

		// the location
		index = primaryLocationComboViewer.getCombo().getSelectionIndex();
		if (index == -1) {
			form.getDisplay().beep();
			form.setMessage("Bitte wählen sie die primäre Dienstelle des Mitarbeiters aus", IMessageProvider.ERROR);
			return;
		}
		staffMember.setPrimaryLocation((Location) primaryLocationComboViewer.getElementAt(index));

		// check the username
		if (uName.getText().length() > 30 || uName.getText().trim().isEmpty()) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen gültigen Benutzernamen ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		loginInfo.setUsername(uName.getText());
		staffMember.setUserName(uName.getText());

		// a new entry must have a password
		if (isNew & (pwd.getText().trim().isEmpty() || pwdRetype.getText().trim().isEmpty())) {
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie ein Passwort ein.", IMessageProvider.ERROR);
			return;
		}

		// the password
		if (!pwd.getText().trim().isEmpty() | !pwdRetype.getText().trim().isEmpty()) {
			// check if they are equal
			if (pwd.getText().equals(pwdRetype.getText()))
				loginInfo.setPassword(pwd.getText());
			else {
				form.getDisplay().beep();
				form.setMessage("Die eingegebenen Passwörter stimmen nicht überein", IMessageProvider.ERROR);
				return;
			}
		}

		// authorization
		index = authorisationComboViewer.getCombo().getSelectionIndex();
		if (index == -1) {
			form.getDisplay().beep();
			form.setMessage("Bitte wählen sie die Berechtigungen des Benutzers aus", IMessageProvider.ERROR);
			return;
		}
		loginInfo.setAuthorization((String) authorisationComboViewer.getElementAt(index));

		// locked
		loginInfo.setIslocked(locked.getSelection());

		// add the competences
		staffMember.getCompetenceList().clear();
		staffMember.getCompetenceList().addAll(compList);

		// add or update the staff member and the login
		if (isNew) {
			AddMessage<Login> addLogin = new AddMessage<Login>(loginInfo);
			addLogin.asnchronRequest(NetWrapper.getSession());

			AddMessage<StaffMember> addStaff = new AddMessage<StaffMember>(staffMember);
			addStaff.asnchronRequest(NetWrapper.getSession());
		}
		else {
			UpdateMessage<Login> updateLogin = new UpdateMessage<Login>(loginInfo);
			updateLogin.asnchronRequest(NetWrapper.getSession());

			UpdateMessage<StaffMember> updateStaff = new UpdateMessage<StaffMember>(staffMember);
			updateStaff.asnchronRequest(NetWrapper.getSession());
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSaveAs() {
		// don't support saving as
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Creates the section to manage the changes
	 */
	public Composite createManageSection(Composite parent) {
		Composite client = createSection(parent, "Mitarbeiter verwalten");

		// create info label and hyperlinks to save and revert the changes
		infoLabel = new CLabel(client, SWT.NONE);
		infoLabel.setText("Hier können sie den aktuellen Mitarbeiter verwalten und die Änderungen speichern.");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));

		// Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Änderungen speichern");
		saveHyperlink.setEnabled(false);
		saveHyperlink.setForeground(CustomColors.GREY_COLOR);
		saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		// info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		data.widthHint = 600;
		infoLabel.setLayoutData(data);

		return client;
	}

	/**
	 * Creates the general section
	 */
	private Composite createGeneralSection(Composite parent) {
		Composite client = createSection(parent, "Allgemeine Daten");

		// create the label and the input field
		final Label labelStaffId = toolkit.createLabel(client, "Personalnummer");
		staffId = toolkit.createText(client, "");
		staffId.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelFName = toolkit.createLabel(client, "Vorname");
		fName = toolkit.createText(client, "");
		fName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelLName = toolkit.createLabel(client, "Nachname");
		lName = toolkit.createText(client, "");
		lName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelPhone1 = toolkit.createLabel(client, "Telefon 1");
		phone1 = toolkit.createText(client, "");
		phone1.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelPhone2 = toolkit.createLabel(client, "Telefon 2");
		phone2 = toolkit.createText(client, "");
		phone2.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelDateOfBirth = toolkit.createLabel(client, "Geburtsdatum");
		dateOfBirth = toolkit.createText(client, "");
		dateOfBirth.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelSex = toolkit.createLabel(client, "Geschlecht");
		Combo sexCombo = new Combo(client, SWT.READ_ONLY);
		sexComboViewer = new ComboViewer(sexCombo);
		sexComboViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object arg0) {
				return StaffMember.STAFF;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}
		});
		sexComboViewer.setInput(new String[] { StaffMember.STAFF_MALE, StaffMember.STAFF_FEMALE });
		sexCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				inputChanged();
			}
		});

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelStaffId.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelFName.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelLName.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhone1.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPhone2.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelDateOfBirth.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelSex.setLayoutData(data);

		// layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 150;
		staffId.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 150;
		fName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 150;
		lName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 150;
		dateOfBirth.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		sexComboViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 150;
		phone1.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.widthHint = 150;
		phone2.setLayoutData(data2);

		return client;
	}

	/**
	 * Creates the service section
	 */
	private Composite createServiceSection(Composite parent) {
		Composite client = createSection(parent, "Ausbildung und Dienststelle");

		// create the label and the dropdown field
		final Label locationLabel = toolkit.createLabel(client, "Dienststelle");

		Combo stationCombo = new Combo(client, SWT.READ_ONLY);
		primaryLocationComboViewer = new ComboViewer(stationCombo);
		primaryLocationComboViewer.setContentProvider(new ArrayContentProvider());
		primaryLocationComboViewer.setLabelProvider(new StationLabelProvider());
		primaryLocationComboViewer.setInput(locationHandler.toArray());
		stationCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		// phone list
		final Label labelCompetence = toolkit.createLabel(client, "Verfügbare Kompetenzen: ");

		// make a subcomposite holding the Hyperlinks and the viewer
		Composite compManage = makeComposite(client, 3);

		Combo comboVerw = new Combo(compManage, SWT.READ_ONLY);
		competenceComboViewer = new ComboViewer(comboVerw);
		competenceComboViewer.setContentProvider(new ArrayContentProvider());
		competenceComboViewer.setLabelProvider(new CompetenceLabelProvider());
		competenceComboViewer.setInput(competenceHandler.toArray());

		addCompetence = toolkit.createHyperlink(compManage, "hinzufügen", SWT.NONE);
		addCompetence.setToolTipText("Die gewählte Kompetenz dem Mitarbeiter zuweisen.");
		addCompetence.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				form.setMessage(null, IMessageProvider.NONE);
				// get the selected item
				int index = competenceComboViewer.getCombo().getSelectionIndex();
				Competence comp = (Competence) competenceComboViewer.getElementAt(index);
				if (compList.contains(comp)) {
					form.getShell().getDisplay().beep();
					form.setMessage("Dem Mitarbeiter wurde diese Kompetenz bereits zugewiesen.", IMessageProvider.ERROR);
				}
				else {
					compList.add(comp);
					competenceViewer.refresh();
					inputChanged();
				}
			}
		});
		// hyperlink to remove a competence
		removeCompetence = toolkit.createHyperlink(compManage, "entfernen", SWT.NONE);
		removeCompetence.setToolTipText("Die selektierte Kompetenz dem Mitarbeiter wieder entziehen.");
		removeCompetence.addHyperlinkListener(new HyperlinkAdapter() {

			@Override
			public void linkActivated(HyperlinkEvent e) {
				form.setMessage(null, IMessageProvider.NONE);
				// get the selected element
				ISelection selection = competenceViewer.getSelection();
				if (!selection.isEmpty()) {
					// get the selected object
					Object selectedCompetence = ((IStructuredSelection) selection).getFirstElement();
					compList.remove(selectedCompetence);
					competenceViewer.refresh(true);
					inputChanged();
				}
				else {
					form.getShell().getDisplay().beep();
					form.setMessage("Bitte wählen sie eine Kompetenz aus die entfernt werden soll", IMessageProvider.ERROR);
				}
			}
		});

		final Label labelManage = toolkit.createLabel(client, "Kompetenzen des Mitarbeiters: ");
		// create the table holding the competences
		competenceViewer = new TableViewer(client, SWT.BORDER);
		competenceViewer.setLabelProvider(new CompetenceLabelProvider());
		// set this staff members competences as content provider
		competenceViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object arg0) {
				return compList.toArray();
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}
		});
		competenceViewer.setInput(compList);

		// layout the composites
		GridData data = new GridData();
		data.widthHint = 150;
		locationLabel.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCompetence.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelManage.setLayoutData(data);

		// layout for the input fields
		GridData data2 = new GridData(GridData.FILL_BOTH | GridData.BEGINNING);
		primaryLocationComboViewer.getCombo().setLayoutData(data2);

		// Layout the combo
		data2 = new GridData(GridData.FILL_BOTH | GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING);
		competenceComboViewer.getCombo().setLayoutData(data2);

		// layout the table
		competenceViewer.getTable().setLayout(new GridLayout());
		data2 = new GridData(GridData.FILL_BOTH | GridData.BEGINNING);
		data2.heightHint = 50;
		competenceViewer.getTable().setLayoutData(data2);

		return client;
	}

	/**
	 * Creates the login section
	 */
	private Composite createLoginSection(Composite parent) {
		Composite client = createSection(parent, "Daten zum Anmelden am System und Online-Dienstplan");

		// create the label and the input field
		final Label labelUsername = toolkit.createLabel(client, "Username: ");
		uName = toolkit.createText(client, "");
		uName.setToolTipText("Der Benutzername mit dem sich der Mitarbeiter am OnlineDienstplan und am Client anmelden kann");
		uName.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelPwd = toolkit.createLabel(client, "Passwort: ");
		pwd = toolkit.createText(client, "");
		pwd.setEchoChar('*');
		pwd.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		final Label labelPwdRetype = toolkit.createLabel(client, "Passwort (wiederholen): ");
		pwdRetype = toolkit.createText(client, "");
		pwdRetype.setEchoChar('*');
		pwdRetype.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent me) {
				inputChanged();
			}
		});

		locked = toolkit.createButton(client, "Benutzer sperren: ", SWT.CHECK);
		locked.setToolTipText("Wenn der Benutzer gesperrt ist, kann er sich nit mehr am Client und am OnlineDienstplan anmelden");
		locked.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				inputChanged();
			}
		});

		final Label labelAuth = toolkit.createLabel(client, "Authorisierung :");
		Combo authCombo = new Combo(client, SWT.READ_ONLY);
		authorisationComboViewer = new ComboViewer(authCombo);
		authorisationComboViewer.setContentProvider(new IStructuredContentProvider() {

			@Override
			public Object[] getElements(Object arg0) {
				return Login.AUTHORIZATION;
			}

			@Override
			public void dispose() {
			}

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
			}
		});
		authorisationComboViewer.setInput(Login.AUTHORIZATION);
		authCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				inputChanged();
			}
		});

		// set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelUsername.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPwd.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelPwdRetype.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelAuth.setLayoutData(data);

		// layout for the input fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		uName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		pwd.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		pwdRetype.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		authorisationComboViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_BOTH);
		data2.horizontalSpan = 2;
		locked.setLayoutData(data2);

		return client;
	}

	@Override
	public void dataChanged(Message<Object> message, MessageIoSession messageIoSession) {
		// update the staff members
		if (message.getFirstElement() instanceof StaffMember) {
			StaffMember staffMember = (StaffMember) message.getFirstElement();
			switch (message.getMessageType()) {
				case ADD:
				case UPDATE:
					addOrUpdateStaff(staffMember);
					break;
			}
		}
		// update the logins
		if (message.getFirstElement() instanceof Login) {
			Login login = (Login) message.getFirstElement();
			switch (message.getMessageType()) {
				case ADD:
				case UPDATE:
					addOrUpdateLogin(login);
					break;
			}
		}

		// refresh the combos
		competenceComboViewer.refresh(true);
		competenceViewer.refresh(true);
		primaryLocationComboViewer.refresh(true);
	}

	/**
	 * Helper method to add or update a staff member
	 */
	private void addOrUpdateStaff(StaffMember updatedStaff) {
		if (!updatedStaff.equals(staffMember)) {
			return;
		}
		// save the updated staff member
		setInput(new StaffMemberEditorInput(updatedStaff, loginInfo, false));
		setPartName(updatedStaff.getLastName());
		staffMember = updatedStaff;
		isNew = false;
		// reset the password fields
		pwd.setText("");
		pwdRetype.setText("");
		// update the editor
		loadData();
		// show the result
		isDirty = false;
		infoLabel.setText("Änderungen gespeichert");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.ok"));
		Display.getCurrent().beep();
	}

	/**
	 * Helper method to add or update the login
	 */
	private void addOrUpdateLogin(Login updatedLogin) {
		if (!updatedLogin.equals(loginInfo)) {
			return;
		}
		// save the updated staff member
		setInput(new StaffMemberEditorInput(staffMember, updatedLogin, false));
		loginInfo = updatedLogin;
		isNew = false;
		// reset the password fields
		pwd.setText("");
		pwdRetype.setText("");
		// update the editor
		loadData();
		// show the result
		isDirty = false;
		infoLabel.setText("Änderungen gespeichert");
		infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.ok"));
		Display.getCurrent().beep();
	}

	/**
	 * Creates and returns a section and a composite with two colums
	 * 
	 * @param parent
	 *            the parent composite
	 * @param sectionName
	 *            the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent, String sectionName) {
		// create the section
		Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		// composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		// layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		return client;
	}

	/**
	 * Helper method to create a composite
	 * 
	 * @param parent
	 *            the parent control
	 * @param col
	 *            the number of cols
	 * @return the returned composite
	 */
	private Composite makeComposite(Composite parent, int col) {
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(col, false);
		nameValueComp.setLayout(layout);
		nameValueComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return nameValueComp;
	}

	/**
	 * This is called when the input of a text box or a combo box was changes
	 */
	private void inputChanged() {
		// reset the flag
		isDirty = false;

		// get the current input
		StaffMemberEditorInput staffInput = (StaffMemberEditorInput) getEditorInput();
		StaffMember persistantMember = staffInput.getStaffMember();
		Login persistantLogin = staffInput.getLoginInformation();

		// check the id
		if (!staffId.getText().equalsIgnoreCase(String.valueOf(persistantMember.getStaffMemberId()))) {
			isDirty = true;
		}
		// check the lastname
		if (!lName.getText().equalsIgnoreCase(persistantMember.getLastName())) {
			isDirty = true;
		}
		// check the firstname
		if (!fName.getText().equalsIgnoreCase(persistantMember.getFirstName())) {
			isDirty = true;
		}
		// check the phone1
		if (!phone1.getText().equalsIgnoreCase(persistantMember.getPhone1())) {
			isDirty = true;
		}
		// check the phone2
		if (!phone2.getText().equalsIgnoreCase(persistantMember.getPhone2())) {
			isDirty = true;
		}
		// check the date of birth
		if (!dateOfBirth.getText().equalsIgnoreCase(persistantMember.getBirthday())) {
			isDirty = true;
		}
		// check the username
		if (!uName.getText().equalsIgnoreCase(persistantLogin.getUsername())) {
			isDirty = true;
		}
		// check the sex
		if (!sexComboViewer.getSelection().isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) sexComboViewer.getSelection();
			String selectedSex = (String) structuredSelection.getFirstElement();
			if (selectedSex.equalsIgnoreCase(StaffMember.STAFF_MALE) & !persistantMember.isMale()) {
				isDirty = true;
			}
		}
		// check the primary location
		if (!primaryLocationComboViewer.getSelection().isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) primaryLocationComboViewer.getSelection();
			Location selectedLocation = (Location) structuredSelection.getFirstElement();
			if (!selectedLocation.equals(persistantMember.getPrimaryLocation())) {
				isDirty = true;
			}
		}
		// check the authorization
		if (!authorisationComboViewer.getSelection().isEmpty()) {
			IStructuredSelection structuredSelection = (IStructuredSelection) authorisationComboViewer.getSelection();
			String selectedAuthorization = (String) structuredSelection.getFirstElement();
			if (!selectedAuthorization.equalsIgnoreCase(persistantLogin.getAuthorization())) {
				isDirty = true;
			}
		}

		// loop and check the competences
		for (Competence comp : compList) {
			if (!persistantMember.getCompetenceList().contains(comp))
				isDirty = true;
		}
		for (Competence comp : persistantMember.getCompetenceList()) {
			if (!compList.contains(comp))
				isDirty = true;
		}

		// check for a password change
		if (!pwd.getText().trim().isEmpty() & !pwdRetype.getText().isEmpty())
			isDirty = true;

		// check the lock
		if (locked.getSelection() & !persistantLogin.isIslocked())
			isDirty = true;

		// notify the user that the input has changes
		if (isDirty) {
			infoLabel.setText("Bitte speichern Sie ihre lokalen Änderungen.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("info.warning"));
			saveHyperlink.setEnabled(true);
			saveHyperlink.setForeground(CustomColors.COLOR_BLUE);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.save"));
		}
		else {
			infoLabel.setText("Hier können sie den aktuellen Mitarbeiter verwalten und die Änderungen speichern.");
			infoLabel.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.info"));
			saveHyperlink.setEnabled(false);
			saveHyperlink.setForeground(CustomColors.GREY_COLOR);
			saveHyperlink.setImage(UiWrapper.getDefault().getImageRegistry().get("admin.saveDisabled"));
		}

		// set the dirty flag
		firePropertyChange(IWorkbenchPartConstants.PROP_DIRTY);
	}
}
