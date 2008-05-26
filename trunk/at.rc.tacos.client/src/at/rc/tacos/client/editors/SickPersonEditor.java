package at.rc.tacos.client.editors;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;

import at.rc.tacos.client.controller.EditorCloseAction;
import at.rc.tacos.client.controller.EditorDeleteAction;
import at.rc.tacos.client.controller.EditorSaveAction;
import at.rc.tacos.client.modelManager.ModelFactory;
import at.rc.tacos.client.util.CustomColors;
import at.rc.tacos.common.IKindOfTransport;
import at.rc.tacos.core.net.NetWrapper;
import at.rc.tacos.factory.ImageFactory;
import at.rc.tacos.model.SickPerson;
import at.rc.tacos.model.StaffMember;

public class SickPersonEditor extends EditorPart implements PropertyChangeListener
{
	public static final String ID = "at.rc.tacos.client.editors.sickpersonEditor";

	//properties
	boolean isDirty;
	private FormToolkit toolkit;
	private ScrolledForm form;
	
	private ImageHyperlink saveHyperlink,removeHyperlink;
	private Text lastname, firstname,street,city, svnr;
	private TextViewer notesViewer;
	private ComboViewer sexComboViewer, kindOfTransportComboViewer;

	//managed data
	private SickPerson person;
	private boolean isNew;
	
	/**
	 * Default class constructor
	 */
	public SickPersonEditor()
	{
		ModelFactory.getInstance().getSickPersonManager().addPropertyChangeListener(this);
	}
	
