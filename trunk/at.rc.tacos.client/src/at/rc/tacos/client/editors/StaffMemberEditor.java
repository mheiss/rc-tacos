package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
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

import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.providers.CompetenceContentProvider;
import at.rc.tacos.client.providers.CompetenceLabelProvider;
import at.rc.tacos.client.providers.MobilePhoneContentProvider;
import at.rc.tacos.client.providers.MobilePhoneLabelProvider;
import at.rc.tacos.client.providers.StationContentProvider;
import at.rc.tacos.client.providers.StationLabelProvider;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.Competence;
import at.rc.tacos.model.Location;
import at.rc.tacos.model.Login;
import at.rc.tacos.model.MobilePhoneDetail;
import at.rc.tacos.model.StaffMember;

public class StaffMemberEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.staffMemberEditor"; 

	//the toolkit
	private FormToolkit toolkit;
	//the form holding the widgets
	private ScrolledForm form;

	//the values that can be changed
	private Text staffId,fName,lName,dateOfBirth;
	private Text uName,pwd,pwdRetype;
	private TableViewer phoneViewer,competenceViewer;
	private ComboViewer phoneComboViewer,primaryLocationComboViewer,competenceComboViewer,authorisationComboViewer,sexComboViewer;
	private Button locked;
	private Hyperlink addPhone,removePhone,removeCompetence,addCompetence;
	private ImageHyperlink saveHyperlink;

	//indicates non-saved changes
	protected boolean isDirty;

	//the staff member to manage
	private StaffMember staffMember;
	private Login loginInfo;
	private boolean isNew;

	/**
	 * Default class constructor
	 */
	public StaffMemberEditor()
	{
		ModelFactory.getInstance().getStaffManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLoginManager().addPropertyChangeListener(this);
		//keep on track when new locations or competences are added,updated or removed
		ModelFactory.getInstance().getCompetenceManager().addPropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().addPropertyChangeListener(this);
	}

	/**
	 * Clean up and remove the listener 
	 */
	@Override
	public void dispose()
	{ 
		ModelFactory.getInstance().getStaffManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLoginManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getCompetenceManager().removePropertyChangeListener(this);
		ModelFactory.getInstance().getLocationManager().removePropertyChangeListener(this);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		//save the staff member 
		staffMember = ((StaffMemberEditorInput)getEditorInput()).getStaffMember();
		loginInfo = ((StaffMemberEditorInput)getEditorInput()).getLoginInformation();
		isNew = ((StaffMemberEditorInput)getEditorInput()).isNew();

		//Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());

		Composite composite = form.getBody();
		composite.setLayout(new GridLayout(2,false));

		Composite manage = createManageSection(composite);
		createGeneralSection(composite);
		createLoginSection(composite);
		createServiceSection(composite);	

		//set the layout
		GridData data = new GridData(GridData.BEGINNING);
		data.horizontalSpan = 2;
		manage.getParent().setLayoutData(data);

		//load the data
		if(!isNew)
			loadData();
		else
			form.setText("Neuen Mitarbeiter anlegen");

		//force redraw
		form.pack(true);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details des Mitarbeiters "+staffMember.getFirstName() + " "+staffMember.getLastName());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setText("�nderungen speichern");
			//username is not editable
			uName.setEditable(false);
			uName.setBackground(CustomColors.GREY_COLOR);
			uName.setToolTipText("Der Benutzername kann nicht ver�ndert werden");
			//personal numer is not changeable
			//id is not changeable
			staffId.setEditable(false);
			staffId.setBackground(CustomColors.GREY_COLOR);
		}
		//set the data of the staff member
		staffId.setText(String.valueOf(staffMember.getStaffMemberId()));
		fName.setText(staffMember.getFirstName());
		lName.setText(staffMember.getLastName());
		if(staffMember.getBirthday() != null)
			dateOfBirth.setText(staffMember.getBirthday());
		if(staffMember.getPhonelist() != null)
			for(MobilePhoneDetail detail:staffMember.getPhonelist())
				phoneViewer.add(detail);
		if(staffMember.isMale())
			sexComboViewer.setSelection(new StructuredSelection(StaffMember.STAFF_MALE));
		else
			sexComboViewer.setSelection(new StructuredSelection(StaffMember.STAFF_FEMALE));
		primaryLocationComboViewer.setSelection(new StructuredSelection(staffMember.getPrimaryLocation()));
		//update the login
		uName.setText(loginInfo.getUsername());
		locked.setSelection(loginInfo.isIslocked());
		authorisationComboViewer.setSelection(new StructuredSelection(loginInfo.getAuthorization()));

		//update the phone and competence view
		phoneViewer.refresh(true);
		competenceViewer.refresh(true);
	}

	/**
	 * Passes the focus to the view
	 */
	@Override
	public void setFocus() 
	{  
		form.setFocus();
	}

	@Override
	public void doSave(IProgressMonitor monitor) 
	{
		//reset error message
		form.setMessage(null, IMessageProvider.NONE);

		//validate the staff id
		String pattern = "5\\d{7}";
		if(!staffId.getText().matches(pattern))
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine g�ltige Personalnummer in der Form 5xxxxxxx ein.", IMessageProvider.ERROR);
			return;
		}
		staffMember.setStaffMemberId(Integer.parseInt(staffId.getText()));

		//save the input in the staff member
		if(fName.getText().length() >30 || fName.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen g�ltigen Vornamen ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		staffMember.setFirstName(fName.getText());

		//Set the lastname
		if(lName.getText().length() >30 || lName.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen g�ltigen Nachnamen ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		staffMember.setLastName(lName.getText());

		//date of birth
		String patternDate = "\\d{2}\\-\\d{2}-\\d{4}";
		if(!dateOfBirth.getText().trim().isEmpty() &! dateOfBirth.getText().matches(patternDate))
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben sie ein Geburtsdatum in der Form dd-mm-yyyy ein", IMessageProvider.ERROR);
			return;
		}
		if(!dateOfBirth.getText().trim().isEmpty())
			staffMember.setBirthday(dateOfBirth.getText());
		//sex
		int index = sexComboViewer.getCombo().getSelectionIndex();
		if(index != -1)
		{
			String selectedSex = (String)sexComboViewer.getElementAt(index);
			if(selectedSex.equalsIgnoreCase(StaffMember.STAFF_MALE))
				staffMember.setMale(true);
			else
				staffMember.setMale(false);
		}
		
		

		//the location
		index = primaryLocationComboViewer.getCombo().getSelectionIndex();
		if(index == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte w�hlen sie die prim�re Dienstelle des Mitarbeiters aus", IMessageProvider.ERROR);
			return;
		}
		staffMember.setPrimaryLocation((Location)primaryLocationComboViewer.getElementAt(index));

		//phones are already assigned, so we dont't need to assign them :)
		//Competences are already assigned, so we dont' have to assign them :)

		//check the username
		if(uName.getText().length() >30 || uName.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen g�ltigen Benutzernamen ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		loginInfo.setUsername(uName.getText());
		staffMember.setUserName(uName.getText());

		//a new entry must have a password
		if(isNew & (pwd.getText().trim().isEmpty() || pwdRetype.getText().trim().isEmpty()))
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie ein Passwort ein.", IMessageProvider.ERROR);
			return;
		}

		//the password
		if(!pwd.getText().trim().isEmpty() |! pwdRetype.getText().trim().isEmpty())
		{
			//check if they are equal
			if(pwd.getText().equals(pwdRetype.getText()))
				loginInfo.setPassword(pwd.getText());
			else
			{
				form.getDisplay().beep();
				form.setMessage("Die eingegebenen Passw�rter stimmen nicht �berein", IMessageProvider.ERROR);
				return;
			}
		}

		//authorization
		index = authorisationComboViewer.getCombo().getSelectionIndex();
		if(index == -1)
		{
			form.getDisplay().beep();
			form.setMessage("Bitte w�hlen sie die Berechtigungen des Benutzers aus", IMessageProvider.ERROR);
			return;
		}
		loginInfo.setAuthorization((String)authorisationComboViewer.getElementAt(index));

		//locked
		loginInfo.setIslocked(locked.getSelection());

		//add or update the staff member and the login
		if(isNew)
		{
			NetWrapper.getDefault().sendAddMessage(Login.ID, loginInfo);
			NetWrapper.getDefault().sendAddMessage(StaffMember.ID, staffMember);
		}
		else
		{
			NetWrapper.getDefault().sendUpdateMessage(StaffMember.ID, staffMember);
			NetWrapper.getDefault().sendUpdateMessage(Login.ID, loginInfo);
		}
	}

	@Override
	public boolean isSaveAsAllowed() 
	{
		return false;
	}

	@Override
	public void doSaveAs() 
	{
		// don't support saving as
	}

	@Override
	public boolean isDirty() 
	{
		return isDirty;
	}

	/**
	 * Creates the section to manage the changes
	 */
	public Composite createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Mitarbeiter verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier k�nnen sie den aktuellen Mitarbeiter verwalten und die �nderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neuen Mitarbeiter speichern");
		saveHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.save"));
		saveHyperlink.addHyperlinkListener(new HyperlinkAdapter() 
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				EditorSaveAction saveAction = new EditorSaveAction();
				saveAction.run();
			}
		});

		//info label should span over two
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		infoLabel.setLayoutData(data);
		//save hyperlink should span over two
		data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		saveHyperlink.setLayoutData(data);

		return client;
	}

	/**
	 * Creates the general section
	 */
	private Composite createGeneralSection(Composite parent)
	{
		Composite client = createSection(parent, "Allgemeine Daten");

		//create the label and the input field
		final Label labelStaffId = toolkit.createLabel(client,"Personalnummer");
		staffId = toolkit.createText(client, "");

		final Label labelFName = toolkit.createLabel(client, "Vorname");
		fName = toolkit.createText(client, "");

		final Label labelLName = toolkit.createLabel(client, "Nachname");
		lName = toolkit.createText(client, "");

		final Label labelDateOfBirth = toolkit.createLabel(client, "Geburtsdatum");
		dateOfBirth = toolkit.createText(client, "");

		final Label labelSex = toolkit.createLabel(client, "Geschlecht");
		Combo sexCombo = new Combo(client,SWT.READ_ONLY);
		sexComboViewer = new ComboViewer(sexCombo);
		sexComboViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return StaffMember.STAFF;
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		sexComboViewer.setInput(new String[] { StaffMember.STAFF_MALE, StaffMember.STAFF_FEMALE });

		//phone list 
		final Label labelPhone = toolkit.createLabel(client, "Telefonnummern",SWT.LEFT | SWT.TOP);

		//make a subcomposite holding the Hyperlinks and the viewer
		Composite phoneSub = makeComposite(client, 3);

		//create the viewer
		Combo comboPhone = new Combo(phoneSub, SWT.READ_ONLY);
		phoneComboViewer = new ComboViewer(comboPhone);
		phoneComboViewer.setContentProvider(new MobilePhoneContentProvider());
		phoneComboViewer.setLabelProvider(new MobilePhoneLabelProvider());
		phoneComboViewer.setInput(ModelFactory.getInstance().getPhoneManager().getMobilePhoneList());

		addPhone = toolkit.createHyperlink(phoneSub, "Nummer hinzuf�gen", SWT.NONE);
		addPhone.setToolTipText("Mit diesem Link kann dem Mitarbeiter eine Telefonnummer zugewiesen werden");
		addPhone.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				form.setMessage(null, IMessageProvider.NONE);
				//get the selected phone
				ISelection selection = phoneComboViewer.getSelection();
				
				//get the selected phone
				StructuredSelection structuredSelection = (StructuredSelection)selection;
				MobilePhoneDetail phone = (MobilePhoneDetail)structuredSelection.getFirstElement();
				//check if this member has already the phone
				if(staffMember.getPhonelist().contains(phone))
				{
					form.getShell().getDisplay().beep();
					form.setMessage("Dem Mitarbeiter wurde diese Telefonnummer bereits zugewiesen.", IMessageProvider.ERROR);
					return;
				}
				staffMember.getPhonelist().add(phone);
				phoneViewer.refresh();
			}
		});
		//hyperlink to remove a phone
		removePhone = toolkit.createHyperlink(phoneSub, "Nummer entfernen", SWT.LEFT);
		removePhone.setToolTipText("Mit diesem Link wird die selektierte Telefonnummern entfernt");
		removePhone.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e)
			{
				form.setMessage(null, IMessageProvider.NONE);
				//get the selected element
				ISelection selection = phoneViewer.getSelection();
				if(!selection.isEmpty())
				{
					//get the selected object
					Object selectedPhone = ((IStructuredSelection)selection).getFirstElement();
					phoneViewer.remove(selectedPhone);
					staffMember.getPhonelist().remove(selectedPhone);
				}
				else
				{
					form.getShell().getDisplay().beep();
					form.setMessage("Bitte w�hlen Sie eine Telefonnummer aus die entfernt werden soll", IMessageProvider.ERROR);
				}
			}
		});	

		//the viewer, should span over two columns
		phoneViewer = new TableViewer(phoneSub,SWT.BORDER);
		phoneViewer.setLabelProvider(new MobilePhoneLabelProvider());
		//set this staff members phone list as content provider
		phoneViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return staffMember.getPhonelist().toArray();
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		phoneViewer.setInput(staffMember.getPhonelist().toArray());	

		//set the layout for the composites
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
		labelDateOfBirth.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelSex.setLayoutData(data);
		data = new GridData(GridData.BEGINNING);
		data.widthHint = 150;
		data.horizontalAlignment = GridData.HORIZONTAL_ALIGN_BEGINNING;
		data.verticalAlignment = GridData.VERTICAL_ALIGN_BEGINNING;
		labelPhone.setLayoutData(data);
		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		staffId.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		fName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		lName.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		dateOfBirth.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		sexComboViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_BOTH);
		data2.horizontalSpan = 2;
		data2.heightHint = 50;
		phoneViewer.getTable().setLayoutData(data2);

		return client;
	}

	/**
	 * Creates the service section
	 */
	private Composite createServiceSection(Composite parent)
	{
		Composite client = createSection(parent, "Ausbildung und Dienststelle");

		//create the label and the dropdown field
		final Label locationLabel = toolkit.createLabel(client, "Dienststelle");

		Combo stationCombo = new Combo(client, SWT.READ_ONLY);
		primaryLocationComboViewer = new ComboViewer(stationCombo);
		primaryLocationComboViewer.setContentProvider(new StationContentProvider());
		primaryLocationComboViewer.setLabelProvider(new StationLabelProvider());
		primaryLocationComboViewer.setInput(ModelFactory.getInstance().getLocationManager());

		//phone list 
		final Label labelCompetence = toolkit.createLabel(client, "Verf�gbare Kompetenzen: ");

		//make a subcomposite holding the Hyperlinks and the viewer
		Composite compManage = makeComposite(client, 3);

		Combo comboVerw = new Combo(compManage, SWT.READ_ONLY);
		competenceComboViewer = new ComboViewer(comboVerw);
		competenceComboViewer.setContentProvider(new CompetenceContentProvider());
		competenceComboViewer.setLabelProvider(new CompetenceLabelProvider());
		competenceComboViewer.setInput(ModelFactory.getInstance().getCompetenceManager().toArray());

		addCompetence = toolkit.createHyperlink(compManage, "hinzuf�gen", SWT.NONE);
		addCompetence.setToolTipText("Die gew�hlte Kompetenz dem Mitarbeiter zuweisen.");
		addCompetence.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				form.setMessage(null, IMessageProvider.NONE);
				//get the selected item
				int index = competenceComboViewer.getCombo().getSelectionIndex();
				Competence comp = (Competence)competenceComboViewer.getElementAt(index);
				if(staffMember.getCompetenceList().contains(comp))
				{
					form.getShell().getDisplay().beep();
					form.setMessage("Dem Mitarbeiter wurde diese Kompetenz bereits zugewiesen.",IMessageProvider.ERROR);
				}
				else
				{
					staffMember.getCompetenceList().add(comp);
					competenceViewer.refresh();
				}
			}
		});
		//hyperlink to remove a competence
		removeCompetence = toolkit.createHyperlink(compManage, "entfernen", SWT.NONE);
		removeCompetence.setToolTipText("Die selektierte Kompetenz dem Mitarbeiter wieder entziehen.");
		removeCompetence.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e)
			{
				form.setMessage(null, IMessageProvider.NONE);
				//get the selected element
				ISelection selection = competenceViewer.getSelection();
				if(!selection.isEmpty())
				{
					//get the selected object
					Object selectedCompetence = ((IStructuredSelection)selection).getFirstElement();
					competenceViewer.remove(selectedCompetence);
					staffMember.getCompetenceList().remove(selectedCompetence);
				}
				else
				{
					form.getShell().getDisplay().beep();
					form.setMessage("Bitte w�hlen sie eine Kompetenz aus die entfernt werden soll", IMessageProvider.ERROR);
				}
			}
		});

		final Label labelManage = toolkit.createLabel(client, "Kompetenzen des Mitarbeiters: ");
		//create the table holding the competences
		competenceViewer = new TableViewer(client,SWT.BORDER);
		competenceViewer.setLabelProvider(new CompetenceLabelProvider());
		//set this staff members competences as content provider
		competenceViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return staffMember.getCompetenceList().toArray();
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		competenceViewer.setInput(staffMember.getCompetenceList().toArray());

		//layout the composites
		GridData data = new GridData();
		data.widthHint = 150;
		locationLabel.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelCompetence.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelManage.setLayoutData(data);

		//layout for the input fields
		GridData data2 = new GridData(GridData.FILL_BOTH | GridData.BEGINNING);
		primaryLocationComboViewer.getCombo().setLayoutData(data2);

		//Layout the combo
		data2 = new GridData(GridData.FILL_BOTH | GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING);
		competenceComboViewer.getCombo().setLayoutData(data2);

		//layout the table
		competenceViewer.getTable().setLayout(new GridLayout());
		data2 = new GridData(GridData.FILL_BOTH | GridData.BEGINNING);
		data2.heightHint = 50;
		competenceViewer.getTable().setLayoutData(data2);

		return client;
	}

	/**
	 * Creates the login section
	 */
	private Composite createLoginSection(Composite parent)
	{
		Composite client = createSection(parent, "Daten zum anmelden am System und Online-Dienstplan");

		//create the label and the input field
		final Label labelUsername = toolkit.createLabel(client, "Username: ");
		uName = toolkit.createText(client, "");
		uName.setToolTipText("Der Benutzername mit dem sich der Mitarbeiter am OnlineDienstplan und am Client anmelden kann");

		final Label labelPwd = toolkit.createLabel(client, "Passwort: ");
		pwd = toolkit.createText(client, "");
		pwd.setEchoChar('*');

		final Label labelPwdRetype= toolkit.createLabel(client, "Passwort (wiederholen): ");
		pwdRetype = toolkit.createText(client, "");
		pwdRetype.setEchoChar('*');

		locked = toolkit.createButton(client, "Benutzer sperren: ", SWT.CHECK);
		locked.setToolTipText("Wenn der Benutzer gesperrt ist, kann er sich nit mehr am Client und am OnlineDienstplan anmelden");

		final Label labelAuth = toolkit.createLabel(client, "Authorisierung :");
		Combo authCombo = new Combo(client,SWT.READ_ONLY);
		authorisationComboViewer = new ComboViewer(authCombo);
		authorisationComboViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return Login.AUTHORIZATION;
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { } 		
		});
		authorisationComboViewer.setInput(Login.AUTHORIZATION);

		//set the layout for the composites
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

		//layout for the input fields
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
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if("STAFF_UPDATE".equals(evt.getPropertyName()) 
				|| "LOGIN_UPDATE".equals(evt.getPropertyName())
				|| "STAFF_ADD".equals(evt.getPropertyName())
				|| "LOGIN_ADD".equals(evt.getPropertyName()))
		{
			StaffMember updateMember = null;
			Login updateLogin = null;

			//get the new value
			if(evt.getNewValue() instanceof StaffMember)
			{
				updateMember = (StaffMember)evt.getNewValue();
				updateLogin = ModelFactory.getInstance().getLoginManager().getLoginByUsername(updateMember.getUserName());
			}
			if(evt.getNewValue() instanceof Login)
			{
				updateLogin = (Login)evt.getNewValue();
				updateMember = ModelFactory.getInstance().getStaffManager().getStaffMemberByUsername(updateLogin.getUsername());
			}
			//assert we have both values
			if(updateLogin == null || updateMember == null)
				return;

			//is this staff member the current edited member
			if(updateMember.equals(staffMember))
			{
				//save the updated staff member
				setInput(new StaffMemberEditorInput(updateMember,updateLogin,false));
				setPartName(updateMember.getLastName());
				staffMember = updateMember;
				loginInfo = updateLogin;
				isNew = false;
				//update the editor
				loadData();
			}
		}
		//refresh the comboview when locations are added,updated or removed
		if("LOCATION_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "LOCATION_REMOVE".equalsIgnoreCase(evt.getPropertyName())
				|| "LOCATION_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//just refresh the combo so that the new data is loaded
			primaryLocationComboViewer.refresh(true);
		}
		//refresh the comboview when competences are added,updated or removed
		if("COMPETENCE_ADD".equalsIgnoreCase(evt.getPropertyName())
				|| "COMPETENCE_REMOVE".equalsIgnoreCase(evt.getPropertyName())
				|| "COMPETENCE_UPDATE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//just refresh the combo so that the new data is loaded
			competenceComboViewer.refresh(true);
			competenceViewer.refresh(true);
		}
	}

	/**
	 * Creates and returns a section and a composite with two colums
	 * @param parent the parent composite
	 * @param sectionName the title of the section
	 * @return the created composite to hold the other widgets
	 */
	private Composite createSection(Composite parent,String sectionName)
	{
		//create the section
		Section section = toolkit.createSection(parent,ExpandableComposite.TITLE_BAR | ExpandableComposite.TWISTIE);
		toolkit.createCompositeSeparator(section);
		section.setText(sectionName);
		section.setLayout(new GridLayout());
		section.setLayoutData(new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING));
		section.setExpanded(true);
		//composite to add the client area
		Composite client = new Composite(section, SWT.NONE);
		section.setClient(client);

		//layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = false;
		client.setLayout(layout);
		GridData clientDataLayout = new GridData(GridData.BEGINNING | GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_BOTH);
		client.setLayoutData(clientDataLayout);

		return client;
	}

	/**
	 * Helper method to create a composite
	 * @param parent the parent control
	 * @param col the number of cols
	 * @return the returned composite
	 */
	private Composite makeComposite(Composite parent, int col) 
	{
		Composite nameValueComp = toolkit.createComposite(parent);
		GridLayout layout = new GridLayout(col, false);
		nameValueComp.setLayout(layout);
		nameValueComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return nameValueComp;
	}
}