	/**
	 * Cleanup
	 */
	@Override
	public void dispose()
	{
		ModelFactory.getInstance().getSickPersonManager().removePropertyChangeListener(this);
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	@Override
	public void createPartControl(final Composite parent) 
	{	
		person = ((SickPersonEditorInput)getEditorInput()).getSickPerson();
		isNew = ((SickPersonEditorInput)getEditorInput()).isNew();

		//Create the form
		toolkit = new FormToolkit(CustomColors.FORM_COLOR(parent.getDisplay()));
		form = toolkit.createScrolledForm(parent);
		toolkit.decorateFormHeading(form.getForm());
		form.getBody().setLayout(new GridLayout());
		form.getBody().setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//create the content
		createManageSection(form.getBody());
		createDetailSection(form.getBody());

		//load the data
		if(!isNew)
			loadData();
		else
			form.setText("Neuen Patienten anlegen");

		//force redraw
		form.pack(true);
	}
	
	/**
	 * Creates the section to manage the changes
	 */
	private void createManageSection(Composite parent)
	{
		Composite client = createSection(parent, "Patient verwalten");

		//create info label and hyperlinks to save and revert the changes
		CLabel infoLabel = new CLabel(client,SWT.NONE);
		infoLabel.setText("Hier können sie den aktuellen Patienten verwalten und die Änderungen speichern.");
		infoLabel.setImage(ImageFactory.getInstance().getRegisteredImage("admin.info"));

		//Create the hyperlink to save the changes
		saveHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		saveHyperlink.setText("Neuen Patienten anlegen");
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

		//Create the hyperlink to remove the competence
		removeHyperlink = toolkit.createImageHyperlink(client, SWT.NONE);
		removeHyperlink.setText("Patient löschen");
		removeHyperlink.setImage(ImageFactory.getInstance().getRegisteredImage("admin.patientDelete"));
		removeHyperlink.addHyperlinkListener(new HyperlinkAdapter()
		{
			@Override
			public void linkActivated(HyperlinkEvent e) 
			{
				boolean result = MessageDialog.openConfirm(getSite().getShell(), 
						"Löschen des Patienten bestätigen", 
						"Möchten sie den Patienten " +person.getLastName() +" wirklich löschen?");
				if(!result)
					return;
				//send the remove request
				EditorDeleteAction deleteAction = new EditorDeleteAction(SickPerson.ID,person);
				deleteAction.run();
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
	}
	
	/**
	 * Creates the section containing the job details
	 * @param parent the parent composite
	 */
	private void createDetailSection(Composite parent)
	{
		Composite client = createSection(parent, "Patienten-Details");
		
		//label and the text field
		final Label labelLastname = toolkit.createLabel(client, "Nachname");
		lastname = toolkit.createText(client, "");
		final Label labelFirstname = toolkit.createLabel(client, "Vorname");
		firstname = toolkit.createText(client, "");
		
		//the sex combo
		final Label labelSex = toolkit.createLabel(client, "Geschlecht");
		Combo sexCombo = new Combo(client,SWT.READ_ONLY);
		sexComboViewer = new ComboViewer(sexCombo);
		sexComboViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return SickPerson.SICKPERSON;
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		sexComboViewer.setInput(new String[] { StaffMember.STAFF_MALE, StaffMember.STAFF_FEMALE });
		
		
		final Label labelSVNR = toolkit.createLabel(client, "Sozialversicherungsnummer");
		svnr = toolkit.createText(client, "");
		
		
		//the kind of transport combo
		final Label labelKindOfTransport = toolkit.createLabel(client, "Transportart");
		Combo kindOfTransportCombo = new Combo(client,SWT.READ_ONLY);
		kindOfTransportComboViewer = new ComboViewer(kindOfTransportCombo);
		kindOfTransportComboViewer.setContentProvider(new IStructuredContentProvider()
		{
			@Override
			public Object[] getElements(Object arg0) 
			{
				return IKindOfTransport.KINDS;
			}

			@Override
			public void dispose() { }

			@Override
			public void inputChanged(Viewer arg0, Object arg1, Object arg2) { }
		});
		kindOfTransportComboViewer.setInput(new String[] { IKindOfTransport.TRANSPORT_KIND_GEHEND, IKindOfTransport.TRANSPORT_KIND_TRAGSESSEL, IKindOfTransport.TRANSPORT_KIND_KRANKENTRAGE, IKindOfTransport.TRANSPORT_KIND_ROLLSTUHL});

		
		final Label labelStreet = toolkit.createLabel(client, "Straße");
		street = toolkit.createText(client, "");
		final Label labelCity = toolkit.createLabel(client, "Stadt");
		city = toolkit.createText(client, "");
		
		
		//the notes section
		final Label labelNotes = toolkit.createLabel(client,"Notizen zum Patienten");
		notesViewer = new TextViewer(client, SWT.BORDER | SWT.FLAT | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		notesViewer.setDocument(new Document());
		notesViewer.getControl().setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		notesViewer.setEditable(true);
		
		//set the layout for the composites
		GridData data = new GridData();
		data.widthHint = 150;
		labelLastname.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = 150;
		labelFirstname.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = 150;
		labelSex.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = 150;
		labelSVNR.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = 150;
		labelStreet.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = 150;
		labelCity.setLayoutData(data);
		
		data = new GridData();
		data.widthHint = 150;
		labelKindOfTransport.setLayoutData(data);
		data = new GridData();
		data.widthHint = 150;
		labelNotes.setLayoutData(data);

		//layout for the text fields
		GridData data2 = new GridData(GridData.FILL_HORIZONTAL);
		lastname.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		street.setLayoutData(data2);	
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		firstname.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		svnr.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		city.setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		kindOfTransportComboViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		sexComboViewer.getCombo().setLayoutData(data2);
		data2 = new GridData(GridData.FILL_HORIZONTAL);
		data2.heightHint = 100;
		notesViewer.getTextWidget().setLayoutData(data2);
	}

	/**
	 * Loads the data and shows them in the view
	 */
	private void loadData()
	{
		form.setText("Details des Patienten "+person.getLastName());
		if(!isNew)
		{
			//adjust the links
			saveHyperlink.setEnabled(true);
			saveHyperlink.setText("Änderungen speichern");
		}
		lastname.setText(person.getLastName());
		firstname.setText(person.getFirstName());
		street.setText(person.getStreetname());
		city.setText(person.getCityname());
		svnr.setText(person.getSVNR());
		if(person.getKindOfTransport() != null)
			kindOfTransportComboViewer.setSelection(new StructuredSelection(person.getKindOfTransport()));
		notesViewer.getTextWidget().setText(person.getNotes());
		if(person.isMale())
			sexComboViewer.setSelection(new StructuredSelection(SickPerson.SICKPERSON_MALE));
		else
			sexComboViewer.setSelection(new StructuredSelection(SickPerson.SICKPERSON_FEMALE));
	}

	@Override
	public void doSave(IProgressMonitor monitor) 
	{
		//reset error message
		form.setMessage(null, IMessageProvider.NONE);
		
		//save the name
		if(lastname.getText().length() > 30 || lastname.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen gültigen Nachnamen ein (max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		person.setLastName(lastname.getText());
		
		if(firstname.getText().length() > 30 || firstname.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie einen gültigen Vornamen ein (max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		person.setFirstName(firstname.getText());
		
		//save the street
		if(street.getText().length() > 30 || street.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine Straße ein", IMessageProvider.ERROR);
			return;
		}
		person.setStreetname(street.getText());
		
		//save the city
		if(city.getText().trim().isEmpty())
		{
			form.getDisplay().beep();
			form.setMessage("Bitte geben Sie eine gültige Stadt ein(max. 30 Zeichen)", IMessageProvider.ERROR);
			return;
		}
		person.setCityname(city.getText());
		
		
		//sex
		int index = sexComboViewer.getCombo().getSelectionIndex();
		if(index != -1)
		{
			String selectedSex = (String)sexComboViewer.getElementAt(index);
			if(selectedSex.equalsIgnoreCase(SickPerson.SICKPERSON_MALE))
				person.setMale(true);
			else
				person.setMale(false);
		}
		
		//save the svnr
		person.setSVNR(svnr.getText());
		
		//the notes can be empty
		person.setNotes(notesViewer.getTextWidget().getText());
		
		index = kindOfTransportComboViewer.getCombo().getSelectionIndex();
		if(index != -1)
			person.setKindOfTransport((String)kindOfTransportComboViewer.getElementAt(index));
		
		//add or update the person
		if(isNew)
			NetWrapper.getDefault().sendAddMessage(SickPerson.ID, person);
		else
			NetWrapper.getDefault().sendUpdateMessage(SickPerson.ID, person);
	}

	@Override
	public void doSaveAs() 
	{
		//not supported
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException 
	{
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	@Override
	public void setFocus() 
	{
		form.setFocus();
	}

	@Override
	public boolean isDirty() 
	{
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() 
	{
		//not supported
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) 
	{
		if("SICKPERSON_UPDATE".equals(evt.getPropertyName())
				|| "SICKPERSON_ADD".equalsIgnoreCase(evt.getPropertyName()))
		{
			SickPerson updatePerson = null;
			//get the new value
			if(evt.getNewValue() instanceof SickPerson)
				updatePerson = (SickPerson)evt.getNewValue();

			//assert we have a value
			if(updatePerson == null)
				return;

			//if this sick person is the current -> update it
			if(person.equals(updatePerson) 
					|| person.getSickPersonId() == updatePerson.getSickPersonId())
			{
				//save the updated sick person
				setInput(new SickPersonEditorInput(updatePerson,false));
				setPartName(person.getLastName());
				person = updatePerson;
				isNew = false;
				//update the editor
				loadData();
			}
		}
		if("SICKPERSON_REMOVE".equalsIgnoreCase(evt.getPropertyName()))
		{
			//get the removed sick person
			SickPerson removedPerson = (SickPerson)evt.getOldValue();
			//current open?
			if(person.equals(removedPerson))
			{
				MessageDialog.openInformation(getSite().getShell(), 
						"Patient wurde gelöscht",
						"Der Patient, welchen sie gerade bearbeiten, wurde gelöscht");
				EditorCloseAction closeAction = new EditorCloseAction(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
				closeAction.run();
			}
		}
	}
	
	//Helper methods
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
}